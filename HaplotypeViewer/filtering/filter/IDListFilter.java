package filtering.filter;

import java.util.ArrayList;
import java.util.List;

import dataStorage.SNP;

public class IDListFilter implements Filter {

	String[] snvIDs;
	
	public IDListFilter(String snvIDs) {
		this.snvIDs = snvIDs.split(",");
		for(int i = 0; i < this.snvIDs.length; i++) {
			this.snvIDs[i] = this.snvIDs[i].trim();
		}
	}
	
	@Override
	public List<SNP> applyFilter(List<SNP> unfiltered) {
		List<SNP> filtered = new ArrayList<SNP>();
		for(SNP s : unfiltered) {
			String id = s.getRsid();
			if(contains(id)) {
				filtered.add(s);
			}
			//check if all snvs have been filtered
			if(filtered.size() == this.snvIDs.length) {
				break;
			}
		}
		return filtered;
	}
	
	private boolean contains(String id) {
		for(int i = 0; i < this.snvIDs.length; i++) {
			if(snvIDs[i].equals(id)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "ID-List Filter";
	}
}
