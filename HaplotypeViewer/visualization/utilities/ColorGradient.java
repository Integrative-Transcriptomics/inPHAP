package visualization.utilities;

import java.awt.Color;

import visualization.utilities.agents.AbstractAgent;
import visualization.utilities.agents.Agent_Rainbow;
import visualization.utilities.agents.Agent_Tricolore;

public class ColorGradient extends ColorSelector {
	
	public enum MIDPOINT_MODE { 
		Center, Minimum, Maximum, Zero, Custom;
		
		double getValue(double min, double max, double currentmidpoint) {
			switch (this) {
			case Minimum: return min;
			case Maximum: return max;
			case Zero: return Math.max(0.0, min);
			case Custom: return Math.max(min,Math.min(max,currentmidpoint)); // don not change input value, only make safe			
			default: return (max-min)/2 + min; // == CENTER
			}
		}
	};
	
	private boolean needsUpdate = true;
	protected int resolution;
	protected double min, max, midpoint, largeWidth;
	protected MIDPOINT_MODE midpointMode;
	protected boolean symmetrical;
	protected String name;
	
	protected AbstractAgent agent; 
	// this is the REAL color gradient maker, it can be replaced without replacing the outer gradient object


	public static ColorGradient createRainbowGradient(double Min, double Max) {
		ColorGradient ret = new ColorGradient(Min, (Max-Min)/2d+Min, Max, true, 1024, MIDPOINT_MODE.Center, new Agent_Rainbow());
		ret.setName("Rainbow");
		return ret;
	}
	
	public static ColorGradient createDefaultGradient(double Min, double Max) {
		Color a = PredefinedGradients.COLOR_BREWER_DARK2.getLower();
		Color b = PredefinedGradients.COLOR_BREWER_DARK2.getMid();
		Color c = PredefinedGradients.COLOR_BREWER_DARK2.getUpper();
		return new ColorGradient(Min, (Max-Min)/2d+Min, Max, true, 1024, MIDPOINT_MODE.Center, new Agent_Tricolore(false, a, b, c, 0.) );
	}

	
	public ColorGradient(ColorGradient other) {
		copySettings(other);
	}
	
	public ColorGradient(double Min, double Mid, double Max, boolean Symmetrical, int Resolution, MIDPOINT_MODE MidpointMode, AbstractAgent agent) {
		min=Min;
		max=Max;
		midpoint=Mid;
		midpointMode = MidpointMode;
		symmetrical=Symmetrical;
		resolution=Resolution;
		this.agent = agent;
		this.agent.setColorGradient(this);
	}
	
	public final int getResolution() {
		return resolution;
	}
	
	public final void setResolution(int resolution) {
		if (this.resolution!=resolution) {
			this.resolution = resolution;
			markUpdate();
		}
	}
	
	public final boolean isSymmetrical() {
		return symmetrical;
	}

	public final void setSymmetrical(boolean Symmetrical) {
		if (symmetrical!=Symmetrical) {
			symmetrical=Symmetrical;
			markUpdate();			
		}
	}
	
	
	public final double getMin() {
		return min;
	}

	public final double getMax() {
		return max;
	}

	public final double getMidpoint() {
		return midpoint;
	}

	public final MIDPOINT_MODE getMidpointMode() {
		return midpointMode;
	}

	public final void setMin(double min) {
		if (this.min!=min) {
			this.min = min;
			markUpdate();
		}
	}
	
	public final void setMax(double max) {
		if (this.max!=max) {
			this.max = max;
			markUpdate();
		}
	}
	
	public final void setMidpoint(double midpoint) {
		if (this.midpoint!=midpoint) {
			this.midpoint = midpoint;
			markUpdate();
		}
	}
	
	public final void setMidpointMode(MIDPOINT_MODE mode) {
		this.midpointMode = mode;
		double newMidpoint = mode.getValue(min, max, midpoint);
		if (newMidpoint!=midpoint)
			markUpdate();
	}
	
	
	protected synchronized void checkAndUpdateColors() {		
		if (needsUpdate || agent.needsUpdating()) {
			midpoint = midpointMode.getValue(min, max, midpoint);
			if (max<midpoint || midpoint<min)
				throw new RuntimeException("Gradient is not sane.");
			largeWidth = Math.max(max-midpoint, midpoint-min);
			agent.setColorGradient(this);
			agent.updateColors();
			needsUpdate = false;
		}
	}

	protected synchronized void markUpdate() {
		needsUpdate = true;
	}
	
	public final double mapValueToPercentage(double value) {
		checkAndUpdateColors();
		
		double lowerBound, range, gradientStart;
		
		double totalRange = symmetrical ? (2*largeWidth) : (max-min);
		
		if (value<midpoint || value==midpoint&&midpointMode==MIDPOINT_MODE.Maximum) { //use left half of the gradient
		
			lowerBound = symmetrical? midpoint-largeWidth : min;
			range = symmetrical? largeWidth : midpoint-min;			
			gradientStart = 0;
			
		} else {
			
			lowerBound = midpoint;
			range = symmetrical? largeWidth : max-midpoint;
			gradientStart = ( totalRange-range ) / totalRange; // in symmetrical case this is 0.5
			
		}

		// percentage in the gradient sub part
		double mappedValue = (value - lowerBound) / range;
		
		// percentage respective to the whole gradient
		double gradientPart = range / totalRange; // in symmetrical case this is 0.5
		mappedValue = mappedValue * gradientPart;
		mappedValue += gradientStart;
		
		return mappedValue;
	}
	
	public final int mapValueToIndex(double value) {
		return (int)(mapValueToPercentage(value) * (resolution-1));
	}
	
	public final Color mapValueToColor(double value) {

		if (max-min<=0) 
			return Color.black;
		
		return getColor(mapValueToIndex(value));
	}
	
	public Color getColor(int index) {
		if (needsUpdate || agent.needsUpdating())
			checkAndUpdateColors();
		return agent.getColor(index);
	}

	public void copySettings(ColorGradient otherGradient) {
		min=otherGradient.min;
		max=otherGradient.max;
		midpoint=otherGradient.midpoint;
		symmetrical=otherGradient.symmetrical;
		resolution=otherGradient.resolution;
		setMidpointMode(otherGradient.midpointMode);
		this.agent = otherGradient.agent.clone();
		this.agent.setColorGradient(this);
		markUpdate();
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;		
	}

	public String toString() {
		return "Color Gradient \""+getName()+"\"";
	}
	
	public AbstractAgent getAgent() {
		return agent;
	}
	
	public void setAgent(AbstractAgent aa) {
		if (agent!=aa) {
			agent=aa;
			agent.setColorGradient(this);
			markUpdate();
		}			
	}
	
	public boolean equals(ColorGradient otherGradient) {
		return min==otherGradient.min 
					&& max==otherGradient.max 
					&& midpoint==otherGradient.midpoint 
					&& symmetrical==otherGradient.symmetrical
					&& resolution==otherGradient.resolution
					&& midpointMode == otherGradient.midpointMode
					&& agent.equals(otherGradient.getAgent());
	}
	
	public String serialize() {
		String s = min+";"+midpoint+";"+max+";"+symmetrical+";"+resolution+";"+midpointMode.ordinal()+";";
		if (agent instanceof Agent_Tricolore) {   // could be pluggable
			s+="Tricolore";
		} else {
			s+="Rainbow";
		}
		s+=";"+agent.serialize();	
		return s;
	}
	
	public void deserialize(String s) {
		try {
			String[] part = s.split(";");
			min = Double.parseDouble(part[0]);
			midpoint = Double.parseDouble(part[1]);
			max = Double.parseDouble(part[2]);
			symmetrical = Boolean.parseBoolean(part[3]);
			resolution = Integer.parseInt(part[4]);
			midpointMode = MIDPOINT_MODE.values()[Integer.parseInt(part[5])];
			if (part[6].equals("Tricolore")) // could be pluggable
				setAgent(new Agent_Tricolore(false,Color.black,Color.black,Color.black,0d));
			else
				setAgent(new Agent_Rainbow());
			if (part.length>7)
				agent.deserialize(part[7]);
		} catch (Exception frig) {
			System.out.println("Could not deserialize ColorGradient from "+s);
		}
	}
	
	/** this is internal to the agents */
	public double getLargeWidth() {
		return largeWidth;
	}
}
