package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameplay;
import org.newdawn.slick.util.Log;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public abstract class SelfAwareBlock extends Block{
  /**
    * CoordZ is always absolute and relative at the same time.
    */
   public int coordZ;
   
   private int absCoordX;

   private int absCoordY;   
   private int relCoordX;
   private int relCoordY;
    
    SelfAwareBlock(){
        super();
    }
    
   SelfAwareBlock(int id){
       super(id);
   }
   
   SelfAwareBlock(int id,int value){
       super(id, value);
   }
   
   /**
    * 
    * @return
    */
   public int getAbsCoordX() {
        return absCoordX;
    }

   /**
    * 
    * @return
    */
   public int getAbsCoordY() {
        return absCoordY;
    }

    /**
     * 
     * @return
     */
    public int getCoordZ() {
        return coordZ;
    }

     /**
      * 
      * @param coordZ
      */
     public void setCoordZ(int coordZ) {
        this.coordZ = coordZ;
    }

     /**
      * 
      * @return
      */
     public int getRelCoordX() {
        return relCoordX;
    }

     /**
     * Set the relative X Coordinate.
     * @param X
     */
    public void setRelCoordX(int X){
        if (X < Chunk.BLOCKS_X*3){
            relCoordX = X;
        } else {
            this.relCoordX = 3*Chunk.BLOCKS_X-1;
            Gameplay.MSGSYSTEM.add("RelativeCoordX ist too high:"+X);
            Log.warn("RelativeCoordX ist too high:"+X);
        }
        
        if (X >= 0) {
            relCoordX = X;
        } else {
            relCoordX = 0;
            Gameplay.MSGSYSTEM.add("RelativeCoordX ist too low:"+X);
            Log.warn("RelativeCoordX ist too low:"+X);
        }
    }

    /**
     * 
     * @return
     */
    public int getRelCoordY() {
        return relCoordY;
    }

  /**
     * Set the relative Y Coordinate
     * @param Y
     */
    public void setRelCoordY(int Y){
        if (Y < Chunk.BLOCKS_Y*3){
            relCoordY = Y;
        }else {
            relCoordY = 3*Chunk.BLOCKS_Y-1;
            Gameplay.MSGSYSTEM.add("RelativeCoordY ist too high: "+Y);
            Log.warn("RelativeCoordY ist too high: "+Y);
        }
        
        if (Y >= 0) {
            relCoordY = Y;
        }else {
            relCoordY = 0;
            Gameplay.MSGSYSTEM.add("RelativeCoordY ist too low: "+Y);
            Log.warn("RelativeCoordY ist too low: "+Y);
        }
    }
    
     /**
     * Sets the absolute X and relative X coord.
     * @param X 
     */
    public void setAbsCoordX(int X){
        absCoordX = X;
        setRelCoordX(X - Controller.getMap().getCoordlistX(4)  * Chunk.BLOCKS_X);
    }
    

    /**
     * 
     * @param Y
     */
    public void setAbsCoordY(int Y){
        absCoordY = Y;
        //da das Map Coordinatensystem in y-Richtung in zwei unterschiedliche Richtungen geht (hier "+" ???)
        setRelCoordY(Y + Controller.getMap().getCoordlistY(4) *Chunk.BLOCKS_Y);
    }    
   
    /**
     * 
     * @param X
     * @param Y
     * @param Z
     */
    public void setAbsCoords(int X, int Y, int Z){
        setAbsCoordX(X);
        setAbsCoordY(Y);
        coordZ = Z;
        //if Z is too high set to highes possible position
        if (coordZ > Chunk.BLOCKS_Z-2) coordZ = Chunk.BLOCKS_Z -2;
    }
    
    //how can you make that this methods are only avilable to extending classes?
    /**
     * 
     */
    protected void selfDestroy(){
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ, new Block(0,0));
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ+1, new Block(0,0));
    }
    
    /**
     * 
     */
    protected void selfRebuild(){
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ, this);
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ+1, new Block(40,1));
    }
    
   /**
     * Returns a block next to it
     * 701
     * 682
     * 543
     * @param side Clockwise starting from top
     * @param relZ if you want to check another layer. relZ is added to coordZ.
     * @return The neighbour block
     */
    public Block getNeighbourBlock(int side, int relZ){
       int z = coordZ+relZ;
        switch(side){
            case 0:
                return Controller.getMapData(getRelCoordX(),getRelCoordY()-2, z);
            case 1:
                return Controller.getMapData(getRelCoordX() -(getRelCoordY() % 2 == 1 ? 1 : 0), getRelCoordY()-1, z);
            case 2:
                return Controller.getMapData(getRelCoordX()+1, getRelCoordY(), z);
            case 3:
                return Controller.getMapData(getRelCoordX() +(getRelCoordY() % 2 == 1 ? 1 : 0), getRelCoordY()+1, z);
            case 4:
                return Controller.getMapData(getRelCoordX(), getRelCoordY()+2, z);
            case 5:
                return Controller.getMapData(getRelCoordX() -(getRelCoordY() % 2 == 0 ? 1 : 0), getRelCoordY()+1, z);
            case 6:
                return Controller.getMapData(getRelCoordX()-1, getRelCoordY(), z);         
            case 7:
                return Controller.getMapData(getRelCoordX() -(getRelCoordY() % 2 == 0 ? 1 : 0), getRelCoordY()-1, z);
            default:
                return Controller.getMapData(getRelCoordX(), getRelCoordY(), z);        
        } 
    }
}
