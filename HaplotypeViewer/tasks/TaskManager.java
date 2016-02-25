/*
 * Created on 11.02.2005
 */
package tasks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import tasks.gui.TaskManagerFrame;
import tasks.gui.TaskManagerStatusBarItem;


public class TaskManager implements TaskStateListener {

    public static TaskManager sharedInstance=new TaskManager();
    
    public static final String TITLE="Task Manager";
       
    private LinkedList<AbstractTask> tasks=new LinkedList<AbstractTask>();
    
    private TaskManagerStatusBarItem StatusBarItem=TaskManagerStatusBarItem.singleInstance;
       
    protected ConcurrentLinkedQueue<AbstractTask> justStarted = new ConcurrentLinkedQueue<AbstractTask>();
    protected DelayedUpdateTask taskAdder = new DelayedUpdateTask("Waiting for new tasks to mature", 1000) {
	
		protected void performUpdate() {
			while (justStarted.size()>0) {
				AbstractTask next = justStarted.poll();
				// add the task, but only if it is still running OR has something to report
				if (next!=null) {
					if ((next.getTaskState() & TaskStateEvent.TASK_FINISHED)==0 || next.getGUI().hasLog())
						addTask0(next);
				}
			}
			StatusBarItem.updateTasklist();
		}
	
		protected boolean needsUpdating() {
			return justStarted.size()>0;
		}
	};
    
	public synchronized void addTask(AbstractTask task) {
		justStarted.add(task);
		taskAdder.trigger();
		StatusBarItem.updateTasklist();
	}
	
    public synchronized void addTask0(AbstractTask task)
    {
    	boolean alreadyAdded=false;
    	for(AbstractTask anotherTask : tasks) {
    		if (anotherTask==task)
    			alreadyAdded=true;
    	}
    	
    	if (!alreadyAdded) {
    		task.addStateListener(this);      	
    		tasks.add(task);
    	}
        
    	getDialog().setVisible(true);

    	updateGUI();
    }
    
    
    protected synchronized void updateGUI() {    	
    	getDialog().updateTasklist();
        StatusBarItem.updateTasklist();
    }
    

    public synchronized void removeTask(AbstractTask task) {
    	removeTask(task,false);
    }
    
    public synchronized void removeTask(AbstractTask task, boolean force) {
    	tasks.remove(task);       
    	TaskManagerFrame.getInstance().removeTask(task, force);
    	updateGUI();
    }

    public synchronized void removeFinished() {
    	LinkedList<AbstractTask> tasks_ = new LinkedList<AbstractTask>();
    	tasks_.addAll(tasks);
    	for (AbstractTask at : tasks_)
    		if ((at.getTaskState() & TaskStateEvent.TASK_FINISHED) == TaskStateEvent.TASK_FINISHED)
    			removeTask(at, true);
    }
    
	public int getStateMask() {
		return TaskStateEvent.TASK_ANYSTATE;
	}

	public void processEvent(TaskStateEvent evt) {
		AbstractTask task = evt.getSource();
		if ( (task.getTaskState() & TaskStateEvent.TASK_FINISHED) == TaskStateEvent.TASK_FINISHED) {
			removeTask(task);
		} 
		updateGUI();
	}
	
	public TaskManagerFrame getDialog() {
		return TaskManagerFrame.getInstance();
	}
	
	public List<AbstractTask> getTasks() {
		return Collections.unmodifiableList(tasks);
	}
	
	public boolean showToolbarIndicator() {
		return numberOfRunningTasks()>0;
	}
	
	public int numberOfRunningTasks() {
		int rt = 0;
		for (AbstractTask t : tasks) {
			if ((t.getTaskState() & TaskStateEvent.TASK_FINISHED)==0)
				rt++;
		}
		for (AbstractTask t : justStarted) {
			if ((t.getTaskState() & TaskStateEvent.TASK_FINISHED)==0)
				rt++;
		}
		return rt;
	}
	
	public AbstractTask getTopTask() {
		for (AbstractTask t : tasks) {
			if ((t.getTaskState() & TaskStateEvent.TASK_FINISHED)==0)
				return t;
		}
		for (AbstractTask t : justStarted) {
			if ((t.getTaskState() & TaskStateEvent.TASK_FINISHED)==0)
				return t;
		}
		return null;
	}
}
