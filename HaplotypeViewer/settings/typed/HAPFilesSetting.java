package settings.typed;

import java.io.File;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class HAPFilesSetting extends HierarchicalSetting {

	private StringSetting chromosome;
	private FileSetting hapFileSetting;
	private FileSetting legendFileSetting;
	private FileSetting sampleFileSetting;
	
	public HAPFilesSetting(String title) {
		super(title);
		
		addSetting(chromosome = new StringSetting("Chromosome", "X"));
		addSetting(hapFileSetting = new FileSetting("HAP File"));
		addSetting(legendFileSetting = new FileSetting("Legend File"));
		addSetting(sampleFileSetting = new FileSetting("Sample File"));
	}
	
	public String getChromosome() {
		return this.chromosome.getValue();
	}
	
	public File getHAPFile() {
		return this.hapFileSetting.getFile();
	}
	
	public File getLegendFile() {
		return this.legendFileSetting.getFile();
	}
	
	public File getSampleFile() {
		return this.sampleFileSetting.getFile();
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
	}
}
