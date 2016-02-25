package visualization.utilities.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import events.EventFirer;
import visualization.utilities.ColorGradient;

public abstract class GradientList extends AbstractGradientSetupComponent implements ListSelectionListener {
	
	protected DefaultListModel dlm;
	protected ColorGradient baseGradient;
	protected JList theList;
	
	protected EventFirer<ActionEvent, ActionListener> firer = new EventFirer<ActionEvent, ActionListener>() {
		protected void dispatchEvent(ActionEvent event, ActionListener listener) {
			listener.actionPerformed(event);
		}
	};
	
	public GradientList(boolean useFullGradient) {
		theList = new JList();
		theList.setModel(dlm = new DefaultListModel());
		theList.setCellRenderer(new GradientListCellRenderer(useFullGradient));
		theList.setVisibleRowCount(5);
		theList.getSelectionModel().addListSelectionListener(this);
		theList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fill();
	}

	public JComponent getJComponent() {
		return theList;
	}

	public abstract void fill();
	
	public void valueChanged(ListSelectionEvent e) {
		fire();
	}


}
