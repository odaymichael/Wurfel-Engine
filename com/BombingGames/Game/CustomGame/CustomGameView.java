package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Controller;
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
     }
 
     @Override
     public void render(Graphics g) throws SlickException {
         super.render(g);
     } 
 }
