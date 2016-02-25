package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tasks.AbstractTask;
import dataStorage.CategoryData;
import dataStorage.DataSet;
import dataStorage.IMetaData;
import dataStorage.NumericData;

/**
 * Created by peltzer on 13.02.14.
 */
public class SubjectMetadataParser {
    private File f;
    private FileReader fr;
    private BufferedReader bfr;
    private DataSet dataSet;

    public SubjectMetadataParser(String filepath) throws Exception {
        f = new File(filepath);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);
    }
    
    public void setDataSet(DataSet dataSet) {
    	this.dataSet = dataSet;
    }

    /**
     * Reads the input file. Expects a metadata PED file with following header specification:
     * ##\t <FIELDNAME1>\t<FIELDNAME2>\t....
     * #\t Type \t Type, where Type is either "Categoric" or "Numeric".
     * @param abstractTask
     * @throws Exception
     */
    public void readFile(AbstractTask abstractTask) throws Exception {
        String output = "";
        String firstHeaderLine = "";
        ArrayList<String> lines = new ArrayList<String>();

        while ((output = bfr.readLine()) != null) {
            if (abstractTask.hasBeenCancelled()) {
                break;
            }
            if (output.startsWith("##")) {
                firstHeaderLine = output;
            } else if (output.startsWith("#")) {
                initializeHeader(output, firstHeaderLine);
            } else {
            	if(dataSet.containsSubject(output.split("\t")[0])) {
            		lines.add(output);
            	}
            }
        }
        bfr.close();

        Collections.sort(lines, new MetaComparator(this.getDataSet()));
        //This has to be fixed!!
        for(String s : lines){
            parseMetaDataLine(s);
        }
    }

    /**
     * Initializes the header and sets the types correspondingly
     *
     * @param output
     * @param fields
     * @throws Exception
     */
    private void initializeHeader(String output, String fields) throws Exception{
        String[] fieldNames = fields.split("\t");
        String[] fieldTypes = output.split("\t");
        ArrayList<IMetaData> listofmetadata = new ArrayList<IMetaData>(this.dataSet.getNumSubjects());

        for (int i = 2; i < fieldNames.length; i++){
            if(fieldTypes[i].equals("Numeric")){
                NumericData data = new NumericData(this.dataSet.getNumSubjects());
                data.setName(fieldNames[i]);
                listofmetadata.add(data);
            } else if(fieldTypes[i].equals("Categoric")){
                CategoryData data = new CategoryData(this.dataSet.getNumSubjects());
                data.setName(fieldNames[i]);
                listofmetadata.add(data);
            } else {
                throw new ParseException("Can not parse this data. It should be either numeric or categoric." + fieldTypes[i], 1);
            }
        }
        this.dataSet.setListofMetaData(listofmetadata);

    }

    /**
     * Parses the actual Metadata Lines and passes them to the metadata set.
     * @param output
     * @throws Exception
     */
    private void parseMetaDataLine(String output) throws Exception {
        String[] outputSplit = output.split("\t");
        String name = outputSplit[0];
        int index = dataSet.getSubjectIndex(name);

        ArrayList<IMetaData> metadata = this.dataSet.getListofMetaData();

        for(int i = 1; i < outputSplit.length; i++){
           IMetaData meta = metadata.get(i-1);
           meta.add(index, outputSplit[i]);
        }
    }

    /**
     * Get the dataset
     *
     */
    public DataSet getDataSet(){
        return this.dataSet;
    }

    private class MetaComparator implements Comparator<String>{
        private DataSet dataSet;

        private MetaComparator(DataSet dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public int compare(String a, String b) {
            String personAid = a.split("\t")[0];
            String personBid = b.split("\t")[0];

            return Integer.compare(dataSet.getSubjectIndex(personAid), 
            		dataSet.getSubjectIndex(personBid));
        }
    }
}
