package visualization.utilities.colormaps;

public class BasicColorMap extends ColorMap {
	
	public BasicColorMap(ColorMap colorMap) {
		this.copySettings(colorMap);
	}
	
	@Override
	public void initializePreviewColors() {
		//nothing to do
	}

	@Override
	public void initializeColorMapColors(int size) {
		// nothing to do	
	}

	@Override
	public void initializeMaxNumClasses() {
		// nothing to do
	}
	
	public String getTitle() {
		return "Basic Color Map";
	}
}
