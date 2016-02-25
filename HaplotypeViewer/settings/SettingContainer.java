package settings;

import gui.layout.ExcellentBoxLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.events.SettingChangeEvent;

public class SettingContainer extends HierarchicalSetting {
	
	public SettingContainer(String title) {
		super(title);
	}

	private JComponent guiComponent;
	
	@Override
	public void settingChanged(SettingChangeEvent e) {
		//pass it on
		fireSettingChanged(e);
	}

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		JPanel p = new JPanel(new ExcellentBoxLayout(true, 5));
		
		for(HierarchicalSetting s : this.children) {
			p.add(s.getViewComponent());
		}
		
		this.guiComponent = p;
		
		return this.guiComponent;
	}
}
