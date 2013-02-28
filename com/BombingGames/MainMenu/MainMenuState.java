package com.BombingGames.MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
/**
 * The game state of the Main Menu
 * @author Benedikt
 */
public class MainMenuState extends BasicGameState{
    /**
     * 
     */
    public static boolean loadmap = false;
    int stateID = 1;
    /**
     * 
     */
    public static GameContainer gc;
    /**
     * 
     */
    public static StateBasedGame sbg;
 
    private static View View;
    private static Controller Controller;
    
    /**
     * 
     * @param stateID
     */
    public MainMenuState( int stateID) {
       this.stateID = stateID;
    }

 
    @Override
    public int getID() {
        return stateID;
    }
 
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        MainMenuState.gc = gc;
        MainMenuState.sbg = sbg;
        Controller = new Controller(); 
        View = new View(gc);
    }
  
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Controller.update(gc,sbg,delta);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        View.render(Controller); 
    }

    public static com.BombingGames.MainMenu.Controller getController() {
        return Controller;
    }

    public static com.BombingGames.MainMenu.View getView() {
        return View;
    }
}