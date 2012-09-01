package OutThere;


import Game.GameplayState;
import MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Starter extends StateBasedGame {
    int gamestate = 0;
    public static GameContainer gc;
    
    
    public Starter() throws SlickException{
        super("Out There V0.1");
    }
 
    @Override
    public void initStatesList(GameContainer pgc) throws SlickException {
        MainMenuState state = new MainMenuState(1);
        addState(state);
        addState(new GameplayState(2));
        
        gc = pgc;
    }
    
    
}