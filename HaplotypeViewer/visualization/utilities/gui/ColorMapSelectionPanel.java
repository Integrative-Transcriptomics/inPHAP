package visualization.utilities.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.colormaps.AccentColorMap;
import visualization.utilities.colormaps.ColorMap;
import visualization.utilities.colormaps.Dark2ColorMap;
import visualization.utilities.colormaps.PairedColorMap;
import events.EventFirer;
import gui.layout.ExcellentBoxLayout;

public class ColorMapSelectionPanel extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9118815441811623463L;

	private ColorMap selected;
	
	private List<ColorMap> colorMaps = new LinkedList<ColorMap>();
	
	protected EventFirer<ChangeEvent, ChangeListener> eventfirer = new EventFirer<ChangeEvent, ChangeListener>() {
		protected void dispatchEvent(ChangeEvent event, ChangeListener listener) {
			listener.stateChanged(event);
		}		
	};
	
	public ColorMapSelectionPanel() {
		this.setLayout(new ExcellentBoxLayout(true, 5));
		this.initColorMaps();
	}
	
	private void initColorMaps() {
		//TODO add more color maps if more are implemented
		this.addColorMap(new AccentColorMap());
		this.addColorMap(new Dark2ColorMap());
		this.addColorMap(new PairedColorMap());
		this.selected = colorMaps.get(2);
		selected.getPreviewPanel().setBorder(BorderFactory.createLineBorder(Color.RED));
	}
	
	public void addColorMap(ColorMap colorMap) {
		JPanel previewPanel = colorMap.getPreviewPanel();
		previewPanel.addMouseListener(this);
		this.add(previewPanel);
		this.colorMaps.add(colorMap);
	}
	
	public void addChangeListener(ChangeListener listener) {
		eventfirer.addListener(listener);
	}
	
	public ColorMap getColorMap() {
		return this.selected;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		PreviewPanel panel = (PreviewPanel)e.getSource();
		ColorMap clicked = panel.getColorMap();
		
		if(clicked != selected) {
			selected.getPreviewPanel().setBorder(BorderFactory.createEmptyBorder());
			selected = clicked;
			selected.getPreviewPanel().setBorder(BorderFactory.createLineBorder(Color.RED));
			this.eventfirer.fireEvent(new ChangeEvent(selected));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
