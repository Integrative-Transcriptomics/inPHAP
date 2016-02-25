package visualization.utilities.gui;

import javax.swing.JPanel;

import visualization.utilities.colormaps.ColorMap;

public class PreviewPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2368046730638662072L;
	private ColorMap colorMap;
	
	public PreviewPanel(ColorMap colorMap) {
		this.colorMap = colorMap;
	}
	
	public ColorMap getColorMap() {
		return this.colorMap;
	}

}
