package visualization.utilities.gui;

import gui.layout.ExcellentBoxLayout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.colormaps.BasicColorMap;
import visualization.utilities.colormaps.ColorMap;

public class ColorMapEditorPanel extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7171049148454481363L;
	
	private ColorMap base;
	private ColorMap state;
	
	private ColorMapSelectionPanel selectionPanel;
	
	public ColorMapEditorPanel(ColorMap colorMap) {
		super(new ExcellentBoxLayout(true, 5));
		this.base = colorMap;
		this.state = new BasicColorMap(this.base);
		
		Integer[] numClasses = new Integer[]{3,4,5,6,7,8,9,10,11,12};
		final JComboBox<Integer> numClassSelection = new JComboBox<Integer>(numClasses);
		numClassSelection.setSelectedItem(state.getSize());
		
		numClassSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer selection = (Integer)numClassSelection.getSelectedItem();
//				System.out.println("Num classes changed: " + selection);
				state.setSize(selection.intValue());
//				System.out.println(state.getTitle());
//				System.out.println(state.getSize());
			}
		});
		
		JPanel classSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		classSelectionPanel.add(new JLabel("Number of data classes"));
		classSelectionPanel.add(numClassSelection);
		
		this.selectionPanel = new ColorMapSelectionPanel(); 
		this.selectionPanel.addChangeListener(this);
		
		this.add(classSelectionPanel);
		this.add(this.selectionPanel);
	}
	
	public void apply() {
		this.base.copySettings(this.state);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.state = selectionPanel.getColorMap();
		System.out.println("ColorMap changed to " + this.state.getTitle());
	}
}
