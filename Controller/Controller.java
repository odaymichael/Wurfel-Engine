package Controller;


import View.View;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

public class Controller {
    public static View View;
            
    public String name;
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    //anzahl der Chunks. Muss ungerade Sein        
    final int fieldsize = 3;
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
        Controller.View = new View();
        addListener();
        int i=0;
        for (int y=(int) (fieldsize-Math.floor(fieldsize/2)-1); y>-fieldsize+Math.floor(fieldsize/2); y--)
            for (int x=(int) (-fieldsize+Math.floor(fieldsize/2)+1); x<fieldsize-Math.floor(fieldsize/2); x++){
                chunklist[i] = new Chunk(x,y,x*ChunkSizeX,y*ChunkSizeY);
                i++;
            }

    }
    
    public class Chunk {
        public int coordX, coordY, posX, posY;
        public boolean chunkdata[][] = new boolean[20][20];

        //Konstruktor
        public Chunk(int X, int Y, int startposX, int startposY){
            //die größe der Chunks, hängt von der Auflösung ab
            coordX = X;
            coordY = Y;
            posX = startposX;
            posY = startposY;
            
            //chunkdata will contain the blocks and objects
            for (int i=0;i<chunkdata.length;i++)
                for (int j=0;j<chunkdata.length;j++)
                    chunkdata[i][j] = new Random().nextBoolean();
        }
        
       /* public static void draw(){
             hier hätte ich gerne eine Methode die mit einem Aufruf von Chunk.daw()
             in einer Schleife neun mal die drawChunk ausführt. Im moment in draw_all_Chunks zu finden
         }*/

    }
    
     
    
    private void addListener(){
        Controller.View.setMouseDraggedListener(new MouseDraggedListener());
        Controller.View.setMousePressedListener(new MousePressedListener());
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
            
            //if middle chunk is scrolled down over the middle then 
            if (chunklist[4].posX > 400) 
                setcenterchunk(3);   
            
            
            if (chunklist[4].posX < 0) 
                setcenterchunk(5);   
            
            
            if (chunklist[4].posY > 400) 
                setcenterchunk(1);   
            
            
            if (chunklist[4].posX < 0)
                setcenterchunk(7);   
            
            
            
            Controller.View.draw_all_chunks(); 
        }
    }
    
    private void setcenterchunk(int newcenter){
        //newcenter ist 1,3,5 oder 7
        System.out.println("New Center: " +newcenter);
        for (int i=0;i<8;i++){
            if ( (i-newcenter)%3 == 0){
                chunklist[i] = new Chunk(
                    chunklist[i].coordX + (newcenter==3 ? -1 : (newcenter==5 ? 1 : 0)),
                    chunklist[i].coordY + (newcenter==1 ? 1 : (newcenter==7 ? -1 : 0)),
                    chunklist[i].posX + (newcenter==3 ? ChunkSizeX : (newcenter==5 ? -ChunkSizeX : 0)),
                    chunklist[i].posY + (newcenter==1 ? ChunkSizeY : (newcenter==7 ? -ChunkSizeY : 0))
                    );
            System.out.println("Neuer Chunk an chunkliste["+i+"]: "+chunklist[i].coordX+","+chunklist[i].coordY);
            System.out.println("Pos: "+chunklist[i].posX+","+chunklist[i].posY);
            } else
                chunklist[i] = chunklist[i+newcenter-4];
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
