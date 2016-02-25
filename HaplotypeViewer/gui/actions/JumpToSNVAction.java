package gui.actions;

import gui.jumpToSetting.JumpToSNVDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import viewmodel.ViewModel;

public class JumpToSNVAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5905424190625132812L;

	private ViewModel viewModel;
	
	public JumpToSNVAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JumpToSNVDialog dialog = new JumpToSNVDialog(viewModel.getOwner(), viewModel);
		dialog.setVisible(true);
	}
}
