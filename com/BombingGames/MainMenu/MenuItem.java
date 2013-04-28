package com.BombingGames.MainMenu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

/**
 *A menu item is an object wich can be placed on a menu.
 * @author Benedikt
 */
public class MenuItem {
    private static SpriteSheet spritesheet;
    private final int index;
    private final String label;
    private int X;
    private int Y;

    /**
     * 
     * @param index
     */
    public MenuItem(int index, String label) {
        this.index = index;
        this.label = label;
    }
    
    /**
     * 
     */
    public void draw(Graphics g){
        spritesheet.getSubImage(0, 1).draw(X, Y);
        g.drawString(label, X+300, Y+40-g.getFont().getLineHeight()/2);
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

    /**
     * 
     * @return
     */
    public static SpriteSheet getSpritesheet() {
        return spritesheet;
    }

    /**
     * 
     * @param spritesheet
     */
    public static void setSpritesheet(SpriteSheet spritesheet) {
        MenuItem.spritesheet = spritesheet;
    }
}
