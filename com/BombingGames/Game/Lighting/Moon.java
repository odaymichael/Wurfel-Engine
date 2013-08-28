package com.BombingGames.Game.Lighting;

import com.BombingGames.EngineCore.Controller;
import com.badlogic.gdx.graphics.Color;

/**
 *The moon is a moving object, like the sun but it should rise at night.
 * @author Benedikt Vogler
 */
public class Moon extends GlobalLightSource{
    /**
     *Create a moon.
     */
    public Moon() {
        super(180-Controller.getMap().getWorldSpinDirection(), 0, new Color(0.4f,0.6f,1f,1), 60);
    }
}
