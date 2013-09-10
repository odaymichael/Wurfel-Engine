package com.BombingGames.Game.Lighting;

import com.BombingGames.EngineCore.Controller;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author Benedikt Vogler
 */
public class GlobalLightSource {
    /**
     *The higher the value the faster/shorter is the day.
     */
    protected final float LONGSPEED = 1/128f;
    private Color brightness;
    private Color tone; //the color of the light
    private float latPos;
    private float longPos;
    private int amplitude; //the max possible angle (from horizon) the sun can has

    /**
     * A GlobalLightSource can be the moon, the sun or even something new.
     * @param longPos The starting position.
     * @param latPos The starting position.
     * @param tone the starting color of the light
     * @param amplitude the maximum degree during a day the LightSource rises
     */
    public GlobalLightSource(float longPos, float latPos, Color tone, int amplitude) {
        this.longPos = longPos;
        this.latPos = latPos;
        this.tone = tone;
        this.amplitude = amplitude;
    }

    /**
     * The light color has an intensity which can be get by this function. It adds every component and then divides by three.
     * @return a value between 0 and 1
     */
//    public float getBrightness() {
//        return (brightness.r + brightness.g +brightness.b)/3;
//    }
    
    public float getBrightness() {
	int	red, green, blue;
	int	cMin, cMax;
	int	base12;
	float	grey;

	red   = (int) (brightness.r*255);
	green = (int) (brightness.g*255);
	blue  = (int) (brightness.b*255);
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

    /**
     *
     * @return
     */
    public Color getTone() {
        return tone;
    }

    /**
     *
     * @return
     */
    public float getLatPos() {
        return latPos;
    }

    /**
     *
     * @return
     */
    public float getLongPos() {
        return longPos;
    }

    /**
     *
     * @return
     */
    public float getLongSpeed() {
        return LONGSPEED;
    }

    /**
     *
     * @return
     */
    public int getMaxAngle() {
        return amplitude;
    }

    /**
     *
     * @param latPos
     */
    public void setLatPos(float latPos) {
        this.latPos = latPos;
        if (this.latPos >= 360) {
            this.latPos -= 360;
        } else if (this.latPos < 0) this.latPos += 360; 
    }

    /**
     *
     * @param longPos
     */
    public void setLongPos(float longPos) {
        this.longPos = longPos;
        if (this.longPos >= 360) {
            this.longPos -= 360;
        }else if (this.longPos < 0) this.longPos += 360;
    }

    /**
     *
     * @param tone
     */
    public void setTone(Color tone) {
        this.tone = tone;
    }
    
    

    /**
     *
     * @param delta
     */
    public void update(int delta) {
        float numBrightness = 0; //should raise at morning and fall at night but stay over teh day
            
        //automove
        if (LONGSPEED != 0) {
            longPos += LONGSPEED * delta;
            latPos = (float) (amplitude * Math.sin((longPos + Controller.getMap().getWorldSpinDirection()) * Math.PI / 180));
        }
            
        //brightness calculation
        if (latPos > amplitude/2 && latPos < 180)
                numBrightness = 1;//day
        else if (latPos < -amplitude/2)
                 numBrightness = 0;//night
            else if (latPos < amplitude/2) 
                    numBrightness = (float) (0.5f + 0.5f*Math.sin(latPos * Math.PI/amplitude)); //morning   & evening
        

        if (numBrightness > 1) numBrightness=1;
        if (numBrightness < 0) numBrightness=0;
        
        //value to color;
        brightness = getPseudoGrey(numBrightness);
        
        
        //if (longPos>180+IGLPrototype.TWISTDIRECTION)
        //color = new Color(127 + (int) (brightness * 128), 255, 255);
        //else color = new Color(1f,1f,1f);
        //I_a = (int) ((90-latPos)*0.1f);
    }

    
    public Color getLight() {
        return getTone().cpy().mul(getBrightness());
    }
    
    /**
     * Algorithm by Rich Franzen, 22 July 1999
     *mailto:rfranzen@my-deja.com
     *(c) 1999, Rich Franzen
     * @param grey
     * @return 
     */
    public static Color getPseudoGrey(float grey) {
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
}
