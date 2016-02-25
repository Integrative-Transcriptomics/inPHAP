package dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by peltzer on 12.02.14.
 */
public class NumericData implements IMetaData {
    private String name;
    private String type;
    private Double[] data;
    
    private double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

    public NumericData(int size) {
        this.type = NUMERICAL;
        this.data = new Double[size];
    }

    public void setData(Double[] data) {
        this.data = data;
        for(double d: data) {
        	if(d > max)
        		max = d;
        	if(d < min)
        		min = d;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void add(int index, String data) {
        Double input = Double.parseDouble(data);
        this.data[index] = input;
        if(input > this.max)
        	this.max = input;
        if(input < this.min)
        	this.min = input;
    }

	@Override
	public double getMinimum() {
		return this.min;
	}

	@Override
	public double getMaximum() {
		return this.max;
	}

	@Override
	public Double getMappedValue(int index) {
		if(this.data[index] == null) {
			return null;
		}
		return this.data[index];
	}
	
	public Object getValue(Double mappedValue) {
		return mappedValue;
	}

	@Override
	public Object getValue(int column) {
		return this.data[column];
	}

	@Override
	public Double getMappedValue(Object value) {
		return (Double)value;
	}

	@Override
	public List<List<Integer>> getClustersInDataSet() {
		Map<Double, List<Integer>> clustersMap = new HashMap<Double, List<Integer>>();
		
		for(int i = 0; i < this.data.length; i++) {
			Double key = this.data[i];
			if(clustersMap.containsKey(key)) {
				clustersMap.get(key).add(i);
			} else {
				List<Integer> l = new ArrayList<Integer>();
				l.add(i);
				clustersMap.put(key, l);
			}
		}
		
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		
		for(Double key : clustersMap.keySet()) {
			clusters.add(clustersMap.get(key));
		}
		
		return clusters;
	}
}
