package visualization.meta;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import viewmodel.ViewModel;
import visualization.HVComponent;
import visualization.SNPMap;

@SuppressWarnings("serial")
public class SubjectMetaComponent extends JComponent {
	
	public SubjectMetaHeader colHeader;
	public SubjectMetaData dataComp;
	
	public JScrollPane headerScroller;
	public JScrollPane dataScroller;
	
	private ViewModel viewModel;
	
	public SubjectMetaComponent(ViewModel viewModel, SNPMap snpMap) {
		this.viewModel = viewModel;
		
		colHeader = new SubjectMetaHeader(viewModel);
		dataComp = new SubjectMetaData(snpMap, viewModel);
		
		headerScroller = new JScrollPane(colHeader);
		dataScroller = new JScrollPane(dataComp);
		
		dataScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		dataScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dataScroller.setBorder(null);
		
		JScrollBar vSBcomp = snpMap.compScroller.getVerticalScrollBar();
		JScrollBar vSBdata = dataScroller.getVerticalScrollBar();
		vSBdata.setModel(vSBcomp.getModel()); // synchronize scrollbars
		
		headerScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		headerScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		headerScroller.setBorder(null);
		
		JScrollBar hSBdata = dataScroller.getHorizontalScrollBar();
		JScrollBar hSBheader = headerScroller.getHorizontalScrollBar();
		hSBdata.setModel(hSBheader.getModel()); // synchronize scrollbars
		
		this.setLayout(new BorderLayout());
		
		add(headerScroller, BorderLayout.NORTH);
		add(dataScroller, BorderLayout.CENTER);
	}

	public void resizeComp(int colHeight, int compHeight) {
		int width = viewModel.getSNPMapSetting().getMetaCellWidth() * viewModel.numMetaCols();

		colHeader.setPreferredSize(new Dimension(width, colHeight));
		dataComp.setPreferredSize(new Dimension(width, compHeight));
		
		colHeader.revalidate();
		dataComp.revalidate();
		
		colHeader.repaint();
		dataComp.repaint();
		
		revalidate();
		repaint();
	}

	public HVComponent getHeaderComponent() {
		return this.colHeader;
	}

	public HVComponent getDataComponent() {
		return this.dataComp;
	}
}
