package settings.guiComponents;

import gui.HaplotypeViewerDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ColorChooserComponent extends HaplotypeViewerDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3872073018752587358L;
	
	boolean closedWithOK = false;
	JColorChooser cc;
	
	public ColorChooserComponent(Window owner, Color c) {
		super(owner);
		JButton okButton = new JButton("Apply");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closedWithOK = true;
				setVisible(false);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closedWithOK = false;
				setVisible(false);
			}
		});
		
		cc = new JColorChooser(c);
		
		setLayout(new BorderLayout());
		setModal(true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(cc, BorderLayout.CENTER);
		
		pack();
		centerOnScreen();
	}
	
	public boolean closedWithOK() {
		return this.closedWithOK;
	}
	
	public void showDialog() {
		setVisible(true);
	}
	public Color getSelectedColor() {
		return cc.getColor();
	}
}
