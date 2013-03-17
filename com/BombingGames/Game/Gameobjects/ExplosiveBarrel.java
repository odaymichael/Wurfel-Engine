package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends Block implements IsSelfAware {
    /**Defines the radius of the explosion.*/
    public static int RADIUS = 2;
    private int[] coords;
    private Sound explosionsound;

    /**
     * Create a explosive barrel.
     * @param id the id
     * @param absCoords the absolute coordinates
     */
    protected ExplosiveBarrel(int id, int[] absCoords){
        super(id);
        this.coords = absCoords;
        try {
            explosionsound = new Sound("com/BombingGames/Game/Sounds/explosion2.wav");
        } catch (SlickException ex) {
            Logger.getLogger(ExplosiveBarrel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Explodes the barrel.
     */
    public void explode(){
        int[] relcoords = getRelCoords();
        for (int x=-RADIUS; x<RADIUS; x++)
            for (int y=-RADIUS*2; y<RADIUS*2; y++)
                for (int z=-RADIUS; z<RADIUS; z++){
                    Controller.setMapDataSafe(new int[]{relcoords[0]+x, relcoords[1]+y, relcoords[2]+z}, Block.getInstance(0));//place air
                    Controller.getMap().getEntitylist().add(AbstractEntity.getInstance(41, 0,
                        new int[]{
                                getAbsCoords()[0]+x,
                                getAbsCoords()[1]+y,
                                getAbsCoords()[2]+z
                            })
                   );//spawn effect
                }
         explosionsound.play();
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
        return Controller.getMap().absToRelCoords(coords);
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        setAbsCoords(new int[]{getAbsCoords()[0]+x, getAbsCoords()[1]+y, getAbsCoords()[2]+z});
    }
}
