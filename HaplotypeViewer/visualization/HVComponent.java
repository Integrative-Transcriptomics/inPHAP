package visualization;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public interface HVComponent {

	public void paint(Graphics g);
	public void paintPlot(Graphics2D g, int startx, int stopx, int starty, int stopy);
	public void paintPlotAll(Graphics2D g);
	public Dimension getOptimalSize();
}
