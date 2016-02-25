package export.setting;

import gui.layout.ExcellentBoxLayout;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.typed.FileSetting;
import settings.typed.RadioButtonSetting;

public class ExportSetting extends HierarchicalSetting {
	
	private FileSetting outputFileSetting;
	private RadioButtonSetting chooserSetting;
	
	public static final String COMPLETE_AREA = "Complete Plot Area";
	public static final String VISIBLE_AREA  ="Visible Plot Area";
	
	private String[] values = {COMPLETE_AREA, VISIBLE_AREA};
	
	public ExportSetting() {
		super("Export Setting");
		
		this.addSetting(chooserSetting = new RadioButtonSetting("Plot Area", 0, values));
		this.addSetting(outputFileSetting = new FileSetting("Output file"));
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
	}
	
	public File getOutputFile() {
		return this.outputFileSetting.getFile();
	}
	
	public String getMethod() {
		return this.chooserSetting.getSelectedValue();
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
