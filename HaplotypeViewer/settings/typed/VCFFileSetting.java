package settings.typed;

import java.io.File;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class VCFFileSetting extends HierarchicalSetting {

	private FileSetting fileSetting;
	
	public VCFFileSetting(String title) {
		super(title);
		this.addSetting(fileSetting = new FileSetting("VCF File"));
	}
	
	public File getFile() {
		return this.fileSetting.getFile();
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
	}
}
