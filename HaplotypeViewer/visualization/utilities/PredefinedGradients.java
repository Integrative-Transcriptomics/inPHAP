package visualization.utilities;

import java.awt.Color;

/**
 * @author symons
 * Defines constants for handling predefined color gradients. 
 */
public enum PredefinedGradients {
	
	//Sequential
	COLOR_BREWER_BUGN (new Color(229,245,249), new Color(153,216,201), new Color(44,162,95), "ColorBrewer - BuGn"),
	COLOR_BREWER_BUPU (new Color(224,236,244), new Color(158,188,218), new Color(136,86,167), "ColorBrewer - BuPu"),
	COLOR_BREWER_GNBU (new Color(224,243,219), new Color(168,221,181), new Color(67,162,202), "ColorBrewer - GnBu"),
	COLOR_BREWER_ORRD (new Color(254,232,200), new Color(253,187,132), new Color(227,74,51), "ColorBrewer - OrRd"),
	COLOR_BREWER_PUBU (new Color(236,231,242), new Color(166,189,219), new Color(43,140,190), "ColorBrewer - PuBu"),
	COLOR_BREWER_PUBUGN (new Color(236,226,240), new Color(166,189,219), new Color(28,144,153), "ColorBrewer - PuBuGn"),
	COLOR_BREWER_PURD (new Color(231,225,239), new Color(201,148,199), new Color(221,28,119), "ColorBrewer - PuRd"),
	COLOR_BREWER_RDPU (new Color(253,224,221), new Color(250,159,181), new Color(197,27,138), "ColorBrewer - RdPu"),
	COLOR_BREWER_YLGN (new Color(247,252,185), new Color(173,221,142), new Color(49,163,84), "ColorBrewer - YlGn"),
	COLOR_BREWER_YLGNBU (new Color(237,248,177), new Color(127,205,187), new Color(44,127,184), "ColorBrewer - YlGnBu"),
	COLOR_BREWER_YLORBR (new Color(255,247,188), new Color(254,196,79), new Color(217,95,14), "ColorBrewer - YlOrBr"),
	COLOR_BREWER_YLORRD (new Color(255,237,160), new Color(254,178,76), new Color(240,59,32), "ColorBrewer - YlOrRd"),
	COLOR_BREWER_BLUES (new Color(222,235,247), new Color(158,202,225), new Color(49,130,189), "ColorBrewer - Blues"),
	COLOR_BREWER_GREENS (new Color(229,245,224), new Color(161,217,224), new Color(49,163,84), "ColorBrewer - Greens"),
	COLOR_BREWER_GREYS (new Color(240,240,240), new Color(189,189,189), new Color(99,99,99), "ColorBrewer - Greys"),
	COLOR_BREWER_ORANGES (new Color(254,230,206), new Color(253,174,107), new Color(230,85,13), "ColorBrewer - Oranges"),
	COLOR_BREWER_PURPLES (new Color(239,237,245), new Color(188,189,220), new Color(117,107,177), "ColorBrewer - Purples"),
	COLOR_BREWER_REDS (new Color(254,224,210), new Color(252,146,114), new Color(222,45,38), "ColorBrewer - Reds"),
	
	//Diverging
	COLOR_BREWER_BRBG (new Color(216,179,101), new Color(245,245,245), new Color(90,180,172), "ColorBrewer - BrBG"),
	COLOR_BREWER_PIYG (new Color(233,163,201), new Color(247,247,247), new Color(161,215,106), "ColorBrewer - PiYG"),
	COLOR_BREWER_PRGN (new Color(175,141,195), new Color(247,247,247), new Color(127,191,123), "ColorBrewer - PRGn"),
	COLOR_BREWER_PUOR (new Color(241,163,64), new Color(247,247,247), new Color(153,142,195), "ColorBrewer - PuOr"),
	COLOR_BREWER_RDBU (new Color(239,138,98), new Color(247,247,247), new Color(103,169,207), "ColorBrewer - RdBu"),
	COLOR_BREWER_RDGY (new Color(239,138,98), new Color(255,255,255), new Color(153,153,153), "ColorBrewer - RdGy"),
	COLOR_BREWER_RDYLBU (new Color(252,141,89), new Color(255,255,191), new Color(145,191,219), "ColorBrewer - RdYlBu"),
	COLOR_BREWER_RDYLGN (new Color(252,141,89), new Color(255,255,191), new Color(145,207,96), "ColorBrewer - RdYlGn"),
	
	//Qualitative
	COLOR_BREWER_ACCENT (new Color(127,201,127), new Color(190,174,212), new Color(253,192,134), "ColorBrewer - Accent"),
	COLOR_BREWER_DARK2 (new Color(27,158,119), new Color(217,95,2), new Color(117,112,179), "ColorBrewer - Dark2"),
	COLOR_BREWER_PAIRED (new Color(166,206,227), new Color(31,120,180), new Color(178,223,138), "ColorBrewer - Paired"),
	COLOR_BREWER_PASTEL1 (new Color(251,180,174), new Color(179,205,227), new Color(204,235,197), "ColorBrewer - Pastel1"),
	COLOR_BREWER_PASTEL2 (new Color(179,226,205), new Color(253,205,172), new Color(203,213,232), "ColorBrewer - Pastel2"),
	COLOR_BREWER_SET1 (new Color(228,26,28), new Color(55,126,184), new Color(77,175,74), "ColorBrewer - Set1"),
	COLOR_BREWER_SET2 (new Color(102,194,165), new Color(252,141,98), new Color(141,160,203), "ColorBrewer - Set2"),
	COLOR_BREWER_SET3 (new Color(141,211,199), new Color(255,255,179), new Color(190,186,218), "ColorBrewer - Set3"),
	
	GREEN_BLACK_RED_GRADIENT (Color.green, Color.black, Color.red, "Green - Black - Red"),
	RED_BLACK_GREEN_GRADIENT (Color.red, Color.black, Color.green, "Red - Black - Green"),

	BLUE_WHITE_RED_GRADIENT (Color.blue, Color.white, Color.red, "Blue - White - Red"),
	RED_WHITE_BLUE_GRADIENT (Color.RED, Color.white, Color.BLUE, "Red - White - Blue"),

	HEAT_COLOR_GRADIENT (Color.red, Color.yellow, Color. white, "Heat colors (Red - Yellow - White)"),
	INVERSE_HEAT_COLOR_GRADIENT (Color.white, Color.yellow, Color.red, "Inverse Heat colors (White - Yellow - Red)"),

	BLUE_YELLOW_RED_GRADIENT (Color.blue, Color.yellow, Color. red, "Blue - Yellow - Red"),
	RED_YELLOW_BLUE_GRADIENT (Color.red, Color.yellow, Color. blue, "Red - Yellow - Blue"),


	WHITE_RED_GRADIENT (Color.white, null, Color.red, "White - Red"),
	RED_WHITE_GRADIENT (Color.red, null, Color.white, "Red - White"),

	WHITE_BLUE_GRADIENT (Color.white, null, Color.blue,"White - Blue"),
	BLUE_WHITE (Color.blue, null, Color.white,"Blue - White"),

	BLUE_RED_GRADIENT (Color.blue, null, Color.red,"Blue - Red"),
	RED_BLUE_GRADIENT (Color.red, null, Color.blue,"Red - Blue"),

	// grayscale
	GRAYSCALE_GRADIENT (Color.white, null, Color.black, "Grayscale (White - Black)"),
	GRAYSCALE_INVERSE_GRADIENT (Color.black, null, Color.white, "Grayscale (Black - White)"),

	// black body 
	BLACK_BODY_RADIATION (Color.black, Color.red, Color.white, "Black Body Radiation (Black - Red - White"),
	INVERSE_BLACK_BODY_RADIATION (Color.white, Color.red, Color.black, "Inverse Black Body Radiation (White - Red - Black");
	
	private final Color lower;
	private final Color mid;
	private final Color upper;
	private final String name;


	private PredefinedGradients(Color l, Color m, Color u, String n) {
		lower=l;
		mid=m;
		upper=u;
		name=n;
	}

	public Color getLower() {
		return lower;
	}

	public Color getMid() {
		return mid;
	}

	public Color getUpper() {
		return upper;
	}

	public String toString() {
		return name;
	}

}
