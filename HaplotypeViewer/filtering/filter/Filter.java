package filtering.filter;

import java.util.List;

import dataStorage.SNP;

public interface Filter {

	public List<SNP> applyFilter(List<SNP> unfiltered);
	public String getName();
}
