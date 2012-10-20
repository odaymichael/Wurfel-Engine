package Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState { 
    protected static View View = null;
    protected static Controller Controller = null;
    private int stateID = 2;
    public static MsgSystem iglog;
    public static GameContainer gc;
    
 
    public GameplayState( int stateID ){
       this.stateID = stateID;
    }
    
    @Override
    public int getID() {
        return stateID;
    }
     
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        GameplayState.gc = gc;
    }
    
    @Override 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        iglog = new MsgSystem();
        Controller = new Controller(container, game);
        View = new View(container);
    }
    

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Controller.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //clipping everythoing out of the viewport does not affect anything
        //g.setClip(0, 0, 600, 800);
        View.render(game, g);
    }
 
}