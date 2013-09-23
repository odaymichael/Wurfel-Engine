package com.BombingGames.MainMenu;

import com.BombingGames.EngineCore.GameplayScreen;
import com.BombingGames.Wurfelengine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * The controlelr of the main Menu manages the data.
 * @author Benedikt
 */
public class Controller {
    
    private MenuItem[] menuItems = new MenuItem[3];
    private final Sound fx;
    
    /**
     * Creates a new Controller
     */
    public Controller() {
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/MainMenu/Images/MainMenu.txt"), true);
                
        menuItems[0] = new MenuItem(0, texture.getRegions().get(2));
        menuItems[1] = new MenuItem(1, texture.getRegions().get(0));
        menuItems[2] = new MenuItem(2, texture.getRegions().get(1));
        
        
        fx = Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/MainMenu/click2.wav"));
    }
    
    /**
     * updates game logic
     * @param delta
     */
    public void update(int delta){
        if (menuItems[0].isClicked()){
            MainMenuScreen.setLoadMap(true);
            fx.play();
            Wurfelengine.getInstance().setScreen(new GameplayScreen());
        } else if (menuItems[1].isClicked()) { 
                MainMenuScreen.setLoadMap(false);
                fx.play();
                Wurfelengine.getInstance().setScreen(new GameplayScreen());
            } else if (menuItems[2].isClicked()){
                fx.play();
                Gdx.app.exit();
            }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
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
}