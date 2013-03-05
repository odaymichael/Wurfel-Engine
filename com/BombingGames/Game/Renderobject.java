package com.BombingGames.Game;

/**
 *Saves the information for the rendering.
 * @author Benedikt
 */
public class Renderobject {
    private final int[] coords;
    private final int depth;
    private final int entityindex;

     /**
         * Create an Renderobject
         * @param x the x-coordiante where to render
         * @param y the y-coordiante where to render
         * @param z the z-coordiante where to render
         * @param depth the depth of the object
         * @param entitynumber when it is an entity put the number here. When it is not then set it to -1.
         */
    protected Renderobject(int[] coords, int depth, int entitynumber) {
        this.coords = coords;
        this.depth = depth;
        this.entityindex = entitynumber;
    }

    public int getDepth() {
        return depth;
    }

    public int getEntityindex() {
        return entityindex;
    }

    public int[] getCoords() {
        return coords;
    }

}