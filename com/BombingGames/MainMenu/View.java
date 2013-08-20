package com.BombingGames.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    
    /**
     * Creates a View.
     * @param gc
     * @throws SlickException
     */
    public View(){
        //load textures
        lettering = new Texture(Gdx.files.internal("com/BombingGames/MainMenu/Images/Lettering.png"));
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/MainMenu/Images/MainMenu.txt"));
        
        batch = new SpriteBatch();
        
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * renders the scene
     * @param pController
     */
    public void render(Controller pController){
        //clear & set background to black
        GL11.glClearColor( 0f, 0f, 0f, 1f );
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // render the lettering
        batch.begin();
        batch.draw(lettering, (Gdx.graphics.getWidth() - lettering.getWidth())/2,0);
        batch.end();
        
        // Draw the menu items
        batch.begin();
        for (MenuItem mI : MainMenuScreen.getController().getMenuItems()) {
            mI.draw(batch);
        }
        batch.end();
    }
}

