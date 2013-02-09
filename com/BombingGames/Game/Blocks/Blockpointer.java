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
     * @param x 
     * @param y 
     * @param z 
     */
    public Blockpointer(int x, int y, int z) {
        //here the coordinates are values which are written/read
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Create a blockpointer who points at a SelfAwareBlock and follows him
     * @param block the block where you are pointing at.
     * @param x
     * @param y 
     * @param z  
     */
    public Blockpointer(SelfAwareBlock block, int x, int y, int z){
        this.block = block;
        //here the coordinates have the function to add it
        this.x = x;
        this.y = y;  
        this.z = z;
    }

    /**
     * 
     * @return
     */
    public int getX() {
        if (block != null)
            return block.getCoordX()+x;
        else return x;
    }

    /**
     * Set the summand (pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param X
     */
    public void setX(int X) {
        this.x = X;
    }

    /**
     * 
     * @return
     */
    public int getY() {
        if (block != null)
            return block.getCoordY()+y;
        else return y;
    }

    /**
     * Set the summand (pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param Y
     */
    public void setY(int Y) {
        this.y = Y;
    }

    /**
     * 
     * @return
     */
    public int getZ() {
        if (block != null)
            return block.getCoordZ()+z;
        else return z;
    }

    /**
     * Set the summand (pointer to SelfAwareBlock) or the value (pointer to coordinate triple)
     * @param Z
     */
    public void setZ(int Z) {
        this.z = Z;
    }
    
    /**
     * Returns the Block the pointer is pointing to.
     * @return
     */
    public Block getBlock() {
        return Controller.getMapDataSafe(getX(), getY(), getZ());
    }
    
    /**
     * Set a block at the reference
     * @param block 
     */
    public void setBlock(Block block) {
        Controller.getMap().setData(getX(), getY(), getZ(), block);
    }
}
