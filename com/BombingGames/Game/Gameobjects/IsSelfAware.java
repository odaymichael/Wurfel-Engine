package com.BombingGames.Game.Gameobjects;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public interface IsSelfAware{
   /**
     * Returns the AbsCoords
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
      * 
      * @return
      */
     public int[] getRelCoords();
}