package visualization.utilities.colormaps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import visualization.utilities.ColorSelector;
import visualization.utilities.gui.PreviewPanel;

public abstract class ColorMap extends ColorSelector {
	
	//preview panel element
	Rectangle2D ppElement = new Rectangle2D.Double(0,0,15,15);
	Color[] previewColors = new Color[5];
	
	Color[] colors;
	int maxNumClasses;
	
	private PreviewPanel previewPanel;
	
	public ColorMap() {
		this.initializeMaxNumClasses();
		this.initializePreviewColors();
		this.initializeColorMapColors(this.getMaxNumberOfClasses());
		this.initializePreviewPanel();
	}
	
	public abstract String getTitle();
	
	private void initializePreviewPanel() {
		this.previewPanel = new PreviewPanel(this);
		this.previewPanel.setPreferredSize(new Dimension(75, 15));
		this.previewPanel.setLayout(new BorderLayout());
		
		JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		for(int i = 0; i < 5; i++) {
			JPanel bg = new JPanel();
			bg.setBackground(previewColors[i]);
			colorPanel.add(bg, BorderLayout.CENTER);
		}
		
		this.previewPanel.add(new JLabel(getTitle()), BorderLayout.NORTH);
		this.previewPanel.add(colorPanel, BorderLayout.CENTER);
		this.previewPanel.add(new JLabel("Max number of classes: " + getMaxNumberOfClasses()), BorderLayout.SOUTH);
	}

	public void copySettings(ColorMap colorMap) {
		this.colors = colorMap.getColors();
		this.previewColors = colorMap.getPreviewColors();
		this.maxNumClasses = colorMap.getMaxNumberOfClasses();
	}
	
	private Color[] getPreviewColors() {
		return this.previewColors;
	}

	private Color[] getColors() {
		return this.colors;
	}

	public PreviewPanel getPreviewPanel() {
		return this.previewPanel;
	}
	
	public abstract void initializePreviewColors();
	public abstract void initializeColorMapColors(int size);
	
	public int getSize() {
		return this.colors.length;
	}
	
	public void setSize(int size) {
		this.initializeColorMapColors(size);
	}
	
	public Color getColor(int colorIndex) {
		return this.colors[colorIndex % this.getSize()];
	}
	
	public Color mapValueToColor(double value) {
		int intValue = (int)Math.rint(value);
		return getColor(intValue);
	}
	
	public abstract void initializeMaxNumClasses();
	
	public int getMaxNumberOfClasses() {
		return this.maxNumClasses;
	}
}
