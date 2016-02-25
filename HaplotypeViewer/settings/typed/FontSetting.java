package settings.typed;

import java.awt.Font;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;

public class FontSetting extends HierarchicalSetting {

	private Font font;
	
	private StringSetting typeField;
	private StringChooserSetting styleSelection;
	private IntSetting sizeField;
	
	public FontSetting(String title, String type, int style, int size) {
		super(title);
		
		font = new Font(type, style, size);
		
		String[] styles = new String[]{"Plain", "Bold", "Italic"};
		int fontIndex = 0;
		
		switch(style) {
		case Font.PLAIN:
			fontIndex = 0;
			break;
		case Font.BOLD:
			fontIndex = 1;
			break;
		case Font.ITALIC:
			fontIndex = 2;
			break;
		}
		
		this.typeField = new StringSetting("Font Type", type);
		this.styleSelection = new StringChooserSetting("Font Style", fontIndex, styles);
		this.sizeField = new IntSetting("Font Size", size);
		
		this.addSetting(typeField);
		this.addSetting(styleSelection);
		this.addSetting(sizeField);
	}
	
	@Override
	public void settingChanged(SettingChangeEvent e) {
		String type = getType();
		int style = getStyle();
		int size = getSize();
		this.font = new Font(type, style, size);
		
		fireSettingChanged(new SettingChangeEvent(size, SettingChangeEvent.SETTING_CHANGED, "Font changed"));
	}
	
	private int getSize() {
		return this.sizeField.getValue().intValue();
	}

	private int getStyle() {
		int styleIndex = this.styleSelection.getSelectedIndex();
		switch(styleIndex) {
		case 0:
			return Font.PLAIN;
		case 1:
			return Font.BOLD;
		case 2:
			return Font.ITALIC;
		default:
			return Font.PLAIN;
		}
	}

	private String getType() {
		return typeField.getValue();
	}

	public Font getFont() {
		return this.font;
	}
}
