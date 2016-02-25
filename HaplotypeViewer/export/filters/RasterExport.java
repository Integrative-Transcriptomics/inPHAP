package export.filters;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import export.ExportPlugin;
import export.ExportSetting;
import export.RasterExportSetting;

public abstract class RasterExport extends ExportPlugin {

	protected RasterExportSetting rasterSettings = new RasterExportSetting();
	
	public void exportComponent(Component plotComponent, ExportSetting settings) throws Exception {
		exportRasterImage(getImageTranscoder(), plotComponent, settings);
	}

	protected abstract ImageTranscoder getImageTranscoder();

	protected void exportRasterImage( ImageTranscoder transcoder, Component plotComponent, ExportSetting settings ) throws Exception {        		
		
		OutputStream l_file = new FileOutputStream( settings.getTargetFilename() );
		TranscoderOutput l_transcoderOutput = new TranscoderOutput( l_file );

		// generate the plot
		BufferedImage l_buffer = transcoder.createImage( settings.getDimension().width, settings.getDimension().height );
		Graphics2D graphicsCanvas = l_buffer.createGraphics();        
		
		Object AAHint = rasterSettings.isGraphicsAA()?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF;
		Object AATHint = rasterSettings.isTextAA()?RenderingHints.VALUE_TEXT_ANTIALIAS_ON:RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
		
		graphicsCanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AAHint);
		graphicsCanvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, AATHint);
		
		exportComponentToCanvas(plotComponent, graphicsCanvas, settings);
		
		// transcode and write the image
		transcoder.writeImage( l_buffer, l_transcoderOutput );
		l_file.flush();
		l_file.close();            
	}
	
	public abstract String getFormatName();
}
