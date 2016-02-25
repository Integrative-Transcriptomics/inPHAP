package visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class SNPMapOverview extends JComponent implements AdjustmentListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5004994113859829214L;
	
	private SNPMap snpMap;
	
	private Image overviewImage;
	
	public SNPMapOverview(SNPMap snpMap) {
		this.snpMap = snpMap;
		snpMap.compScroller.getHorizontalScrollBar().addAdjustmentListener(this);
		snpMap.compScroller.getVerticalScrollBar().addAdjustmentListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(new Color(230,230,230));
		g2.clearRect(0,0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paint(g2);
	}
	
	private Rectangle2D ovRec;
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		double width = this.getWidth() - ((BasicStroke)g2.getStroke()).getLineWidth() * 2;
		double height = this.getHeight() - ((BasicStroke)g2.getStroke()).getLineWidth() * 2;
		
		if(this.overviewImage != null) {
			g2.drawImage(overviewImage, 0, 0, this);
		}
		
		
		g2.setColor(Color.LIGHT_GRAY);
		Rectangle2D border = new Rectangle2D.Double(0, 0, width, height);
		g2.draw(border);
		
		//calculate size of the new rectangle
		Rectangle snpMapRec = snpMap.snpMapComponent.getBounds();
		Rectangle snpMapVisRec = snpMap.compScroller.getViewport().getBounds();
		
		double widthF = ((double)snpMapVisRec.width / (double)snpMapRec.width) * width;
		double heightF = ((double)snpMapVisRec.height / (double)snpMapRec.height) * height;
		
		int smRecWidth = (int)Math.rint(widthF);
		int smRecHeight = (int)Math.rint(heightF);
		
		//create new rectangle
		ovRec = new Rectangle2D.Double(0,0,smRecWidth, smRecHeight);
		
		//calculate position of the new rectangle
		AffineTransform af = g2.getTransform();
		
		int startx = snpMap.compScroller.getHorizontalScrollBar().getValue();
		int starty = snpMap.compScroller.getVerticalScrollBar().getValue();
		
		double tx = (startx / (double)snpMapRec.width) * width;
		double ty = (starty / (double)snpMapRec.height) * height;
		
		//translate rectangle to the new position
		af.translate(tx, ty);
		g2.setTransform(af);
		
		//draw the rectangle with the specified size at the specified location
		if(overviewImage == null)
			g2.setColor(new Color(255,255,255));
		else
			g2.setColor(new Color(255,255,255,0));
		
		g2.fill(ovRec);
		g2.setColor(new Color(255,0,10,100));
		g2.draw(ovRec);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.moveRec(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			this.moveRec(e.getX(), e.getY());
		}
	}
	
	private void moveRec(int x, int y) {
		//do nothing if screen is not initialized yet
		if(ovRec == null)
			return;
		
		double recWidthCenter = ovRec.getWidth() / 2;
		double recHeightCenter = ovRec.getHeight() / 2;
		
		double newXPosInOv = x - recWidthCenter;
		double newYPosInOv = y - recHeightCenter;
		
		Rectangle snpMapRec = snpMap.snpMapComponent.getBounds();
		
		int width = getWidth();
		int height = getHeight();
		
		int xValue = (int)Math.rint((newXPosInOv / width) * snpMapRec.width);
		int yValue = (int)Math.rint((newYPosInOv / height) * snpMapRec.height);
		
		snpMap.compScroller.getHorizontalScrollBar().setValue(xValue);
		snpMap.compScroller.getVerticalScrollBar().setValue(yValue);
	}
	
	public void resizePlot(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		
		if(width > 0 && height > 0) {
//			this.overviewImage = this.snpMap.snpMapComponent.getImage();
			if(this.overviewImage != null) {
				this.overviewImage = this.overviewImage.getScaledInstance(getWidth()-1, getHeight()-1, Image.SCALE_FAST);
			}
		} else {
			this.overviewImage = null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
