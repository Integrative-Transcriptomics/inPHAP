package settings.typed;

import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class ComponentPlaceHolderSetting extends HierarchicalSetting {

	private JComponent component;
	
	public ComponentPlaceHolderSetting(String title, JComponent component) {
		super(title);
		this.component = component;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {}

	
	public JComponent getViewComponent() {
		if(this.component == null)
			return new JPanel();
		return this.component;
	}
}
