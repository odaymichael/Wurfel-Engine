/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Lighting;

import com.BombingGames.Game.Controller;
import org.newdawn.slick.Color;

/**
 *
 * @author Benedikt Vogler
 */
public class Moon {
    private final int maxAngle = 70;//the max possible angle (from horizon) the sun can has
    private final float LONGSPEED = 1/32f;
    private float latPos;
    private float longPos = 225;//starting position
    private Color color = new Color(0.8f,0.8f,1f); //the color of the light
    private float brightness = 1;//should raise at morning and fall at night but stay over teh day
    
    public void update(int delta){
        longPos += LONGSPEED*delta;
        latPos = (float) (90 - maxAngle*Math.sin((longPos+Controller.getMap().getWorldSpinDirection())*Math.PI/180));
        
        if (latPos >= 360) latPos=0;
        if (longPos >= 360) longPos=0;
        
        //sunLat = container.getInput().getMouseY()*360/container.getHeight();
        //sunLong = container.getInput().getMouseX()*360/container.getWidth();
        brightness = (-latPos+110)/((maxAngle+20)/2);
        if (brightness > 1)brightness=1;
        if (brightness < 0)brightness=0;
        brightness *=0.3f;
                
        //I_a = (int) ((90-latPos)*0.1f);
        
    }

    public Color getColor() {
        return color;
    }

    public float getLatPos() {
        return latPos;
    }

    public float getLongPos() {
        return longPos;
    }

    public float getLongSpeed() {
        return LONGSPEED;
    }

    public int getMaxAngle() {
        return maxAngle;
    }

    public float getBrightness() {
        return brightness;
    }
    
    
    
}
