package gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcellentBoxLayout implements LayoutManager2 {

	protected List<Component> layoutComponents = new LinkedList<Component>();
	protected Dimension min, pref, max;
	protected boolean isVertical;
	protected int spacing;

	public ExcellentBoxLayout(boolean vertical, int spacing) {
		isVertical = vertical;
		this.spacing = spacing;
	}
	
	public void addLayoutComponent(String name, Component comp) {
		layoutComponents.add(comp);
		min=pref=max=null;
	}

	public void layoutContainer(Container parent) {
		Dimension total = parent.getSize();
		// remove insets
		int iT = parent.getInsets().top,
		    iB = parent.getInsets().bottom,
		    iL = parent.getInsets().left,
		    iR = parent.getInsets().right;
		total.width-=iL+iR;
		total.height-=iT+iB;
		
		
		/* the total size needs to be distributed to all contained components
		 * first each component will get its minimum size
		 * then the remaining space is given to all comps until their max size is reached
		 * then the rest is left empty to the bottom/right of the container
		 */
		HashMap<Component, Dimension> cdims = new HashMap<Component,Dimension>();
		
		// first assign the minimum extend to all components
		for (Component c : layoutComponents) {
			Dimension ms = c.getMinimumSize();
			cdims.put(c,ms);
			setFixedExtend(c, ms, getFixedExtend(total));
			incVariableExtend(total, -(getVariableExtend(ms)+spacing));
		}

		
		if (getVariableExtend(total)>0) {
			
			// now the remaining available space is in total.variableExtend
			// we will distribute this fairly
		
			LinkedList<Component> growable = new LinkedList<Component>();
			for (Component c : layoutComponents)
				if (canGrow(c, cdims.get(c)))
					growable.add(c);

			while (growable.size()>0 && getVariableExtend(total)>0) {
				// now distribute the remaining space equally to all components, if they want as much
				int perComp = getVariableExtend(total) / growable.size();
				if (perComp<1)
					break;
				Iterator<Component> ic = growable.iterator();
				while (ic.hasNext()) {
					Component c = ic.next();				
					Dimension d = cdims.get(c);
					int oldSize = getVariableExtend(d);				
					int newSize = incVariableExtend(c, d, perComp);
					if (newSize<oldSize+perComp)
						ic.remove();
					incVariableExtend(total, -(newSize-oldSize));
				}
			}
			
		} else if (getVariableExtend(total)<0) {
			
			// there is NOT enough space. so every component will be made smaller, somewhat fairly
			int missingExtend = -getVariableExtend(total);
			int sum=0;
			
			for (Component c : layoutComponents) {
				int vex = getVariableExtend(cdims.get(c));
				sum+=vex;
			}

			// reduce each component by a relative amount
			for (Component c : layoutComponents) {
				Dimension d = cdims.get(c);
				int vex = getVariableExtend(cdims.get(c));
				double perc = (double)vex/(double)sum;
				int reduction = (int)(perc*missingExtend);
				setVariableExtend(d, vex-reduction);
			}
			
		}
		
		
		//assign sizes
		Dimension start = new Dimension();
		start.height+=iT;
		start.width+=iL;
		for (Component c : layoutComponents) {
			Dimension d = cdims.get(c);
			c.setSize(d);
			c.setLocation(start.width, start.height);
			incVariableExtend(start, getVariableExtend(d)+spacing);
		}
	}
	
	public Dimension minimumLayoutSize(Container parent) {
		if (min==null) {
			min = new Dimension();
			for (Component c:layoutComponents) {
				Dimension ms = c.getMinimumSize();
				incVariableExtend(min, getVariableExtend(ms));
				setFixedExtend(min, Math.max(getFixedExtend(min),getFixedExtend(ms)));
			}	
			if (!layoutComponents.isEmpty())
				incVariableExtend(min, spacing*layoutComponents.size()-1);
			min.width+=parent.getInsets().left+parent.getInsets().right;
			min.height+=parent.getInsets().top+parent.getInsets().bottom;
		}
		return min;
	}

	public Dimension preferredLayoutSize(Container parent) {
		if (pref==null) {
			pref = new Dimension();
			for (Component c:layoutComponents) {
				Dimension ps = c.getPreferredSize();
				incVariableExtend(pref, getVariableExtend(ps));
				setFixedExtend(pref, Math.max(getFixedExtend(pref),getFixedExtend(ps)));
			}	
			if (!layoutComponents.isEmpty())
				incVariableExtend(pref, spacing*layoutComponents.size()-1);
			pref.width+=parent.getInsets().left+parent.getInsets().right;
			pref.height+=parent.getInsets().top+parent.getInsets().bottom;
		}		
		return pref;
	}
	

	public Dimension maximumLayoutSize(Container parent) {
		if (max==null) {
			max = new Dimension();
			for (Component c:layoutComponents) {
				Dimension ps = c.getMaximumSize();
				int ve =  getVariableExtend(ps);
				incVariableExtend(max,ve, Integer.MAX_VALUE);
				setFixedExtend(max, Math.max(getFixedExtend(max),getFixedExtend(ps)));
			}	
			if (!layoutComponents.isEmpty())
				incVariableExtend(max, spacing*layoutComponents.size()-1, Integer.MAX_VALUE);
			max.width+=parent.getInsets().left+parent.getInsets().right;
			max.height+=parent.getInsets().top+parent.getInsets().bottom;
			if (max.width<0)
				max.width=Integer.MAX_VALUE;
			if (max.height<0)
				max.height=Integer.MAX_VALUE;
		}
		return max;
	}
	
	
	protected int setVariableExtend(Component c, Dimension d, int value) {
		int min = getVariableExtend(c.getMinimumSize());
		int max = getVariableExtend(c.getMaximumSize());
		value = Math.max(min, Math.min(max, value));
		setVariableExtend(d, value);
		return value;
	}
	
	
	protected int incVariableExtend(Component c, Dimension d, int delta) {
		int val = getVariableExtend(d)+delta;
		if (getVariableExtend(d)>0 && delta>0 && val<0)
			val = 0;
		return setVariableExtend(c, d, val);
	}
	
	protected void incVariableExtend(Dimension d, int delta) {
		int val = getVariableExtend(d)+delta;
		if (getVariableExtend(d)>0 && delta>0 && val<0)
			val = 0;		
		setVariableExtend(d, val);
	}
	
	protected void incVariableExtend(Dimension d, int delta, int overflowVal) {
		int val = getVariableExtend(d)+delta;
		if (getVariableExtend(d)>0 && delta>0 && val<0)
			val = overflowVal;		
		setVariableExtend(d, val);
	}
	
	
	protected int incFixedExtend(Component c, Dimension d, int delta) {
		int val = getFixedExtend(d)+delta;
		if (getFixedExtend(d)>0 && delta>0 && val<0)
			val = 0;
		return setFixedExtend(c, d, val);
	}

	protected void incFixedExtend(Dimension d, int delta) {
		int val = getFixedExtend(d)+delta;
		if (getFixedExtend(d)>0 && delta>0 && val<0)
			val = 0;
		setFixedExtend(d, val);
		
	}
	
	protected int setFixedExtend(Component c, Dimension d, int value) {
		int min = getFixedExtend(c.getMinimumSize());
		int max = Integer.MAX_VALUE;//getFixedExtent(c.getMaximumSize()); // max is NOT used here
		value = Math.max(min, Math.min(max, value));
		setFixedExtend(d, value);
		return value;
	}
	
	protected boolean canGrow(Component c, Dimension d) {
		int cur = getVariableExtend(d);
		int max = getVariableExtend(c.getMaximumSize());
		return cur<max;
	}
	
	

	public void removeLayoutComponent(Component comp) {
		layoutComponents.remove(comp);		
		min=pref=max=null;
	}

	
	
	protected int getVariableExtend(Dimension d) {
		return getExtend(d, isVertical);
	}
	
	protected int getFixedExtend(Dimension d) {
		return getExtend(d, !isVertical);
	}
	
	protected int getExtend(Dimension d, boolean vert) {
		if (vert)
			return d.height;
		else
			return d.width;
	}
	
	protected void setVariableExtend(Dimension d, int value) {
		setExtend(d, value, isVertical);
	}
	
	protected void setFixedExtend(Dimension d, int value) {
		setExtend(d, value, !isVertical);
	}
	
	protected void setExtend(Dimension d, int value, boolean vert) {
		if (vert)
			d.height=value;
		else
			d.width=value;
	}

	public void addLayoutComponent(Component comp, Object constraints) {
		addLayoutComponent((String)null, comp);		
	}

	public float getLayoutAlignmentX(Container target) {
		return .5f;
	}

	public float getLayoutAlignmentY(Container target) {
		return .5f;
	}

	public void invalidateLayout(Container target) {	
		min=pref=max=null;
	}

}
