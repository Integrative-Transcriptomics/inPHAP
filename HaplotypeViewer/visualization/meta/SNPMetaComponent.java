package visualization.meta;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import viewmodel.ViewModel;
import visualization.HVComponent;
import visualization.SNPMap;

public class SNPMetaComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -150825471727735933L;

	private ViewModel viewModel;
	
	private SNPMetaHeader header;
	private SNPMetaData data;
	
	public SNPMetaComponent(ViewModel viewModel, SNPMap snpMap) {
		this.viewModel = viewModel;
		
		this.header = new SNPMetaHeader(viewModel);
		this.data = new SNPMetaData(viewModel, snpMap);
		
		JScrollPane headerScroller = new JScrollPane(header);
		JScrollPane dataScroller = new JScrollPane(data);
		
		headerScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		headerScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		headerScroller.setBorder(null);
		
		dataScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dataScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dataScroller.setBorder(null);
		
		JScrollBar hSBcomp = snpMap.compScroller.getHorizontalScrollBar();
		JScrollBar hSBdata = dataScroller.getHorizontalScrollBar();
		hSBdata.setModel(hSBcomp.getModel()); // synchronize scrollbars
		
		JScrollBar vSBdata = dataScroller.getVerticalScrollBar();
		JScrollBar vSBheader = headerScroller.getVerticalScrollBar();
		vSBdata.setModel(vSBheader.getModel()); // synchronize scrollbars
		
		this.setLayout(new BorderLayout());
		this.add(headerScroller, BorderLayout.WEST);
		this.add(dataScroller, BorderLayout.CENTER);
		
	}
	
	public void resizeComp(int rowWidth, int compWidth) {
		int height = viewModel.getSNPMapSetting().getMetaCellHeight() 
				* viewModel.numMetaRows();
		
		header.setPreferredSize(new Dimension(rowWidth, height));
		data.setPreferredSize(new Dimension(compWidth, height));
		
		header.revalidate();
		data.revalidate();
		
		header.repaint();
		data.repaint();
		
		revalidate();
		repaint();
	}

	public HVComponent getHeaderComponent() {
		return this.header;
	}
	
	public HVComponent getDataComponent() {
		return this.data;
	}
}
