package com.BombingGames;


import com.BombingGames.Game.Gameplay;
import com.BombingGames.MainMenu.MainMenuState;
import java.io.File;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The main class of the Wurfelengine.
 * @author Benedikt
 */
public class Wurfelengine extends StateBasedGame {
    /**
     * The Version of the Engine
     */
    public static final String VERSION = "0.4";
    
    private static AppGameContainer game;    
    private static File workingDirectory;
    private static GameContainer gc;
        
    /**
     * Creates a new Wurfelengine
     */
    public Wurfelengine(){
        super("Wurfelengine V" + VERSION);
    }

    /**
     * main method wich starts the game
     * @param args custom display resolution [0] width, [1] height
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
        workingDirectory = WorkingDirectory.getWorkingDirectory("Wurfelengine");
        
        game = new AppGameContainer(new Wurfelengine());         
        game.setUpdateOnlyWhenVisible(true);
         
        //you can start the game with a custom resolution
        if (args.length == 0)
           game.setDisplayMode(game.getScreenWidth(), game.getScreenHeight(), false);
        else
            game.setDisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), false);
        //System.out.println(game.isVSyncRequested());
        game.start();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new MainMenuState(1));
        addState(new Gameplay());
        
        gc = container;
    }

    /**
     * returns the save file folder, different on every OS
     * @return a folder
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }
    
    /**
     * Returns the graphic context
     * @return 
     */
    public static Graphics getGraphics(){
        return game.getGraphics();
    }

    /**
     * Returns the game container
     * @return 
     */
    public static GameContainer getGc() {
        return gc;
    }
}