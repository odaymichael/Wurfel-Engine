package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class Controller {
    public static View View;
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    //anzahl der Chunks. Muss ungerade Sein        
    final int fieldsize = 3;
    public Block renderarray[][] = new Block[ChunkSizeX*fieldsize][ChunkSizeY*fieldsize];  
    public static Chunk chunklist[] = new Chunk[9];
    public static int blockSizeX = 80;
    public static int blockSizeY = 80;
    public final static int ChunkSizeX = 9;
    public final static int ChunkSizeY = 20;
    public final static int ChunkSizeZ = 20;
    public float zoom = 1;
    private boolean changes = true;
    private GameContainer gc;
    
    //Constructor is called when entering the gamemode.
    public Controller(boolean loadmap, View pView,GameContainer container, StateBasedGame game) throws SlickException{
        GameplayState.iglog.add("Controller meldet sich zu diensten!");
        
        GameplayState.iglog.add("W:"+container.getScreenWidth()+" H:"+container.getScreenHeight());
        blockSizeX = container.getScreenWidth()/ChunkSizeX;
        blockSizeY = 4*container.getScreenHeight()/ChunkSizeY;
        GameplayState.iglog.add("BlockWidth:"+blockSizeX+" BlockHeight:"+blockSizeY);
        

        View = pView;
        
        //fill chunklist with Chunks
        int i = 0;
        for (int y=(int) (fieldsize - Math.floor(fieldsize/2)-1); y > - fieldsize + Math.floor(fieldsize/2); y--)
            for (int x=(int) (-fieldsize+Math.floor(fieldsize/2)+1); x < fieldsize-Math.floor(fieldsize/2); x++){
                chunklist[i] = new Chunk(x, y, x*Chunk.width, (-y)*Chunk.height,loadmap);
                i++;               
        }
    }
    
    public void update(GameContainer pgc, StateBasedGame sbg, int delta) throws SlickException{
       gc = pgc;
       
       Input input = gc.getInput();
 
       int mouseX = input.getMouseX();
       int mouseY = input.getMouseY();
       gc.getInput().addMouseListener(new MouseDraggedListener());
       

       if (gc.getInput().isKeyDown(Input.KEY_Q)) gc.exit();
       
       //open menu
       if (gc.getInput().isKeyDown(Input.KEY_ESCAPE)) openmenu();
       
       
       //toggle fullscreen
       if (gc.getInput().isKeyDown(Input.KEY_F)) ((AppGameContainer) gc).setFullscreen(!((AppGameContainer) gc).isFullscreen()); 
       
       //do raytracing
       if (changes) raytracing();
       
       GameplayState.iglog.update();
        
    }

      
    private void setcenterchunk(int newcenter){
        //newcenter is 1,3,5 or 7
        /*
          |0|1|2|
          -------
          |3|4|5|
          -------
          |6|7|8|
         
         */
        //System.out.println("New Center: " +newcenter);
        Chunk chunklist_copy[] = new Chunk[9];
        System.arraycopy(chunklist, 0, chunklist_copy, 0, 9);
        for (int i=0;i<9;i++){
            if (check_chunk_movement(i,newcenter)){
                //System.out.println("chunkliste[" + i + "] bekommt den alten von chunkliste["+ (i + newcenter - 4) +"]");
                chunklist[i] = chunklist_copy[i-4+newcenter];   
            } else {
                chunklist[i] = new Chunk(
                    chunklist[i].coordX + (newcenter == 3 ? -1 : (newcenter == 5 ? 1 : 0)),
                    chunklist[i].coordY + (newcenter == 1 ? 1 : (newcenter == 7 ? -1 : 0)),
                    chunklist[i].posX + (newcenter == 3 ? -Chunk.width : (newcenter == 5 ? Chunk.width : 0)),
                    chunklist[i].posY + (newcenter == 1 ? -Chunk.height : (newcenter == 7 ? Chunk.height : 0)),
                    false
                );
                //System.out.println("chunkliste["+i+"] bekommt einen neuen Chunk: "+ chunklist[i].coordX +","+chunklist[i].coordY);
                //System.out.println("Pos: "+ chunklist[i].posX +","+ chunklist[i].posY);
            }
        }
    }
    
    private boolean check_chunk_movement(int pos, int movement){
        //checks if the number can be reached be moving the net in a direction, very complicated
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

    
    public void raytracing(){
        //create big array out of all other arrays to performe the algorithm
        Block ninechunks[][][] = new Block[ChunkSizeX*fieldsize][ChunkSizeY*fieldsize][ChunkSizeZ];
        
        for (int i=0;i <fieldsize*fieldsize;i++)
            for (int x=0;x <ChunkSizeX;x++)
                for (int y=0;y <ChunkSizeY;y++)
                    System.arraycopy(
                        chunklist[i].data[x][y],
                        0,
                        ninechunks[x+ ChunkSizeX*(i%3)][y+ ChunkSizeY*Math.abs(i/3)],
                        0,
                        ChunkSizeZ
                    );
        
        //generate  array which has render information in it. Now only 2D, later with half transparent block 3D.
        //raytracing
        for (int i=0;i <fieldsize*fieldsize;i++)
            for (int x=0;x < ninechunks.length; x++)
                for (int y=0;y < ninechunks[0].length; y++){
                    //do raytracing until it found a not transparent block.
                    int tempx = x;
                    int tempy = y;
                    int tempz = ChunkSizeZ;
                    do {
                        tempz--;
                        tempy -= 2;
                        
                    } while ((tempy > 0) && (tempz>0) && (ninechunks[tempx][tempy][tempz].transparent));
                    
                    //save the found block in renderarray. If there was none take the highest.
                    Block visibleblock;
                    if ((tempy <= 0) || (tempz<=0))
                        visibleblock = ninechunks[x][y][ChunkSizeZ-1];
                    else visibleblock = ninechunks[tempx][tempy][tempz];
                    renderarray[x][y] = visibleblock;
                }
        changes = false;
    }

      
        
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            System.out.println("change:"+change);
             System.out.println("change/500:"+change/500f);
            zoom = zoom+ zoom*(change/500f);
            if (zoom<0) zoom *= -1;
            System.out.println(zoom);
            GameplayState.iglog.add("Zoom:"+zoom);
        }

        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        }

        @Override
        public void mousePressed(int button, int x, int y) {
        }

        @Override
        public void mouseReleased(int button, int x, int y) {
            
        }

        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug
            gc.getInput().consumeEvent();
            
            //Bug. Is called several times
            for (int i=0;i<9;i++){
                chunklist[i].posX += newx -oldx;
                chunklist[i].posY += newy -oldy ;
                
            }
            
            //if the middle chunk is scrolled down over the middle line then 
            if (chunklist[4].posX > gc.getScreenWidth()/2) 
                setcenterchunk(3);   
            
            
            if (chunklist[4].posX < -Chunk.width) 
                setcenterchunk(5);   
            
            
            if (chunklist[4].posY > gc.getScreenHeight()/2) 
                setcenterchunk(1);   
            
            
            if (chunklist[4].posY < -Chunk.height)
                setcenterchunk(7);     
        }

        @Override
        public void setInput(Input input) {
        }

        @Override
        public boolean isAcceptingInput() {
            return true;
        }

        @Override
        public void inputEnded() {
          
        }

        @Override
        public void inputStarted() {
        }
    }
    
    private void openmenu(){
        boolean openmenu = true;
    }
}