package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.MainMenu.MainMenuState;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.Log;

/**
 *A map stores nine chunks as part of a bigger map.
 * @author Benedikt
 */
public class Map {
    public static final float GRAVITY = 9.81f;
    
    private static int blocksX, blocksY, blocksZ;    
    private Block data[][][];
    private boolean recalcRequested;
    private int[][] coordlist = new int[9][2];
    private Minimap minimap;
    
      
    /**
     * Creates a map.
     * @param load Should the map be generated or loaded from disk?
     */
    public Map(boolean load) {
        Log.debug("Creating the map...");
        Log.debug("Should the Engine load a map: "+load);
        if (load) Chunk.readMapInfo();
        //save chunk size, which is now loaded
        blocksX = Chunk.getBlocksX()*3;
        blocksY = Chunk.getBlocksY()*3;
        blocksZ = Chunk.getBlocksZ();
        data = new Block[blocksX][blocksY][blocksZ];
        
        //Fill the nine chunks
        Chunk tempchunk;
        int pos = 0;
        
        for (int y=-1; y < 2; y++)
            for (int x=-1; x < 2; x++){
                coordlist[pos][0] = x;
                coordlist[pos][1] = y;
                tempchunk = new Chunk(
                        x,
                        y,
                        load
                );
                setChunk(pos, tempchunk);
                pos++;               
            }
        recalcRequested = true;
       
        //minimap = new Minimap();
        Log.debug("...Finished creating the map");
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
    
    Block[][][] getData() {
        return data;
    }
    
    /**
     * Reorgnanises the map and sets the center to param center.
     * Move all chunks when loading or creating a new piece of the map
     * @param center center is 1,3,5 or 7
     */
    public void setCenter(int center){
        Log.debug("ChunkSwitch:"+center);
        /*
                |0|1|2|
                -------------
                |3|4|5|
                -------------
                |6|7|8|
                */
        if (center==1 || center==3 || center==5 || center==7) {
        
        //make a copy of the data
        Block data_copy[][][] = copyOf3Dim(data);
        
        for (int pos=0; pos<9; pos++){
            coordlist[pos][0] += (center == 3 ? -1 : (center == 5 ? 1 : 0));
            coordlist[pos][1] += (center == 1 ? -1 : (center == 7 ? 1 : 0));
            
            if (isMovingChunkPossible(pos,center)){
                //System.out.println("[" + pos + "] <- ["+ (pos + center - 4) +"] (old)");
                setChunk(
                    pos,
                    getChunk(data_copy, pos -4 + center)
                );
            } else {
                setChunk(
                        pos,
                        new Chunk(
                            coordlist[pos][0],
                            coordlist[pos][1],
                            MainMenuState.loadmap
                        )
                );
            }
        }
        //all selfaware objects should be updated here, atm only player
        
        recalcRequested = true;
        } else {
            Log.error("setCenter was called with center:"+center);
        }
    }
    
    /**
     * checks if the number can be reached by moving the net in a direction
     * @param pos the position you want to check
     * @param direction the direction the chunkswitch is made to
     * @return 
     */
     private boolean isMovingChunkPossible(int pos, int direction){
        boolean result = true; 
        switch (direction){
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
     * Get a chunk out of data (should be a copy of this.data)
     * @param data
     * @param pos
     */ 
    private Chunk getChunk(Block[][][] data, int pos) {
        Chunk tmpChunk = new Chunk();
        for (int x = Chunk.getBlocksX()*(pos % 3);
                x < Chunk.getBlocksX()*(pos % 3+1);
                x++
            )
                for (int y = Chunk.getBlocksY()*Math.abs(pos / 3);
                        y < Chunk.getBlocksY()*Math.abs(pos / 3+1);
                        y++
                    ) {
                    System.arraycopy(
                        data[x][y],                
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
     * @param pos The position in the grid
     * @param newchunk The chunk you want to insert
     */
    private void setChunk(int pos, Chunk newchunk) {
        for (int x=0;x < Chunk.getBlocksX(); x++)
            for (int y=0;y < Chunk.getBlocksY();y++) {
                System.arraycopy(
                    newchunk.getData()[x][y],
                    0,
                    data[x+ Chunk.getBlocksX()*(pos%3)][y+ Chunk.getBlocksY()*Math.abs(pos/3)],
                    0,
                    Chunk.getBlocksZ()
                );
            }
    }
    
    /**
     * Informs the map that a recalc is requested. It will do it in the next update. Thhis method exist to prevent exzessive updates.
     */
    public void requestRecalc(){
        recalcRequested = true;
    }
    
    /**
     * When the recalc was requested it calls raytracing and light recalculing. This method should be called every update.
     * Request a recalc with <i>reuqestRecalc()</i>. 
     */
    public void recalcIfRequested(){
        if (recalcRequested) {
           Log.debug("recalc start");
            Gameplay.view.raytracing();
            Gameplay.view.calc_light();
            recalcRequested = false;
            Log.debug("recalc finish");
        }
    }
    
    /**
     * Draws the map
     */
    public void draw() {
        if (Gameplay.controller.hasGoodGraphics()) Block.getBlocksheet().bind();
        if (Gameplay.controller.hasGoodGraphics()) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
        
        Block.getBlocksheet().startUse();
        //render vom bottom to top
        for (int z=0; z < Chunk.getBlocksZ(); z++) {
            for (int y = Gameplay.view.getCamera().getTopBorder(); y < Gameplay.view.getCamera().getBottomBorder(); y++) {//vertikal
                for (int x = Gameplay.view.getCamera().getLeftBorder(); x < Gameplay.view.getCamera().getRightBorder(); x++){//horizontal
                    if (
                        (x < Map.getBlocksX()-1 && data[x+1][y][z].getRenderorder() == -1)
                        ||
                        data[x][y][z].getRenderorder() == 1
                       ) 
                    {
                        x++;
                        data[x][y][z].draw(x,y,z);//draw the right block first
                        data[x-1][y][z].draw(x-1,y,z);    
                    } else data[x][y][z].draw(x,y,z);
                }
            }
       }
       Block.getBlocksheet().endUse(); 
       if (Gameplay.controller.hasGoodGraphics()) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
    }

   /**
     *A list of the coordinates of the current chunks. 
     * @param pos the position of the chunk
     * @return the coordinates of the chunk
     */
    public int[] getCoordlist(int pos) {
        return coordlist[pos];
    }

    /**
     * 
     * @return the minimap of this map
     */
    public Minimap getMinimap() {
        return minimap;
    }
    
    /**
     * Returns a block of the map.
     * @param x If too high or too low, it takes the highest/deepest value possible
     * @param y If too high or too low, it takes the highest/deepest value possible
     * @param z If too high or too low, it takes the highest/deepest value possible
     * @return A single block at the wanted coordinates.
     */
    public Block getData(int x, int y, int z){
        if (x >= Chunk.getBlocksX()*3){
            x = Chunk.getBlocksX()*3-1;
            //Log.warn("X too high!");
        } else if(x<0){
            x = 0;
            //Log.warn("X too low!");
        }
        
        if (y >= Map.blocksY){
            y = Map.blocksY-1;
           // Log.warn("Y too high!");
        } else if(y<0){
            y = 0;
            //Log.warn("Y too low!");
        }
        
        if (z >= Chunk.getBlocksZ()){
            z = Chunk.getBlocksZ()-1;
            //Log.warn("Z too high!");
        } else if(z<0){
            z = 0;
            //Log.warn("Z too low!");
        }
        
        return data[x][y][z];    
    }
    
    /**
     * Returns  a Block without checking the parameters first. Good for debugging and also faster.
     * @param x 
     * @param y 
     * @param z 
     * @return the single block
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public Block getDataUnsafe(int x, int y, int z){
            return data[x][y][z];  
    }
    
    /**
     * Set a block at a specific coordinate
     * @param x
     * @param y
     * @param z
     * @param block The block you want to set.
     */
    public void setData(int x, int y, int z, Block block){
        if (x >= Chunk.getBlocksX()*3){
            x = Chunk.getBlocksX()*3-1;
           // Log.warn("X too high!");
        } else if(x<0){
            x = 0;
           // Log.warn("X too low!");
        }
        
        if (y >= Map.blocksY){
            y = Map.blocksY-1;
            //Log.warn("Y too high!");
        } else if(y<0){
            y = 0;
           // Log.warn("Y too low!");
        }
        
        if (z >= Chunk.getBlocksZ()){
            z = Chunk.getBlocksZ()-1;
            //Log.warn("Z too high!");
        } else if(z<0){
            z = 0;
            //Log.warn("Z too low!");
        }
        data[x][y][z] = block;    
    }
    
    public void earthquake(){
        int numberofblocks = 2000;
        int[] x = new int[numberofblocks];
        int[] y = new int[numberofblocks];
        int[] z = new int[numberofblocks];
        
        //pick random blocks 
        for (int i=0;i<numberofblocks;i++){
            x[i] = (int) (Math.random()*Chunk.getBlocksX()*3-1);
            y[i] = (int) (Math.random()*Map.blocksY-1);
            z[i] = (int) (Math.random()*Chunk.getBlocksZ());
        }
        
        for (int i=0;i<numberofblocks;i++){
            data[x[i]][y[i]][z[i]].setOffset((int) (Math.random()*Block.WIDTH/2)-Block.WIDTH/2, (int) (Math.random()*Block.WIDTH/2)-Block.WIDTH/2);
        }
        requestRecalc();
    }

    public static int getBlocksX() {
        return blocksX;
    }

    public static int getBlocksY() {
        return blocksY;
    }

    public static int getBlocksZ() {
        return blocksZ;
    }
}
