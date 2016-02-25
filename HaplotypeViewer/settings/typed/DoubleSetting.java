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

public class DoubleSetting extends HierarchicalSetting implements KeyListener {

	private Double value;
	private JComponent guiComponent;
	
	public DoubleSetting(String title, Double d) {
		super(title);
		this.value = d;
	}
	
	public void setValue(Double d) {
		this.value = d;
		fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed: " + Double.toString(getValue())));
	}
	
	public Double getValue() {
		return this.value;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}

	private JTextField valueField;
	
	@Override
	public JComponent getViewComponent() {
		if(guiComponent != null)
			return guiComponent;
		
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(getTitle()));
		p.setLayout(new ExcellentBoxLayout(true, 0));
		valueField = new JTextField();
		valueField.setText(Double.toString(this.value));
		valueField.setMaximumSize(new Dimension(100, 20));
		
		valueField.addKeyListener(this);
		
		p.add(valueField);
		this.guiComponent = p;
		return this.guiComponent;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			double newValue = Double.parseDouble(valueField.getText());
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
