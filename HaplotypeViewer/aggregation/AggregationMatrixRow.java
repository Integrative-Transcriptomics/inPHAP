package aggregation;

public class AggregationMatrixRow {
	char[][] nucleotides;
	double[][] frequencies;
	
	public AggregationMatrixRow(int numSNPs) {
		//TODO use same encoding as in the matrix?
		this.nucleotides = new char[numSNPs][2];
		this.frequencies = new double[numSNPs][2];
	}
	
	public void set(int index, char[] c, double[] f) {
		this.nucleotides[index] = c;
		this.frequencies[index] = f;
	}
	
	public char[] getNucleotide(int index) {
		return this.nucleotides[index];
	}
	
	public double[] getFrequency(int index) {
		return this.frequencies[index];
	}
}
