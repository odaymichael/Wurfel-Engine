package com.BombingGames.Game.Blocks;

/**
 *
 * @author Benedikt
 */
public abstract class AnimatedBlock extends Block {
    
    private int delta = 0;

    public AnimatedBlock(int id) {
        super(id);
    }

    
    public void updateGFX(){
        delta++;
        if (delta>2000)
            setValue(2);
            else
            setValue(1);
        
    }

    
}
