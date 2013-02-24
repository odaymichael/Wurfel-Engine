package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends SelfAwareBlock {
    public static int RADIUS = 2;

    protected ExplosiveBarrel() {
    }
    
    /**
     * Creates a barrel
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
                    Controller.setMapData(getCoordX()+x, getCoordY()+y, getCoordZ()+z, new Block());
         Controller.getMap().requestRecalc();
    }
    
}
