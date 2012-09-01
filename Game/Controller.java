package Game;

import MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Benedikt
 */
public class Controller implements MouseListener {
    /**
     *The list which has all nine chunks in it.
     */
    public static Chunk chunklist[] = new Chunk[9];

    public static boolean changes;
    public Player Player;   
    public boolean goodgraphics = true;
    
    private View View;
    private GameContainer gc;
    private StateBasedGame sbg;
    private int oldx;
    private int oldy;
    
    //Constructor is called when entering the gamemode.
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
        sbg = game;
          
        gc.getInput().addMouseListener(this);
        fill_chunklist(MainMenuState.loadmap);
        Player = new Player(6,14,19);
    }
    
    private void fill_chunklist(boolean loadmap){
     
        //fill chunklist with Chunks
        int i = 0;
        
        for (int y=1; y > - 2; y--)
            for (int x=-1; x < 2; x++){
                chunklist[i] = new Chunk(
                    x,
                    y,
                    x*Chunk.SizeX,
                    -y*Chunk.SizeY,
                    loadmap
                );
                i++;               
            }
        changes = true;
    }
    
    public void giveView(View pView) {
        View = pView;
    }
    
    /* Main method which is called every time */
    public void update(int delta) throws SlickException{  
        oldx = View.cameraX;
        oldy = View.cameraY;
        
        
        
        Input input = gc.getInput();
 
        
        if (input.isKeyDown(Input.KEY_Q)) gc.exit();
       
        //open menu
        if (input.isKeyPressed(Input.KEY_ESCAPE)) openmenu();
       
        //toggle fullscreen
        if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 
       
        //pause
        if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

        //good graphics
        if (input.isKeyPressed(Input.KEY_G)) goodgraphics = !goodgraphics;
        
        //restart
        if (input.isKeyPressed(Input.KEY_C)) View.cameramode = !View.cameramode;
        
        //restart
        if (input.isKeyPressed(Input.KEY_N)) fill_chunklist(false);
                
        
        //walk
        if (input.isKeyDown(Input.KEY_UP)) {Player.walk(1);}
        if (input.isKeyDown(Input.KEY_DOWN)) {Player.walk(7);}
        if (input.isKeyDown(Input.KEY_LEFT)) {Player.walk(3);}
        if (input.isKeyDown(Input.KEY_RIGHT)) {Player.walk(5);}
        if (input.isKeyPressed(Input.KEY_SPACE)) Player.jump();
       
        Player.update();
        
        if (View.cameramode == false) {
            View.cameraX = (Player.coordX)*Block.width - View.cameraWidth;
            View.cameraY = Player.coordY*Block.height/2 - (Player.coordZ*Block.height/2) - View.cameraHeight;
        }
       
        for (int i=0;i<9;i++){
           chunklist[i].posX -= View.cameraX - oldx;
           chunklist[i].posY -= View.cameraY - oldy;
        }
       
        
        //earth to right
        if (View.cameraX - Chunk.SizeX*chunklist[4].coordX < -Chunk.SizeX/2)
           setcenterchunk(3);   
            
        //earth to the left
        if (View.cameraX + View.cameraWidth - Chunk.SizeX*chunklist[4].coordX  > 3*Chunk.SizeX/2) 
           setcenterchunk(5);   

        ///scroll up, earth down
        if (View.cameraY - (Chunk.SizeY + Chunk.SizeZ)*chunklist[4].coordY > 3*(Chunk.SizeY) / 2) 
           setcenterchunk(1);   

        //scroll down, earth up
        if (View.cameraY-View.cameraHeight - (Chunk.SizeY+Chunk.SizeZ)*chunklist[4].coordY < -3*(Chunk.SizeY) / 2)
           setcenterchunk(7); 
           
        
        //do raytracing
        if (changes) View.raytracing();
       
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
        
        GameplayState.iglog.add("New Center: "+newcenter);
        Chunk chunklist_copy[] = new Chunk[9];
        System.arraycopy(chunklist, 0, chunklist_copy, 0, 9);
        for (int pos=0; pos<9; pos++){
            if (check_chunk_movement(pos,newcenter)){
                //System.out.println("chunkliste[" + i + "] bekommt den alten von chunkliste["+ (i + newcenter - 4) +"]");
                chunklist[pos] = chunklist_copy[pos-4+newcenter];   
            } else {
                chunklist[pos] = new Chunk(
                    chunklist[pos].coordX + (newcenter == 3 ? -1 : (newcenter == 5 ? 1 : 0)),
                    chunklist[pos].coordY + (newcenter == 1 ? 1 : (newcenter == 7 ? -1 : 0)),
                    chunklist[pos].posX + (int) ((newcenter == 3 ? -Chunk.SizeX : (newcenter == 5 ?  Chunk.SizeX: 0))),
                    chunklist[pos].posY + (int) ((newcenter == 1 ? -Chunk.SizeY : (newcenter == 7 ? Chunk.SizeY : 0))),
                   MainMenuState.loadmap
                );
                //System.out.println("chunkliste["+i+"] bekommt einen neuen Chunk: "+ chunklist[i].coordX +","+chunklist[i].coordY);
                //System.out.println("Pos: "+ chunklist[i].posX +","+ chunklist[i].posY);
            }
        }
        changes = true;
    }
    
    private boolean check_chunk_movement(int pos, int movement){
        //checks if the number can be reached by moving the net in a direction, very complicated
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
    
    
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            View.zoom = View.zoom+ View.zoom*(change/500f);
            if (View.zoom<0) View.zoom *= -1;
            
           /* Block.width =(int) (gc.getScreenWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getScreenHeight()*zoom / Chunk.BlocksY);
            Chunk.SizeX = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SizeY = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            GameplayState.iglog.add("Zoom: "+View.zoom+" Chunk.SizeX: "+Chunk.SizeX+" Chunk.SizeY: "+Chunk.SizeY);   
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
            //workaround for the bug, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            if (View.cameramode) {
                View.cameraX += newx-oldx;
                View.cameraY += newy-oldy;
            }
            /*for (int i=0;i<9;i++){
                chunklist[i].posX += newx - oldx;
                chunklist[i].posY += newy - oldy ;
            }*/
            
            //if the middle chunk is scrolled down over the middle line then 
            //GameplayState.iglog.add("Chunk.SizeX: "+String.valueOf(Chunk.SizeX));
            //GameplayState.iglog.add("chunk: "+String.valueOf(chunklist[4].posX));
            
    
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
    
    
    private void openmenu(){
        boolean openmenu = true;
    }
}