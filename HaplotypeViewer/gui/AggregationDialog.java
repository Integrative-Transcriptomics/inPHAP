package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import events.ViewModelEvent;
import settings.AggregationSetting;
import viewmodel.ViewModel;

public class AggregationDialog extends HaplotypeViewerDialog {

	private AggregationSetting setting;
	private ViewModel viewModel;
	
	public AggregationDialog(Window owner, ViewModel viewModel) {
		super(owner);
		this.viewModel = viewModel;

		this.setTitle("Aggregation Manager");
		this.setting = new AggregationSetting(viewModel);
		
		this.add(this.setting.getViewComponent(), BorderLayout.CENTER);
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AggregationDialog.this.viewModel.fireChanged(new ViewModelEvent(AggregationDialog.this, ViewModelEvent.LOG_UPDATE, "Aggregation Methods Selected -> Aggregations are going to be applied ..."));
				dispose();
				
				switch(setting.getAggregationDataSource()) {
				case AggregationSetting.SELECTION:
					AggregationDialog.this.viewModel.aggregateSelectedRows(setting.getMatrixAggregator(), setting.getMetaAggregator());
					break;
				case AggregationSetting.META_INFORMATION:
					AggregationDialog.this.viewModel.aggregateRowsByMetaInfo(setting.getMatrixAggregator(), setting.getMetaAggregator(), setting.getMetaInformationColumn());
					break;
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.setMinimumSize(new Dimension(300, 30));
		
		this.pack();
		this.centerOnScreen();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6989971254419305591L;

}
