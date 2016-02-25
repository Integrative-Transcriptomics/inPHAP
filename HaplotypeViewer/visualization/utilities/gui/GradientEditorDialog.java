package visualization.utilities.gui;

import events.EventFirer;
import gui.HaplotypeViewerDialog;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.ColorGradient;

@SuppressWarnings("serial")
public class GradientEditorDialog extends HaplotypeViewerDialog {

	protected EventFirer<ChangeEvent, ChangeListener> firer = new EventFirer<ChangeEvent, ChangeListener>() {

		protected void dispatchEvent(ChangeEvent event, ChangeListener listener) {
			listener.stateChanged(event);
		}
	};
	protected GradientEditorPanel ep;
	
	public GradientEditorDialog(Window owner, ColorGradient gradient ) {
		super(owner);
		setTitle("Edit Color Gradient" ); 
		setLayout(new BorderLayout());
		ep = new GradientEditorPanel( gradient );
		add(ep, BorderLayout.CENTER);
		
		JPanel jp = new JPanel();
		jp.add(Box.createHorizontalGlue());
		jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
		jp.add(new JButton(new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}));
		jp.add(Box.createHorizontalStrut(5));
		jp.add(new JButton(new AbstractAction("OK") {
			public void actionPerformed(ActionEvent e) {
				ep.apply();
				firer.fireEvent(new ChangeEvent(this));
				dispose();
			}
		}));
		
		add(jp, BorderLayout.SOUTH);
		
		pack();
		
		setMinimumSize(ep.getMinimumSize());
				
	}
	
	public void setGradient(ColorGradient g) {
		ep.setGradient(g);
	}
	
	public void addChangeListener(ChangeListener cl) {
		firer.addListener(cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		firer.removeListener(cl);
	}
}
