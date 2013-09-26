package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Map.Coordinate;

/**
 *An object that knows his own position IsSelfAware.
 * @author Benedikt
 */
public interface IsSelfAware{
   /**
     * Return the coordinates of the SelfAware object.
     * @return the coordinates where the object is located
     */
    public Coordinate getCoords();
    
    /**
     * Set the coordinates without safety check.
     * @param coords the coordiantes you want to set
     */
    public void setCoords(Coordinate coords);
}