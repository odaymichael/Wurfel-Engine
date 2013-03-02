package com.BombingGames.MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * The View manages ouput
 * @author Benedikt
 */
public class View {
    //private float startGameScale = 1;
    //private float exitScale = 1;
    private final Image background;
    
    
    /**
     * Creates a View.
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException{
        background = new Image("com/BombingGames/MainMenu/Images/Lettering.png"); 
        MenuItem.setSpritesheet(new SpriteSheet("com/BombingGames/MainMenu/Images/MainMenu.png",400,50));
        
        MenuItem startgame = MainMenuState.getController().getStartGameOption();
        startgame.setX((gc.getWidth() - MenuItem.getSpritesheet().getWidth())/2);
        startgame.setY(gc.getHeight()/2 - 100);
        
        MenuItem loadgame = MainMenuState.getController().getLoadGameOption();
        loadgame.setX((gc.getWidth()- MenuItem.getSpritesheet().getWidth())/2);
        loadgame.setY(gc.getHeight()/2);
        
        MenuItem exit = MainMenuState.getController().getExitOption();
        exit.setX((gc.getWidth()- MenuItem.getSpritesheet().getWidth())/2);
        exit.setY(gc.getHeight()/2 + 100);
        
    }

    /**
     * renders the scene
     * @param pController
     */
    public void render(Controller pController){
        // render the background
        background.draw(0,0);
        
        // Draw menu
        MenuItem.getSpritesheet().startUse();
        MainMenuState.getController().getStartGameOption().draw();
        MainMenuState.getController().getLoadGameOption().draw();
        MainMenuState.getController().getExitOption().draw();
        MenuItem.getSpritesheet().endUse();
    }
}

