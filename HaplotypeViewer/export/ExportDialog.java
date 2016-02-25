package export;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import settings.guiComponents.SettingDialog;
import tasks.AbstractTask;

public class ExportDialog {
	
//	boolean visibleOnly;

	public ExportDialog(final Component plotComponent, boolean visibleAreaOnly, Window owner) {
		
		final ExportSetting s = new ExportSetting(!visibleAreaOnly);
		
//		visibleOnly = visibleAreaOnly;
		
		Dimension d = getBaseDimension(plotComponent, visibleAreaOnly);
		s.setInitialDimension(d);
		
		SettingDialog sdl = s.getDialog();
		s.setPreviewActionObjects(plotComponent, sdl);
		sdl.setModal(true);
		sdl.setVisible(true);

		if (sdl.closedWithOK()) {

			String l_defaultFileName = "Exported Plot";
			l_defaultFileName = l_defaultFileName.toLowerCase();
			l_defaultFileName = l_defaultFileName.replace( ' ', '_' ); // replace spaces
			l_defaultFileName += "." + s.getMethod().getFormatName().toLowerCase();

			JFileChooser l_chooser=new JFileChooser();
			String s_lastExportPath= "";
			if(!s_lastExportPath.equals(""))
				l_chooser.setCurrentDirectory(new File(s_lastExportPath));
			l_chooser.setSelectedFile( new File( l_defaultFileName ) );
			int l_option = l_chooser.showSaveDialog( owner );

			if ( l_option  == JFileChooser.APPROVE_OPTION ) {
				String l_fileName = l_chooser.getSelectedFile().getAbsolutePath();
				if ( l_fileName == null )
					return;

				if (new File(l_fileName).exists() && 
						JOptionPane.showConfirmDialog(null, 
								"Do you really want to overwrite the existing file \""+l_fileName+"\"?",
								"Confirm file overwrite", 
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
								!=JOptionPane.YES_OPTION
				) {
					return;
				}
				s.setTargetFileName(l_fileName);

				new AbstractTask("Exporting...") {

					final Exception[] exceptions = new Exception[]{null};
					
					@Override
					protected void doWork() throws Exception {
						
						/* We do this in invokeAndWait because:
						  - we will at some point acquire the AWTTreeLock, most likely inside paintChildren()
						  - if at some point invokeAndWait is used (e.g. in GLCanvas), we will also acquire the AWTInvocationLock
						  - if this happens while another task is in the AWTEventQueue, waiting for us to release the TreeLock,
						    we will deadlock on waitign for the AWTInvocationLock.
						  ==> If we do invokeAndWait now, we make sure that we get the invokation lock now 
						      before we acquire the treelock, and thus we prevent the deadlock. 
						      */
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {								
								ExportPlugin epp = s.getMethod();
								try {
									epp.exportComponent(plotComponent, s);
								} catch (Exception e) {
									exceptions[0] = e;
								}
							}
						});

						if (exceptions[0]!=null) {
							throw exceptions[0];
						}
					}

					protected void initialize() {}

				}.start();
			}
		}
	}
	
	public static Dimension getBaseDimension(Component c, boolean visibleOnly) {		
		Dimension d = c.getSize();
		Dimension p = c.getPreferredSize();
		if (visibleOnly)
			return d;
		
		// this heuristic might be problematic in the future
		if (p!=null && (p.width!=p.height || p.width+p.height>100)) {
			d.setSize(p);
		}
		return d;
	}
}
