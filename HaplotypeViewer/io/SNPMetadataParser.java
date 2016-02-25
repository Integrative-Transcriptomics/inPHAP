package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;

import tasks.AbstractTask;
import dataStorage.DataSet;
import dataStorage.SNPMetaData;

/**
 * Created by peltzer on 3/10/14.
 */
public class SNPMetadataParser {
    private File f;
    private FileReader fr;
    private BufferedReader bfr;
    private DataSet dataSet;
    
    ArrayList<SNPMetaData> metadata;

    public SNPMetadataParser(String filepath) throws Exception {
        f = new File(filepath);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void readFile(AbstractTask abstractTask) throws Exception {
        String output = "";
        String firstHeaderLine = "";

        while ((output = bfr.readLine()) != null) {
            if (abstractTask.hasBeenCancelled()) {
                break;
            }
            if (output.startsWith("##")) {
                firstHeaderLine = output;
            } else if (output.startsWith("#")) {
                initializeHeader(output, firstHeaderLine);
            } else if (dataSet.containsSNP(output.split("\t")[0])) {
            	parseSNPMetaDataLine(output);
            }
        }
        bfr.close();

        this.dataSet.addSNPMetaData(metadata);
    }

    /**
     * Initializes the header and sets the types correspondingly
     *
     * @param output
     * @param fields
     * @throws Exception
     */
    private void initializeHeader(String output, String fields) throws Exception {
        String[] fieldNames = fields.split("\t");
        String[] fieldTypes = output.split("\t");
        this.metadata = new ArrayList<SNPMetaData>();

        for (int i = 2; i < fieldNames.length; i++) {
            if (fieldTypes[i].equals("Numeric")) {
                SNPMetaData data = new SNPMetaData();
                data.setName(fieldNames[i]);
                this.metadata.add(data);
            } else if (fieldTypes[i].equals("Categoric")) {
                SNPMetaData data = new SNPMetaData();
                data.setName(fieldNames[i]);
                this.metadata.add(data);
            } else {
                throw new ParseException("Can not parse this data. It should be either numeric or categoric." + fieldTypes[i], 1);
            }
        }
    }


    /**
     * Parses the actual Metadata Lines and passes them to the metadata set.
     *
     * @param s
     * @throws Exception
     */
    private void parseSNPMetaDataLine(String s) {
        String[] split = s.split("\t");
        String rsID = "";
        String annotation = "";
        rsID = split[0];

        int index = dataSet.getSNPIndex(rsID);

        for (int i = 1; i < split.length; i++) {
            SNPMetaData meta = metadata.get(i - 1);
            meta.add(index, split[i]);
        }
    }


    /**
     * Get the dataset
     */
    public DataSet getDataSet() {
        return this.dataSet;
    }
}
