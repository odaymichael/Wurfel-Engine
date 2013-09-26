package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Map.Map;

/**
 *A Sea Block which has a "waves" effect.
 * @author Benedikt Vogler
 */
public class Sea extends Block implements IsSelfAware{
    /**
     *
     */
    public static int waveAmplitude = 50;
    private static float wavespeed = 1/600f; //the smaller the slower
    private static float currentX = 0;
    private int wavesize = Map.getBlocksX()/7;
    
    private float startvalue;
    
    Coordinate coords;
        
    /**
     *
     * @param id
     * @param coords
     */
    public Sea(int id, Coordinate coords) {
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
    public void update(float delta) {
        coords.setCellOffsetZ(
            startvalue +
            (float) (Math.sin(
                (currentX-coords.getRelX()-coords.getRelY())
                * Math.PI/wavesize
                )*waveAmplitude
            )
        );
    }
    
    /**
     *
     * @param delta
     */
    public static void staticUpdate(float delta){
        currentX += delta*wavespeed;
    }
    
    
    
}
