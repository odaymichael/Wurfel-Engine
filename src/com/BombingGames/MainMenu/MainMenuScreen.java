package com.BombingGames.MainMenu;

import com.badlogic.gdx.Screen;
 
/**
 * The game state of the Main Menu.
 * @author Benedikt
 */
public class MainMenuScreen implements Screen{
    private static boolean loadMap = false;
 
    private static View View;
    private static Controller Controller;
    
    /**
     * Creates the main Menu
     */
    public MainMenuScreen() {
        Controller = new Controller(); 
        View = new View();
    }

    
    @Override
    public void render(float delta) {
        Controller.update((int) (delta*1000));
        View.render(Controller);
        //TODO View.update(delta*1000); 
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
  
    /**
     * 
     * @return
     */
    public static com.BombingGames.MainMenu.Controller getController() {
        return Controller;
    }

    /**
     * 
     * @return
     */
    public static com.BombingGames.MainMenu.View getView() {
        return View;
    }

    /**
     * 
     * @return
     */
    public static boolean shouldLoadMap() {
        return loadMap;
    }

    /**
     * 
     * @param loadmap
     */
    public static void setLoadMap(boolean loadmap) {
        MainMenuScreen.loadMap = loadmap;
    }
}