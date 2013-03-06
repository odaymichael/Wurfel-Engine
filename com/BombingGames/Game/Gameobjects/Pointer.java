package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
  
/**
 *The pointer can point to a self aware block or to a fixed coordinate.
 * @author Benedikt
 */
public class Pointer{
    private int[] vector;
    private IsSelfAware block = null;
    
    /**
     * Create a pointer to field in the coordinate system.
     * @param coords 
     */
    public Pointer(int[] coords) {
        this.vector = coords;
    }
    
    /**
     * Create a blockpointer who points at a IsSelfAware block and follows it.
     * You can add a coordinate offset.
     * @param block the block where you are pointing at.
     * @param offsetvector  the offset added to the coordinates
     */
    public Pointer(IsSelfAware block, int[] offsetvector){
        this.block = block;
        //in this case the coordinates have the function to add to the coordinates
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
     * Set a block in the Map at the field pointing to.
     * @param block the block you want to set.
     */
    public void setBlock(Block block) {
        Controller.getMap().setData(vector, block);
    }

   /**
    * get the coordinates the pointer is pointing too
    * @return 
    */
    public int[] getCoords() {
        if (block != null ) return vector;
        else {
            int[] coords = block.getRelCoords();
            return new int[]{coords[0]+vector[0], coords[1]+vector[1], coords[2]+vector[2]};
        }
    }

    /**
     * Set the offset vector.
     * @param vector
     */
    public void setOffsetVector(int[] vector) {
        this.vector = vector;
    }
    
    /**
     * The same as setOffsetVector.
     * @param coords
     * @see 
     */
    public void setCoords(int[] coords) {
        this.vector = coords;
    } 
}
