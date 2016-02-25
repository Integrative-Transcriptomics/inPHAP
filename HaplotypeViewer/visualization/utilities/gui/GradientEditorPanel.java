package visualization.utilities.gui;

import gui.layout.ExcellentBoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import visualization.utilities.ColorGradient;
import visualization.utilities.gui.setuppers.SetupAgent;
import visualization.utilities.gui.setuppers.SetupExtraOptions;
import visualization.utilities.gui.setuppers.SetupMidpoint;
import visualization.utilities.gui.setuppers.SetupPreview;
import visualization.utilities.gui.setuppers.SetupResolution;

@SuppressWarnings("serial")
public class GradientEditorPanel extends JPanel {
	
	protected ColorGradient base;
	protected ColorGradient state;
	
	protected LinkedList<GradientSetupComponent> setupComponents = new LinkedList<GradientSetupComponent>(); 
	
	public GradientEditorPanel(ColorGradient base) {
		super(new ExcellentBoxLayout(true, 5));
		this.base = base;
		state = new ColorGradient(base);
		init();
	}
	
	protected void addSetupComponent(final GradientSetupComponent gsc) {		
		gsc.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gsc.modifyGradient(state);
				for (GradientSetupComponent sc : setupComponents)
					if (sc!=gsc)
						sc.updateFromGradient(state, false);
			}
		});
		setupComponents.add(gsc);
		add(gsc.getJComponent());
	}
	
	protected void init() {

		addSetupComponent(new SetupResolution());
		addSetupComponent(new SetupAgent());
		addSetupComponent(new SetupExtraOptions());
		addSetupComponent(new SetupMidpoint());
		addSetupComponent(new SetupPreview());
		
		setGradient(base);
		
	}
	
	public void setGradient(ColorGradient base) {
		state = new ColorGradient(base);
		for (GradientSetupComponent sc : setupComponents)
			sc.updateFromGradient(state, true);
	}

	public void apply() {		
		base.copySettings(state);
	}


}
