package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import events.ViewModelEvent;

public class HVVisOptionsPanel extends JPanel implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1346575393014612519L;
	
	private ViewModel viewModel;
	
	public HVVisOptionsPanel(ViewModel viewModel) {
		this.setLayout(new BorderLayout());
//		this.setBorder(BorderFactory.createTitledBorder("Options"));
		this.setPreferredSize(new Dimension(150, 400));
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		
		this.add(viewModel.getSNPMapSetting().getViewComponent(), BorderLayout.CENTER);
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		//TODO
		}
	}
}
