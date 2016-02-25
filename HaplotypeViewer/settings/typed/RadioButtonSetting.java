package settings.typed;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class RadioButtonSetting extends HierarchicalSetting {

	private String[] values;
	private int defaultSelection;
	
	private JComponent guiComponent;
	
	public RadioButtonSetting(String title, int defaultSelection, String... values) {
		super(title);
		this.values = values;
		this.defaultSelection = defaultSelection;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}
	
	public String getSelectedValue() {
		return this.values[defaultSelection];
	}
	
	public int getSelectedIndex() {
		return this.defaultSelection;
	}

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		this.guiComponent = new JPanel(new GridLayout(0,1));
		this.guiComponent.setBorder(BorderFactory.createTitledBorder(getTitle()));
		
		ButtonGroup bg = new ButtonGroup();
		
		for(int i = 0; i < values.length; i++) {
			final JRadioButton b = new JRadioButton(values[i]);
			if(i == this.defaultSelection)
				b.setSelected(true);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String text = b.getText();
					defaultSelection = indexOf(text);
					fireSettingChanged(new SettingChangeEvent(RadioButtonSetting.this, SettingChangeEvent.SETTING_CHANGED, getTitle() + " changed"));
				}
			});
			bg.add(b);
			this.guiComponent.add(b);
		}
		
		return this.guiComponent;
	}
	
	private int indexOf(String text) {
		for(int i = 0; i < values.length; i++) {
			if(values[i].equals(text))
				return i;
		}
		return -1;
	}

}
