package io;

import dataStorage.DataSet;
import dataStorage.SNPMetaData;
import tasks.AbstractTask;

public interface IDataParser {
	public DataSet getDataSet();
	public SNPMetaData getPMSNPMeta();
    public void readFile(AbstractTask task) throws Exception;
}
