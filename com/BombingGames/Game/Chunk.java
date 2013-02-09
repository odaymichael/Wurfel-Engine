package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Wurfelengine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.util.Log;

/**
 * A Chunk is filled with many Blocks and is a part of the map.
 * @author Benedikt
 */
public class Chunk {
    /**
     * The number of the mapgenerator used.
     */
    public static final int MAPGENERATOR = 1;
    
    private static int blocksX = 10;//16:9 => 12:27, 4:3=>12:36
    private static int blocksY = 40;//28
    private static int blocksZ = 10;//20
    
    private int coordX, coordY;
    private Block data[][][] = new Block[blocksX][blocksY][blocksZ];
  
    /**
     * Creates a Chunk filled with air
     */
    public Chunk() {
        for (int x=0; x < blocksX; x++)
            for (int y=0; y < blocksY; y++)
                for (int z=0; z < blocksZ; z++)
                    data[x][y][z] = new Block();
    }
    
    /**
    *Creates a chunk.
    * @param coordX 
    * @param coordY 
    * @param loadmap load from HD(true) or generate new (false)?
    */
    public Chunk(int coordX, int coordY, boolean loadmap){
        this();
        this.coordX = coordX;
        this.coordY = coordY;

        if (loadmap) loadChunk();
            else newChunk();
    }
    /**
     * Generates new content for a chunk.
     */  
    private void newChunk(){
        //chunkdata will contain the blocks and objects
        //alternative to chunkdata.length ChunkBlocks
        Log.debug("Creating new chunk: "+ coordX + ", "+ coordY);
        Gameplay.MSGSYSTEM.add("Creating new chunk: "+coordX+", "+ coordY);
        switch (MAPGENERATOR){
            case 0:{//random pillars
                for (int x=0; x < blocksX; x++)
                    for (int y=0; y < blocksY; y++){
                        int height = (int) (Math.random()*blocksZ-1)+1;
                        //int HEIGHT = (int) Chunk.blocksZ / 2;
                        for (int z=0; z < height; z++){
                            //for (int z=0; z < blocksZ-4; z++)
                            data[x][y][z] = new Block(2);
                            // if ((z>10) && (x>10) && (y>5))
                            //  data[x][y][z] = new Block (9,0);
                            }
                        data[x][y][height] = new Block(1);
                    }
                break;
            }
                
            case 1: {//island
                for (int x=0; x < blocksX; x++)
                    for (int y=0; y < blocksY; y++){
                        data[x][y][0] = new Block(9);
                        data[x][y][1] = new Block(9);
                    }
                
                int mountainx = (int) (Math.random()*blocksX-1);
                int mountainy = (int) (Math.random()*blocksY-1);
                
                for (int x=0; x < blocksX; x++)
                    for (int y=0; y < blocksY; y++){
                        int height = blocksZ-1- Math.abs(mountainy-y)- Math.abs(mountainx-x);
                        if (height>1){
                            for (int z=0; z < height; z++){
                                data[x][y][z] = new Block(2);
                            }
                            data[x][y][height] = new Block(1);
                        }
                    }
                break;
            }
                
            case 2: {//flat gras
               for (int x=0; x < blocksX; x++)
                    for (int y=0; y < blocksY; y++){
                        data[x][y][0] = new Block(2);
                        data[x][y][1] = new Block(1);
                    }
                break;
            }
                
            case 3: {//flat gras with one random pillar per chunk
                int pillarx = (int) (Math.random()*blocksX-1);
                int pillary = (int) (Math.random()*blocksY-1);
                for (int z=0; z < blocksZ; z++) data[pillarx][pillary][z] = new Block(1);
                
                for (int x=0; x < blocksX; x++)
                    for (int y=0; y < blocksY; y++){
                        data[x][y][0] = new Block(2);
                        data[x][y][1] = new Block(1);
                    }
                break;
            }    
        }
    }
    
    /**
     * loads a chunk from
     */
    private void loadChunk(){
        //Reading map files test
        try {
            // if (new File("map/chunk"+coordX+","+coordY+".otmc").exists()) {
            File path = new File(Wurfelengine.getWorkingDirectory().getAbsolutePath() + "/map/chunk"+coordX+","+coordY+".otmc");
            Log.debug("Trying to load Chunk: "+ coordX + ", "+ coordY + " from \"" + path.getAbsolutePath() + "\"");
            Gameplay.MSGSYSTEM.add("Load: "+coordX+","+coordY);
            
            if (path.isFile()) {
                //FileReader input = new FileReader("map/chunk"+coordX+","+coordY+".otmc");
                //BufferedReader bufRead = new BufferedReader(input);
                BufferedReader bufRead = new BufferedReader(new FileReader(path));

                StringBuilder line;
                //jump over first line to prevent problems with length byte
                bufRead.readLine();


                int z = 0;
                int x;
                int y;
                String lastline;

                //finish a layer
                do {
                    line = new StringBuilder();
                    line.append(bufRead.readLine());
                    //optionale Kommentarzeile überspringen
                    if ((line.charAt(1) == '/') && (line.charAt(2) == '/')){
                        line = new StringBuilder();
                        line.append(bufRead.readLine());
                    }

                    //Ebene
                    y = 0;
                    do{
                        x = 0;

                        do{
                            int posdots = line.indexOf(":");

                            int posend = 1;
                            while ((posend < -1+line.length()) && (line.charAt(posend)!= ' '))  {
                                posend++;
                            }

                            data[x][y][z] = new Block(
                                        (int) Integer.parseInt(line.substring(0,posdots)),
                                        (int) Integer.parseInt(line.substring(posdots+1, posend))
                                        );
                            x++;
                            line.delete(0,posend+1);
                        } while (x < blocksX);

                        line = new StringBuilder();
                        line.append(bufRead.readLine());

                        y++;
                    } while (y < blocksY);
                    lastline = bufRead.readLine();
                    z++;
                } while (lastline != null);
            } else {
                Log.debug("...but it could not be found. Creating new.");
                newChunk();
            }
        } catch (IOException ex) {
            Log.debug("Loading of chunk "+coordX+","+coordY + "failed: "+ex);
        }
    }
    
    /**
     * 
     */
    public static void readMapInfo(){
        BufferedReader bufRead = null;
        try {
            File path = new File(Wurfelengine.getWorkingDirectory().getAbsolutePath() + "/map/map.otmi");
            Log.debug("Trying to load Map Info from \"" + path.getAbsolutePath() + "\"");
            bufRead = new BufferedReader(new FileReader(path));
            String mapname = bufRead.readLine();
            mapname = mapname.substring(2, mapname.length());
            Log.info("Loading map: "+mapname);
            Gameplay.MSGSYSTEM.add("Loading map: "+mapname);   
            
            String mapversion = bufRead.readLine(); 
            mapversion = mapversion.substring(2, mapversion.length());
            Log.info("Map Version:"+mapversion);
            
            String blocksXString = bufRead.readLine();
            Log.info("sizex:"+blocksXString);
            blocksXString = blocksXString.substring(2, blocksXString.length());
            blocksX = Integer.parseInt(blocksXString);
            
            String blocksYString = bufRead.readLine();
            Log.info("sizey:"+blocksYString);
            blocksYString = blocksYString.substring(2, blocksYString.length());
            blocksY = Integer.parseInt(blocksYString);
            
            String blocksZString = bufRead.readLine();
            Log.info("sizez:"+blocksZString);
            blocksZString = blocksZString.substring(2, blocksZString.length());
            blocksZ = Integer.parseInt(blocksZString);
        } catch (IOException ex) {
            Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufRead.close();
            } catch (IOException ex) {
                Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns the data of the chunk
     * @return 
     */
    public Block[][][] getData() {
        return data;
    }

    /**
     * 
     * @param data
     */
    public void setData(Block[][][] data) {
        this.data = data;
    }

    /**
     * 
     * @return
     */
    public int getCoordX() {
        return coordX;
    }

    /**
     * 
     * @param coordX
     */
    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    /**
     * 
     * @return
     */
    public int getCoordY() {
        return coordY;
    }

    /**
     * 
     * @param coordY
     */
    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    /**
     * The amount of blocks in X direction
     * @return 
     */
    public static int getBlocksX() {
        return blocksX;
    }

    /**
     * The amount of blocks in Y direction
     * @return 
     */
    public static int getBlocksY() {
        return blocksY;
    }

   /**
     * The amount of blocks in Z direction
     * @return 
     */
    public static int getBlocksZ() {
        return blocksZ;
    }
}
