package viewmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import sorting.IColumnSorter;
import sorting.IRowSorter;
import visualization.NucVisObject;
import aggregation.Aggregation;
import aggregation.AggregationContainer;
import aggregation.AggregationMatrixRow;
import aggregation.AggregationMetaRow;
import aggregation.IMatrixAggregator;
import aggregation.IMetaAggregator;
import dataStorage.DataSet;
import dataStorage.IMetaData;
import dataStorage.SNP;
import dataStorage.SNPMetaData;
import events.ViewModelEvent;
import filtering.filter.Filter;

public class DataSetManipulator implements ViewModelListener {

	private ViewModel viewModel;
	
	private Set<Integer> selectedRows = new TreeSet<Integer>();
	private Set<Integer> selectedColumns = new TreeSet<Integer>();
	
	private PhasedIndexMapper indexMapper;
	private AggregationContainer aggregationContainer;
	private DataSet dataSet;
	
	private List<SNP> filteredSNPs = new ArrayList<SNP>();
	
	public DataSetManipulator(ViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		this.indexMapper = new PhasedIndexMapper(viewModel);
	}
	
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		filteredSNPs = new ArrayList<SNP>(dataSet.getSNPs());
		this.aggregationContainer = new AggregationContainer(viewModel);
		this.indexMapper.setAggregationContainer(this.aggregationContainer);
		this.indexMapper.setDataSet(dataSet);
	}
	
	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		case ViewModelEvent.DATASET_CHANGED:
			this.selectedColumns.clear();
			this.selectedRows.clear();
			this.dataSet = viewModel.getDataSet();
			break;
		}
	}

	public String getColumnID(int columnInVis) {
		if(dataSet != null) {
			int newColumn = this.indexMapper.getColumnInDataSet(columnInVis);
			SNP snp = viewModel.getDataSet().getSNPs().get(newColumn);
			return snp.getRsid();
		}
		return null;
	}

	public String getRowID(int rowInVis) {
		if(dataSet != null) {
			Aggregation a = this.aggregationContainer.get(rowInVis);
			return a.getID();
		}
		return null;
	}
	
	public Collection<Integer> getRowsInDataSet(int rowInVis) {
		Aggregation a = this.aggregationContainer.get(rowInVis);
		List<Integer> indicesInDataSet = new ArrayList<Integer>();
		
		if(a.isElemental()) {
			indicesInDataSet.add(a.getIndex());
		} else {
			List<Aggregation> elemets = a.getElements();
			for(Aggregation a2 : elemets) {
				indicesInDataSet.add(a2.getIndex());
			}
		}
		
		return indicesInDataSet;
	}

	public boolean isRowSelected(int rowInVis) {
		Collection<Integer> newIndices = this.getRowsInDataSet(rowInVis);
		if(selectedRows.containsAll(newIndices)) {
			return true;
		}
		return false;
	}

	public boolean isColumnSelected(int columnInVis) {
		int newIndex = this.getColumnInDataSet(columnInVis);
		if(selectedColumns.contains(newIndex))
			return true;
		return false;
	}

	public IMetaData getMetaColumn(int column) {
		if(dataSet != null)
			return this.dataSet.getMetaColumn(column);
		return null;
	}

	//meta information in the meta-information row panel
	public SNPMetaData getMetaRow(int rowInVis) {
		if(dataSet != null) {
			return this.dataSet.getMetaRow(rowInVis);
		}
		return null;
	}

	public int numMetaCols() {
		//since there is no sorting and aggregation of meta columns
		//we can retrieve this information directly from the dataset
		if(this.dataSet != null)
			return dataSet.getNumMetaCols();
		return 0;
	}

	public int numMetaRows() {
		if(dataSet != null)
			return this.dataSet.getNumMetaRows();
		return 0;
	}

	public Double getMetaRowValue(int columnInVis, int rowInVis) {
		Integer columnInDataSet = this.indexMapper.getColumnInDataSet(columnInVis);
		Integer snpIndex = this.viewModel.getDataSet().getSNPs().get(columnInDataSet).getIndex();
		SNPMetaData metaRow = this.getMetaRow(rowInVis);
		
		if(viewModel.getSNPMapSetting().isPhased()) {
			boolean paternal = this.indexMapper.isPaternalColumnInPlot(columnInVis);
			Double value = metaRow.getMappedValue(snpIndex, paternal);
			return value;
		} else {
			//return mean for unphased data
			Double value1 = metaRow.getMappedValue(snpIndex, true);
			Double value2 = metaRow.getMappedValue(snpIndex, false);
			if(value1 == null && value2 != null)
				return value2;
			if(value2 == null && value1 != null)
				return value1;
			if(value1 == null && value2 == null)
				return null;
			return (value1 + value2) / 2;
		}
	}

	public String getMetaColumnID(int metaColumn) {
		if(dataSet != null)
			return dataSet.getMetaColHeader(metaColumn);
		return "";
	}

	public NucVisObject getSNVInColumn(int columnInVis, int rowInVis) {
		Aggregation a = this.aggregationContainer.get(rowInVis);
		Integer columnInDataSet = this.indexMapper.getColumnInDataSet(columnInVis);
		Integer snpIndex = this.viewModel.getDataSet().getSNPs().get(columnInDataSet).getIndex();
		boolean paternal = this.indexMapper.isPaternalColumnInPlot(columnInVis);
		int patIndex = paternal == true ? 0 : 1;
		if(a.isElemental()) {
			char c =  dataSet.getMatrix().get(snpIndex, a.getIndex())[patIndex];
			return new NucVisObject(c, 1.0);
		} else {
			AggregationMatrixRow matrixRow = this.aggregationContainer.getAggregationMatrixRow(a);
			double f = matrixRow.getFrequency(snpIndex)[patIndex];
			char c = matrixRow.getNucleotide(snpIndex)[patIndex];
			return new NucVisObject(c, f);
		}
	}
	
	public NucVisObject getSNVInColumnUnphased(int columnInVis, int rowInVis) {
		Aggregation a = this.aggregationContainer.get(rowInVis);
		Integer columnInDataSet = this.indexMapper.getColumnInDataSet(columnInVis);
		Integer snpIndex = this.viewModel.getDataSet().getSNPs().get(columnInDataSet).getIndex();

		if(a.isElemental()) {
			char c1 =  dataSet.getMatrix().get(snpIndex, a.getIndex())[0];
			char c2 =  dataSet.getMatrix().get(snpIndex, a.getIndex())[1];
			return new NucVisObject(c1, c2, 1.0, 1.0);
		} else {
			AggregationMatrixRow matrixRow = this.aggregationContainer.getAggregationMatrixRow(a);
			double f1 = matrixRow.getFrequency(snpIndex)[0];
			double f2 = matrixRow.getFrequency(snpIndex)[1];
			char c1 = matrixRow.getNucleotide(snpIndex)[0];
			char c2 = matrixRow.getNucleotide(snpIndex)[1];
			return new NucVisObject(c1, c2, f1, f2);
		}
	}

	public int getColumnInDataSet(int columnInVis) {
		if(dataSet != null) {
			return this.indexMapper.getColumnInDataSet(columnInVis);
		}
		return 0;
	}

	public int numColsInVis() {
		if(dataSet != null) {
			if(viewModel.getSNPMapSetting().isPhased())
				return this.filteredSNPs.size() * 2;
			else
				return this.filteredSNPs.size();
		}
		return 0;
	}
	
	public int numColsInDataSet() {
		if(dataSet != null) {
			return dataSet.getNumSNVs();
		}
		return 0;
	}

	public int numRowsInVis() {
		if(dataSet != null) {
			return aggregationContainer.size();
		}
		return 0;
	}

	public void selectRow(int rowInVis) {
		Collection<Integer> rowsInDataSet = this.getRowsInDataSet(rowInVis);
		if(!selectedRows.containsAll(rowsInDataSet)) {
			selectedRows.addAll(rowsInDataSet);
		}
	}

	public void selectColumn(int columnInVis) {
		int columnInDataSet = this.getColumnInDataSet(columnInVis);
		if(!isColumnSelected(columnInVis)) {
			this.selectedColumns.add(columnInDataSet);
		}
	}

	public Double getMetaColumnValue(int columnInVis, int rowInVis) {
		if(dataSet != null) {
			Aggregation a = this.aggregationContainer.get(rowInVis);
			if(a.isElemental()) {
				Integer indexInDataSet = a.getIndex();
				Double value = dataSet.getMetaColumn(columnInVis).getMappedValue(indexInDataSet.intValue());
				return value;
			} else {
				AggregationMetaRow metaRow = this.aggregationContainer.getAggregationMetaRow(a);
				if(metaRow == null)
					return 0.;
				Double value = metaRow.getValue(columnInVis);
				return value;
			}
		}
		return null;
	}
	
	public String getMetaColumnText(int columnInVis, int rowInVis) {
		if(dataSet != null) {
			Aggregation a = this.aggregationContainer.get(rowInVis);
			if(a.isElemental()) {
				Integer indexInDataSet = a.getIndex();
				Double value = dataSet.getMetaColumn(columnInVis).getMappedValue(indexInDataSet.intValue());
				Object text = dataSet.getMetaColumn(columnInVis).getValue(value);
				if(text != null)
					return value.toString();
				else
					return "";
			} else {
				AggregationMetaRow metaRow = this.aggregationContainer.getAggregationMetaRow(a);
				Double value = metaRow.getValue(columnInVis);
				Object text = dataSet.getMetaColumn(columnInVis).getValue(value);

				if(text != null)
					return text.toString();
				else
					return "";
			}
		}
		return null;
	}

	public String getMetaRowID(int metaRow) {
		if(dataSet != null) {
			return this.dataSet.getMetaRowHeader(metaRow);
		}
		return "";
	}

	public void sortRows(IRowSorter sorter) {
		this.indexMapper.sortRows(sorter);
	}

	public void sortColumns(IColumnSorter sorter) {
		this.indexMapper.sortColumns(sorter);
	}

	public void setColumnSelection(Set<Integer> newSelection) {
		this.selectedColumns = newSelection;
	}

	public Collection<Integer> getSelectedColumns() {
		//prevent unrecognized selection manipulation
		return Collections.unmodifiableCollection(this.selectedColumns);
	}

	public void toggleColumnSelection(int columnInVis) {
		int columnInDataSet = this.getColumnInDataSet(columnInVis);
		if(isColumnSelected(columnInVis)) {
			this.selectedColumns.remove(columnInDataSet);
		} else {
			this.selectedColumns.add(columnInDataSet);
		}
	}

	public void setRowSelection(Set<Integer> newSelection) {
		this.selectedRows = newSelection;
	}
	
	public List<Integer> getSelectedRows() {
		return new ArrayList<Integer>(this.selectedRows);
	}

	public void toggleRowSelection(int rowInVis) {
		Collection<Integer> rowsInDataSet = this.getRowsInDataSet(rowInVis);
		if(isRowSelected(rowInVis))	{
			this.selectedRows.removeAll(rowsInDataSet);
		} else {
			this.selectedRows.addAll(rowsInDataSet);
		}
	}

	public void clearColumnSelection() {
		this.selectedColumns.clear();
	}

	public void clearRowSelection() {
		this.selectedRows.clear();
	}
	
	public void aggregateRowsByMetaInfo(IMatrixAggregator matrixAggregator, IMetaAggregator metaAggregator, int metaColumn) {
		if(dataSet != null) {
			IMetaData metaData = this.dataSet.getMetaColumn(metaColumn);
			List<List<Integer>> clusters = metaData.getClustersInDataSet();
			
			//successively aggregate the calculated clusters
			for(int i = 0; i < clusters.size(); i++) {
				this.indexMapper.aggregate(clusters.get(i), matrixAggregator, metaAggregator);
			}
			
			//clear selection
			this.clearRowSelection();
		}
	}

	public void aggregateSelectedRows(IMatrixAggregator matrixAggregator,
			IMetaAggregator metaAggregator) {
		if(dataSet != null) {
			this.indexMapper.aggregate(this.getSelectedRows(), matrixAggregator, metaAggregator);
			this.clearRowSelection();
		}
	}
	
	public void deaggregateSelectedRows() {
		if(dataSet != null) {
			this.indexMapper.deaggregate(this.getSelectedRows());
			this.clearRowSelection();
		}
	}

	public int getColumnInVis(int columnInDataSet) {
		return this.indexMapper.getColumnInVis(columnInDataSet);
	}

	public int getRowInVis(Integer rowInDataset) {
		Aggregation a = this.indexMapper.getAggregationFromDataSetIndex(rowInDataset);
		int r = this.indexMapper.getVisIndexFromAggregation(a);
		return r;
	}

	public int getRowInVis(Aggregation a) {
		return this.indexMapper.getVisIndexFromAggregation(a);
	}
	
	public void filterSNVs(Filter f) {
		List<SNP> filtered = f.applyFilter(this.filteredSNPs);
		this.filteredSNPs = filtered;
		this.indexMapper.filterSNPs();
		this.clearColumnSelection();
	}
	
	public void clearFiltering() {
		this.filteredSNPs = new ArrayList<SNP>(dataSet.getSNPs());
		this.indexMapper.clearFiltering();
		System.gc();
	}

	public List<SNP> getFilteredSNPs() {
		return Collections.unmodifiableList(filteredSNPs);
	}
}
