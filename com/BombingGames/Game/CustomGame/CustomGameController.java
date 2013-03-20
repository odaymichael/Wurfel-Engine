package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.ExplosiveBarrel;
import com.BombingGames.Game.*;
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
        
        setPlayer(
            (AbstractCharacter) AbstractEntity.getInstance(40, 0, new int[]{0,0, Map.getBlocksZ()-1})
        );
        
        setCamera(
            new Camera(
                getPlayer(),
                0, //left
                0, //top
                gc.getWidth(), //full width 
                gc.getHeight()//full height
            )
        );
        
        setMinimap(new Minimap(this, getCamera()));
        gc.getInput().addMouseListener(new MouseDraggedListener());
    }
    
    @Override
    public void update(int delta) throws SlickException{
        //get input and do actions
        Input input = gc.getInput();
        
        if (!Gameplay.MSGSYSTEM.isListeningForInput()) {
            if (input.isKeyDown(Input.KEY_Q)) gc.exit();

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_E)){ //((ExplosiveBarrel)(getMapData(Chunk.getBlocksX()+5, Chunk.getBlocksY()+5, 3))).explode();
                getMap().earthquake(5000);
                Controller.requestRecalc();
            }
            
            //toggle minimap
            if (input.isKeyPressed(Input.KEY_M)){
                Gameplay.MSGSYSTEM.add("Minimap toggled to: "+ getMinimap().toggleVisibility());
            }
            
            //pause
            if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

            //toggle getCamera()
            //if (input.isKeyPressed(Input.KEY_C)) 

            //restart
            if (input.isKeyPressed(Input.KEY_N)) newMap();

            //reset zoom
            if (input.isKeyPressed(Input.KEY_Z)) {
                getCamera().setZoom(1);
                Gameplay.MSGSYSTEM.add("Zoom reset");
            }        

            //walk
            if (getPlayer() != null)
                if ("WASD".equals(getPlayer().getControlls()))
                    getPlayer().walk(
                        input.isKeyDown(Input.KEY_W),
                        input.isKeyDown(Input.KEY_S),
                        input.isKeyDown(Input.KEY_A),
                        input.isKeyDown(Input.KEY_D),
                        .25f+(input.isKeyDown(Input.KEY_LSHIFT)? 0.75f: 0)
                    );
                if (input.isKeyPressed(Input.KEY_SPACE)) getPlayer().jump();
            
        } else {
            //fetch input and write it down
            //to-do!
        }
        
        //toggle input for msgSystem
        if (input.isKeyPressed(Input.KEY_ENTER)) Gameplay.MSGSYSTEM.listenForInput(!Gameplay.MSGSYSTEM.isListeningForInput());
        
        super.update(delta);
    }
    
    class MouseDraggedListener implements MouseListener{
        private float zoom = 1;
        
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoom = zoom + change/1000f;
            getCamera().setZoom(zoom);
            
            Gameplay.MSGSYSTEM.add("Zoom: " + getCamera().getZoom());   
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
//            Log.info(
//                    Double.toString(Math.atan(
//                        Math.abs(getView().getCamera().getCenterofBlock(getPlayer().getCoordX(), getPlayer().getCoordY(), getPlayer().getCoordZ())[1 ]- newy * getView().getEqualizationScale()) /
//                        (float) Math.abs(getView().getCamera().getCenterofBlock(getPlayer().getCoordX(), getPlayer().getCoordY(), getPlayer().getCoordZ())[0] - newx * getView().getEqualizationScale())
//                    )*180/Math.PI)+"°"
//                );
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug in slick, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            int coords[] = getView().ScreenToGameCoords(newx,newy);

            if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){ //left click
                if (coords[2] < Map.getBlocksZ()-1) coords[2]++;

                setMapData(coords, Block.getInstance(71, 0, Controller.getMap().relToAbsCoords(coords)));
                getCamera().traceRayTo(coords, true);
            } else {//right click
                if (getMapDataSafe(coords) instanceof ExplosiveBarrel)
                    ((ExplosiveBarrel) getMapDataSafe(coords)).explode();
            }
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
