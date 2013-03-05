package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
  
/**
 *The pointer can point to a SelfAwareBlock or to a fixed coordinate.
 * @author Benedikt
 */
public class Blockpointer{
    private int[] vector;
    private SelfAwareBlock block;
    
    /**
     * Create a pointer to field in the coordinate system.
     * @param x the coordinates of the block you are pointing to
     * @param y the coordinates of the block you are pointing to
     * @param z the coordinates of the block you are pointing to
     */
    public Blockpointer(int[] coords) {
        this.vector = coords;
    }
    
    /**
     * Create a blockpointer who points at a SelfAwareBlock and follows him.
     * You can add a coordinate offset.
     * @param block the block where you are pointing at.
     * @param x the amount of x added to the coordiantes of the block
     * @param y the amount of x added to the coordiantes of the block
     * @param z the amount of x added to the coordiantes of the block 
     */
    public Blockpointer(SelfAwareBlock block, int[] offsetvector){
        this.block = block;
        //here the coordinates have the function to add it
        this.vector = offsetvector;
    }

    /**
     * Returns the Block the pointer is pointing to.
     * @return
     */
    public Block getBlock() {
        return Controller.getMapDataSafe(vector);
    }
    
    /**
     * Set a block at the reference.
     * @param block 
     */
    public void setBlock(Block block) {
        Controller.getMap().setData(vector, block);
    }

   /**
    * get the coordinates the pointer is pointing too
    * @return 
    */
    public int[] getCoords() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }
    
    /**
     * The same as setVector.
     * @param coords
     * @see 
     */
    public void setCoords(int[] coords) {
        this.vector = coords;
    } 
}
