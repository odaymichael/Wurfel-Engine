package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
  
/**
 *The pointer can point to a self aware block or to a fixed coordinate.
 * @author Benedikt
 */
public class Pointer {
    private int[] coords;
    
    /**
     * Create a pointer to field in the coordinate system.
     * @param coords 
     */
    public Pointer(int[] coords) {
        this.coords = coords;
    }

    /**
     * Returns the Block the pointer is pointing to.
     * @return
     */
    public Block getBlock() {
        return Controller.getMapDataSafe(coords);
    }
    
    /**
     * Set a block in the Map at the field pointing to.
     * @param block the block you want to set.
     */
    public void setBlock(Block block) {
        Controller.getMap().setData(coords, block);
    }

   /**
    * get the coordinates the pointer is pointing too
    * @return 
    */
    public int[] getCoords() {
        return coords;
    }
    
    /**
     * Set the coords.
     * @param coords
     * @see 
     */
    public void setCoords(int[] coords) {
        this.coords = coords;
    } 
}
