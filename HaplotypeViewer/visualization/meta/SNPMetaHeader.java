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

import javax.swing.JComponent;

import sorting.MetaDataColumnSorter;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.HVComponent;
import events.ViewModelEvent;

public class SNPMetaHeader extends JComponent implements MouseListener, ViewModelListener, HVComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6730670229820106928L;

	private ViewModel viewModel;
	
	public SNPMetaHeader(ViewModel viewModel) {
		this.viewModel = viewModel;
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
		Graphics2D g2d = (Graphics2D)g;
		this.paintPlot(g2d, 0, 0, 0, 0);
	}
	
	@Override
	public void viewModelChanged(ViewModelEvent e) {
		switch(e.getChange()) {
		case ViewModelEvent.SNP_META_INFO_CHANGED:
			repaint();
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int y = e.getY();
		int index = y / viewModel.getSNPMapSetting().getMetaCellHeight();
		
		if(index < viewModel.numMetaRows()) {
			if(e.getClickCount() == 2 && !e.isControlDown()) {
				MetaDataColumnSorter sorter = new MetaDataColumnSorter(viewModel.getMetaRow(index), true);
				viewModel.sortColumns(sorter);
			}
			if(e.getClickCount() == 2 && e.isControlDown()) {
				MetaDataColumnSorter sorter = new MetaDataColumnSorter(viewModel.getMetaRow(index), false);
				viewModel.sortColumns(sorter);
			}
		}
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
	public void paintPlot(Graphics2D g2d, int startx, int stopx, int starty,
			int stopy) {
		g2d.setColor(Color.BLACK);
		
		int cellHeight = viewModel.getSNPMapSetting().getMetaCellHeight();
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2d.setFont(font);;
		
		AffineTransform af = g2d.getTransform();
		
		for(int i = 0; i < viewModel.numMetaRows(); i++) {
			String header = viewModel.getMetaRowID(i);
			Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(header, g2d);
			
			if(cellHeight < bounds.getHeight()/1.5) {
				break;
			}
			
			double w = getWidth()-bounds.getWidth();
			double h = (cellHeight - bounds.getHeight()) / 2.;
			
			g2d.translate(w, i * cellHeight+h);
			g2d.drawString(header, 0, 10);
			
			g2d.setTransform(af);
		}
		
		//reset transformation matrix
		g2d.setTransform(af);
	}

	@Override
	public void paintPlotAll(Graphics2D g) {
		this.paintPlot(g, 0, 0, 0, 0);
	}

	@Override
	public Dimension getOptimalSize() {
		int width = this.getWidth();
		int height = viewModel.numMetaRows() * viewModel.getSNPMapSetting().getMetaCellHeight();
		return new Dimension(width, height);
	}
}
