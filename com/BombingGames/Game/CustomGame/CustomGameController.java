package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.*;
import java.net.URL;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

/**
 *The <i>CustomGameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class CustomGameController extends Controller {
    private GameContainer gc;
    private AbstractEntity focusentity;
    private Sound gras1;
    private Sound gras2;
    
    /**
     * The custom game code belongs here.
     * @param gc 
     * @param game
     * @throws SlickException
     */
    public CustomGameController(GameContainer gc, StateBasedGame game) throws SlickException{
        super(gc, game);
        this.gc = gc;
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL file  = classLoader.getResource("com/BombingGames/Game/Sounds/grass1.ogg");
        URL file2 = classLoader.getResource("com/BombingGames/Game/Sounds/grass2.ogg");
        
        gras1 = new Sound(file);
        gras2 = new Sound(file2);        
        setPlayer(
            (AbstractCharacter) AbstractEntity.getInstance(40, 0, new int[]{Chunk.getBlocksX()/2,Chunk.getBlocksY()/2, Map.getBlocksZ()-1})
        );
        
        addCamera(
            new Camera(
                getPlayer(),
                0, //left
                0, //top
                gc.getWidth(), //full width 
                gc.getHeight()//full height
            )
        );
        
        setMinimap(
            new Minimap(this, getCameras().get(0), gc.getScreenWidth() - 10,10)
        );

        
        focusentity = AbstractEntity.getInstance(13, 0, new int[]{0,0, Map.getBlocksZ()-1});
        focusentity.exist();
        
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
                Controller.requestRecalc();
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
            
            if (input.isKeyPressed(Input.KEY_K)) {
                AbstractEntity zombie = AbstractEntity.getInstance(42, 0,
                    Controller.getMap().relToAbsCoords(
                        new int[]{
                            (int) (Math.random()*(Map.getBlocksX()-1)),
                            (int) (Math.random()*(Map.getBlocksY()-1)),
                            Map.getBlocksZ()-1}
                    )
                );
                zombie.exist();   
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
        if (input.isKeyPressed(Input.KEY_ENTER)) Gameplay.msgSystem().listenForInput(!Gameplay.msgSystem().isListeningForInput());
        
        super.update(delta);
    }

    public AbstractEntity getFocusentity() {
        return focusentity;
    }
      
    class MouseDraggedListener implements MouseListener{
        private float zoom = 1;
        
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoom = zoom + change/1000f;
            getCameras().get(0).setZoom(zoom);
            
            Gameplay.msgSystem().add("Zoom: " + getCameras().get(0).getZoom());   
        }

        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        }

        @Override
        public void mousePressed(int button, int x, int y) {
             //workaround for the bug in slick, because the event is called multiple times
            //gc.getInput().consumeEvent();
            
            int coords[] = getView().ScreenToGameCoords(x,y);
            if (coords[2] < Map.getBlocksZ()-1) coords[2]++;
            
            if (button == 0){ //left click
                setMapData(coords, Block.getInstance(0));
                getCameras().get(0).traceRayTo(coords, true);
                gras1.play();
            } else {//right click
                if (getMapData(coords).getId() == 0){
                    setMapData(coords, Block.getInstance(2));
                    getCameras().get(0).traceRayTo(coords, true);
                    gras2.play();
                }
            }    
        }

        @Override
        public void mouseReleased(int button, int x, int y) {   
        }

        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            int[] tmpcoords = getView().ScreenToGameCoords(newx,newy);
            tmpcoords[2]++;
            focusentity.setRelCoords(tmpcoords);
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
