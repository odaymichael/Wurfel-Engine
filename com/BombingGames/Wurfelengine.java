package com.BombingGames;

import com.BombingGames.EngineCore.WorkingDirectory;
import com.BombingGames.MainMenu.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;

/**
 *The Main Class of the engine.
 * @author Benedikt Vogler
 */
public class Wurfelengine {
    /**
     * The Version of the Engine
     */
    public static final String VERSION = "1.0.4";    
    private static File workingDirectory;
    public static final Core CORE = new Core();
    private static boolean fullscreen = false;;
    

    public static class Core extends Game {
        
        @Override
        public void create() {
            this.setScreen(
                new MainMenuScreen()
            );
        }
    }
    
    /**
     * Create the Engine.
     * @param title The title, which is displayed in the window.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @throws SlickException
     */
    public Wurfelengine(String title, String[] args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

         config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
         config.fullscreen = false;
         config.vSyncEnabled = false;
         
        //arguments
        //you can start the game with a custom resolution
        if (args.length == 0){
           config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        } else {
            if (args.length >= 3)
                config.width = Integer.parseInt(args[0]);
                config.height = Integer.parseInt(args[1]);
                config.fullscreen = ("true".equals(args[2]));
        }    
        
        config.title = title + " " + config.width + "x"+config.height;     

        workingDirectory = WorkingDirectory.getWorkingDirectory("Wurfelengine");
        
        Texture.setEnforcePotImages(false);
        LwjglApplication application = new LwjglApplication(CORE, config);
        //basic engine setting
        
        //LIBGDX: no equivalent found in libGDX yet
        //setUpdateOnlyWhenVisible(true);        
        //setMaximumLogicUpdateInterval(200);//delta can not be bigger than 200ms ^= 5 FPS
        //setMinimumLogicUpdateInterval(1);//delta can not be smaller than 1 ^= 1000FPS  
    }
    
    /**
     * Gives the credits of the engine.
     * @return a long string with breaks
     */
    public static String getCredits(){
        String newline = System.getProperty("line.separator");
        return "Idea:"+newline
            + " Benedikt Vogler"+newline+newline
            + "Programming:"+newline
            + "Benedikt Vogler"+newline+newline
            + "Art:"+newline
            + "Benedikt Vogler"+newline
            + "Pia Lenßen"+newline+newline
            + "Quality Assurance"+newline
            + "Thomas Vogt";
    }
    
   /**
     * Returns the save file folder, wich is different on every OS.
     * @return a folder
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }

    public static void setFullscreen(boolean fullscreen) {
        Wurfelengine.fullscreen = fullscreen;
        Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fullscreen);
        Gdx.graphics.setTitle("Wurfelengine V" + Wurfelengine.VERSION + " " + Gdx.graphics.getWidth() + "x"+Gdx.graphics.getHeight());
    }

    public static boolean isFullscreen() {
        return fullscreen;
    } 
}