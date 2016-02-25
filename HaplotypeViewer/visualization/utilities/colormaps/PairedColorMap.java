package visualization.utilities.colormaps;

import java.awt.Color;

public class PairedColorMap extends ColorMap {

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
		case 9:
			this.colors = get9ClassColors();
			break;
		case 10:
			this.colors = get10ClassColors();
			break;
		case 11:
			this.colors = get11ClassColors();
			break;
		case 12:
			this.colors = get12ClassColors();
			break;
		default:
			this.colors = get8ClassColors();
		}
	}

	private Color[] get3ClassColors() {
		Color[] colors = new Color[3];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		return colors;
	}
	
	private Color[] get4ClassColors() {
		Color[] colors = new Color[4];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		return colors;
	}
	
	private Color[] get5ClassColors() {
		Color[] colors = new Color[5];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		return colors;
	}
	
	private Color[] get6ClassColors() {
		Color[] colors = new Color[6];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		return colors;
	}
	
	private Color[] get7ClassColors() {
		Color[] colors = new Color[7];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		return colors;
	}
	
	private Color[] get8ClassColors() {
		Color[] colors = new Color[8];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		colors[7] = new Color(255,127,0);
		return colors;
	}
	
	private Color[] get9ClassColors() {
		Color[] colors = new Color[9];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		colors[7] = new Color(255,127,0);
		colors[8] = new Color(202,178,214);
		return colors;
	}
	
	private Color[] get10ClassColors() {
		Color[] colors = new Color[10];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		colors[7] = new Color(255,127,0);
		colors[8] = new Color(202,178,214);
		colors[9] = new Color(106,61,154);
		return colors;
	}
	
	private Color[] get11ClassColors() {
		Color[] colors = new Color[11];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		colors[7] = new Color(255,127,0);
		colors[8] = new Color(202,178,214);
		colors[9] = new Color(106,61,154);
		colors[10] = new Color(255,255,153);
		return colors;
	}
	
	private Color[] get12ClassColors() {
		Color[] colors = new Color[12];
		colors[0] = new Color(166,206,227);
		colors[1] = new Color(31,120,180);
		colors[2] = new Color(178,223,138);
		colors[3] = new Color(51,160,44);
		colors[4] = new Color(251,154,153);
		colors[5] = new Color(227,26,28);
		colors[6] = new Color(253,191,111);
		colors[7] = new Color(255,127,0);
		colors[8] = new Color(202,178,214);
		colors[9] = new Color(106,61,154);
		colors[10] = new Color(255,255,153);
		colors[11] = new Color(177,89,40);
		return colors;
	}

	@Override
	public void initializeMaxNumClasses() {
		this.maxNumClasses = 12;
	}

	@Override
	public String getTitle() {
		return "ColorBrewer: Paired";
	}
}
