package dataStorage;

import java.util.ArrayList;

import viewmodel.ViewModel;
import visualization.utilities.BidirectionalHashMap;
import events.ViewModelEvent;

public class DataSet {

	private GenotypeMatrix matrix;
    private ArrayList<Person> Person;
    private BidirectionalHashMap<String, Integer> subjectIDMapping = new BidirectionalHashMap<String, Integer>();
    private BidirectionalHashMap<String, Integer> snpPosMapping = new BidirectionalHashMap<String, Integer>();
    private ArrayList<SNP> snps;
    private BidirectionalHashMap<String, Integer> snpIDMapping = new BidirectionalHashMap<String, Integer>();
    private ArrayList<IMetaData> subjectMetaData;
    private ArrayList<SNPMetaData> snpMetaData;
    
    private ViewModel viewModel;

    public DataSet() {
        this.matrix = null;
        this.Person = new ArrayList<Person>();
        this.snps = new ArrayList<SNP>();
        this.subjectMetaData = new ArrayList<IMetaData>();
        this.snpMetaData = new ArrayList<SNPMetaData>();
    }
    
    public void setViewModel(ViewModel viewModel) {
    	this.viewModel = viewModel;
    }

    public void setMatrix(GenotypeMatrix matrix) {
		this.matrix = matrix;
	}
	
	public GenotypeMatrix getMatrix() {
		return this.matrix;
	}

    public ArrayList<IMetaData> getListofMetaData() {
        return subjectMetaData;
    }

    public void setListofMetaData(ArrayList<IMetaData> listofMetaData) {
        this.subjectMetaData = listofMetaData;
    }

    public void setListOfSNPMetaData(ArrayList<SNPMetaData> listOfSNPMetaData) {this.snpMetaData = listOfSNPMetaData;}

    public ArrayList<SNPMetaData> getListofSNPMetaData() { return snpMetaData;}
	
	public int getNumSNVs() {
		return this.matrix.numRows();
	}
	
	public int getNumSubjects() {
		return this.matrix.numCols();
	}

	public int getNumPhased() {
		return this.matrix.numPhased();
	}
	
	public int getNumUnphased() {
		return this.matrix.numUnphased();
	}

    public ArrayList<Person> getSubjects() {
        return Person;
    }

    public void setSubjects(ArrayList<Person> subjects) {
        Person = subjects;
        for(Person p : subjects) {
        	subjectIDMapping.put(p.getName(), p.getIndex());
        }
    }
    
    public int getSubjectIndex(String name) {
    	return this.subjectIDMapping.get(name);
    }
    
    public int getSNPIndex(String id) {
    	Integer index = snpIDMapping.getLeft(id); 
    	if(index != null) {
    		return index;
    	} else {
    		viewModel.writeLogMessage("SNV with id=" + id + " could not be found!");
    		return -1;
    	}
    }
    
    public boolean containsSubject(String subjectName) {
    	return this.subjectIDMapping.get(subjectName) != null;
    }
    
    public boolean containsSNP(String snpID) {
    	return this.snpIDMapping.get(snpID) != null;
    }

    public ArrayList<SNP> getSNPs() {
        return snps;
    }

    public void setSNPs(ArrayList<SNP> snps) {
        this.snps = snps;
        for(SNP s : snps) {
        	this.snpIDMapping.put(s.getRsid(), s.getIndex());
        	this.snpPosMapping.put(s.getRsid(), s.getChrompos());
        }
    }

	public void addSubjectMetaData(ArrayList<IMetaData> metadata) {
		this.subjectMetaData.addAll(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SUBJECT_META_INFO_CHANGED, "Subject Meta-Information added"));
	}
	
	public void addSubjectMetaData(IMetaData metadata) {
		this.subjectMetaData.add(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SUBJECT_META_INFO_CHANGED, "Subject Meta-Information added"));
	}
	
	public void addSNPMetaData(ArrayList<SNPMetaData> metadata) {
		this.snpMetaData.addAll(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SNP_META_INFO_CHANGED, "SNP Meta-Information added"));
	}
	
	public void addSNPMetaData(SNPMetaData metadata) {
		this.snpMetaData.add(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SNP_META_INFO_CHANGED, "SNP Meta-Information added"));
	}
	
	public void removeSNPMetaData(IMetaData metadata) {
		this.snpMetaData.remove(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SNP_META_INFO_CHANGED, "SNP Meta-Information removed"));
	}
	
	public void removeSNPMetaData(int index) {
		this.snpMetaData.remove(index);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SNP_META_INFO_CHANGED, "SNP Meta-Information removed"));
	}
	
	public void removeSubjectMetaData(IMetaData metadata) {
		this.subjectMetaData.remove(metadata);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SUBJECT_META_INFO_CHANGED, "Subject Meta-Information removed"));
	}
	
	public void removeSubjectMetaData(int index) {
		this.subjectMetaData.remove(index);
		this.viewModel.fireChanged(new ViewModelEvent(this, 
				ViewModelEvent.SUBJECT_META_INFO_CHANGED, "Subject Meta-Information removed"));
	}

	public int getNumMetaCols() {
		return subjectMetaData.size();
	}

	public int getNumMetaRows() {
		return snpMetaData.size();
	}

	public String getMetaColHeader(int metaColumn) {
		if(this.subjectMetaData.size() > metaColumn)
			return this.subjectMetaData.get(metaColumn).getName();
		return "";
	}
	
	public String getMetaRowHeader(int metaRow) {
		if(this.snpMetaData.size() > metaRow)
			return this.snpMetaData.get(metaRow).getName();
		return "";
	}

	public IMetaData getMetaColumn(int column) {
		if(subjectMetaData.size() > column)
			return this.subjectMetaData.get(column);
		return null;
	}

	public SNPMetaData getMetaRow(int row) {
		if(snpMetaData.size() > row)
			return snpMetaData.get(row);
		return null;
	}

	public int getSNPByPosition(int snpPos) {
		Integer pos = this.snpPosMapping.getRight(snps);
		if(pos != null) {
			return pos;
		} else {
			viewModel.writeLogMessage("No SNV was found at position = " + Integer.toString(snpPos));
			return 0;
		} 
	}

	public void addSNP(SNP snp) {
		this.snps.add(snp);
		this.snpIDMapping.put(snp.getRsid(), snp.getIndex());
		this.snpPosMapping.put(snp.getRsid(), snp.getChrompos());
	}

	public Integer getSubjectByName(String value) {
		Integer index = subjectIDMapping.getLeft(value);
    	if(index != null) {
    		return index;
    	} else {
    		viewModel.writeLogMessage("Subject with name=" + value + " could not be found!");
    		return 0;
    	}
	}
}
