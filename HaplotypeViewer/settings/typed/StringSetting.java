package settings.typed;

import gui.layout.ExcellentBoxLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class StringSetting extends HierarchicalSetting {
	
	private String value;
	private JComponent guiComponent;
	
	public StringSetting(String title, String defaultValue) {
		super(title);
		this.value = defaultValue;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		this.guiComponent = new JPanel();
		this.guiComponent.setLayout(new ExcellentBoxLayout(true, 0));
		this.guiComponent.setBorder(BorderFactory.createTitledBorder(getTitle()));
		
		final JTextField valueField = new JTextField(this.value);
		valueField.setMinimumSize(new Dimension(15,20));
		valueField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireSettingChanged(new SettingChangeEvent(StringSetting.this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed"));
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				value = valueField.getText();
			}
		});
		
		this.guiComponent.add(valueField, BorderLayout.CENTER);
		return this.guiComponent;
	}

}
