package gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import events.ViewModelEvent;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;

public class HVVisPanel extends JPanel implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1864703325737898876L;
	
	private ViewModel viewModel;
	
	public HVVisPanel(ViewModel viewModel) {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Visualization View"));
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		//TODO
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		case ViewModelEvent.DATASET_CHANGED:
			//TODO
			break;
		case ViewModelEvent.ROW_SORTING_CHANGED:
			//TODO
			break;
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			//TODO
			break;
		}
	}
}
