package tasks;

public final class SubTaskProgressListener implements ProgressListener {
	
	protected ProgressListener parent;
	protected int of;
	protected double sc;
	protected String fixedMessage;
	
	public SubTaskProgressListener(ProgressListener par, int offset, double scale) {
		this(par, offset, scale, null);
	}
	
	public SubTaskProgressListener(ProgressListener par, int offset, double scale, String fixedMessage) {
		parent = par;
		sc = scale;
		of = offset;
		this.fixedMessage = fixedMessage;
	}
	
	
	public void setProgress(int percentageX100) {
		if (percentageX100<0)
			return;
		if (fixedMessage!=null) {
			parent.setProgress((int)(percentageX100*(sc/10000d))+of, fixedMessage);
		} else {
			parent.setProgress((int)(percentageX100*(sc/10000d))+of);
		}
	}
	
	public void setProgress(int percentageX100, String message) {
		if (percentageX100<0)
			return;
		parent.setProgress((int)(percentageX100*(sc/10000d))+of, fixedMessage!=null?fixedMessage+" ("+message+")":message);
	}
		
}
