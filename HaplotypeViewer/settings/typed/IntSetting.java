package settings.typed;

import gui.layout.ExcellentBoxLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class IntSetting extends HierarchicalSetting implements KeyListener {
	
	private Integer value;
	private JComponent guiComponent;
	
	public IntSetting(String title, Integer value) {
		super(title);
		this.value = value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
		fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed: " + Integer.toString(getValue())));
	}
	
	public Integer getValue() {
		return this.value;
	}

	private JTextField valueField;
	
	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(getTitle()));
		p.setLayout(new ExcellentBoxLayout(true, 0));
		valueField = new JTextField();
		valueField.setText(Integer.toString(this.value));
		valueField.setMaximumSize(new Dimension(100, 20));
		valueField.addKeyListener(this);
		p.add(valueField);
		this.guiComponent = p;
		return p;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			int newValue = Integer.parseInt(valueField.getText());
			valueField.setBackground(Color.WHITE);
			setValue(newValue);
		} catch (Exception ex) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				JOptionPane.showMessageDialog(this.guiComponent, 
						valueField.getText() + " is not a valid number!", 
						"Number Format Error", 
						JOptionPane.ERROR_MESSAGE);
			} else {
				valueField.setBackground(Color.RED);
			}
		}
	}
}
