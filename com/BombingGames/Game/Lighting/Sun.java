package com.BombingGames.Game.Lighting;

import com.BombingGames.Game.Controller;
import org.newdawn.slick.Color;

/**
 * The sun circles the world. The sun is only virtual. You can never see it inisometric perspective.
 * @author Benedikt Vogler
 */
public class Sun {
    private final int maxAngle = 70;//the max possible angle (from horizon) the sun can has
    private final float LONGSPEED = 1/64f;
    private float latPos;
    private float longPos = 0;//longitudinal starting position
    private Color color = new Color(255,255,255); //the color of the light
    private float brightness;//should raise at morning and fall at night but stay over teh day

    /**
     *
     */
    public Sun() {
        longPos = -Controller.getMap().getWorldSpinDirection();
    }
    
    
    /**
     *
     * @param delta
     */
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
               
         //if (longPos>180+IGLPrototype.TWISTDIRECTION)
        color = new Color(127+(int) (brightness*128), 255, 255);
        //else color = new Color(1f,1f,1f); 
        
        //I_a = (int) ((90-latPos)*0.1f);
        
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
