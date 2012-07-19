import OutThere.Starter;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class OutThere {
    
    public OutThere(){
    }
 
    public static void main(String[] args) throws SlickException {
         AppGameContainer app = new AppGameContainer(new Starter());
         app.setDisplayMode(800, 800, false);
         app.start();
    }
 
}