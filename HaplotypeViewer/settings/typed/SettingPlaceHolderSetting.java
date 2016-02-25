package settings.typed;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class SettingPlaceHolderSetting extends HierarchicalSetting {

	private HierarchicalSetting setting;
	private JComponent guiComponent = null;
	
	public SettingPlaceHolderSetting(HierarchicalSetting setting) {
		super("");
		if(setting != null) {
			super.setTitle(setting.getTitle());
			this.setting = setting;
		}
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {}
	
	public void replaceSetting(HierarchicalSetting setting) {
		String oldTitle = this.setting == null ? "No Setting" : this.setting.getTitle();
		
		this.guiComponent.removeAll();
		this.setting = setting;
		
		if(this.setting != null)
			this.guiComponent.add(this.setting.getViewComponent(), BorderLayout.CENTER);
		
		this.guiComponent.revalidate();
		this.guiComponent.repaint();
		
		String newTitle = this.setting == null ? "No Setting" : this.setting.getTitle();
		fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.SETTING_CHANGED, oldTitle + " replaced with " + newTitle));
		
	}
	
	public JComponent getViewComponent() {
		if(setting != null) {
			if(this.guiComponent != null)
				return this.guiComponent;
			this.guiComponent = new JPanel(new BorderLayout());
			this.guiComponent.add(setting.getViewComponent(), BorderLayout.CENTER);
		} else {
			if(this.guiComponent != null)
				return this.guiComponent;
			this.guiComponent = new JPanel(new BorderLayout());
		}
		return this.guiComponent;
	}
}
