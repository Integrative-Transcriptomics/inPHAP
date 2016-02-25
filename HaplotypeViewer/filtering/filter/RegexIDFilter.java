package filtering.filter;

import java.util.ArrayList;
import java.util.List;

import dataStorage.SNP;

public class RegexIDFilter implements Filter {

	private String regEx;
	
	public RegexIDFilter(String regEx) {
		this.regEx = regEx;
	}
	
	@Override
	public List<SNP> applyFilter(List<SNP> unfiltered) {
		List<SNP> filtered = new ArrayList<SNP>();
		for(int i = 0; i < unfiltered.size(); i++) {
			SNP s = unfiltered.get(i);
			if(s.getRsid().matches(this.regEx)) {
				filtered.add(s);
			}
		}
		return filtered;
	}

	@Override
	public String getName() {
		return "Regular Expression";
	}
}
