package com.BombingGames;

import java.io.File;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *The Main Class of the engine.
 * @author Benedikt
 */
public class Wurfelengine extends AppGameContainer {
    /**
     * The Version of the Engine
     */
    public static final String VERSION = "0.7";    
    private static File workingDirectory;

    /**
     * Create the Engine.
     * @param sbGame The surrounding StatebasedGame
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @throws SlickException
     */
    public Wurfelengine(StateBasedGame sbGame, String[] args) throws SlickException {
        super(sbGame);
        workingDirectory = WorkingDirectory.getWorkingDirectory("Wurfelengine");
        
        setUpdateOnlyWhenVisible(true);        
        setMaximumLogicUpdateInterval(200);//delta can not be bigger than 200ms
        
        //you can start the game with a custom resolution
        if (args.length == 0){
           setDisplayMode(getScreenWidth(), getScreenHeight(), false);
        } else {
            boolean fullscreen = true;
            if (args.length >= 3)
                fullscreen = ("true".equals(args[2]));
            setDisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), fullscreen);
        }
        
        start();        
    }
    
   /**
     * Returns the save file folder, wich is different on every OS.
     * @return a folder
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }
}
