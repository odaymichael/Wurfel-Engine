package Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chunk {
        public int coordX, coordY, posX, posY;
        public static int width;
        public static int height;
        public static int SizeX = 9;
        public static int SizeY = 20;
        public static int SizeZ = 20;
        public Block data[][][] = new Block[SizeX][SizeY][SizeZ];

        //Konstruktor
        public Chunk(int ChunkX, int ChunkY, int startposX, int startposY, boolean loadmap){
            coordX = ChunkX;
            coordY = ChunkY;
            posX = startposX;
            posY = startposY;
            
            width = SizeX*Block.width;
            height = SizeY*Block.height/4;
           
            //fill everything with air to avoid crashes
            for (int x=0; x < SizeX; x++)
                for (int y=0; y < SizeY; y++)
                    for (int z=0; z < SizeZ; z++)
                        data[x][y][z] = new Block(0,0);
            
            if (loadmap) {
                loadchunk();
            }else{
                //chunkdata will contain the blocks and objects
                //alternative to chunkdata.length ChunkSize
                
                for (int x=0; x < SizeX; x++)
                    for (int y=0; y < SizeY; y++){
                        //Dirt from 0 to 8
                        for (int z=0; z < SizeZ / 2 - 1; z++)
                            data[x][y][z] = new Block(2,0);

                        //data[0][y][SizeZ / 2 - 1] = new Block(2,0);
                        
                        //Gras on z=9
                        //if ((x > 0) && (x < SizeX-1))
                        data[x][y][SizeZ / 2 - 1] = new Block(1,0);
                        
                        if (y == SizeY-1)
                            data[x][y][SizeZ/2-1] = new Block(0,0);
                        
                        

                        //air from z=10 to 19
                        for (int z = SizeZ / 2; z < SizeZ; z++)
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
