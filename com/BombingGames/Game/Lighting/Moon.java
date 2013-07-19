/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Lighting;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import org.newdawn.slick.Color;

/**
 *The moon is a moving object, like the sun but it should rise at night.
 * @author Benedikt Vogler
 */
public class Moon {
    private final int maxAngle = 70;//the max possible angle (from horizon) the sun can has
    private final float LONGSPEED = 1/64f;
    private float latPos;
    private float longPos = 0;//longitudinal starting position
    private Color color = new Color(0.8f,0.8f,1f); //the color of the light
    private float brightness = 1;//should raise at morning and fall at night but stay over teh day

    /**
     *Create a moon.
     */
    public Moon() {
        longPos = 180-Map.getWorldSpinDirection();
    }
    
    
    
    /**
     *
     * @param delta
     */
    public void update(int delta){
        longPos += LONGSPEED*delta;
        latPos = (float) (90 - maxAngle*Math.sin((longPos+Map.getWorldSpinDirection())*Math.PI/180));
        
        if (latPos >= 360) latPos=0;
        if (longPos >= 360) longPos=0;
        
        //sunLat = container.getInput().getMouseY()*360/container.getHeight();
        //sunLong = container.getInput().getMouseX()*360/container.getWidth();
        brightness = (-latPos+110)/((maxAngle+20)/2);
        if (brightness > 1) brightness=1;
        if (brightness < 0) brightness=0;
        brightness *=0.6f;
                
        //I_a = (int) ((90-latPos)*0.1f);
        
    }

    /**
     *The  color of the moon's light.
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     *The latitudinal position is a degree in the latitudinal direction where the moon is located. Value 0-360.
     * @return
     */
    public float getLatPos() {
        return latPos;
    }

    /**
     *The longitudinal position is a degree in the longitudinal direction where the moon is located. Value 0-360.
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
     *The highest possible angle the moon can rise.
     * @return
     */
    public int getMaxAngle() {
        return maxAngle;
    }

    /**
     *
     * @return
     */
    public float getBrightness() {
        return brightness;
    }
    
    
    
}
