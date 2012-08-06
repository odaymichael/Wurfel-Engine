/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainMenu;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Benedikt
 */
public class CustomImage extends Image {
    public int X = 0;
    public int Y = 0;

    CustomImage(String mainMenuImagesbgpng) throws SlickException {
        super(mainMenuImagesbgpng);
    }

    @Override
    public void draw(){
        draw(X,Y);
    }
    
   
    
    
}
