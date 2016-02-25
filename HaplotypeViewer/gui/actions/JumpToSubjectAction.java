package gui.actions;

import gui.jumpToSetting.JumpToSubjectDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import viewmodel.ViewModel;

public class JumpToSubjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164390654331285129L;
	
	private ViewModel viewModel;
	
	public JumpToSubjectAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JumpToSubjectDialog dialog = new JumpToSubjectDialog(viewModel.getOwner(), viewModel);
		dialog.setVisible(true);
	}

}
