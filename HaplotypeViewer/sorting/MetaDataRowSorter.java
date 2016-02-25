package sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import viewmodel.ViewModel;
import aggregation.Aggregation;
import dataStorage.IMetaData;

public class MetaDataRowSorter implements IRowSorter {
	
	private ViewModel viewModel;
	private boolean ascending;
	private Integer metaDataIndex;
	
	public MetaDataRowSorter(ViewModel viewModel , Integer metaDataIndex, boolean ascending) {
		this.viewModel = viewModel;
		this.metaDataIndex = metaDataIndex;
		this.ascending = ascending;
	}
	
	@Override
	public void sort(List<Aggregation> mapping, Integer metaDataIndex, boolean ascending) {
		Collections.sort(mapping, new MetaDataComparator(viewModel, metaDataIndex, ascending));
	}
	
	private class MetaDataComparator implements Comparator<Aggregation> {		
		
		private ViewModel viewModel;
		private Integer metaDataIndex;
		private boolean ascending;
		
		public MetaDataComparator(ViewModel viewModel, Integer metaDataIndex, boolean ascending) {
			this.metaDataIndex = metaDataIndex;
			this.ascending = ascending;
			this.viewModel = viewModel;
		}
		
		@Override
		public int compare(Aggregation a1, Aggregation a2) {
			String type = viewModel.getMetaColumn(metaDataIndex).getType();
			int row1 = viewModel.getRowInVis(a1);
			int row2 = viewModel.getRowInVis(a2);
			
			switch(type) {
			case IMetaData.CATEGORICAL:
				//fall through
			case IMetaData.NUMERICAL:
				Double d1 = viewModel.getMetaColumnValue(metaDataIndex, row1);
				d1 = d1 == null ? Double.MAX_VALUE : d1;
				Double d2 = viewModel.getMetaColumnValue(metaDataIndex, row2);
				d2 = d2 == null ? Double.MAX_VALUE : d2;
				return ascending ? Double.compare(d1, d2): Double.compare(d2, d1);
			default:
				return ascending? a1.getID().compareTo(a2.getID()) : a2.getID().compareTo(a1.getID());
			}
		}
	}

	@Override
	public void sort(List<Aggregation> mapping) {
		this.sort(mapping, this.metaDataIndex, this.ascending);
	}
}
