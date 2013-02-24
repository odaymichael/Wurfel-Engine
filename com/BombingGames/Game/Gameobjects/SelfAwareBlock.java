package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameplay;
import com.BombingGames.Game.Map;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public class SelfAwareBlock extends Block{
   private int[] absCoords = new int[3];
    
    protected SelfAwareBlock() {
    }
   
    protected SelfAwareBlock(int x,int y, int z) {
        setAbsCoords(x,y,z);
    }
    
   

  /**
     * Sets the absolute X and relative X coord.
     * @param X 
     */
    public void setAbsCoordX(int X){
        absCoords[0] = X;
    }
    

    /**
     * Sets the absolute Y and relative Y coord.
     * @param Y
     */
    public void setAbsCoordY(int Y){
        absCoords[1] = Y;
    } 
    
   /**
      *  CoordZ is always absolute and relative at the same time because there are no chunks in z direction.
      * @param z the new value for z
      */
    public void setCoordZ(int z) {
        //if Z is too high set to highes possible position
        if (z > Map.getBlocksZ()-2)
            absCoords[2] = Map.getBlocksZ() -2;
        else absCoords[2] = z;
    }
   
    /**
     * Set the absolute Coordinates
     * @param x
     * @param y
     * @param z
     */
    public final void setAbsCoords(int x, int y, int z){
        absCoords[0] = x;
        absCoords[1] = y;
        setCoordZ(z);
    }
   
    public int[] getCoords(){
        return new int[]{getCoordX(), getCoordY(), absCoords[2]};
    }
    
     /**
      * 
      * @return
      */
     public int getCoordX() {
        return absCoords[0] - Controller.getMap().getChunkCoords(4)[0]  * Chunk.getBlocksX();
    }

    /**
     * 
     * @return
     */
    public int getCoordY() {
        return absCoords[1] - Controller.getMap().getChunkCoords(4)[1] * Chunk.getBlocksY();
    }
    
    /**
    * 
    * @return
    */
   public int getAbsCoordX() {
        return absCoords[0];
    }

   /**
    * 
    * @return
    */
   public int getAbsCoordY() {
        return absCoords[1];
    }

    /**
     *  CoordZ is always absolute and relative at the same time because there are no chunks in z direction.
     * @return
     */
    public int getCoordZ() {
        return absCoords[2];
    }

    /**
     * Destroys the reference in the map.
     */
    protected void selfDestroy(){
        Controller.getMap().setData(getCoordX(), getCoordY(), absCoords[2], Block.create());
    }
    
    /**
     * Put the reference to this object at the coordinates inside the map
     */
    protected void selfRebuild(){
        Controller.getMap().setData(getCoordX(), getCoordY(), absCoords[2], this);
        Gameplay.getView().getCamera().traceRayTo(getCoordX(), getCoordY(), getCoordZ(), false);
    }
    
    
    /**
     * Get the neighbour block to a side
     * @param side the id of the side
     * @return the neighbour block
     */
    public Block getNeighbourBlock(int side){
        int neighbourcoords[] = Block.sideNumbToNeighbourCoords(new int[]{getCoordX(), getCoordY(), absCoords[2]}, side);
        return Controller.getMapDataSafe(neighbourcoords[0], neighbourcoords[1], neighbourcoords[2]);
    }
}