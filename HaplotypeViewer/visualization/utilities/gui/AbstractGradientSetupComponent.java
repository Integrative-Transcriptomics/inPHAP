package visualization.utilities.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import events.EventFirer;

public abstract class AbstractGradientSetupComponent implements GradientSetupComponent {
	
	protected boolean silent;
	
	protected EventFirer<ActionEvent, ActionListener> firer = new EventFirer<ActionEvent, ActionListener>() {
		protected void dispatchEvent(ActionEvent event, ActionListener listener) {
			listener.actionPerformed(event);
		}
	};
	
	public void addListener(ActionListener l) {
		firer.addListener(l);
	}

	public void removeListener(ActionListener l) {
		firer.removeListener(l);
	}
	
	protected void fire() {
		if (!silent)
			firer.fireEvent(new ActionEvent(this,0,""));
	}


}
