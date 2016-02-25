package visualization.utilities.gui.setuppers;

import gui.components.ColorPreview;
import gui.components.ColorPreview.ColorChangeEvent;
import gui.layout.ExcellentBoxLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.ColorGradient;
import visualization.utilities.agents.AbstractAgent;
import visualization.utilities.agents.Agent_Tricolore;
import visualization.utilities.gui.AbstractGradientSetupComponent;
import visualization.utilities.gui.GradientList;

public class SetupAgent extends AbstractGradientSetupComponent implements ActionListener, ChangeListener {

	protected JRadioButton usePredefined, useCustom;	
	protected ButtonGroup useWhich;	
	protected ColorPreview low, mid, high;
	protected JPanel p1;
	protected GradientList predefinedGradients;
	protected AbstractAgent state;
	
	public SetupAgent() {
		p1 = new JPanel(new ExcellentBoxLayout(true, 5));
		p1.setBorder(BorderFactory.createTitledBorder("Select gradient coloring"));		
		
		usePredefined = new JRadioButton("Predefined");
		useCustom = new JRadioButton("Custom");
		useWhich = new ButtonGroup();
		useWhich.add(usePredefined);
		useWhich.add(useCustom);
		useCustom.addActionListener(this);
		usePredefined.addActionListener(this);
		
		predefinedGradients = new SetupAgentList();
		predefinedGradients.addListener(this);
		
		JPanel p1a = new JPanel(new BorderLayout());
		p1a.add(usePredefined, BorderLayout.NORTH);
		JPanel jspp = new JPanel(new BorderLayout());
		JScrollPane jsp = new JScrollPane(predefinedGradients.getJComponent());
		jspp.add(jsp);
		jspp.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
		p1a.add(jspp, BorderLayout.CENTER);
		p1.add(p1a);
		
		JPanel p1b = new JPanel(new ExcellentBoxLayout(false, 5));
		p1b.add(useCustom);
		p1b.add(low = new ColorPreview(Color.red, true));
		p1b.add(mid = new ColorPreview(Color.black, true));
		p1b.add(high = new ColorPreview(Color.green, true));
		p1.add(p1b);
		low.addChangeListener(this);
		mid.addChangeListener(this);
		high.addChangeListener(this);
		low.setEditable(true);
		mid.setEditable(true);
		high.setEditable(true);
	}
	
	@Override
	public JComponent getJComponent() {
		return p1;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==predefinedGradients) {
			usePredefined.setSelected(true); // will trigger new event
		}
		fire();
	}

	public void stateChanged(ChangeEvent e) {
		if ((e.getSource()==low || e.getSource()==high || e.getSource()==mid) && e instanceof ColorChangeEvent) {
			useCustom.setSelected(true);
			fire();
		}
	}

	public void modifyGradient(ColorGradient c) {
		// only change the coloring!

		if (useCustom.isSelected()) {
			if (state instanceof Agent_Tricolore) {
				Agent_Tricolore theAgent = (Agent_Tricolore)state;
				theAgent.setLowColor(low.getColor());
				theAgent.setMidColor(mid.getColor());
				theAgent.setHighColor(high.getColor());
			} else {
				state = new Agent_Tricolore(false, low.getColor(), mid.getColor(), high.getColor(), 5d);
			}
			c.setAgent(state.clone());
		} else {
			predefinedGradients.modifyGradient(c);
			state=c.getAgent().clone();
		}
	}

	@Override
	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		silent=overrideEverything;
		
		predefinedGradients.updateFromGradient(c, overrideEverything);
		boolean isCustom = ((JList)predefinedGradients.getJComponent()).getSelectedValue()==null;
		
		useCustom.setSelected(isCustom);
		
		state = c.getAgent();

		// set the colored boxes correctly
		if (state instanceof Agent_Tricolore) {
			Agent_Tricolore at = (Agent_Tricolore)state;
			low.setColor(at.getLowColor());
			mid.setColor(at.getMidColor());
			high.setColor(at.getHighColor());
		}
		
		silent=false;
	}
}
