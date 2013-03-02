package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends SelfAwareBlock {
    /**
     * the radius of the explosion
     */
    public static int RADIUS = 2;

    /**
     * 
     *  @see com.BombingGames.Game.Gameobjects.Block#create(int) 
     */
    protected ExplosiveBarrel() {
    }
    
    /**
     * Creates a barrel
     * @param x 
     * @param y 
     * @param z 
     * @see com.BombingGames.Game.Gameobjects.Block#create(int) 
     */
    protected ExplosiveBarrel(int x, int y, int z) {
        super(x,y,z);
    }
    
    
    /**
     * explodes the barrel. No safety checks so pay attention!
     */
    public void explode(){
        for (int x=-RADIUS;x<RADIUS;x++)
            for (int y=-RADIUS*2;y<RADIUS*2;y++)
                for (int z=-RADIUS;z<RADIUS;z++)
                    Controller.setMapDataSafe(getCoordX()+x, getCoordY()+y, getCoordZ()+z, Block.create());
         Controller.getMap().requestRecalc();
    }
    
}
