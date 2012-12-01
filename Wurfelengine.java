import Game.Gameplay;
import MainMenu.MainMenuState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Wurfelengine extends StateBasedGame {
    public static AppGameContainer app = null; 
    public int gamestate = 0;
    public static GameContainer gc;
    public static final String version = "0.2";
        
    public Wurfelengine(){
        super("Wurfelengine V" + version);
    }

    public static void main(String[] args) throws SlickException {
        //has to call starter to avoid problems with the default package. Maybe the main function can run in starter
         app = new AppGameContainer(new Wurfelengine());         
         app.setUpdateOnlyWhenVisible(true);
         
         //you can start the game with a custom resolution
         if (args.length==0)
            app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), false);
         else app.setDisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), false);
         //System.out.println(app.isVSyncRequested());
         app.start();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        MainMenuState state = new MainMenuState(1);
        addState(state);
        addState(new Gameplay(2));
        
        gc = container;
    }
 
}