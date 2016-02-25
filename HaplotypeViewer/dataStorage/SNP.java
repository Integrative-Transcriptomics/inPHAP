package dataStorage;

/**
 * Created by peltzer on 12.02.14.
 */
public class SNP {
    private final int index;
    private String rsid;
    private int chrompos;
    private char reference;
    private String chrom;

    public SNP(int index, String rsid, int chrompos, char reference, String chromosome){
        this.index = index;
        this.rsid = rsid;
        this.chrompos = chrompos;
        this.reference = reference;
        this.chrom = chromosome;
    }

    public int getIndex() {
        return index;
    }

    public String getRsid() {
    	if(rsid.equals(".")) {
    		return chrom + Integer.toString(chrompos);
    	}
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

    public int getChrompos() {
        return chrompos;
    }

    public void setChrompos(int chrompos) {
        this.chrompos = chrompos;
    }
    
    public char getReference() {
    	return this.reference;
    }
    
    public String getChromosome() {
    	return this.chrom;
    }
}
