package com.BombingGames;

import com.BombingGames.Game.Gameplay;
import com.BombingGames.MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *A class wich holds the game states.
 * @author Benedikt Vogler
 */
public class StateBasedEngine extends StateBasedGame {
   /**
     * Creates a new Launcher.
     * @param title The title of the game.
     */
    public StateBasedEngine(String title){
        super(title);
    }
    
    /**
     * Initialize the statesList
     * @param container
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState(1));
        addState(new Gameplay(2));
    }
}
