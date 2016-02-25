package gui;

import java.awt.BorderLayout;
import java.awt.Window;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.HaplotypeViewer;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import events.ViewModelEvent;

public class HVLogWindow extends HaplotypeViewerDialog implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6602374419613559418L;
	
	private ViewModel viewModel;
	private StringBuffer sb;
	
	private JTextArea textArea;
	
	public HVLogWindow(Window owner, ViewModel viewModel) {
		super(owner);
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		this.sb = new StringBuffer();
		this.sb.append(HaplotypeViewer.TITLE + " (" + HaplotypeViewer.VERSION + ") " + "started ...\n");
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setTitle(HaplotypeViewer.TITLE + " Log Messages");
		
		this.textArea = new JTextArea(30,60);
		this.textArea.setEditable(false);
		this.textArea.setText(sb.toString());
		JScrollPane scroller = new JScrollPane(this.textArea);
		this.add(scroller, BorderLayout.CENTER);
		
		pack();
		centerOnScreen();
	}
	
	public void showWindow() {
		this.setVisible(true);
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		DateFormat dataFormat = new SimpleDateFormat("dd.MM.yyy - HH:mm:ss");
		Date date = new Date();
		String dateString = "(" + dataFormat.format(date) + "): ";
		String status = e.getStatus();
		String message = dateString + status;
		sb.append(message);
		sb.append("\n");
		
		this.textArea.setText(sb.toString());
		this.textArea.revalidate();
	}
}
