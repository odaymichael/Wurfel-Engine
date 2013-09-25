package com.BombingGames.Game;

import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.Map.Minimap;
import com.BombingGames.EngineCore.WECamera;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Controller;
import static com.BombingGames.EngineCore.Controller.getLightengine;
import static com.BombingGames.EngineCore.Controller.getMap;
import com.BombingGames.EngineCore.GameplayScreen;
import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Wurfelengine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 *The <i>CustomGameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class CustomGameController extends Controller {
    
    /**
     * The custom game code belongs here.
     */
    public CustomGameController() {
        super();

         AbstractCharacter player = (AbstractCharacter) AbstractEntity.getInstance(
                40,
                0,
                Coordinate.getMapCenter(Map.getBlocksZ()*Block.GAMEDIMENSION)
        );
        player.setControls("WASD");
        setPlayer(player);
        
        addCamera(
            new WECamera(
                getPlayer(),
                0, //left
                0, //top
                Gdx.graphics.getWidth(), //width 
                Gdx.graphics.getHeight()//height
            )
        );
        
//        addCamera(
//            new WECamera(
//                Gdx.graphics.getWidth()/2, //left
//                0, //top
//                Gdx.graphics.getWidth()/2, //width 
//                Gdx.graphics.getHeight()//height
//            )
//        );
        
        setMinimap(
            new Minimap(this, getCameras().get(0), Gdx.graphics.getWidth() - 400,10)
        );
        
        Gdx.input.setInputProcessor(new InputListener());
    }

    
    @Override
    public void update(float delta){
        //get input and do actions
        Input input = Gdx.input;
        
        if (!GameplayScreen.msgSystem().isListeningForInput()) {
            if (input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();


            //walk
            if (getPlayer() != null){
                if ("WASD".equals(getPlayer().getControls()))
                    getPlayer().walk(
                        input.isKeyPressed(Input.Keys.W),
                        input.isKeyPressed(Input.Keys.S),
                        input.isKeyPressed(Input.Keys.A),
                        input.isKeyPressed(Input.Keys.D),
                        .25f+(input.isKeyPressed(Input.Keys.SHIFT_LEFT)? 0.75f: 0)
                    );
                if (input.isKeyPressed(Input.Keys.SPACE)) getPlayer().jump();
            } else {
                //update camera position
                WECamera camera = getCameras().get(0);
                camera.setGamePosY( camera.getGamePosY()
                    - (input.isKeyPressed(Input.Keys.W)? 3: 0)
                    + (input.isKeyPressed(Input.Keys.S)? 3: 0)
                    );
                camera.setGamePosX( camera.getGamePosX()
                    + (input.isKeyPressed(Input.Keys.D)? 3: 0)
                    - (input.isKeyPressed(Input.Keys.A)? 3: 0)
                    );
            }
            
        } else {
            //fetch input and write it down
            //to-do!
            //Gdx.input.getTextInput(new textInput(), "Überschrift", "test");
            //TextField textfield = new TextField("enter text", new Skin());           
        }
        
        super.update(delta);
    }
    
    class InputListener implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            
           //toggle minimap
            if (keycode == Input.Keys.M){
                GameplayScreen.msgSystem().add("Minimap toggled to: "+ getMinimap().toggleVisibility());
            }
            //toggle fullscreen
            if (keycode == Input.Keys.F){
                Wurfelengine.setFullscreen(!Wurfelengine.isFullscreen());
                Gdx.app.log("DEBUG","Set to fullscreen:"+!Wurfelengine.isFullscreen());
            }

            //toggle eathquake
            if (keycode == Input.Keys.E){ //((ExplosiveBarrel)(getMapData(Chunk.getBlocksX()+5, Chunk.getBlocksY()+5, 3))).explode();
                getMap().earthquake(5000);
            }
            
            //pause
            //if (input.isKeyDown(Input.Keys.P)) Gdx.app.setPaused(true);
            //time is set 0 but the game keeps running
              if (keycode == Input.Keys.P) {
                setTimespeed(0);
             } 

            //reset zoom
            if (keycode == Input.Keys.Z) {
                getCameras().get(0).setZoom(1);
                GameplayScreen.msgSystem().add("Zoom reset");
             }  
            
            //show/hide light engine
            if (keycode == Input.Keys.L) {
                getLightengine().RenderData(!getLightengine().isRenderingData());
                Gdx.app.log("DEBUG","Toggled lightengine data rendering:"+ !getLightengine().isRenderingData());
             } 
            
            //toggle input for msgSystem
            if (keycode == Input.Keys.ENTER)
                GameplayScreen.msgSystem().listenForInput(!GameplayScreen.msgSystem().isListeningForInput());
            
             if (keycode == Input.Keys.T) {
                setTimespeed();
             } 
            return true;
            
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            getCameras().get(0).setZoom(getCameras().get(0).getZoom() - amount/100f);
            
            GameplayScreen.msgSystem().add("Zoom: " + getCameras().get(0).getZoom());   
            return true;
        }
        
       
    }
}
