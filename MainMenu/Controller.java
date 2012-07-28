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
 
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        if (
             ( mouseX >= 50 && mouseX <= 50 + View.startGameOption.getWidth()) &&
             ( mouseY >= 50 && mouseY <= 50 + View.startGameOption.getHeight()) &&
             ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            ) {
            fx.play(); 
            sbg.enterState(2);            
        }
    }
   
}
