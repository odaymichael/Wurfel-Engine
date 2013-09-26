package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 *A menu item is an object wich can be placed on a menu.
 * @author Benedikt
 */
public class MenuItem extends Sprite {
    /**
     * Create a new menu Item and say which texture it should have.
     * @param index
     * @param texture  
     */
    public MenuItem(int index, TextureRegion texture) {
        super(texture);
        this.setX((Gdx.graphics.getWidth()-getWidth())/2);
        this.setY(Gdx.graphics.getHeight()/2-120+index*80);
    }

    MenuItem(int index, TextureRegion texture, String title) {
        this(index, texture);
    }


    /**
     *
     * @param spriteBatch
     * @param camera The camera rendering the MenuItem
     */
    public void render(SpriteBatch spriteBatch, Camera camera) {
        super.draw(spriteBatch);        
    }
    

    /**
     * Check if ithe mouse clicked the menuItem.
     * @return
     */
    public boolean isClicked() {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        
        return (
            Gdx.input.isButtonPressed(Buttons.LEFT) &&
            (mouseX >= getX() && mouseX <= getX() + getWidth()) &&
            (mouseY >= getY() && mouseY <= getY() + getHeight())
        );
    }
}