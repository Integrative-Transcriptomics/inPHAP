package visualization.utilities.gui.setuppers;

import gui.layout.ExcellentBoxLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.AbstractGradientSetupComponent;
import visualization.utilities.gui.GradientSetupComponent;

public class SetupExtraOptions extends AbstractGradientSetupComponent implements ActionListener {
		
	protected JPanel p2;
	protected GradientSetupComponent extraSetting;
	
	public SetupExtraOptions() {
		p2 = new JPanel(new ExcellentBoxLayout(true,0));
		p2.setBorder(BorderFactory.createTitledBorder("Extra gradient options"));
		
	}

	public JComponent getJComponent() {
		return p2;
	}

	public void actionPerformed(ActionEvent e) {
		fire();
	}

	public void modifyGradient(ColorGradient c) {
		if (extraSetting!=null)
			extraSetting.modifyGradient(c);
	}

	@Override
	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		GradientSetupComponent newgsc = c.getAgent().getSetupComponent();
		
		if (extraSetting==null || newgsc==null || extraSetting.getClass()!=newgsc.getClass()) {
			
			if (extraSetting!=null) {
				extraSetting.removeListener(this);
				p2.removeAll();
				p2.setVisible(false);
			}
			extraSetting = newgsc;
		
			if (extraSetting!=null) {
				extraSetting.addListener(this);
				p2.add(extraSetting.getJComponent(), BorderLayout.CENTER);
				p2.setVisible(true);
			}
			
		}
		
		if (extraSetting!=null)
			extraSetting.updateFromGradient(c, overrideEverything);
		
		if (!overrideEverything)
			fire();
		
	}

}
