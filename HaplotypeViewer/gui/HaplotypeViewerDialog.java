package gui;

import java.awt.Window;

import javax.swing.JDialog;

public class HaplotypeViewerDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9141815434172783130L;

	public HaplotypeViewerDialog(Window owner) {
		super(owner);
	}

	public void centerOnScreen() {
		this.setLocationRelativeTo(null);
	}
}
