package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends SelfAwareBlock {
    /**
     * Defines the radius of the explosion.
     */
    public static int RADIUS = 2;   
    
    /**
     * Explodes the barrel.
     */
    public void explode(){
        for (int x=-RADIUS;x<RADIUS;x++)
            for (int y=-RADIUS*2;y<RADIUS*2;y++)
                for (int z=-RADIUS;z<RADIUS;z++)
                    Controller.setMapDataSafe(getCoordX()+x, getCoordY()+y, getCoordZ()+z, Block.getInstance());
         Controller.getMap().requestRecalc();
    }
    
}
