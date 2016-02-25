package aggregation;

import java.util.List;

import viewmodel.ViewModel;
import dataStorage.GenotypeMatrix;

/**
 * Created by peltzer on 18.02.14.
 */
public class MaximumMatrixAggregator implements IMatrixAggregator {
    private int pat_A = 0;
    private int pat_T = 0;
    private int pat_C = 0;
    private int pat_G = 0;
    private int mat_A = 0;
    private int mat_T = 0;
    private int mat_C = 0;
    private int mat_G = 0;

	@Override
    public AggregationMatrixRow aggregate(ViewModel viewModel, Aggregation a) {
        GenotypeMatrix gm = viewModel.getDataSet().getMatrix();
        AggregationMatrixRow amr = new AggregationMatrixRow(viewModel.numColsInDataSet());
        List<Aggregation> aggregations = a.getElements();
        
        for (int i = 0; i < viewModel.numColsInDataSet(); i++){
    		for (Aggregation a2 : aggregations){
            	//since a2 is elementary, its index is equal to the subjects index
                int index = a2.getIndex();
                countFrequencies(gm.get(i, index));
            }
            char maxmat = getMaxMaternal();
            char maxpat = getMaxPaternal();
            amr.set(i,new char[]{maxpat, maxmat}, new double[]{getPaternalFrequency(maxpat), getMaternalFrequency(maxmat)});
            reset();
        }
        return amr;
    }

    private void reset() {
    	this.pat_A = 0;
    	this.pat_C = 0;
    	this.pat_G = 0;
    	this.pat_T = 0;
    	this.mat_A = 0;
    	this.mat_C = 0;
    	this.mat_G = 0;
    	this.mat_T = 0;
    }

    private void countFrequencies(char[] nuc){
        switch(nuc[0]) {
            case 'A':
                pat_A++;
                break;
            case 'T':
                pat_T++;
                break;
            case 'C':
                pat_C++;
                break;
            case 'G':
                pat_G++;
                break;
        }
        switch(nuc[1]){
            case 'A':
                mat_A++;
                break;
            case 'T':
                mat_T++;
                break;
            case 'C':
                mat_C++;
                break;
            case 'G':
                mat_G++;
                break;
        }
    }

    private char getMaxPaternal(){
       char out = 0;
       int max = Math.max(pat_A, Math.max(pat_C, Math.max(pat_G,pat_T)));
       if(max == pat_A){
         out = 'A';
       } else if (max == pat_C){
         out = 'C';
       } else if(max == pat_T){
         out = 'T';
       } else if(max == pat_G){
         out = 'G';
       }

       return out;
    }

    private char getMaxMaternal(){
        char out = 0;
        int max = Math.max(mat_A, Math.max(mat_C, Math.max(mat_G,mat_T)));
        if(max == mat_A){
            out = 'A';
        } else if (max == mat_C){
            out = 'C';
        } else if(max == mat_T){
            out = 'T';
        } else if(max == mat_G){
            out = 'G';
        }
        return out;
    }


    private double getPaternalFrequency(char which){
        double frequency = 0.0;
        double sum = pat_A+pat_C+pat_G+pat_T;
        switch(which){
            case 'A':
                frequency =  pat_A/sum;
                break;
            case 'C':
                frequency =   pat_C/sum;
                break;
            case 'T':
                frequency =   pat_T/sum;
                break;
            case 'G':
                frequency =   pat_G/sum;
                break;
        }
        return frequency;
    }

    private double getMaternalFrequency(char which){
        double frequency = 0.0;
        double sum = mat_A+mat_C+mat_G+mat_T;
        switch(which){
            case 'A':
                frequency =  mat_A/sum;
                break;
            case 'C':
                frequency =   mat_C/sum;
                break;
            case 'T':
                frequency =   mat_T/sum;
                break;
            case 'G':
                frequency =   mat_G/sum;
                break;
        }
        return frequency;
    }

}
