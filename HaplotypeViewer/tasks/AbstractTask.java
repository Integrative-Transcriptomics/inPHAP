/*
 * Created on 11.02.2005
 */
package tasks;

import java.util.concurrent.Semaphore;

import javax.swing.AbstractAction;
import javax.swing.event.EventListenerList;

import tasks.gui.TaskStateGUI;

public abstract class AbstractTask extends Thread implements ProgressListener
{
	private int state = TaskStateEvent.TASK_NEW;
	private int percentageX100 = 0;
	private long remainingSeconds = -1;
	private long startTime = 0;
    private boolean isCancelled = false;
    protected boolean isHidden = false;
    protected String formattedPercentage;
    
    private TaskStateGUI gui; 

    //ability to wait for this run() method to be finished
    private final Semaphore semaphore=new Semaphore(1);

    private EventListenerList  eventListenerList  = new EventListenerList();

    private ProgressListener progress;
	
	public AbstractTask(String title) {
		super(title);			
		initialize();
		try {
			semaphore.acquire(); //acquire early
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getProgress() {
		return percentageX100;
	}
	
	public void setProgressListener(ProgressListener parent) {
		progress = parent;
	}
	
	public int getTaskState() {
		return state;
	}
	
	public long getRemainingSeconds() {
		return remainingSeconds;
	}
	
	public String getRemainingTime() {
		if (remainingSeconds>0) {
			int hours = (int)remainingSeconds / 3600;
			int remsecs = (int)(remainingSeconds - hours*3600);
			int minutes = remsecs / 60;			
			int seconds = remsecs - minutes*60;  
			// Format time
			String hstr = hours>0? hours+":" : "";
			String mstr = minutes+":";
			while (hours>0 && mstr.length()<3)
				mstr = "0"+mstr;
			String sstr = ""+seconds;
			while (sstr.length()<2)
				sstr = "0"+sstr;
			return hstr+mstr+sstr;
		}
		return "";
	}
	
	protected String formatPercentage(int percentageX100) {
		if (formattedPercentage==null) {
			if (percentageX100==-1)
				formattedPercentage="";
			else {
				int ppart = (percentageX100 % 100);
				String pdec;
				if (ppart<10)
					pdec = "0"+ppart;
				else 
					pdec = Integer.toString(ppart);

				if (pdec.length()<2) 
					pdec = "0"+pdec;
				formattedPercentage=percentageX100/100+"."+pdec+" %";
			}
		}
		return formattedPercentage;
	}
	
	public void setProgress(int percentageX100) {
		if (percentageX100==this.percentageX100)
			return;
		try {
			formattedPercentage=null;
			if (percentageX100>-1)		
				setProgress(percentageX100, formatPercentage(percentageX100));
			else
				setProgress(-1,null);		
		} catch(Exception e) {
			// ignore
		}
	}
	
	public void setProgress(int percentageX100, String progressInfo) {
		try {
			if (percentageX100!=this.percentageX100) {
				recomputeRemainingTime(percentageX100);
				this.percentageX100 = percentageX100;
				formattedPercentage=null;
			}
			if (gui!=null)
				gui.setProgress(percentageX100, progressInfo);
			if (progress!=null && percentageX100>-1) // propagate DETERMINATE state to parent
				progress.setProgress(percentageX100);
		} catch (Exception e) {
			// ignore
		}
	}
	
	public void writeLog(String logLine) {
		getGUI().writeLog(logLine);
	}
	
	private void recomputeRemainingTime(int new_percentage) {		
		long currentTime = System.currentTimeMillis();
		if (startTime!=0 && new_percentage!=0) {
			long time_delta = currentTime - startTime;
			//System.out.print(time_delta+" millis for "+new_percentage+" centi%  -> ");
			double time_per_point_zero_one_percent = (double)time_delta / (double)new_percentage;
			if (time_per_point_zero_one_percent==0) {
				remainingSeconds=0;
			} else {
				long remaining_percent = 10000 - new_percentage;
				double time_for_100_percent = time_per_point_zero_one_percent * remaining_percent;
				//System.out.print(time_for_100_percent+" millis = "+remainingSeconds+ " seconds");
				remainingSeconds = (long)time_for_100_percent / 1000;
			}
			//System.out.println();
		}
		
		if (startTime==0)
			startTime = currentTime;
	}	
	
	
	protected void setTaskState(int newstate) {
		this.state=newstate;
		fireChangeEvent();
	}
	
	public TaskStateGUI getGUI() {
		if (gui==null)
			gui=new TaskStateGUI(this);
		return gui;
	}
	
    public void addStateListener(TaskStateListener tsl) {
    	eventListenerList.add(TaskStateListener.class,tsl);
    }
	
        
    /**
     * Initialize setting before starting the thread. Here, the
     * Actions can be appended or dialog
     * settings can be set (see {@linkplain #getStandAloneDialog()}).
     * <p>
     * Normally this method contains the {@linkplain #addAction(AbstractAction)} 
     * methods.
     * <p>
     * Note: Do not set the modal dialog to visible in here!
     */
    protected abstract void initialize();
    
   
    /**
     * 
     * Overrides <code>Thread.run()</code>. You cannot override
     * this method anymore. Please use {@linkplain #doWork} to implement
     * your tasks. So there will always be a 
     * <code>TaskFinishedEvent</code> fired, when the thread properly
     * stops.
     * @deprecated Do not use run(), use start() instead.
     */
    @Deprecated
    public final void run() //you may not overwrite this method
    {      
		
        //this is needed to finish the task properly
        try
        {
        	if (!isHidden) 
        		TaskManager.sharedInstance.addTask(this);
        	
    		this.setPriority(Thread.MIN_PRIORITY);
        	
        	setTaskState(TaskStateEvent.TASK_RUNNING);
        	
            doWork();
            
            if (!(getTaskState()==TaskStateEvent.TASK_FAILED || getTaskState()==TaskStateEvent.TASK_CANCELLED))
            	setTaskState(TaskStateEvent.TASK_FINISHED);
            
        } catch (TaskCancelledException tce) {
        	setTaskState(TaskStateEvent.TASK_CANCELLED);
        	
        } catch (OutOfMemoryError ome) {
        	setTaskState(TaskStateEvent.TASK_FAILED); //should be called here!
        	writeLog("Out of memory while running this task.\n");
        	
        } catch(Throwable t) {
        	
          setTaskState(TaskStateEvent.TASK_FAILED); //should be called here!
          
          writeLog("** Task failed **\n");
          if (!(t instanceof NullPointerException)) {
        	  writeLog("** Exception message: "+t.getMessage()+"\n");
        	  if (t.getCause()!=null && !(t.getCause() instanceof NullPointerException))
        		  writeLog("** Extended message: "+t.getCause().getMessage()+"\n");
          }
          writeLog("** Type of exception: "+t.getClass().getSimpleName());
          if (t.getCause()!=null) {
        	  writeLog("  (caused by a "+t.getCause().getClass().getSimpleName()+")");
          }
          writeLog("\n");
          
//          if (MaydayDefaults.isDebugMode()) {
//        	  writeLog("** Stack trace: \n");
//        	  StringWriter sw = new StringWriter();
//        	  PrintWriter ps = new PrintWriter(sw) {
//        		  public void println(String s) {
//        			  super.println("** "+s);
//        		  }
//        		  public void println(Object x) {
//        			  // empty
//        		  }        				
//        	  };
//        	  t.printStackTrace(ps);
//        	  ps.flush();
//        	  writeLog(sw.getBuffer().toString());
//          } else {
//        	  writeLog("** A strack trace was written to the message window\n");
//          }
          t.printStackTrace();
        }
                
        semaphore.release();
        
        System.gc();
    }
    
    public final void cancel() {
    	if (!isCancelled) {
    		isCancelled=true;    	
    		this.setTaskState(TaskStateEvent.TASK_CANCELLING);
    		doCancel();
    	}
    	// make sure the thread will cancel after at most 10 seconds
    	new Thread("Waiting to kill task "+this.getName()) {
    		@SuppressWarnings("deprecation")
			public void run() {
    			try {
					Thread.sleep(10000);
					if (AbstractTask.this.getTaskState()==TaskStateEvent.TASK_CANCELLING)
						AbstractTask.this.stop(new TaskCancelledException());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			
    		}
    	}.start();
    }
    
    // Call this method as soon as you start processing a cancel request
    // This will prevent your task from being killed after 10 seconds of not reacting!
    public void processingCancelRequest() {
    	setTaskState(TaskStateEvent.TASK_CANCELLING_CLEANUP);
    	setProgress(-1);
    }
    
    public boolean hasBeenCancelled() {
    	return isCancelled;
    }

    // overload this method to process cancel requests, or call hasBeenCancelled in your task loop
    public void doCancel() {
    }
    
    /**
     *  Override this method to do your work!
     *  
     *  To inform the listeners for changing their properties
     *  use the <code>ProgressStateEvent</code>. First clear
     *  the events settings with <code>clearSettings()</code>
     *  and <code>put()</code> the appropriate properties.  
     *  Then you can <code>fireProcessStateChanged()</code>.
     */
    abstract protected void doWork()
    throws Exception; // added by NG in order to be able to propagate exceptions (2005-10-24)
    
    /**
     * Wait for finishing this task.
     * Use this method to synchronize with other threads.
     * 
     */
    public void waitFor()
    {
        if (Thread.currentThread()==this) 
        	return;
        
        try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.semaphore.release();
    }
    
  
    public void fireChangeEvent()
    {
    	TaskStateEvent evt = new TaskStateEvent(this);
        // Guaranteed to return a non-null array
        Object[] listeners = eventListenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event        
        for (int i = listeners.length-1; i>=0; i-=2) {        	
        	TaskStateListener tsl = (TaskStateListener)listeners[i];
        	if ( tsl.getStateMask()==TaskStateEvent.TASK_ANYSTATE || 
        		 (tsl.getStateMask() & this.getTaskState()) == tsl.getStateMask() ) {
        		tsl.processEvent(evt);        		
        	}
        }
    }
    
    
    /* Interface functions for plugins using the old abstracttask methods */
    public void fireIndeterminateChanged(boolean indeterminate) {
    	if (indeterminate) 
    		setProgress(-1);
    	else 
    		setProgress(0);
    }
    
    
}
