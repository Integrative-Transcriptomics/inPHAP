package export;

import java.awt.Dimension;

import javax.swing.JComponent;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.typed.BooleanSetting;
import settings.typed.IntSetting;

public class LinkedDimensionSetting extends HierarchicalSetting {
	
	public IntSetting width, height;
	public BooleanSetting linkDimensions;

	public LinkedDimensionSetting() {
		super("Target Dimensions");
		addSetting(width = new IntSetting("Width",0));
		addSetting(height = new IntSetting("Height",0));
		addSetting(linkDimensions = new BooleanSetting("Keep aspect ratio", true));
	}
	
	public void setTargetDimension( Dimension d ) {
		height.setValue(d.height);
		width.setValue(d.width);
	}
	
	public Dimension getTargetDimension() {
		return new Dimension(width.getValue(), height.getValue());
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {}
	
	public JComponent getViewComponent() {
		return new LinkedDimensionSettingComponent(this);
	}
}


