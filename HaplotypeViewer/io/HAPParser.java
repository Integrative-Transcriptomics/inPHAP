package io;

import dataStorage.*;
import tasks.AbstractTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by peltzer on 3/4/14.
 * modified by jaeger on 8.3.2014
 */
public class HAPParser implements IDataParser {
	
    private File legendfile;
    private FileReader legendaryfilereader;
    private BufferedReader legendarybufferedreader;
    private File samplefile;
    private FileReader samplefilereader;
    private BufferedReader samplefilebufferedreader;
    private File hapfile;
    private FileReader hapfilereader;
    private BufferedReader hapfilebufferedreader;
    private DataSet dataSet = new DataSet();
    private SNPMetaData pmSNPInfo;
    
    private String chromosome;

    public HAPParser(String legendFilepath, String sampleFilepath, String hapFilepath, String chromosome) throws IOException {
        legendfile = new File(legendFilepath);
        legendaryfilereader = new FileReader(legendfile);
        legendarybufferedreader = new BufferedReader(legendaryfilereader);
        
        samplefile = new File(sampleFilepath);
        samplefilereader = new FileReader(samplefile);
        samplefilebufferedreader = new BufferedReader(samplefilereader);
        
        hapfile = new File(hapFilepath);
        hapfilereader = new FileReader(hapfile);
        hapfilebufferedreader = new BufferedReader(hapfilereader);
        
        this.chromosome = chromosome;
    }

    @Override
    public void readFile(AbstractTask abstractTask) throws Exception {
        String currLineLegend = "";
        int counter = 0;

        //First initialize everything
        int numSubjects = initializeMatrix();
        
        abstractTask.writeLog("Number of subjects = " + Integer.toString(numSubjects));
        
        this.dataSet.setMatrix(new GenotypeMatrix(numSubjects));
        
        boolean headerline = true;

        while ((currLineLegend = legendarybufferedreader.readLine()) != null){
            //ID position a0 a1
            //rs190131231 154933384 G A , check for starting with "rs", otherwise skip!
        	
        	//FIXME the file can also contain snps without an rsid!
        	
        	//see file format description at:
        	//http://mathgen.stats.ox.ac.uk/impute/README_1000G_phase1integrated_v3.txt
            if(abstractTask.hasBeenCancelled()){
              break;
            }
            
            if(headerline) {
            	headerline = false;
            	continue;
            }
            
            //since we always want the same line in both files, we have to read the line
            //before we skip any non-snvs line!
            String currLineHap = hapfilebufferedreader.readLine();
            
            //hap file size differs from legend file size!!
            if(currLineHap == null) {
            	throw new IOException("Number of lines in legend file and hap file differ!");
            }

            //
            if(!currLineLegend.startsWith("rs")){
                continue;
            }
            
            String[] legendsplit = currLineLegend.split(" ");
            
            String rsID = legendsplit[0];
            Integer position = Integer.parseInt(legendsplit[1]);
            char reference = legendsplit[2].charAt(0);
            char alternative = legendsplit[3].charAt(0);
            
            //create a new snp and add it to the dataset
            SNP s = new SNP(counter, rsID, position, reference, chromosome);
            this.dataSet.addSNP(s);
            //add p/m meta info to the metainfo object
            pmSNPInfo.add("Paternal\tMaternal");

            //increase snp index counter
            counter++;
            
            //TODO read in frequency some time later
            
            fillGenotypeMatrix(currLineHap, reference, alternative, s.getIndex());
        }
    }
    
    /*
     * parsing line according to the description at:
     * https://mathgen.stats.ox.ac.uk/impute/impute_v2.html#home
     */
    private void fillGenotypeMatrix(String currlineHap, char reference,
			char alternative, int snpIndex) {
    	GenotypeMatrix m = this.dataSet.getMatrix();
    	
    	String[] split = currlineHap.split(" ");
    	
    	char pat = 0;
    	char mat = 0;
    	
    	for(int i = 0; i < split.length; i+=2) {
    		//paternal is in second column
    		char pat_temp = split[i+1].charAt(0);
    		//maternal is in first column
    		char mat_temp = split[i].charAt(0);
    		
    		switch(pat_temp) {
    		case '0':
    			pat = reference;
    			break;
    		case '1':
    			pat = alternative;
    			break;
    		case '-':
    			pat = '-';
    			break;
    		default:
    			pat = reference;
    		}
    		
    		switch(mat_temp) {
    		case '0':
    			mat = reference;
    			break;
    		case '1':
    			mat = alternative;
    			break;
    		case '-':
    			mat = '-';
    			break;
    		default:
    			mat = reference;
    		}
    		
//    		System.out.println(mat + ", " + pat + ", " + reference + ", " + alternative);
    		
    		int subjectIndex = i / 2;
    		
    		m.add(pat, mat, true, snpIndex, subjectIndex);
    	}
	}

    private int initializeMatrix() throws Exception {
        String currLineSample = null;
        int count = 0;
        boolean headerline = true;
        ArrayList<Person> persons = new ArrayList<Person>();
        
        //initialize phased meta-info
        this.pmSNPInfo = new SNPMetaData("P/M");
        
        while((currLineSample = samplefilebufferedreader.readLine()) != null) {
        	//skip empty lines
        	if(currLineSample.trim().length() == 0) {
        		continue;
        	}
        	//skip header line
            if(headerline) {
            	headerline = false;
                continue;
            }
            
            String[] splitSamples = currLineSample.split(" ");
            persons.add(new Person(count, splitSamples[0]));
            count++;
        }
        
        this.dataSet.setSubjects(persons);
        return count; //Number of non-empty lines in SAMPLE file == number of persons!
    }

    /**
     * Returns the Dataset
     *
     * @return
     */
    public DataSet getDataSet() {
        return this.dataSet;
    }

    public SNPMetaData getPMSNPMeta() {
        return this.pmSNPInfo;
    }
}
