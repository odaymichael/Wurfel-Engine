package Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gameplay extends BasicGameState { 
    protected static View view = null;
    protected static Controller controller = null;
    private int stateID = 2;
    public static MsgSystem iglog;
    public static GameContainer gc;
    
 
    public Gameplay( int stateID ){
       this.stateID = stateID;
    }
    
    @Override
    public int getID() {
        return stateID;
    }
     
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Gameplay.gc = gc;
    }
    
    @Override 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        iglog = new MsgSystem();
        controller = new Controller(container, game);
        view = new View(container);
    }
    

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        controller.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //clipping everythoing out of the viewport does not affect anything
        //g.setClip(0, 0, 600, 800);
        view.render(game, g);
    }
 
}