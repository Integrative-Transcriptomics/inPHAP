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
public class SNPMapRowHeader extends JComponent implements MouseListener, ViewModelListener, MouseMotionListener, HVComponent {

	private ViewModel viewModel;
	private SNPMap snpMap;
	
	public SNPMapRowHeader(ViewModel viewModel, SNPMap snpMap) {
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
		
		int cellHeight = viewModel.getSNPMapSetting().getCellHeight();

		Rectangle vBounds = snpMap.compScroller.getVisibleRect();
		int starty = snpMap.compScroller.getVerticalScrollBar().getValue();
		int start2 = Math.max((int)Math.floor(starty / cellHeight), 0);
		int stop2 = (int)Math.ceil((starty + vBounds.height) / cellHeight) + 1;
		stop2 = Math.min(stop2, viewModel.numRowsInVis());
		
		this.paintPlot(g2, 0, 0, start2, stop2);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int y = e.getY();
			int cellSize = viewModel.getSNPMapSetting().getCellHeight();
			Integer index = y / cellSize;
			
			if(index < viewModel.numRowsInVis()) {
				if(e.isControlDown()) {
					viewModel.toggleRowSelected(index);
				} else {
					viewModel.selectRow(index);
				}
			}
		}
	}

	private int startIndex = -1;
	private int stopIndex = -1;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int y = e.getY();
			int cellSize = viewModel.getSNPMapSetting().getCellHeight();
			startIndex = y / cellSize;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int y = e.getY();
			int cellSize = viewModel.getSNPMapSetting().getCellHeight();
			stopIndex = y / cellSize;
			
			if(dragged) {
				dragged = false;
				
				if(startIndex > stopIndex) {
					int tmp = startIndex;
					startIndex = stopIndex;
					stopIndex = tmp;
				}
				
				if(startIndex < viewModel.numRowsInVis() && startIndex >= 0
						&& stopIndex < viewModel.numRowsInVis() && stopIndex >= 0) {
					
					if(startIndex != stopIndex) {
						Set<Integer> selection = new HashSet<Integer>();
						for(int i = startIndex; i <= stopIndex; i++) {
							selection.addAll(viewModel.getRowsInDataSet(i));
						}
						
						Set<Integer> newSelection = new HashSet<Integer>(viewModel.getSelectedRows());
						
						if(e.isControlDown()) {
							if(newSelection.containsAll(selection)) {
								newSelection.removeAll(selection);
							} else {
								newSelection.addAll(selection);
							}
						} else {
							newSelection = selection;
						}
						viewModel.setRowSelection(newSelection);
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
		case ViewModelEvent.ROW_SELECTION_CHANGED:
			repaint();
			break;
		case ViewModelEvent.ROW_SORTING_CHANGED:
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
	public void paintPlot(Graphics2D g2, int startx, int stopx, int starty,
			int stopy) {
		int cellHeight = viewModel.getSNPMapSetting().getCellHeight();
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2.setFont(font);
		
		AffineTransform af = g2.getTransform();
		double transY = starty * cellHeight;
		af.translate(0, transY);
		g2.setTransform(af);

		Color selectionColor = viewModel.getSNPMapSetting().getSelectionColor();
		Color c = new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 100);
		Rectangle2D rec = new Rectangle2D.Double(0, 0, getWidth(), cellHeight);
		
		int count = 0;
		for(int i = starty; i < stopy; i++) {
			Rectangle2D bounds = g2.getFontMetrics().getStringBounds(viewModel.getRowID(i), g2);
		
			if(cellHeight < bounds.getHeight()/1.5)
				break;
			
			g2.translate(getWidth()-bounds.getWidth(), count * cellHeight + 1);
			count++;
			
			if(viewModel.isRowSelected(i)) {
				g2.setColor(c);
				g2.fill(rec);
				g2.setColor(Color.BLACK);
			}
			
			g2.translate(0, bounds.getHeight() + (cellHeight - bounds.getHeight())/2. - 2);
			g2.drawString(viewModel.getRowID(i), 0, 0);
			g2.setTransform(af);
		}
		
		//reset transformation matrix
		g2.setTransform(af);
	}

	@Override
	public void paintPlotAll(Graphics2D g) {
		int starty = 0;
		int stopy = viewModel.numRowsInVis();
		this.paintPlot(g, 0, 0, starty, stopy);
	}

	@Override
	public Dimension getOptimalSize() {
		int width = getWidth();
		int height = viewModel.numRowsInVis() * viewModel.getSNPMapSetting().getCellHeight();
		return new Dimension(width, height);
	}
}
