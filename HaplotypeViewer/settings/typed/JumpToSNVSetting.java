package settings.typed;

import gui.layout.ExcellentBoxLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class JumpToSNVSetting extends HierarchicalSetting {
	
	public static final String SNV_ID = "SNV identifier";
	public static final String SNV_POS = "SNV position";
	public static final String SNV_INDEX = "SNV index";
	
	private RadioButtonSetting selectionSetting;
	private StringSetting idSetting;
	
	private String[] values = {SNV_ID, SNV_POS, SNV_INDEX};
	
	public JumpToSNVSetting() {
		super("Jump To SNV ...");
		
		this.addSetting(selectionSetting = new RadioButtonSetting("Select SNV information type", 0, values));
		this.addSetting(idSetting = new StringSetting("Provide SNV information", ""));
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}
	
	public String getValue() {
		return this.idSetting.getValue();
	}
	
	public String getMethod() {
		return this.selectionSetting.getSelectedValue();
	}

	@Override
	public JComponent getViewComponent() {
		JPanel panel = new JPanel(new ExcellentBoxLayout(true, 5));
		panel.setBorder(BorderFactory.createTitledBorder(getTitle()));
		for(int i = 0; i < children.size(); i++) {
			panel.add(children.get(i).getViewComponent());
		}
		return panel;
	}
}
