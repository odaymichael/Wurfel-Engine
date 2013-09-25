package com.BombingGames;

import android.os.Environment;

import com.BombingGames.EngineCore.WorkingDirectory;
import com.BombingGames.MainMenu.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;

/**
 *The Main class of the engine. To create a new engine use  {@link com.BombingGames.WurfelEngine#construct(java.lang.String, java.lang.String[]) WurfelEngine.construct}
 * The Wurfel Engine needs the API libGDX0.9.8. It has not been tested with other versions.
 * @author Benedikt Vogler
 */
public class WurfelEngine extends Game {
    /**
     * The Version of the Engine
     */
    public static final String VERSION = "1.1.6";    
    private static File workingDirectory;
    private static boolean fullscreen = false;
    private static WurfelEngine instance;
   
    /**
     * Create the Engine.
     * @param title The title, which is displayed in the window.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     */
    private WurfelEngine(){
        
    	
    	workingDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/Wurfel-Engine/");
    	
        //workingDirectory = WorkingDirectory.getWorkingDirectory("Wurfelengine");
        
        Texture.setEnforcePotImages(false);


        
        //LIBGDX: no equivalent found in libGDX yet
        //setUpdateOnlyWhenVisible(true);        
        //setMaximumLogicUpdateInterval(200);//delta can not be bigger than 200ms ^= 5 FPS
        //setMinimumLogicUpdateInterval(1);//delta can not be smaller than 1 ^= 1000FPS  
    }
    
    @Override
    public void create() {
        setScreen(
            new MainMenuScreen()
            );
    }
    
    /**
     * Singleton method.
     * @return the wurfelengine
     */
    public static WurfelEngine getInstance(){
        return instance;
    }
    
    /**
     * Create the Engine.
     * @param title The title, which is displayed in the window.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @return the engine
     */
    public static WurfelEngine construct(){
        instance = new WurfelEngine();
        return instance;
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

    /**
     *
     * @param fullscreen
     */
    public static void setFullscreen(boolean fullscreen) {
        WurfelEngine.fullscreen = fullscreen;
        Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fullscreen);
        Gdx.graphics.setTitle("Wurfelengine V" + WurfelEngine.VERSION + " " + Gdx.graphics.getWidth() + "x"+Gdx.graphics.getHeight());
    }

    /**
     *
     * @return
     */
    public static boolean isFullscreen() {
        return fullscreen;
    } 
}