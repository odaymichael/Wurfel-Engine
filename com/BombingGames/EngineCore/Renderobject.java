package com.BombingGames.EngineCore;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;

/**
 *Saves the information for the rendering.
 * @author Benedikt
 */
public class Renderobject {
    private final Coordinate coords;
    private final int depth;
    private final int entityindex;

     /**
         * Create an Renderobject with a Block
         * @param coords 
         * @param entitynumber when it is an entity put the number here. When it is not then set it to -1.
         */
    protected Renderobject(Coordinate coords, int entitynumber) {
        this.coords = coords;
        Block block = Controller.getMapData(coords); 
        this.depth = block.getDepth(coords);
        this.entityindex = entitynumber;
    }
    
    /**
         * Create an Renderobject with an entity
         * @param entity The entity which should be contained by the renderobject.
         * @param entitynumber when it is an entity put the number here. When it is not then set it to -1.
         */
    protected Renderobject(AbstractEntity entity, int entitynumber) {
        this.coords = entity.getCoords();
        this.depth = entity.getDepth(coords);
        this.entityindex = entitynumber;
    }

    /**
     * 
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     * 
     * @return
     */
    public int getIndexposition() {
        return entityindex;
    }

    /**
     * 
     * @return
     */
    public Coordinate getCoords() {
        return coords;
    }

}