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

import sorting.MetaDataRowSorter;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.HVComponent;
import events.ViewModelEvent;

@SuppressWarnings("serial")
public class SubjectMetaHeader extends JComponent implements MouseListener, ViewModelListener, HVComponent {

	private ViewModel viewModel;
	
	public SubjectMetaHeader(ViewModel viewModel) {
		this.viewModel = viewModel;
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
	public void viewModelChanged(ViewModelEvent vme) {
		switch(vme.getChange()) {
		case ViewModelEvent.SUBJECT_META_INFO_CHANGED:
			repaint();
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int index = x / viewModel.getSNPMapSetting().getMetaCellWidth();
		
		if(index < viewModel.numMetaCols()) {
			if(e.getClickCount() == 2 && !e.isControlDown()) {
				MetaDataRowSorter sorter = new MetaDataRowSorter(viewModel, index, true);
				viewModel.sortRows(sorter);
			}
			if(e.getClickCount() == 2 && e.isControlDown()) {
				MetaDataRowSorter sorter = new MetaDataRowSorter(viewModel, index, false);
				viewModel.sortRows(sorter);
			}
		}
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
	public void paintPlot(Graphics2D g2d, int startx, int stopx, int starty,
			int stopy) {
		g2d.setColor(Color.BLACK);
		
		int cellWidth = viewModel.getSNPMapSetting().getMetaCellWidth();
		
		Font font = viewModel.getSNPMapSetting().getFont();
		g2d.setFont(font);
		
		AffineTransform af = g2d.getTransform();
		
		for(int i = 0; i < viewModel.numMetaCols(); i++) {
			String header = viewModel.getMetaColumnID(i);
			Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(header, g2d);
			
			if(cellWidth < bounds.getHeight()/1.5) {
				break;
			}
			
			g2d.translate((i+1) * cellWidth - (cellWidth - bounds.getHeight())/2., getHeight() - bounds.getWidth() - 5);
			g2d.rotate( Math.PI / 2 );
			
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
		int width = viewModel.numMetaCols() * viewModel.getSNPMapSetting().getMetaCellWidth();
		int height = this.getHeight();
		return new Dimension(width, height);
	}
}
