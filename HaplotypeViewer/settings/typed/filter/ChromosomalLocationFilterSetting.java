package settings.typed.filter;

import filtering.filter.ChromosomalLocationFilter;
import filtering.filter.Filter;
import settings.typed.FilterSetting;
import settings.typed.IntSetting;
import settings.typed.StringSetting;

public class ChromosomalLocationFilterSetting extends FilterSetting {

	private StringSetting chromosome;
	private IntSetting startPosition;
	private IntSetting stopPosition;
	
	public ChromosomalLocationFilterSetting() {
		super("Chromosomal Location Filter Setting");
		
		addSetting(chromosome = new StringSetting("Chromosome", ""));
		addSetting(startPosition = new IntSetting("Start", 0));
		addSetting(stopPosition = new IntSetting("End", 0));
	}

	@Override
	public Filter getFilter() {
		return new ChromosomalLocationFilter(chromosome.getValue(), startPosition.getValue(), stopPosition.getValue());
	}
}