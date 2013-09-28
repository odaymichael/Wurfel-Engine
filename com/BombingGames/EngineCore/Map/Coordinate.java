package com.BombingGames.EngineCore.Map;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Gameobjects.AbstractGameObject;
import com.BombingGames.EngineCore.Gameobjects.Block;

/**
 *A coordinate is a reference to a specific cell in the map. The coordinate can transfer between relative and absolute coordiantes.
 * Relative coordinates are similar to the currently loaded map array. Absolute coordinates  are indipendent of the current map but to acces them you must have the chunk thet the coordiantes are in the currently loaded chunks.
 * @author Benedikt Vogler
 */
public class Coordinate {
    private final int topleftX;//top left chunk x coordinate
    private final int topleftY;//topl left chunk Y coordinate
    
    private int x;
    private int y;
    private float height;
    
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
     * Creates a new relative(!) coordinate from an existing coordinate
     * @param coord the Coordinate
     */
    public Coordinate(Coordinate coord) {
        this.x = coord.getRelX();
        this.y = coord.getRelY();
        this.height = coord.getHeight();
        
        topleftX = Controller.getMap().getChunkCoords(0)[0];
        topleftY = Controller.getMap().getChunkCoords(0)[1];
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
        return
            new Coordinate(
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
     * Geht the height (z-value) of the coordinate (game dimension).
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
        return Controller.getMap().getCellOffset(this);
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
     *Set the coordinates Z component. It will be transversed into a float value (height).
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
        Controller.getMap().setCelloffset(this, 2, height);
    }
    
    /**
     *
     * @param block
     */
    public void setBlock(Block block){
        Controller.getMap().setData(this, block);
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
     * Add a vector to the coordinates. If you just want the result and don't change the coordiantes use addVectorCpy.
     * @param vector
     * @return the new coordiantes which resulted of the addition
     */
    public Coordinate addVector(int[] vector) {
        this.x += vector[0];
        this.y += vector[1];
        this.height += vector[2]*Block.GAMEDIMENSION;
        return this;
    }
    
   /**
     * Add a vector to the coordinates. This method does not change the coordinates.
     * @param vector
     * @return the new coordiantes which resulted of the addition
     */
    public Coordinate addVectorCpy(int[] vector) {
        Coordinate newvec = this.cpy();
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
     * @param x
     * @param y
     * @param z
     * @return the new coordiantes which resulted of the addition
     */
    public Coordinate addVectorCpy(int x, int y, int z) {
        return addVectorCpy(new int[]{x,y,z});
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
        return getCellOffset()[0] != 0 || getCellOffset()[1] != 0 || getCellOffset()[2] != 0;
    }
    
   /**
     * The block hides the past block when it has sides and is not transparent (like normal block)
     * @return true when hiding the past Block
     */
    public boolean hidingPastBlock(){
        return (getBlock().hasSides() && ! getBlock().isTransparent() && ! hasOffset());
    }
    
    /**
     *Returns the screen x-position where the object is rendered without regarding the camera. It also adds the cell offset.
     * @return
     */
    public int get2DPosX() {
        int offset = 0;
        if (getZ()>=0)
            offset = (int) (getCellOffset()[0]);
        return getRelX() * Block.SCREEN_WIDTH //x-coordinate multiplied by it's dimension in this direction
               + (getRelY() % 2) * AbstractGameObject.SCREEN_WIDTH2 //offset by y
               + offset;
    }
    
    /**
     *Returns the screen y-position where the object is rendered without regarding the camera. It also adds the cell offset.
     * @return
     */
    public int get2DPosY() {
        int offset = 0;
        if (getZ()>=0)
            offset = (int) (getCellOffset()[1] / 2) //add the objects position inside this coordinate
                    - (int) (getCellOffset()[2] / Math.sqrt(2)); //add the objects position inside this coordinate
        return getRelY() * Block.SCREEN_DEPTH2 //y-coordinate * the tile's half size size
               - (int) (getHeight() / Math.sqrt(2)) //take axis shortening into account
               + offset;
    }
    
    /** @return a copy of this coordinate */
    public Coordinate cpy () {
            return new Coordinate(this);
    }
    
    /**
     * Checks if the coordiantes are accessable with the currently loaded Chunks.
     * @return 
     */
    public boolean onLoadedMap(){
        return (getRelX() >= 0 && getRelX() < Map.getBlocksX()
            && getRelY() >= 0 && getRelY() < Map.getBlocksY());
    }
    
    /**
     * Returns the field-id where the coordiantes are inside in relation to the current field. Field id count clockwise, starting with the top with 0.
     * If you want to get the neighbour you can use neighbourSidetoCoords(Coordinate coords, int sideID) with the second parameter found by this function.
     * The numbering of the sides:<br>
     * 7 \ 0 / 1<br>
     * -------<br>
     * 6 | 8 | 2<br>
     * -------<br>
     * 5 / 4 \ 3<br>
     * @param x game-space-coordinates, value in pixels
     * @param y game-space-coordinates, value in pixels
     * @return Returns the fieldnumber of the coordinates. 8 is the field itself.
     * @see com.BombingGames.Game.Gameobjects.AbstractGameObject#neighbourSidetoCoords(com.BombingGames.EngineCore.Map.Coordinate, int)
     */
    public static int getNeighbourSide(float x, float y) {       
        int result = 8;//standard result
        if (x + y <= Block.SCREEN_DEPTH) {
            result = 7;
        }
        if (x - y >= Block.SCREEN_DEPTH) {
            if (result == 7) {
                result = 0;
            } else {
                result = 1;
            }
        }
        if (x + y >= 3 * Block.SCREEN_DEPTH) {
            if (result == 1) {
                result = 2;
            } else {
                result = 3;
            }
        }
        if (-x + y >= Block.SCREEN_DEPTH) {
            if (result == 3) {
                result = 4;
            } else if (result == 7) {
                result = 6;
            } else {
                result = 5;
            }
        }
        return result;
    }

    /**
     * Get the neighbour coordinates of the neighbour of the coords you give.
     * @param coords the coordinates of the field
     * @param neighbourSide the side number of the given coordinates
     * @return The coordinates of the neighbour.
     */
    public static Coordinate neighbourSidetoCoords(Coordinate coords, int neighbourSide) {
        int[] result = new int[3];
        switch (neighbourSide) {
            case 0:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY() - 2;
                break;
            case 1:
                result[0] = coords.getRelX() + (coords.getRelY() % 2 == 1 ? 1 : 0);
                result[1] = coords.getRelY() - 1;
                break;
            case 2:
                result[0] = coords.getRelX() + 1;
                result[1] = coords.getRelY();
                break;
            case 3:
                result[0] = coords.getRelX() + (coords.getRelY() % 2 == 1 ? 1 : 0);
                result[1] = coords.getRelY() + 1;
                break;
            case 4:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY() + 2;
                break;
            case 5:
                result[0] = coords.getRelX() - (coords.getRelY() % 2 == 0 ? 1 : 0);
                result[1] = coords.getRelY() + 1;
                break;
            case 6:
                result[0] = coords.getRelX() - 1;
                result[1] = coords.getRelY();
                break;
            case 7:
                result[0] = coords.getRelX() - (coords.getRelY() % 2 == 0 ? 1 : 0);
                result[1] = coords.getRelY() - 1;
                break;
            default:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY();
        }
        result[2] = coords.getZ();
        return new Coordinate(result[0], result[1], result[2], true);
    }
}