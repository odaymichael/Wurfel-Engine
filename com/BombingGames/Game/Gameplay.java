package com.BombingGames.Game;

import com.BombingGames.Game.CustomGame.CustomGameController;
import com.BombingGames.Game.CustomGame.CustomGameView;
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
    private static MsgSystem msgSystem;    
    
    private View view = null;
    private Controller controller = null;
    private int stateId;

    /**
     * Create the gameplay state.
     * @param id the id used by this state.
     */
    public Gameplay(int id) {
        this.stateId = id;
    }
     
    
    /**
     * Returns the state Id of this gameState.
     * @return
     */
    @Override
    public int getID() {
        return stateId;
    }
     
    /**
     * Initailize the gameplay.
     * @param gc
     * @param sbg
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    }
    
    /**
     * 
     * @param container
     * @param game
     * @throws SlickException
     */
    @Override 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        msgSystem = new MsgSystem(container.getWidth()/2, 3*container.getHeight()/4);
        container.setClearEachFrame(false);
        //Wurfelengine.getGameContainer().setSmoothDeltas(true);
        
        controller = new CustomGameController(container, game);
        view = new CustomGameView(container, controller);
        controller.setView(view);
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
        controller.update(delta);
    }

    /**
     * Part of the game loop. Cals view.render(g);
     * @param container
     * @param game
     * @param g
     * @throws SlickException
     */
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        view.render(g);
    }

    /**
     * Returns the Message System. Use .add() to add messages to the msgSystem.
     * @return The msgSystem.
     */
    public static MsgSystem msgSystem() {
        return msgSystem;
    }
    
}