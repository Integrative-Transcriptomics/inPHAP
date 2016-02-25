package visualization.utilities.agents;

import java.awt.Color;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.GradientSetupComponent;

public abstract class AbstractAgentArray implements AbstractAgent {

	protected Color[] colors;	
	protected ColorGradient parent;

	public Color getColor(int index) {
		return colors[index];
	}

	public GradientSetupComponent getSetupComponent() {		
		return null;
	}

	public void setColorGradient(ColorGradient parent) {
		this.parent = parent;
		colors=null;
	}

	public boolean needsUpdating() {
		return colors==null;
	}
	
	public AbstractAgent clone() {
		throw new RuntimeException("Amusing");
	}
	
}