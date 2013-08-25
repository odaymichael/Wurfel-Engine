package com.BombingGames.EngineCore;

import com.BombingGames.Game.CustomGameController;
import com.BombingGames.Game.CustomGameView;
import com.BombingGames.Wurfelengine;
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
     * @param id the id used by this state.
     */
    public GameplayScreen() {
        msgSystem = new MsgSystem(Gdx.graphics.getWidth()/2, 3*Gdx.graphics.getHeight()/4);
        //Wurfelengine.getGameContainer().setSmoothDeltas(true);
        
        controller = new CustomGameController();
        view = new CustomGameView(controller);
        controller.setView(view);
    }
             

    /**
     * Returns the Message System. Use .add() to add messages to the msgSystem.
     * @return The msgSystem.
     */
    public static MsgSystem msgSystem() {
        return msgSystem;
    }

    @Override
    public void render(float delta) {
        controller.update((int) (delta*1000));
        view.render();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.graphics.setTitle("Wurfelengine V" + Wurfelengine.VERSION + " " + Gdx.graphics.getWidth() + "x"+Gdx.graphics.getHeight());
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