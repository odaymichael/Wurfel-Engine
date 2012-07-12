package BombingGames.Controller;

import BombingGames.View.GameView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.Random;


public class GameController {
    public static GameView GameView;
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    //anzahl der Chunks. Muss ungerade Sein        
    final int fieldsize = 3;
    public static Chunk chunklist[] = new Chunk[9];
    static int clickstartX,clickstartY;
    public final static int tilesizeX = 80;
    public final static int tilesizeY = 40;
    public final static int tilesizeZ = 80;
    public final static int ChunkSizeX = 9;
    public final static int ChunkSizeY = 20;
    public final static int ChunkSizeZ = 20; 
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
    
    
    //Constructor
    public GameController(boolean loadmap) throws IOException{
        GameController.GameView = new GameView();
        addListener();
        int i = 0;
        //chunklist mit Chunks füllen
        for (int y=(int) (fieldsize - Math.floor(fieldsize/2)-1); y > - fieldsize + Math.floor(fieldsize/2); y--)
            for (int x=(int) (-fieldsize+Math.floor(fieldsize/2)+1); x < fieldsize-Math.floor(fieldsize/2); x++){
                chunklist[i] = new Chunk(x, y, x*Chunk.width, (-y)*Chunk.height,loadmap);
                i++;               
            }
    }
    
    private void addListener(){
        GameController.GameView.setMouseDraggedListener(new MouseDraggedListener());
        GameController.GameView.setMousePressedListener(new MousePressedListener());
    }
    
      
    private void setcenterchunk(int newcenter){
        //newcenter ist 1,3,5 oder 7
        System.out.println("New Center: " +newcenter);
        Chunk chunklist_copy[] = new Chunk[9];
        System.arraycopy(chunklist, 0, chunklist_copy, 0, 9);
        for (int i=0;i<9;i++){
            if ( (i-newcenter)%3 == 0 || i + newcenter-4<0){
                chunklist[i] = new Chunk(
                    chunklist[i].coordX + (newcenter == 3 ? -1 : (newcenter == 5 ? 1 : 0)),
                    chunklist[i].coordY + (newcenter == 1 ? 1 : (newcenter == 7 ? -1 : 0)),
                    chunklist[i].posX + (newcenter == 3 ? -Chunk.width : (newcenter == 5 ? Chunk.width : 0)),
                    chunklist[i].posY + (newcenter == 1 ? ChunkSizeY : (newcenter == 7 ? -Chunk.height : 0)),
                    false
                );
            System.out.println("chunkliste["+i+"] bekommt einen neuen Chunk: "+ chunklist[i].coordX +","+chunklist[i].coordY);
            System.out.println("Pos: "+ chunklist[i].posX +","+ chunklist[i].posY);
            } else {
                System.out.println("chunkliste[" + i + "] bekommt den alten von chunkliste["+ (i + newcenter - 4) +"]");
                chunklist[i] = chunklist_copy[i + newcenter - 4];
            }
        }
    }
        
   
        
    class MouseDraggedListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e){
            for (int i=0;i<9;i++){
                chunklist[i].posX -= clickstartX - e.getX();
                chunklist[i].posY -= clickstartY - e.getY();
            }
            clickstartX = e.getX();
            clickstartY = e.getY();
            
            //if the middle chunk is scrolled down over the middle line then 
            if (chunklist[4].posX > 400) 
                setcenterchunk(3);   
            
            
            if (chunklist[4].posX < -BombingGames.Controller.Chunk.width) 
                setcenterchunk(5);   
            
            
            if (chunklist[4].posY > 400) 
                setcenterchunk(1);   
            
            
            if (chunklist[4].posY < -BombingGames.Controller.Chunk.height)
                setcenterchunk(7);   
            

              GameView.draw_all_chunks();
              //View.drawChunk(chunklist[4]);
            
        }
    }
    
  
     class MousePressedListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            clickstartX = e.getX();
            clickstartY = e.getY();
        }
    }
}