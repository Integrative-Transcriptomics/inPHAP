package tasks;

public interface ProgressListener {
	
	public void setProgress(int percentageX100);
	
	public void setProgress(int percentageX100, String message);
		
}
