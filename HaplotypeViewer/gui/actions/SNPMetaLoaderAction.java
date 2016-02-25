package gui.actions;

import events.ViewModelEvent;
import io.SNPMetadataParser;
import io.SubjectMetadataParser;
import tasks.AbstractTask;
import viewmodel.ViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by peltzer on 2/28/14.
 */
public class SNPMetaLoaderAction extends AbstractAction {
    private ViewModel viewModel;
    private String oldPath = "";

    public SNPMetaLoaderAction(ViewModel viewmodel, String title){
        super(title);
        this.viewModel = viewmodel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        this.loadSNPMetaInformation();

    }


    public void loadSNPMetaInformation() {
        JFileChooser fileChooser = new JFileChooser(oldPath);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getName().toLowerCase().endsWith(".snpmeta"))
                    return true;
                if(f.isDirectory())
                    return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "Haplotype Viewer SNP Meta-Information (.snpmeta)";
            }
        });

        int approve = fileChooser.showOpenDialog(null);

        if(approve == JFileChooser.APPROVE_OPTION) {
            final File f = fileChooser.getSelectedFile();
            oldPath = f.getParent();
            if(!f.isDirectory() && f.canRead()) {
                String path = f.getAbsolutePath();

                try {
                    final SNPMetadataParser parser = new SNPMetadataParser(path);

                    AbstractTask readFileTask = new AbstractTask("Loading file: " + f.getName()) {

                        @Override
                        protected void initialize() {}

                        @Override
                        protected void doWork() throws Exception {
                            parser.setDataSet(viewModel.getDataSet());
                            parser.readFile(this);
                            viewModel.fireChanged(new ViewModelEvent(this, ViewModelEvent.SNP_META_INFO_CHANGED, "SNP Meta-Information added - " + f.getName()));
                        }
                    };

                    readFileTask.start();

                    if(readFileTask.isInterrupted()) {
                        System.out.println("Failed");
                    }

                } catch(Exception ex) {
                    ex.printStackTrace();

                    String message = "Meta-Information file cannot be read!";
                    JOptionPane.showMessageDialog(this.viewModel.getOwner(),
                            message,
                            "Import Meta-Information Error",
                            JOptionPane.ERROR_MESSAGE);
                    viewModel.fireChanged(new ViewModelEvent(this, ViewModelEvent.ERROR, message));
                }
            } else {
                String message = "Meta-Information file cannot be read!";
                JOptionPane.showMessageDialog(this.viewModel.getOwner(),
                        message,
                        "Import Meta-Information Error",
                        JOptionPane.ERROR_MESSAGE);
                viewModel.fireChanged(new ViewModelEvent(this,ViewModelEvent.ERROR, message));
            }
        }
    }
}
