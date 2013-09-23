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
             (int) (
                (Gdx.graphics.getWidth()/2 / getEqualizationScale())
                - Block.getSpritesheet().findRegion("toolbar").originalWidth/2
             ),
             (int) (
                (Gdx.graphics.getHeight()/getEqualizationScale())
                - Block.getSpritesheet().findRegion("toolbar").originalWidth
             )
         );
     }


     @Override
     public void render(){
         super.render();
         controller.getBlockToolbar().render(this);
     } 
 }
