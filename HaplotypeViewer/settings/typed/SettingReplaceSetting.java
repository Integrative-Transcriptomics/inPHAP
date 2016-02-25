package settings.typed;

import java.awt.CardLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class SettingReplaceSetting extends HierarchicalSetting {

	private BooleanSetting shallReplaceSetting;
	
	private JComponent guiComponent;
	private boolean firstComp;
	
	public SettingReplaceSetting(HierarchicalSetting one, HierarchicalSetting two, BooleanSetting shallReplaceSetting) {
		super(null);
		
		addSetting(one);
		addSetting(two);
		this.shallReplaceSetting = shallReplaceSetting;
		firstComp = shallReplaceSetting.getValue();
		this.shallReplaceSetting.addSettingChangeListener(this);
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		if(e.getSource().equals(shallReplaceSetting)) {
			if(firstComp != shallReplaceSetting.getValue()) {
				replace();
				fireSettingChanged(e);
			}
		} else {
			//pass on all other events
			fireSettingChanged(e);
		}
	}
	
	private void replace() {
		if(layout != null) {
			if(firstComp = shallReplaceSetting.getValue()) {
				layout.show(this.guiComponent, getSetting(0).getTitle());
			} else {
				layout.show(this.guiComponent, getSetting(1).getTitle());
			}
		}
		this.guiComponent.revalidate();
	}
	
	private CardLayout layout;

	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		this.guiComponent = new JPanel(layout = new CardLayout());
		this.guiComponent.add(getSetting(0).getViewComponent(), getSetting(0).getTitle());
		this.guiComponent.add(getSetting(1).getViewComponent(), getSetting(1).getTitle());
		return this.guiComponent;
	}

	public int getChosenSettingIndex() {
		if(this.firstComp)
			return 0;
		return 1;
	}
}
