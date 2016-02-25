package visualization.meta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.VolatileImage;

import javax.swing.JComponent;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.HVComponent;
import visualization.SNPMap;
import visualization.utilities.ColorSelector;
import events.ViewModelEvent;

@SuppressWarnings("serial")
public class SubjectMetaData extends JComponent implements MouseListener, ViewModelListener, HVComponent {
	
	private ViewModel viewModel;
	private SNPMap snpMap;
	
	public SubjectMetaData(SNPMap snpMap, ViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		this.snpMap = snpMap;
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.white);
		g2.clearRect(0,0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paint(g2);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		this.paintPlot(g2d, 0, 0, 0, 0);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//TODO react on click event
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void viewModelChanged(ViewModelEvent vme) {
		switch(vme.getChange()) {
		case ViewModelEvent.ROW_SELECTION_CHANGED:
			//TODO
			repaint();
			break;
		}
	}

	@Override
	public void paintPlot(Graphics2D g2d, int startx, int stopx, int starty,
			int stopy) {
		g2d.setColor(Color.BLACK);
		
		int cellWidth = viewModel.getSNPMapSetting().getMetaCellWidth();
		int cellHeight = viewModel.getSNPMapSetting().getCellHeight();
		
		int arcWidth = 0;
		int arcHeight = 0;
		
		if(cellWidth >= 4) {
			arcWidth = 2;
		}
		
		if(arcHeight >= 4) {
			arcHeight = 2;
		}
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2d.setFont(font);
		
		RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, cellWidth, cellHeight, arcWidth, arcHeight);
		
		AffineTransform afOri = g2d.getTransform();
		
		for(int i = 0; i < viewModel.numMetaCols(); i++) {
			AffineTransform af = g2d.getTransform();
			ColorSelector cg = viewModel.getSNPMapSetting().getMetaColumnColorSelector(i);
			
			//start drawing as soon as color gradients are ready
			if(cg == null) {
				continue;
			}
			
			String oldText = "";
			
			for(int j = 0; j < viewModel.numRowsInVis(); j++) {
				Double value = viewModel.getMetaColumnValue(i, j);
				String text = viewModel.getMetaColumnText(i, j);
				
				if(value == null) {
					VolatileImage image = snpMap.getImageProvider().getNAImageColumn();
					g2d.drawImage(image, 0, 0, this);
				} else {
					Color c = cg.mapValueToColor(value);
					g2d.setColor(c);
					Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(text, g2d);
					
					g2d.fill(rect);
					
					if(cellHeight >= bounds.getWidth() + 2) {
						if(oldText.compareTo(text) != 0) {
							Color c2 = g2d.getColor();
							g2d.setColor(Color.BLACK);
							AffineTransform af2 = g2d.getTransform();
							g2d.rotate( Math.PI / 2 );
							double transX = -(cellWidth - bounds.getHeight())/2.;
							g2d.translate(2, transX);
							g2d.drawString(text, 0, 0);
							g2d.setTransform(af2);
							g2d.setColor(c2);
						}
					} else if(cellWidth >= bounds.getWidth() + 2) {
						if(oldText.compareTo(text) != 0) {
							Color c2 = g2d.getColor();
							g2d.setColor(Color.BLACK);
							AffineTransform af2 = g2d.getTransform();
							g2d.translate(2, bounds.getHeight());
							g2d.drawString(text, 0, 0);
							g2d.setTransform(af2);
							g2d.setColor(c2);
						}
					}
				}
				
				oldText = text;
				
				if(viewModel.isRowSelected(j)) {
					g2d.drawImage(snpMap.getImageProvider().getMetaColumnSelectionImage(), 0, 0, this);
				}
				
				g2d.translate(0, cellHeight);
			}
			
			g2d.setTransform(af);
			g2d.translate(cellWidth, 0);
		}
		
		//reset transformation matrix
		g2d.setTransform(afOri);
	}

	@Override
	public void paintPlotAll(Graphics2D g) {
		this.paintPlot(g, 0, 0, 0, 0);
	}

	@Override
	public Dimension getOptimalSize() {
		int width = viewModel.numMetaCols() * viewModel.getSNPMapSetting().getMetaCellWidth();
		int height = viewModel.numRowsInVis() * viewModel.getSNPMapSetting().getCellHeight();
		return new Dimension(width, height);
	}
}
