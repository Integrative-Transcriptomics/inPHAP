package settings.typed;

import java.util.ArrayList;
import java.util.List;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.typed.filter.ChromosomalLocationFilterSetting;
import settings.typed.filter.IDListFilterSetting;
import settings.typed.filter.RarityFilterSetting;
import settings.typed.filter.RegExFilterSetting;
import settings.typed.filter.SelectionFilterSetting;
import viewmodel.ViewModel;
import filtering.filter.Filter;

public class FilterSelectionSetting extends HierarchicalSetting {

	public static final String REGEX_FILTER = "Regular Expression";
	public static final String IDLIST_FILTER = "Identifier List";
	public static final String CHROMOSOMAL_LOCATION = "Chromosomal Location";
	public static final String FREQUENCY_THRESHOLD = "Frequency";
	public static final String SELECTION_FILTER = "Selection";
	
	private GeneralSettingReplaceSetting replaceSetting;
	
	private final String[] filters = {REGEX_FILTER, IDLIST_FILTER, CHROMOSOMAL_LOCATION, FREQUENCY_THRESHOLD, SELECTION_FILTER};
	
	public FilterSelectionSetting(String title, ViewModel viewModel) {
		super(title);
		
		//list must be in the same order as the filters string
		List<HierarchicalSetting> filterSettings = new ArrayList<HierarchicalSetting>();
		filterSettings.add(new RegExFilterSetting());
		filterSettings.add(new IDListFilterSetting());
		filterSettings.add(new ChromosomalLocationFilterSetting());
		filterSettings.add(new RarityFilterSetting(viewModel));
		filterSettings.add(new SelectionFilterSetting(viewModel));
		
		StringChooserSetting chooser = new StringChooserSetting("Filter Method", 0, filters);
		this.replaceSetting = new GeneralSettingReplaceSetting(filterSettings, 0, chooser);
		
		addSetting(chooser);
		addSetting(replaceSetting);
	}
	
	public Filter getFilter() {
		FilterSetting fs = (FilterSetting)replaceSetting.getChoosenSetting();
		return fs.getFilter();
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//pass on the event
		this.fireSettingChanged(e);
	}
}
