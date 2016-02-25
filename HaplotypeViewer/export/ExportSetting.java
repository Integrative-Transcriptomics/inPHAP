package export;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import settings.HierarchicalSetting;
import settings.SettingContainer;
import settings.events.SettingChangeEvent;
import settings.events.SettingChangeListener;
import settings.guiComponents.SettingDialog;
import settings.typed.BooleanSetting;
import settings.typed.ComponentPlaceHolderSetting;
import settings.typed.SettingPlaceHolderSetting;
import settings.typed.StringChooserSetting;
import export.filters.ExportJPG;
import export.filters.ExportPNG;
import export.filters.ExportSVG;
import export.filters.ExportTIFF;
import export.filters.PDFExport;
import export.filters.Preview;
import gui.HaplotypeViewerDialog;
import gui.layout.ExcellentBoxLayout;

public class ExportSetting extends HierarchicalSetting implements SettingChangeListener {

	private LinkedDimensionSetting lds;

	private StringChooserSetting method;
	private String targetFile;
	private Dimension baseDimension;	
	private BooleanSetting scaleContent;
	private SettingPlaceHolderSetting placeHolderSetting;
	
	public static final String JPG = "JPG";
	public static final String PNG = "PNG";
	public static final String SVG = "SVG";
	public static final String TIFF = "TIFF";
	public static final String PDF = "PDF";
	
	String[] methods = {JPG, PNG, SVG, TIFF, PDF};
	
	private ExportPlugin chosenplugin;

	private Component pc;
	private SettingDialog preview_settings_dialog;
	
	public ExportSetting(boolean allowChangeScaling) {
		super("Graphics Export Settings");
		
		scaleContent = new BooleanSetting("Scale content",true);
		
		SettingContainer h = new SettingContainer("Dimensions");
		h.addSetting(lds = new LinkedDimensionSetting());
		
		if (allowChangeScaling)
			h.addSetting( scaleContent );		
		h.addSetting(new ComponentPlaceHolderSetting("preview", new JButton(new PreviewAction())));

		addSetting(h);
		addSetting(method = new StringChooserSetting("Format", 0, methods));
		
		addSetting(placeHolderSetting = new SettingPlaceHolderSetting(getMethod().getSetting()));
		
		method.addSettingChangeListener(this);
	}
	
	public Dimension getDimension() {
		return lds.getTargetDimension();		
	}
	
	public Dimension getBaseDimension() {
		return baseDimension;
	}
	
	public void setInitialDimension(Dimension d) {
		baseDimension = d;
		lds.setTargetDimension(d);
	}
	
	public void setTargetDimension(Dimension d) {
		lds.setTargetDimension(d);
	}
	
	public String getTargetFilename() {
		return targetFile;
	}
	
	public void setTargetFileName(String tfn) {
		targetFile = tfn;
	}
	
	public boolean scaleContent() {
		return scaleContent.getValue();
	}
	
	public void setScaleContent(boolean sc) {
		scaleContent.setValue(sc);
	}
	
	public ExportPlugin getMethod() {
		switch(method.getSelectedValue()) {
			case JPG:
				chosenplugin = new ExportJPG();
				break;
			case PNG:
				chosenplugin = new ExportPNG();
				break;
			case SVG:
				chosenplugin = new ExportSVG();
				break;
			case TIFF:
				chosenplugin = new ExportTIFF();
				break;
			case PDF:
				chosenplugin = new PDFExport();
				break;
			default:
				chosenplugin = new ExportJPG();
		}

		return chosenplugin;
	}

	public void setPreviewActionObjects(Component plotComponent, SettingDialog dialog) {
		pc = plotComponent;
		preview_settings_dialog = dialog;
	}

	
	@SuppressWarnings("serial")
	protected class PreviewAction extends AbstractAction {
		public PreviewAction() {
			super("Preview with these settings");
		}
		public void actionPerformed(ActionEvent e) {			
			HaplotypeViewerDialog previewWindow = new HaplotypeViewerDialog(null);
//			previewWindow.add(preview_settings_dialog);
			previewWindow.setTitle("Export preview");
			previewWindow.setModal(true);
			JScrollPane jsp = new JScrollPane();
			previewWindow.add(jsp);
			Preview p = new Preview();
			try {
				p.exportComponent(pc, (ExportSetting)preview_settings_dialog.getSetting());
				JLabel jl  = new JLabel(new ImageIcon(p.getImage()));
				jsp.setViewportView(jl);
				previewWindow.pack();
				previewWindow.setMinimumSize(new Dimension(200,200));
				previewWindow.setPreferredSize(new Dimension(800,600));
				previewWindow.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		if(e.getSource().equals(method)) {
			HierarchicalSetting s = getMethod().getSetting();
			this.placeHolderSetting.replaceSetting(s);
			if(sd != null) {
				sd.resize();
			}
		}
	}

	@Override
	public JComponent getViewComponent() {
		JPanel panel = new JPanel(new ExcellentBoxLayout(true, 5));
		panel.setBorder(BorderFactory.createTitledBorder(getTitle()));
		for(int i = 0; i < children.size(); i++) {
			panel.add(children.get(i).getViewComponent());
		}
		return panel;
	}
	
	private SettingDialog sd;

	public SettingDialog getDialog() {
		if(sd != null)
			return sd;
		sd = new SettingDialog(null, this);
		return sd;
	}
}
