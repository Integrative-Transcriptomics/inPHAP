package visualization.utilities.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visualization.utilities.ColorGradient;

@SuppressWarnings("serial")
public class GradientPreviewPanel extends JButton {

	ColorGradient gradient;
	protected boolean editable = false; 

	public GradientPreviewPanel(ColorGradient cg) {
		super("X");
		gradient = cg;
		
		FontRenderContext DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext( null, true, false );
		Font DEFAULT_PLOT_SMALL_LEGEND_FONT = new Font( "Lucida Sans", Font.PLAIN, 9);
		
		FontRenderContext l_frc = new FontRenderContext( DEFAULT_FONT_RENDER_CONTEXT.getTransform(), 
				false, DEFAULT_FONT_RENDER_CONTEXT.usesFractionalMetrics() );
		TextLayout sLayout = new TextLayout( "0123456789", DEFAULT_PLOT_SMALL_LEGEND_FONT, l_frc );

		setPreferredSize(new Dimension(100, 
				(int)(16+2+sLayout.getDescent()+sLayout.getAscent())+5
				+getInsets().bottom+getInsets().top));
		setMinimumSize(getPreferredSize());
		setOpaque(true);
		setBackground(Color.white);
		setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (isEditable()) {
					final ColorGradient gradient = getGradient();
					GradientEditorDialog ged = new GradientEditorDialog(null, gradient);
					ged.setModal(true);
					ged.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							setGradient(gradient);				
						}
					});
					ged.setVisible(true);
				}
			}			
		});
	}

	public void setGradient(ColorGradient cg) {
		gradient = cg;
		repaint();
	}
	
	public ColorGradient getGradient() {
		return gradient;
	}
	
	public void setEditable(boolean ed) {
		editable = ed;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	protected int getInnerWidth() {
		return getWidth() - getInsets().left - getInsets().right;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2 = (Graphics2D)g;
		
		AffineTransform originalTransform = g2.getTransform();
		
		Insets i = getInsets();
		g2.translate(i.left, i.top);
		extraManipulations(g2);
		
		drawGradient( g2, getInnerWidth() );

		g.translate(0, 16+2);
		g.setColor(Color.black);
		// draw min, max and center indicators		
		drawExtremumIndicator(g2, gradient.getMin());
		drawExtremumIndicator(g2, gradient.getMax());
		drawExtremumIndicator(g2, gradient.getMidpoint());
		
		g2.setTransform(originalTransform);
		if (editable)
			super.paintBorder(g);
	}
	
	protected void extraManipulations(Graphics2D g2) {
		// nothing here
	}

	protected void drawGradient(Graphics2D g2, int width ) {

		int colorCount = gradient.getResolution();
		double oneColorWidth = ((double)width)/((double)colorCount);
		
		Rectangle2D.Double r2d = new Rectangle2D.Double( 0, 0, Math.max(oneColorWidth,1.0), 16);
		
		for ( int i = 0; i!=colorCount; ++i ) {
			g2.setColor( gradient.getColor(i) );			
			r2d.x = i*oneColorWidth;
			g2.fill(r2d);
		}
		
	}
	
	protected void drawExtremumIndicator(Graphics2D g, double value) {
		
		double positionInGradient = gradient.mapValueToPercentage(value);
		float realPosition = (float)(getInnerWidth() * positionInGradient);
		
		drawExtremumIndicator(g, realPosition, 0f);
		
		AffineTransform at = g.getTransform();
		
		g.translate(0,5);
		String sValue = new DecimalFormat( "###,##0.00" ).format( value );

		FontRenderContext DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext( null, true, false );
		Font DEFAULT_PLOT_SMALL_LEGEND_FONT = new Font( "Lucida Sans", Font.PLAIN, 9);
		
		FontRenderContext l_frc = new FontRenderContext( DEFAULT_FONT_RENDER_CONTEXT.getTransform(), 
				false, DEFAULT_FONT_RENDER_CONTEXT.usesFractionalMetrics() );

		TextLayout sLayout = new TextLayout( sValue, DEFAULT_PLOT_SMALL_LEGEND_FONT, l_frc );
		
		if (realPosition + sLayout.getAdvance() > getInnerWidth() + getInsets().left)
			realPosition -= sLayout.getAdvance();
		
		g.translate(realPosition, sLayout.getAscent());
		sLayout.draw( g, 0, 0 );
		g.setTransform(at);
	}

	protected void drawExtremumIndicator( Graphics2D graphics, float tipX, float tipY ) {
		GeneralPath l_path = new GeneralPath( GeneralPath.WIND_NON_ZERO );

		// create outline path (a triangle pointing downwards)
		l_path.moveTo( 3f, 0f );
		l_path.lineTo( 6f, 3f );
		l_path.lineTo( 0f, 3f );
		l_path.lineTo( 3f, 0f );

		tipX -= 3;

		// move outline path to desired location
		graphics.translate( tipX, tipY );

		// draw triangle
		graphics.draw( l_path );

		// re-translate graphics device
		graphics.translate( -tipX, -tipY );
	}
	
}
