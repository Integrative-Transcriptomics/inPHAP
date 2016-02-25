package settings.events;

import java.util.EventListener;

public interface SettingChangeListener extends EventListener {

	public void settingChanged(SettingChangeEvent e);
}
