package settings;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.events.SettingChangeEvent;
import settings.guiComponents.TabbedSettingContainer;
import settings.typed.BooleanSetting;
import settings.typed.ColorGradientSetting;
import settings.typed.ColorMapSetting;
import settings.typed.ColorSelectorSetting;
import settings.typed.ColorSetting;
import settings.typed.FontSetting;
import settings.typed.IntSetting;
import settings.typed.SettingReplaceSetting;
import settings.typed.StringChooserSetting;
import viewmodel.ViewModel;
import viewmodel.ViewModelListener;
import visualization.utilities.ColorGradient;
import visualization.utilities.ColorSelector;
import visualization.utilities.colormaps.PairedColorMap;
import events.ViewModelEvent;
import gui.layout.ExcellentBoxLayout;

public class SNPMapSetting extends HierarchicalSetting implements ViewModelListener {

	private IntSetting cellWidth;
	private IntSetting cellHeight;
	
	private IntSetting metaCellWidth;
	private IntSetting metaCellHeight;
	
	private ColorSetting noSNPColor;
	private ColorSetting heteroSNPColor;
	private ColorSetting homoSNPColor;
	
	private ColorSetting AColor;
	private ColorSetting CColor;
	private ColorSetting TColor;
	private ColorSetting GColor;
	
	private ColorSetting selectionColor;
	
	private BooleanSetting referenceEncoding;
	private BooleanSetting phased;
	private StringChooserSetting aggregationEncoding;
	
	private TabbedSettingContainer metaColumnColorSelectorContainer;
	private TabbedSettingContainer metaRowColorSelectorContainer;
	
	private FontSetting font;
	
	private String title = "View Settings";
	
	private ViewModel viewModel;
	
	public static final String SATURATION = "Saturation";
	public static final String BOX_HEIGHT = "Box Height";
	
	String[] aggregationEncNames = {SATURATION, BOX_HEIGHT};
	
	/**
	 * @param snpMap
	 */
	public SNPMapSetting(ViewModel viewModel) {
		super("Settings");
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		
		this.addSetting(cellWidth = new IntSetting("Cell Width", 15));
		this.addSetting(cellHeight = new IntSetting("Cell Height", 15));
		
		this.addSetting(metaCellWidth = new IntSetting("Meta-Cell Width", 15));
		this.addSetting(metaCellHeight = new IntSetting("Meta-Cell Height", 15));
		
		this.addSetting(aggregationEncoding = new StringChooserSetting("Aggregation", 1, aggregationEncNames));
		
		SettingContainer unphasedSettings = new SettingContainer("Unphased Settings");
		SettingContainer phasedSettings = new SettingContainer("PhasedSettings");
		
		unphasedSettings.addSetting(noSNPColor = new ColorSetting("Reference Color", new Color(26,152,80)));
		unphasedSettings.addSetting(heteroSNPColor = new ColorSetting("Heterocygous SNP Color", new Color(254,224,139)));
		unphasedSettings.addSetting(homoSNPColor = new ColorSetting("Homozygous SNP Color", new Color(215,48,39)));
		
		phasedSettings.addSetting(referenceEncoding = new BooleanSetting("Ref Encoding", false));
		phasedSettings.addSetting(AColor = new ColorSetting("A Color", new Color(171,221,164)));
		phasedSettings.addSetting(CColor = new ColorSetting("C Color", new Color(43,131,186)));
		phasedSettings.addSetting(TColor = new ColorSetting("T Color", new Color(215,25,28)));
		phasedSettings.addSetting(GColor = new ColorSetting("G Color", new Color(253,174,97)));
		
		this.addSetting(selectionColor = new ColorSetting("Selection Color", Color.BLACK));
		
		this.addSetting(phased = new BooleanSetting("Phased data", true));
		
		this.metaColumnColorSelectorContainer = new TabbedSettingContainer("Subject Meta-Information Color");
		this.metaColumnColorSelectorContainer.addSettingChangeListener(this);
		
		this.metaRowColorSelectorContainer = new TabbedSettingContainer("SNP Meta-Information Color");
		this.metaRowColorSelectorContainer.addSettingChangeListener(this);
		
		SettingReplaceSetting replaceSetting = new SettingReplaceSetting(phasedSettings, unphasedSettings, phased);
		this.addSetting(replaceSetting);
		
		this.addSetting(font = new FontSetting("Font Setting", Font.SERIF, Font.PLAIN, 10));
	}
	
	public Font getFont() {
		return this.font.getFont();
	}
	
	public boolean isReferenceEncoding() {
		return this.referenceEncoding.getValue();
	}
	
	public int getCellWidth() {
		return this.cellWidth.getValue();
	}
	
	public int getCellHeight() {
		return this.cellHeight.getValue();
	}
	
	public int getMetaCellWidth() {
		return this.metaCellWidth.getValue();
	}
	
	public int getMetaCellHeight() {
		return this.metaCellHeight.getValue();
	}
	
	public Color getNoSNPColor() {
		return this.noSNPColor.getColor();
	}
	
	public Color getHeteroSNPColor() {
		return this.heteroSNPColor.getColor();
	}
	
	public Color getHomoSNPColor() {
		return this.homoSNPColor.getColor();
	}
	
	public Color getSelectionColor() {
		return this.selectionColor.getColor();
	}
	
	public Color getAColor() {
		return this.AColor.getColor();
	}
	
	public Color getTColor() {
		return this.TColor.getColor();
	}
	
	public Color getCColor() {
		return this.CColor.getColor();
	}
	
	public Color getGColor() {
		return this.GColor.getColor();
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void fireSettingChanged(SettingChangeEvent e) {
		int change = e.getChange();
		
		switch(change) {
		case SettingChangeEvent.COLOR_CHANGED:
			this.viewModel.fireChanged(new ViewModelEvent(e.getSource(), ViewModelEvent.COLOR_CHANGED, e.getMessage()));
			break;
		case SettingChangeEvent.SIZE_CHANGED:
			this.viewModel.fireChanged(new ViewModelEvent(e.getSource(), ViewModelEvent.PLOT_RESIZE, e.getMessage()));
			break;
		case SettingChangeEvent.COLOR_GRADIENT_CHANGED:
			this.viewModel.fireChanged(new ViewModelEvent(e.getSource(), ViewModelEvent.COLOR_GRADIENT_CHANGED, e.getMessage()));
			break;
		default:
			this.viewModel.fireChanged(new ViewModelEvent(e.getSource(), ViewModelEvent.SETTINGS_CHANGED, e.getMessage()));
		}
	}

	public ColorSelector getMetaColumnColorSelector(int i) {
		if(this.metaColumnColorSelectorContainer.getNumChildren() <= i) {
			return null;
		}
		ColorSelector cg = ((ColorSelectorSetting)this.metaColumnColorSelectorContainer.getSetting(i)).getColorSelector();
		return cg;
	}
	
	public ColorSelector getMetaRowColorSelector(int i) {
		if(this.metaRowColorSelectorContainer.getNumChildren() <= i) {
			return null;
		}
		ColorSelector cg = ((ColorSelectorSetting)this.metaRowColorSelectorContainer.getSetting(i)).getColorSelector();
		return cg;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		//pass event to viewmodel
		fireSettingChanged(e);
	}
	
	public boolean isPhased() {
		return this.phased.getValue();
	}
	
	public String getAggregationEncoding() {
		return this.aggregationEncoding.getSelectedValue();
	}

	@Override
	public JComponent getViewComponent() {
		JPanel panel = new JPanel(new ExcellentBoxLayout(true, 5));
		panel.setBorder(BorderFactory.createTitledBorder(title));
		for(int i = 0; i < children.size(); i++) {
			panel.add(children.get(i).getViewComponent());
		}
		return panel;
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		switch(e.getChange()) {
		case ViewModelEvent.SUBJECT_META_INFO_CHANGED:
			int metaInfoSize = this.metaColumnColorSelectorContainer.children.size();
			int numMetaCols = viewModel.getDataSet().getNumMetaCols();
			
			for(int i = metaInfoSize; i < numMetaCols; i++) {
				double min = viewModel.getMetaColumn(i).getMinimum();
				double max = viewModel.getMetaColumn(i).getMaximum();
				
				ColorSelectorSetting s = new ColorSelectorSetting(viewModel.getMetaColumnID(i), 
						new ColorGradientSetting("ColorGradient", ColorGradient.createDefaultGradient(min, max)), 
						new ColorMapSetting("ColorMap", new PairedColorMap()));
				
				this.metaColumnColorSelectorContainer.addSetting(s);
				fireSettingChanged(new SettingChangeEvent(s, SettingChangeEvent.COLOR_GRADIENT_CHANGED, "Color Gradient/Map Settings added ..."));
			}
			break;
		case ViewModelEvent.SNP_META_INFO_CHANGED:
			metaInfoSize = this.metaRowColorSelectorContainer.children.size();
			int numMetaRows = viewModel.getDataSet().getNumMetaRows();
			
			for(int i = metaInfoSize; i < numMetaRows; i++) {
				double min = viewModel.getMetaRow(i).getMinimum();
				double max = viewModel.getMetaRow(i).getMaximum();
				
				ColorSelectorSetting s = new ColorSelectorSetting(viewModel.getMetaRowID(i), new ColorGradientSetting(
						"ColorGradient", ColorGradient.createDefaultGradient(min, max)), 
						new ColorMapSetting("ColorMap", new PairedColorMap()));
				this.metaRowColorSelectorContainer.addSetting(s);
				fireSettingChanged(new SettingChangeEvent(s, SettingChangeEvent.COLOR_GRADIENT_CHANGED, "Color Gradient/Map Settings added ..."));
			}
			break;
		}
	}

	public HierarchicalSetting getMetaColumnSelectorSettingContainer() {
		return this.metaColumnColorSelectorContainer;
	}

	public HierarchicalSetting getMetaRowSelectorSettingContainer() {
		return this.metaRowColorSelectorContainer;
	}
}
