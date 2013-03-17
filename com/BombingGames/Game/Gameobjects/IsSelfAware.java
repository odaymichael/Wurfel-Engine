package com.BombingGames.Game.Gameobjects;

/**
 *An object that knows his own position IsSelfAware.
 * @author Benedikt
 */
public interface IsSelfAware{
   /**
     * Return the absolute coordinates.
     * @return
     */
    public int[] getAbsCoords();
    
    /**
     * Set the absolute Coordinates without safety check.
     * @param coords the new coordinates.
     */
    public void setAbsCoords(int[] coords);
   
    /**
     * Does a vector addition to the coordinates.
     * @param coords the vector which is added to teh coords
     */
    public void addVector(int[] coords);
     /**
      * Returns the relative coordinates wich can be found on the map.
      * @return relative coordinates
      */
     public int[] getRelCoords();
}