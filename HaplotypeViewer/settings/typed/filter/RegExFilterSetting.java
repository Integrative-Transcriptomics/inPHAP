package settings.typed.filter;

import settings.typed.FilterSetting;
import settings.typed.StringSetting;
import filtering.filter.Filter;
import filtering.filter.RegexIDFilter;

public class RegExFilterSetting extends FilterSetting {

	private StringSetting stringSetting;
	
	public RegExFilterSetting() {
		super("Regular Expression Filter Setting");
		this.addSetting(stringSetting = new StringSetting("Regular Expression", ""));
	}

	@Override
	public Filter getFilter() {
		return new RegexIDFilter(this.stringSetting.getValue());
	}
}
