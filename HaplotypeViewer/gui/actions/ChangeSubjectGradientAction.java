package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import settings.HierarchicalSetting;
import settings.guiComponents.SettingDialog;
import viewmodel.ViewModel;

/**
 * Created by peltzer on 2/27/14.
 */
public class ChangeSubjectGradientAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5669078885557198149L;
	private ViewModel model;

    public ChangeSubjectGradientAction(ViewModel viewModel, String title){
        super(title);
        this.model = viewModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.showSubMetInfGradientDialog();

    }

    private void showSubMetInfGradientDialog() {
    	HierarchicalSetting s = this.model.getSNPMapSetting().getMetaColumnSelectorSettingContainer();
        SettingDialog sd = new SettingDialog(model.getOwner(), s);
        sd.setVisible(true);
    }
}
