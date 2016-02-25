package visualization.utilities.colormaps;

import java.awt.Color;

public class AccentColorMap extends ColorMap {

	@Override
	public void initializePreviewColors() {
		this.previewColors = get5ClassColors();
	}

	@Override
	public void initializeColorMapColors(int size) {
		switch(size) {
		case 3:
			this.colors = get3ClassColors();
			break;
		case 4:
			this.colors = get4ClassColors();
			break;
		case 5:
			this.colors = get5ClassColors();
			break;
		case 6:
			this.colors = get6ClassColors();
			break;
		case 7:
			this.colors = get7ClassColors();
			break;
		case 8:
			this.colors = get8ClassColors();
			break;
		default:
			this.colors = get8ClassColors();
		}
	}
	
	public Color[] get3ClassColors() {
		Color[] colors = new Color[3];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		return colors;
	}
	
	public Color[] get4ClassColors() {
		Color[] colors = new Color[4];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		colors[3] = new Color(255,255,153);
		return colors;
	}
	
	public Color[] get5ClassColors() {
		Color[] colors = new Color[5];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		colors[3] = new Color(255,255,153);
		colors[4] = new Color(56,108,176);
		return colors;
	}
	
	public Color[] get6ClassColors() {
		Color[] colors = new Color[6];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		colors[3] = new Color(255,255,153);
		colors[4] = new Color(56,108,176);
		colors[5] = new Color(240,2,127);
		return colors;
	}
	
	public Color[] get7ClassColors() {
		Color[] colors = new Color[7];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		colors[3] = new Color(255,255,153);
		colors[4] = new Color(56,108,176);
		colors[5] = new Color(240,2,127);
		colors[6] = new Color(191,91,23);
		return colors;
	}
	
	public Color[] get8ClassColors() {
		Color[] colors = new Color[8];
		colors[0] = new Color(127,201,127);
		colors[1] = new Color(190,174,212);
		colors[2] = new Color(253,192,134);
		colors[3] = new Color(255,255,153);
		colors[4] = new Color(56,108,176);
		colors[5] = new Color(240,2,127);
		colors[6] = new Color(191,91,23);
		colors[7] = new Color(102,102,102);
		return colors;
	}

	@Override
	public void initializeMaxNumClasses() {
		this.maxNumClasses = 8;
	}

	@Override
	public String getTitle() {
		return "ColorBrewer: Accent";
	}
}
