package Game;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 

public class GameplayState extends BasicGameState { 
    private View View = null;
    private Controller Controller = null;
    private int stateID = -1;
    public static MsgSystem iglog;
    
 
    public GameplayState( int stateID ){
       this.stateID = stateID;
    }
 
    
    @Override
    public int getID() {
        return stateID;
    }
 
     
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

    }
    
    
    @Override 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
        iglog = new MsgSystem();
        iglog.add("Starting Game....");
        View = new View();
        Controller = new Controller(false,View,container,game);
    }
    
    
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Controller.update(gc,sbg,delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //clipping everythoing out of the viewport does not affect anything
        //g.setClip(0, 0, 600, 800);
        View.render(Controller,container,game,g);
    }
 
}