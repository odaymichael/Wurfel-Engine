package com.BombingGames.EngineCore;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.Game.Gameobjects.AbstractGameObject;

/**
 *Saves the information for the rendering. This class is only used in the rendering process.
 * @author Benedikt
 */
public class Renderobject {
    private final Coordinate coords;
    private final int depth;
    private AbstractGameObject content;

     /**
         * Create an Renderobject with a regular Block in the map
         * @param object 
         * @param coords The coordinates where the object should be rendered
         */
    protected Renderobject(AbstractGameObject object, Coordinate coords) {
        this.coords = coords;
        this.depth = object.getDepth(coords);
        content = object;
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
    public Coordinate getCoords() {
        return coords;
    }

    /**
     *
     * @return
     */
    public AbstractGameObject getObject() {
        return content;
    }

    
}