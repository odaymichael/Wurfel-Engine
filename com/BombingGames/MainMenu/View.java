package com.BombingGames.MainMenu;

import org.newdawn.slick.*;

/**
 * The View manages ouput
 * @author Benedikt
 */
public class View {
    //private float startGameScale = 1;
    //private float exitScale = 1;
    private final Image lettering;
    private final Image background;
    private GameContainer gc;
    
    
    /**
     * Creates a View.
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException{
        lettering = new Image("com/BombingGames/MainMenu/Images/Lettering.png");
        background = new Image("com/BombingGames/MainMenu/Images/background.png");
        this.gc = gc;
        MenuItem.setSpritesheet(new SpriteSheet("com/BombingGames/MainMenu/Images/MainMenu.png",800,80));
        
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
     * @param controller
     * @param g
     */
    void render(Controller Controller, Graphics g) {
                //Background        
        for (int x = 0; x-1 < gc.getWidth()/background.getWidth(); x++) {
            for (int y = 0; y-1 < gc.getHeight()/background.getHeight(); y++) {
                background.draw(x*background.getWidth(), y*background.getHeight());
            }
        }
        
        // render the lettering
        lettering.draw((gc.getWidth() - lettering.getWidth())/2,80);
        
        // Draw menu
        MainMenuState.getController().getStartGameOption().draw(g);
        MainMenuState.getController().getLoadGameOption().draw(g);
        MainMenuState.getController().getExitOption().draw(g);
    }
}

