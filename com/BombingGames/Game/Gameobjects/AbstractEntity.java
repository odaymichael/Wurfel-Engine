package com.BombingGames.Game.Gameobjects;

/**
 *An entity is a game object wich is selfAware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends SelfAwareBlock {
   
    /**
     * Create an abstractEntity. You should use Block.getInstance(int) 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    protected AbstractEntity(){
        setVisible(true);
    }
}
