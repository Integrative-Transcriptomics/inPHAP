package viewmodel;

import filtering.filter.Filter;
import gui.HVMainPanel;

import java.awt.Window;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import settings.SNPMapSetting;
import sorting.IColumnSorter;
import sorting.IRowSorter;
import tasks.AbstractTask;
import visualization.NucVisObject;
import visualization.SNPMap;
import aggregation.Aggregation;
import aggregation.IMatrixAggregator;
import aggregation.IMetaAggregator;
import dataStorage.DataSet;
import dataStorage.IMetaData;
import dataStorage.SNP;
import dataStorage.SNPMetaData;
import events.EventFirer;
import events.ViewModelEvent;

public class ViewModel {
	private DataSet dataSet;
	private HVMainPanel mainPanel;
	private DataSetManipulator manipulator;
	
	private SNPMapSetting snpMapSettings;
	
	private Window owner;
	private SNPMap visComponent;
	
	/*
	 * EventFirer simplifies handling events to react on user input from the GUI
	 */
	protected EventFirer<ViewModelEvent, ViewModelListener> eventfirer = new EventFirer<ViewModelEvent, ViewModelListener>() {
		protected void dispatchEvent(ViewModelEvent event, ViewModelListener listener) {
			listener.viewModelChanged(event);
		}		
	};
	
	public ViewModel(Window owner) {
		this.snpMapSettings = new SNPMapSetting(this);
		this.manipulator = new DataSetManipulator(this);
		this.owner = owner;
	}
	
	public DataSet getDataSet() {
		return this.dataSet;
	}
	
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		this.dataSet.setViewModel(this);
		this.manipulator.setDataSet(dataSet);
		System.gc();
	}
	
	public void setMainPanel(HVMainPanel panel) {
		this.mainPanel = panel;
	}
	
	public void addViewModelListener(ViewModelListener vml) {
		eventfirer.addListener(vml);
	}
	
	public void removeViewModelListener(ViewModelListener vml) {
		eventfirer.removeListener(vml);
	}
	
	public void fireChanged(ViewModelEvent e) {
		eventfirer.fireEvent(e);
	}
	
	public void showLog() {
		if(!mainPanel.logWindow.isVisible()) {
			mainPanel.logWindow.showWindow();
		}
	}

	public int numColsInVis() {
		return this.manipulator.numColsInVis();
	}
	
	public int numRowsInVis() {
		return this.manipulator.numRowsInVis();
	}

	public String getColumnID(int columnInVis) {
		return this.manipulator.getColumnID(columnInVis);
	}

	public String getRowID(int rowInVis) {
		return this.manipulator.getRowID(rowInVis);
	}
	
	public void selectRow(int rowInVis) {
		this.manipulator.selectRow(rowInVis);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.ROW_SELECTION_CHANGED, "Row selection changed ..."));
	}
	
	public void selectColumn(int columnInVis) {
		this.manipulator.selectColumn(columnInVis);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.COLUMN_SELECTION_CHANGED, "Column selection changed ..."));
	}
	
	public boolean isRowSelected(int rowInVis) {
		return this.manipulator.isRowSelected(rowInVis);
	}
	
	public boolean isColumnSelected(int columnInVis) {
		return this.manipulator.isColumnSelected(columnInVis);
	}

	public IMetaData getMetaColumn(int columnInVis) {
		return this.manipulator.getMetaColumn(columnInVis);
	}
	
	public SNPMetaData getMetaRow(int rowInVis) {
		return this.manipulator.getMetaRow(rowInVis);
	}

	public int numMetaCols() {
		return this.manipulator.numMetaCols();
	}
	
	public int numMetaRows() {
		return this.manipulator.numMetaRows();
	}

	public Double getMetaRowValue(int column, int row) {
		return this.manipulator.getMetaRowValue(column, row);
	}

	public SNPMapSetting getSNPMapSetting() {
		return this.snpMapSettings;
	}

	public String getMetaColumnID(int metaColumnInVis) {
		return this.manipulator.getMetaColumnID(metaColumnInVis);
	}

	public NucVisObject getSNVInColumn(int columnInVis, int rowInVis) {
		if(dataSet != null) {
			return this.manipulator.getSNVInColumn(columnInVis, rowInVis);
		}
		return null;
	}

	public char getReferenceInColumn(int columnInVis) {
		if(dataSet != null) {
			int newIndex = this.manipulator.getColumnInDataSet(columnInVis);
			char ref = dataSet.getSNPs().get(newIndex).getReference();
			return ref;
		}
		return 0;
	}

	public Double getMetaColumnValue(int columnInVis, int rowInVis) {
		return this.manipulator.getMetaColumnValue(columnInVis, rowInVis);
	}
	
	public String getMetaColumnText(int columnInVis, int rowInVis) {
		return this.manipulator.getMetaColumnText(columnInVis, rowInVis);
	}

	public String getMetaRowID(int metaRowInVis) {
		return this.manipulator.getMetaRowID(metaRowInVis);
	}
	
	public void sortRows(final IRowSorter sorter) {
		AbstractTask task = new AbstractTask("Sorting Subjects") {
			@Override
			protected void initialize() {}
			
			@Override
			protected void doWork() throws Exception {
				manipulator.sortRows(sorter);
				fireChanged(new ViewModelEvent(this, ViewModelEvent.ROW_SORTING_CHANGED, "Row sorting changed"));
			}
		};
		task.start();
	}
	
	public void sortColumns(final IColumnSorter sorter) {
		AbstractTask task = new AbstractTask("Sorting SNVs") {
			@Override
			protected void initialize() {}
			
			@Override
			protected void doWork() throws Exception {
				manipulator.sortColumns(sorter);
				fireChanged(new ViewModelEvent(this, ViewModelEvent.COLUMN_SORTING_CHANGED, "Column sorting changed"));		
			}
		};
		task.start();
	}

	public Collection<Integer> getSelectedColumns() {
		return this.manipulator.getSelectedColumns();
	}

	public Integer getColumnInDataSet(int columnInPlot) {
		return this.manipulator.getColumnInDataSet(columnInPlot);
	}

	public void setColumnSelection(Set<Integer> newSelection) {
		this.manipulator.setColumnSelection(newSelection);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.COLUMN_SELECTION_CHANGED, "Column selection changed"));
	}

	public void toggleColumnSelected(int columnInVis) {
		this.manipulator.toggleColumnSelection(columnInVis);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.COLUMN_SELECTION_CHANGED, "Column selection changed ..."));
	}

	public Collection<Integer> getRowsInDataSet(int rowInVis) {
		return this.manipulator.getRowsInDataSet(rowInVis);
	}

	public void setRowSelection(Set<Integer> newSelection) {
		this.manipulator.setRowSelection(newSelection);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.ROW_SELECTION_CHANGED, "Row selection changed"));
	}

	public Collection<Integer> getSelectedRows() {
		return this.manipulator.getSelectedRows();
	}

	public void toggleRowSelected(int rowInVis) {
		this.manipulator.toggleRowSelection(rowInVis);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.ROW_SELECTION_CHANGED, "Row selection changed"));
	}

	public void clearColumnSelection() {
		this.manipulator.clearColumnSelection();
		fireChanged(new ViewModelEvent(this, ViewModelEvent.COLUMN_SELECTION_CHANGED, "Cleared Column Selection"));
	}

	public void clearRowSelection() {
		this.manipulator.clearRowSelection();
		fireChanged(new ViewModelEvent(this, ViewModelEvent.ROW_SELECTION_CHANGED, "Cleared Row Selection"));
	}
	
	public void aggregateRowsByMetaInfo(IMatrixAggregator matrixAggregator, IMetaAggregator metaAggregator, int metaColumn) {
		this.manipulator.aggregateRowsByMetaInfo(matrixAggregator, metaAggregator, metaColumn);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.AGGREGATION_CHANGED, "Rows have been aggregated according to meta-information"));
	}

	public void aggregateSelectedRows(IMatrixAggregator matrixAggregator, IMetaAggregator metaAggregator) {
		this.manipulator.aggregateSelectedRows(matrixAggregator, metaAggregator);
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.AGGREGATION_CHANGED, "The selected rows have been aggregated"));
	}

	public void deaggregateSelectedRows() {
		this.manipulator.deaggregateSelectedRows();
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.AGGREGATION_CHANGED, "The selected rows have been de-aggregated"));
	}

	public Window getOwner() {
		return owner;
	}

	public int getColumnInVis(int columnInDataSet) {
		return this.manipulator.getColumnInVis(columnInDataSet);
	}
	
	public int getRowInVis(Integer rowInDataset) {
		return this.manipulator.getRowInVis(rowInDataset);
	}

	public void jumpToColumn(Integer columnInVis) {
		this.fireChanged(new ViewModelEvent(columnInVis, ViewModelEvent.COLUMN_JUMP_EVENT, "Jumping to SNV in column " + Integer.toString(columnInVis)));
	}
	
	public void jumpToRow(Integer rowInVis) {
		this.fireChanged(new ViewModelEvent(rowInVis, ViewModelEvent.ROW_JUMP_EVENT, "Jumping to subject in row " + Integer.toString(rowInVis)));
	}

	public void writeLogMessage(String message) {
		this.fireChanged(new ViewModelEvent(this, ViewModelEvent.LOG_UPDATE, message));
	}

	public int numColsInDataSet() {
		return this.manipulator.numColsInDataSet();
	}

	public SNPMap getVisComponent() {
		return this.visComponent;
	}

	public void setVisComponent(SNPMap component) {
		this.visComponent = component;
	}

	public int getRowInVis(Aggregation a) {
		return this.manipulator.getRowInVis(a);
	}

	public NucVisObject getSNVInColumnUnphased(int columnInVis, int rowInVis) {
		if(dataSet != null) {
			return this.manipulator.getSNVInColumnUnphased(columnInVis, rowInVis);
		}
		return null;
	}

	public List<SNP> getFilteredSNPs() {
		return this.manipulator.getFilteredSNPs();
	}

	public void filterSNVs(Filter f) {
		this.manipulator.filterSNVs(f);
		fireChanged(new ViewModelEvent(this, ViewModelEvent.SNPS_FILTERED, "SNVs have been filter according to " + f.getName()));
	}
	
	public void removeFilters() {
		this.manipulator.clearFiltering();
		fireChanged(new ViewModelEvent(this, ViewModelEvent.SNPS_FILTERED, "All SNV filters have been removed."));
	}
}
