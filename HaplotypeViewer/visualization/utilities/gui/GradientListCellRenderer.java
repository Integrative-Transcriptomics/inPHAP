package visualization.utilities.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import visualization.utilities.ColorGradient;

@SuppressWarnings("serial")
public class GradientListCellRenderer extends JPanel implements TableCellRenderer, ListCellRenderer {

	protected JLabel gradName = new JLabel("X");
	protected SimpleGradientComponent gradComp;
	protected DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
	protected DefaultListCellRenderer lcr = new DefaultListCellRenderer();
	
	public GradientListCellRenderer(boolean useFullGradient) {
		setLayout(null);
		add(gradComp= new SimpleGradientComponent(null, useFullGradient));
		add(gradName);
	}
	
	public Dimension getPreferredSize() {
		Dimension d1 = gradName.getPreferredSize();
		Dimension d2 = gradComp.getPreferredSize();
		d1.width+=d2.width+5;
		d1.height = Math.max(d1.height, d2.height);
		return d1;
	}
	
	protected void commonSetup(JComponent c, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		// recompute widths
		
		int totalW = c.getWidth();
		int gW = (int)(.6*totalW);
		int tW = totalW-gW-5;
		
		gradName.setSize(tW,(int)gradName.getPreferredSize().getHeight());//tW, c.getHeight());
		gradComp.setSize(gW-2, gradName.getHeight()+50);
		gradName.setLocation(gW+5, 0);
		gradComp.setLocation(0, 0);

		gradName.setForeground(getForeground());
		gradName.setBackground(getBackground());
		gradName.setOpaque(false);
		gradName.setText(((ColorGradient)value).getName());
		
		gradComp.setGradient(((ColorGradient)value));			
		
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {		
		commonSetup(table, value, isSelected, hasFocus, row, column);
		tcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		this.setForeground(tcr.getForeground());
		this.setBackground(tcr.getBackground());
		this.setBorder(tcr.getBorder());
		return this;
	}


	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		lcr.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		this.setForeground(lcr.getForeground());
		this.setBackground(lcr.getBackground());
		this.setBorder(lcr.getBorder());

		commonSetup(list, value, isSelected, cellHasFocus, index, 0);
		return this;
	}




}
