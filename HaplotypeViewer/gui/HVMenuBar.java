package gui;

import events.ViewModelEvent;
import gui.actions.AboutAction;
import gui.actions.AggregateAction;
import gui.actions.ChangeSNPGradientAction;
import gui.actions.ChangeSubjectGradientAction;
import gui.actions.DeaggregateAction;
import gui.actions.ExportAction;
import gui.actions.FilterAction;
import gui.actions.HelpAction;
import gui.actions.JumpToColumnSelectionAction;
import gui.actions.JumpToRowSelectionAction;
import gui.actions.JumpToSNVAction;
import gui.actions.JumpToSubjectAction;
import gui.actions.NewAction;
import gui.actions.RemoveAllFiltersAction;
import gui.actions.RemoveMetaColumnAction;
import gui.actions.RemoveMetaRowAction;
import gui.actions.SNPMetaLoaderAction;
import gui.actions.SubjectMetaLoaderAction;
import gui.actions.SystemInfoAction;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import viewmodel.ViewModel;
import viewmodel.ViewModelListener;

public class HVMenuBar extends JMenuBar implements ActionListener, ViewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166799248533383700L;

	protected JMenu fileMenu;
	protected JMenu editMenu;
	protected JMenu helpMenu;
	
	private NewAction newAction;
    private ExportAction exportAction;
    private ExportAction exportVisibleAction;
    private AggregateAction aggregateAction;
    private DeaggregateAction deaggregateAction;
    private ChangeSubjectGradientAction changeSubjectGradientAction;
    private ChangeSNPGradientAction changeSNPGradientAction;
    private FilterAction filterAction;
    private HelpAction helpAction;
    private SNPMetaLoaderAction snpmetaloader;
    private SubjectMetaLoaderAction subjectMetaLoaderAction;
    private AboutAction aboutAction;
    private SystemInfoAction systemInfoAction;
    
    private RemoveMetaColumnAction removeMetaColumnAction;
    private RemoveMetaRowAction removeMetaRowAction;
    
    private RemoveAllFiltersAction removeAllFiltersAction;
	
	private ViewModel viewModel;
	
	public HVMenuBar(Window owner, ViewModel viewModel) {
		super();
		this.viewModel = viewModel;
		this.viewModel.addViewModelListener(this);
		
		this.newAction = new NewAction(viewModel, "New");
        this.exportAction = new ExportAction(viewModel, "Export", false);
        this.exportVisibleAction = new ExportAction(viewModel, "Export Visible Plot Area", true);
        this.aggregateAction = new AggregateAction(viewModel, "Aggregate");
        this.deaggregateAction = new DeaggregateAction(viewModel, "Deaggregate");
        this.changeSubjectGradientAction = new ChangeSubjectGradientAction(viewModel, "Change Subject MI Color");
        this.changeSNPGradientAction = new ChangeSNPGradientAction(viewModel, "Change SNP MI Color");
        this.filterAction = new FilterAction(viewModel, "Apply Filter");
        this.helpAction = new HelpAction(viewModel, "Help");
        this.snpmetaloader = new SNPMetaLoaderAction(viewModel, "Import SNP Meta-Information");
        this.subjectMetaLoaderAction = new SubjectMetaLoaderAction(viewModel, "Import Subject Meta-Information");
        this.aboutAction = new AboutAction(viewModel, "About");
        this.systemInfoAction = new SystemInfoAction(viewModel, "System Information");
        
        this.removeMetaColumnAction = new RemoveMetaColumnAction(viewModel);
        this.removeMetaRowAction = new RemoveMetaRowAction(viewModel);
        
        this.removeAllFiltersAction = new RemoveAllFiltersAction(viewModel);
		
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		helpMenu = new JMenu("Help");
		
		add(fileMenu);
		add(editMenu);
		add(helpMenu);
		
		JMenu metaInformationMenu = new JMenu("Meta-Information");
		this.editMenu.add(metaInformationMenu);
		
		JMenuItem changeMetaRowGradientsItem = new JMenuItem(changeSNPGradientAction);
		JMenuItem changeMetaColGradientsItem = new JMenuItem(changeSubjectGradientAction);
		JMenuItem removeMetaInfoColItem = new JMenuItem(removeMetaColumnAction);
		JMenuItem removeMetaInfoRowItem = new JMenuItem(removeMetaRowAction);
		
		metaInformationMenu.add(changeMetaRowGradientsItem);
		metaInformationMenu.add(changeMetaColGradientsItem);
		metaInformationMenu.add(removeMetaInfoColItem);
		metaInformationMenu.add(removeMetaInfoRowItem);
		
		JMenu selectionMenu = new JMenu("Selection");
		this.editMenu.add(selectionMenu);
		
		JMenuItem clearRowSelectionItem = new JMenuItem("Clear Row Selection");
		clearRowSelectionItem.addActionListener(this);
		selectionMenu.add(clearRowSelectionItem);
		
		JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
		clearColumnSelectionItem.addActionListener(this);
		selectionMenu.add(clearColumnSelectionItem);
		
		JMenu filterMenu = new JMenu("Filtering");
		JMenuItem filterItem = new JMenuItem(filterAction);
		filterMenu.add(filterItem);
		
		JMenuItem removeFiltersItem = new JMenuItem(removeAllFiltersAction);
		filterMenu.add(removeFiltersItem);
		
		this.editMenu.add(filterMenu);
		
		JMenu aggregationMenu = new JMenu("Aggregation");
		this.editMenu.add(aggregationMenu);
		
		JMenuItem aggregateRowsItem = new JMenuItem(aggregateAction);
		aggregateRowsItem.addActionListener(this);
		aggregationMenu.add(aggregateRowsItem);
		
		JMenuItem deAggregateRowsItem = new JMenuItem(deaggregateAction);
		deAggregateRowsItem.addActionListener(this);
		aggregationMenu.add(deAggregateRowsItem);
		
		//File Menu
		JMenuItem newItem = new JMenuItem(newAction);
		newItem.addActionListener(this);

		JMenu exportMenu = new JMenu("Export");
		
		JMenuItem exportPlotItem = new JMenuItem(exportAction);
		JMenuItem exportVisiblePlotItem = new JMenuItem(exportVisibleAction);
		
		exportMenu.add(exportPlotItem);
		exportMenu.add(exportVisiblePlotItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		
		JMenu importMenu = new JMenu("Import");
		JMenuItem importSubjectMetaInfoItem = new JMenuItem(subjectMetaLoaderAction);
	//	importSubjectMetaInfoItem.addActionListener(this);
		importMenu.add(importSubjectMetaInfoItem);
		
		JMenuItem importSNPMetaInfoItem = new JMenuItem(snpmetaloader);
	//	importSNPMetaInfoItem.addActionListener(this);
		importMenu.add(importSNPMetaInfoItem);
		
		//Help Menu
		JMenuItem logItem = new JMenuItem("Log");
		logItem.addActionListener(this);
		JMenuItem helpItem = new JMenuItem(helpAction);
		helpItem.addActionListener(this);
		JMenuItem aboutItem = new JMenuItem(aboutAction);
		aboutItem.addActionListener(this);
		JMenuItem systemInfoItem = new JMenuItem(this.systemInfoAction);
		
		//Jump to menu
		JMenu jumpToMenu = new JMenu("Jump To ...");
		this.editMenu.add(jumpToMenu);
		
		JMenuItem jumpToRowSelection = new JMenuItem(new JumpToRowSelectionAction(viewModel, "Jump to selected row"));
		JMenuItem jumpToColumnSelection = new JMenuItem(new JumpToColumnSelectionAction(viewModel, "Jump to selected column"));
		JMenuItem jumpToSNVSetting = new JMenuItem(new JumpToSNVAction(viewModel, "Jump to SNV"));
		JMenuItem jumpToSubjectSetting = new JMenuItem(new JumpToSubjectAction(viewModel, "Jump to Subject"));
		
		jumpToMenu.add(jumpToRowSelection);
		jumpToMenu.add(jumpToColumnSelection);
		jumpToMenu.add(jumpToSubjectSetting);
		jumpToMenu.add(jumpToSNVSetting);
		
		fileMenu.add(newItem);
		fileMenu.add(importMenu);
		fileMenu.add(exportMenu);
		fileMenu.add(exitItem);
		
		helpMenu.add(logItem);
		helpMenu.add(systemInfoItem);
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)e.getSource();
		
		switch(source.getText()) {
		case "Log":
			this.viewModel.showLog();
			break;
		case "Exit":
			this.exit();
			break;
		case "Clear Row Selection":
			viewModel.clearRowSelection();
			break;
		case "Clear Column Selection":
			viewModel.clearColumnSelection();
			break;
		default:
			System.out.println("No function for this button");
		}
	}


	private void exit() {
		int option = JOptionPane.showConfirmDialog(null,
                "Do you really want to close the program?",
                "Exit",
                JOptionPane.YES_NO_OPTION);
		
		if(option == JOptionPane.YES_OPTION) {
			System.out.println("Bye, bye :-(");
			System.exit(0);
		}
	}

	@Override
	public void viewModelChanged(ViewModelEvent e) {
		int change = e.getChange();
		switch(change) {
		//TODO
		}
	}
}
