import OutThere.Starter;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class OutThere {
    
    public OutThere(){
    }

    public static void main(String[] args) throws SlickException {
        //has to call starter to avoid problems with the default package. Maybe the main function can run in starter
         AppGameContainer app = new AppGameContainer(new Starter());
         //appsetDisplayMode(1280, 720, false);
         app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
         //System.out.println(app.isVSyncRequested());
         app.start();
    }
 
}