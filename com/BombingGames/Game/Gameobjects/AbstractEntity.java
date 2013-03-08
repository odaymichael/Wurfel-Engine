package com.BombingGames.Game.Gameobjects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends GameObject implements IsSelfAware {
    /**
     * Create an abstractEntity. You should use Block.getInstance(int) 
     * @param id 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    protected AbstractEntity(int id){
        super(id);
        setVisible(true);
    }
    
    /**
     * Create an entity.
     * @param id the object id of the entity.
     * @param value The value at start.
     * @param coords the coordiantes where the entity is.
     * @return the entity.
     */
    public static AbstractEntity getInstance(int id, int value, int[] coords){
        AbstractEntity entity = null;
        //define the default SideSprites
        switch (id){
            case 40:
                    try {
                        entity = new Player(id);
                        entity.setAbsCoords(coords);
                        entity.setTransparent(true);
                        entity.setObstacle(true);
                        entity.setDimensionY(2);
                    } catch (SlickException ex) {
                        Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
           case 50: 
                    entity = new AnimatedEntity(
                                id,
                                value,
                                coords,
                                new int[]{500,500},
                                true,
                                true
                            );//explosion
                    break;
        }
        entity.setValue(value);
        return entity;
    }
}
