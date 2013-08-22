package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 *A menu item is an object wich can be placed on a menu.
 * @author Benedikt
 */
public class MenuItem extends Sprite {
    private final int index;

    /**
     * 
     * @param index
     */
    public MenuItem(int index, TextureRegion texture) {
        super(texture);
        this.index = index;
        this.setX((Gdx.graphics.getWidth()-getWidth())/2);
        this.setY(index*80);
    }


    public void draw(SpriteBatch spriteBatch, Camera camera) {
        super.draw(spriteBatch);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
      
        shapeRenderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
        shapeRenderer.filledRect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        
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
            (mouseY >= getY() && mouseY <= getY() + getHeight())
        );
    }
}
