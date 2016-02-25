package visualization.utilities.gui.setuppers;

import gui.layout.ExcellentBoxLayout;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.AbstractGradientSetupComponent;

public class SetupResolution extends AbstractGradientSetupComponent {
	
	protected static final Integer[] resolutionOptions = { 3, 7, 15, 31, 63, 127, 255, 512, 1024 };
	
	protected ArrayList<Integer> myOptions;
	
	protected JPanel rp;
	protected JSlider resolution;
	
	public SetupResolution() {
		resolution = new JSlider(0, 1, 1);

		rp = new JPanel(new ExcellentBoxLayout(false, 5));
		rp.setBorder(BorderFactory.createTitledBorder("Number of colors"));
		rp.add(resolution);
		Dimension d = rp.getPreferredSize();
		d.width=Integer.MAX_VALUE;
		rp.setMaximumSize(d);
		
		myOptions = new ArrayList<Integer>();
		for (int i : resolutionOptions)
			myOptions.add(i);

		makeLabelTable();
		
		resolution.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fire();
			}
		});
		resolution.setPaintTicks(true);
		resolution.setPaintLabels(true);
		resolution.setMajorTickSpacing(1);
		resolution.setSnapToTicks(true);
		
	}
	
	protected void makeLabelTable() {
		Dictionary<Integer, JComponent> dict = new Hashtable<Integer, JComponent>();
		for (int i =0; i!=myOptions.size(); ++i) {
			dict.put(i, new JLabel(""+myOptions.get(i)));
		}		
		
		resolution.setLabelTable(dict);
		
		if (resolution.getMaximum()!=dict.size()-1) {
			resolution.setMaximum(dict.size()-1);
			resolution.setValue(dict.size()-1);
		}
	}

	public JComponent getJComponent() {
		return rp;
	}

	public void modifyGradient(ColorGradient c) {
		c.setResolution(myOptions.get(resolution.getValue()));
	}

	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		silent=overrideEverything;
		boolean found = false;
		for (int i=0; i!=myOptions.size(); ++i) {
			if (myOptions.get(i)==c.getResolution()) {
				resolution.setValue(i);
				found = true;
			}
		}
		if (!found) {
			myOptions.add(c.getResolution());
			makeLabelTable();
		}
		silent=false;
	}


}
