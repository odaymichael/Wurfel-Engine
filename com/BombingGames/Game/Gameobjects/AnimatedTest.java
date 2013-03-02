package com.BombingGames.Game.Gameobjects;

/**
 *
 * @author Benedikt
 */
public class AnimatedTest extends AbstractAnimatedBlock {
    private int timer = 0;
    
    protected AnimatedTest(){}
    
    @Override
    public void update(int delta){
        timer += delta;
        if (timer > 100)
            setValue(1);
        if (timer > 500) {
            setValue(0);
            timer=0;
        }
        
    }
}
