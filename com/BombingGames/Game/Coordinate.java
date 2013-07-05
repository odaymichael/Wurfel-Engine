package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.Block;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;

/**
 *
 * @author Benedikt Vogler
 */
public class Coordinate {
    int x;
    int y;
    float height;
    int topleftX;//top left chunk x coordinate
    int topleftY;//topl left chunk Y coordinate
    
    /**
     * Creates a coordiante at the current relative postion
     * @param x
     * @param y
     * @param z 
     */
    public Coordinate(int x, int y, int z, boolean relative) {
        topleftX = Controller.getMap().getChunkCoords(0)[0];
        topleftY = Controller.getMap().getChunkCoords(0)[1];
        if (relative){
            this.x = x;
            this.y = y;
        } else {
            this.x = x - topleftX * Chunk.getBlocksX();
            this.y = y - topleftY * Chunk.getBlocksY();
        }
        this.height = z*Block.GAMEDIMENSION;
    }
    
     /**
     * Creates a coordiante at the current relative postion
     * @param x
     * @param y
     * @param height 
     */
    public Coordinate(int x, int y, float height, boolean relative) {
        topleftX = Controller.getMap().getChunkCoords(0)[0];
        topleftY = Controller.getMap().getChunkCoords(0)[1];
        if (relative){
            this.x = x;
            this.y = y;
        } else {
            this.x = x - topleftX * Chunk.getBlocksX();
            this.y = y - topleftY * Chunk.getBlocksY();
        }
        this.height = height;
    }
    
    /**
     * Creates a coordinate
     * @param x
     * @param y
     * @param z
     * @param chunkX
     * @param chunkY 
     */
    public Coordinate(int x, int y, int z, int chunkX,  int chunkY) {
        this.x = x;
        this.y = y;
        this.height = z*Block.GAMEDIMENSION;
        topleftX = chunkX;
        topleftY = chunkY;
    }
    
    public static Coordinate getMapCenter(){
        return getMapCenter(Map.getBlocksZ()*Block.GAMEDIMENSION/2);
    }
    
   public static Coordinate getMapCenter(float height){
        return new Coordinate(
                                Chunk.getBlocksX()/2,
                                Chunk.getBlocksY()/2,
                                height,
                                false
                            );
    }
        
    
    public int getRelX(){
        return x + (Controller.getMap().getChunkCoords(0)[0]-topleftX) * Chunk.getBlocksX();
    }
    public int getRelY(){
        return y + (Controller.getMap().getChunkCoords(0)[1]-topleftY) * Chunk.getBlocksY();
    }
    
    public int getAbsX(){
        return x + topleftX *Chunk.getBlocksX();
    }
    public int getAbsY(){
         return y + topleftY *Chunk.getBlocksY();
    }
    
    public int getZ(){
        return (int) (height/Block.GAMEDIMENSION);
    }
    
    public float getHeight(){
        return height;
    }

    
    public void setRelX(int x){
        this.x = x;
    }
    
    public void setRelY(int y){
        this.y = y;
    }
    
    public void setZ(int z){
        this.height = z*Block.GAMEDIMENSION;
    }
    
    public void setHeight(float height){
        this.height = height;
    }
    
    public int[] getRel(){
        return new int[]{getRelX(), getRelY(), getZ()};
    }
    
    public int[] getAbs(){
        return new int[]{getAbsX(), getAbsY(), getZ()};
    }
    public Coordinate addVector(int[] vector) {
            Coordinate newvec = this;
            newvec.x += vector[0];
            newvec.y += vector[1];
            newvec.height += vector[2]*Block.GAMEDIMENSION;
            return newvec;
    }

    public Coordinate addVector(int x, int y, int z) {
        return addVector(new int[]{x,y,z});
    }
    
    public Block getBlock(){
        return Controller.getMapData(this);
    }
    
    public float[] getCellOffset(){
        return Controller.getMap().getCellOffset(this);
    }
    
    public Block getBlockSafe(){
        return Controller.getMapDataSafe(this);
    }
    

    
    /**
     * Has the object an offset (pos vector)?
     * @return when it has offset true, else false
     */
    public boolean hasOffset() {
        return getCellOffset()[0] != DIM2 || getCellOffset()[1] != DIM2 || getCellOffset()[2] != 0;
    }
    
   /**
     * The block hides the past block when it has sides and is not transparent (like normal block)
     * @return true when hiding the past Block
     */
    public boolean hidingPastBlock(){
        return (getBlock().hasSides() && ! getBlock().isTransparent() && ! hasOffset());
    }
    
    public int getScreenPosX() {
        return getRelX() * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (getRelY() % 2) * DIM2; //y-coordinate multiplied by it's dimension in this direction
    }
    
    public int getScreenPosY() {
        return getRelY() * DIM4 //x-coordinate * the tile's size
               - (int) (getHeight() / Math.sqrt(2)); //take axis shortening into account
    }
}
