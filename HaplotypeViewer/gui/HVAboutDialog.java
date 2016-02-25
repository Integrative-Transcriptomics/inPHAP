package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.HaplotypeViewer;

public class HVAboutDialog extends HaplotypeViewerDialog {

	public HVAboutDialog(Window owner) {
		super(owner);
		this.setPreferredSize(new Dimension(400, 400));
		this.setTitle(HaplotypeViewer.TITLE + "(" + HaplotypeViewer.VERSION + ")");
		this.setLayout(new BorderLayout());
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JLabel aboutText = new JLabel(getText());
		this.add(aboutText, BorderLayout.CENTER);
		this.add(close, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.centerOnScreen();
	}
	
	private String getText() {
		return "<html><p>" + HaplotypeViewer.TITLE + ": " + HaplotypeViewer.LONG_TITLE + "</p><p>Günter Jäger, Alexander Peltzer and Kay Nieselt</p><p > </p><p>" + HaplotypeViewer.TITLE + " is free software licensed under GPLv2</p><p> </p><p>Core Developers</p><p> </p><p >Günter Jäger, Alexander Peltzer</p><p> </p><p> </p><p> </p><p>Junior Research Group Integrative Transcriptomics&#13;</p><p>Eberhard-Karls-Universität Tübingen, Germany&#13;</p><p >&#13;</p><p>http://it.informatik.uni-tuebingen.de</p></body></html></html>";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8365121499603407341L;

}
