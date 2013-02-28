package com.BombingGames.MainMenu;

import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Benedikt
 */
public class MenuItem {
    public static SpriteSheet spritesheet;
    public final int index;
    private int X = 0;
    private int Y = 0;

    public MenuItem(int index) {
        this.index = index;
    }
    
    public void draw(){
        spritesheet.renderInUse(X, Y, 0, index);
    }

    public boolean isClicked(Input input) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        return (
            input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) &&
            (mouseX >= X && mouseX <= X + spritesheet.getSprite(0, 0).getWidth()) &&
            (mouseY >= Y && mouseY <= Y + spritesheet.getSprite(0, 0).getHeight())
        );
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }
}
