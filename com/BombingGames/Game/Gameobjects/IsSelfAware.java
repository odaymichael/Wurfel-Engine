package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Coordinate;

/**
 *An object that knows his own position IsSelfAware.
 * @author Benedikt
 */
public interface IsSelfAware{
   /**
     * Return the absolute coordinates.
     * @return
     */
    public Coordinate getCoords();
    
    /**
     * Set the absolute Coordinates without safety check.
     * @param absCoords the new coordinates.
     */
    public void setCoords(Coordinate coords);
    
}