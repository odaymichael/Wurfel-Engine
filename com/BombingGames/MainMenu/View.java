package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.lwjgl.opengl.GL11;


/**
 * The View manages ouput
 * @author Benedikt
 */
public class View {
    private final Texture lettering;    
    private SpriteBatch batch;
    
    /**
     * Creates a View.
     * @param gc
     * @throws SlickException
     */
    public View(){
        lettering = new Texture(Gdx.files.internal("com/BombingGames/MainMenu/Images/Lettering.png"));
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/MainMenu/Images/MainMenu.txt"));
        batch = new SpriteBatch();
    }

    /**
     * renders the scene
     * @param pController
     */
    public void render(Controller pController){
        GL11.glClearColor( 0f, 1f, 0f, 1f );
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // render the lettering
//        batch.begin();
//        batch.draw(lettering, (Gdx.graphics.getWidth() - lettering.getWidth())/2,0);
//        batch.end();
        
        // Draw menu
        batch.begin();
        MainMenuScreen.getController().getMenuItems()[0].draw(batch);
        MainMenuScreen.getController().getMenuItems()[1].draw(batch);
        MainMenuScreen.getController().getMenuItems()[2].draw(batch);
        batch.end();
    }
}

