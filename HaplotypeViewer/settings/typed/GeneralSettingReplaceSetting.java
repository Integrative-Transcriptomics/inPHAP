package settings.typed;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class GeneralSettingReplaceSetting extends HierarchicalSetting {
	
	private StringChooserSetting chooser;
	private JComponent guiComponent;
	
	private int oldIndex;
	
	public GeneralSettingReplaceSetting(List<HierarchicalSetting> settings, int defaultSetting, StringChooserSetting chooser) {
		super("");
		this.chooser = chooser;
		
		for(int i = 0; i < settings.size(); i++) {
			addSetting(settings.get(i));
		}
		
		chooser.addSettingChangeListener(this);
		this.oldIndex = defaultSetting;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		if(e.getSource().equals(chooser)) {
			if(oldIndex != getChosenSettingIndex()) {
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
			layout.show(this.guiComponent, getSetting(getChosenSettingIndex()).getTitle());
		}
		oldIndex = getChosenSettingIndex();
		this.guiComponent.revalidate();
		this.guiComponent.repaint();
	}
	
	private CardLayout layout;
	
	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null)
			return this.guiComponent;
		
		this.guiComponent = new JPanel(layout = new CardLayout());
		
		for(int i = 0; i < this.getNumChildren(); i++) {
			JPanel containerPanel = new JPanel(new BorderLayout());
			containerPanel.add(getSetting(i).getViewComponent(), BorderLayout.NORTH);
			containerPanel.add(new JPanel(), BorderLayout.CENTER);
			this.guiComponent.add(containerPanel, getSetting(i).getTitle());
		}
		
		layout.show(this.guiComponent, getSetting(getChosenSettingIndex()).getTitle());
		return this.guiComponent;
	}

	public int getChosenSettingIndex() {
		return chooser.getSelectedIndex();
	}
	
	public HierarchicalSetting getChoosenSetting() {
		return this.getSetting(getChosenSettingIndex());
	}
}
