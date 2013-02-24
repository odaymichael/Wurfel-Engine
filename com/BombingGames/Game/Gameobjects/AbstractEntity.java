package com.BombingGames.Game.Gameobjects;

/**
 *A block which can move himself around the map, therefore it must also be a SelfAwareBlock.
 * @author Benedikt
 */
public abstract class AbstractEntity extends SelfAwareBlock {
   
    protected AbstractEntity(int x,int y, int z){
        super(x,y,z);
        setVisible(true);
    }
}
