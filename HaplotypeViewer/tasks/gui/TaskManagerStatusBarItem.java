package tasks.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import tasks.AbstractTask;
import tasks.DelayedUpdateTask;
import tasks.TaskManager;
import tasks.TaskStateEvent;

@SuppressWarnings("serial")
public class TaskManagerStatusBarItem  extends JProgressBar implements StatusBarItem {

	public final static TaskManagerStatusBarItem singleInstance = new TaskManagerStatusBarItem();

	private AbstractTask topTask;
	
//	private static final String TOOLTIPTEXT=
//		"<html><head><style type=\"text/css\">" +
//		"<!-- " +
//		"body {" +
//		" margin:1px;}" +
//		" --></style></head><body>"+
//		"<b>"+TaskManager.TITLE+"</b><hr>"+
//		"</body></html>";

	private TaskManagerStatusBarItem() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount()==2)  {
					TaskManagerFrame.getInstance().setVisible(true);
				}
			}
		});
		setVisible(false);
		setIndeterminate(true);
		setStringPainted(false);
		setMaximum(10000);
		setMinimum(0);
	}

	public JComponent getRenderingComponent() {
		return this;
	}

	public int getPosition() {
		// always take the last position in the status bar
		return Integer.MAX_VALUE; 
	}

	public void updateTasklist() {
//		updateToolTip();
		int running = TaskManager.sharedInstance.numberOfRunningTasks();
		boolean b = running>0;
		setVisible(b);	
		try {
			setIndeterminate(b);
		} catch (Exception e) {
			// ignore GUI exceptions
		}
		replaceTopTask(TaskManager.sharedInstance.getTopTask());
	}
	
	protected void replaceTopTask(AbstractTask at) {
		synchronized (singleInstance) {
			topTask = at;
		}		
		statusBarProgressUpdater.trigger();
	}
	
	protected DelayedUpdateTask statusBarProgressUpdater = new DelayedUpdateTask("Status Bar Progress", 250) {

		protected boolean needsUpdating() {
			synchronized(singleInstance) {
				return (topTask!=null && (topTask.getTaskState()&TaskStateEvent.TASK_FINISHED)==0);
			}
		}

		@Override
		protected void performUpdate() {
			while (true) {
				synchronized (singleInstance) {
					if (topTask!=null && (topTask.getTaskState()&TaskStateEvent.TASK_FINISHED)==0) {
						int pg = topTask.getProgress();
						setValue(pg);
						setIndeterminate(pg<1);
					} else
						break;
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// 
				}				
			}
		}
		
	};

}
