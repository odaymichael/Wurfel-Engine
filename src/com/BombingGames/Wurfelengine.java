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
 *The Main class of the engine. To create a new engine use  {@link com.BombingGames.Wurfelengine#construct(java.lang.String, java.lang.String[]) Wurfelengine.construct}
 * The Wurfel Engine needs the API libGDX0.9.8. It has not been tested with other versions.
 * @author Benedikt Vogler
 */
public class Wurfelengine extends Game {
    /**
     * The Version of the Engine
     */
    public static final String VERSION = "1.1.6";    
    private static File workingDirectory;
    private static boolean fullscreen = false;
    private static Wurfelengine instance;
   
    /**
     * Create the Engine.
     * @param title The title, which is displayed in the window.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     */
    public Wurfelengine(){
        
    	
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
    public static Wurfelengine getInstance(){
        return instance;
    }
    
    /**
     * Create the Engine.
     * @param title The title, which is displayed in the window.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @return the engine
     */
    public static Wurfelengine construct(){
        instance = new Wurfelengine();
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
        Wurfelengine.fullscreen = fullscreen;
        Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fullscreen);
        Gdx.graphics.setTitle("Wurfelengine V" + Wurfelengine.VERSION + " " + Gdx.graphics.getWidth() + "x"+Gdx.graphics.getHeight());
    }

    /**
     *
     * @return
     */
    public static boolean isFullscreen() {
        return fullscreen;
    } 
}