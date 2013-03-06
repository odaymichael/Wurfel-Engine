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
     * Increases/decreases the coordinates by the parameters
     * @param x the value added to x-coordiante
     * @param y y-coordiante
     * @param z z-coordiante
     */
    public void addToAbsCoords(int x, int y, int z);
     /**
      * Returns the relative coordinates wich can be found on the map.
      * @return relative coordinates
      */
     public int[] getRelCoords();
}