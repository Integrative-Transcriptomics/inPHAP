package export.filters;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;

import settings.HierarchicalSetting;

public class ExportTIFF extends RasterExport {

	@Override
	protected ImageTranscoder getImageTranscoder() {
		return new TIFFTranscoder();	
	}

	@Override
	public String getFormatName() {
		return "TIFF";
	}

	@Override
	public HierarchicalSetting getSetting() {
		return null;
	}
}
