package filtering.filter;

import java.util.ArrayList;
import java.util.List;

import viewmodel.ViewModel;
import dataStorage.GenotypeMatrix;
import dataStorage.SNP;

public class RarityFilter implements Filter {

	private double threshold = 0.05;
	private boolean greater = false;
	private ViewModel viewModel;
	
	public RarityFilter(ViewModel viewModel, double threshold, boolean greater) {
		this.threshold = threshold;
		this.greater = greater;
		this.viewModel = viewModel;
	}
	
	@Override
	public List<SNP> applyFilter(List<SNP> unfiltered) {
		List<SNP> filtered = new ArrayList<SNP>();
		
		for(int i = 0; i < unfiltered.size(); i++) {
			SNP s = unfiltered.get(i);
			
			int index = s.getIndex();
			char ref = s.getReference();
			
			double freq = calculateFrequency(viewModel.getDataSet().getMatrix(), index, ref);
			
			if(this.greater) {
				if(freq >= threshold) {
					filtered.add(s);
				}
			} else {
				if(freq <= threshold) {
					filtered.add(s);
				}
			}
		}
		
		return filtered;
	}

	private double calculateFrequency(GenotypeMatrix matrix, int index, char ref) {
		int freq = 0;
		
		for(int i = 0; i < matrix.numCols(); i++) {
			char[] patMat = matrix.get(index, i);
			for(char c : patMat) {
				if(c != ref) {
					freq += 1;
				}
			}
		}
		
		double res = freq / ((double)matrix.numCols() * 2.);
		return res;
	}

	@Override
	public String getName() {
		return "SNV Frequency";
	}
}
