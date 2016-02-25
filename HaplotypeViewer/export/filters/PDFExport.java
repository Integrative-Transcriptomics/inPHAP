package export.filters;

import java.awt.Component;
import java.awt.Graphics2D;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import main.HaplotypeViewer;
import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import settings.typed.BooleanSetting;
import settings.typed.SettingReplaceSetting;
import settings.typed.StringSetting;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import export.ExportPlugin;
import export.ExportSetting;
import gui.layout.ExcellentBoxLayout;

public class PDFExport  extends ExportPlugin {
	
	private SettingReplaceSetting metaDataSetting;
	private StringSetting author;
	private StringSetting title;
	private StringSetting subject;
	private StringSetting keywords;
	
	public PDFExport() 	{
		HierarchicalSetting one = new HierarchicalSetting("PDF Meta Data") {
			
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
		};
		
		HierarchicalSetting placeHolderSetting = new HierarchicalSetting("") {
			
			@Override
			public void settingChanged(SettingChangeEvent e) {}
			
			@Override
			public JComponent getViewComponent() {
				return new JPanel();
			}
		};
		
		author=new StringSetting("Author", "");
		title=new StringSetting("Title", "");
		subject=new StringSetting("Subject", "");
		keywords=new StringSetting("Key Words", "");
		
		one.addSetting(title);
		one.addSetting(author);
		one.addSetting(subject);
		one.addSetting(keywords);
		
		BooleanSetting useMetaData = new BooleanSetting("PDF Meta Data?", true);
		
		metaDataSetting = new SettingReplaceSetting(one, placeHolderSetting, useMetaData);
	}
	
	@Override
	public void exportComponent(Component plotComponent, ExportSetting settings) throws Exception {
		// points = 1/72 inches. 
		// first two parameters are lower left, then upper right)
		Rectangle docBounds=new Rectangle(0, 0, settings.getDimension().width, settings.getDimension().height);
		Document document = new Document(docBounds);
				//PageSize.A4);

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(settings.getTargetFilename()));

		document.addCreationDate();
		document.addCreator("Created by "+HaplotypeViewer.getFullName());
		
		if(metaDataSetting.getChosenSettingIndex() == 0) {
			document.addAuthor(author.getValue());
			document.addTitle(title.getValue());
			document.addSubject(subject.getValue()); 
			document.addKeywords(keywords.getValue()); 
		}
		
		document.open();

		// no idea what this does. 
		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate tp = cb.createTemplate((float)settings.getDimension().getWidth(),(float)settings.getDimension().getHeight());
		Graphics2D g2 = tp.createGraphics((float)settings.getDimension().getWidth(),(float)settings.getDimension().getHeight(), new DefaultFontMapper());

		// Create your graphics here - draw on the g2 Graphics object
		exportComponentToCanvas(plotComponent, g2, settings);
		
		g2.dispose();
		cb.addTemplate(tp, 0,0); // 0, 100 = x,y positioning of graphics in PDF page
		document.close();	
	}

	@Override
	public String getFormatName() {
		return "pdf";
	}
	
	@Override
	public HierarchicalSetting getSetting() {
		return metaDataSetting;
	}
}
