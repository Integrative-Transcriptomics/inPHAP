package visualization.utilities.gui.setuppers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import visualization.utilities.ColorGradient;
import visualization.utilities.ColorGradient.MIDPOINT_MODE;
import visualization.utilities.gui.GradientList;

public class SetupMidpointList extends GradientList {

	public SetupMidpointList() {
		super(false);
		theList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				ColorGradient sel = ((ColorGradient)theList.getSelectedValue());
				if (sel!=null && sel.getMidpointMode()==MIDPOINT_MODE.Custom) {
					if (evt.getClickCount()==2 && evt.getButton()==MouseEvent.BUTTON1) {
						String mp = JOptionPane.showInputDialog("Enter a custom value for the midpoint", sel.getMidpoint());
						if (mp!=null) {
							Double d = Double.parseDouble(mp);
							sel.setMidpoint(d);
							updateFromGradient(sel, true);
							fire();
						}
					}
				}
			}
		});
	}		

	public void fill() {
		for (MIDPOINT_MODE mm : MIDPOINT_MODE.values()) {
			ColorGradient cgneu = ColorGradient.createDefaultGradient(0, 16); 
			cgneu.setMidpointMode(mm);
			if (mm==MIDPOINT_MODE.Custom) {
				cgneu.setName(mm.toString()+": "+cgneu.getMidpoint());
			} else {
				cgneu.setName(mm.toString());
			}
			dlm.addElement(cgneu);
		}		
	}

	public void modifyGradient(ColorGradient c) {
		ColorGradient ret = (ColorGradient)theList.getSelectedValue();
		if (ret!=null) {
			c.setMidpointMode(ret.getMidpointMode());				
			c.setMidpoint(ret.getMidpoint());
		}
	}

	public void updateFromGradient(ColorGradient c, boolean overrideEverything) {
		silent = overrideEverything;
		MIDPOINT_MODE inputMidpointMode = c.getMidpointMode();

		for (int i=0; i!=dlm.getSize(); ++i) {
			ColorGradient cg = ((ColorGradient)dlm.get(i));
			MIDPOINT_MODE thisMidpointMode = cg.getMidpointMode(); // keep midpoint mode
			double thisMidpoint = cg.getMidpoint(); // keep midpoint					

			cg.copySettings(c);
			cg.setMidpointMode(thisMidpointMode);
			

			if (overrideEverything && c.getMidpointMode()==MIDPOINT_MODE.Custom) // replace midpoint on init
				cg.setMidpoint(c.getMidpoint());
			else 
				cg.setMidpoint(thisMidpoint);

			if (cg.getMidpointMode()==inputMidpointMode) 
				theList.setSelectedIndex(i);					

			if (thisMidpointMode==MIDPOINT_MODE.Custom) {
				cg.setName(thisMidpointMode.toString()+": "+cg.getMidpoint());
			} else {
				cg.setName(cg.getMidpointMode().toString());
			}

		}
		theList.repaint();
		silent = false;
	}

}
