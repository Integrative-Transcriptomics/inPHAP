package export.setting;

import events.ViewModelEvent;
import gui.HaplotypeViewerDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import viewmodel.ViewModel;

public class ExportSettingDialog extends HaplotypeViewerDialog {

	private ExportSetting setting;
	
	public ExportSettingDialog(Window owner, final ViewModel viewModel) {
		super(owner);
		this.setting = new ExportSetting();
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(600,800));
		this.setTitle(setting.getTitle());
		
		this.add(setting.getViewComponent(), BorderLayout.CENTER);
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JButton export = new JButton("Export");
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO check setting consistency
				viewModel.fireChanged(new ViewModelEvent(setting, ViewModelEvent.EXPORT_PLOT, "Plot export initialized"));
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(export);
		buttonPanel.add(close);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
		this.centerOnScreen();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4766110863702168611L;

}
