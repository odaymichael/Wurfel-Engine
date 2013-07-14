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
    public static int waveAmplitude = 30;
    private static float currentwave;
    private static float wavespeed = 1/500f;
    
    private float startvalue;
    
    Coordinate coords;
        
    public Water(int id, Coordinate coords) {
        super(id);
        setTransparent(true);
        
        if (coords == null) throw new NullPointerException("No coordinates given to ExplosiveBarrel during creation."); 
        
        this.coords = coords;
        startvalue = (float) (coords.getCellOffset()[2] + Math.random()*waveAmplitude - waveAmplitude);
       
    }

    @Override
    public Coordinate getCoords() {
        return coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    @Override
    public void update(int delta) {
        coords.setCellOffsetZ(startvalue + (float) (Math.sin(currentwave)*waveAmplitude));
    }
    
    public static void staticUpdate(int delta){
        currentwave += delta*wavespeed;
    }
    
    
    
}
