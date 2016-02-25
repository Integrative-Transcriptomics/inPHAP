package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import viewmodel.ViewModel;

public class RemoveAllFiltersAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7956972311489167121L;

	private ViewModel viewModel;
	
	public RemoveAllFiltersAction(ViewModel viewModel) {
		super("Remove all SNV Fitlers");
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.viewModel.removeFilters();
	}
}
