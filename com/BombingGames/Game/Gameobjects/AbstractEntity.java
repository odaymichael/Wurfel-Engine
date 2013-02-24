package com.BombingGames.Game.Gameobjects;

/**
 *An entity is a game object wich is selfaware.
 * @author Benedikt
 */
public abstract class AbstractEntity extends SelfAwareBlock {
   
    protected AbstractEntity(int x,int y, int z){
        super(x,y,z);
        setVisible(true);
    }
}
