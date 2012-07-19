package Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chunk {
        public int coordX, coordY, posX, posY;
        public Block data[][][] = new Block[Controller.ChunkSizeX][Controller.ChunkSizeY][Controller.ChunkSizeZ];
        public static final int width = Controller.ChunkSizeX*Controller.tilesizeX;
        public static final int height = Controller.ChunkSizeY*Controller.tilesizeY/2;

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
                for (int x=0; x < Controller.ChunkSizeX; x++)
                    for (int y=0; y < Controller.ChunkSizeY; y++){
                        //Dirt from 0 to 8
                        for (int z=0; z < Controller.ChunkSizeZ / 2 - 1; z++)
                            data[x][y][z] = new Block(2,0);

                        //Gras on z=9
                        data[x][y][Controller.ChunkSizeZ / 2 - 1] = new Block(1,0);

                        //air from z=10 to 19
                        for (int z = Controller.ChunkSizeZ / 2; z < Controller.ChunkSizeZ; z++)
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
