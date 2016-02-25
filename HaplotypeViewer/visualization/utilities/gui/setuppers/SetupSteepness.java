package visualization.utilities.gui.setuppers;

import gui.layout.ExcellentBoxLayout;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.ColorGradient;
import visualization.utilities.agents.Agent_Tricolore;
import visualization.utilities.gui.AbstractGradientSetupComponent;

public class SetupSteepness extends AbstractGradientSetupComponent {

	protected JSlider steepness;
	protected JPanel rp;
	protected JLabel theLabel;
	protected final static String labelPre = "Gradient steepness";


	protected final static Double[] steepnessOptions = {
		200d, 175d, 150d, 125d, 100d, 90d, 80d, 70d, 60d, 50d, 40d, 30d, 20d, 10d, 5d, 2d, 1d, .1};  

	public SetupSteepness() {
		steepness = new JSlider(0, steepnessOptions.length, 0);		
		rp = new JPanel(new ExcellentBoxLayout(true, 5));
		theLabel = new JLabel(labelPre);
		rp.add(theLabel);
		rp.add(steepness);
		Dimension d = rp.getPreferredSize();
		d.width=Integer.MAX_VALUE;
		rp.setMaximumSize(d);
		rp.setPreferredSize(d);
		steepness.setPaintTicks(true);
		steepness.setMajorTickSpacing(1);
		steepness.setSnapToTicks(true);

		steepness.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				updateLabel();
				fire();
			}

		});
		
		updateLabel();

	}
	
	protected void updateLabel() {
		String labelT;
		if (steepness.getValue()==0)
			labelT = "Linear";
		else
			labelT = "Sigmoid, steepness "+(steepnessOptions[steepness.getValue()-1]);
		theLabel.setText(labelPre+": "+labelT);
	}

	public JComponent getJComponent() {
		return rp;
	}

	public void modifyGradient(ColorGradient c) {
		if (c.getAgent() instanceof Agent_Tricolore) {
			Agent_Tricolore theAgent = (Agent_Tricolore)c.getAgent();
			if (steepness.getValue()==0)
				theAgent.setSigmoid(false);
			else {
				theAgent.setSigmoid(true);
				theAgent.setSteepness(steepnessOptions[steepness.getValue()-1]);
			}
		}				
	}


	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		if (overrideEverything) {
			silent = true;
			if (c.getAgent() instanceof Agent_Tricolore) {
				Agent_Tricolore theAgent = (Agent_Tricolore)c.getAgent();

				double newSteep = theAgent.getSteepness();
				boolean newSig = theAgent.isSigmoid();
				if (newSig) {					
					for (int i=0; i!=steepnessOptions.length; ++i)
						if (steepnessOptions[i]==newSteep)
							steepness.setValue(i+1);
				} else {
					steepness.setValue(0);
				}

			}
			silent = false;
		}
	}
}
