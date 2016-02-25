package sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import viewmodel.ViewModel;
import visualization.utilities.BidirectionalHashMap;
import dataStorage.SNPMetaData;

public class MetaDataColumnSorter implements IColumnSorter {

	private SNPMetaData metaData;
	private boolean ascending;
	
	public MetaDataColumnSorter(SNPMetaData metaData, boolean ascending) {
		this.metaData = metaData;
		this.ascending = ascending;
	}
	
	@Override
	public BidirectionalHashMap<Integer, Integer> sort(ViewModel viewModel, BidirectionalHashMap<Integer, Integer> mapping,
			SNPMetaData metaData, boolean ascending, boolean phased) {
		
		List<Integer> oldSorting = new ArrayList<Integer>();
		
		for(int i = 0; i < mapping.size(); i++) {
			int oldSortIndex = mapping.getRight(i);
			oldSorting.add(oldSortIndex);
		}

		Collections.sort(oldSorting, new MetaDataComparator(viewModel, metaData, ascending, phased));
		
		BidirectionalHashMap<Integer, Integer> new_mapping = new BidirectionalHashMap<Integer, Integer>();
		
		for(Integer index : mapping.getLeftElements()) {
			new_mapping.put(index, oldSorting.indexOf(index));
		}
		
		return new_mapping;
	}
	
	private class MetaDataComparator implements Comparator<Integer> {
		
		private SNPMetaData metaData;
		private boolean ascending;
		private boolean phased;
		private ViewModel viewModel;
		
		public MetaDataComparator(ViewModel viewModel, SNPMetaData metaData, boolean ascending, boolean phased) {
			this.metaData = metaData;
			this.ascending = ascending;
			this.phased = phased;
			this.viewModel = viewModel;
		}
		
		@Override
		public int compare(Integer o1, Integer o2) {
			if(phased) {
				
				boolean paternal1 = o1 % 2 == 0;
				boolean paternal2 = o2 % 2 == 0;
				
				o1 = viewModel.getFilteredSNPs().get(o1/2).getIndex();
				o2 = viewModel.getFilteredSNPs().get(o2/2).getIndex();
				
				String s1 = (String)metaData.getValue(o1, paternal1);
				String s2 = (String)metaData.getValue(o2, paternal2);
				
				s1 = s1 == null ? "~" : s1;
				s2 = s2 == null ? "~" : s2;
				
				int r = ascending ? s1.compareTo(s2) : s2.compareTo(s1);
				
				return r;
			} else {
				o1 = viewModel.getFilteredSNPs().get(o1).getIndex();
				o2 = viewModel.getFilteredSNPs().get(o2).getIndex();
				
				String s1 = (String)metaData.getValue(o1, true);
				String s2 = (String)metaData.getValue(o2, true);
				
				s1 = s1 == null ? "~" : s1;
				s2 = s2 == null ? "~" : s2;
				
				int r = ascending ? s1.compareTo(s2) : s2.compareTo(s1);
				
				return r;
			}
		}
	}

	@Override
	public BidirectionalHashMap<Integer, Integer> sort(ViewModel viewModel, BidirectionalHashMap<Integer, Integer> mapping, boolean phased) {
		return this.sort(viewModel, mapping, this.metaData, this.ascending, phased);
	}
}
