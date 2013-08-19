package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 *A menu item is an object wich can be placed on a menu.
 * @author Benedikt
 */
public class MenuItem extends Sprite {
    private static TextureAtlas spritesheet;
    private final int index;

    /**
     * 
     * @param index
     */
    public MenuItem(int index, Texture texture) {
        super(texture);
        this.index = index;
        this.setX(500);
        this.setY(index*200);
    }



    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
    }
    

    /**
     * 
     * @param input
     * @return
     */
    public boolean isClicked() {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        
        return (
            Gdx.input.isButtonPressed(Buttons.LEFT) &&
            (mouseX >= getX() && mouseX <= getX() + getWidth()) &&
            (mouseY >= getX() && mouseY <= getY() + getHeight())
        );
    }

    /**
     * 
     * @return
     */
    public static TextureAtlas getSpritesheet() {
        return spritesheet;
    }

    /**
     * 
     * @param spritesheet
     */
    public static void setSpritesheet(TextureAtlas spritesheet) {
        MenuItem.spritesheet = spritesheet;
    }
}
