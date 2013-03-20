package com.BombingGames;

import com.BombingGames.Game.Gameplay;
import com.BombingGames.MainMenu.MainMenuState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The main class of the Launcher.
 * @author Benedikt
 */
public class Launcher extends StateBasedGame {
    /**
     * Creates a new Launcher.
     */
    public Launcher(){
        super("Wurfelengine V" + Wurfelengine.VERSION);
    }

    /**
     * Main method wich starts the game
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer game = new Wurfelengine(new Launcher(), args);         
    }

    /**
     * Initialize the statesList
     * @param container
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState(1));
        addState(new Gameplay());
    }
}