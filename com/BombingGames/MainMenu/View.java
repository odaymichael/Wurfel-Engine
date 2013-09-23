package com.BombingGames.MainMenu;

import com.BombingGames.Wurfelengine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * The View manages ouput
 * @author Benedikt
 */
public class View {
    private final Sprite lettering;    
    private final Sprite background;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    
    /**
     * Creates a View.
     */
    public View(){
        //load textures
        lettering = new Sprite(new Texture(Gdx.files.internal("com/BombingGames/MainMenu/Images/Lettering.png")));
        lettering.setX((Gdx.graphics.getWidth() - lettering.getWidth())/2);
        lettering.setY(50);
        lettering.flip(false, true);
        
        background = new Sprite(new Texture(Gdx.files.internal("com/BombingGames/MainMenu/Images/background.png")));
        background.setX((Gdx.graphics.getWidth() - lettering.getWidth())/2);
        background.setY(50);
        background.flip(false, true);
        
        batch = new SpriteBatch();
        
        //set the center to the top left
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        font = new BitmapFont(Gdx.files.internal("com/BombingGames/EngineCore/arial.fnt"), true);
        font.setColor(Color.WHITE);
    }

    /**
     * renders the scene
     * @param controller
     */
    public void render(Controller pController){
        

        
        //update camera and set the projection matrix
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        //Background        
        batch.begin();
        for (int x = 0; x-1 < Gdx.graphics.getWidth()/background.getWidth(); x++) {
            for (int y = 0; y-1 < Gdx.graphics.getHeight()/background.getHeight(); y++) {
                background.setPosition(x*background.getWidth(), y*background.getHeight());
                background.draw(batch);
            }
        }
        batch.end();
        
        batch.begin();
        font.draw(batch, "FPS:"+ Gdx.graphics.getFramesPerSecond(), 20, 20);
        font.draw(batch, Gdx.input.getX()+ ","+Gdx.input.getY(), Gdx.input.getX(), Gdx.input.getY());
        batch.end();
        

        
        
        // render the lettering
        batch.begin();
        lettering.draw(batch);
        batch.end();
        
        // Draw the menu items
        batch.begin();
        for (MenuItem mI : MainMenuScreen.getController().getMenuItems()) {
            mI.draw(batch, camera);
        }
        batch.end();
        
        batch.begin();
        font.draw(batch, "FPS:"+ Gdx.graphics.getFramesPerSecond(), 20, 20);
        font.draw(batch, Gdx.input.getX()+ ","+Gdx.input.getY(), Gdx.input.getX(), Gdx.input.getY());
        batch.end();
        
        batch.begin();
        font.drawMultiLine(batch, Wurfelengine.getCredits(), 50, 50);
        batch.end();
    }
}

