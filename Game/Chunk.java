package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chunk {
        public int coordX, coordY, posX, posY;
        public static int width;
        public static int height;
        public static int SizeX = 12;//16:9
        public static int SizeY = 27;
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
            
            if (loadmap) 
                loadChunk();
            else
                newChunk();
        }
       
        private void newChunk(){
            //chunkdata will contain the blocks and objects
            //alternative to chunkdata.length ChunkSize
            
            System.out.println("Neu:"+coordX+","+ coordY);
            for (int x=0; x < SizeX; x++)
                for (int y=0; y < SizeY; y++){
                    //Dirt from 0 to 8
                    for (int z=0; z < SizeZ / 2 - 1; z++){
                        //for (int z=0; z < SizeZ-4; z++)
                        data[x][y][z] = new Block(2,0);
                        // if ((z>10) && (x>10) && (y>5))
                        //  data[x][y][z] = new Block (9,0);
                     }

                    //data[0][y][SizeZ / 2 - 1] = new Block(2,0);
                        
                       
                    //Gras on z=9
                    //if ((x > 0) && (x < SizeX-1) && (y>0))                        
                    data[x][y][SizeZ/2-1] = new Block(1,0);
                    data[x][y][SizeZ/2] = new Block(9,0);
                        
                        
                    /*
                    if (y == SizeY-1)
                        data[x][y][SizeZ/2-1] = new Block(0,0);
                        
                        

                    //air from z=10 to 19
                    for (int z = SizeZ / 2; z < SizeZ; z++)
                        data[x][y][z] = new Block(0,0);
                            
                    */
                    }
        }
        
       private void loadChunk(){
            /*
            //to find root path of project
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
                if (new File("build/classes/map1/chunk"+coordX+","+coordY+".otmc").exists()) {
                    System.out.println("Load:"+coordX+","+ coordY);
                    FileReader input = new FileReader("build/classes/map1/chunk"+coordX+","+coordY+".otmc");
                    BufferedReader bufRead = new BufferedReader(input);

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
                                            (int) Integer.parseInt(line.substring(posdots+1,posend))
                                            );
                                x++;
                                line.delete(0,posend+1);
                            } while (x < SizeX);
                            
                            line = new StringBuilder();
                            line.append(bufRead.readLine());
                            
                            y++;
                        }while (y < SizeY);
                        lastline = bufRead.readLine();
                        z++;
                    }while (lastline != null);
                } else {
                    newChunk();
                }
                
            } catch (IOException ex) {
                
            }
       }
}
