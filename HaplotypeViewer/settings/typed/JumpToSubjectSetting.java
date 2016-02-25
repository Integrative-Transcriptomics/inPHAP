package settings.typed;

import gui.layout.ExcellentBoxLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class JumpToSubjectSetting extends HierarchicalSetting {

	public static final String SUBJECT_NAME = "Subject Name";
	public static final String SUBJECT_INDEX = "Subject Index";
	
	private RadioButtonSetting selectionSetting;
	private StringSetting idSetting;
	
	private String[] values = {SUBJECT_NAME, SUBJECT_INDEX};
	
	public JumpToSubjectSetting() {
		super("Jump to Subject Setting");
		
		this.addSetting(selectionSetting = new RadioButtonSetting("Select Subject Information Type", 0, values));
		this.addSetting(idSetting = new StringSetting("Provide Subject Information", ""));
	}
	
	public String getMethod() {
		return this.selectionSetting.getSelectedValue();
	}
	
	public String getValue() {
		return this.idSetting.getValue();
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
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
