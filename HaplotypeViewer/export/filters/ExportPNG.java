package export.filters;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import settings.HierarchicalSetting;

public class ExportPNG extends RasterExport {

	@Override
	protected ImageTranscoder getImageTranscoder() {
		return new PNGTranscoder();	
	}

	@Override
	public String getFormatName() {
		return "PNG";
	}

	@Override
	public HierarchicalSetting getSetting() {
		return this.rasterSettings;
	}
}
