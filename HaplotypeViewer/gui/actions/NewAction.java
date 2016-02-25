package gui.actions;

import io.IDataParser;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import settings.guiComponents.SettingDialog;
import settings.typed.NewProjectSetting;
import tasks.AbstractTask;
import viewmodel.ViewModel;
import events.ViewModelEvent;

public class NewAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4700369056422938642L;
	private ViewModel viewModel;
	
	public NewAction(ViewModel viewModel, String title) {
		super(title);
		this.viewModel = viewModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final NewProjectSetting setting = new NewProjectSetting();
		SettingDialog dialog = new SettingDialog(viewModel.getOwner(), setting);
		dialog.setVisible(true);

		if(dialog.closedWithOK()) {
            final AbstractTask readFileTask = new AbstractTask("Loading file: "){

                @Override
                protected void initialize() {}

                @Override
                protected void doWork() throws Exception {
                	IDataParser parser = setting.getDataParser();
                    parser.readFile(this);
                    viewModel.setDataSet(parser.getDataSet());
                    viewModel.fireChanged(new ViewModelEvent(this, ViewModelEvent.DATASET_CHANGED, "New Dataset loaded - "));
                    parser.getDataSet().addSNPMetaData(parser.getPMSNPMeta());
                }
            };
            readFileTask.start();
			//this.loadNewDataSet();
		}
	}
	
	//Method not needed any more
//	public void loadNewDataSet() {
//		JFileChooser fileChooser = new JFileChooser(oldPath);
//		fileChooser.setFileFilter(new FileFilter() {
//			@Override
//			public boolean accept(File f) {
//				if(f.getName().toLowerCase().endsWith(".vcf"))
//					return true;
//				if(f.isDirectory())
//					return true;
//				return false;
//			}
//
//			@Override
//			public String getDescription() {
//				return "Variant Call Format File (.vcf)";
//			}
//		});
//		
//		int approve = fileChooser.showOpenDialog(viewModel.getOwner());
//		
//		if(approve == JFileChooser.APPROVE_OPTION) {
//			final File f = fileChooser.getSelectedFile();
//			oldPath = f.getParent();
//			if(!f.isDirectory() && f.canRead()) {
//				String path = f.getAbsolutePath();
//				try {
//					final VCFParser parser = new VCFParser(path);
//					
//					AbstractTask readFileTask = new AbstractTask("Loading file: " + f.getName()) {
//
//						@Override
//						protected void initialize() {}
//
//						@Override
//						protected void doWork() throws Exception {
//							parser.readFile(this);
//							DataSet dataSet = parser.getDataSet();
//							viewModel.setDataSet(dataSet);
//							viewModel.fireChanged(new ViewModelEvent(this, ViewModelEvent.DATASET_CHANGED, "New DataSet loaded - " + f.getName()));
//							dataSet.addSNPMetaData(parser.getPMSNPMeta());
//						}
//					};
//					
//					readFileTask.start();
//				} catch(Exception ex) {
//					ex.printStackTrace();
//					
//					String message = "File cannot be read!";
//					JOptionPane.showMessageDialog(viewModel.getOwner(), 
//							message, 
//							"New DataSet Error", 
//							JOptionPane.ERROR_MESSAGE);
//					viewModel.fireChanged(new ViewModelEvent(this,ViewModelEvent.ERROR, message));
//				}
//			} else {
//				String message = "File cannot be read!";
//				JOptionPane.showMessageDialog(viewModel.getOwner(), 
//						message, 
//						"New DataSet Error", 
//						JOptionPane.ERROR_MESSAGE);
//				viewModel.fireChanged(new ViewModelEvent(this,ViewModelEvent.ERROR, message));
//			}
//		}
//	}
}
