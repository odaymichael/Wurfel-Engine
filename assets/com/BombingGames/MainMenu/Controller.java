package com.BombingGames.MainMenu;

import com.BombingGames.Game.CustomGameController;
import com.BombingGames.Game.CustomGameView;
import com.BombingGames.Game.ExplosivesDemoController;
import com.BombingGames.WurfelEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * The controlelr of the main Menu manages the data.
 * @author Benedikt
 */
public class Controller {
    
    private final MenuItem[] menuItems = new MenuItem[4];
    private final Sound fx;
    
    /**
     * Creates a new Controller
     */
    public Controller() {
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/MainMenu/Images/MainMenu.txt"), true);
                
        menuItems[0] = new MenuItem(0, texture.getRegions().get(3));
        menuItems[1] = new MenuItem(1, texture.getRegions().get(1));
        menuItems[2] = new MenuItem(2, texture.getRegions().get(0));
        menuItems[3] = new MenuItem(3, texture.getRegions().get(2));
        
        fx = Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/MainMenu/click2.wav"));
        Gdx.input.setInputProcessor(new InputListener());
    }
    
    /**
     * updates game logic
     * @param delta
     */
    public void update(int delta){
        if (menuItems[0].isClicked()){
            MainMenuScreen.setLoadMap(true);
            fx.play();
            WurfelEngine.startGame(new CustomGameController(), new CustomGameView());
        } else if (menuItems[1].isClicked()) { 
                MainMenuScreen.setLoadMap(false);
                fx.play();
                WurfelEngine.startGame(new CustomGameController(), new CustomGameView());
            } else if (menuItems[2].isClicked()){
                    MainMenuScreen.setLoadMap(false);
                    fx.play();
                    WurfelEngine.startGame(new ExplosivesDemoController(), new CustomGameView());
                } else if (menuItems[3].isClicked()){
                    fx.play();
                    Gdx.app.exit();
                }
    }

    /**
     *
     * @return
     */
    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    /**
     *
     */
    public void dispose(){
        fx.dispose();
    }

    private class InputListener implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE)
                Gdx.app.exit();
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return true;
        }

        @Override
        public boolean scrolled(int amount) {
            return true;
        }
    }
}