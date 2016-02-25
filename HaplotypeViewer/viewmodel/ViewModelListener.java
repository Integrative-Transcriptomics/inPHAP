package viewmodel;

import java.util.EventListener;

import events.ViewModelEvent;

public interface ViewModelListener extends EventListener {
	
	public void viewModelChanged(ViewModelEvent e);
}
