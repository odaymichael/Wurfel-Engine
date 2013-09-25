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
    protected final float AZIMUTH_SPEED = 1/64f;
    private float power;
    private Color tone; //the color of the light
    private float height;
    private float azimuth;
    private int amplitude; //the max possible angle (from horizon) the sun can has

    /**
     * A GlobalLightSource can be the moon, the sun or even something new.
     * @param azimuth The starting position.
     * @param height The starting position.
     * @param color the starting color of the light. With this parameter you controll its brightness.
     * @param amplitudeHeight the maximum degree during a day the LightSource rises
     */
    public GlobalLightSource(float azimuth, float height, Color color, int amplitudeHeight) {
        this.azimuth = azimuth;
        this.height = height;
        this.tone = color;
        this.amplitude = amplitudeHeight;
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
    public float getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public float getAzimuth() {
        return azimuth;
    }

    /**
     *
     * @return
     */
    public float getAzimuthSpeed() {
        return AZIMUTH_SPEED;
    }

    /**
     *
     * @return
     */
    public int getMaxAngle() {
        return amplitude;
    }

    /**
     *The Latitude posiiton. 
     * @param height
     */
    public void setHeight(float height) {
        if (height >= 360) {
            this.height = height % 360;
        } else if (this.height < 0) this.height += 360; 
    }

    /**
     *The longitudinal position
     * @param azimuth
     */
    public void setAzimuth(float azimuth) {
        if (azimuth >= 360) {
            this.azimuth = azimuth % 360;
        } else if (this.azimuth < 0) this.azimuth += 360;
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
    public void update(float delta) {    
        //automove
        if (AZIMUTH_SPEED != 0) {
            azimuth += AZIMUTH_SPEED * delta;
            height = (float) (amplitude * Math.sin((azimuth + Controller.getMap().getWorldSpinDirection()) * Math.PI / 180));
        }
            
        //brightness calculation
        if (height > amplitude/2 && height < 180)
                power = 1;//day
        else if (height < -amplitude/2)
                 power = 0;//night
            else if (height < amplitude/2) 
                    power = (float) (0.5f + 0.5f*Math.sin(height * Math.PI/amplitude)); //morning   & evening
        

        if (power > 1) power=1;
        if (power < 0) power=0;    
        
        //if (azimuth>180+IGLPrototype.TWISTDIRECTION)
        //color = new Color(127 + (int) (power * 128), 255, 255);
        //else color = new Color(1f,1f,1f);
        //I_a = (int) ((90-height)*0.1f);
    }

    /**
     * Returns the light the GLS emits.
     * @return 
     */
    public Color getLight() {
        return getTone().cpy().mul(power);
    }
}
