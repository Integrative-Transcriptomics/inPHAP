package settings.typed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class StringChooserSetting extends HierarchicalSetting {

	private String[] values;
	private int selectedValue = 0;
	
	JComponent guiComponent;
	
	public StringChooserSetting(String title, int defaultValue, String... values) {
		super(title);
		this.selectedValue = defaultValue;
		this.values = values;
	}
	
	public String getSelectedValue() {
		if(this.values.length == 0)
			return null;
		return this.values[selectedValue];
	}
	
	public int getSelectedIndex() {
		if(this.values.length == 0)
			return -1;
		return this.selectedValue;
	}
	
	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		if(this.values.length == 0) {
			this.guiComponent = new JPanel();
			return this.guiComponent;
		}
		
		this.guiComponent = new JPanel(new BorderLayout());
		this.guiComponent.setBorder(BorderFactory.createTitledBorder(getTitle()));

		final JComboBox<String> valueSelectionBox = new JComboBox<String>(this.values);
		valueSelectionBox.setSelectedIndex(this.selectedValue);
		valueSelectionBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedValue = valueSelectionBox.getSelectedIndex();
				fireSettingChanged(new SettingChangeEvent(StringChooserSetting.this, SettingChangeEvent.SETTING_CHANGED, "Changed StringChooser Setting"));
			}
		});
		
		this.guiComponent.add(valueSelectionBox, BorderLayout.CENTER);
		
		return this.guiComponent;
	}

}
