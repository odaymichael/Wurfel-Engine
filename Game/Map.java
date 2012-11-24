/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Blocks.Block;
import MainMenu.MainMenuState;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.Log;

/**
 *
 * @author Benedikt
 */
public class Map {
    /**
     * The map's data is stored inside here.
     */
    public Block data[][][] = new Block[Chunk.BlocksX*3][Chunk.BlocksY*3][Chunk.BlocksZ];

    private boolean recalcRequested;

    private int[] coordlistX = new int[9];

    private int[] coordlistY = new int[9];;

    /**
     * Contains the minimap
     */
    private Minimap minimap;
    
    
    /**
     * Creates a map.
     * @param loadmap Should the map be generated or loaded from disk?
     */
    public Map(boolean loadmap) {
        //create the map
        Chunk tempchunk;
        int pos = 0;
        
        for (int y=1; y > - 2; y--)
            for (int x=-1; x < 2; x++){
                coordlistX[pos] = x;
                coordlistY[pos] = y;
                tempchunk = new Chunk(
                        x,
                        y,
                        x*Chunk.SizeX,
                        -y*Chunk.SizeY,
                        loadmap
                );
                setChunk(pos, tempchunk);
                pos++;               
            }
        recalcRequested = true;
       
        minimap = new Minimap();
    }
    
    /**
     * Copies a 3D array
     * @param array
     * @return 
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
     * Reorgnanises the map and sets the center to param center.
     * Move all chunks when loading or creating a new piece of the map
     * @param center center is 1,3,5 or 7
     */
    public void setCenter(int center){
        /*
                |0|1|2|
                -------------
                |3|4|5|
                -------------
                |6|7|8|
                */
        if (center==1 || center==3 || center==5 || center==7) {
        
        //GameplayState.iglog.add("New Center: "+center);
        //System.out.println("New Center: "+center);
        
        //make a copy of the data
        Block data_copy[][][] = copyOf3Dim(data);
        
        for (int pos=0; pos<9; pos++){
            coordlistX[pos] += (center == 3 ? -1 : (center == 5 ? 1 : 0));
            coordlistY[pos] += (center == 1 ? 1 : (center == 7 ? -1 : 0));
            
            if (isChunkSwitchPosibble(pos,center)){
                //System.out.println("[" + pos + "] <- ["+ (pos + center - 4) +"] (old)");
                setChunk(
                    pos,
                    getChunk(data_copy, pos -4 + center)
                );
            } else {
                setChunk(
                        pos,
                        new Chunk(
                            coordlistX[pos],
                            coordlistY[pos],
                            coordlistX[pos] + (int) ((center == 3 ? -Chunk.SizeX : (center == 5 ?  Chunk.SizeX: 0))),
                            coordlistY[pos] + (int) ((center == 1 ? -Chunk.SizeY : (center == 7 ? Chunk.SizeY : 0))),
                            MainMenuState.loadmap
                        )
                );
                //System.out.println("["+pos+"] new: "+ coordlistX[pos] +","+coordlistY[pos]);
            }
        }
        //player switches chunk
        //System.out.println("Player was rel: "+Controller.player.getRelCoordX() + " | " + Controller.player.getRelCoordY() + " | " + Controller.player.coordZ);
        Gameplay.controller.player.setRelCoordX(Gameplay.controller.player.getRelCoordX() +  (center == 3 ? 1 : (center == 5 ? -1 : 0))*Chunk.BlocksX);
        Gameplay.controller.player.setRelCoordY(Gameplay.controller.player.getRelCoordY() + (center == 1 ? 1 : (center == 7 ? -1 : 0))*Chunk.BlocksY);
        //System.out.println("Player is rel: "+Controller.player.getRelCoordX() + " | " + Controller.player.getRelCoordY() + " | " + Controller.player.coordZ);
        recalcRequested = true;
        } else {
            Log.error("setCenter was called with center:"+center);
        }
    }
    
     private boolean isChunkSwitchPosibble(int pos, int movement){
        //checks if the number can be reached by moving the net in a direction, very complicated
        boolean result = true; 
        switch (movement){
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
        for (int x = Chunk.BlocksX*(pos % 3);
                x < Chunk.BlocksX*(pos % 3+1);
                x++
            )
                for (int y = Chunk.BlocksY*Math.abs(pos / 3);
                        y < Chunk.BlocksY*Math.abs(pos / 3+1);
                        y++
                    ) {
                    System.arraycopy(
                        data[x][y],                
                        0,
                        tmpChunk.data[x-Chunk.BlocksX*(pos % 3)][y-Chunk.BlocksY*(pos / 3)],
                        0,
                        Chunk.BlocksZ
                    );
                }
        return tmpChunk;
    }

    /**
     * Inserts a chunk in the map.
     * @param pos The position in the grid
     * @param newchunk 
     */
    private void setChunk(int pos, Chunk newchunk) {
        for (int x=0;x < Chunk.BlocksX; x++)
            for (int y=0;y < Chunk.BlocksY;y++) {
                System.arraycopy(
                    newchunk.data[x][y],
                    0,
                    data[x+ Chunk.BlocksX*(pos%3)][y+ Chunk.BlocksY*Math.abs(pos/3)],
                    0,
                    Chunk.BlocksZ
                );
            }
    }
    
    /**
     * Informs the map that a recalc is requested.
     */
    public void requestRecalc(){
        recalcRequested = true;
    }
    
    /**
     * When the recalc was requested it calls raytracing and light recalculing.
     * Request a recalc with <i>reuqestRecalc()</i>. This method should be called every update.
     */
    public void recalcIfRequested(){
        if (recalcRequested) {
            Gameplay.view.raytracing();
            Gameplay.view.calc_light();
            recalcRequested = false;
        }
    }
    
    /**
     * 
     */
    public void draw() {
        if (Gameplay.controller.goodgraphics) Block.Blocksheet.bind();
        if (Gameplay.controller.goodgraphics) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
        Block.Blocksheet.startUse();
        //render vom bottom to top
        for (int z=0; z < Chunk.BlocksZ; z++) {
            for (int y = Gameplay.view.camera.getTopBorder(); y < Chunk.BlocksY*3; y++) {//vertikal
                for (int x = Gameplay.view.camera.getLeftBorder(); x < Gameplay.view.camera.getRightBorder(); x++){//horizontal
                    if (
                        (x < Chunk.BlocksX*3-1 && Controller.map.data[x+1][y][z].renderorder == -1)
                        ||
                        Controller.map.data[x][y][z].renderorder == 1
                       ) {
                        x++;
                        data[x][y][z].renderblock(x,y,z);//draw the right block first
                        data[x-1][y][z].renderblock(x-1,y,z);
                        
                    } else data[x][y][z].renderblock(x,y,z);
                }
            }
       }
       Block.Blocksheet.endUse(); 
       if (Gameplay.controller.goodgraphics) GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
    }

   /**
     *A list of the X coordinates of the current chunks. 
     * @param i 
     * @return 
     */
    public int getCoordlistX(int i) {
        return coordlistX[i];
    }
  
   /**
     *A list for the y coordinates of the current chunks.
     * @param i 
     * @return 
     */
    public int getCoordlistY(int i) {
        return coordlistY[i];
    }
   

    /**
     * 
     * @return the minimap of this map
     */
    public Minimap getMinimap() {
        return minimap;
    }
    
    
}
