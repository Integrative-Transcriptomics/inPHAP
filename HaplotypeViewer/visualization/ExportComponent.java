package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class ExportComponent extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2134443356486754061L;
	
	private JPanel placeHolderLeft;
	private JPanel columnHeader;
	private JPanel metaColumnHeader;
	
	private JPanel rowHeader;
	private JPanel dataComp;
	private JPanel metaDataComp;
	
	private JPanel metaRowHeader;
	private JPanel metaRowDataComp;
	private JPanel placeHolderRight;
	
	private int hSpace = 5;

	public ExportComponent(final SNPMap map) {
		
		placeHolderLeft = map.getPlaceHolderForOverview();
		columnHeader = getContainerPanel(map.getMapColumnHeader());
		metaColumnHeader = getContainerPanel(map.getMetaColumnHeader());
		
		int topWidth = placeHolderLeft.getWidth() + columnHeader.getWidth() + metaColumnHeader.getWidth();
		int topHeight = columnHeader.getHeight();
		
		rowHeader = getContainerPanel(map.getMapRowHeader());
		dataComp = getContainerPanel(map.getMapDataComponent());
		metaDataComp = getContainerPanel(map.getMetaColumnDataComponent());
		
		int centerHeight = rowHeader.getHeight();
		
		metaRowHeader = getContainerPanel(map.getMetaRowHeader());
		metaRowDataComp = getContainerPanel(map.getMetaRowDataComponent());
		
		placeHolderRight = new JPanel();
		placeHolderRight.setBackground(Color.WHITE);
		Dimension d = new Dimension(columnHeader.getWidth(), rowHeader.getHeight());
		placeHolderRight.setSize(d);
		placeHolderRight.setPreferredSize(d);
		
		int bottomHeigth = metaRowHeader.getHeight();
		
		int width = topWidth + hSpace;
		int height = topHeight + centerHeight + bottomHeigth;
		
		//this is the size used for visible area only
		this.setSize(new Dimension(width, height));
		
		//this is the size used when displaying the complete area
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public JPanel getContainerPanel(final HVComponent c) {
		JPanel p = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3935078627748122580L;

			public void paint(Graphics g) {
				super.paint(g);
				c.paintPlotAll((Graphics2D)g);
			}
		};
		
		p.setBackground(Color.WHITE);
		p.setSize(c.getOptimalSize());
		p.setPreferredSize(c.getOptimalSize());
		
		return p;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform af = g2d.getTransform();
		
		//paint top row
		this.placeHolderLeft.paint(g2d);
		g2d.translate(this.placeHolderLeft.getWidth(), 0);
		this.columnHeader.paint(g2d);
		g2d.translate(this.columnHeader.getWidth() + hSpace, 0);
		this.metaColumnHeader.paint(g2d);
		
		g2d.setTransform(af);
		
		//paint center row
		g2d.translate(0, this.columnHeader.getHeight());
		
		this.rowHeader.paint(g2d);
		g2d.translate(this.rowHeader.getWidth(), 0);
		this.dataComp.paint(g2d);
		g2d.translate(this.dataComp.getWidth() + hSpace, 0);
		this.metaDataComp.paint(g2d);
		
		g2d.setTransform(af);
		//paint bottom row
		g2d.translate(0, this.columnHeader.getHeight() + this.rowHeader.getHeight());
		
		this.metaRowHeader.paint(g2d);
		g2d.translate(this.metaRowHeader.getWidth(), 0);
		this.metaRowDataComp.paint(g2d);
		g2d.translate(this.metaRowDataComp.getWidth() + hSpace, 0);
		this.placeHolderRight.paint(g2d);
		
		g2d.setTransform(af);
	}
}
