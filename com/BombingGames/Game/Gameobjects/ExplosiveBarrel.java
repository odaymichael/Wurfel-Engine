package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends Block implements ISelfAware {
    /**
     * Defines the radius of the explosion.
     */
    public static int RADIUS = 2;
    
    private int[] coords;

    protected ExplosiveBarrel(int id) {
        super(id);
    }
    
    /**
     * Explodes the barrel.
     */
    public void explode(){
        for (int x=-RADIUS;x<RADIUS;x++)
            for (int y=-RADIUS*2;y<RADIUS*2;y++)
                for (int z=-RADIUS;z<RADIUS;z++){
                    int[] tmp = new int[]{coords[0]+x, coords[1]+y, coords[2]+z};
                    Controller.setMapDataSafe(tmp, Block.getInstance());
                    Controller.getMap().getEntitylist().add(AbstractEntity.getInstance(50, 0, tmp));
                }
         Controller.getMap().requestRecalc();
    }

    @Override
    public int[] getAbsCoords() {
      return coords; 
    }

    @Override
    public void setAbsCoords(int[] coords) {
        this.coords = coords;
    }

    @Override
    public int[] getRelCoords() {
        return Controller.getMap().absoluteToRelativeCoords(coords);
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        setAbsCoords(new int[]{getAbsCoords()[0]+x, getAbsCoords()[1]+y, getAbsCoords()[2]+z});
    }
}
