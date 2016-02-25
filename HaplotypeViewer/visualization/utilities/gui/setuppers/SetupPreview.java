package visualization.utilities.gui.setuppers;

import javax.swing.JComponent;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.AbstractGradientSetupComponent;
import visualization.utilities.gui.GradientPreviewPanel;

public class SetupPreview extends AbstractGradientSetupComponent {
		
	protected GradientPreviewPanel preview;
	
	public SetupPreview() {
		preview = new GradientPreviewPanel(ColorGradient.createDefaultGradient(0, 16));
	}

	public JComponent getJComponent() {
		return preview;
	}

	public void modifyGradient(ColorGradient c) {
		//void
	}


	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		preview.setGradient(c);
		
	}

}
