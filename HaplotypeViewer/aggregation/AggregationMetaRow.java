package aggregation;

import java.util.ArrayList;
import java.util.List;

public class AggregationMetaRow {

	private List<Double> aggregatedRow;
	
	public AggregationMetaRow(int size) {
		this.aggregatedRow = new ArrayList<Double>(size);
	}
	
	public void add(int index, Double value) {
		this.aggregatedRow.add(index, value);
	}
	
	public Double getValue(int index) {
		return this.aggregatedRow.get(index);
	}

	public List<Double> getValues() {
		return this.aggregatedRow;
	}
}
