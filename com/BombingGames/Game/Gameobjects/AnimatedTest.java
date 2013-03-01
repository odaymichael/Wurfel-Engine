package com.BombingGames.Game.Gameobjects;

/**
 *
 * @author Benedikt
 */
public class AnimatedTest extends AbstractAnimatedBlock {
    private int delta = 0;
    
    protected AnimatedTest(){}
    
    @Override
    public void update(){
        delta++;
        if (delta>50)
            setValue(1);
        if (delta>100) {
            setValue(0);
            delta=0;
        }
        
    }
}
