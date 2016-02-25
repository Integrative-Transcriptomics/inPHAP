package visualization;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JSplitPane;

public class SplitPaneSynchronizer implements PropertyChangeListener {
	private final static String DIVIDER_LOCATION = "dividerLocation";

	private ArrayList<JSplitPane> splitPanes;

	public SplitPaneSynchronizer(JSplitPane... splitPanes) {
		this.splitPanes = new ArrayList<JSplitPane>(splitPanes.length);

		for (JSplitPane splitPane : splitPanes)
			addSplitPane( splitPane );
	}

	public void addSplitPane(JSplitPane splitPane)
	{
		splitPane.addPropertyChangeListener(DIVIDER_LOCATION, this);
		splitPanes.add( splitPane );
	}

	public void removeSplitPane(JSplitPane splitPane) {
		splitPane.removePropertyChangeListener(DIVIDER_LOCATION, this);
		splitPanes.remove( splitPane );
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		int location = ((Integer)e.getNewValue()).intValue();

		for (JSplitPane splitPane : splitPanes)	{
			if (splitPane != source
					&&  splitPane.getDividerLocation() != location)
				splitPane.setDividerLocation( location );
		}
	}
}
