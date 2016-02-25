package settings.events;

public class SettingChangeEvent {
	
	public static final int SETTING_CHANGED = 0;
	public static final int REPLACE = 1;
	public static final int COLOR_CHANGED = 2;
	public static final int SIZE_CHANGED = 3;
	public static final int COLOR_GRADIENT_CHANGED = 4;
	
	private Object source;
	private int type;
	private String message;
	
	public SettingChangeEvent(Object source, int type, String message) {
		this.source = source;
		this.type = type;
		this.message = message;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public int getChange() {
		return this.type;
	}
	
	public String getMessage() {
		return this.message;
	}
}
