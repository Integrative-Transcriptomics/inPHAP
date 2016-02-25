package sorting;

import viewmodel.ViewModel;
import visualization.utilities.BidirectionalHashMap;
import dataStorage.SNPMetaData;

public interface IColumnSorter {

	public BidirectionalHashMap<Integer, Integer> sort(ViewModel viewModel, BidirectionalHashMap<Integer, Integer> mapping, SNPMetaData metaData, boolean ascending, boolean phased);
	public BidirectionalHashMap<Integer, Integer> sort(ViewModel viewModel, BidirectionalHashMap<Integer, Integer> mapping, boolean phased);
}
