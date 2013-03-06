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
         * @param coords 
         * @param depth the depth of the object
         * @param entitynumber when it is an entity put the number here. When it is not then set it to -1.
         */
    protected Renderobject(int[] coords, int depth, int entitynumber) {
        this.coords = coords;
        this.depth = depth;
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
    public int getEntityindex() {
        return entityindex;
    }

    /**
     * 
     * @return
     */
    public int[] getCoords() {
        return coords;
    }

}