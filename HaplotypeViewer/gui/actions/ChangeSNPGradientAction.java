package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import settings.HierarchicalSetting;
import settings.guiComponents.SettingDialog;
import viewmodel.ViewModel;

/**
 * Created by peltzer on 2/27/14.
 */
public class ChangeSNPGradientAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4042552914732431399L;
	private ViewModel model;

    public ChangeSNPGradientAction(ViewModel viewModel, String title){
        super(title);
        this.model = viewModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.showSNPMetInfColorDialog();
    }

    private void showSNPMetInfColorDialog() {
    	HierarchicalSetting s = this.model.getSNPMapSetting().getMetaRowSelectorSettingContainer();
        SettingDialog sd = new SettingDialog(model.getOwner(), s);
        sd.setVisible(true);
    }
}
