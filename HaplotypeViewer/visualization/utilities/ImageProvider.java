package visualization.utilities;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.VolatileImage;

import javax.swing.JComponent;

import viewmodel.ViewModel;

public class ImageProvider {
	
	public static final int NUM_BUCKETS = 100;
	
	VolatileImage[] saturation = new VolatileImage[NUM_BUCKETS];
	
	VolatileImage ASquare;
	VolatileImage CSquare;
	VolatileImage TSquare;
	VolatileImage GSquare;
	
	VolatileImage selectionImage;
	VolatileImage metaRowSelectionImage;
	VolatileImage metaColumnSelectionImage;
	
	VolatileImage referenceImage;
	VolatileImage heteroImage;
	VolatileImage homoImage;
	
	VolatileImage naImageRow;
	VolatileImage naImageColumn;
	
	private BasicStroke defaultStroke = new BasicStroke(1);
	private BasicStroke selectionStroke = new BasicStroke(2);
	
	GraphicsConfiguration gc;
	private ViewModel viewModel;
	
	public ImageProvider(JComponent c, ViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	public void setGraphicsConfiguration(JComponent c) {
		gc = c.getGraphicsConfiguration();
		this.redrawImages(viewModel.getSNPMapSetting().getCellWidth(), 
				viewModel.getSNPMapSetting().getCellHeight(),
				viewModel.getSNPMapSetting().getMetaCellWidth(),
				viewModel.getSNPMapSetting().getMetaCellHeight());
	}
	
	public void redrawImages() {
		int width = viewModel.getSNPMapSetting().getCellWidth();
		int height = viewModel.getSNPMapSetting().getCellHeight();
		int metaWidth = viewModel.getSNPMapSetting().getMetaCellWidth();
		int metaHeight = viewModel.getSNPMapSetting().getMetaCellHeight();
		this.redrawImages(width, height, metaWidth, metaHeight);
	}
	
	public VolatileImage getNAImageRow() {
		return this.naImageRow;
	}
	
	public VolatileImage getNAImageColumn() {
		return this.naImageColumn;
	}
	
	public VolatileImage getSelectionImage() {
		return this.selectionImage;
	}
	
	public VolatileImage getMetaRowSelectionImage() {
		return this.metaRowSelectionImage;
	}
	
	public VolatileImage getMetaColumnSelectionImage() {
		return this.metaColumnSelectionImage;
	}
	
	public VolatileImage getImage(char A) {
		switch(A) {
		case 'A':
			return ASquare;
		case 'T':
			return TSquare;
		case 'C':
			return CSquare;
		case 'G':
			return GSquare;
		default:
			return saturation[0];
		}
	}
	
	public VolatileImage getSaturationImage(double d) {
		int index = mapToBucket(d);
		//TODO check if the order has to be changed!
		return saturation[index];
	}
	
	private int mapToBucket(double d) {
		int bucket = (int)Math.rint(d * NUM_BUCKETS);
		//map to ranges
		if(bucket >= NUM_BUCKETS) {
			bucket = NUM_BUCKETS - 1;
		}
		
		if(bucket < 0)
			bucket = 0;
		
		return bucket;
	}
	
	private void redrawImages(int width, int height, int metaWidth, int metaHeight) {
		if(gc == null)
			return;
		
		int arcWidth = 0;
		int arcHeight = 0;
		
		if(width >= 4) {
			arcWidth = 2;
		}
		
		if(height >= 4) {
			arcHeight = 2;
		}
		
		for(int i = 0; i < NUM_BUCKETS; i++) {
			saturation[i] = gc.createCompatibleVolatileImage(width, height, VolatileImage.TRANSLUCENT);
			Graphics2D g2 = (Graphics2D)saturation[i].getGraphics();
			g2.setColor(Color.WHITE);
			//set composite mode in order to paint with transparency
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, (i+1)/((float)NUM_BUCKETS)));
			g2.fillRoundRect(0, 0, saturation[i].getWidth(), saturation[i].getHeight(), arcWidth, arcHeight);
			g2.dispose();
		}
		
		ASquare = gc.createCompatibleVolatileImage(width, height);
		Graphics2D g2 = (Graphics2D)ASquare.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getAColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		TSquare = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)TSquare.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getTColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		GSquare = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)GSquare.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getGColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		CSquare = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)CSquare.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getCColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		referenceImage = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)referenceImage.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getNoSNPColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		heteroImage = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)heteroImage.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getHeteroSNPColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		homoImage = gc.createCompatibleVolatileImage(width, height);
		g2 = (Graphics2D)homoImage.getGraphics();
		g2.setColor(viewModel.getSNPMapSetting().getHomoSNPColor());
		g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		g2.dispose();
		
		selectionImage = gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
		g2 = selectionImage.createGraphics();
		g2.setStroke(selectionStroke);
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
		g2.fillRoundRect(0, 0, selectionImage.getWidth(), selectionImage.getHeight(), arcWidth, arcHeight);
		g2.setColor(viewModel.getSNPMapSetting().getSelectionColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.drawRect(0, 0, selectionImage.getWidth(),  selectionImage.getHeight());
		g2.setStroke(defaultStroke);
		g2.dispose();
		
		metaRowSelectionImage = gc.createCompatibleVolatileImage(width, metaHeight, Transparency.TRANSLUCENT);
		g2 = metaRowSelectionImage.createGraphics();
		g2.setStroke(selectionStroke);
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
		g2.fillRoundRect(0, 0, metaRowSelectionImage.getWidth(), metaRowSelectionImage.getHeight(), arcWidth, arcHeight);
		g2.setColor(viewModel.getSNPMapSetting().getSelectionColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.drawRect(0, 0, metaRowSelectionImage.getWidth(),  metaRowSelectionImage.getHeight());
		g2.setStroke(defaultStroke);
		g2.dispose();
		
		metaColumnSelectionImage = gc.createCompatibleVolatileImage(metaWidth, height, Transparency.TRANSLUCENT);
		g2 = metaColumnSelectionImage.createGraphics();
		g2.setStroke(selectionStroke);
		g2.setColor(Color.WHITE);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
		g2.fillRoundRect(0, 0, metaColumnSelectionImage.getWidth(), metaColumnSelectionImage.getHeight(), arcWidth, arcHeight);
		g2.setColor(viewModel.getSNPMapSetting().getSelectionColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.drawRect(0, 0, metaColumnSelectionImage.getWidth(),  metaColumnSelectionImage.getHeight());
		g2.setStroke(defaultStroke);
		g2.dispose();
		
		naImageRow = gc.createCompatibleVolatileImage(width, metaHeight);
		g2 = naImageRow.createGraphics();
		g2.setColor(Color.RED);
		if(width >= 3 && metaHeight >= 3) {
			g2.setStroke(selectionStroke);
			g2.draw(new Line2D.Double(0, 0, width, metaHeight));
			g2.draw(new Line2D.Double(0, metaHeight, width, 0));
			g2.setStroke(defaultStroke);
		}
		g2.dispose();
		
		naImageColumn = gc.createCompatibleVolatileImage(metaWidth, height);
		g2 = naImageColumn.createGraphics();
		g2.setColor(Color.RED);
		if(metaWidth >= 3 && height >= 3) {
			g2.setStroke(selectionStroke);
			g2.draw(new Line2D.Double(0, 0, metaWidth, height));
			g2.draw(new Line2D.Double(0, height, metaWidth, 0));
			g2.setStroke(defaultStroke);
		}
		g2.dispose();
		
		System.gc();
	}
	
	public VolatileImage getImage(char c1, char c2, char ref) {
		if(c1 == c2) {
			if(c1 == ref) {
				return this.referenceImage;
			} else {
				return this.homoImage;
			}
		} else {
			if(c1 == ref || c2 == ref) {
				return this.heteroImage;
			} else {
				return this.homoImage;
			}
		}
	}
}
