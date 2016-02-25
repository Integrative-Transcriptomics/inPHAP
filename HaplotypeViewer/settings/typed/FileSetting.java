package settings.typed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class FileSetting extends HierarchicalSetting {

	private JComponent guiComponent;
	private File selectedFile;
	
	public FileSetting(String title) {
		super(title);
		this.selectedFile = null;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
	}

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		this.guiComponent = new JPanel(new BorderLayout());
		this.guiComponent.setBorder(BorderFactory.createTitledBorder(getTitle()));
		
		final JTextField oPathField = new JTextField();
		JButton selectPathButton = new JButton("Select");
		
		selectPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				int approve = fileChooser.showSaveDialog(null);
				
				if(approve == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					oPathField.setText(selectedFile.getAbsolutePath());
					fireSettingChanged(new SettingChangeEvent(FileSetting.this, SettingChangeEvent.SETTING_CHANGED, "A file has been selected"));
				}
			}
		});
		
		this.guiComponent.add(oPathField, BorderLayout.CENTER);
		this.guiComponent.add(selectPathButton, BorderLayout.EAST);
		
		return this.guiComponent;
	}

	public File getFile() {
		return this.selectedFile;
	}

}
