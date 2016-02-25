package dataStorage;

import java.util.ArrayList;
import java.util.List;

import visualization.utilities.BidirectionalHashMap;

public class SNPMetaData {

	private String name = "SNP-Meta";
	private List<String[]> data;
	
	private BidirectionalHashMap<String, Integer> valueMapping = new BidirectionalHashMap<String, Integer>();
	private int mappedValue = 0;
	
	public SNPMetaData() {
		this.data = new ArrayList<String[]>();
	}
	
	public SNPMetaData(String name) {
		this();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add(int index, String data) {
		//do not split if data is missing
		if(data == null) {
			if(index >= this.data.size()) {
				this.data.add(new String[]{"",""});
			} else {
				this.data.add(index, new String[]{"",""});
			}
		}
		
		String[] dataSplit = data.split(":");
		
		if(dataSplit.length == 0) {
			dataSplit = new String[]{"",""};
		}

		if(index >= this.data.size()) {
			this.data.add(dataSplit);
		} else {
			this.data.add(index, dataSplit);
		}
		
		if(this.valueMapping.get(dataSplit[0]) == null) {
			if(dataSplit[0].length() > 0) {
				this.valueMapping.put(dataSplit[0], mappedValue);
	        	mappedValue++;
			}
        }

		if(this.valueMapping.get(dataSplit[1]) == null) {
			if(dataSplit[1].length() > 0) {
				this.valueMapping.put(dataSplit[1], mappedValue);
	            mappedValue++;
			}
        }
	}

	public double getMinimum() {
		return 0;
	}

	public double getMaximum() {
		if(mappedValue >= 1)
			return mappedValue - 1;
		return 0;
	}

	public Double getMappedValue(int index, boolean paternal) {
		int p = paternal ? 0 : 1;
		Integer value = this.valueMapping.getLeft(this.data.get(index)[p]);
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

	public Object getValue(int column, boolean paternal) {
		int p = paternal ? 0 : 1;
		return this.data.get(column)[p];
	}

	public Double getMappedValue(Object value) {
		Integer in = this.valueMapping.getLeft(value);
		if(value == null) {
			return null;
		}
		return new Double(in.doubleValue());
	}

	public void add(String value) {
		this.add(this.data.size(), value);
	}
}
