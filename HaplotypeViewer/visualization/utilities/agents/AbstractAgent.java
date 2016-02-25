package visualization.utilities.agents;

import java.awt.Color;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.GradientSetupComponent;

public interface AbstractAgent {
	
	public void setColorGradient(ColorGradient parent);
	
	public Color getColor(int index);
	
	public void updateColors();
	
	public boolean needsUpdating();
	
	public AbstractAgent clone();
	
	public GradientSetupComponent getSetupComponent();
	
	public boolean equals(AbstractAgent otherAgent);

	public String serialize();
	public void deserialize(String s);
}
