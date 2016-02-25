package dataStorage;

import java.util.List;


/**
 * Created by peltzer on 12.02.14.
 */
public interface IMetaData {

    public String getName();
    public void setName(String name);

    public static final String CATEGORICAL = "Categorical";
    public static final String NUMERICAL = "Numerical";

    public String getType();

    public void add(int index, String data);
	
    //min and max methods are needed for color gradients
    public double getMinimum();
    
	public double getMaximum();
	
	public Double getMappedValue(int index);
	
	public Object getValue(Double mappedValue);
	public Object getValue(int column);
	public Double getMappedValue(Object value);
	public List<List<Integer>> getClustersInDataSet();
}
