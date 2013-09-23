package com.BombingGames.Game;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.View;
import com.BombingGames.Game.Gameobjects.Block;
import com.badlogic.gdx.Gdx;


/**
 *
 * @author Benedikt
 */
public class CustomGameView extends View{
     private CustomGameController controller;
 
     /**
     *
     * @param controller
     */
    public CustomGameView(Controller controller) {
         super(controller);
         this.controller = (CustomGameController) controller;
         
         this.controller.getBlockToolbar().setPos(
            (Gdx.graphics.getWidth()/2)
            - Block.getSpritesheet().findRegion("toolbar").originalWidth/2,
            (Gdx.graphics.getHeight())
            - Block.getSpritesheet().findRegion("toolbar").originalHeight);
     }


     @Override
     public void render(){
         super.render();
         controller.getBlockToolbar().render(this);
     } 
 }
