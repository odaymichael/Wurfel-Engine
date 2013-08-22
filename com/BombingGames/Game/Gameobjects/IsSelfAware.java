package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Coordinate;

/**
 *An object that knows his own position IsSelfAware.
 * @author Benedikt
 */
public interface IsSelfAware{
   /**
     * Return the coordinates.
     * @return
     */
    public Coordinate getCoords();
    
    /**
     * Set the coordinates without safety check.
     * @param coords 
     */
    public void setCoords(Coordinate coords);
}