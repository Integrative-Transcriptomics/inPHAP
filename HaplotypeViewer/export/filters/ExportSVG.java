/**
 * 
 */
package export.filters;

import java.awt.Color;
import java.awt.Component;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import main.HaplotypeViewer;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

import settings.HierarchicalSetting;
import export.ExportPlugin;
import export.ExportSetting;

public class ExportSVG extends ExportPlugin {

	@Override
	public String getFormatName() {
		return "SVG";
	}

	@Override
	public void exportComponent(Component plotComponent, ExportSetting settings)
			throws Exception {
		// create a new DOM document
		Document l_document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		// create a context for the SVG renderer
		SVGGeneratorContext l_context = SVGGeneratorContext.createDefault( l_document );
		l_context.setComment( " Generated by " +
				HaplotypeViewer.getFullName() +
		" using Apache Batik SVG Generator. " );
		l_context.setEmbeddedFontsOn( true );

		// create SVG renderer    
		SVGGraphics2D l_SVGGenerator = new SVGGraphics2D( l_context, false );

		l_SVGGenerator.setBackground( Color.white );
		//l_SVGGenerator.setClip(0,0,width,height);
		l_SVGGenerator.setSVGCanvasSize(settings.getDimension());
		
		exportComponentToCanvas(plotComponent, l_SVGGenerator, settings);
		
		FileOutputStream l_FileOutStream = new FileOutputStream(settings.getTargetFilename());
		OutputStreamWriter l_fileWriter = new  OutputStreamWriter(l_FileOutStream,"UTF-8"); // changed JD
		l_SVGGenerator.stream( l_fileWriter );
		l_fileWriter.close();    
		l_FileOutStream.close(); 		
	}

	@Override
	public HierarchicalSetting getSetting() {
		return null;
	}
}