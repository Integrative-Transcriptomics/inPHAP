package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import viewmodel.ViewModel;
import main.HaplotypeViewer;

public class SystemInfoAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -853439086661826250L;
	private ViewModel viewModel;
	private String title;
	
	public SystemInfoAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
		this.title = title;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String fullName = HaplotypeViewer.getFullName();
		String systemInfo = HaplotypeViewer.getSystemInfo();
		
		String message = fullName + "\n\n" + systemInfo;
		
		JOptionPane.showMessageDialog(viewModel.getOwner(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
