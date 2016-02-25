package visualization.meta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.HVComponent;
import visualization.SNPMap;
import visualization.utilities.ColorSelector;
import events.ViewModelEvent;

public class SNPMetaData extends JComponent implements ViewModelListener, HVComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4809746566579048733L;

	private ViewModel viewModel;
	private SNPMap snpMap;
	
	public SNPMetaData(ViewModel viewModel, SNPMap snpMap) {
		this.viewModel = viewModel;
		this.snpMap = snpMap;
		this.viewModel.addViewModelListener(this);
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
	public void viewModelChanged(ViewModelEvent e) {
		switch(e.getChange()) {
		case ViewModelEvent.COLOR_GRADIENT_CHANGED:
			repaint();
			break;
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			repaint();
			break;
		case ViewModelEvent.SNP_META_INFO_CHANGED:
			repaint();
			break;
		case ViewModelEvent.COLUMN_SELECTION_CHANGED:
			repaint();
			break;
		}
	}

	@Override
	public void paintPlot(Graphics2D g2d, int startx, int stopx, int starty,
			int stopy) {
		g2d.setColor(Color.BLACK);
		
		int cellWidth = viewModel.getSNPMapSetting().getCellWidth();
		int cellHeight = viewModel.getSNPMapSetting().getMetaCellHeight();
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2d.setFont(font);
		
		int arcWidth = 0;
		int arcHeight = 0;
		
		if(cellWidth >= 4) {
			arcWidth = 2;
		}
		
		if(cellHeight >= 4) {
			arcHeight = 2;
		}
		
		RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, cellWidth, cellHeight, arcWidth, arcHeight);
		
		AffineTransform afOri = g2d.getTransform();
		
		for(int i = 0; i < viewModel.numMetaRows(); i++) {
			AffineTransform af = g2d.getTransform();
			ColorSelector cg = viewModel.getSNPMapSetting().getMetaRowColorSelector(i);

			//start drawing as soon as color gradients are ready
			if(cg == null) {
				continue;
			}
			
			String oldText = "";
			
			for(int j = 0; j < viewModel.numColsInVis(); j++) {
				Double value = viewModel.getMetaRowValue(j, i);
				String text = (String)viewModel.getMetaRow(i).getValue(value);
				
				if(value == null) {
					g2d.drawImage(snpMap.getImageProvider().getNAImageRow(), 0, 0, this);
				} else {
					Color c = cg.mapValueToColor(value);
					g2d.setColor(c);
					g2d.fill(rect);
					
					Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(text, g2d);
					
					if(cellHeight >= bounds.getWidth() + 2) {
						if(text != null && oldText != null)
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
					}
				}
				
				oldText = text;
				
				if(viewModel.isColumnSelected(j)) {
					g2d.drawImage(snpMap.getImageProvider().getMetaRowSelectionImage(), 0, 0, this);
				}
				
				g2d.translate(cellWidth, 0);
			}
			
			g2d.setTransform(af);
			g2d.translate(0, cellHeight);
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
		int width = viewModel.numColsInVis() * viewModel.getSNPMapSetting().getCellWidth();
		int height = viewModel.numMetaRows() * viewModel.getSNPMapSetting().getMetaCellHeight();
		return new Dimension(width, height);
	}
}
