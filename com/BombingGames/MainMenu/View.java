package com.BombingGames.MainMenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * The View manages ouput
 * @author Benedikt
 */
public class View {
    float startGameScale = 1;
    float exitScale = 1;
    private Image background = null;
    /**
     * 
     */
    protected MenuItem startGameOption = new MenuItem();
    /**
     * 
     */
    protected MenuItem loadGameOption = new MenuItem();
    /**
     * 
     */
    protected MenuItem exitOption = new MenuItem();
    
    
    /**
     * Creates a View.
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException{
        background = new Image("com/BombingGames/MainMenu/Images/Logo.png");
 
        // load the menu images
        Image menuOptions = new Image("com/BombingGames/MainMenu/Images/MainMenu.png");

        startGameOption.image = menuOptions.getSubImage(0, 0, 400, 50);
        startGameOption.X = (gc.getWidth() - startGameOption.image.getWidth())/2;
        startGameOption.Y = 50;
        
        loadGameOption.image = menuOptions.getSubImage(0,50,400,50);
        loadGameOption.X = (gc.getWidth()-loadGameOption.image.getWidth())/2;
        loadGameOption.Y = 100;
        
        exitOption.image = menuOptions.getSubImage(0, 100, 400, 50);
        exitOption.X = 300;
        exitOption.Y = 300;
    }

    /**
     * renders the scene
     * @param pController
     */
    public void render(Controller pController){
        // render the background
        background.draw(0,0);
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