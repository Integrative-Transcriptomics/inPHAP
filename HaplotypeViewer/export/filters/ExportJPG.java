package export.filters;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import settings.HierarchicalSetting;

public class ExportJPG extends RasterExport {

	@Override
	protected ImageTranscoder getImageTranscoder() {
		JPEGTranscoder l_transcoder = new JPEGTranscoder();
		l_transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1.0f));
		return l_transcoder;
	}

	@Override
	public String getFormatName() {
		return "JPG";
	}

	@Override
	public HierarchicalSetting getSetting() {
		return this.rasterSettings;
	}
}
