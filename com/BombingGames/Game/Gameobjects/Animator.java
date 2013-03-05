package com.BombingGames.Game.Gameobjects;

/**
 *A block wich changes it's value automatically and with that it's image.
 * @author Benedikt
 */
public class Animator{
    private Animator(){};
    
    protected static int updateAnimation(Object object, int[] durations, int delta, int counter, boolean loop) {
            counter += delta;
            if (counter >= durations[object.getValue()]){
                object.setValue(object.getValue()+1);
                counter=0;
            }
            if (loop && object.getValue() >= durations.length) {
                object.setValue(0);
            }
            return counter;
    }
}
