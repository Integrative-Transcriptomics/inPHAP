package main;
import events.ViewModelEvent;
import gui.HVMainPanel;

import java.awt.Dimension;
import java.io.File;
import java.text.NumberFormat;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.jtattoo.demo.app.GUIProperties;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;


public class HaplotypeViewer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7440665452330657222L;
	public static final String TITLE = "inPHAP";
	public static final String VERSION = "1.1";
	public static final String LONG_TITLE = "Interactive Visualization of Genotype and Phased Haplotype Data";
	
	private static Runtime runtime = Runtime.getRuntime();
	
	public static void main(String[] args) {
		
		try {
			String font = "Arial PLAIN 11";
			Properties props = new Properties();
            props.put("logoString", "inPHAP");
			props.put("linuxStyleScrollBar", "false");
			props.put("controlTextFont", font);
			props.put("systemTextFont", font);
			props.put("userTextFont", font);
			props.put("menuTextFont", font);
			props.put("windowTextFont", font);
			props.put("subTextFont", font);
			AcrylLookAndFeel.setCurrentTheme(props);
			UIManager.setLookAndFeel(GUIProperties.PLAF_ACRYL);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		HaplotypeViewer htv = new HaplotypeViewer();
		htv.setTitle(TITLE + " (" + VERSION + " )");
		
		HVMainPanel mainPanel = new HVMainPanel(htv);
		htv.add(mainPanel);
		
		htv.setDefaultCloseOperation(EXIT_ON_CLOSE);
		htv.setPreferredSize(new Dimension(1280, 960));
		htv.pack();
		htv.centerOnScreen();
		htv.setVisible(true);
		
		//fire report on system properties
		ViewModelEvent vme = new ViewModelEvent(htv, ViewModelEvent.LOG_UPDATE, getSystemInfo());
		mainPanel.getViewModel().fireChanged(vme);
		
		//fire version log
		vme = new ViewModelEvent(htv, ViewModelEvent.LOG_UPDATE, getFullName());
		mainPanel.getViewModel().fireChanged(vme);
	}
	
	private void centerOnScreen() {
		this.setLocationRelativeTo(null);
	}

	public static String getFullName() {
		return TITLE + "(" + VERSION + ") " + LONG_TITLE;
	}
	
	public static String getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(OsInfo());
        sb.append(MemInfo());
        sb.append(DiskInfo());
        return sb.toString();
    }
	
	private static String OSname() {
        return System.getProperty("os.name");
    }

    private static String OSversion() {
        return System.getProperty("os.version");
    }

    private static String OsArch() {
        return System.getProperty("os.arch");
    }

    private static String MemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Free memory (MB): ");
        sb.append(format.format(freeMemory / (1024*1024)));
        sb.append("\n");
        sb.append("Allocated memory (MB): ");
        sb.append(format.format(allocatedMemory / (1024*1024)));
        sb.append("\n");
        sb.append("Max memory (MB): ");
        sb.append(format.format(maxMemory / (1024*1024)));
        sb.append("\n");
        sb.append("Total free memory (MB): ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / (1024*1024)));
        sb.append("\n");
        return sb.toString();

    }

    private static String OsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(OSname());
        sb.append(", ");
        sb.append("Version: ");
        sb.append(OSversion());
        sb.append(" : ");
        sb.append(OsArch());
        sb.append("\n");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append("\n");
        return sb.toString();
    }

    private static String DiskInfo() {
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        StringBuilder sb = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();

        /* For each filesystem root, print some info */
        for (File root : roots) {
            sb.append("File system root: ");
            sb.append(root.getAbsolutePath());
            sb.append("\n");
            sb.append("Total space (MB): ");
            sb.append(format.format(root.getTotalSpace() / (1024*1024)));
            sb.append("\n");
            sb.append("Free space (MB): ");
            sb.append(format.format(root.getFreeSpace() / (1024*1024)));
            sb.append("\n");
            sb.append("Usable space (MB): ");
            sb.append(format.format(root.getUsableSpace() / (1024*1024)));
            sb.append("\n");
        }
        return sb.toString();
    }
}
