package settings.typed.filter;

import settings.typed.DoubleSetting;
import settings.typed.FilterSetting;
import settings.typed.StringChooserSetting;
import viewmodel.ViewModel;
import filtering.filter.Filter;
import filtering.filter.RarityFilter;

public class RarityFilterSetting extends FilterSetting {

	public static final String GREATER = "Greater Equal";
	public static final String LESS = "Less Equal";
	
	private StringChooserSetting greater;
	private DoubleSetting threshold;
	
	private String[] values = {GREATER, LESS};
	
	private ViewModel viewModel;
	
	public RarityFilterSetting(ViewModel viewModel) {
		super("SNV Frequency");
		this.viewModel = viewModel;
		
		addSetting(greater = new StringChooserSetting("Comparator", 1, values));
		addSetting(threshold = new DoubleSetting("Threshold", 0.05));
	}

	@Override
	public Filter getFilter() {
		boolean greaterV = greater.getSelectedValue() == GREATER ? true : false;
		return new RarityFilter(viewModel, threshold.getValue(), greaterV);
	}
}
