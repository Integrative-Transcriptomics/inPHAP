package events;

public class ViewModelEvent {

	public static final int ROW_SORTING_CHANGED = 0;
	public static final int COLUMN_SORTING_CHANGED = 1;
	public static final int DATASET_CHANGED = 2;
	public static final int ERROR = 3;
	public static final int ROW_SELECTION_CHANGED = 4;
	public static final int COLUMN_SELECTION_CHANGED = 5;
	public static final int SETTINGS_CHANGED = 6;
	public static final int SUBJECT_META_INFO_CHANGED = 7;
	public static final int COLOR_CHANGED = 8;
	public static final int PLOT_RESIZE = 9;
	public static final int COLOR_GRADIENT_CHANGED = 10;
	public static final int SNP_META_INFO_CHANGED = 11;
	public static final int LOG_UPDATE = 12;
	public static final int COLUMN_JUMP_EVENT = 13;
	public static final int ROW_JUMP_EVENT = 14;
	public static final int EXPORT_PLOT = 15;
	public static final int AGGREGATION_CHANGED = 16;
	public static final int SNPS_FILTERED = 17;
	
	private int change;
	private Object source;
	private String status;
	
	public ViewModelEvent(Object source, int change, String status) {
		this.source = source;
		this.change = change;
		this.status  =status;
	}
	
	public int getChange() {
		return this.change;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public String getStatus() {
		return this.status;
	}
}
