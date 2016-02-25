package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import settings.guiComponents.SettingDialog;
import settings.typed.StringChooserSetting;
import viewmodel.ViewModel;

public class RemoveMetaColumnAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315343253202712906L;
	
	private ViewModel viewModel;
	
	public RemoveMetaColumnAction(ViewModel viewModel) {
		super("Remove Meta-Information Column");
		this.viewModel = viewModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(viewModel.numMetaCols() > 0) {
			String[] metaCols = new String[viewModel.numMetaCols()];
			for(int i = 0; i < metaCols.length; i++) {
				metaCols[i] = viewModel.getMetaColumnID(i);
			}
			
			StringChooserSetting chooser = new StringChooserSetting("Select Meta-Info column", 0, metaCols);
			
			SettingDialog dialog = new SettingDialog(viewModel.getOwner(), chooser);
			dialog.setVisible(true);
			
			if(dialog.closedWithOK()) {
				int index = chooser.getSelectedIndex();
				viewModel.getDataSet().removeSubjectMetaData(index);
			}
		}
	}
}
