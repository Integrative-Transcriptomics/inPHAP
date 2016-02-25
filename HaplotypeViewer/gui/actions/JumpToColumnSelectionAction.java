package gui.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;

import events.ViewModelEvent;
import viewmodel.ViewModel;

public class JumpToColumnSelectionAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2459096462136276085L;

	private ViewModel viewModel;
	
	public JumpToColumnSelectionAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Collection<Integer> selectedColumns = viewModel.getSelectedColumns();
		if(selectedColumns.size() > 0) {
			Integer firstSelectedCol = selectedColumns.iterator().next();
			Integer colInVis = viewModel.getColumnInVis(firstSelectedCol);
			viewModel.fireChanged(new ViewModelEvent(colInVis, ViewModelEvent.COLUMN_JUMP_EVENT, "Jump to column: " + Integer.toString(firstSelectedCol)));
		}
	}
}
