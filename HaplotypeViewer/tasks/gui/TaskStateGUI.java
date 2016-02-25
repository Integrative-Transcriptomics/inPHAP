package tasks.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import tasks.AbstractTask;
import tasks.TaskManager;
import tasks.TaskStateEvent;
import tasks.TaskStateListener;

@SuppressWarnings("serial")
public class TaskStateGUI extends JPanel implements TaskStateListener {



	private JLabel TaskName;
	private JProgressBar progress;
	private JLabel currentState;
	private JButton cancelButton;
	private AbstractTask myTask;
	private JTextArea logTextArea;
	
	public TaskStateGUI(AbstractTask theTask) {		
		super(new BorderLayout());
		myTask = theTask;
		
		setBackground(Color.white);
		
		// NORTH
		JPanel northPanel = new JPanel();//new BorderLayout());
		{
			northPanel.setLayout(new GridBagLayout());
			
			TaskName = new JLabel(myTask.getName());

			northPanel.add(TaskName,
					new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
							new Insets( 1, 1, 1, 10), 0, 0 )
			);
			
			progress = new JProgressBar(0,10000);
			progress.setAlignmentX(JProgressBar.CENTER_ALIGNMENT);
			northPanel.add(progress,
					new GridBagConstraints( 1, 0, 1, 1, 100.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, 
							new Insets( 1, 1, 1, 1), 0, 0 )
			);

			currentState = new JLabel();
			northPanel.add(currentState,
					new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
							new Insets( 1, 5, 1, 5), 0, 0 )
			);
			
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					if (cancelButton.getText().equals("Cancel")) {
						cancelButton.setVisible(false);						
						myTask.cancel();
						cancelButton.setText("Remove");
					} else {
						cancelButton.setVisible(false);
						TaskManager.sharedInstance.removeTask(myTask, true);
					}
				}
			});
			
			northPanel.add(cancelButton,
					new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
							new Insets( 1, 1, 1, 1), 0, 0 )
			);
			northPanel.setBackground(Color.white);

		}
		add(northPanel, BorderLayout.NORTH);
				
		setPreferredSize(getMinimumSize());		
		//setMaximumSize(null);
		
		setProgress(-1,null);
		processEvent(new TaskStateEvent(myTask)); // update according to current task state

		myTask.addStateListener(this); // keep updated
		
		
	}
	
	
	private Dimension maxDim;
	
	public Dimension getMaximumSize() {		
		if (maxDim==null)
			maxDim = new Dimension();//getOuterWidth(), progress.getPreferredSize().height);

		maxDim.width = getOuterWidth();
		maxDim.height = progress.getPreferredSize().height+getInsets().top+getInsets().bottom;
		
		if (logTextArea!=null) {
			maxDim.height = 100000;
//			setMaximumSize(null);
//			return super.getMaximumSize();
		}
		return maxDim;
	}
	
	protected int getOuterWidth() {
		Component c = getParent();
		while (c!=null && !(c instanceof JViewport)) {
			c = c.getParent();
		}
		if (c!=null) {
			JViewport jvc = ((JViewport)c);
			int v = jvc.getExtentSize().width-10;
			return v;
		}
		return 100000;			
	}
	
	public Dimension getMinimumSize() {
		return super.getMinimumSize();
//		getMaximumSize();
//		return maxDim;
	}
	
	public void writeLog(String logLine) {
		if (logTextArea==null) {
			logTextArea = new JTextArea();
			logTextArea.setEditable(false);
			logTextArea.setWrapStyleWord(true);
			JScrollPane centerPanel = new JScrollPane();
			centerPanel.setViewportView(logTextArea);
			centerPanel.setBackground(Color.white);
			centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			this.add(centerPanel, BorderLayout.CENTER);
//			this.add(new JSeparator(), BorderLayout.SOUTH);
			maxDim=null;
		}
		logTextArea.append(logLine);
		logTextArea.setCaretPosition(logTextArea.getText().length());
		invalidate();
		validate();		
	}
	
	public void setProgress(int Progress, String info) {
		
		try {
			progress.setIndeterminate(Progress<0);
			progress.setStringPainted(info!=null);
		
			if (Progress>0)
				progress.setValue(Progress);
			
			if (info!=null) 
				progress.setString(info);	
			
			if (myTask.getTaskState()==TaskStateEvent.TASK_RUNNING) {
				currentState.setText(myTask.getRemainingTime());
			} 
			repaint();
		} catch (Throwable t) {
			// this sould NEVER affect the _real_ underlying task. 
			// Exceptions can occur here if setIndet(false) is called before a previous call to setIndet(true)
			// was fully processed by BasicProgressBarUI$Animator.start(), such that timer==null in  BasicProgressBarUI$Animator.stop().
		}
	}
	
	public void processEvent(TaskStateEvent evt) {
		int state = myTask.getTaskState(); // wer sollte uns sonst was schicken?
		if (state!=TaskStateEvent.TASK_RUNNING) {
			currentState.setText(TaskStateEvent.StateNames[state]);
			revalidate();
		}
		if ( (state & TaskStateEvent.TASK_FINISHED) == TaskStateEvent.TASK_FINISHED) {
			//cancelButton.setVisible(false);
			if (cancelButton!=null) {
				cancelButton.setText("Remove");
				cancelButton.setVisible(true);
			}
			progress.setIndeterminate(false);
		}
	}
	
	public boolean hasLog() {
		return logTextArea!=null;
	}
	
	public String getLogContent() {
		return hasLog()?logTextArea.getText():"";
	}
	
	
	public void clearLog() {
		if (hasLog()) {
			logTextArea.setText("");
			logTextArea = null;
		}
	}

	public int getStateMask() {
		return TaskStateEvent.TASK_ANYSTATE;
	}
}
	
