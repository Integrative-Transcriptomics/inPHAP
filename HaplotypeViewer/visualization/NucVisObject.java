package visualization;

public class NucVisObject {

	private char c1;
	private double f1;
	private double f2;
	
	private char c2;
	
	public NucVisObject(char c1, double f1) {
		this.c1 = c1;
		this.f1 = f1;
	}
	
	public NucVisObject(char c1, char c2, double f1, double f2) {
		this.c1 = c1;
		this.c2 = c2;
		this.f1 = f1;
		this.f2 = f2;
	}
	
	public char getChar() {
		return this.c1;
	}
	
	public char getCharPat() {
		return this.c1;
	}
	
	public char getCharMat() {
		return this.c2;
	}
	
	public double getFrequency() {
		return this.f1;
	}
	
	public double getMeanFrequency() {
		return (this.f1 + this.f2)/2.;
	}
}
