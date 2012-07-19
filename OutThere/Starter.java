package OutThere;


import Game.GameplayState;
import MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Starter extends StateBasedGame {
    int gamestate = 0;
    
    public Starter() throws SlickException{
        super("SlickBlocks");
    }
    
 
 
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new MainMenuState(1));
        this.addState(new GameplayState(2));
    }
}