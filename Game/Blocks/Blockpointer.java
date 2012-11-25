/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Blocks;

import Game.Controller;


  
/**
 *
 * @author Benedikt
 */
public class Blockpointer{
    private int x;
    private int y;
    private int z;    
    
    /**
     * 
     * @param X
     * @param Y
     * @param Z
     */
    public Blockpointer(int X, int Y, int Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

    /**
     * 
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * 
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
        return y;
    }

    /**
     * 
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
        return z;
    }

    /**
     * 
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
        return Controller.map.getData(x, y, z);
    }
}
