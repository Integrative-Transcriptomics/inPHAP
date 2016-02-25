package gui.actions;

import gui.HVAboutDialog;
import viewmodel.ViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by peltzer on 2/28/14.
 */
public class AboutAction extends AbstractAction {
    private ViewModel viewModel;

    public AboutAction(ViewModel viewmodel, String title){
        super(title);
        this.viewModel = viewmodel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.showAboutDialog();

    }

    private void showAboutDialog() {
        HVAboutDialog dialog = new HVAboutDialog(this.viewModel.getOwner());
        dialog.setVisible(true);
    }

}
