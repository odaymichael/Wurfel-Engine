package MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
public class MainMenuState extends BasicGameState{
    public static boolean loadmap = false;
    int stateID = 1;
    public static GameContainer gc;
    public static StateBasedGame sbg;
 
    View View = null;
    Controller Controller = null;
    
    public MainMenuState( int stateID) {
       this.stateID = stateID;
    }

 
    @Override
    public int getID() {
        return stateID;
    }
 
    @Override
    public void init(GameContainer pgc, StateBasedGame psbg) throws SlickException {
        gc = pgc;
        sbg = psbg;
        View = new View(pgc);
        Controller = new Controller(View); 
    }
  
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Controller.update(gc,sbg,delta);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        View.render(Controller);
        
    }
}