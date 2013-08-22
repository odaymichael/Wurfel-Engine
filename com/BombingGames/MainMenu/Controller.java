package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
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
     * @throws SlickException
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
     * @param gc
     * @param sbg
     * @param delta
     */
    public void update(float delta){
        if (menuItems[0].isClicked()){
            MainMenuScreen.setLoadMap(false);
            fx.play();
            //sbg.enterState(2);            
        } else if (menuItems[1].isClicked()) { 
                MainMenuScreen.setLoadMap(true);
                fx.play();
                //sbg.enterState(2); 
            } else if (menuItems[2].isClicked()){
                fx.play();
                Gdx.app.exit();
            }
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }

}