package dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import visualization.utilities.BidirectionalHashMap;


/**
 * Created by peltzer on 12.02.14.
 */
public class CategoryData implements IMetaData {
	
    private String name;
    private String type;
    private String[] data;
    
    private BidirectionalHashMap<String, Integer> valueMapping = new BidirectionalHashMap<String, Integer>();
    private int mappedValue = 0;

    public CategoryData(int size) {
        this.data = new String[size];
        this.type = CATEGORICAL;
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
        return type;
    }

    @Override
    public void add(int index, String data) {
        this.data[index] =  data;
        if(this.valueMapping.get(data) == null) {
        	this.valueMapping.put(data, mappedValue);
        	mappedValue++;
        }
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
        for(int i = 0; i < this.data.length; i++) {
        	if(this.valueMapping.get(this.data[i]) == null) {
        		this.valueMapping.put(this.data[i], mappedValue);
        		mappedValue++;
        	}
        }
    }

	@Override
	public double getMinimum() {
		return 0;
	}

	@Override
	public double getMaximum() {
		if(mappedValue >= 1)
			return mappedValue - 1;
		return 0;
	}

	@Override
	public Double getMappedValue(int index) {
		Integer value = this.valueMapping.getLeft(this.data[index]);
		if(value == null) {
			return null;
		}
		return new Double(value.doubleValue());
	}
	
	public Object getValue(Double mappedValue) {
		if(mappedValue != null) {
			String value = this.valueMapping.getRight(mappedValue.intValue());
			return value;
		}
		return null;
	}

	@Override
	public Object getValue(int column) {
		return this.data[column];
	}

	@Override
	public Double getMappedValue(Object value) {
		return this.valueMapping.getRight(value);
	}

	@Override
	public List<List<Integer>> getClustersInDataSet() {
		Map<String, List<Integer>> clustersMap = new HashMap<String, List<Integer>>();
		
		for(int i = 0; i < this.data.length; i++) {
			String key = this.data[i];
			if(clustersMap.containsKey(key)) {
				clustersMap.get(key).add(i);
			} else {
				List<Integer> l = new ArrayList<Integer>();
				l.add(i);
				clustersMap.put(key, l);
			}
		}
		
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		
		for(String key : clustersMap.keySet()) {
			clusters.add(clustersMap.get(key));
		}
		
		return clusters;
	}
}
