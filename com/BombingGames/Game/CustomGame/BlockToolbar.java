package com.BombingGames.Game.CustomGame;

import com.BombingGames.Game.Gameobjects.Block;
import org.newdawn.slick.Graphics;

/**
 *The Minecraft-like toolbar
 * @author Benedikt Vogler
 */
public class BlockToolbar {
    private int posX;
    private int posY;
     
    public void setPos(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    
    public void render(Graphics g){
        Block.getSpritesheet().getSprite("gui").draw(posX, posY); 
    }

}
