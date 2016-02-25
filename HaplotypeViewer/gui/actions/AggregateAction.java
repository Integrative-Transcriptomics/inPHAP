package gui.actions;

import gui.AggregationDialog;
import viewmodel.ViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by peltzer on 2/27/14.
 */
public class AggregateAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8314046273842004029L;
	private ViewModel vm;


    public AggregateAction(ViewModel viewModel, String title){
        super(title);
        this.vm = viewModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AggregationDialog dialog = new AggregationDialog(vm.getOwner(), vm);
        dialog.setVisible(true);
    }
}
