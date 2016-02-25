package visualization.utilities.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import visualization.utilities.ColorGradient;

@SuppressWarnings("serial")
public class SimpleGradientComponent extends JPanel {

	ColorGradient gradient;
	protected boolean fullMode;

	public SimpleGradientComponent(ColorGradient cg, boolean fullMode) {
		gradient = cg;
		setPreferredSize(new Dimension(100, 
				16
				+getInsets().bottom+getInsets().top));
		setMinimumSize(getPreferredSize());
		setOpaque(true);
		setBackground(Color.white);
		this.fullMode = fullMode;
	}

	public void setGradient(ColorGradient cg) {
		gradient = cg;
		repaint();
	}
	
	public ColorGradient getGradient() {
		return gradient;
	}

	protected int getInnerWidth() {
		return getWidth() - getInsets().left - getInsets().right;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2 = (Graphics2D)g;
		
		AffineTransform originalTransform = g2.getTransform();
		
		Insets i = getInsets();
		g2.translate(i.left, i.top);
		
		drawGradient( g2, getInnerWidth() );

		g2.setTransform(originalTransform);
	}
	
	protected void drawGradient(Graphics2D g2, int width ) {
		if (gradient==null)
			return;
		
		int colorCount, mi, ma;
		
		if (!fullMode) {
		// render not colorcount but min-max
			double min = gradient.getMin();
			double max = gradient.getMax();
			mi = gradient.mapValueToIndex(min);
			ma = gradient.mapValueToIndex(max);
			colorCount = (ma-mi)+1;
		} else {
			colorCount = gradient.getResolution();
			mi=0;
			ma=colorCount-1;
		}
		
		double oneColorWidth = ((double)width)/((double)colorCount);
		
		Rectangle2D.Double r2d = new Rectangle2D.Double( 0, 0, Math.max(oneColorWidth,1.0), 16);
		
		for ( int i = mi; i<=ma; ++i ) {
			g2.setColor( gradient.getColor(i) );			
			r2d.x = (i-mi)*oneColorWidth;
			g2.fill(r2d);
		}
		
	}
	
}
