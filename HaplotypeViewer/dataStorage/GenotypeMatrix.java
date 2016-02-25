package dataStorage;
import java.util.ArrayList;

/**
 * Created by peltzer on 10.02.14.
 * Modified by jaeger on 11.02.14
 * GenotypeMatrix Object with two fields (ArrayLists<T>) for holding the byte / boolean encoded data.
 * Initialization with <200,000>X x_dimensions Vector
 */
public class GenotypeMatrix {
    private ArrayList<byte[][]> byteArray; //For SNP Information
    private ArrayList<boolean[][]> boolArray; //For Phase information (1 = phased, 0 = unphased)

    private int numRows0 = 0;
    private int numCols = 0;
    private int numPhased = 0;
    private int numUnphased = 0;
    //for testing issues this is 2
    //increase to 2e5 later
    private static final int MAX_ROW = 200000;
    
    /**
     * 
     * @param x_dimensionality, usually the number of subjects in the dataset
     * data is represented in the genotype matrix according to the vcf file: columns = subjects, rows=snvs
     */
    public GenotypeMatrix(int x_dimensionality) {
        byteArray = new ArrayList<byte[][]>();
        byteArray.add(0, new byte[MAX_ROW][x_dimensionality]);
        boolArray = new ArrayList<boolean[][]>();
        boolArray.add(0, new boolean[MAX_ROW][x_dimensionality]);
        this.numCols = x_dimensionality; 
    }

    public int add(char SNV_paternal, char SNV_maternal, boolean phased, int snpIndex, int subjectIndex) {
    	int insertionIndex = snpIndex % MAX_ROW;
    	byte encoded = GenotypeCoding.encode(SNV_paternal, SNV_maternal);
    	//increase rows if necessary
    	numRows0 = Math.max(numRows0, snpIndex);
    	int lastIndex = numRows0 / MAX_ROW;
    	int numArrays = numArrays();
    	//insert into old matrix
    	if((lastIndex < numArrays) || numRows0 == 0) {
    		byteArray.get(lastIndex)[insertionIndex][subjectIndex] = encoded;
    		boolArray.get(lastIndex)[insertionIndex][subjectIndex] = phased;
    	} else { //construct a new matrix and insert into the new matrix
    		byte[][] byteTmp = new byte[MAX_ROW][numCols];
    		boolean[][] boolTmp = new boolean[MAX_ROW][numCols];
    		byteTmp[insertionIndex][subjectIndex] = encoded;
    		boolTmp[insertionIndex][subjectIndex] = phased;
    		byteArray.add(byteTmp);
    		boolArray.add(boolTmp);
    	}
    	
    	if(phased)
    		numPhased++;
    	else
    		numUnphased++;
    	
    	//return the insertion index
		return insertionIndex;
    }
    
    public char[] get(int snpIndex, int subjectIndex) {
    	int listIndex = snpIndex / MAX_ROW;
    	int rowIndex = snpIndex % MAX_ROW;
    	byte encoded = byteArray.get(listIndex)[rowIndex][subjectIndex];
    	char[] decoded = GenotypeCoding.decode(encoded); 
    	return decoded;
    }
    
    public boolean isPhased(int snpIndex, int subjectIndex) {
    	int listIndex = snpIndex / MAX_ROW;
    	int rowIndex = snpIndex % MAX_ROW;
    	boolean phased = boolArray.get(listIndex)[rowIndex][subjectIndex]; 
    	return phased;
    }
    
    public int numRows() {
    	return this.numRows0+1;
    }
    
    public int numCols() {
    	return this.numCols;
    }
    
    private int numArrays() {
    	return this.boolArray.size();
    }
    
    protected static class GenotypeCoding {
    	
    	public static byte encode(char SNV_paternal, char SNV_maternal) {
    		char SNV_p_upper = Character.toUpperCase(SNV_paternal);
    		char SNV_m_upper = Character.toUpperCase(SNV_maternal);
    		
    		//check all possible 16 cases of allele combinations
    		/*
    		 * enocding is as follows:
    		 * AA=A, AC=B, AT=C, AG=D
    		 * TA=E, TC=F, TT=G, TG=H
    		 * CA=I, CC=J, CT=K, CG=L
    		 * GA=M, GC=N, GT=O, GG=P
    		 * NA=a, NC=b, NT=c, NG=d
    		 * AN=e, CN=f, TN=g, GN=h
    		 * NN=i
    		 */
    		if(SNV_p_upper == 'A') {
    			if(SNV_m_upper == 'A') {
        			return 'A';
        		} else if(SNV_m_upper == 'C') {
        			return 'B';
        		} else if(SNV_m_upper == 'T') {
        			return 'C';
        		} else if(SNV_m_upper == 'G') {
        			return 'D';
        		} else if(SNV_m_upper == 'N') {
        			return 'e';
        		}
    		} else if(SNV_p_upper == 'C') {
    			if(SNV_m_upper == 'A') {
    				return 'I';
        		} else if(SNV_m_upper == 'C') {
        			return 'J';
        		} else if(SNV_m_upper == 'T') {
        			return 'K';
        		} else if(SNV_m_upper == 'G') {
        			return 'L';
        		} else if(SNV_m_upper == 'N') {
        			return 'f';
        		}
    		} else if(SNV_p_upper == 'T') {
    			if(SNV_m_upper == 'A') {
    				return 'E';
        		} else if(SNV_m_upper == 'C') {
        			return 'F';
        		} else if(SNV_m_upper == 'T') {
        			return 'G';
        		} else if(SNV_m_upper == 'G') {
        			return 'H';
        		} else if(SNV_m_upper == 'N') {
        			return 'g';
        		}
    		} else if(SNV_p_upper == 'G') {
    			if(SNV_m_upper == 'A') {
    				return 'M';
        		} else if(SNV_m_upper == 'C') {
        			return 'N';
        		} else if(SNV_m_upper == 'T') {
        			return 'O';
        		} else if(SNV_m_upper == 'G') {
        			return 'P';
        		} else if(SNV_m_upper == 'N') {
        			return 'h';
        		}
    		} else if(SNV_p_upper == 'N') {
    			if(SNV_m_upper == 'A') {
    				return 'a';
        		} else if(SNV_m_upper == 'C') {
        			return 'b';
        		} else if(SNV_m_upper == 'T') {
        			return 'c';
        		} else if(SNV_m_upper == 'G') {
        			return 'd';
        		} else if(SNV_m_upper == 'N') {
        			return 'i';
        		}
    		}
    		
    		throw new RuntimeException("No valid nucleotide provided for encoding: " 
    				+ SNV_p_upper + " : " + SNV_m_upper);
    	}
    	
    	public static char[] decode(byte encoding) {
    		switch(encoding) {
    		case 'A':
    			return new char[]{'A','A'};
    		case 'B':
    			return new char[]{'A','C'};
    		case 'C':
    			return new char[]{'A','T'};
    		case 'D':
    			return new char[]{'A','G'};
    		case 'E':
    			return new char[]{'T','A'};
    		case 'F':
    			return new char[]{'T','C'};
    		case 'G':
    			return new char[]{'T','T'};
    		case 'H':
    			return new char[]{'T','G'};
    		case 'I':
    			return new char[]{'C','A'};
    		case 'J':
    			return new char[]{'C','C'};
    		case 'K':
    			return new char[]{'C','T'};
    		case 'L':
    			return new char[]{'C','G'};
    		case 'M':
    			return new char[]{'G','A'};
    		case 'N':
    			return new char[]{'G','C'};
    		case 'O':
    			return new char[]{'G','T'};
    		case 'P':
    			return new char[]{'G','G'};
    		case 'a':
    			return new char[]{'A','N'};
    		case 'b':
    			return new char[]{'T', 'N'};
    		case 'c':
    			return new char[]{'C', 'N'};
    		case 'd':
    			return new char[]{'G', 'N'};
    		case 'e':
    			return new char[]{'N', 'A'};
    		case 'f':
    			return new char[]{'N', 'T'};
    		case 'g':
    			return new char[]{'N', 'C'};
    		case 'h':
    			return new char[]{'N', 'G'};
    		case 'i':
    			return new char[]{'N', 'N'};
    		}
    		
    		throw new RuntimeException("the byte " 
    				+ encoding + " is not a valid encoding character");
    	}
    }
    
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	
    	for(int i = 0; i < numRows(); i++) {
    		for(int j = 0; j < numCols; j++) {
    			if(j != 0)
					sb.append(" ");
    			char[] decoded = get(i, j);
    			sb.append(decoded[0] + "|" + decoded[1]);
    		}
    		sb.append("\n");
    	}
    	
    	return sb.toString();
    }
    
    /*
     * just for testing issues, can be removed later
     */
    public static void main(String[] args) {
    	byte b = GenotypeCoding.encode('A', 'G');
    	char[] decoded = GenotypeCoding.decode(b);
    	
    	System.out.println(b);
    	System.out.println("Genotype: " + decoded[0] + " : " + decoded[1]);
    	
    	GenotypeMatrix matrix = new GenotypeMatrix(2);
    	matrix.add('A', 'A', true, 0, 0);
    	matrix.add('T', 'T', true, 0, 1);
    	matrix.add('A', 'T', false, 1, 0);
    	matrix.add('A', 'T', true, 1, 1);
    	matrix.add('T', 'A', true, 2, 0);
    	matrix.add('T', 'A', false, 2, 1);
    	matrix.add('T', 'T', false, 3, 0);
    	matrix.add('T', 'T', false, 3, 1);
    	System.out.println("Matrix: ");
    	System.out.println(matrix.toString());
    	
    	System.out.println("Num Arrays: " + matrix.numArrays());
    	System.out.println("Row Count: " + matrix.numRows());
    	System.out.println("Col Count: " + matrix.numCols());
    }

	public int numPhased() {
		return numPhased;
	}
	
	public int numUnphased() {
		return numUnphased;
	}
}
