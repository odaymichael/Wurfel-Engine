package com.BombingGames.Game.Lighting;

import com.BombingGames.Game.Map;
import org.newdawn.slick.Color;

/**
 *
 * @author Benedikt Vogler
 */
public class GlobalLightSource {
    /**
     *The higher the value the faster/shorter is the day.
     */
    protected final float LONGSPEED = 1/128f;
    private float brightness; //should raise at morning and fall at night but stay over teh day
    private Color color; //the color of the light
    private float latPos;
    private float longPos;
    private int amplitude; //the max possible angle (from horizon) the sun can has

    /**
     * A GlobalLightSource can be the moon, the sun or even something new.
     * @param longPos The starting position.
     * @param latPos The starting position.
     * @param color the starting color of the light
     * @param amplitude the maximum degree during a day the LightSource rises
     */
    public GlobalLightSource(float longPos, float latPos, Color color, int amplitude) {
        this.longPos = longPos;
        this.latPos = latPos;
        this.color = color;
        this.amplitude = amplitude;
    }

    /**
     *
     * @return a value between 0 and 1
     */
    public float getBrightness() {
        return brightness*getPower();
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
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
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    

    /**
     *
     * @param delta
     */
    public void update(int delta) {
        //automove
        if (LONGSPEED != 0) {
            longPos += LONGSPEED * delta;
            latPos = (float) (amplitude * Math.sin((longPos + Map.getWorldSpinDirection()) * Math.PI / 180));
        }
            
        //brightness calculation
        if (latPos > amplitude/2 && latPos < 180)
                brightness = 1;//day
        else if (latPos < -amplitude/2)
                 brightness = 0;//night
            else if (latPos < amplitude/2) 
                    brightness = (float) (0.5f + 0.5f*Math.sin(latPos * Math.PI/amplitude)); //morning   & evening
        

        if (brightness > 1) brightness=1;
        if (brightness < 0) brightness=0;
        
        //if (longPos>180+IGLPrototype.TWISTDIRECTION)
        //color = new Color(127 + (int) (brightness * 128), 255, 255);
        //else color = new Color(1f,1f,1f);
        //I_a = (int) ((90-latPos)*0.1f);
    }


    /**
     * The light color has an intensity which can be get by this function. It adds every component and then divides by three.
     * @return 
     */
    public float getPower() {
        return (color.r + color.g +color.b)/3;
    }  
}
