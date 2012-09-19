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
    
    private View view;
    private GameContainer gc;
    private StateBasedGame sbg;
    private int oldx;
    private int oldy;
    private float zoomx = 1;
    
    
    //Constructor is called when entering the gamemode.
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
        sbg = game;
          
        gc.getInput().addMouseListener(new MouseDraggedListener());
        map = new Map(MainMenuState.loadmap);
        map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19] = new Player((int) (Chunk.BlocksX*1.5),(int) (Chunk.BlocksY*1.5),19);
        player = (Player) map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19];
        minimap = new Minimap(gc);
    }
    
    
    public void giveView(View view) {
         this.view = view;
    }
    
    /* Main method which is called every time */
    public void update(int delta) throws SlickException{  

        Input input = gc.getInput();
 
        if (input.isKeyDown(Input.KEY_Q)) gc.exit();
       
        //open menu
        if (input.isKeyPressed(Input.KEY_ESCAPE)) openmenu();
       
        //toggle fullscreen
        if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 
       
        //pause
        if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

        //good graphics
        if (input.isKeyPressed(Input.KEY_G)) {
            goodgraphics = !goodgraphics;
            GameplayState.iglog.add("Good Graphics is now "+goodgraphics);
        }
        
        //toggle camera
        if (input.isKeyPressed(Input.KEY_C)) view.cameramode = !view.cameramode;
        
        //restart
        if (input.isKeyPressed(Input.KEY_N)) map = new Map(false);
                
        //reset zoom
        if (input.isKeyPressed(Input.KEY_Z)) {
            view.setzoom(1);
            GameplayState.iglog.add("Zoom reset");
        }
        
        
        //walk
        if (input.isKeyDown(Input.KEY_W)) {player.walk(1,delta);}
        if (input.isKeyDown(Input.KEY_S)) {player.walk(7,delta);}
        if (input.isKeyDown(Input.KEY_A)) {player.walk(3,delta);}
        if (input.isKeyDown(Input.KEY_D)) {player.walk(5,delta);}
        if (input.isKeyPressed(Input.KEY_SPACE)) player.jump();
       
       

        //earth to right
        if (view.cameraX < Chunk.SizeX/3)
           map.setCenter(3);
        else {       
            //earth to the left
            if (view.cameraX + view.cameraWidth > 8*Chunk.SizeX/3) 
                map.setCenter(5); 
        }
        
       //scroll up, earth down            
        if (view.cameraY  <= 0) 
            map.setCenter(1);
        else {
            //scroll down, earth up
            if (view.cameraY+view.cameraHeight > Chunk.SizeY*3)
                map.setCenter(7);
            }
        
        //camera
        oldx = view.cameraX;
        oldy = view.cameraY;
        
        player.update(delta);
        
        if (view.cameramode == false) {
            view.cameraX = player.getRelCoordX()*Block.width - view.cameraWidth / 2;
            view.cameraY = player.getRelCoordY()*Block.height/2 - (player.coordZ*Block.height/2) - view.cameraHeight;
        } 
        
        map.posX -= view.cameraX - oldx;
        map.posY -= view.cameraY - oldy;
            

       
        
        //do raytracing
        if (map.changes) {
            view.raytracing();
            view.calc_light();
        }
        
       
        GameplayState.iglog.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoomx = zoomx + change/1000f;
            view.setzoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            view.cameraWidth = (int) (gc.getScreenWidth() / view.getzoom());
            view.cameraHeight= (int) (gc.getScreenHeight() / view.getzoom());
            
           /* Block.width =(int) (gc.getScreenWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getScreenHeight()*zoom / Chunk.BlocksY);
            Chunk.SizeX = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SizeY = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            GameplayState.iglog.add("Zoom: "+view.getzoom()+" Chunk.SizeX: "+Chunk.SizeX+" Chunk.SizeY: "+Chunk.SizeY);   
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
            
            if (view.cameramode) {
                view.cameraX += newx-oldx;
                view.cameraY += newy-oldy;
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