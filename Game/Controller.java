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
    /**
     * Contains the player
     */
    public static Player player;   
   
   /**
     * Should the graphic be a bit slower but better? Must be in Controller because is needed for e.g. the Block and there used as data
     */
    public boolean goodgraphics = true;
    
    public GameContainer gc;
    private StateBasedGame sbg;
    private int oldx;
    private int oldy;
    private float zoomx = 1;
    
    /**
     * Constructor is called when entering the gamemode.
     * @param container
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
        sbg = game;
          
        gc.getInput().addMouseListener(new MouseDraggedListener());
        map = new Map(MainMenuState.loadmap);
        gameConstructor();
    }
    
    /**
     *Later this method will be abstract to split into engine and game
     */
    private void gameConstructor() throws SlickException{
        map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19] = new Player((int) (Chunk.BlocksX*1.5),(int) (Chunk.BlocksY*1.5),19);
        //the player is created but must be saved into the field
        player = (Player) map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19];
    }
    
    

    
    /**
     * Main method which is called every time
     * @param delta
     * @throws SlickException
     */
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
            GameplayState.Controller.goodgraphics = !GameplayState.Controller.goodgraphics;
            GameplayState.iglog.add("Good Graphics is now "+GameplayState.Controller.goodgraphics);
            Block.reloadSprites(GameplayState.View.getZoom());
        }
        
        //toggle camera
        if (input.isKeyPressed(Input.KEY_C)) GameplayState.View.cameramode = !GameplayState.View.cameramode;
        
        //restart
        if (input.isKeyPressed(Input.KEY_N)) map = new Map(false);
                
        //reset zoom
        if (input.isKeyPressed(Input.KEY_Z)) {
            GameplayState.View.setZoom(1);
            GameplayState.iglog.add("Zoom reset");
        }
        
        
        //walk
        if ("WASD".equals(player.getControls())){
            if (input.isKeyDown(Input.KEY_W)) player.walk(1,delta);
            if (input.isKeyDown(Input.KEY_S)) player.walk(7,delta);
            if (input.isKeyDown(Input.KEY_A)) player.walk(3,delta);
            if (input.isKeyDown(Input.KEY_D)) player.walk(5,delta);
            if (input.isKeyPressed(Input.KEY_SPACE)) player.jump();
        }
       
       

        //earth to right
        if (GameplayState.View.cameraX < Chunk.SizeX/3)
           map.setCenter(3);
        else {       
            //earth to the left
            if (GameplayState.View.cameraX + GameplayState.View.cameraWidth > 8*Chunk.SizeX/3) 
                map.setCenter(5); 
        }
        
       //scroll up, earth down            
        if (GameplayState.View.cameraY  <= 0) {
            map.setCenter(1);
        } else {
            //scroll down, earth up
            if (GameplayState.View.cameraY+GameplayState.View.cameraHeight > Chunk.SizeY*3)
                map.setCenter(7);
        }
        
        //camera
        oldx = GameplayState.View.cameraX;
        oldy = GameplayState.View.cameraY;
        
        player.update(delta);
        
        if (GameplayState.View.cameramode == false) {
            GameplayState.View.cameraX = player.getRelCoordX() * Block.width + Block.width / 2 *(player.getRelCoordY() % 2) + player.getOffsetX() - GameplayState.View.cameraWidth / 2;
            GameplayState.View.cameraY = (player.getRelCoordY() - player.coordZ) * Block.height/2 + player.getOffsetY()*2 - GameplayState.View.cameraHeight;
        } 
        
        map.data[player.getRelCoordX()][player.getRelCoordY()][player.coordZ] = player;
        map.posX -= GameplayState.View.cameraX - oldx;
        map.posY -= GameplayState.View.cameraY - oldy;
            


        //do raytracing
        if (map.changes) {
            GameplayState.View.raytracing();
            GameplayState.View.calc_light();
        }        
       
        //update the log
        GameplayState.iglog.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoomx = zoomx + change/1000f;
            GameplayState.View.setZoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            GameplayState.View.cameraWidth = (int) (gc.getWidth() / GameplayState.View.getZoom());
            GameplayState.View.cameraHeight= (int) (gc.getHeight() / GameplayState.View.getZoom());
            
           /* Block.width =(int) (gc.getWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getHeight()*zoom / Chunk.BlocksY);
            Chunk.SizeX = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SizeY = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            GameplayState.iglog.add("Zoom: "+GameplayState.View.getZoom()+" Chunk.SizeX: "+Chunk.SizeX+" Chunk.SizeY: "+Chunk.SizeY);   
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
            
            if (GameplayState.View.cameramode) {
                GameplayState.View.cameraX += newx-oldx;
                GameplayState.View.cameraY += newy-oldy;
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