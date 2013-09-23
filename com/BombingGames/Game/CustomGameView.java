package com.BombingGames.Game;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.View;


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
     }


     @Override
     public void render(){
         super.render();
     } 
 }
