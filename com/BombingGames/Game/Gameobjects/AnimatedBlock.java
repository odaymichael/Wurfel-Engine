package com.BombingGames.Game.Gameobjects;

/**
 *A block wich changes it's value automatically and with that it's image.
 * @author Benedikt
 */
public class AnimatedBlock extends Block {
    private int[] animationduration;
    private int timer = 0;

    /**
     * create this Block with an array wich has the time of every animation step in ms in it.
     * @param animationinformation 
     */
    protected AnimatedBlock(int[] animationinformation) {
        animationduration = animationinformation;
    }
    
    /**
     * updates the block and the animation.
     * @param delta the time wich has passed since last update
     */
    @Override
    public void update(int delta){
        timer += delta;
        if (timer >= animationduration[getValue()]){
            setValue(getValue()+1);
            timer=0;
        }
        if (getValue() >= animationduration.length) {
            setValue(0);
        }
    }
}
