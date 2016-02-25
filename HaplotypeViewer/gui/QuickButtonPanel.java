package gui;

import events.ViewModelEvent;
import gui.actions.AggregateAction;
import gui.actions.ChangeSNPGradientAction;
import gui.actions.ChangeSubjectGradientAction;
import gui.actions.DeaggregateAction;
import gui.actions.ExportAction;
import gui.actions.FilterAction;
import gui.actions.HelpAction;
import gui.actions.NewAction;

import java.awt.Image;
import java.awt.Window;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;

public class QuickButtonPanel extends JToolBar implements ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6201068523808761896L;
	
	private static final int imgSize = 20;
	
	private ViewModel viewModel;

    private JButton newfile;
    private JButton export;
    private JButton aggregate;
    private JButton deaggregate;
    private JButton filtering;
    private JButton changeSNPmetacol;
    private JButton changeSubjectMetacol;
    private JButton helpme;
	
	public QuickButtonPanel(Window owner, ViewModel viewModel) {
        super();
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);

        try {
            newfile = new JButton(new NewAction(viewModel, "New"));
            newfile.setIcon(getImageIcon("libraries/icons/new-file.png"));

            export  = new JButton(new ExportAction(viewModel,"Export", false));
            export.setIcon(getImageIcon("libraries/icons/export.png"));

            aggregate = new JButton(new AggregateAction(viewModel,"Aggregate Rows"));
            aggregate.setIcon(getImageIcon("libraries/icons/aggregate.png"));

            deaggregate = new JButton(new DeaggregateAction(viewModel,"Deaggregate Rows"));
            deaggregate.setIcon(getImageIcon("libraries/icons/deaggregate.png"));

            filtering = new JButton(new FilterAction(viewModel,"Filtering"));
            filtering.setIcon(getImageIcon("libraries/icons/filter.png"));

            changeSNPmetacol = new JButton(new ChangeSNPGradientAction(viewModel, "Change SNP MI Color"));
            changeSNPmetacol.setIcon(getImageIcon("libraries/icons/changecolorgradient.png"));

            changeSubjectMetacol = new JButton(new ChangeSubjectGradientAction(viewModel, "Change Subject MI Color"));
            changeSubjectMetacol.setIcon(getImageIcon("libraries/icons/changecolorgradient.png"));

            helpme = new JButton(new HelpAction(viewModel,"Help"));
            helpme.setIcon(getImageIcon("libraries/icons/changecolorgradient.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Add everything
        
        this.add(newfile);
        this.add(export);
        this.add(aggregate);
        this.add(deaggregate);
        this.add(filtering);
        this.add(changeSNPmetacol);
        this.add(changeSubjectMetacol);
        this.add(helpme);
        
        this.setVisible(true);
	}
	
	private ImageIcon getImageIcon(String resource) throws IOException {
		Image img = ImageIO.read(getClass().getClassLoader().getResource(resource));
        Image resizedImg = img.getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {

	}

}