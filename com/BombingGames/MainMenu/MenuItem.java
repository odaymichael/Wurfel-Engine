package com.BombingGames.MainMenu;

import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

/**
 *A menu item is an object wich can be placed on a menu.
 * @author Benedikt
 */
public class MenuItem {

    private static SpriteSheet spritesheet;
    private final int index;
    private int X = 0;
    private int Y = 0;

    /**
     * 
     * @param index
     */
    public MenuItem(int index) {
        this.index = index;
    }
    
    /**
     * 
     */
    public void draw(){
        spritesheet.renderInUse(X, Y, 0, index);
    }

    /**
     * 
     * @param input
     * @return
     */
    public boolean isClicked(Input input) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        return (
            input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) &&
            (mouseX >= X && mouseX <= X + spritesheet.getSprite(0, 0).getWidth()) &&
            (mouseY >= Y && mouseY <= Y + spritesheet.getSprite(0, 0).getHeight())
        );
    }

    /**
     * 
     * @return
     */
    public int getX() {
        return X;
    }

    /**
     * 
     * @param X
     */
    public void setX(int X) {
        this.X = X;
    }

    /**
     * 
     * @return
     */
    public int getY() {
        return Y;
    }

    /**
     * 
     * @param Y
     */
    public void setY(int Y) {
        this.Y = Y;
    }

    public static SpriteSheet getSpritesheet() {
        return spritesheet;
    }

    public static void setSpritesheet(SpriteSheet spritesheet) {
        MenuItem.spritesheet = spritesheet;
    }
}
