package gui;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;

import events.ViewModelEvent;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.SNPMap;

public class HVMainPanel extends JPanel implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3677827912776676938L;
	
	public HVMenuBar menuBar;
	public HVInfoBar infoBar;
	public HVVisPanel visPanel;
	public HVVisOptionsPanel optionsPanel;
	public DataSetSummaryPanel summaryPanel;
	public QuickButtonPanel buttonPanel;
	public HVLogWindow logWindow;
	
	private ViewModel viewModel;
	
	public HVMainPanel(Window owner) {
		this.setLayout(new BorderLayout());
		this.viewModel = new ViewModel(owner);
		this.viewModel.setMainPanel(this);
		this.viewModel.addViewModelListener(this);
		
		this.menuBar = new HVMenuBar(owner, viewModel);
		this.visPanel = new HVVisPanel(viewModel);
		this.infoBar = new HVInfoBar(viewModel);
		this.optionsPanel = new HVVisOptionsPanel(viewModel);
		this.summaryPanel = new DataSetSummaryPanel(viewModel);
		this.buttonPanel = new QuickButtonPanel(owner, viewModel);
		this.logWindow = new HVLogWindow(owner, viewModel);
		
		this.add(visPanel, BorderLayout.CENTER);
		this.add(infoBar, BorderLayout.SOUTH);
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(optionsPanel, BorderLayout.CENTER);
		rightPanel.add(summaryPanel, BorderLayout.SOUTH);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(this.menuBar, BorderLayout.NORTH);
		topPanel.add(this.buttonPanel, BorderLayout.CENTER);
		
		SNPMap snpMap = new SNPMap(viewModel);
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(rightPanel, BorderLayout.EAST);
		this.add(snpMap, BorderLayout.CENTER);
	}

	public ViewModel getViewModel() {
		return this.viewModel;
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			break;
		case ViewModelEvent.ROW_SORTING_CHANGED:
			break;
		}
	}
}
