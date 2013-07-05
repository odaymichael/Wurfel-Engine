package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Player;
import com.BombingGames.Game.*;
import com.BombingGames.Game.Gameobjects.Block;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *The <i>CustomGameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class CustomGameController extends Controller {
    private GameContainer gc;
    
    /**
     * The custom game code belongs here.
     * @param gc 
     * @param game
     * @throws SlickException
     */
    public CustomGameController(GameContainer gc, StateBasedGame game) throws SlickException{
        super(gc, game);
        this.gc = gc;
        
        
//        Player player = (Player) AbstractEntity.getInstance(
//                40,
//                0,
//                Coordinate.getMapCenter(Map.getBlocksZ()*Block.GAMEDIMENSION)
//            );
//        player.setControls("WASD");
//        setPlayer(player);
        
//        addCamera(
//            new Camera(
//                getPlayer(),
//                0, //left
//                0, //top
//                gc.getWidth(), //full width 
//                gc.getHeight()//full height
//            )
//        );
        
        addCamera(
            new Camera(
                0, //left
                0, //top
                gc.getWidth(), //full width 
                gc.getHeight()//full height
            )
        );
        
        setMinimap(
            new Minimap(this, getCameras().get(0), gc.getScreenWidth() - 10,10)
        );
        gc.getInput().addMouseListener(new MouseDraggedListener());
    }
    
    @Override
    public void update(int delta) throws SlickException{
        //get input and do actions
        Input input = gc.getInput();
        
        if (!Gameplay.msgSystem().isListeningForInput()) {
            if (input.isKeyDown(Input.KEY_ESCAPE)) gc.exit();

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_E)){ //((ExplosiveBarrel)(getMapData(Chunk.getBlocksX()+5, Chunk.getBlocksY()+5, 3))).explode();
                getMap().earthquake(5000);
            }
            
            //toggle minimap
            if (input.isKeyPressed(Input.KEY_M)){
                Gameplay.msgSystem().add("Minimap toggled to: "+ getMinimap().toggleVisibility());
            }
            
            //pause
            if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);


            //reset zoom
            if (input.isKeyPressed(Input.KEY_Z)) {
                getCameras().get(0).setZoom(1);
                Gameplay.msgSystem().add("Zoom reset");
             }        

            Camera camera = getCameras().get(0);
            camera.setOutputPosY(camera.getOutputPosY()- (input.isKeyDown(Input.KEY_W)? 3: 0));
            camera.setOutputPosY(camera.getOutputPosY()+ (input.isKeyDown(Input.KEY_S)? 3: 0));
            camera.setOutputPosX(camera.getOutputPosX()+ (input.isKeyDown(Input.KEY_D)? 3: 0));
            camera.setOutputPosX(camera.getOutputPosX()- (input.isKeyDown(Input.KEY_A)? 3: 0));
            
            //walk
            if (getPlayer() != null){
                if ("WASD".equals(getPlayer().getControls()))
                    getPlayer().walk(
                        input.isKeyDown(Input.KEY_W),
                        input.isKeyDown(Input.KEY_S),
                        input.isKeyDown(Input.KEY_A),
                        input.isKeyDown(Input.KEY_D),
                        .25f+(input.isKeyDown(Input.KEY_LSHIFT)? 0.75f: 0)
                    );
                if (input.isKeyPressed(Input.KEY_SPACE)) getPlayer().jump();
            }
            
        } else {
            //fetch input and write it down
            //to-do!
        }
        
        //toggle input for msgSystem
        if (input.isKeyPressed(Input.KEY_ENTER)) Gameplay.msgSystem().listenForInput(!Gameplay.msgSystem().isListeningForInput());
        
        super.update(delta);
    }
    
    class MouseDraggedListener implements MouseListener{
        private float zoom = 1;
        
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoom = zoom + change/1000f;
            if (zoom < 1) zoom = 1;
            getCameras().get(0).setZoom(zoom);
            
            Gameplay.msgSystem().add("Zoom: " + getCameras().get(0).getZoom());   
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
}
