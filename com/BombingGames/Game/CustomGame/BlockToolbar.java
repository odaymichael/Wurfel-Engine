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
    private int[] slot = new int[9];

    public BlockToolbar() {
        slot[0] = 1;
        slot[1] = 2;
        slot[2] = 3;
        slot[3] = 4;
        slot[4] = 5;
        slot[5] = 6;
        slot[6] = 7;
        slot[7] = 8;
        slot[8] = 9;
    }

    
    public void setSlot(int slot, int id) {
        this.slot[slot] = id;
    } 
    
     
    public void setPos(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    
    public void render(Graphics g){
        Block.getSpritesheet().getSprite("gui").draw(posX, posY); 
        for (int i = 0; i < 9; i++) {
            if (slot[i]!=0){
                Block.getBlockSprite(slot[i], 0,1).draw(posX+i*80+17, posY+15, 0.35f);
                Block.getBlockSprite(slot[i], 0,0).draw(posX+i*80+17, posY+15+14, 0.35f);
                Block.getBlockSprite(slot[i], 0,2).draw(posX+i*80+17+28, posY+15+14, 0.35f);
            }
        }

    }

}
