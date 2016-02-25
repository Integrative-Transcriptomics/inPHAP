package settings;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.events.SettingChangeEvent;
import settings.events.SettingChangeListener;
import events.EventFirer;
import gui.layout.ExcellentBoxLayout;

public abstract class HierarchicalSetting implements SettingChangeListener {
	
	protected List<HierarchicalSetting> children;
	
	private String title;
	
	protected EventFirer<SettingChangeEvent, SettingChangeListener> eventfirer = new EventFirer<SettingChangeEvent, SettingChangeListener>() {
		protected void dispatchEvent(SettingChangeEvent event, SettingChangeListener listener) {
			listener.settingChanged(event);
		}
	};
	
	public HierarchicalSetting(String title) {
		this.title = title;
		this.children = new ArrayList<HierarchicalSetting>();
	}
	
	public void addSetting(HierarchicalSetting setting) {
		this.children.add(setting);
		setting.addSettingChangeListener(this);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public HierarchicalSetting getSetting(int index) {
		if(index >= 0 && index < children.size()) {
			return this.children.get(index);
		}
		return null;
	}
	
	public void addSettingChangeListener(SettingChangeListener setting) {
		this.eventfirer.addListener(setting);
	}
	
	public void removeSettingChangeListener(SettingChangeListener setting) {
		this.eventfirer.removeListener(setting);
	}
	
	public void fireSettingChanged(SettingChangeEvent e) {
		this.eventfirer.fireEvent(e);
	}
	
	public int getNumChildren() {
		return this.children.size();
	}
	
	public JComponent getViewComponent() {
		JPanel panel = new JPanel(new ExcellentBoxLayout(true, 5));
		panel.setBorder(BorderFactory.createTitledBorder(getTitle()));
		for(int i = 0; i < children.size(); i++) {
			panel.add(children.get(i).getViewComponent());
		}
		return panel;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}