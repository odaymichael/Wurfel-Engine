package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Wurfelengine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.newdawn.slick.util.Log;

/**
 * A Chunk is filled with many Blocks and is a part of the map.
 * @author Benedikt
 */
public class Chunk {
    /**
     * 
     */
    public int coordX, coordY;

    /**
     * The amount of blocks in X direction
     */
    public static final int BLOCKS_X = 12;//16:9 => 12:27, 4:3=>12:36
    /**
     * The amount of blocks in y direction
     */
    public static final int BLOCKS_Y = 28;//28
    /**
     * The amount of blocks in Z direction
     */
    public static final int BLOCKS_Z = 20;//20
    /**
    *The size of a chunk in pixels
    */
    public static int SizeX = BLOCKS_X*Block.WIDTH;
    /**
    *The size of a chunk in pixels
    */
    public static int SizeY = BLOCKS_Y*Block.HEIGHT/2;
    /**
     * The size of a chunk in pixels
     */
    public static int SizeZ = BLOCKS_Z*Block.HEIGHT;

    /**
     * 
     */
    public Block data[][][] = new Block[BLOCKS_X][BLOCKS_Y][BLOCKS_Z];
  
    /**
     * Creates a Chunk filled with air
     */
    public Chunk() {
        //fill everything with air
        for (int x=0; x < BLOCKS_X; x++)
            for (int y=0; y < BLOCKS_Y; y++)
                for (int z=0; z < BLOCKS_Z; z++)
                    data[x][y][z] = new Block(0,0);
    }
    
    /**
    *Creates a chunk.
    * @param ChunkX
    * @param ChunkY
    * @param startposX
    * @param startposY
    * @param loadmap load from HD(true) or generate new (false)?
    */
    public Chunk(int ChunkX, int ChunkY, int startposX, int startposY, boolean loadmap){
        this();
        coordX = ChunkX;
        coordY = ChunkY;

        if (loadmap) loadChunk();
            else newChunk();
    }
       
    private void newChunk(){
        //chunkdata will contain the blocks and objects
        //alternative to chunkdata.length ChunkBlocks
        Log.debug("Creating new chunk: "+ coordX + ", "+ coordY);
        Gameplay.MSGSYSTEM.add("Creating new chunk: "+coordX+", "+ coordY);
        for (int x=0; x < BLOCKS_X; x++)
            for (int y=0; y < BLOCKS_Y; y++){
                //Dirt from 0 to 8
                int height = (int) (Math.random()*BLOCKS_Z-1)+1;
                //int HEIGHT = (int) Chunk.BLOCKS_Z / 2;
                for (int z=0; z < height; z++){
                    //for (int z=0; z < BLOCKS_Z-4; z++)
                    data[x][y][z] = new Block(2);
                    // if ((z>10) && (x>10) && (y>5))
                    //  data[x][y][z] = new Block (9,0);
                    }
                data[x][y][height] = new Block(1);

                //data[0][y][BLOCKS_Z / 2 - 1] = new Block(2,0);


                //Gras on z=9
                //if ((x > 0) && (x < BLOCKS_X-1) && (y>0))                        
                //data[x][y][BLOCKS_Z/2-1] = new Block(1);
                //data[x][y][BLOCKS_Z/2] = new Block(9);


                /*
                if (y == BLOCKS_Y-1)
                    data[x][y][BLOCKS_Z/2-1] = new Block(0,0);

                //air from z=10 to 19
                for (int z = BLOCKS_Z / 2; z < BLOCKS_Z; z++)
                    data[x][y][z] = new Block(0,0);   
                */
            }
    }
        
    private void loadChunk(){
        /*//to find root path of project
        File here = new File(".");
        System.out.println(here.getAbsolutePath());*/

        //Reading map files test
        try {
            /*
                //the path may cause problems with other IDE's
            FileReader input = new FileReader("build/classes/map1/map.otmi");
            BufferedReader bufRead = new BufferedReader(input);

            int count = 1;  // Line number of count 


            String readstring = "";

            // Read through file one line at time. Print line # and line
            while (readstring != null){
                readstring = bufRead.readLine();
                StringBuilder line = new StringBuilder();
                line.append(readstring);
                line.deleteCharAt(0);

                if (readstring != null) System.out.println(count+":"+line.toString());

                count++;
            }

            bufRead.close();
            */
            // if (new File("map/chunk"+coordX+","+coordY+".otmc").exists()) {
            File  path = new File(Wurfelengine.getWorkingDirectory().getAbsolutePath() + "/map/chunk"+coordX+","+coordY+".otmc");
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

                //nimm einen Block auseinander
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
                        } while (x < BLOCKS_X);

                        line = new StringBuilder();
                        line.append(bufRead.readLine());

                        y++;
                    } while (y < BLOCKS_Y);
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
}
