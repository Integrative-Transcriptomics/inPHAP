package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import filtering.filter.Filter;
import settings.guiComponents.SettingDialog;
import settings.typed.FilterSelectionSetting;
import viewmodel.ViewModel;

/**
 * Created by peltzer on 2/27/14.
 */
public class FilterAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5574496770854923495L;
	private ViewModel viewModel;

    public FilterAction(ViewModel viewModel, String title){
        super(title);
        this.viewModel = viewModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.filter();
    }

    public void filter() {
        FilterSelectionSetting setting = new FilterSelectionSetting("Choose SNV filter", viewModel);
        SettingDialog dialog = new SettingDialog(viewModel.getOwner(), setting);
        dialog.setVisible(true);
        
        if(dialog.closedWithOK()) {
        	Filter f = setting.getFilter();
        	viewModel.filterSNVs(f);
        }
    }
}
