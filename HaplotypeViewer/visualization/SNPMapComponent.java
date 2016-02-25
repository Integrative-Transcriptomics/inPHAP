package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.JComponent;

import settings.SNPMapSetting;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import events.ViewModelEvent;

@SuppressWarnings("serial")
public class SNPMapComponent extends JComponent implements MouseListener, ViewModelListener, HVComponent {
	
	private ViewModel viewModel;
	private SNPMap snpMap;
	
	public SNPMapComponent(ViewModel viewModel, SNPMap snpMap) {
		this.viewModel = viewModel;
		this.snpMap = snpMap;
		this.viewModel.addViewModelListener(this);
		this.addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.white);
		g2.clearRect(0,0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paint(g2);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		this.paintPlot(g2);
	}
	
	protected void resizePlot() {
		this.revalidate();
		this.repaint();
	}
	
	public void paintPlot(Graphics2D g) {
		int cellWidth = viewModel.getSNPMapSetting().getCellWidth();
		int cellHeight = viewModel.getSNPMapSetting().getCellHeight();
		
		Rectangle bounds = snpMap.compScroller.getVisibleRect();
		int startx = snpMap.compScroller.getHorizontalScrollBar().getValue();
		int start = Math.max((int)Math.floor(startx / cellWidth), 0);
		int stop = (int)Math.ceil((startx + bounds.width) / cellWidth) + 1;
		stop = Math.min(stop, viewModel.numColsInVis());
		
		int starty = snpMap.compScroller.getVerticalScrollBar().getValue();
		int start2 = Math.max((int)Math.floor(starty / cellHeight), 0);
		int stop2 = (int)Math.ceil((starty + bounds.height) / cellHeight) + 1;
		stop2 = Math.min(stop2, viewModel.numRowsInVis());
		
		this.paintPlot(g, start, stop, start2, stop2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//TODO
//		if(e.getButton() == MouseEvent.BUTTON1) {
//			int x = e.getX();
//			int cellWidth = snpMap.setting.getCellWidth();
//			int cellHeight = snpMap.setting.getCellHeight();
//			int snpIndex = x / cellWidth;
//			
//			if(snpIndex < snpMap.snps.size()) {
//				SNP s = snpMap.snps.get(snpIndex);
//				
//				int y = e.getY();
//				Integer pIndex = y / cellHeight;
//				
//				if(pIndex < snpMap.persons.size()) {
//					Integer personIndex = snpMap.personIndices.get(pIndex);
//					
//					if(personIndex < snpMap.personIndices.size()) {
//						Subject p = snpMap.persons.get(personIndex);
//						RevealViewModel model = snpMap.getViewModel();
//						
//						if(e.isControlDown()) {
//							if(!model.isSelected(s))
//								model.toggleSNPSelected(s);
//							if(!model.isSelected(p))
//								model.togglePersonSelected(p);
//							if(e.isShiftDown()) {
//								if(model.isSelected(s))
//									model.toggleSNPSelected(s);
//								if(model.isSelected(p))
//									model.togglePersonSelected(p);
//							}
//						} else {
//							model.setSNPSelection(s);
//							model.setPersonSelection(p);
//						}	
//					}
//				}
//			}
//		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void viewModelChanged(ViewModelEvent vme) {
		switch(vme.getChange()) {
		case ViewModelEvent.DATASET_CHANGED:
			repaint();
		case ViewModelEvent.COLUMN_SELECTION_CHANGED:
			repaint();
			break;
		case ViewModelEvent.ROW_SELECTION_CHANGED:
			repaint();
			break;
		case ViewModelEvent.ROW_SORTING_CHANGED:
			repaint();
			break;
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			repaint();
		}		
	}

	@Override
	public void paintPlot(Graphics2D g, int startx, int stopx, int starty,
			int stopy) {
		int cellWidth = viewModel.getSNPMapSetting().getCellWidth();
		int cellHeight = viewModel.getSNPMapSetting().getCellHeight();
		
		AffineTransform af = g.getTransform();
		double transX = startx * cellWidth;
		double transY = starty * cellHeight;
		af.translate(transX, transY);
		g.setTransform(af);
		
		boolean phasedData = viewModel.getSNPMapSetting().isPhased();
		boolean referenceEncoding = viewModel.getSNPMapSetting().isReferenceEncoding();
		
		for(int j = starty; j < stopy; j++) {
			AffineTransform af2 = g.getTransform();
			for(int i = startx; i < stopx; i++) {
				
				double f = 1.;
				
				Image r;
				
				if(phasedData) {
					NucVisObject nvo = viewModel.getSNVInColumn(i,j);
					char c = nvo.getChar();
					f = nvo.getFrequency();
					
					if(referenceEncoding) {
						int colInDataSet = viewModel.getColumnInDataSet(i);
						char ref = viewModel.getDataSet().getSNPs().get(colInDataSet).getReference();
						if(nvo.getChar() == ref) {
							r = snpMap.getImageProvider().getImage(ref, ref, ref);
						} else {
							r = snpMap.getImageProvider().getImage(nvo.getChar(), nvo.getChar(), ref);
						}
					} else {
						r = snpMap.getImageProvider().getImage(c);
					}
					
					if(f == 1) {
						g.drawImage(r,0,0,this);
					}
				} else {
					NucVisObject nvo = viewModel.getSNVInColumnUnphased(i,j);
					int colInDataSet = viewModel.getColumnInDataSet(i);
					char ref = viewModel.getDataSet().getSNPs().get(colInDataSet).getReference();
					
					char c1 = nvo.getCharPat();
					char c2 = nvo.getCharMat();
					
					f = nvo.getMeanFrequency();
					
					r = snpMap.getImageProvider().getImage(c1, c2, ref);
					if(f == 1) {
						g.drawImage(r,0,0,this);
					}
				}
				
				if(f < 1) {
					String aggregationEncoding = viewModel.getSNPMapSetting().getAggregationEncoding();
					
					switch(aggregationEncoding) {
					case SNPMapSetting.SATURATION:
						g.drawImage(r, 0, 0, this);
						VolatileImage s = snpMap.getImageProvider().getSaturationImage(f);
						g.drawImage(s,0,0,this);
						break;
					case SNPMapSetting.BOX_HEIGHT:
						int h = r.getHeight(this);
						int newHeihgt = (int)Math.rint(h * f);
						int h2 = h - newHeihgt;
						int w = r.getWidth(this);
						g.drawImage(r, 0, h2, w, h, 0, h2, w, h, this);
						break;
					}
				}
				
				if(viewModel.isColumnSelected(i) || viewModel.isRowSelected(j)) {
					r = snpMap.getImageProvider().getSelectionImage();
					g.drawImage(r, 0, 0, this);
				}
				
				g.translate(cellWidth, 0);
			}
			
			g.setTransform(af2);
			g.translate(0, cellHeight);
		}
		
		//reset transformation matrix
		g.setTransform(af);
	}

	@Override
	public void paintPlotAll(Graphics2D g) {
		int startx = 0;
		int starty = 0;
		int stopx = viewModel.numColsInVis();
		int stopy = viewModel.numRowsInVis();
		this.paintPlot(g, startx, stopx, starty, stopy);
	}

	@Override
	public Dimension getOptimalSize() {
		int width = viewModel.numColsInVis() * viewModel.getSNPMapSetting().getCellWidth();
		int height = viewModel.numRowsInVis() * viewModel.getSNPMapSetting().getCellHeight();
		return new Dimension(width, height);
	}

	public BufferedImage getImage() {
		int width = (int)getPreferredSize().getWidth();
		int height = (int)getPreferredSize().getHeight();
		if(width <= 3000 && height <= 3000) {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = image.getGraphics();
			this.paintPlotAll((Graphics2D)g);
			g.dispose();
			return image;
		}
		return null;
	}
}
