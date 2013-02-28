package com.BombingGames.MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Benedikt
 */
public class Controller {
    private Sound fx;
    
    private final MenuItem startGameOption = new MenuItem(0);
    private final MenuItem loadGameOption = new MenuItem(1);
    private final MenuItem exitOption = new MenuItem(2);
    
    /**
     * Creates a new Controller
     * @param pView
     * @throws SlickException
     */
    public Controller() throws SlickException{
        fx = new Sound("com/BombingGames/MainMenu/click2.wav");
    }
    
    /**
     * updates game logic
     * @param gc
     * @param sbg
     * @param delta
     */
    public void update(GameContainer gc, StateBasedGame sbg, int delta){
        Input input = gc.getInput();
         
        if (startGameOption.isClicked(input)){
            MainMenuState.loadmap = false;
            fx.play(); 
            sbg.enterState(2);            
        } else if (loadGameOption.isClicked(input)) { 
                MainMenuState.loadmap = true;
                fx.play();
                //fade in is a bad idea because afer the fade in is a lag.
                //sbg.enterState(2, new FadeInTransition(), new FadeInTransition());
                sbg.enterState(2); 
        }else if (exitOption.isClicked(input)){
            gc.exit();
        }
    }

    public MenuItem getExitOption() {
        return exitOption;
    }

    public MenuItem getLoadGameOption() {
        return loadGameOption;
    }

    public MenuItem getStartGameOption() {
        return startGameOption;
    }
 
   
}
