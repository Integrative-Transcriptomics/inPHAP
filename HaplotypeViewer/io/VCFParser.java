package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tasks.AbstractTask;
import dataStorage.DataSet;
import dataStorage.GenotypeMatrix;
import dataStorage.Person;
import dataStorage.SNP;
import dataStorage.SNPMetaData;

/**
 * Created by peltzer on 10.02.14.
 * Class used to parse a VCFEntry
 */
public class VCFParser implements IDataParser {
	private File f;
    private FileReader fr;
    private BufferedReader bfr;
    private DataSet dataSet = new DataSet();
    private SNPMetaData pmSNPInfo;

    public VCFParser(String filepath) throws IOException {
    	f = new File(filepath);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);
    }

    public void readFile(AbstractTask abstractTask) throws Exception {
        String output = "";
        int counter = 0;

        while ((output = bfr.readLine()) != null) {
            if(abstractTask.hasBeenCancelled()){
                break;
            }

            if (output.startsWith("##")) {     //These are just comments, we don't need them right now
                //counter++;
                continue;
            } else if (output.startsWith("#")) {   //This is a descriptor, informing us about the # of people in the VCF.
                //counter++;
                initializeMatrix(output);
            } else {
                String[] infoAlt = parseVCFLine(output, counter);    //Regular SNP Line -> We need this.
                if(checkIfSNP(infoAlt[0], infoAlt[1])){
                    counter++;
                }
            }
        }

        bfr.close();
    }


    /**
     * This method uses the header line to initialize our Matrix in <X> dimensions, as we know now how many people
     * we are going to elaborate
     * * @param headerdescription
     */

    private void initializeMatrix(String headerdescription) {
        String[] headerSplit = headerdescription.split("\t");
        int x_axis = headerSplit.length - 9;
        this.dataSet.setMatrix(new GenotypeMatrix(x_axis));
        ArrayList<Person> persons = new ArrayList<Person>();
        for (int i = 9; i < headerSplit.length; i++){
           persons.add(new Person(i-9, headerSplit[i]));
        }
        this.dataSet.setSubjects(persons);
        this.pmSNPInfo = new SNPMetaData("P/M");
    }

    /**
     * VCFParser parsing a single VCF Line.
     *
     * @param output
     */

    private String[] parseVCFLine(String output, int counter) throws Exception {
        String[] splitArray = output.split("\t");

        //All the information of each line
        //This is the fixed content of each row in the VCF 4.1 File Format Specification
        String chromosome = splitArray[0].trim();
        String position = splitArray[1].trim();
        String id = splitArray[2].trim();
        String ref = splitArray[3].trim();
        String alt = splitArray[4].trim();
        //String qual = splitArray[5].trim();
        //String filter = splitArray[6].trim();
        String info = splitArray[7].trim();
        String format = splitArray[8].trim();
        
        if(id.equals(".")) {
        	id = chromosome + ":" + position;
        }

        //Adds SNPs to our list if applicable
        if(checkIfSNP(info, alt)) {
        	this.dataSet.addSNP(new SNP(counter, id, Integer.parseInt(position),ref.charAt(0), chromosome));
        }

        //Format Field needs to provide us the index of our GT information
        String[] GT_split = format.split(":");
        int GTindex = 0;
        for (int i = 0; i < GT_split.length; i++){
            if(GT_split[i].contains("GT")){
              GTindex=i;
                break;
            }
        }


        //And now the "important" information,dynamic depending on the input file

        boolean phased = true;
        
        for (int i = 9; i < splitArray.length; i++) {
            String[] SNVInfo = decodeVCFType(splitArray[i], GTindex);
            //ALL Info here, now pass this to the GenotypeMatrix
            if(checkIfSNP(info, alt)){   //Check if we're dealing with a SNP, DEL, INS first and only continue with the prior
            	if(Boolean.parseBoolean(SNVInfo[2]) == false) {
            		phased = false;
            	}
                this.dataSet.getMatrix().add(getAlleleValue(SNVInfo[0], ref, alt),
                        getAlleleValue(SNVInfo[1], ref, alt),
                        Boolean.parseBoolean(SNVInfo[2]),
                        counter, i - 9);
            }
        }
        
        //add paternal/maternal snp meta information
        if(phased) {
        	pmSNPInfo.add("Paternal:Maternal");
        } else {
        	pmSNPInfo.add("Unknown:Unknown");
        }

        return new String[]{info, alt};
    }

    /**
     * Parse an VCF Person Type
     */

    private String[] decodeVCFType(String rawinput, int GTIndex) throws Exception {
        String[] decodedGTInformation = new String[5];
        String[] rawSplit = rawinput.split(":");
        //Now get the GT information
        String GTInfo = rawSplit[GTIndex];

        //Determine first if it's a phased haplotype
        boolean isPhased = false;
        if (GTInfo.contains("|")) {
            isPhased = true;
        }
        //Now split according to our information ;-)
       if (isPhased) {
            String[] rawGTSplit = GTInfo.split("\\|");
            String GT_paternal = rawGTSplit[0];
            String GT_maternal = rawGTSplit[1];
            //Setting everything correctly now
            decodedGTInformation[0] = GT_paternal;
            decodedGTInformation[1] = GT_maternal;
            decodedGTInformation[2] = "true";
        } else {
            String[] rawGTSplit = GTInfo.split("/");
           if(rawGTSplit.length > 1){
               String GT_paternal = rawGTSplit[0];
               String GT_maternal = rawGTSplit[1];
               //Setting everything correctly now
               decodedGTInformation[0] = GT_paternal;
               decodedGTInformation[1] = GT_maternal;
               decodedGTInformation[2] = "false";
           } else {
               String GT_paternal = rawGTSplit[0];
               String GT_maternal = rawGTSplit[0];
               //Setting everything correctly now
               decodedGTInformation[0] = GT_paternal;
               decodedGTInformation[1] = GT_maternal;
               decodedGTInformation[2] = "false";

           }

        }

        return decodedGTInformation;
    }

    /**
     * Returning the correct allele values as listed
     * in VCF 4.1 format description.
     * <p/>
     * "The allele values are 0 for the reference allele (what is in the REF field),
     * 1 for the first allele listed in ALT, 2 for the second allele list in ALT and so on."
     *
     * @param genotype
     * @param ref
     * @param alt
     * @return
     */

    private char getAlleleValue(String genotype, String ref, String alt) {
        if(genotype.equals(".")) { //unspecified genotype, missing information
        	return 'N';
        }
        
    	int GT = Integer.parseInt(genotype);       
        char[] ref_chars = ref.toCharArray();
        String[] altSplit = alt.split(",");
        if (GT == 0) {
            return ref_chars[0];
        } else {
            return altSplit[GT - 1].charAt(0);
        }

    }

    /**
     * Returns the Dataset
     *
     * @return
     */
    public DataSet getDataSet() {
        return this.dataSet;
    }

    /**
     * Only adds the input to matrix if we're not dealing with insertions or deletions
     * @param alt 
     */

    private boolean checkIfSNP(String INFO, String alt) {
        if(INFO.contains("VT=INDEL")){
            return false;
        } else if(INFO.contains("VT=INS")){
            return false;
        } else if(INFO.contains("VT=DEL")){
            return false;
        } else if(INFO.contains("VT=SNP")){
            return true;
        }
        
        //the info field did not provide the necessary information, check the alt field instead
        
        String[] split = alt.split(",");
        
        for(int i = 0; i < split.length; i++) {
        	if(split[i].length() > 1)
        		return false;
        }
        
        return true;
    }

	public SNPMetaData getPMSNPMeta() {
		return this.pmSNPInfo;
	}
}
