package com.BombingGames.Game.Gameobjects;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public interface ISelfAware{
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
   
    public void addToAbsCoords(int x, int y, int z);
     /**
      * 
      * @return
      */
     public int[] getRelCoords();
}