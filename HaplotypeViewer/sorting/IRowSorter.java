package sorting;

import java.util.List;

import aggregation.Aggregation;

public interface IRowSorter {
	
	public void sort(List<Aggregation> aggregations, Integer metaDataIndex, boolean ascending);
	public void sort(List<Aggregation> mapping);
}
