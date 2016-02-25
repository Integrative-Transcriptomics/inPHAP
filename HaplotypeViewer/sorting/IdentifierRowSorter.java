package sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aggregation.Aggregation;

public class IdentifierRowSorter implements IRowSorter {

	private boolean ascending;
	
	public IdentifierRowSorter(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public void sort(List<Aggregation> mapping, Integer metaDataIndex, boolean ascending) {
		Collections.sort(mapping, new IdentifierComparator(ascending));
	}
	
	private class IdentifierComparator implements Comparator<Aggregation> {
		
		boolean ascending;
		
		public IdentifierComparator(boolean ascending) {
			this.ascending = ascending;
		}
		
		@Override
		public int compare(Aggregation a1, Aggregation a2) {
			if(ascending) {
				return a1.getID().compareTo(a2.getID());
			} else {
				return a2.getID().compareTo(a1.getID());
			}
		}
	}

	@Override
	public void sort(List<Aggregation> mapping) {
		this.sort(mapping, null, this.ascending);
	}
}
