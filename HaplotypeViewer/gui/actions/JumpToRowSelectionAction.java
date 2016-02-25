package gui.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;

import events.ViewModelEvent;
import viewmodel.ViewModel;

public class JumpToRowSelectionAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7239313424121212897L;

	private ViewModel viewModel;
	
	public JumpToRowSelectionAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Collection<Integer> selectedRows = viewModel.getSelectedRows();
		if(selectedRows.size() > 0) {
			Integer firstSelectedRow = selectedRows.iterator().next();
			Integer rowInVis = viewModel.getRowInVis(firstSelectedRow);
			viewModel.fireChanged(new ViewModelEvent(rowInVis, ViewModelEvent.COLUMN_JUMP_EVENT, "Jump to column: " + Integer.toString(firstSelectedRow)));
		}
	}
}
