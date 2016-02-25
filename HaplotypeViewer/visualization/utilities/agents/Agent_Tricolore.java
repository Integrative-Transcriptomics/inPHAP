package visualization.utilities.agents;

import gui.GUIUtilities;

import java.awt.Color;
import java.util.ArrayList;

import visualization.utilities.ColorGradient.MIDPOINT_MODE;
import visualization.utilities.gui.GradientSetupComponent;
import visualization.utilities.gui.setuppers.SetupSteepness;

public class Agent_Tricolore extends AbstractAgentArray {

	protected boolean sigmoid;	
	protected Color lowColor;
	protected Color midColor;
	protected Color highColor;
	protected double steepness;
	
	public void updateColors() {
		double totalRange = parent.isSymmetrical() ? (2*parent.getLargeWidth()) : (parent.getMax()-parent.getMin());
		double lowerRange = parent.isSymmetrical()? parent.getLargeWidth() : parent.getMidpoint()-parent.getMin();			
		double upperRange = parent.isSymmetrical()? parent.getLargeWidth() : parent.getMax()-parent.getMidpoint();

		double lowerRangePart = lowerRange / totalRange;
		double upperRangePart = upperRange / totalRange;

		int lowerSteps = (int)(Math.round((double)(parent.getResolution()-1) * lowerRangePart));
		int upperSteps = (int)(Math.round((double)(parent.getResolution()-1) * upperRangePart));

		ArrayList<Color> upColors, dnColors;

//		checkMidColor();
		
		if (midColor == null) {
			
			if (sigmoid) {
				upColors = GUIUtilities.computeSemiSigmoidColorGradient( lowColor, highColor, parent.getResolution()-1, false, steepness );
			} else {
				upColors = GUIUtilities.computeLinearColorGradient( lowColor, highColor, parent.getResolution()-1, false );
			}
			ArrayList<Color> finalList = new ArrayList<Color>(upColors);
			finalList.add(highColor);
			colors = finalList.toArray(new Color[0]);
			
		} else {
		
			if (sigmoid) {
			// 	center color is part of upper range, see ColorGradient.mapValueToColor
				upColors = GUIUtilities.computeSemiSigmoidColorGradient( midColor, highColor, upperSteps, false, steepness );
				dnColors = GUIUtilities.computeSemiSigmoidColorGradient( midColor, lowColor, lowerSteps, false, steepness );				
			} else {
			// 	center color is part of upper range, see ColorGradient.mapValueToColor
				upColors = GUIUtilities.computeLinearColorGradient( midColor, highColor, upperSteps, false );
				dnColors = GUIUtilities.computeLinearColorGradient( midColor, lowColor, lowerSteps, false );
			}

			dnColors = GUIUtilities.reverseColorGradient(dnColors);

			ArrayList<Color> finalList = new ArrayList<Color>(dnColors);
			finalList.add(midColor);
			finalList.addAll(upColors);
			colors = finalList.toArray(new Color[0]);
			
		}
			
	}
		
	protected void checkMidColor() {
//		if (midColor==null) {
//			if (parent!=null) {
//				if (parent.midpointMode == MIDPOINT_MODE.Minimum)
//					midColor = lowColor;
//				else if (parent.midpointMode==MIDPOINT_MODE.Maximum)
//					midColor = highColor;
//			}
//			if (midColor==null) {
//				// interpolate
//				midColor = new Color( 
//					(lowColor.getRed()+highColor.getRed())/2,
//					(lowColor.getBlue()+highColor.getBlue())/2,
//					(lowColor.getGreen()+highColor.getGreen())/2
//					);
//			}
//		} 
	}
	
	public Agent_Tricolore clone() {
		return new Agent_Tricolore(this);
	}

	public Agent_Tricolore(Agent_Tricolore other) {
		copySettings(other);
	}	
	
	public Agent_Tricolore(boolean Sigmoid, Color Low, Color Mid, Color High, Double Steepness) {
		sigmoid=Sigmoid;
		lowColor=Low;
		highColor=High;
		steepness=Steepness;
		setMidColor(Mid);
	}
	
	public boolean equals(AbstractAgent otherAgent) {
		if (otherAgent instanceof Agent_Tricolore) {
			Agent_Tricolore at = (Agent_Tricolore)otherAgent;
			boolean midColEq = at.midColor==null && this.midColor==null || (at.midColor!=null && this.midColor!=null && at.midColor.equals(midColor));
			return at.sigmoid==sigmoid && at.lowColor.equals(lowColor) && midColEq
			&& at.highColor.equals(highColor) && at.steepness==steepness;
		}
		return false;
	}

	
	public void setLowColor(Color LowColor) {
		if (lowColor.getRGB()!=lowColor.getRGB())
			colors=null;
		lowColor = LowColor;
	}

	public void setHighColor(Color HighColor) {
		if (highColor.getRGB()!=HighColor.getRGB())
			colors=null;
		highColor = HighColor;
	}


	public void setMidColor(Color MidColor) {
		if (MidColor==null) {
			setMidColor(lowColor);
			if (parent!=null)
				parent.setMidpointMode(MIDPOINT_MODE.Minimum);
			colors=null;
		} else {
			if (midColor==null || midColor.getRed()!=MidColor.getRGB())
				colors=null;
			midColor = MidColor;
		}
	}

	public void setSteepness(double Steepness) {
		if (steepness!=Steepness)
			colors=null;
		steepness = Steepness;
	}

	public void setSigmoid(boolean Sigmoid) {
		if (sigmoid!=Sigmoid)
			colors=null;
		sigmoid=Sigmoid;
	}
	
	public boolean isSigmoid() {
		return sigmoid;
	}

	public Color getLowColor() {
		return lowColor;
	}

	public Color getMidColor() {
		return midColor;
	}

	public Color getHighColor() {
		return highColor;
	}

	public double getSteepness() {
		return steepness;
	}
	
	
	public void copySettings(Agent_Tricolore otherGradient) {
		lowColor = otherGradient.lowColor;
		highColor =otherGradient.highColor;
		midColor = otherGradient.midColor;
		steepness = otherGradient.steepness;
		sigmoid = otherGradient.sigmoid;
	}
	
	public GradientSetupComponent getSetupComponent() {		
		return new SetupSteepness();
	}
	
	public void deserialize(String s) {
		String[] part = s.split(":");
		lowColor = new Color(Integer.parseInt(part[0]));
		midColor = new Color(Integer.parseInt(part[1]));
		highColor = new Color(Integer.parseInt(part[2]));
		steepness = Double.parseDouble(part[3]);
		sigmoid = Boolean.parseBoolean(part[4]);
	}

	@Override
	public String serialize() {
		return lowColor.getRGB()+":"+midColor.getRGB()+":"+highColor.getRGB()+":"+steepness+":"+sigmoid;
	}
	
		
//		return new GradientList(true) {
//
//			public void update() {
//				int i=getSelectedIndex();
//				silent=true;
//				dlm.clear();
//				if (baseGradient.getAgent() instanceof Agent_Tricolore) {
//					ColorGradient cgneu = new ColorGradient(baseGradient);
//					((Agent_Tricolore)cgneu.getAgent()).setSigmoid(false);
//					cgneu.setName("Linear");
//					dlm.addElement(cgneu);
//					for (double steep : steepnessOptions) {
//						cgneu = new ColorGradient(baseGradient);
//						((Agent_Tricolore)cgneu.getAgent()).setSigmoid(true);
//						((Agent_Tricolore)cgneu.getAgent()).setSteepness(steep);
//						cgneu.setName("Sigmoid, "+steep);
//						dlm.addElement(cgneu);
//					}
//				}
//				silent=false;
//				if (i>=0 && i<dlm.getSize())
//					setSelectedIndex(i);
//			}
//			
//		};
	

}