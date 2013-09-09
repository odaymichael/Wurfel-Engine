package com.BombingGames.EngineCore.Map;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.MainMenu.MainMenuScreen;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/**
 *A map stores nine chunks as part of a bigger map.
 * @author Benedikt Vogler
 */
public class Map {
    /**
     * The gravity constant in m/s^2
     */
    public static final float GRAVITY = 9.81f;
    /**
     *
     */
    public final static boolean ENABLECHUNKSWITCH = false;
    
    /**in which direction is the world spinning? This is needed for the light engine.
     * WEST->SOUTH->EAST = 0
      * SOUTH->WEST->NORTH = -90
      * EAST->NORTH->WEST = -180
       *NORTH->EAST->SOUT = -270
       **/
    private int worldSpinDirection;
    
    private boolean newMap;
    
    /**A list which has all current nine chunk coordinates in it.*/
    private int[][] coordlist = new int[9][2];
    
    private static int blocksX, blocksY, blocksZ;
    /** the map data are the blocks in their cells */
    private Block[][][] data;
    private float[][][][] cellOffset;
    /** every entity on the map is stored in this field */
    private ArrayList<AbstractEntity> entitylist = new ArrayList<AbstractEntity>();
        
    
  
    /**
     *
     * @param newMap  when "true" a new map os generated, when "false" a map is loaded from disk 
     */
    public Map(boolean newMap){
        this(newMap,0);
    }  
    
    /**
     * Creates a map. Fill the map with fillWithBlocks(boolean load);
     * @param newMap when "true" a new map os generated, when "false" a map is loaded from disk
     * @param worldSpinDirection the angle of the "morning" (0° is left).
     * @see fillWithBlocks(boolean load)
     */
    public Map(boolean newMap, int worldSpinDirection) {
        Gdx.app.log("DEBUG","Should the Engine generate a new map: "+newMap);
        this.newMap = newMap;
        this.worldSpinDirection = worldSpinDirection;
        
        
        if (!newMap) Chunk.readMapInfo();
        
        //save chunk size, which are now loaded
        blocksX = Chunk.getBlocksX()*3;
        blocksY = Chunk.getBlocksY()*3;
        blocksZ = Chunk.getBlocksZ();
        data = new Block[blocksX][blocksY][blocksZ];//create Array where teh data is stored
        
        //set the offset for every cell
        cellOffset = new float[blocksX][blocksY][blocksZ][];
        for (int x = 0; x < cellOffset.length; x++) {
            for (int y = 0; y < cellOffset[x].length; y++) {
                for (int z = 0; z < cellOffset[x][y].length; z++) {
                    cellOffset[x][y][z] = new float[]{Block.DIM2, Block.DIM2,0};
                }
            }   
        }
    }
    
    /**
     * Fill the data array of the map with blocks.
     */
    public void fillWithBlocks(){
        Gdx.app.log("DEBUG","Filling the map with blocks...");

        //Fill the nine chunks
        Chunk tempchunk;
        int chunkpos = 0;
        
        for (int y=-1; y < 2; y++)
            for (int x=-1; x < 2; x++){
                coordlist[chunkpos][0] = x;
                coordlist[chunkpos][1] = y;  
                tempchunk = new Chunk(chunkpos, x, y, newMap);
                insertChunk(chunkpos, tempchunk);
                chunkpos++;
        }
       
        Gdx.app.log("DEBUG","...Finished filling the map");
    }
    
     /**
     * Returns the amount of Blocks inside the map in x-direction.
     * @return
     */
    public static int getBlocksX() {
        return blocksX;
    }

    /**
     * Returns the amount of Blocks inside the map in y-direction.
     * @return
     */
    public static int getBlocksY() {
        return blocksY;
    }

    /**
     * Returns the amount of Blocks inside the map in z-direction.
     * @return 
     */
    public static int getBlocksZ() {
        return blocksZ;
    }
    
    
    /**
     * Copies an array with three dimensions. Code by Kevin Brock from http://stackoverflow.com/questions/2068370/efficient-system-arraycopy-on-multidimensional-arrays
     * @param array
     * @return The copy of the array-
     */
    private Block[][][] copyOf3Dim(Block[][][] array) {
        Block[][][] copy;
        copy = new Block[array.length][][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = new Block[array[i].length][];
            for (int j = 0; j < array[i].length; j++) {
                copy[i][j] = new Block[array[i][j].length];
                System.arraycopy(array[i][j], 0, copy[i][j], 0, 
                    array[i][j].length);
            }
        }
        return copy;
    } 
    
    /**
     * Get the data of the map
     * @return
     */
    public Block[][][] getData() {
        return data;
    }
    
    /**
     * Reorgnanises the map and sets the new middle chunk to param newmiddle.
     * Move all chunks when loading or creating a new piece of the map
     *    |0|1|2|
     *     -------------
     *    |3|4|5|
     *     -------------
     *    |6|7|8|
     * @param newmiddle newmiddle is 1, 3, 5 or 7
     */
    public void setCenter(int newmiddle){
        if (ENABLECHUNKSWITCH){
            Gdx.app.log("DEBUG","ChunkSwitch:"+newmiddle);
            if (newmiddle==1 || newmiddle==3 || newmiddle==5 || newmiddle==7) {

            //make a copy of the data
            Block data_copy[][][] = copyOf3Dim(data);

            for (int pos=0; pos<9; pos++){
                //refresh coordinates
                coordlist[pos][0] += (newmiddle == 3 ? -1 : (newmiddle == 5 ? 1 : 0));
                coordlist[pos][1] += (newmiddle == 1 ? -1 : (newmiddle == 7 ? 1 : 0));

                if (isMovingChunkPossible(pos, newmiddle)){
                    insertChunk(pos, getChunk(data_copy, pos - 4 + newmiddle));
                } else {

                    insertChunk(
                            pos,
                            new Chunk(pos,
                                coordlist[pos][0],
                                coordlist[pos][1],
                                MainMenuScreen.shouldLoadMap()
                            )
                    );

                }
            }

            Controller.requestRecalc();
            } else {
                Gdx.app.log("ERROR","setCenter was called with center:"+newmiddle);
            }
        }
    }
    
    /**
     * checks if the number can be reached by moving the net in a newmiddle
     * @param cellOffset the position you want to check
     * @param newmiddle the newmiddle the chunkswitch is made to
     * @return 
     */
     private boolean isMovingChunkPossible(int pos, int newmiddle){
        boolean result = true; 
        switch (newmiddle){
            case 1: if ((pos==0) || (pos==1) || (pos==2)) result = false;
            break;
            
            case 3: if ((pos==0) || (pos==3) || (pos==6)) result = false;
            break;  
                
            case 5: if ((pos==2) || (pos==5) || (pos==8)) result = false;
            break;
                
            case 7: if ((pos==6) || (pos==7) || (pos==8)) result = false;
            break;
        } 
        return result;
    }
     
    /**
     * Get a chunk out of a map (should be a copy of Map.data)
     * @param src The map
     * @param cellOffset The chunk number
     */ 
    private Chunk getChunk(Block[][][] src, int pos) {
        Chunk tmpChunk = new Chunk();
        //copy the data in two loops and arraycopy
        for (int x = Chunk.getBlocksX()*(pos % 3);
                x < Chunk.getBlocksX()*(pos % 3+1);
                x++
            )
                for (int y = Chunk.getBlocksY()*Math.abs(pos / 3);
                        y < Chunk.getBlocksY()*Math.abs(pos / 3+1);
                        y++
                    ) {
                    System.arraycopy(
                        src[x][y],                
                        0,
                        tmpChunk.getData()[x-Chunk.getBlocksX()*(pos % 3)][y - Chunk.getBlocksY()*(pos / 3)],
                        0,
                        Chunk.getBlocksZ()
                    );
                }
        return tmpChunk;
    }

    /**
     * Inserts a chunk in the map.
     * @param cellOffset The position in the grid
     * @param newChunk The chunk you want to insert
     */
    private void insertChunk(int pos, Chunk newChunk) {
        for (int x=0;x < Chunk.getBlocksX(); x++)
            for (int y=0;y < Chunk.getBlocksY();y++) {
                System.arraycopy(
                    newChunk.getData()[x][y],
                    0,
                    data[x+ Chunk.getBlocksX()*(pos%3)][y+ Chunk.getBlocksY()*Math.abs(pos/3)],
                    0,
                    Chunk.getBlocksZ()
                );
            }
    }
    
    
    /**
     * Draws the map
     * @param view 
     * @param camera 
     */
    public void render(View view, WECamera camera) {
        //if (Gameplay.getController().hasGoodGraphics()) Block.getSpritesheet().bind();
        //if (Gameplay.getView().hasGoodGraphics()) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
        
        view.getBatch().begin();
        //Block.getSpritesheet().getFullImage().startUse();
        //render vom bottom to top
        for (int i=0; i < camera.depthsortlistSize() ;i++) {
            Coordinate coords = camera.getDepthsortCoord(i);//get the coords of the current renderobject
            int entitynumber = camera.getEntityIndex(i); //get the entityindex to check if it is an entity
            
            if (entitynumber == -1) //if a block then  get it and draw it
                getData(coords).render(view, camera, coords);
            else //if it's an entity get it and draw it
                entitylist.get(entitynumber).render(view, camera, coords);        
        }
        view.getBatch().end();
       //Block.getSpritesheet().getFullImage().endUse(); 
      // if (Gameplay.getView().hasGoodGraphics()) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
    }

   /**
     *Get the coordinates of a chunk. 
     * @param pos 
     * @return the coordinates of the chunk
     */
    public int[] getChunkCoords(int pos) {
        return coordlist[pos];
    }
    
   
    
    /**
     * Returns  a Block without checking the parameters first. Good for debugging and also faster.
     * @param x position
     * @param y position
     * @param z position
     * @return the single renderobject you wanted
     */
    public Block getData(int x, int y, int z){
        return data[x][y][z];  
    }
    
    /**
     *
     * @param coord
     * @return
     */
    public Block getData(Coordinate coord){
        return data[coord.getRelX()][coord.getRelY()][coord.getZ()];  
    }
    
     /**
     * Returns a renderobject of the map.
     * @param x If too high or too low, it takes the highest/deepest value possible
     * @param y If too high or too low, it takes the highest/deepest value possible
     * @param z If too high or too low, it takes the highest/deepest value possible
     * @return A single Block at the wanted coordinates.
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public Block getDataSafe(int x, int y, int z){
        if (x >= blocksX){
            x = blocksX-1;
            Gdx.app.log("DEBUG","X:"+x);
        } else if( x<0 ){
            x = 0;
            Gdx.app.log("DEBUG","X:"+x);
        }
        
        if (y >= blocksY){
            y = blocksY-1;
            Gdx.app.log("DEBUG","Y:"+y);
        } else if( y < 0 ){
            y = 0;
            Gdx.app.log("DEBUG","Y:"+y);
        }
        
        if (z >= blocksZ){
            z = blocksZ-1;
            Gdx.app.log("DEBUG","Z:"+z);
        } else if( z < 0 ){
            z = 0;
            Gdx.app.log("DEBUG","Z:"+z);
        }
        
        return data[x][y][z];    
    }
    
    /**
     * 
     * @param coords
     * @return
     */
    public Block getDataSafe(Coordinate coords) {
        return getDataSafe(coords.getRelX(), coords.getRelY(), coords.getZ());
    }
    
    /**
     * Set a block at a specific coordinate.
     * @param x position
     * @param y position
     * @param z position
     * @param block  
     */
    public void setData(int x, int y, int z, Block block){
        data[x][y][z] = block;
    }
    
    /**
     * Set a block at a specific coordinate.
     * @param coords
     * @param block
     */
    public void setData(Coordinate coords, Block block) {
        data[coords.getRelX()][coords.getRelY()][coords.getZ()] = block;
    }
        
   /**
     * 
     * @param coords
     * @param block
     */
    public void setDataSafe(int[] coords, Block block) {       
        if (coords[0] >= blocksX){
            coords[0] = blocksX-1;
        } else if( coords[0]<0 ){
            coords[0] = 0;
        }
        
        if (coords[1] >= blocksY){
            coords[1] = blocksY-1;
        } else if( coords[1] < 0 ){
            coords[1] = 0;
        }
        
        if (coords[2] >= blocksZ){
            coords[2] = blocksZ-1;
        } else if( coords[2] < 0 ){
            coords[2] = 0;
        }
        
        data[coords[0]][coords[1]][coords[2]] = block;
    }
    
    /**
     * 
     * @param coord 
     * @param block
     */
    public void setDataSafe(Coordinate coord, Block block) {        
        setDataSafe(new int[]{
            coord.getRelX(),
            coord.getRelY(),
            coord.getZ()},
            block);
    }
    
    
    /**
     * a method who gives random blocks offset
     * @param numberofblocks the amount of moved blocks
     */
    public void earthquake(int numberofblocks){
        int[] x = new int[numberofblocks];
        int[] y = new int[numberofblocks];
        int[] z = new int[numberofblocks];
        
        //pick random blocks 
        for (int i=0;i<numberofblocks;i++){
            x[i] = (int) (Math.random()*blocksX-1);
            y[i] = (int) (Math.random()*blocksY-1);
            z[i] = (int) (Math.random()*blocksZ-1);
        }
        
        for (int i=0;i < numberofblocks; i++){
                //cellPos[x[i]][y[i]][z[i]][0] = (float) (Math.random()*Block.DIM2);
                //cellPos[x[i]][y[i]][z[i]][1] = (float) (Math.random()*Block.DIM2);
                cellOffset[x[i]][y[i]][z[i]][2] = (float) (Math.random()*Block.GAMEDIMENSION);
            
        }
        Controller.requestRecalc();
    }
    
   
    
    /**
     * Returns the entitylist
     * @return
     */
    public ArrayList<AbstractEntity> getEntitys() {
        return entitylist;
    }

    /**
     *
     * @return
     */
    public int getWorldSpinDirection() {
        return worldSpinDirection;
    }
    
    /**
     *
     * @return
     */
    public float[][][][] getCellOffset() {
        return cellOffset;
    }
    
    /**
     *
     * @param coord
     * @return
     */
    public float[] getCellOffset(Coordinate coord) {
        return cellOffset[coord.getRelX()][coord.getRelY()][coord.getZ()];
    }   
    
    /**
     *Set the offset in one cell. 
     * @param coord the cell
     * @param field 0 = X, 1 = y, 2 = z
     * @param value the value you want to set the field
     */
    public void setCelloffset(Coordinate coord, int field, float value){
        cellOffset[coord.getRelX()][coord.getRelY()][coord.getZSafe()][field] = value;
    }
    
    public <type> ArrayList<type> getAllEntitysOfType(Class type) {
        ArrayList<type> list=new ArrayList<type>();
        //e inst=(e) e.newInstance();

        for (AbstractEntity entity : entitylist) {
            if (type.isInstance(entity)) {
                list.add((type) entity);
            }
        }
        return list;
    }
}