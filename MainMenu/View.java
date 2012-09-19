package MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

//import com.BombingGames.View.Blockimages;

public class View {
    float startGameScale = 1;
    float exitScale = 1;
    private Image background = null;
    public MenuItem startGameOption = new MenuItem();
    public MenuItem loadGameOption = new MenuItem();
    public MenuItem exitOption = new MenuItem();
    
    
    //Konstruktor
    public View(GameContainer pgc) throws SlickException{
        background = new Image("MainMenu/Images/landschaft.jpg");
 
        // load the menu images
        Image menuOptions = new Image("MainMenu/Images/MainMenu.png");

        startGameOption.image = menuOptions.getSubImage(0, 0, 400, 50);
        startGameOption.X = (pgc.getScreenWidth() - startGameOption.image.getWidth())/2;
        startGameOption.Y = 50;
        
        loadGameOption.image = menuOptions.getSubImage(0,50,400,50);
        loadGameOption.X = (pgc.getScreenWidth()-loadGameOption.image.getWidth())/2;
        loadGameOption.Y = 100;
        
        exitOption.image = menuOptions.getSubImage(0, 100, 400, 50);
        exitOption.X = 300;
        exitOption.Y = 300;
    }

    public void render(Controller pController){
        // render the background
        background.draw(0,0,1920,1080);
        // Draw menu

        startGameOption.draw();
        loadGameOption.draw();
        exitOption.draw();
    }
    /*private static int window_width(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.width - insets.left - insets.right;
        //Controller.ChunkSizeX = value ;
        return value;
   }
   
    private static int window_height(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.height - insets.top - insets.bottom;
        //Controller.ChunkSizeY = value ;
        return value;
   }*/
}

class MenuItem{
    public int X = 0;
    public int Y = 0;
    public Image image;
    
    public void draw(){
        image.draw(X,Y);
    }

    boolean isClicked(Input input) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        if (
            (mouseX >= X && mouseX <= X + image.getWidth()) &&
            (mouseY >= Y && mouseY <= Y + image.getHeight()) &&
            (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
           )
            return true;
       else
            return false;
    }
    

}