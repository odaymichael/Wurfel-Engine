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
    View View = null;
    Sound fx = null;
    
    /**
     * Creates a new Controller
     * @param pView
     * @throws SlickException
     */
    public Controller(View pView) throws SlickException{
        View = pView;
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
         
        if (View.startGameOption.isClicked(input)){
            MainMenuState.loadmap = false;
            fx.play(); 
            sbg.enterState(2);            
        } else if (View.loadGameOption.isClicked(input)) { 
                MainMenuState.loadmap = true;
                fx.play();
                //fade in is a bad idea because afer the fade in is a lag.
                //sbg.enterState(2, new FadeInTransition(), new FadeInTransition());
                sbg.enterState(2); 
        }else if (View.exitOption.isClicked(input)){
            gc.exit();
        }
    }
   
   
}
