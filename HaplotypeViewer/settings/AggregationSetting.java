package settings;

import gui.layout.ExcellentBoxLayout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.events.SettingChangeEvent;
import settings.typed.ComponentPlaceHolderSetting;
import settings.typed.GeneralSettingReplaceSetting;
import settings.typed.StringChooserSetting;
import viewmodel.ViewModel;
import aggregation.IMatrixAggregator;
import aggregation.IMetaAggregator;
import aggregation.MaximumMatrixAggregator;
import aggregation.MaximumMetaAggregator;
import aggregation.MeanMetaAggregator;
import aggregation.MinimumMatrixAggregator;
import aggregation.MinimumMetaAggregator;

public class AggregationSetting extends HierarchicalSetting {

	
	StringChooserSetting dataSourceSetting;
	StringChooserSetting metaInfoChooser;
	StringChooserSetting matrixAggregationSetting;
	StringChooserSetting metaAggregationSetting;
	
	public static final String MATRIX_MAX = "Maximum";
	public static final String MATRIX_MIN = "Minimum";
	
	public static final String META_MAX = "Maximum";
	public static final String META_MIN = "Minimum";
	public static final String META_MEAN = "Mean";
	
	public static final String SELECTION = "Selection";
	public static final String META_INFORMATION = "Meta-Information";
	
	String[] matrixAgMethods = {MATRIX_MAX, MATRIX_MIN};
	String[] metaAgMethods = {META_MAX, META_MIN, META_MEAN};
	String[] dataSources = {SELECTION, META_INFORMATION};
	
	public AggregationSetting(ViewModel viewModel) {
		super("Aggregation Setting");
		
		this.addSetting(matrixAggregationSetting = new StringChooserSetting("Matrix Aggregation Method", 0, matrixAgMethods));
		this.addSetting(metaAggregationSetting = new StringChooserSetting("Meta Aggregation Method", 2, metaAgMethods));
		
		String[] metaColumns = new String[viewModel.numMetaCols()];
		for(int i = 0; i < metaColumns.length; i++) {
			metaColumns[i] = viewModel.getMetaColumnID(i);
		}
		
		this.dataSourceSetting = new StringChooserSetting("Aggregation Source", 0, dataSources);
		this.metaInfoChooser = new StringChooserSetting("Meta-Information", 0, metaColumns);
		
		List<HierarchicalSetting> settings = new ArrayList<HierarchicalSetting>();
		settings.add(new ComponentPlaceHolderSetting("", null));
		settings.add(metaInfoChooser);
		
		GeneralSettingReplaceSetting replaceSetting = new GeneralSettingReplaceSetting(settings, 0, dataSourceSetting);
		
		this.addSetting(dataSourceSetting);
		this.addSetting(replaceSetting);
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {}

	@Override
	public JComponent getViewComponent() {
		JPanel panel = new JPanel(new ExcellentBoxLayout(true, 5));
		panel.setBorder(BorderFactory.createTitledBorder(getTitle()));
		for(int i = 0; i < children.size(); i++) {
			panel.add(children.get(i).getViewComponent());
		}
		return panel;
	}
	
	public String getAggregationDataSource() {
		return this.dataSourceSetting.getSelectedValue();
	}
	
	public int getMetaInformationColumn() {
		return this.metaInfoChooser.getSelectedIndex();
	}
	
	public IMatrixAggregator getMatrixAggregator() {
		switch(matrixAggregationSetting.getSelectedValue()) {
		case MATRIX_MAX:
			return new MaximumMatrixAggregator();
		case MATRIX_MIN:
			return new MinimumMatrixAggregator();
		default:
			return new MaximumMatrixAggregator();
		}
	}
	
	public IMetaAggregator getMetaAggregator() {
		switch(metaAggregationSetting.getSelectedValue()) {
		case META_MAX:
			return new MaximumMetaAggregator();
		case META_MIN:
			return new MinimumMetaAggregator();
		case META_MEAN:
			return new MeanMetaAggregator();
		default:
			return new MaximumMetaAggregator();
		}
	}
}