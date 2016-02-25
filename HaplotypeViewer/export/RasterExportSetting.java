package export;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.typed.BooleanSetting;

public class RasterExportSetting extends HierarchicalSetting {
	
	protected BooleanSetting textAA;
	protected BooleanSetting graphicsAA;
	
	public RasterExportSetting() {
		super("Anti-Aliasing");
		addSetting(textAA = new BooleanSetting("Text",true));
		addSetting(graphicsAA = new BooleanSetting("Graphics",true));
	}
	
	public boolean isTextAA() {
		return textAA.getValue();
	}
	
	public boolean isGraphicsAA() {
		return graphicsAA.getValue();
	}
	
	public void setAntialiasing(boolean text, boolean graphics) {
		textAA.setValue(text);
		graphicsAA.setValue(graphics);
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {}
}
