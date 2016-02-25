package settings.typed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import settings.HierarchicalSetting;
import settings.events.SettingChangeEvent;
import visualization.utilities.colormaps.ColorMap;
import visualization.utilities.gui.ColorMapEditorPanel;

public class ColorMapSetting extends HierarchicalSetting {
	
	private ColorMap colorMap;
	private JComponent guiComponent;
	private ColorMapEditorPanel cmEditorPanel;
	
	public ColorMapSetting(String title, ColorMap colorMap) {
		super(title);
		this.colorMap = colorMap;
	}

	@Override
	public void settingChanged(SettingChangeEvent e) {
		// nothing to do
	}
	
	public ColorMap getColorMap() {
		return this.colorMap;
	}
	
	@Override
	public JComponent getViewComponent() {
		if(this.guiComponent != null) 
			return this.guiComponent;
		this.guiComponent = new JPanel();
		cmEditorPanel = new ColorMapEditorPanel(this.getColorMap());
		
		this.guiComponent.setLayout(new BorderLayout());
		this.guiComponent.add(cmEditorPanel, BorderLayout.CENTER);
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmEditorPanel.apply();
				fireSettingChanged(new SettingChangeEvent(this, SettingChangeEvent.COLOR_GRADIENT_CHANGED, "Color Gradient Changed for " + getTitle())); 
			}
		});
		
		this.guiComponent.add(applyButton, BorderLayout.SOUTH);
		
		return this.guiComponent;
	}
}
