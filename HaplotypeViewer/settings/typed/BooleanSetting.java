package settings.typed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.layout.ExcellentBoxLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class BooleanSetting extends HierarchicalSetting {

	private boolean value;
	private JComponent guiComponent;
	
	public BooleanSetting(String title, boolean value) {
		super(title);
		this.value = value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
		cb.setSelected(value);
		fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed: " + Boolean.toString(getValue())));
	}
	
	public boolean getValue() {
		return this.value;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}
	
	private JCheckBox cb;

	@Override
	public JComponent getViewComponent() {
		if(guiComponent != null)
			return guiComponent;
		
		JPanel p = new JPanel();
		p.setLayout(new ExcellentBoxLayout(true, 5));
		
		cb = new JCheckBox(getTitle());
		cb.setSelected(getValue());
		
		cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setValue(cb.isSelected());
			}
		});
		
		p.add(cb);
		this.guiComponent = p;

		return this.guiComponent;
	}
}
