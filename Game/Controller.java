package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class Controller {
    public static View View;
    //amount of chunks. must be ungerade        
    private final int fieldsize = 3;
    public Block renderarray[][][] = new Block[Chunk.SizeX*fieldsize][Chunk.SizeY*fieldsize][Chunk.SizeZ];  
    public static Chunk chunklist[] = new Chunk[9];
    public float zoom = 1;
    
    private boolean changes = true;
    private GameContainer gc;
    
    //Constructor is called when entering the gamemode.
    public Controller(boolean loadmap, View pView,GameContainer container, StateBasedGame game) throws SlickException{
        //update resolution things
        GameplayState.iglog.add("W: "+container.getScreenWidth()+" H: "+container.getScreenHeight());
        Block.width = container.getScreenWidth()/Chunk.SizeX;
        Block.height = 4*container.getScreenHeight()/Chunk.SizeY;
        GameplayState.iglog.add("BlockWidth: "+Block.width+" BlockHeight: "+Block.height);
        
        Chunk.width = Chunk.SizeX*Block.width;
        Chunk.height = Chunk.SizeY*Block.height/2;
        GameplayState.iglog.add("Chunk.Width: "+Chunk.width+" Chunk.height: "+Chunk.height);

        View = pView;
        
        //fill chunklist with Chunks
        int i = 0;
        for (int y=(int) (fieldsize - Math.floor(fieldsize/2)-1); y > - fieldsize + Math.floor(fieldsize/2); y--)
            for (int x=(int) (-fieldsize+Math.floor(fieldsize/2)+1); x < fieldsize-Math.floor(fieldsize/2); x++){
                chunklist[i] = new Chunk(
                    x,
                    y,
                    x*Chunk.width,
                    -y*Chunk.height,
                    loadmap
                );
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
       
       GameplayState.iglog.update(delta);
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
        GameplayState.iglog.add("New Center: "+newcenter);
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
                    chunklist[i].posX + (newcenter == 3 ? -Chunk.width : (newcenter == 5 ?  Chunk.width: 0)),
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
        //fill renderarray with air
        for (int x=0;x <Chunk.SizeX*fieldsize;x++)
            for (int y=0;y <Chunk.SizeY*fieldsize;y++)
                for (int z=0;z <Chunk.SizeZ;z++)
                    renderarray[x][y][z] = new Block(0,0);
            
            
        //create big array out of all other arrays to performe the algorithm
        Block bigchunk[][][] = new Block[Chunk.SizeX*fieldsize][Chunk.SizeY*fieldsize][Chunk.SizeZ];
        
        for (int i=0;i <fieldsize*fieldsize;i++)
            for (int x=0;x <Chunk.SizeX;x++)
                for (int y=0;y <Chunk.SizeY;y++)
                    System.arraycopy(
                        chunklist[i].data[x][y],
                        0,
                        bigchunk[x+ Chunk.SizeX*(i%3)][y+ Chunk.SizeY*Math.abs(i/3)],
                        0,
                        Chunk.SizeZ
                    );
        
        //generate array which has render information in it. It filters every non visible block
        for (int i=0;i <fieldsize*fieldsize;i++)
            for (int x=0;x < bigchunk.length; x++)
                for (int y=0;y < bigchunk[0].length; y++){
                    //do raytracing until it found a not transparent block.
                    int tempx = x;
                    int tempy = y+2;
                    int tempz = Chunk.SizeZ;
                    
                    do {
                        tempy -= 2;
                        tempz--;                        
                    } while ((tempy >= 0) && (tempz>=0) && (bigchunk[tempx][tempy][tempz].transparent));
                    
                    //save the found block in renderarray. If there was none do nothing
                    if ((tempy >= 0) && (tempz>=0))
                        renderarray[tempx][tempy][tempz] = bigchunk[tempx][tempy][tempz];
                }
        changes = false;
    }

      
        
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoom = zoom+ zoom*(change/500f);
            if (zoom<0) zoom *= -1;
            
            Block.width = (int) (160*zoom);
            Block.height = (int) (160*zoom);
            Chunk.width = (int) (Chunk.SizeX*Block.width*zoom);
            Chunk.height = (int) (Chunk.SizeY*Block.height*zoom/2);
            
            GameplayState.iglog.add("Zoom:"+zoom+" Chunk.width:"+Chunk.width+" Chunk.height"+Chunk.height);   
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
                chunklist[i].posX += newx - oldx;
                chunklist[i].posY += newy - oldy ;
            }
            
            //if the middle chunk is scrolled down over the middle line then 
            //GameplayState.iglog.add("Chunk.width: "+String.valueOf(Chunk.width));
            //GameplayState.iglog.add("chunk: "+String.valueOf(chunklist[4].posX));
            if (chunklist[4].posX > Chunk.width/2)
                setcenterchunk(3);   
            
            
            if (chunklist[4].posX < -Chunk.width/2) 
                setcenterchunk(5);   
            
            
            if (chunklist[4].posY > Chunk.height/2) 
                setcenterchunk(1);   
            
            
            if (chunklist[4].posY < -Chunk.height/2)
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