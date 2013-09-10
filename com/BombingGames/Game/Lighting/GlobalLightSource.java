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
    protected final float LONGSPEED = 1/64f;
    private float power;
    private Color tone; //the color of the light
    private float latPos;
    private float longPos;
    private int amplitude; //the max possible angle (from horizon) the sun can has

    /**
     * A GlobalLightSource can be the moon, the sun or even something new.
     * @param longPos The starting position.
     * @param latPos The starting position.
     * @param color the starting color of the light. With this parameter you controll its brightness.
     * @param amplitude the maximum degree during a day the LightSource rises
     */
    public GlobalLightSource(float longPos, float latPos, Color color, int amplitude) {
        this.longPos = longPos;
        this.latPos = latPos;
        this.tone = color;
        this.amplitude = amplitude;
    }

    /**
     * A light source shines can shine brighter and darker. This amplitude is called power. What it real emits says the brightness.
     * @return a value between 0 and 1
     */
    public float getPower() {
        return power;
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
        //automove
        if (LONGSPEED != 0) {
            longPos += LONGSPEED * delta;
            latPos = (float) (amplitude * Math.sin((longPos + Controller.getMap().getWorldSpinDirection()) * Math.PI / 180));
        }
            
        //brightness calculation
        if (latPos > amplitude/2 && latPos < 180)
                power = 1;//day
        else if (latPos < -amplitude/2)
                 power = 0;//night
            else if (latPos < amplitude/2) 
                    power = (float) (0.5f + 0.5f*Math.sin(latPos * Math.PI/amplitude)); //morning   & evening
        

        if (power > 1) power=1;
        if (power < 0) power=0;    
        
        //if (longPos>180+IGLPrototype.TWISTDIRECTION)
        //color = new Color(127 + (int) (power * 128), 255, 255);
        //else color = new Color(1f,1f,1f);
        //I_a = (int) ((90-latPos)*0.1f);
    }

    
    public Color getLight() {
        return getTone().cpy().mul(power);
    }
}
