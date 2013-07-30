package com.BombingGames.Game.Lighting;

import com.BombingGames.Game.Map;
import org.newdawn.slick.Color;

/**
 *The moon is a moving object, like the sun but it should rise at night.
 * @author Benedikt Vogler
 */
public class Moon extends GlobalLightSource{
    /**
     *Create a moon.
     */
    public Moon() {
        super(-Map.getWorldSpinDirection(), 180, new Color(0.8f,0.8f,1f), 90);
    }
    
    
    
    /**
     *
     * @param delta
     */
    @Override
    public void update(int delta){
        super.update(delta);
        //setColor(new Color(127 + (int) (getBrightness() * 128), 255, 255));
        
    }
}
