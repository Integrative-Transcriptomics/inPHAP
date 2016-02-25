package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import events.ViewModelEvent;

public class DataSetSummaryPanel extends JPanel implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5210242524411665580L;
	
	private ViewModel viewModel;
	
	private JLabel numSHeader;
	private JLabel numSNVHeader;
	private JLabel numPhasedHeader;
	private JLabel numUnphasedHeader;
	private JLabel numMetaInfoColsHeader;
	private JLabel numMetaInfoRowsHeader;
	
	private JLabel numS;
	private JLabel numSNV;
	private JLabel numPhased;
	private JLabel numUnphased;
	private JLabel numMetaInfoCols;
	private JLabel numMetaInfoRows;
	
	public DataSetSummaryPanel(ViewModel viewModel) {
		this.setLayout(new GridLayout(6,2));
		this.setBorder(BorderFactory.createTitledBorder("DataSet Summary"));
		this.setPreferredSize(new Dimension(150, 150));
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		this.initializeComponents();
	}
	
	private void initializeComponents() {
		this.numSHeader = new JLabel("# Subjects:");
		this.numPhasedHeader = new JLabel("# Phased:");
		this.numSNVHeader = new JLabel("# SNVs:");
		this.numUnphasedHeader = new JLabel("# Unphased:");
		this.numMetaInfoColsHeader = new JLabel("# MI Cols:");
		this.numMetaInfoRowsHeader = new JLabel("# MI Rows");
		
		this.numS = new JLabel("");
		this.numSNV = new JLabel("");
		this.numPhased = new JLabel("");
		this.numUnphased = new JLabel("");
		this.numMetaInfoCols = new JLabel("");
		this.numMetaInfoRows = new JLabel("");
		
		add(this.numSHeader);
		add(this.numS);
		add(this.numSNVHeader);
		add(this.numSNV);
		add(this.numPhasedHeader);
		add(this.numPhased);
		add(this.numUnphasedHeader);
		add(this.numUnphased);
		add(this.numMetaInfoColsHeader);
		add(this.numMetaInfoCols);
		add(this.numMetaInfoRowsHeader);
		add(this.numMetaInfoRows);
	}
	
	public void update() {
		this.numPhased.setText(
				Integer.toString(this.viewModel.getDataSet().getNumPhased()));
		this.numS.setText(
				Integer.toString(this.viewModel.getDataSet().getNumSubjects()));
		this.numSNV.setText(
				Integer.toString(this.viewModel.getDataSet().getNumSNVs()));
		this.numUnphased.setText(
				Integer.toString(this.viewModel.getDataSet().getNumUnphased()));
		this.numMetaInfoCols.setText(Integer.toString(this.viewModel.getDataSet().getNumMetaCols()));
		this.numMetaInfoRows.setText(Integer.toString(this.viewModel.getDataSet().getNumMetaRows()));
		this.revalidate();
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		case ViewModelEvent.DATASET_CHANGED:
			update();
			break;
		case ViewModelEvent.SUBJECT_META_INFO_CHANGED:
			update();
			break;
		case ViewModelEvent.SNP_META_INFO_CHANGED:
			update();
			break;
		}
	}
}
