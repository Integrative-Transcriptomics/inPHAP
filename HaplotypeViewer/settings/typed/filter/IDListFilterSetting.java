package settings.typed.filter;

import filtering.filter.Filter;
import filtering.filter.IDListFilter;
import settings.typed.FilterSetting;
import settings.typed.StringSetting;

public class IDListFilterSetting extends FilterSetting {

	StringSetting idList;
	
	public IDListFilterSetting() {
		super("Identifier List Filter");
		this.addSetting(idList = new StringSetting("Identifiers", ""));
	}

	@Override
	public Filter getFilter() {
		return new IDListFilter(this.idList.getValue());
	}
}
