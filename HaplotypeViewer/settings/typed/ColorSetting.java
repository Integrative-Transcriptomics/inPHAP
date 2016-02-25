package settings.typed;

import gui.layout.ExcellentBoxLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.guiComponents.ColorChooserComponent;

public class ColorSetting extends HierarchicalSetting {

	private Color color;
	private JComponent guiComponent;
	private JButton colorChooserButton;
	
	public ColorSetting(String title, Color color) {
		super(title);
		this.color = color;
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.colorChooserButton.setBackground(color);
		fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed: " + getColorName(getColor())));
	}
	
	public Color getColor() {
		return this.color;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}
	
	@Override
	public JComponent getViewComponent() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(getTitle()));
		p.setLayout(new ExcellentBoxLayout(true, 0));
		
		colorChooserButton = new JButton();
		colorChooserButton.setForeground(getColor());
		colorChooserButton.setBackground(getColor());
		colorChooserButton.setMinimumSize(new Dimension(15,20));
		colorChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorChooserComponent c = new ColorChooserComponent(null, getColor());
				c.showDialog();
				
				if(c.closedWithOK()) {
					setColor(c.getSelectedColor());
				}
			}
		});
		
		p.add(colorChooserButton);
		
		this.guiComponent = p;
		return this.guiComponent;
	}
	
	private String getColorName(Color c) {
		StringBuffer sb = new StringBuffer();
		sb.append("RGB=[");
		sb.append(Integer.toString(c.getRed()));
		sb.append(",");
		sb.append(Integer.toString(c.getGreen()));
		sb.append(",");
		sb.append(Integer.toString(c.getBlue()));
		sb.append("]");
		return sb.toString();
	}

}
