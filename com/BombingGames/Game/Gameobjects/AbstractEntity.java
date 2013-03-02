package com.BombingGames.Game.Gameobjects;

/**
 *An entity is a game object wich is selfAware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends SelfAwareBlock {
   
    /**
     * Create an abstractEntity. You should use Block.create(int) 
     * @param x
     * @param y
     * @param z
     * @see com.BombingGames.Game.Gameobjects.Block#create(int) 
     */
    protected AbstractEntity(int x,int y, int z){
        super(x,y,z);
        setVisible(true);
    }
}
