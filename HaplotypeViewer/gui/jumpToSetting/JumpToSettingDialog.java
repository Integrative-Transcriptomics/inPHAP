package gui.jumpToSetting;

import gui.HaplotypeViewerDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import settings.HierarchicalSetting;

public abstract class JumpToSettingDialog extends HaplotypeViewerDialog {
	
	private HierarchicalSetting setting;
	
	public JumpToSettingDialog(Window owner, HierarchicalSetting setting) {
		super(owner);
		this.setting = setting;
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(300,30));
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				jumpTo();
				dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(setting.getViewComponent(), BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.centerOnScreen();
	}
	
	public HierarchicalSetting getSetting() {
		return this.setting;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6755154950986388914L;
	
	protected abstract void jumpTo();
}
