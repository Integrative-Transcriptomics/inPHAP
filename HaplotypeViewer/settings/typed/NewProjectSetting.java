package settings.typed;

import io.HAPParser;
import io.IDataParser;
import io.VCFParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class NewProjectSetting extends HierarchicalSetting {

	public static final String VCF = "VCF file format";
	public static final String HAP = "HAP file format";
	
	GeneralSettingReplaceSetting replaceSetting;
	StringChooserSetting chooser;
	
	String[] values = {VCF, HAP};
	
	public NewProjectSetting() {
		super("Select the file format");
		
		this.chooser = new StringChooserSetting("Choose data format", 0, values);
		
		List<HierarchicalSetting> settings = new ArrayList<HierarchicalSetting>();
		settings.add(new VCFFileSetting(VCF));
		settings.add(new HAPFilesSetting(HAP));
		
		this.replaceSetting = new GeneralSettingReplaceSetting(settings, 0, chooser);
		
		addSetting(chooser);
		addSetting(replaceSetting);
	}
	
	public IDataParser getDataParser() throws IOException {
		switch(chooser.getSelectedValue()) {
		case VCF:
			VCFFileSetting s = (VCFFileSetting)replaceSetting.getSetting(chooser.getSelectedIndex());
			File vcfFile = s.getFile();
			return new VCFParser(vcfFile.getAbsolutePath());
		case HAP:
			HAPFilesSetting h = (HAPFilesSetting)replaceSetting.getSetting(chooser.getSelectedIndex());
			String chrom = h.getChromosome();
			File hapFile = h.getHAPFile();
			File legendFile = h.getLegendFile();
			File sampleFile = h.getSampleFile();
			return new HAPParser(legendFile.getAbsolutePath(), sampleFile.getAbsolutePath(), hapFile.getAbsolutePath(), chrom);
		}
		
		return null;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothing to do
	}
}
