package settings.typed.filter;

import settings.typed.FilterSetting;
import viewmodel.ViewModel;
import filtering.filter.Filter;
import filtering.filter.SelectionFilter;

public class SelectionFilterSetting extends FilterSetting {

	private ViewModel viewModel;
	
	public SelectionFilterSetting(ViewModel viewModel) {
		super("Selection Filter Setting");
		this.viewModel = viewModel;
	}
	
	@Override
	public Filter getFilter() {
		return new SelectionFilter(viewModel);
	}
}
