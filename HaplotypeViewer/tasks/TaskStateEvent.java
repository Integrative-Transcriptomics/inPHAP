/**
 * 
 */
package tasks;

import java.util.EventObject;

@SuppressWarnings("serial")
public class TaskStateEvent extends EventObject {

	public static final int TASK_ANYSTATE = 15;		 // 1111
	public static final int TASK_NEW = 1;            // 001
	public static final int TASK_RUNNING = 2;        // 010
	public static final int TASK_CANCELLING = 3;     // 011
	
	public static final int TASK_FINISHED = 4;       // 100 -> finish-bit set
	public static final int TASK_CANCELLED = 5;      // 101 -> finish-bit set
	public static final int TASK_FAILED    = 6;      // 110 -> finish-bit set

	public static final int TASK_CANCELLING_CLEANUP = 8;     // 1000

	public static String[] StateNames = new String[]{
		"",
		"Starting",  //1
		"Running...",   //2 
		"Cancelling...",//3
		"Finished",  //4
		"Cancelled", //5
		"Failed", //6
		"",	 //7
		"Cleaning up...",	 //8
		"", //9
		"", //10
		"", //11
		"", //12
		"", //13
		"", //14
		""	 //15
		};
	
	public TaskStateEvent(Object sender) {
		super(sender);
	}
	
	public AbstractTask getSource() {
		return (AbstractTask)super.getSource();
	}
	
}