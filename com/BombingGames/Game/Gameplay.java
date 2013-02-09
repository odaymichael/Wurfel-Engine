package com.BombingGames.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The Gameplay State. This is state where the magic happens.
 * @author Benedikt
 */
public class Gameplay extends BasicGameState { 
  /**
     * Contains the Message System
     */
    public static final MsgSystem MSGSYSTEM = new MsgSystem();    
    
    private static View view = null;
    private static Controller controller = null;
     
    
    @Override
    public int getID() {
        return 2;
    }
     
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    }
    
    @Override 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        controller = new GameController(container, game);
        view = new View(container);
    }    

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        controller.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        view.render(g);
    }

    /**
     * 
     * @return
     */
    public static Controller getController() {
        return controller;
    }

    /**
     * 
     * @return
     */
    public static View getView() {
        return view;
    }
    
    
}