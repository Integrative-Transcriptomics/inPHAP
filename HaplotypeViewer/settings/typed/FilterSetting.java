package settings.typed;

import filtering.filter.Filter;
import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public abstract class FilterSetting extends HierarchicalSetting {
	
	public FilterSetting(String title) {
		super(title);
	}
	
	public abstract Filter getFilter();

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//
	}
}
