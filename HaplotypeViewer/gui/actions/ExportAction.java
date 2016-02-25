package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import viewmodel.ViewModel;
import export.ExportDialog;

/**
 * Created by peltzer on 2/27/14.
 */
public class ExportAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6081552914710614189L;
	private ViewModel viewModel;
	private boolean visibleAreaOnly = false;

    public ExportAction(ViewModel vm, String title, boolean visibleAreaOnly){
        super(title);
        this.viewModel = vm;
        this.visibleAreaOnly = visibleAreaOnly;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.exportPlot();
    }


    public void exportPlot(){
    	new ExportDialog(viewModel.getVisComponent().getExportComponent(), visibleAreaOnly, viewModel.getOwner());
    }
}
