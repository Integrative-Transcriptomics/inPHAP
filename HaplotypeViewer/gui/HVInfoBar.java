package gui;

import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import events.ViewModelEvent;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;

public class HVInfoBar extends JPanel implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -397221288192230071L;
	
	protected JLabel infoTextLabel;
	private ViewModel viewModel;
	
	public HVInfoBar(ViewModel viewModel) {
		super();
		this.infoTextLabel = new JLabel("HaplotypeViewer started ...");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(this.infoTextLabel);
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
	}
	
	public void setInfoText(String text) {
		this.infoTextLabel.setText(text);
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		DateFormat dataFormat = new SimpleDateFormat("dd.MM.yyy - HH:mm:ss");
		Date date = new Date();
		String dateString = "(" + dataFormat.format(date) + "): ";
		String status = e.getStatus();
		this.infoTextLabel.setText(dateString + status);
		this.revalidate();
	}
}
