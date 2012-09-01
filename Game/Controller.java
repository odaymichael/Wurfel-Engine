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
public class Controller {
    /**
     *The list which has all current nine chunks in it.
     */
    public static Map map;
    public static Player player;   
    public boolean goodgraphics = true;
    public Minimap minimap;
    
    private View View;
    private GameContainer gc;
    private StateBasedGame sbg;
    private int oldx;
    private int oldy;
    
    
    //Constructor is called when entering the gamemode.
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
        sbg = game;
          
        gc.getInput().addMouseListener(new MouseDraggedListener());
        map = new Map(MainMenuState.loadmap);
        player = new Player((int) (Chunk.BlocksX*1.5),(int) (Chunk.BlocksY*1.5),19);
        minimap = new Minimap(gc);
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
        if (input.isKeyPressed(Input.KEY_N)) map = new Map(false);
                
        
        //walk
        if (input.isKeyDown(Input.KEY_UP)) {player.walk(1);}
        if (input.isKeyDown(Input.KEY_DOWN)) {player.walk(7);}
        if (input.isKeyDown(Input.KEY_LEFT)) {player.walk(3);}
        if (input.isKeyDown(Input.KEY_RIGHT)) {player.walk(5);}
        if (input.isKeyPressed(Input.KEY_SPACE)) player.jump();
       
        player.update();
        
        if (View.cameramode == false) {
            View.cameraX = player.relCoordX()*Block.width - View.cameraWidth / 2;
            View.cameraY = player.relCoordY()*Block.height/2 - (player.coordZ*Block.height/2) - View.cameraHeight;
        }
       
        map.posX -= View.cameraX - oldx;
        map.posY -= View.cameraY - oldy;
        
       
        
        //earth to right
        if (View.cameraX - Chunk.SizeX*map.coordlistX[4] < -Chunk.SizeX/2)
           map.setCenter(3);  
            
        /*
         * //earth to the left
        if (View.cameraX + View.cameraWidth - Chunk.SizeX*map.coordlistX[4]  > 3*Chunk.SizeX/2) 
           map.setcenterchunk(5);   
*/
        ///scroll up, earth down
        /*if (View.cameraY - (Chunk.SizeY + Chunk.SizeZ)*map.coordlistY[4] > 3*(Chunk.SizeY) / 2) 
           map.setcenterchunk(1);   

        //scroll down, earth up
        if (View.cameraY-View.cameraHeight - (Chunk.SizeY+Chunk.SizeZ)*map.coordlistY[4] < -3*(Chunk.SizeY) / 2)
           map.setcenterchunk(7); 
          */ 
        
        //do raytracing
        if (map.changes) View.raytracing();
       
        GameplayState.iglog.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
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
    }
    
    private void openmenu(){
        boolean openmenu = true;
    }
}