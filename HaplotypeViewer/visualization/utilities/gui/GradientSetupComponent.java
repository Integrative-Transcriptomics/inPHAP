package visualization.utilities.gui;

import java.awt.event.ActionListener;

import javax.swing.JComponent;

import visualization.utilities.ColorGradient;

public interface GradientSetupComponent {

	public void addListener(ActionListener l);	
	public void removeListener(ActionListener l);
	
	public JComponent getJComponent();
	
	public void modifyGradient(ColorGradient c);
	
	public void updateFromGradient(ColorGradient c, boolean overrideEverything);
		
}
