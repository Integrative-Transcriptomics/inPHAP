package aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aggregation extends ArrayList<Aggregation> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7347032164304254237L;
	
	private int numAgOb;
	private String id;
	private int index;
	
	/*
	 * this list contains all aggregations of size 1 representing a subject in
	 * the dataset
	 */
	private List<Aggregation> elements = new ArrayList<Aggregation>();
	
	public Aggregation(String id, int index) {
		super();
		this.id = id;
		this.numAgOb = 0;
		this.index = index;
	}
	
	public int numAggregatedObjects() {
		return this.numAgOb;
	}
	
	public boolean add(Aggregation value) {
		numAgOb += value.numAggregatedObjects();
		if(value.isElemental()) {
			this.elements.add(value);
		} else {
			this.elements.addAll(value.getElements());
		}
		return super.add(value);
	}

	public String getID() {
		if(this.getElements().size() > 0) {
			return this.id + " (" + this.getElements().size() + ")";
		} else {
			return this.id;
		}
	}

	public int getIndex() {
		return this.index;
	}
	
	public List<Aggregation> getElements() {
		return Collections.unmodifiableList(this.elements);
	}
	
	public boolean isElemental() {
		return this.size() == 0;
	}
	
	public boolean equals(Object a) {
		if(!(a instanceof Aggregation)) {
			return false;
		}
		return this.getID().equals(((Aggregation)a).getID());
	}
	
	public String toString() {
		return "[" + this.getID() + ":" + this.getIndex() + "] " + "size=" + this.size();
	}
}
