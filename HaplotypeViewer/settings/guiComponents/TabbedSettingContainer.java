package settings.guiComponents;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class TabbedSettingContainer extends HierarchicalSetting {
	
	public TabbedSettingContainer(String title) {
		super(title);
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//pass it on
		fireSettingChanged(e);
	}

	@Override
	public JComponent getViewComponent() {
		 JTabbedPane p = new JTabbedPane();
		
		for(HierarchicalSetting setting : this.children) {
			p.addTab(setting.getTitle(), setting.getViewComponent());
		}
		
		return p;
	}
}
