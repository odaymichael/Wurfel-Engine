package com.BombingGames.Game;

import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.Map.Minimap;
import com.BombingGames.EngineCore.WECamera;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Controller;
import static com.BombingGames.EngineCore.Controller.getLightengine;
import static com.BombingGames.EngineCore.Controller.getMap;
import com.BombingGames.EngineCore.GameplayScreen;
import com.BombingGames.EngineCore.Map.Chunk;
import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.Zombie;
import com.BombingGames.MainMenu.MainMenuScreen;
import com.BombingGames.WurfelEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.openal.Ogg.Sound;

/**
 *The <i>CustomGameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class CustomGameController extends Controller {
    private AbstractEntity focusentity;
    private Sound gras1;
    private Sound gras2;
    private BlockToolbar blockToolbar;

        
    @Override
    public void init(){
         Chunk.setGenerator(0);
         super.init();


        gras1 = (Sound) Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/grass1.ogg"));
        gras2 = (Sound) Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/grass2.ogg"));
        
        
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
        
        blockToolbar = new BlockToolbar();
        
        focusentity = AbstractEntity.getInstance(13, 0, new Coordinate(0, 0, Map.getBlocksZ()-1, true));
        //focusentity.setPositionY(Block.DIM2+1f);
        focusentity.exist();

        
        Gdx.input.setInputProcessor(new InputListener());
    }

    
    @Override
    public void update(float delta){
        //get input and do actions
        Input input = Gdx.input;
        
        if (!GameplayScreen.msgSystem().isListeningForInput()) {

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
        }
        
        super.update(delta);
    }
    
    private class InputListener implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            if (!GameplayScreen.msgSystem().isListeningForInput()) {
                //toggle minimap
                 if (keycode == Input.Keys.M){
                     GameplayScreen.msgSystem().add("Minimap toggled to: "+ getMinimap().toggleVisibility());
                 }
                 //toggle fullscreen
                 if (keycode == Input.Keys.F){
                     WurfelEngine.setFullscreen(!WurfelEngine.isFullscreen());
                     Gdx.app.log("DEBUG","Set to fullscreen:"+!WurfelEngine.isFullscreen());
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

                  if (keycode == Input.Keys.T) {
                     setTimespeed();
                  } 

                 if (keycode == Input.Keys.ESCAPE)// Gdx.app.exit();
                     WurfelEngine.getInstance().setScreen(new MainMenuScreen());
                 
                 if (keycode == Input.Keys.K) {
                    Zombie zombie = (Zombie) AbstractEntity.getInstance(
                        43,
                        0,
                        focusentity.getCoords()
                    );
                    zombie.setTarget(getPlayer());
                    zombie.exist();   
                 }
             
                if (keycode == Input.Keys.NUM_1) blockToolbar.setSelection(0);
                if (keycode == Input.Keys.NUM_2) blockToolbar.setSelection(1);
                if (keycode == Input.Keys.NUM_3) blockToolbar.setSelection(2);
                if (keycode == Input.Keys.NUM_4) blockToolbar.setSelection(3);
                if (keycode == Input.Keys.NUM_5) blockToolbar.setSelection(4);
                if (keycode == Input.Keys.NUM_6) blockToolbar.setSelection(5);
                if (keycode == Input.Keys.NUM_7) blockToolbar.setSelection(6);
                if (keycode == Input.Keys.NUM_8) blockToolbar.setSelection(7);
                if (keycode == Input.Keys.NUM_9) blockToolbar.setSelection(8);
            }


             //toggle input for msgSystem
             if (keycode == Input.Keys.ENTER)
                 GameplayScreen.msgSystem().listenForInput(!GameplayScreen.msgSystem().isListeningForInput());

            return true;            
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            GameplayScreen.msgSystem().getInput(character);
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Coordinate coords = getView().ScreenToGameCoords(screenX,screenY);
            if (coords.getZ() < Map.getBlocksZ()-1) coords.addVector(0, 0, 1);
            
            if (button == 0){ //left click
                setMapData(coords, Block.getInstance(0));
                requestRecalc();
                //getCameras().get(0).traceRayTo(coords, true);
                gras1.play();
            } else {//right click
                if (getMapData(coords).getId() == 0){
                    setMapData(coords, Block.getInstance(blockToolbar.getSelectionID(),0,coords));
                    requestRecalc();
                    gras2.play();
                }
            }    
            return true;
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
            focusentity.setCoords(getView().ScreenToGameCoords(screenX,screenY).addVector(0, 0, 1));
            return true;
        }

        @Override
        public boolean scrolled(int amount) {
            getCameras().get(0).setZoom(getCameras().get(0).getZoom() - amount/100f);
            
            GameplayScreen.msgSystem().add("Zoom: " + getCameras().get(0).getZoom());   
            return true;
        }
    }
    
    public BlockToolbar getBlockToolbar() {
        return blockToolbar;
    }
    
    public AbstractEntity getFocusentity() {
        return focusentity;
    }
}
