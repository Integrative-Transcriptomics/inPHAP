package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import viewmodel.ViewModel;
import visualization.meta.SNPMetaComponent;
import visualization.meta.SubjectMetaComponent;
import visualization.utilities.ImageProvider;
import events.ViewModelEvent;

/**
 * @author jaeger
 *
 */
@SuppressWarnings("serial")
public class SNPMap extends AbstractVisualization {
	
	private Integer rowWidthIndex = null, colHeightIndex = null, metaColHeightIndex = null, metaRowWidthIndex = null;
	
	protected SNPMapComponent snpMapComponent;
	private SNPMapColumnHeader columnHeader;
	private SNPMapRowHeader rowHeader;
	private SNPMapOverview snpMapOverview;
	private JPanel colPanel;
	
	public JScrollPane compScroller;
	public JScrollPane rowScroller;
	public JScrollPane colScroller;
	
	private PlaceHolder placeHolderRight;
	
	public SubjectMetaComponent subjectMetaComp;
	public SNPMetaComponent snpMetaComp;
	
	private JSplitPane splitterH;
	private JSplitPane splitterV;
	private JSplitPane splitterH2;
	
	private JPanel mainPanel;
	public ViewModel viewModel;
	
	private ImageProvider imageProvider;
	
	/**
	 * @param projectHandler
	 */
	public SNPMap(ViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.setVisComponent(this);
		this.viewModel.addViewModelListener(this);
		this.setLayout(new BorderLayout());
		
		this.imageProvider = new ImageProvider(this, viewModel);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		snpMapComponent = new SNPMapComponent(viewModel, this);
		compScroller = new JScrollPane(snpMapComponent);
		mainPanel.add(compScroller, BorderLayout.CENTER);
		
		snpMapOverview = new SNPMapOverview(this);
		
		colPanel = new JPanel(new BorderLayout());
		colPanel.add(snpMapOverview, BorderLayout.WEST);
		columnHeader = new SNPMapColumnHeader(viewModel, this);
		colScroller = new JScrollPane(columnHeader);
		colPanel.add(colScroller, BorderLayout.CENTER);
		colPanel.add(placeHolderRight = new PlaceHolder(), BorderLayout.EAST);
		mainPanel.add(colPanel, BorderLayout.NORTH);
		
		rowHeader = new SNPMapRowHeader(viewModel, this);
		rowScroller = new JScrollPane(rowHeader);
		mainPanel.add(rowScroller, BorderLayout.WEST);
		
		JScrollBar hSBcomp = compScroller.getHorizontalScrollBar();
		JScrollBar hSBcol = colScroller.getHorizontalScrollBar();
		hSBcol.setModel(hSBcomp.getModel()); // synchronize scrollbars
		//hide scrollbars
		colScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		colScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		
		JScrollBar vSBcomp = compScroller.getVerticalScrollBar();
		JScrollBar vSBrow = rowScroller.getVerticalScrollBar();
		vSBrow.setModel(vSBcomp.getModel()); // synchronize scrollbars
		//hide scrollbars
		rowScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		rowScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		rowScroller.setBorder(null);
		colScroller.setBorder(null);
		
		compScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		compScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		subjectMetaComp = new SubjectMetaComponent(viewModel, this);
		
		splitterH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, subjectMetaComp);
		splitterH.setOneTouchExpandable(true);
		splitterH.setDividerLocation(0.8);
		splitterH.setResizeWeight(1);
		splitterH.setContinuousLayout(true);
		
		Dimension dim = new Dimension(0,0);
		mainPanel.setMinimumSize(dim);
		subjectMetaComp.setMinimumSize(dim);
		
		snpMetaComp = new SNPMetaComponent(viewModel, this);
		
		JScrollPane snpMetaCompScroller = new JScrollPane(snpMetaComp);
		snpMetaCompScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		snpMetaCompScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		snpMetaCompScroller.setBorder(null);
		
		splitterH2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, snpMetaComp, new PlaceHolder());
		splitterH2.setDividerLocation(0.8);
		splitterH2.setResizeWeight(1);
		splitterH2.setContinuousLayout(true);
		
		splitterV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitterH, splitterH2);
		splitterV.setOneTouchExpandable(true);
		splitterV.setDividerLocation(0.8);
		splitterV.setResizeWeight(1);
		splitterV.setContinuousLayout(true);
		
		new SplitPaneSynchronizer(splitterH, splitterH2);
		
		this.add(splitterV, BorderLayout.CENTER);
	}
	
	public ImageProvider getImageProvider() {
		return this.imageProvider;
	}
	
	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		case ViewModelEvent.DATASET_CHANGED:
			this.imageProvider.setGraphicsConfiguration(this);
			resize();
			break;
		case ViewModelEvent.COLUMN_SORTING_CHANGED:
			//TODO
			break;
		case ViewModelEvent.ROW_SORTING_CHANGED:
			repaint();
			break;
		case ViewModelEvent.ROW_SELECTION_CHANGED:
			//TODO
			break;
		case ViewModelEvent.COLUMN_SELECTION_CHANGED:
			//TODO
			break;
		case ViewModelEvent.PLOT_RESIZE:
			resize();
			break;
		case ViewModelEvent.SUBJECT_META_INFO_CHANGED:
			colHeightIndex = null;
			resize();
			break;
		case ViewModelEvent.SNP_META_INFO_CHANGED:
			rowWidthIndex = null;
			resize();
			break;
		case ViewModelEvent.SETTINGS_CHANGED:
			rowWidthIndex = null;
			colHeightIndex = null;
			resize();
			break;
		case ViewModelEvent.COLOR_GRADIENT_CHANGED:
			repaint();
			break;
		case ViewModelEvent.COLUMN_JUMP_EVENT:
			Integer colJumpIndex = (Integer)e.getSource();
			int xValue = colJumpIndex * viewModel.getSNPMapSetting().getCellWidth();
			this.compScroller.getHorizontalScrollBar().setValue(xValue);
			break;
		case ViewModelEvent.ROW_JUMP_EVENT:
			Integer rowJumpIndex = (Integer)e.getSource();
			int yValue = rowJumpIndex * viewModel.getSNPMapSetting().getCellHeight();
			this.compScroller.getVerticalScrollBar().setValue(yValue);
			break;
		case ViewModelEvent.AGGREGATION_CHANGED:
			this.rowWidthIndex = null;
			resize();
			break;
		case ViewModelEvent.SNPS_FILTERED:
			resize();
			break;
		}
	}
	
	public JComponent getExportComponent() {
		ExportComponent c = new ExportComponent(this);
		return c;
	}
	
	public void resize() {
		int compWidth = viewModel.getSNPMapSetting().getCellWidth() * viewModel.numColsInVis();
		int compHeight = viewModel.getSNPMapSetting().getCellHeight() * viewModel.numRowsInVis();
		
		Graphics g = this.getGraphics();
		Font font = viewModel.getSNPMapSetting().getFont();
		((Graphics2D)g).setFont(font);
		
		int offset = 10;
		//calculate optimal header panel sizes
		int rowWidth = getRowHeaderSize(g) + offset;
		int colHeight = getColumnHeaderSize(g) + offset;
		int metaColHeight = getMetaColumnHeaderSize(g) + offset;
		int metaRowWidth = getMetaRowHeaderSize(g) + offset;
		
		//adjust rowWidth and colHeight
		colHeight = Math.max(colHeight, metaColHeight);
		rowWidth = Math.max(rowWidth, metaRowWidth);
		
		imageProvider.redrawImages();
		
		int scrollBarSize = (int)compScroller.getVerticalScrollBar().getPreferredSize().getWidth();
		
		snpMapComponent.setPreferredSize(new Dimension(compWidth, compHeight));
		columnHeader.setPreferredSize(new Dimension(compWidth, colHeight));
		rowHeader.setPreferredSize(new Dimension(rowWidth, compHeight));
		snpMapOverview.resizePlot(rowWidth, colHeight);
		placeHolderRight.setPreferredSize(new Dimension(scrollBarSize, colHeight));
		
		snpMapOverview.revalidate();
		snpMapOverview.repaint();
		placeHolderRight.revalidate();
		placeHolderRight.repaint();
		
		snpMapComponent.resizePlot();
		
		mainPanel.revalidate();
		mainPanel.repaint();
		
		columnHeader.revalidate();
		columnHeader.repaint();
		
		rowHeader.revalidate();
		rowHeader.repaint();
		
		subjectMetaComp.resizeComp(colHeight, compHeight);
		snpMetaComp.resizeComp(rowWidth, compWidth);
		
		splitterH.setDividerLocation(0.9);
		splitterV.setDividerLocation(0.9);
		splitterH.repaint();
		splitterV.repaint();
		
		revalidate();
		repaint();
	}

	private int getMetaRowHeaderSize(Graphics g) {
		if(metaRowWidthIndex != null) {
			String id = viewModel.getMetaRowID(metaRowWidthIndex);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			return (int)Math.rint(bounds.getWidth());
		}
		
		int metaRowWidth = 0;
		
		for(int i = 0; i < viewModel.numMetaRows(); i++) {
			String id = viewModel.getMetaRowID(i);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			if(bounds.getWidth() > metaRowWidth) {
				metaRowWidth = (int)Math.rint(bounds.getWidth());
				metaRowWidthIndex = i;
			}
		}
		
		return metaRowWidth;
	}

	private int getMetaColumnHeaderSize(Graphics g) {
		if(metaColHeightIndex != null) {
			String id = viewModel.getMetaColumnID(metaColHeightIndex);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			return (int)Math.rint(bounds.getWidth());
		}
		
		int metaColHeight = 0;
		
		for(int i = 0; i < viewModel.numMetaCols(); i++) {
			String id = viewModel.getMetaColumnID(i);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			if(bounds.getWidth() > metaColHeight) {
				metaColHeight = (int)Math.rint(bounds.getWidth());
				metaColHeightIndex = i;
			}
		}
		
		return metaColHeight;
	}

	private int getColumnHeaderSize(Graphics g) {
		if(colHeightIndex != null) {
			String id = viewModel.getDataSet().getSNPs().get(colHeightIndex).getRsid();
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			return (int)Math.rint(bounds.getWidth());
		}
		
		int colHeight = 0;
		
		for(int i = 0; i < viewModel.numColsInVis(); i++) {
			String id = viewModel.getColumnID(i);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			if(bounds.getWidth() > colHeight) {
				colHeight = (int)Math.rint(bounds.getWidth());
				colHeightIndex = i;
			}
		}
		
		return colHeight;
	}
	
	private int getRowHeaderSize(Graphics g) {
		
		if(rowWidthIndex != null) {
			String id = viewModel.getDataSet().getSubjects().get(rowWidthIndex).getName();
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			return (int)Math.rint(bounds.getWidth());
		}
		
		int rowWidth = 0;
		
		for(int i = 0; i < viewModel.numRowsInVis(); i++) {
			String id = viewModel.getRowID(i);
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(id, g);
			if(bounds.getWidth() > rowWidth) {
				rowWidth = (int)Math.rint(bounds.getWidth());
				rowWidthIndex = i;
			}
		}
		
		return rowWidth;
	}

	@Override
	public void updatePlot() {
		repaint();
	}
	
	private class PlaceHolder extends JComponent {
		
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setBackground(Color.white);
			g2.clearRect(0,0, getWidth(), getHeight());
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paint(g2);
		}
		
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, getWidth(), getHeight());
		}
	}

	public JPanel getPlaceHolderForOverview() {
		JPanel placeHolder = new JPanel();
		placeHolder.setBackground(Color.WHITE);
		placeHolder.setSize(this.snpMapOverview.getSize());
		placeHolder.setPreferredSize(this.snpMapOverview.getSize());
		return placeHolder;
	}

	public HVComponent getMapColumnHeader() {
		return this.columnHeader;
	}
	
	public HVComponent getMapRowHeader() {
		return this.rowHeader;
	}
	
	public HVComponent getMapDataComponent() {
		return this.snpMapComponent;
	}
	
	public HVComponent getMetaRowHeader() {
		return this.snpMetaComp.getHeaderComponent();
	}
	
	public HVComponent getMetaRowDataComponent() {
		return this.snpMetaComp.getDataComponent();
	}
	
	public HVComponent getMetaColumnHeader() {
		return this.subjectMetaComp.getHeaderComponent();
	}
	
	public HVComponent getMetaColumnDataComponent() {
		return this.subjectMetaComp.getDataComponent();
	}
}
