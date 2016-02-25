package export.filters;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import settings.HierarchicalSetting;
import export.ExportPlugin;
import export.ExportSetting;
import export.RasterExportSetting;

public class Preview extends ExportPlugin {

	protected RasterExportSetting rasterSettings = new RasterExportSetting();
	
	protected BufferedImage l_buffer;
	
	public void exportComponent(Component plotComponent, ExportSetting settings) throws Exception {
		// generate the plot
		l_buffer = new BufferedImage( settings.getDimension().width, settings.getDimension().height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D graphicsCanvas = l_buffer.createGraphics();        
		
		Object AAHint = RenderingHints.VALUE_ANTIALIAS_OFF;
		Object AATHint = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
		
		graphicsCanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AAHint);
		graphicsCanvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, AATHint);
		
		exportComponentToCanvas(plotComponent, graphicsCanvas, settings);
	}

	@Override
	public HierarchicalSetting getSetting() {
		return rasterSettings; 
	}

	public BufferedImage getImage() {
		return l_buffer;
	}

	@Override
	public String getFormatName() {
		return null;
	}
}
