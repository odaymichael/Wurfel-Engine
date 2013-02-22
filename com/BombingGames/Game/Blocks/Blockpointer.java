package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Controller;
  
/**
 *The pointer can point to a SelfAwareBlock or to a fixed coordinate.
 * @author Benedikt
 */
public class Blockpointer{
    private int x;
    private int y;
    private int z;
    private SelfAwareBlock block;
    
    /**
     * Create a pointer to field in the coordinate system.
     * @param x the coordinates of the block you are pointing to
     * @param y the coordinates of the block you are pointing to
     * @param z the coordinates of the block you are pointing to
     */
    public Blockpointer(int x, int y, int z) {
        //here the coordinates are values which are written/read
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Create a blockpointer who points at a SelfAwareBlock and follows him. You can add a coordainte offset.
     * @param block the block where you are pointing at.
     * @param x the amount of x added to the coordiantes of the block
     * @param y the amount of x added to the coordiantes of the block
     * @param z the amount of x added to the coordiantes of the block 
     */
    public Blockpointer(SelfAwareBlock block, int x, int y, int z){
        this.block = block;
        //here the coordinates have the function to add it
        this.x = x;
        this.y = y;  
        this.z = z;
    }

    /**
     * Returns the coorinates of the block.
     * @return
     */
    public int getCoordX() {
        if (block != null)
            return block.getCoordX()+x;
        else return x;
    }

    /**
     * Set the x-coordainte-offset (when pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param X
     */
    public void setCoordX(int X) {
        this.x = X;
    }

    /**
     * 
     * @return
     */
    public int getCoordY() {
        if (block != null)
            return block.getCoordY()+y;
        else return y;
    }

    /**
     * Set the y-coordainte-offset (pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param Y
     */
    public void setCoordY(int Y) {
        this.y = Y;
    }

    /**
     * Returns the coorinates of the block.
     * @return
     */
    public int getCoordZ() {
        if (block != null)
            return block.getCoordZ()+z;
        else return z;
    }

    /**
     * Set the z-coordainte-offset (when pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param Z
     */
    public void setCoordZ(int Z) {
        this.z = Z;
    }
    
    /**
     * Returns the Block the pointer is pointing to.
     * @return
     */
    public Block getBlock() {
        return Controller.getMapDataSafe(getCoordX(), getCoordY(), getCoordZ());
    }
    
    /**
     * Set a block at the reference.
     * @param block 
     */
    public void setBlock(Block block) {
        Controller.getMap().setData(getCoordX(), getCoordY(), getCoordZ(), block);
    }
}
