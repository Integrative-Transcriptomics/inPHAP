package filtering.filter;

import java.util.ArrayList;
import java.util.List;

import dataStorage.SNP;


public class ChromosomalLocationFilter implements Filter {
	
	private String chromosome;
	private int startPosition;
	private int stopPosition;
	
	public ChromosomalLocationFilter(String chromosome, int startPosition, int stopPosition) {
		this.chromosome = chromosome;
		this.startPosition = startPosition;
		this.stopPosition = stopPosition;
	}
	
	public List<SNP> applyFilter(List<SNP> snps) {
		List<SNP> result = new ArrayList<SNP>();
		
		//nothing to do, since no snp can fulfill this
		if(startPosition > stopPosition)
			return result;
		
		//check for all snps if they satisfy the specified locus
		for(int i = 0; i < snps.size(); i++) {
			SNP s = snps.get(i);
			if(s.getChromosome().toLowerCase().equals(chromosome.toLowerCase())) {
				if(s.getChrompos() >= startPosition 
						&& s.getChrompos() <= stopPosition) {
					result.add(s);
				}
			}
		}
		
		return result;
	}

	@Override
	public String getName() {
		return "Chromosomal Location";
	}
}
