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
     * The Version of the Engine
     */
    public static final String VERSION = "0.7";
    
    private static AppGameContainer game;    
    private static GameContainer gc;
        
    /**
     * Creates a new Launcher
     */
    public Launcher(){
        super("Wurfelengine V" + VERSION);
    }

    /**
     * main method wich starts the game
     * @param args custom display resolution [0] width, [1] height
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
        
        game = new Wurfelengine(new Launcher(),args);         
        //game.setIcon(VERSION); 
    }

    /**
     * 
     * @param container
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState(1));
        addState(new Gameplay());
        
        gc = container;
    }
}