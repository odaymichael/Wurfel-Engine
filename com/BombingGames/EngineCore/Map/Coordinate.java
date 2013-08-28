package com.BombingGames.EngineCore.Map;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.Game.Gameobjects.Block;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;

/**
 *A coordinate poitns to a cell in the map. The coordainte can transfer between relative and absolute coordiantes.
 * Relative coordinates are similar to the currently laoded map array. Absolute coordinates  are indipendent of the current map but to acces themyou must have the chunk loaded.
 * @author Benedikt Vogler
 */
public class Coordinate {
    int x;
    int y;
    float height;
    int topleftX;//top left chunk x coordinate
    int topleftY;//topl left chunk Y coordinate
    
    /**
     * Creates a coordiante. You can specify wether the given values are absolute or relative to the map.
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     * @param relative   True when the coordiantes are relative to the currently loaded map. False when they are absolute.
     */
    public Coordinate(int x, int y, int z, final boolean relative) {
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
     * Creates a coordiante. You can specify wether the given values are absolute or relative to the map.
     * @param x The x value.
     * @param y The y value.
     * @param height The height of the Coordiante.
     * @param relative  True when the coordiantes are relative to the currently loaded map. False when they are absolute.
     */
    public Coordinate(int x, int y, float height, final boolean relative) {
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
     *Returns a coordinate pointing to the absolute(?) center of the map. Height is half the map's height.
     * @return
     */
    public static Coordinate getMapCenter(){
        return getMapCenter(Map.getBlocksZ()*Block.GAMEDIMENSION/2);
    }
    
    /**
     *Returns a corodinate pointing to the absolute(?) center of the map.
     * @param height You custom height.
     * @return
     */
    public static Coordinate getMapCenter(float height){
        return new Coordinate(
                                Chunk.getBlocksX()/2,
                                Chunk.getBlocksY()/2,
                                height,
                                false
                            );
    }
        
    
    /**
     *
     * @return
     */
    public int getRelX(){
        return x + (topleftX-Controller.getMap().getChunkCoords(0)[0]) * Chunk.getBlocksX();
    }
    /**
     *
     * @return
     */
    public int getRelY(){
        return y + (topleftY-Controller.getMap().getChunkCoords(0)[1]) * Chunk.getBlocksY();
    }
    
    /**
     *
     * @return
     */
    public int getAbsX(){
        return x + topleftX *Chunk.getBlocksX();
    }
    /**
     *
     * @return
     */
    public int getAbsY(){
         return y + topleftY *Chunk.getBlocksY();
    }
    
    /**
     *
     * @return
     */
    public int getZ(){
        return (int) (height/Block.GAMEDIMENSION);
    }
    
    /**
     *
     * @return
     */
    public int getZSafe(){
        int tmpZ =  (int) (height/Block.GAMEDIMENSION);
        if (tmpZ >= Map.getBlocksZ())
            return Map.getBlocksZ() -1;
        else if (tmpZ < 0) return 0;
            else return tmpZ;
    }
    
    /**
     * Geht the height (z-value) of the coordinate.
     * @return
     */
    public float getHeight(){
        return height;
    }

    
   /**
     *
     * @return an array with the offset of the cell
     */
    public float[] getCellOffset(){
        return Map.getCellOffset(this);
    }
 
    
    /**
     *Set the coordiantes X component.
     * @param x
     */
    public void setRelX(int x){
        this.x = x;
    }
    
    /**
     *Set the coordiantes Y component.
     * @param y
     */
    public void setRelY(int y){
        this.y = y;
    }
    
    /**
     *Set the coordiantes Z component. It will be transversed into a float value (height).
     * @param z
     */
    public void setZ(int z){
        this.height = z*Block.GAMEDIMENSION;
    }
    
    /**
     * 
     * @param height
     */
    public void setHeight(float height){
        this.height = height;
        
    }
    
    /**
     *Set the vertical offset in the cell, where the coordiante is pointing at.
     * @param height
     */
    public void setCellOffsetZ(float height){
        Map.setCelloffset(this, 2, height);
    }
    
    /**
     *
     * @return
     */
    public int[] getRel(){
        return new int[]{getRelX(), getRelY(), getZ()};
    }
    
    /**
     *
     * @return
     */
    public int[] getAbs(){
        return new int[]{getAbsX(), getAbsY(), getZ()};
    }
    /**
     * Add a vector to the coordinates. This method does not change the coordinates.
     * @param vector
     * @return the new coordiantes which resulted of the addition
     */
    public Coordinate addVector(int[] vector) {
            Coordinate newvec = this;
            newvec.x += vector[0];
            newvec.y += vector[1];
            newvec.height += vector[2]*Block.GAMEDIMENSION;
            return newvec;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return the new coordiantes which resulted of the addition
     */
    public Coordinate addVector(int x, int y, int z) {
        return addVector(new int[]{x,y,z});
    }
    
    /**
     *
     * @return
     */
    public Block getBlock(){
        return Controller.getMapData(this);
    }
    

    
    /**
     *
     * @return
     */
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
    
    /**
     *
     * @return
     */
    public int get2DPosX() {
        return getRelX() * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (getRelY() % 2) * DIM2; //y-coordinate multiplied by it's dimension in this direction
    }
    
    /**
     *
     * @return
     */
    public int get2DPosY() {
        return getRelY() * DIM4 //x-coordinate * the tile's size
               - (int) (getHeight() / Math.sqrt(2)); //take axis shortening into account
    }
}
