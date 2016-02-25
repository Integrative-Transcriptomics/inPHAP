package viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import sorting.IColumnSorter;
import sorting.IRowSorter;
import visualization.utilities.BidirectionalHashMap;
import aggregation.Aggregation;
import aggregation.AggregationContainer;
import aggregation.AggregationMatrixRow;
import aggregation.AggregationMetaRow;
import aggregation.IMatrixAggregator;
import aggregation.IMetaAggregator;
import dataStorage.DataSet;
import events.ViewModelEvent;

public class PhasedIndexMapper implements ViewModelListener {
	
	/*
	 * maps the index of a subject to the index of its aggregated form
	 * at the beginning each subject is mapped to an aggregation containing
	 * only himself
	 * Key = Subjects, Value = Aggregations
	 * 
	 * If elements get aggregated mapping has to be changed!
	 * the old aggregation objects have to be removed and a new aggregation object has to be introduced
	 * as well as links from each subject that gets aggregated to the new aggregation object!
	 * 
	 * This has to be done recursively!
	 * 
	 */
	private HashMap<Integer, Aggregation> dataSetToAggregation = new HashMap<Integer, Aggregation>();

	//column in data set = left, column in plot = right
	private BidirectionalHashMap<Integer, Integer> columnSorting = new BidirectionalHashMap<Integer, Integer>();
	private BidirectionalHashMap<Integer, Integer> unphasedColumnSorting = new BidirectionalHashMap<Integer, Integer>();
	
	/*
	 * the phased index mapper holds a reference to the aggregation container and to the dataset
	 * from the aggregation container it gets size and indices of aggregations
	 * from the dataset it gets size and indices of subjects
	 * - mapping from view to dataset is done in two phases:
	 * 1. map from view to aggregation index -> reverse sorting!
	 * 2. map from aggregation index to subjects -> reverse aggregation
	 * 
	 * -mapping from dataset to view is done in two phases:
	 * 1. map from dataset index to aggregation index -> aggregate
	 * 2. map from aggregation index to view index -> sort
	 */
	
	private DataSet dataSet;
	private AggregationContainer aggregationContainer;
	private ViewModel viewModel;
	
	public PhasedIndexMapper(ViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	
	
	private void createInitialSorting() {
		for(int i = 0; i < viewModel.getFilteredSNPs().size() * 2; i++) {
			this.columnSorting.put(i, i);
		}
		
		for(int i = 0; i < viewModel.getFilteredSNPs().size(); i++) {
			this.unphasedColumnSorting.put(i, i);
		}
	}

	private void createInitialMapping() {
		for(int i = 0; i < dataSet.getNumSubjects(); i++) {
			this.dataSetToAggregation.put(dataSet.getSubjects().get(i).getIndex(), 
					this.aggregationContainer.get(i));
		}
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		this.createInitialSorting();
		this.createInitialMapping();
	}

	public int numColsInVis() {
		if(dataSet != null) {
			return viewModel.getFilteredSNPs().size();
		}
		return 0;
	}
	
	//data set column is left
	public int getColumnInDataSet(int columnInPlot) {
		if(viewModel.getSNPMapSetting().isPhased()) {
			//if column is even than the SNV has index column DIV 2 in the data set
			Integer indexInFilteredList = ((Integer)this.columnSorting.getRight(columnInPlot)) / 2;
			Integer cDS = viewModel.getFilteredSNPs().get(indexInFilteredList).getIndex();
			return cDS;
		} else {
			//for unphased data there is just a single column
			Integer indexInFilteredList = ((Integer)this.unphasedColumnSorting.getRight(columnInPlot));
			Integer cDS = viewModel.getFilteredSNPs().get(indexInFilteredList).getIndex();
			return cDS;
		}
	}

	//true if the given column in the plot is paternal
	public boolean isPaternalColumnInPlot(int columnInPlot) {
		//TODO introduce filtering
		Integer mappedColumn = this.columnSorting.getRight(columnInPlot);
		return mappedColumn % 2 == 0;
	}

	public void sortColumns(IColumnSorter sorter) {
		BidirectionalHashMap<Integer, Integer> mapping = sorter.sort(viewModel, columnSorting, true);
		this.columnSorting = mapping;
		BidirectionalHashMap<Integer, Integer> unphased_mapping = sorter.sort(viewModel, unphasedColumnSorting, false);
		this.unphasedColumnSorting = unphased_mapping;
		System.gc();
	}

	public void sortRows(IRowSorter sorter) {
		sorter.sort(this.aggregationContainer);
	}

	public int getColumnInVis(int columnInDataSet) {
		if(viewModel.getSNPMapSetting().isPhased()) {
			//always returns paternal index
			Integer columnInVis = ((Integer)this.columnSorting.getLeft(columnInDataSet * 2));
			return columnInVis;
		} else {
			//always returns paternal index
			Integer columnInVis = ((Integer)this.unphasedColumnSorting.getLeft(columnInDataSet * 2));
			return columnInVis;
		}
	}

	public void aggregate(List<Integer> indicesInDataSet, IMatrixAggregator matrixAggregator, IMetaAggregator metaAggregator) {
		List<Integer> indicesInVis = new ArrayList<Integer>();
		
		for(Integer indexInDataSet : indicesInDataSet) {
			Aggregation a = this.getAggregationFromDataSetIndex(indexInDataSet);
			int indexInVis = this.getVisIndexFromAggregation(a);
			if(!indicesInVis.contains(indexInVis)) {
				indicesInVis.add(indexInVis);
			}
		}
		
		//sort this list
		Collections.sort(indicesInVis);
		
		if(indicesInVis.size() > 1) {
			/*
			 * remember the first index in vis
			 * the new aggregation will be placed at that specific location
			 */
			Integer firstIndexInVis = indicesInVis.get(0);
			Aggregation firstAggregation = getAggregationFromVisIndex(firstIndexInVis);
			Aggregation ag = new Aggregation(this.aggregationContainer.getNextAggregationName(), firstAggregation.getIndex());
			
			//add all elements to new aggregation object
			for(int i = 0; i  < indicesInVis.size(); i++) {
				Integer indexInVis = indicesInVis.get(i);
				Aggregation a =  getAggregationFromVisIndex(indexInVis);
				ag.add(a);
			}
			
			//aggregate the data from the dataset
			AggregationMatrixRow matrixRow = matrixAggregator.aggregate(viewModel, ag);
			//TODO implement metaAggregator
			if(dataSet.getNumMetaCols() > 0) {
				AggregationMetaRow metaRow = metaAggregator.aggregate(viewModel, ag);
				//finally add aggregation object to aggregationContainer
				this.aggregationContainer.addAggregation(firstIndexInVis, ag, matrixRow, metaRow);
			} else {
				//finally add aggregation object to aggregationContainer
				this.aggregationContainer.addAggregation(firstIndexInVis, ag, matrixRow, null);
			}
			
			//remove all aggregated aggregations from aggregationContainer
			this.aggregationContainer.removeAggregations(ag);
			
			/*
			 * fix aggregation mapping
			 * 
			 * therefore get all elementary aggregation and remove for
			 * each subject they represent, the corresponding link and
			 * add a new link to the new aggregation
			 */
			for(Aggregation a : ag.getElements()) {
				/*
				 * all elementary aggregation objects share the same index
				 * as their respective subject in the underlying data set!
				 */
				int subjectIndexInDataSet = a.getIndex();
				//remove the old link to the aggregation
				this.dataSetToAggregation.remove(subjectIndexInDataSet);
				//ad a new link to the new aggregation
				this.dataSetToAggregation.put(subjectIndexInDataSet, ag);
			}
			
			//done!
		}
	}
	
	public void deaggregate(List<Integer> indicesInDataSet) {
		List<Integer> indicesInVis = new ArrayList<Integer>();
		
		for(Integer indexInDataSet : indicesInDataSet) {
			Aggregation a = this.getAggregationFromDataSetIndex(indexInDataSet);
			int indexInVis = this.getVisIndexFromAggregation(a);
			if(!indicesInVis.contains(indexInVis)) {
				indicesInVis.add(indexInVis);
			}
		}
		
		//sort this list
		Collections.sort(indicesInVis);
		//reverse the order to prevent vis-index-changes while removing and adding elements
		Collections.reverse(indicesInVis);
		
		for(Integer indexInVis : indicesInVis) {
			Aggregation ag = this.getAggregationFromVisIndex(indexInVis);
			//de-aggregation is only possible if the selected aggregation is not 
			if(!ag.isElemental()) {
				//remove ag from the aggregation container
				this.aggregationContainer.removeAggregation(ag);
				/*
				 * for each aggregation in ag insert in reverse order
				 * this maintains the same order as before the aggregation step
				 */
				for(int i = ag.size()-1; i >= 0; i--) {
					//add all aggregations from ag to the aggregation container
					this.aggregationContainer.add(indexInVis, ag.get(i));
				}
				
				//fix data set mapping
				for(Aggregation a : ag) {
					if(a.isElemental()) {
						this.dataSetToAggregation.put(a.getIndex(), a);
					} else {
						//get all elemetary aggregations from a
						for(Aggregation a2: a.getElements()) {
							//link all subjects from a to a
							this.dataSetToAggregation.put(a2.getIndex(), a);
						}
					}
				}
				
				//done!
			}
			
			//if ag is elemental then we don't have to do anything
		}
	}

	/*
	 * get from an aggregation to the vis
	 */
	public Aggregation getAggregationFromVisIndex(Integer indexInVis) {
		return this.aggregationContainer.get(indexInVis);
	}
	
	/*
	 * get from the vis to an aggregation
	 */
	public Integer getVisIndexFromAggregation(Aggregation a) {
		return this.aggregationContainer.indexOf(a);
	}
	
	/*
	 * get from an aggregation to 
	 */
	public Aggregation getAggregationFromDataSetIndex(Integer indexInDataSet) {
		return this.dataSetToAggregation.get(indexInDataSet);
	}
	
	public List<Integer> getDataSetIndicesFromAggregation(Aggregation ag) {
		List<Integer> dataSetIndices = new ArrayList<Integer>();
		for(Aggregation a : ag.getElements()) {
			dataSetIndices.add(a.getIndex());
		}
		return dataSetIndices;
	}
	
	/*
	 * set the aggregation container
	 */
	public void setAggregationContainer(AggregationContainer aggregationContainer) {
		this.aggregationContainer = aggregationContainer;
	}
	
	public void filterSNPs() {
		this.columnSorting.clear();
		this.unphasedColumnSorting.clear();
		this.createInitialSorting();
	}
	
	@Override
	public void viewModelChanged(ViewModelEvent e) {
		switch(e.getChange()) {
		case ViewModelEvent.DATASET_CHANGED:
			this.setDataSet(viewModel.getDataSet());
			System.gc();
			break;
		}
	}

	public void clearFiltering() {
		filterSNPs();
	}
}
