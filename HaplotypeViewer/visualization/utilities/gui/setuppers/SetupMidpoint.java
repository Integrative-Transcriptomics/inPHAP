package visualization.utilities.gui.setuppers;

import gui.layout.ExcellentBoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.AbstractGradientSetupComponent;
import visualization.utilities.gui.GradientSetupComponent;

public class SetupMidpoint extends AbstractGradientSetupComponent implements ActionListener {
		
	protected JPanel p3;
	protected JCheckBox symmetrical;
	protected Double customMP;
	protected GradientSetupComponent midpointLocation;

	
	public SetupMidpoint() {
		p3 = new JPanel(new ExcellentBoxLayout(true, 5));
		p3.setBorder(BorderFactory.createTitledBorder("Midpoint location"));
		symmetrical = new JCheckBox("Symmetrical around midpoint");
		symmetrical.addActionListener(this);
		p3.add(symmetrical);
		midpointLocation = new SetupMidpointList();
		midpointLocation.addListener(this);
		p3.add(midpointLocation.getJComponent());
		
	}

	public JComponent getJComponent() {
		return p3;
	}

	public void modifyGradient(ColorGradient c) {
		c.setSymmetrical(symmetrical.isSelected());
		midpointLocation.modifyGradient(c);
	}


	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		silent = overrideEverything;
		symmetrical.setSelected(c.isSymmetrical());
		midpointLocation.updateFromGradient(c, overrideEverything);
		silent = false;
	}

	public void actionPerformed(ActionEvent e) { //via symmetrical, midpointlocation
		fire();
	}

}
