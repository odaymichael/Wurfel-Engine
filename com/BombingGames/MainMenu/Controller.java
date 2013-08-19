package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * The controlelr of the main Menu manages the data.
 * @author Benedikt
 */
public class Controller {
    //private Sound fx;
    
    private MenuItem[] menuItems;
    
    /**
     * Creates a new Controller
     * @throws SlickException
     */
    public Controller() {
        this.menuItems = new MenuItem[3];
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/MainMenu/Images/MainMenu.txt"));
                
        this.menuItems = new MenuItem[3];
        menuItems[0] = new MenuItem(0, (Texture) texture.getTextures().toArray()[0]);
        menuItems[1] = new MenuItem(1, (Texture) texture.getTextures().toArray()[0]);
        menuItems[2] = new MenuItem(2, (Texture) texture.getTextures().toArray()[0]);
        
        //fx = new Sound("com/BombingGames/MainMenu/click2.wav");
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
            //fx.play(); 
            //sbg.enterState(2);            
        } else if (menuItems[1].isClicked()) { 
                MainMenuScreen.setLoadMap(true);
                //fx.play();
                //sbg.enterState(2); 
            } else if (menuItems[2].isClicked()){
                Gdx.app.exit();
            }
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }


}