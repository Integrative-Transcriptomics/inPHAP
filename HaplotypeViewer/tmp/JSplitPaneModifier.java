package tmp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.jtattoo.demo.app.GUIProperties;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;


public class JSplitPaneModifier {

	public static void main(String[] args) throws Exception{
		
		Properties props = new Properties();
		props.put("linuxStyleScrollBar", "on");
		AcrylLookAndFeel.setCurrentTheme(props);
		UIManager.setLookAndFeel(GUIProperties.PLAF_ACRYL);
		
		
		JFrame frame = new JFrame("SplitPane Modifier");
		
		JPanel left = new JPanel();
		left.setBorder(BorderFactory.createEmptyBorder());
		left.setBackground(Color.YELLOW);
		left.setPreferredSize(new Dimension(100,100));
		JPanel right = new JPanel();
		right.setBorder(BorderFactory.createEmptyBorder());
		right.setBackground(Color.RED);
		right.setPreferredSize(new Dimension(100,100));
		
		JPanel down_right = new JPanel();
		down_right.setBorder(BorderFactory.createEmptyBorder());
		down_right.setBackground(Color.GREEN);
		down_right.setPreferredSize(new Dimension(100,100));
		
		JPanel down_left = new JPanel();
		down_left.setBorder(BorderFactory.createEmptyBorder());
		down_left.setBackground(Color.BLUE);
		down_left.setPreferredSize(new Dimension(100,100));
		
		JSplitPaneModifier m = new JSplitPaneModifier();
		
		//a new splitpane with a zero size divider! this is much nicer than the usual very thick divider!
		JSplitPaneWithZeroSizeDivider splitterH1 = m.new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT, left, right);
		splitterH1.setOneTouchExpandable(true);
		splitterH1.setDividerLocation(0.5);
		splitterH1.setResizeWeight(1);
		
		JSplitPaneWithZeroSizeDivider splitterH2 = m.new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT, down_left, down_right);
		splitterH2.setOneTouchExpandable(true);
		splitterH2.setDividerLocation(0.5);
		splitterH2.setResizeWeight(1);
		
		JSplitPaneWithZeroSizeDivider splitterV = m.new JSplitPaneWithZeroSizeDivider(JSplitPane.VERTICAL_SPLIT, splitterH1, splitterH2);
		splitterV.setResizeWeight(1);
		
		frame.add(splitterV);
		
		JMenu menu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		menu.add(newItem);
		menu.addSeparator();
		menu.add(exitItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);
		
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	
	public class JSplitPaneWithZeroSizeDivider extends JSplitPane {
		private int dividerDragSize= 10;
		private int dividerDragOffset = dividerDragSize / 2;
		
		public JSplitPaneWithZeroSizeDivider(int orientation, JComponent left, JComponent right) {
			super(orientation, left, right);
			setDividerSize(3);
			setContinuousLayout(true);
		}
		
		@Override
		public void layout() {
			super.layout();
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI)getUI()).getDivider();
			Rectangle bounds = divider.getBounds();
			if(orientation == HORIZONTAL_SPLIT) {
				bounds.x -= dividerDragOffset;
				bounds.width = dividerDragSize;
			} else {
				bounds.y -= dividerDragOffset;
				bounds.height = dividerDragSize;
			}
			
			divider.setBounds(bounds);
		}
		
		@Override
		public void updateUI() {
			setUI(new SplitPaneWithZeroSizeDividerUI());
		}
		
		private class SplitPaneWithZeroSizeDividerUI extends BasicSplitPaneUI {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new ZeroSizeDivider(this);
			}
		}
		
		private class ZeroSizeDivider extends BasicSplitPaneDivider {
			public ZeroSizeDivider(BasicSplitPaneUI ui) {
				super(ui);
				super.setBorder(null);
				setBackground(UIManager.getColor("controlShadow"));
			}
			
			@Override
			public void setBorder(Border border) {
				//do nothing
			}
			
			public void paint(Graphics g) {
				g.setColor(getBackground());
				if(orientation == HORIZONTAL_SPLIT) {
					g.drawLine(dividerDragOffset+1, 0, dividerDragOffset+1, getHeight()-1);
				} else {
					g.drawLine(0, dividerDragOffset+1, getWidth()-1, dividerDragOffset+1);
				}
			}
			
			@Override
			protected void dragDividerTo(int location) {
				super.dragDividerTo(location + dividerDragOffset);
			}
			
			@Override
			protected void finishDraggingTo(int location) {
				super.finishDraggingTo(location + dividerDragOffset);
			}
		}
	}
}
