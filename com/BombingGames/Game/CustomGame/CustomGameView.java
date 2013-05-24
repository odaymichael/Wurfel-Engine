package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.View;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Benedikt
 */
public class CustomGameView extends View{
     private CustomGameController controller;
 
     public CustomGameView(GameContainer gc, Controller controller) throws SlickException {
         super(gc, controller);
         this.controller = (CustomGameController) controller;
         this.controller.getBlockToolbar().setPos(
             (gc.getWidth() - Block.getSpritesheet().getSprite("toolbar").getWidth()) /2,
             gc.getHeight() - Block.getSpritesheet().getSprite("toolbar").getHeight()
         );
     }
 
     @Override
     public void render(Graphics g) throws SlickException {
         super.render(g);
         controller.getBlockToolbar().render(g);
     } 
 }
