package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import settings.guiComponents.SettingDialog;
import settings.typed.StringChooserSetting;
import viewmodel.ViewModel;

public class RemoveMetaRowAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4721868119971584885L;
	
	private ViewModel viewModel;
	
	public RemoveMetaRowAction(ViewModel viewModel) {
		super("Remove Meta-Information Row");
		this.viewModel = viewModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(viewModel.numMetaRows() > 0) {
			String[] metaRows = new String[viewModel.numMetaRows()];
			for(int i = 0; i < metaRows.length; i++) {
				metaRows[i] = viewModel.getMetaRowID(i);
			}
			
			StringChooserSetting chooser = new StringChooserSetting("Select Meta-Info row", 0, metaRows);
			
			SettingDialog dialog = new SettingDialog(viewModel.getOwner(), chooser);
			dialog.setVisible(true);
			
			if(dialog.closedWithOK()) {
				int index = chooser.getSelectedIndex();
				viewModel.getDataSet().removeSNPMetaData(index);
			}
		}
	}
}
