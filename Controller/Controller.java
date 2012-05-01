package Controller;


import View.View;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Controller {
    public String name;
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    public static Chunk chunklist[] = new Chunk[9];
    static int clickstartX,clickstartY;
    public static int tilesizeX = 80;
    public static int tilesizeY = 40;
    public static int ChunkSizeX = 400;
    public static int ChunkSizeY = 400;

   
    final static Random RANDOM = new Random();
   
    /*Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
 
            while (!shouldExitGame) {
                //processKeys();
                //updateGameState();
                //drawScene();
                try {
                    TimeUnit.MILLISECONDS.sleep(15L);
                } catch (InterruptedException e) {
                }
            }
            //shutdown();
            
            
        }
    };*/
    
    public Controller(){ 
        //i und j fängt bei i/2+0,5 an und geht bis i/2-0,5
        //k von 0 bis feldgröße^2-1   
        
        
        //anzahl der Chunks. Muss ungerade Sein        
        int fieldsize = 3;
        int i=0;
        for (int y=0; y<fieldsize; y++){
            for (int x=0; x<fieldsize; x++){
                System.out.println(i+" has the coords: "+x+"|"+y);
                //chunklist[i] = new Chunk(x,y);
                i++;
            }
        }
        
        chunklist[0] = new Chunk(-1,1);
        chunklist[1] = new Chunk(0,1);
        chunklist[2] = new Chunk(1,1);
        chunklist[3] = new Chunk(-1,0);
        chunklist[4] = new Chunk(0,0);
        chunklist[5] = new Chunk(1,0);
        chunklist[6] = new Chunk(-1,-1);
        chunklist[7] = new Chunk(0,-1);
        chunklist[8] = new Chunk(1,-1); 
    }
    
    public class Chunk {
        public int coordX, coordY, posX, posY;

        //Konstruktor
        public Chunk(int X, int Y){
            //die größe der Chunks, hängt von der Auflösung ab
            coordX = X;
            coordY = Y;
            posX = X*ChunkSizeX;
            posY = -Y*ChunkSizeY;    
        }
        
       /* public static void draw(){
             hier hätte ich gerne eine Methode die mit einem Aufruf von Chunk.daw()
             in einer Schleife neun mal die drawChunk ausführt. Im moment in draw_all_Chunks zu finden
         }*/
    }      
    
    public static void mousePressed(MouseEvent e) {
        clickstartX = e.getX();
        clickstartY = e.getY();
    }
    
    public static void mouseDragged(MouseEvent e){
         for (int i=0;i<9;i++){
                    chunklist[i].posX -= clickstartX - e.getX();
                    chunklist[i].posY -= clickstartY - e.getY();
                }
                clickstartX = e.getX();
                clickstartY = e.getY();
                //draw_all_chunks();
                View.draw_all_chunks();
    }
    
    
}
