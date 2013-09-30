package com.BombingGames.EngineCore.LightEngine;

import com.badlogic.gdx.graphics.Color;

/**
* Algorithm by Rich Franzen, 22 July 1999
*mailto:rich@r0k.us
*(c) 1999, Rich Franzen
 * @author Rich Franzen, Benedikt Vogler
 */
public class PseudoGrey {
    /**
     * @param grey
     * @return 
     */
    public static Color toColor(float grey) {
	int i4k, boost, r, g, b;

	i4k = (int)(grey * 4095);
	if (i4k >= 4095)
	{   // only one case of "infinity",
	    // but the >= also protects against over-bounding (r,g,b)
	    r = 255;
	    g = 255;
	    b = 255;
	}
	  else if (i4k >= 0)
	  { // normal case
	    if (i4k < 0xfe0)
	    {
		r = g = b = i4k >> 4;
		boost = i4k & 0x00f;	// 16 possibilities
	    }
	      else
	      { // accelerate rate near "infinity"
		r = g = b = 0xfe;	// base level of 254
		boost  = i4k & 0x01f;	// 32 possibilities
		boost /= 2;		// back down to 16
	      }

	    if (boost >= 14)
	    {
		r += 1;
		g += 1;
	    }
	      else if (boost >= 11)
	      {
		g += 1;
		b += 1;
	      }
	      else if (boost >= 9)
		g += 1;
	      else if (boost >= 7)
	      {
		r += 1;
		b += 1;
	      }
	      else if (boost >= 5)
		r += 1;
	      else if (boost >= 2)
		b += 1;
	  }
	  else
	  { // protect against error condition of negative grey
	    r = 0;
	    g = 0;
	    b = 0;
	  }
	return new Color(r/256f, g/256f, b/256f,1);
    }

    /**
     *
     * @param c
     * @return
     */
    public static float toFloat(Color c) {
	int	red, green, blue;
	int	cMin, cMax;
	int	base12;
	float	grey;

	red   = (int) (c.r*255);
	green = (int) (c.g*255);
	blue  = (int) (c.b*255);
	cMin  = Math.min(red, Math.min(green, blue));
	cMax  = Math.max(red, Math.max(green, blue));

	if (cMin == 255)
	    grey = 1.0f;
	  else if (cMax == cMin)
	  { // short-cut this common case
	    base12 = cMin << 4;
	    grey = base12 / 4095.0f;
	  }
	  else if ((cMax - cMin) < 2)
	  { // pseudoGrey
	    int	delta = 0;			// luma weights:
	    if (cMax == blue)   delta += 2;	// .114 * 16 == 1.824
	    if (cMax == red)    delta += 5;	// .299 * 16 == 4.784
	    if (cMax == green)  delta += 9;	// .587 * 16 == 9.392
	    if (cMin == 254)    delta *= 2;  // accelerate near "infinity"
	    base12 = (cMin << 4) + delta;
	    grey = base12 / 4095.0f;
	  }
	  else
	  { // use luma conversion
	    grey  = (.299f*red + .587f*green + .114f*blue) / 255.0f;
	  }

	return grey;
    }
}