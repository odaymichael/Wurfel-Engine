package com.BombingGames.Game;

import com.BombingGames.EngineCore.Gameobjects.AbstractGameObject;
import com.BombingGames.EngineCore.Gameobjects.Block;
import com.BombingGames.EngineCore.View;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.GL11;

/**
 *The Minecraft-like toolbar
 * @author Benedikt Vogler
 */
public class BlockToolbar {
    private int posX;
    private int posY;
    private final Block[] slot = new Block[9];
    private int selection = 0;

    public BlockToolbar() {
        slot[0] = Block.getInstance(1);
        slot[1] = Block.getInstance(2);
        slot[2] = Block.getInstance(3);
        slot[3] = Block.getInstance(4);
        slot[4] = Block.getInstance(5);
        slot[5] = Block.getInstance(6);
        slot[6] = Block.getInstance(7);
        slot[7] = Block.getInstance(8);
        slot[8] = Block.getInstance(21);
    }

    public void setSlot(int slot, int id) {
        this.slot[slot] = Block.getInstance(id);
    } 
    
     
    public void setPos(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }
    
    public int getSelectionID(){
        return slot[selection].getId();
    }
    
    public void render(View view){
        SpriteBatch batch = view.getBatch();
        batch.begin();
        batch.setColor(Color.WHITE);
        view.setDrawmode(GL11.GL_MODULATE);
        batch.draw(AbstractGameObject.getSpritesheet().findRegion("toolbar"), posX, posY);
        batch.draw(AbstractGameObject.getSpritesheet().findRegion("selection"), posX+80*selection, posY-5);
        
        for (int i = 0; i < 9; i++) {
            if (slot[i].getId() != 0){
                slot[i].renderAt(view, posX+i*80+17, posY+15, Color.GRAY.cpy(), true, -0.65f);
            }
        }
        batch.end();
    }
}
