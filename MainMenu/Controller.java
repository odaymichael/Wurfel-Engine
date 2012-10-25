package MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

public class Controller {
    View View = null;
    Sound fx = null;
    public FadeInTransition transition = null;
    
    public Controller(View pView) throws SlickException{
        View = pView;
        fx = new Sound("MainMenu/click2.wav");
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta){
        Input input = gc.getInput();
         
        if (View.startGameOption.isClicked(input)){
            MainMenuState.loadmap = false;
            fx.play(); 
            sbg.enterState(2);            
        } else if (View.loadGameOption.isClicked(input)) { 
                MainMenuState.loadmap = true;
                fx.play();
                sbg.enterState(2); 
        }else if (View.exitOption.isClicked(input)){
            gc.exit();
        }
        
    }
   
   
}
