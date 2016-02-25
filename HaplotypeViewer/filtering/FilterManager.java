package filtering;

import java.util.ArrayList;
import java.util.List;

import events.ViewModelEvent;
import filtering.filter.Filter;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;

public class FilterManager implements ViewModelListener {

	private ViewModel viewModel;
	
	private List<Filter> filters;
	
	public FilterManager(ViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		this.filters = new ArrayList<Filter>();
	}
	
	public void addFilter(Filter f) {
		this.filters.add(f);
	}
	
	public void removeFilter(Filter f) {
		this.filters.remove(f);
	}
	
	public Filter getFilter(int index) {
		return this.filters.get(index);
	}
	
	public void applyFilters() {
		for(int i = 0; i < filters.size(); i++) {
			this.applyFilter(filters.get(i));
		}
	}
	
	private void applyFilter(Filter f) {
		//TODO
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		switch(e.getChange()) {
		case ViewModelEvent.DATASET_CHANGED:
			//TODO
			break;
		}
	}
}
