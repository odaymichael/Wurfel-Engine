package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;

/**
 *A Block that knows his own position
 * @author Benedikt
 */
public class SelfAwareBlock extends Block{
   private int absCoordX, absCoordY, coordZ;
    
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
     * Sets the absolute X and relative X coord.
     * @param X 
     */
    public void setAbsCoordX(int X){
        absCoordX = X;
    }
    

    /**
     * Sets the absolute Y and relative Y coord.
     * @param Y
     */
    public void setAbsCoordY(int Y){
        absCoordY = Y;
    } 
    
   /**
      *  CoordZ is always absolute and relative at the same time because there are no chunks in z direction.
      * @param z the new value for z
      */
    public void setCoordZ(int z) {
        coordZ = z;
    }
   
    /**
     * Set the absolute Coordinates
     * @param x
     * @param y
     * @param z
     */
    public void setAbsCoords(int x, int y, int z){
        absCoordX = x;
        absCoordY = y;
        
        //if Z is too high set to highes possible position
        if (z > Map.BLOCKS_Z-2)
            coordZ = Map.BLOCKS_Z -2;
        else coordZ = z;
    }
   
     /**
      * 
      * @return
      */
     public int getRelCoordX() {
        return absCoordX - Controller.getMap().getCoordlist(4)[0]  * Chunk.BLOCKS_X;
    }

    /**
     * 
     * @return
     */
    public int getRelCoordY() {
        return absCoordY - Controller.getMap().getCoordlist(4)[1] * Chunk.BLOCKS_Y;
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
     *  CoordZ is always absolute and relative at the same time because there are no chunks in z direction.
     * @return
     */
    public int getCoordZ() {
        return coordZ;
    }

    /**
     * Destroys the reference in the map.
     */
    protected void selfDestroy(){
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ, new Block(0,0));
    }
    
    /**
     * Put the reference to this object at the coordinates inside the map
     */
    protected void selfRebuild(){
        Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ, this);

    }
    
   /**
     * Returns a block next to it.
     * 701
     * 682
     * 543
     * @param side Counts clockwise startin with the top 0.
     * @param relZ if you want to check another layer. relZ is added to current Z coord.
     * @return The neighbour block
     */
    public int[] getNeighbourCoords(int side, int relZ){
       int result[] = new int[3];
       result[2] = coordZ+relZ;
        switch(side){
            case 0:
                result[0] = getRelCoordX();
                result[1] = getRelCoordY()-2;
            case 1:
                result[0] = getRelCoordX() -(getRelCoordY() % 2 == 1 ? 1 : 0);
                result[1] = getRelCoordY()-1;
            case 2:
                result[0] = getRelCoordX()+1;
                result[1] = getRelCoordY();
            case 3:
                result[0] = getRelCoordX() +(getRelCoordY() % 2 == 1 ? 1 : 0);
                result[1] = getRelCoordY()+1;
            case 4:
                result[0] = getRelCoordX();
                result[1] = getRelCoordY()+2;
            case 5:
                result[0] = getRelCoordX() -(getRelCoordY() % 2 == 0 ? 1 : 0);
                result[1] = getRelCoordY()+1;;
            case 6:
                result[0] = getRelCoordX()-1;
                result[1] = getRelCoordY();        
            case 7:
                result[0] = getRelCoordX() -(getRelCoordY() % 2 == 0 ? 1 : 0);
                result[1] = getRelCoordY()-1;
            default:
                result[0] = getRelCoordX();
                result[1] = getRelCoordY();      
        }
        return result;
    }
    
    public Block getNeighbourBlock(int side, int relZ){
        int neighbour[] = getNeighbourCoords(side,relZ);
        return Controller.getMapData(neighbour[0],neighbour[1],neighbour[2]);
    }
}
