package tasks;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public abstract class DelayedUpdateTask {
		
		private boolean valueIsAdjusting = false;
		private long UPDATE_INTERVAL = 300;
		private Timer timer;
		private Semaphore timerLock = new Semaphore(1);
		private int triggerCount;
		
		protected Thread executingThread;		
		protected String name;
		
		
		private class ComeBackLater extends TimerTask {
			public void run() {
				executingThread = Thread.currentThread();
				try {
					update();
				} catch (Throwable ie) {
					if (debug1) {
						System.out.println(EXCEPTED);
						ie.printStackTrace();
					}
				}
				executingThread = null;
			}				
		}
		
		private final String UPDATENOW; 
		private final String NONEED;
		private final String EXCEPTED;
		private final String EXCEPTION_THROWN;
			
		protected final static boolean debug1 = false; //MaydayDefaults.isDebugMode();
		protected final static boolean debug2 = false; //MaydayDefaults.isDebugMode();
		
		public DelayedUpdateTask(String name) {
			UPDATENOW = name + " - updating now...";
			NONEED = name + " - no update needed";
			EXCEPTED = name + " - encountered an exception";
			EXCEPTION_THROWN = name + " - stopping via exception";
			this.name = name;
		}
		
		public DelayedUpdateTask(String name, long delay) {
			this(name);
			UPDATE_INTERVAL=delay;
		}
		
		protected abstract boolean needsUpdating();
		
		protected abstract void performUpdate();
		
		protected String getAdditionalDebugInfo() {
			return "";
		}
		
		protected void setTimerNull() {
			timerLock.acquireUninterruptibly();
			timer = null;
			timerLock.release();
		}
		
		protected void cancelTimer() {
			timerLock.acquireUninterruptibly();					
			if (timer!=null) { // no need to come back later
				timer.cancel();
				if (debug2) System.out.println(name+"- scheduler stopped");
			}
			timerLock.release();
		}
		
		protected void setTimerIfNull() {
			timerLock.acquireUninterruptibly();
			if (timer==null) {
				timer = new Timer(name, true);
				timer.schedule(new ComeBackLater(), UPDATE_INTERVAL, UPDATE_INTERVAL);
			}
			timerLock.release();
		}
		
		protected void update() {
				if (!valueIsAdjusting) {					
					cancelTimer();					
					// make sure that the contents REALLY changed
					if (needsUpdating()) {
						if (debug1) System.out.println(UPDATENOW+" after "+triggerCount+" requests ("+getAdditionalDebugInfo()+")");
						triggerCount=0;
						performUpdate();					
					} else {
						if (debug2) System.out.println(NONEED);
						triggerCount=0;
					}
					setTimerNull();
					if (triggerCount>0) {
						// some requests in the meantime
						if (debug2) System.out.println(name+" - Rescheduling with "+triggerCount+" requests");
						valueIsAdjusting = true;
						setTimerIfNull();
					}
					if (debug2) System.out.println(name+" - finished");
				} else {				
					valueIsAdjusting = false;
					if (debug2) System.out.println(name+" - deferring update.");
					//reschedule to prevent needless updating during massive changes
					setTimerIfNull();
				}				
		}
		
		public void trigger() {
			++triggerCount;
			valueIsAdjusting = true; 
			setTimerIfNull();
		}
		
		@SuppressWarnings("deprecation")
		public void killIfRunning() {
			Thread t = executingThread;
			if (executingThread!=null) {
				try {
					if (debug1) System.out.println(EXCEPTION_THROWN);
					t.stop(new Throwable(""));
				} catch (Throwable tr) 
				{
					
				}
			}				
		}
		
	}