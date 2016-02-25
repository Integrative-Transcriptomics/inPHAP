package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import events.ViewModelEvent;

@SuppressWarnings("serial")
public class SNPMapColumnHeader extends JComponent implements MouseListener, ViewModelListener, MouseMotionListener, HVComponent {

	private SNPMap snpMap;
	private ViewModel viewModel;
	
	public SNPMapColumnHeader(ViewModel viewModel, SNPMap snpMap) {
		this.viewModel = viewModel;
		this.snpMap = snpMap;
		this.viewModel.addViewModelListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
		int cellWidth = viewModel.getSNPMapSetting().getCellWidth();
		
		Rectangle vBounds = snpMap.compScroller.getVisibleRect();
		int startx = snpMap.compScroller.getHorizontalScrollBar().getValue();
		int start = Math.max((int)Math.floor(startx / cellWidth), 0);
		int stop = (int)Math.ceil((startx + vBounds.width) / cellWidth) + 1;
		stop = Math.min(stop, viewModel.numColsInVis());
		
		this.paintPlot(g2, start, stop, 0, 0);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int x = e.getX();
			int cellSize = viewModel.getSNPMapSetting().getCellWidth();
			int snpIndex = x / cellSize;
			
			if(snpIndex < viewModel.numColsInVis()) {
				if(e.isControlDown()) {
					viewModel.toggleColumnSelected(snpIndex);
				} else {
					viewModel.selectColumn(snpIndex);
				}
			}
		}
	}
	
	private int startIndex = -1;
	private int stopIndex = -1;

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int x = e.getX();
			int cellSize = viewModel.getSNPMapSetting().getCellWidth();
			startIndex = x / cellSize;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int x = e.getX();
			int cellSize = viewModel.getSNPMapSetting().getCellWidth();
			stopIndex = x / cellSize;
			
			if(dragged) {
				dragged = false;
				
				if(startIndex > stopIndex) {
					int tmp = startIndex;
					startIndex = stopIndex;
					stopIndex = tmp;
				}
				
				if(startIndex < viewModel.numColsInVis() && startIndex >= 0
						&& stopIndex < viewModel.numColsInVis() && stopIndex >= 0) {
					
					if(startIndex != stopIndex) {
						Set<Integer> selection = new HashSet<Integer>();
						for(int i = startIndex; i <= stopIndex; i++) {
							selection.add(viewModel.getColumnInDataSet(i));
						}
						
						Set<Integer> newSelection = new HashSet<Integer>(viewModel.getSelectedColumns());
						
						if(e.isControlDown()) {
							if(newSelection.containsAll(selection)) {
								newSelection.removeAll(selection);
							} else {
								newSelection.addAll(selection);
							}
						} else {
							newSelection = selection;
						}
						viewModel.setColumnSelection(newSelection);
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void viewModelChanged(ViewModelEvent vme) {
		switch(vme.getChange()) {
		case ViewModelEvent.COLUMN_SELECTION_CHANGED:
			repaint();
			break;
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			repaint();
			break;
		}
	}
	
	private boolean dragged = false;

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void paintPlot(Graphics2D g2, int start, int stop, int starty,
			int stopy) {
		int cellWidth = viewModel.getSNPMapSetting().getCellWidth();
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2.setFont(font);
		
		AffineTransform af = g2.getTransform();
		double transX = start * cellWidth;
		af.translate(transX, 0);
		g2.setTransform(af);
		
		Color c = viewModel.getSNPMapSetting().getSelectionColor();
		Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
		Rectangle2D rec = new Rectangle2D.Double(0, 0, getHeight(), cellWidth);
		
		int count = 0;
		for(int i = start; i < stop; i++) {
			String cID;
			Rectangle2D bounds = g2.getFontMetrics().getStringBounds(cID = viewModel.getColumnID(i), g2);

			if(cellWidth < bounds.getHeight())
				break;
			
			if(viewModel.isColumnSelected(i)) {
				g2.translate((count+1) * cellWidth, 0);
				g2.rotate( Math.PI / 2 );
				g2.setColor(c2);
				g2.fill(rec);
				g2.setColor(Color.BLACK);
				g2.setTransform(af);
			}
			
			g2.translate(count * cellWidth, getHeight() - bounds.getWidth() - 5);
			g2.rotate( Math.PI / 2. );
			
			g2.translate(0, -bounds.getHeight());
			g2.translate(0, -(cellWidth - bounds.getHeight())/2.);
			//g2.drawRect(0, 0, (int)bounds.getWidth(), (int)bounds.getHeight());
			g2.translate(0, bounds.getHeight());
			g2.drawString(cID, 0, 0);
			
			g2.setTransform(af);
			
			count++;
		}
		
		//reset transformation matrix
		g2.setTransform(af);
	}

	@Override
	public void paintPlotAll(Graphics2D g) {
		int startx = 0;
		int stopx = viewModel.numColsInVis();
		this.paintPlot(g, startx, stopx, 0, 0);
	}

	@Override
	public Dimension getOptimalSize() {
		int width = viewModel.numColsInVis() * viewModel.getSNPMapSetting().getCellWidth();
		int height = this.getHeight();
		return new Dimension(width, height);
	}
}
