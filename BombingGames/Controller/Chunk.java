package BombingGames.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chunk {
        public int coordX, coordY, posX, posY;
        public Block data[][][] = new Block[GameController.ChunkSizeX][GameController.ChunkSizeY][GameController.ChunkSizeZ];
        public static final int width = GameController.ChunkSizeX*GameController.tilesizeX;
        public static final int height = GameController.ChunkSizeY*GameController.tilesizeY/2;

        //Konstruktor
        public Chunk(int ChunkX, int ChunkY, int startposX, int startposY, boolean loadmap){
            coordX = ChunkX;
            coordY = ChunkY;
            posX = startposX;
            posY = startposY;
            
            if (loadmap) {
                loadchunk();
            }else{
                //chunkdata will contain the blocks and objects
                //alternative to chunkdata.length ChunkSize
                for (int x=0; x < GameController.ChunkSizeX; x++)
                    for (int y=0; y < GameController.ChunkSizeY; y++){
                        //Dirt from 0 to 8
                        for (int z=0; z < GameController.ChunkSizeZ / 2 - 1; z++)
                            data[x][y][z] = new Block(2,0);

                        //Gras on z=9
                        data[x][y][GameController.ChunkSizeZ / 2 - 1] = new Block(1,0);

                        //air from z=10 to 19
                        for (int z = GameController.ChunkSizeZ / 2; z < GameController.ChunkSizeZ; z++)
                            data[x][y][z] = new Block(0,0);
                    }
            }
        }
       
        
       private void loadchunk(){
            /*
            //to find root path of project
            File here = new File(".");
            System.out.println(here.getAbsolutePath());*/
        
            //Reading map files test
            try {
                //the path may cause problems with other IDE's
                FileReader input = new FileReader("build/classes/map1/Map.otmi");
                BufferedReader bufRead = new BufferedReader(input);

                String line;    // String that holds current file line
                int count = 0;  // Line number of count 

                // Read first line
                line = bufRead.readLine();
                count++;

                // Read through file one line at time. Print line # and line
                while (line != null){
                    System.out.println(count+": "+line);
                    line = bufRead.readLine();
                    count++;
                }

                bufRead.close();
            } catch (IOException ex) {
                Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
}
