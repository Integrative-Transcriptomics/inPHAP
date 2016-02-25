package settings.guiComponents;

import gui.HaplotypeViewerDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import settings.HierarchicalSetting;

public class SettingDialog extends HaplotypeViewerDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1426666601615232588L;
	
	private HierarchicalSetting setting;
	
	private boolean closedWithOK = false;
	
	public SettingDialog(Window owner, HierarchicalSetting setting) {
		super(owner);
		this.setting = setting;
		this.setLayout(new BorderLayout());
		this.setModal(true);
		this.setMinimumSize(new Dimension(300,0));
		
		this.setTitle(setting.getTitle());
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JButton apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closedWithOK = true;
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(apply);
		buttonPanel.add(close);
		
		this.add(setting.getViewComponent(), BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		centerOnScreen();
	}
	
	public HierarchicalSetting getSetting() {
		return this.setting;
	}
	
	public boolean closedWithOK() {
		return this.closedWithOK;
	}

	public void resize() {
		setSize(new Dimension(getMinimumSize()));
		pack();
		setPreferredSize(getSize());
		revalidate();
		repaint();
	}
}
