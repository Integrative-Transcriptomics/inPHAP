/*
 * Created on Feb 4, 2005
 *
 */
package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * @author gehlenbo
 *
 */
public class GUIUtilities
{
	public enum HorizontalAlignment { LEFT, RIGHT, CENTER, NONE }
	public enum VerticalAlignment { TOP, BOTTOM, CENTER, NONE }
	public enum GradientRange { FULL, UPPER, LOWER }

	public static Box createAlignedComponent( JComponent component,
			VerticalAlignment vAlign )
	{
		return ( createAlignedComponent( component, HorizontalAlignment.NONE, vAlign ) );
	}


	public static Box createAlignedComponent( JComponent component,
			HorizontalAlignment hAlign )
	{
		return ( createAlignedComponent( component, hAlign, VerticalAlignment.NONE ) );
	}


	public static Box createAlignedComponent( JComponent component,
			HorizontalAlignment hAlign, VerticalAlignment vAlign )
	{
		Box l_hbox = Box.createHorizontalBox();
		Box l_vbox = Box.createVerticalBox();

		if ( hAlign == HorizontalAlignment.LEFT )
		{
			l_hbox.add( component );
			l_hbox.add( Box.createHorizontalGlue() );      
		}
		else if ( hAlign == HorizontalAlignment.RIGHT )
		{
			l_hbox.add( Box.createHorizontalGlue() );      
			l_hbox.add( component );
		}
		else if ( hAlign == HorizontalAlignment.CENTER )
		{
			l_hbox.add( Box.createHorizontalGlue() );      
			l_hbox.add( component );
			l_hbox.add( Box.createHorizontalGlue() );      
		}
		else
		{
			l_hbox.add( component );
		}

		if ( vAlign == VerticalAlignment.TOP )
		{
			l_vbox.add( l_hbox );
			l_vbox.add( Box.createVerticalGlue() );      
		}
		else if ( vAlign == VerticalAlignment.BOTTOM )
		{
			l_vbox.add( Box.createVerticalGlue() );      
			l_vbox.add( l_hbox );
		}
		else if ( vAlign == VerticalAlignment.CENTER )
		{
			l_vbox.add( Box.createVerticalGlue() );      
			l_vbox.add( l_hbox );
			l_vbox.add( Box.createVerticalGlue() );      
		}
		else
		{
			l_vbox.add( l_hbox );
		}

		return ( l_vbox );
	}


	public static JLabel createCaptionLabel( String caption )
	{
		return ( new JLabel( "<html><b>" + caption + "</b></html>" ) );
	}


	/** produce the traditional Mayday rainbow */
	public static Color[] rainbow( int colors, double saturation )
	{
		int groups = 6;
		
		if ( saturation > 1 )
			saturation = 1;

		if ( saturation < 0 )
			saturation = 0;

		int l_maxComponentValue =(int)(255 * saturation);

		int r = 255-l_maxComponentValue;
		int g = l_maxComponentValue;
		int b = l_maxComponentValue;
		int counter = 0;

		int maxC = groups * (l_maxComponentValue + 1);
		
		Color[] l_rainbow = new Color[maxC];

		
		for ( counter = 0; counter < maxC; ++counter )
		{
			      if ( counter < 1 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r, g, b-- ); // from cyan to green
			      }
			      if ( counter < 2 * l_maxComponentValue && counter >= 1 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r++, g, b ); // from green to yellow 
			      }
			      if ( counter < 3 * l_maxComponentValue && counter >= 2 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r, g--, b ); // from yellow to red
			      }
			      if ( counter < 4 * l_maxComponentValue && counter >= 3 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r, g, b++ ); // from red to magenta
			      }
			      if ( counter < 5 * l_maxComponentValue && counter >= 4 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r--, g, b ); // from magenta to blue
			      }
			      if ( counter < 6 * l_maxComponentValue && counter >= 5 * l_maxComponentValue )
			      {
			        l_rainbow[counter] = new Color( r, g++, b ); // from blue to cyan
			      }
		}

		Color[] l_result = new Color[colors];

		for ( int i = 0; i < colors; ++i )
		{
			double pos = ((double)maxC  / (double)colors ) * i;			
			l_result[i] = l_rainbow[(int)pos];
		}

		return ( l_result ); 
	}
	
	/** a rainbow gradient like the visible light spectrum */
	public static Color[] rainbow2( int colors, double saturation )
	{
		int groups = 5;

		if ( saturation > 1 )
			saturation = 1;

		if ( saturation < 0 )
			saturation = 0;

		int l_maxComponentValue =(int)(255 * saturation);

		int r = 255-l_maxComponentValue;
		int g = 255 - l_maxComponentValue;
		int b = 255-l_maxComponentValue;
		int counter = 0;

		int step=0;

		int maxC = groups * (l_maxComponentValue + 1);
		
		Color[] l_rainbow = new Color[maxC];

		
		for ( counter = 0; counter < maxC; ++counter )
		{
			if (counter % l_maxComponentValue == 0)
				++step;
			
			switch( step ) {
			case 3:
				l_rainbow[counter] = new Color( r, g, b-- ); // from cyan to green
				break;
			case 4:
				l_rainbow[counter] = new Color( r++, g, b ); // from green to yellow 
				break;
			case 5:
				l_rainbow[counter] = new Color( r, g--, b ); // from yellow to red
				break;
			case 6:
				l_rainbow[counter] = new Color( r, g, b++ ); // from red to magenta
				break;
			case 1:
				l_rainbow[counter] = new Color( r, g, b++ ); // from magenta to blue
				break;
			case 2:
				l_rainbow[counter] = new Color( r, g++, b ); // from blue to cyan
				break;
			}


			//    			
			//      if ( counter < 1 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r, g, b-- ); // from cyan to green
			//      }
			//      if ( counter < 2 * l_maxComponentValue && counter >= 1 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r++, g, b ); // from green to yellow 
			//      }
			//      if ( counter < 3 * l_maxComponentValue && counter >= 2 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r, g--, b ); // from yellow to red
			//      }
			//      if ( counter < 4 * l_maxComponentValue && counter >= 3 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r, g, b++ ); // from red to magenta
			//      }
			//      if ( counter < 5 * l_maxComponentValue && counter >= 4 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r--, g, b ); // from magenta to blue
			//      }
			//      if ( counter < 6 * l_maxComponentValue && counter >= 5 * l_maxComponentValue )
			//      {
			//        l_rainbow[counter] = new Color( r, g++, b ); // from blue to cyan
			//      }
		}

		Color[] l_result = new Color[colors];

		for ( int i = 0; i < colors; ++i )
		{
			double pos = ((double)maxC  / (double)colors ) * i;			
			l_result[i] = l_rainbow[(int)pos];
		}

		return ( l_result ); 
	}


	private static Double sum( ArrayList< Double > l_items )
	{
		Double l_sum = 0.0;

		for ( Double i: l_items )
		{
			l_sum += i;
		}

		return ( l_sum );
	}


	public static ArrayList< Color > computeSemiSigmoidColorGradient( Color begin, Color end, int steps, boolean includeBegin, Double steepness )
	{
		ArrayList< Color > l_gradient = new ArrayList< Color >();
		ArrayList< Double > l_scalingFactors = new ArrayList< Double >();
		int l_start;

		if ( includeBegin )
		{
			l_start = 0;
		}
		else
		{
			l_start = 1;
		}

		// compute scaling factor for each step (sum of scaling factors does not equal number of steps!)
		for ( int i = l_start; i < steps + 1; ++i )
		{
			double l_scalingFactor = ((double)steps) / ( 1.0 + Math.exp( ((double)i)/steepness ) ); 

			l_scalingFactors.add( l_scalingFactor  );
		}

		Double l_uncorrectedSum = sum( l_scalingFactors );
		Double l_correctionFactor = ((double)steps)/l_uncorrectedSum;

		// correct scaling factors to make sum of scaling factors equal number to number of steps
		for ( int i = 0; i < l_scalingFactors.size(); ++i )
		{       
			l_scalingFactors.set( i, l_scalingFactors.get( i ) * l_correctionFactor );
		}

		double l_red = (double)(end.getRed() - begin.getRed() )/(double)(steps - 1 + l_start);
		double l_green = (double)(end.getGreen() - begin.getGreen() )/(double)(steps - 1 + l_start);
		double l_blue = (double)(end.getBlue() - begin.getBlue() )/(double)(steps - 1 + l_start);

		double l_currentRed = 0.0;
		double l_currentGreen = 0.0;
		double l_currentBlue = 0.0;

		for ( int i = l_start; i < steps + 1; ++i )
		{
			l_currentRed += l_red * l_scalingFactors.get( i - l_start );
			l_currentGreen += l_green * l_scalingFactors.get( i - l_start );
			l_currentBlue += l_blue * l_scalingFactors.get( i - l_start );

			l_gradient.add( new Color( (int)(begin.getRed() + l_currentRed),
					(int)(begin.getGreen() + l_currentGreen),
					(int)(begin.getBlue() + l_currentBlue) ) );
		}

		return ( l_gradient );
	}


	public static ArrayList< Color > computeLinearColorGradient( Color begin, Color end, int steps, boolean includeBegin )
	{
		ArrayList< Color > l_gradient = new ArrayList< Color >();
		int l_start;    

		if ( includeBegin )
		{
			l_start = 0;
		}
		else
		{
			l_start = 1;
		}

		double l_red = (double)(end.getRed() - begin.getRed() )/(double)(steps - 1 + l_start);
		double l_green = (double)(end.getGreen() - begin.getGreen() )/(double)(steps - 1 + l_start);
		double l_blue = (double)(end.getBlue() - begin.getBlue() )/(double)(steps - 1 + l_start);    


		for ( int i = l_start; i < steps + 1; ++i )
		{
			l_gradient.add( new Color( (int)(begin.getRed() + i*l_red),
					(int)(begin.getGreen() + i*l_green),
					(int)(begin.getBlue() + i*l_blue) ) );
		}

		return ( l_gradient );
	}


	public static ArrayList< Color > reverseColorGradient( ArrayList< Color > gradient )
	{
		Collections.reverse( gradient );

		return ( gradient );
	}


	public static ArrayList< Color > expandColorGradient( ArrayList< Color > gradient, int newSize )
	{
		ArrayList< Color > l_newGradient = new ArrayList< Color >();
		int l_currentSize = gradient.size();

		if ( l_currentSize >= newSize )
		{
			return ( gradient );
		}

		int l_repeatCount = (int)Math.ceil( (double)newSize / (double)l_currentSize ); // how many times each color has to be repeated
		int l_lastRepeatCount = newSize % l_repeatCount; // number of times to repeat the last color

		if ( l_lastRepeatCount == 0 )
		{
			l_lastRepeatCount = l_repeatCount;
		}

		for ( int i = 0; i < l_currentSize; ++i )
		{
			if ( i < l_currentSize - 1 )
			{
				for ( int j = 0; j < l_repeatCount; ++j )
				{
					l_newGradient.add( gradient.get( i ) );
				}
			}
			else
			{
				for ( int j = 0; j < l_lastRepeatCount; ++j )
				{
					l_newGradient.add( gradient.get( i ) );
				}        
			}      
		}

		return ( l_newGradient );
	}


	public static ArrayList< Color > joinColorGradient( ArrayList< Color > gradient1, ArrayList< Color > gradient2 )
	{
		ArrayList< Color > l_newGradient = new ArrayList< Color >();

		l_newGradient.addAll( gradient1 );    
		l_newGradient.addAll( gradient2 );

		return ( l_newGradient );
	}


	public static Color mapValueToColorGradient( double value, ArrayList< Color > gradient, double max, GradientRange range )
	throws RuntimeException
	{
		if ( Math.abs( value ) > Math.abs( max ) )
		{
			throw ( new RuntimeException( "Absolute of given value greater than absolute maximum." ) );
		}

		// determine number of colors in gradient    
		int l_colors = gradient.size(); 

		// determine the mean index and the position of the central color      
		double l_meanIndex;
		int l_center;
		double l_divisor; 

		if ( range != GradientRange.FULL )
		{
			l_meanIndex = 0.0;
			l_center = 0;
			l_divisor = 1.0;
		}
		else
		{
			l_meanIndex = (double)l_colors/2.0;
			l_center = (int)Math.floor( l_meanIndex );
			l_divisor = 2.0;
		}

		// corrective factor
		double l_cf = 0.5 * ( 1.0 - (double)(l_colors % 2) );

		// determine color index
		int l_index;

		if ( value != 0 )
		{
			l_index = (int)((l_meanIndex + ((double)l_colors/l_divisor) * ( value / max ))*
					((Math.floor((double)l_colors/l_divisor) - l_cf)/((double)l_colors/l_divisor)));

			if ( range == GradientRange.LOWER )
			{
				l_index = l_colors + l_index - 1;
			}
		}
		else
		{
			l_index = l_center; 
		}


		return ( gradient.get( l_index ) );
	}  
	
	
	public static JButton makeIconButton(AbstractAction a, int mnemonic, String tooltip, ImageIcon icon) {
		JButton bt = new JButton( a );
        bt.setMnemonic( mnemonic );
        bt.setToolTipText( tooltip );
        if (icon!=null) {
        	bt.setIcon(icon);
        	bt.setText("");
        	bt.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        }
        return bt;
	}
	
}