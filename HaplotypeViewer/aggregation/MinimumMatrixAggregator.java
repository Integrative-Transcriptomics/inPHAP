package aggregation;

import java.util.List;

import viewmodel.ViewModel;
import dataStorage.GenotypeMatrix;

/**
 * Created by peltzer on 19.02.14.
 */
public class MinimumMatrixAggregator implements IMatrixAggregator {

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
        List<Aggregation> aggregations = a.getElements();
        AggregationMatrixRow amr = new AggregationMatrixRow(viewModel.numColsInDataSet());
        for (int i = 0; i < viewModel.numColsInDataSet(); i++){
            for (Aggregation a2 : aggregations){
            	//since a2 is elementary, its index is equal to the corresponding subjects index
                int index = a2.getIndex();
                countFrequencies(gm.get(i, index));
            }
            char minmat = getMinMaternal();
            char minpat = getMinPaternal();
            amr.set(i,new char[]{minpat, minmat}, new double[]{1 - getPaternalFrequency(minpat), 1 - getMaternalFrequency(minmat)});
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

    private char getMinPaternal(){
        char out = 0;
        
        if(pat_A == 0)
        	pat_A = Integer.MAX_VALUE;
        if(pat_T == 0)
        	pat_T = Integer.MAX_VALUE;
        if(pat_G == 0)
        	pat_G = Integer.MAX_VALUE;
        if(pat_C == 0)
        	pat_C = Integer.MAX_VALUE;
        
        int max = Math.min(pat_A, Math.min(pat_C, Math.min(pat_G,pat_T)));
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

    private char getMinMaternal(){
        char out = 0;
        
        if(mat_A == 0)
        	mat_A = Integer.MAX_VALUE;
        if(mat_T == 0)
        	mat_T = Integer.MAX_VALUE;
        if(mat_G == 0)
        	mat_G = Integer.MAX_VALUE;
        if(mat_C == 0)
        	mat_C = Integer.MAX_VALUE;
        
        int max = Math.min(mat_A, Math.min(mat_C, Math.min(mat_G,mat_T)));
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
