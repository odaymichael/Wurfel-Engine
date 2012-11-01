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
    public Player player;   
   
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
        player = (Player) map.data[(int) (Chunk.BlocksX*1.5)][(int) (Chunk.BlocksY*1.5)][19];
        //the player is created but must be saved into the field

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
            Gameplay.controller.goodgraphics = !Gameplay.controller.goodgraphics;
            Gameplay.iglog.add("Good Graphics is now "+Gameplay.controller.goodgraphics);
            Block.reloadSprites(Gameplay.view.getZoom());
        }
        
        //toggle camera
        if (input.isKeyPressed(Input.KEY_C)) Gameplay.view.cameramode = !Gameplay.view.cameramode;
        
        //restart
        if (input.isKeyPressed(Input.KEY_N)) map = new Map(false);
                
        //reset zoom
        if (input.isKeyPressed(Input.KEY_Z)) {
            Gameplay.view.setZoom(1);
            Gameplay.iglog.add("Zoom reset");
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
        if (Gameplay.view.cameraX < Chunk.SizeX/3)
           map.setCenter(3);
        else {       
            //earth to the left
            if (Gameplay.view.cameraX + Gameplay.view.cameraWidth > 8*Chunk.SizeX/3) 
                map.setCenter(5); 
        }
        
       //scroll up, earth down            
        if (Gameplay.view.cameraY  <= 0) {
            map.setCenter(1);
        } else {
            //scroll down, earth up
            if (Gameplay.view.cameraY+Gameplay.view.cameraHeight > Chunk.SizeY*3)
                map.setCenter(7);
        }
        
        //camera
        oldx = Gameplay.view.cameraX;
        oldy = Gameplay.view.cameraY;
        
        player.update(delta);
        
        if (Gameplay.view.cameramode == false) {
            Gameplay.view.cameraX = player.getRelCoordX() * Block.width + Block.width / 2 *(player.getRelCoordY() % 2) + player.getOffsetX() - Gameplay.view.cameraWidth / 2;
            Gameplay.view.cameraY = (player.getRelCoordY() - player.coordZ) * Block.height/2 + player.getOffsetY()*2 - Gameplay.view.cameraHeight;
        } 
        
        map.data[player.getRelCoordX()][player.getRelCoordY()][player.coordZ] = player;
        map.changePosX(- Gameplay.view.cameraX + oldx);
        map.changePosY(-Gameplay.view.cameraY + oldy);
            


        //recalc if requested
        map.recalcIfRequested();      
       
        //update the log
        Gameplay.iglog.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoomx = zoomx + change/1000f;
            Gameplay.view.setZoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            Gameplay.view.cameraWidth = (int) (gc.getWidth() / Gameplay.view.getZoom());
            Gameplay.view.cameraHeight= (int) (gc.getHeight() / Gameplay.view.getZoom());
            
           /* Block.width =(int) (gc.getWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getHeight()*zoom / Chunk.BlocksY);
            Chunk.SizeX = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SizeY = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            Gameplay.iglog.add("Zoom: "+Gameplay.view.getZoom()+" Chunk.SizeX: "+Chunk.SizeX+" Chunk.SizeY: "+Chunk.SizeY);   
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
            
            if (Gameplay.view.cameramode) {
                Gameplay.view.cameraX += newx-oldx;
                Gameplay.view.cameraY += newy-oldy;
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