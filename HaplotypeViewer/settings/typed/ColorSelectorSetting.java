package settings.typed;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import visualization.utilities.ColorSelector;

public class ColorSelectorSetting extends HierarchicalSetting {

	private ColorGradientSetting gradientSetting;
	private ColorMapSetting colorMapSetting;
	private BooleanSetting replace;
	
	public ColorSelectorSetting(String title, ColorGradientSetting gradientSetting, ColorMapSetting colorMapSetting) {
		super(title);
		this.gradientSetting = gradientSetting;
		this.colorMapSetting = colorMapSetting;
		this.replace = new BooleanSetting("User color maps?", true);
		SettingReplaceSetting replaceSetting = new SettingReplaceSetting(this.colorMapSetting, this.gradientSetting, replace);
		this.addSetting(replace);
		this.addSetting(replaceSetting);
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//nothgin to do
	}
	
	public ColorSelector getColorSelector() {
		boolean colorMap = this.replace.getValue();
		if(colorMap) {
			return this.colorMapSetting.getColorMap();
		} else {
			return this.gradientSetting.getColorGradient();
		}
	}
}
