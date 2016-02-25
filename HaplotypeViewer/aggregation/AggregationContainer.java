package aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import viewmodel.ViewModel;
import dataStorage.Person;

public class AggregationContainer extends ArrayList<Aggregation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5059334331721159617L;
	
	private HashMap<Aggregation, AggregationMatrixRow> matrixValueMapping = new HashMap<Aggregation, AggregationMatrixRow>();
	private HashMap<Aggregation, AggregationMetaRow> metaValueMapping = new HashMap<Aggregation, AggregationMetaRow>();
	
	private int agID;
	
	public AggregationContainer(ViewModel viewModel) {
		super();
		
		/*
		 * setup the aggregation container
		 */
		List<Person> subjects = viewModel.getDataSet().getSubjects();
		
		/*
		 * create one aggregation object per subject
		 */
		for(int i = 0; i < subjects.size(); i++) {
			Person s = subjects.get(i);
			Aggregation a = new Aggregation(s.getName(), s.getIndex());
			this.add(a);
		}
		
//		this.agID = subjects.size();
		this.agID = 1;
	}
	
	public void addAggregation(Integer insertionIndex, Aggregation a, AggregationMatrixRow matrixRow, AggregationMetaRow metaRow) {
		this.add(insertionIndex, a);
		this.matrixValueMapping.put(a, matrixRow);
		this.metaValueMapping.put(a, metaRow);
	}
	
	public void removeAggregation(Aggregation a) {
		this.matrixValueMapping.remove(a);
		this.metaValueMapping.remove(a);
		this.remove(a);
	}

	public AggregationMatrixRow getAggregationMatrixRow(Aggregation r) {
		return this.matrixValueMapping.get(r);
	}
	
	public AggregationMetaRow getAggregationMetaRow(Aggregation r) {
		return this.metaValueMapping.get(r);
	}

	public String getNextAggregationName() {
		return "AGN" + Integer.toString(agID++);
	}

	public void removeAggregations(List<Aggregation> aggregations) {
		for(Aggregation a : aggregations)
			this.remove(a);
	}
}
