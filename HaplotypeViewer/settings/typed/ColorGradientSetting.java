package settings.typed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import visualization.utilities.ColorGradient;
import visualization.utilities.gui.GradientEditorPanel;

public class ColorGradientSetting extends HierarchicalSetting {

	private ColorGradient colorGradient;
	private JComponent guiComponent;
	
	public ColorGradientSetting(String title, ColorGradient colorGradient) {
		super(title);
		this.colorGradient = colorGradient;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// TODO
	}

	public ColorGradient getColorGradient() {
		return this.colorGradient;
	}
	
	private GradientEditorPanel gep;
	
	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null) 
			return this.guiComponent;
		this.guiComponent = new JPanel();
		gep = new GradientEditorPanel(this.getColorGradient());
		
		this.guiComponent.setLayout(new BorderLayout());
		this.guiComponent.add(gep, BorderLayout.CENTER);
		
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gep.apply();
				fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.COLOR_GRADIENT_CHANGED, "Color Gradient Changed for " + getTitle())); 
			}
		});
		
		this.guiComponent.add(applyButton, BorderLayout.SOUTH);
		
		return this.guiComponent;
	}
}
