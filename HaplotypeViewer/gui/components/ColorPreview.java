package gui.components;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is an implementation of a preview panel for the color
 * associated with a probe list.
 * 
 * @author Nils Gehlenborg
 * @version 0.1
 */
@SuppressWarnings("serial")
public class ColorPreview
extends JButton 
{
	private Color color;
	private boolean filled;
	protected boolean editable=false;

	public ColorPreview() {
		this( false );
	}
	
	public ColorPreview( boolean filled ) {
		this(Color.BLACK, filled);
	}
	
	public ColorPreview( Color color )	{
		this( color, false );
	}

	public ColorPreview( Color color, boolean filled )	{
		super("X");
		this.setAction(new ChangeColorAction());

		this.color = color;
		this.filled = filled;
//		setBorder(BorderFactory.createLoweredBevelBorder());
		
	}

	public void setParentComponent(Component c) {
		
	}

	public void setColor( Color color )	{
		this.color = color;
		this.setForeground( this.color );
		repaint();
	}

	public Color getColor()	{
		return ( this.color );
	}


	/**
	 * Hides the original <code>paint</code> routine from <code>Panel</code>. This implementation
	 * draws horizontal line from the left end to the right end of the panel, using the
	 * color associated with the probe list currently manipulated in the 
	 * <code>ProbeListPropertyDialog</code>. 
	 * 
	 * @param graphics This graphics context is used for drawing.
	 */
	public void paint( Graphics graphics )
	{
//		super.paint(graphics);
		Graphics2D l_g2d = (Graphics2D)graphics;

		l_g2d.setBackground( this.getBackground() );
		l_g2d.clearRect( this.getX(), this.getY(), this.getWidth(), this.getHeight() );

		l_g2d.setColor( this.color );

		if ( !filled )
		{
			l_g2d.draw( new Line2D.Double( 0, getHeight()/2, getWidth(), getHeight()/2 ) );      
		}
		else
		{
			l_g2d.fill( new Rectangle2D.Double( 0, 0, this.getWidth(), this.getHeight() ) );      
		}
		super.paintBorder(graphics);
	}


	/**
	 * Ensures that the panel will be completely redrawn when ever it is necessary. Hides
	 * the original <code>update</code> routine from <code>Panel</code>.
	 * 
	 * @param graphics This graphics context is used for drawing.
	 */
	public void update( Graphics graphics )
	{
		paint( graphics );
	}
	
	protected class ChangeColorAction extends AbstractAction {
		
		
		public ChangeColorAction() {
			super(" ");
		}
		
		public void actionPerformed(ActionEvent e) {
			if (isEditable()) {
				Component parent = ColorPreview.this;
				while (parent.getParent()!=null)
					parent = parent.getParent();
				//TODO
//				if (!(parent instanceof Window))
//					parent = Mayday.sharedInstance;
				Color lcolor = JColorChooser.showDialog( parent, "Choose Color", getColor() );
				if ( lcolor != null && !lcolor.equals(getColor())) {
					setColor(lcolor);
					fireStateChanged2();
					repaint();
				}
			}
		}
	}
	
	
	protected void fireStateChanged2() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                ChangeEvent c = new ColorChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(c);
            }          
        }
    }   
	
	public static class ColorChangeEvent extends ChangeEvent {
		public ColorChangeEvent(Object source) {
			super(source);
		}
	}


	public void setEditable(boolean ed) {
		editable=ed;
	}

	public boolean isEditable() {
		return editable;
	}

}
