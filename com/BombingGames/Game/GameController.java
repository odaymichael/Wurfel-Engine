package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.ExplosiveBarrel;
import com.BombingGames.Game.Gameobjects.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 *The <i>GameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class GameController extends Controller {
    private GameContainer gc;
    /**
     * The custom game code belongs here
     * @param gc 
     * @param game
     * @throws SlickException
     */
    public GameController(GameContainer gc, StateBasedGame game) throws SlickException{
        super(gc, game);
        this.gc = gc;
        
        setPlayer((Player) Block.create(40,0,(int) (Chunk.getBlocksX()*1.5),(int) (Chunk.getBlocksY()*1.5), Chunk.getBlocksZ()-2));
        
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
            if (input.isKeyPressed(Input.KEY_E)) ((ExplosiveBarrel)(getMapData(Chunk.getBlocksX()+5, Chunk.getBlocksY()+5, 3))).explode();
            //getMap().earthquake(); 
            
            //pause
            if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

            //good graphics
            if (input.isKeyPressed(Input.KEY_G)) {
                Gameplay.getView().setGoodgraphics(!Gameplay.getView().hasGoodGraphics());
                Gameplay.MSGSYSTEM.add("Good Graphics is now "+Gameplay.getView().hasGoodGraphics());
            }

            //toggle getCamera()
            //if (input.isKeyPressed(Input.KEY_C)) 

            //restart
            if (input.isKeyPressed(Input.KEY_N)) newMap();

            //reset zoom
            if (input.isKeyPressed(Input.KEY_Z)) {
                Gameplay.getView().getCamera().setZoom(1);
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
                        .25f+(input.isKeyDown(Input.KEY_LSHIFT)? 0.75f: 0),
                        delta
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
            Gameplay.getView().getCamera().setZoom((float) (3f*Math.sin(zoom-1.5f)+3.5f));
            
            Gameplay.MSGSYSTEM.add("Zoom: "+Gameplay.getView().getCamera().getZoom());   
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
//                        Math.abs(Gameplay.getView().getCamera().getCenterofBlock(getPlayer().getCoordX(), getPlayer().getCoordY(), getPlayer().getCoordZ())[1 ]- newy * Gameplay.getView().getEqualizationScale()) /
//                        (float) Math.abs(Gameplay.getView().getCamera().getCenterofBlock(getPlayer().getCoordX(), getPlayer().getCoordY(), getPlayer().getCoordZ())[0] - newx * Gameplay.getView().getEqualizationScale())
//                    )*180/Math.PI)+"°"
//                );
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug in slick, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            int coords[] = Gameplay.getView().ScreenToGameCoords(newx,newy);
            if (coords[2] < Map.getBlocksZ()-1) coords[2]++; 
            setMapDataSafe(coords[0], coords[1], coords[2], Block.getInstance(1));
            Gameplay.getView().getCamera().traceRayTo(coords, true);
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
