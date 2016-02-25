package aggregation;

import java.util.Arrays;
import java.util.List;

import viewmodel.ViewModel;

/**
 * Created by alex on 02.03.14.
 */
public class MinimumMetaAggregator implements IMetaAggregator {

    @Override
    public AggregationMetaRow aggregate(ViewModel viewModel, Aggregation a) {
    	List<Aggregation> aggregations = a.getElements();
        Double[][] data = new Double[aggregations.size()][viewModel.getDataSet().getNumMetaCols()];
        
      //initialize data array, otherwise it is unclear what it contains in fields that are not touched
        for(int i = 0; i < data.length; i++) {
        	Arrays.fill(data[i], 0.0);
        }
        
        int ic = 0;
        for (Aggregation a2 : aggregations){
        	int r = viewModel.getRowInVis(a2);
            for(int j = 0; j < viewModel.getDataSet().getNumMetaCols(); j++){
                data[ic][j] = viewModel.getMetaColumnValue(j,r);
            }
            ic++;
        }
        return calculateFrequencies(data, viewModel);
    }

    private AggregationMetaRow calculateFrequencies(Double[][] input, ViewModel vmr){
        AggregationMetaRow amr = new AggregationMetaRow(vmr.numMetaCols());
        Double[] cols = new Double[input[0].length];
        for(int i = 0; i < input.length; i++){
            for(int j = 0; j < input[i].length; j++) {
            	if(cols[j] == null && input[i][j] != null) {
            		cols[j] = input[i][j];
            	} else if(input[i][j] == null && cols[j] != null) {
            		//skip
            	} else if(cols[j] == null && input[i][j] == null) {
            		//skip
            	} else if(cols[j] > input[i][j]) {
                    cols[j] = input[i][j];
                }
            }
        }
        for(int i = 0; i < cols.length; i++){
            amr.add(i, cols[i]);
        }
        return amr;
    }
}
