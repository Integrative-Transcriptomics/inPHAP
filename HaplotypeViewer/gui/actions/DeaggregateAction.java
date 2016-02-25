package gui.actions;

import viewmodel.ViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by peltzer on 2/27/14.
 */
public class DeaggregateAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4246975815200058656L;
	private ViewModel viewModel;

    public DeaggregateAction(ViewModel vm, String title){
        super(title);
        this.viewModel = vm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        viewModel.deaggregateSelectedRows();
    }
}
