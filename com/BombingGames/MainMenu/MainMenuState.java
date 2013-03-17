package com.BombingGames.MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
/**
 * The game state of the Main Menu.
 * @author Benedikt
 */
public class MainMenuState extends BasicGameState{
    private static boolean loadMap = false;
    private int stateID = 1;
    private GameContainer gc;
    private StateBasedGame sbg;
 
    private static View View;
    private static Controller Controller;
    
    /**
     * Creates the main Menu
     * @param stateID
     */
    public MainMenuState( int stateID) {
       this.stateID = stateID;
    }

 
    /**
     * Returns the state id.
     * @return
     */
    @Override
    public int getID() {
        return stateID;
    }
 
    /**
     * 
     * @param gc
     * @param sbg
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.gc = gc;
        this.sbg = sbg;
        Controller = new Controller(); 
        View = new View(gc);
    }
  
    /**
     * 
     * @param gc
     * @param sbg
     * @param delta
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Controller.update(gc,sbg,delta);
    }

    /**
     * 
     * @param gc
     * @param sbg
     * @param g
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        View.render(Controller); 
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
        MainMenuState.loadMap = loadmap;
    }
    
    

}