package com.BombingGames;

import java.io.File;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Benedikt
 */
public class Wurfelengine extends AppGameContainer{
    private static File workingDirectory;

    public Wurfelengine(Launcher core, String[] args) throws SlickException {
        super(core);
        workingDirectory = WorkingDirectory.getWorkingDirectory("Wurfelengine");
        
        setUpdateOnlyWhenVisible(true);
        
        setMaximumLogicUpdateInterval(200);//delta can not be bigger than 200ms
        
        //you can start the game with a custom resolution
        if (args.length == 0)
           setDisplayMode(getScreenWidth(), getScreenHeight(), false);
        else{
            boolean fullscreen = true;
            if (args.length >= 3)
                fullscreen = ("true".equals(args[2]));
            setDisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), fullscreen);
        }
        //System.out.println(game.isVSyncRequested());
        start();
    }
    
   /**
     * returns the save file folder, different on every OS
     * @return a folder
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }
}
