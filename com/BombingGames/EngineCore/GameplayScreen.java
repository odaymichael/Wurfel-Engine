package com.BombingGames.EngineCore;

import com.BombingGames.WurfelEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * The GameplayScreen State. This is state where the magic happens.
 * @author Benedikt
 */
public class GameplayScreen implements Screen{ 
  /**
     * Contains the Message System
     */
    private static MsgSystem msgSystem;    
    
    private View view = null;
    private Controller controller = null;
    
    /**
     * Create the gameplay state.
     * @param controller The controller of this screen.
     * @param view  The view of this screen.
     */
    public GameplayScreen(Controller controller, View view) {
        msgSystem = new MsgSystem(Gdx.graphics.getWidth()/2, 3*Gdx.graphics.getHeight()/4);
        //Wurfelengine.getGameContainer().setSmoothDeltas(true);
        
        this.controller = controller;
        this.controller.init();
        this.view = view;
        this.view.init(controller);
    }
             

    /**
     * Returns the Message System. Use .add() to add messages to the msgSystem.
     * @return The msgSystem.
     */
    public static MsgSystem msgSystem() {
        return msgSystem;
    }

    public View getView() {
        return view;
    }

    public Controller getController() {
        return controller;
    }
    
    

    @Override
    public void render(float delta) {
        controller.update(delta*1000);
        view.render();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.graphics.setTitle("Wurfelengine V" + WurfelEngine.VERSION + " " + Gdx.graphics.getWidth() + "x"+Gdx.graphics.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
    
}