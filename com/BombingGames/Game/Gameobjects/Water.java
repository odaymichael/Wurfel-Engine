/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Coordinate;

/**
 *
 * @author Benedikt Vogler
 */
public class Water extends Block implements IsSelfAware{
    public final int waveAmplitude = 40;
    Coordinate coords;
        
    public Water(int id, Coordinate coords) {
        super(id);
        setTransparent(true);
        
        if (coords == null) throw new NullPointerException("No coordinates given to ExplosiveBarrel during creation."); 
        
        coords.setHeight((float) (coords.getHeight() + Math.random()*waveAmplitude - waveAmplitude), true);
        this.coords = coords;
       
    }

    @Override
    public Coordinate getCoords() {
        return coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }
    
    
    
}
