package filtering.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import viewmodel.ViewModel;
import dataStorage.SNP;

public class SelectionFilter implements Filter {

	private ViewModel viewModel;
	
	public SelectionFilter(ViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public List<SNP> applyFilter(List<SNP> unfiltered) {
		Collection<Integer> selectedColumns = viewModel.getSelectedColumns();
		List<SNP> filtered = new ArrayList<SNP>();
		for(SNP s : unfiltered) {
			Integer snpIndex = s.getIndex();
			if(selectedColumns.contains(snpIndex)) {
				filtered.add(s);
			}
		}
		return filtered;
	}

	@Override
	public String getName() {
		return "Selection";
	}

}
