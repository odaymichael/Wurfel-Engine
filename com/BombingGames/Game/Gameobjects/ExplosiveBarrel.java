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
        if (absCoords == null) throw new NullPointerException("No coordinates given to ExplosiveBarrel during creation."); 
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
                    //place air
                    Controller.setMapDataSafe(
                        new int[]{relcoords[0]+x, relcoords[1]+y, relcoords[2]+z}, Block.getInstance(0)
                    );
                    
                    //spawn effect
                    if (x*x + y*y >= RADIUS*RADIUS){
                        Controller.getMap().getEntitylist().add(
                            AbstractEntity.getInstance(41, 0,
                                new int[]{
                                    getAbsCoords()[0]+x,
                                    getAbsCoords()[1]+y,
                                    getAbsCoords()[2]+z
                                }
                            )
                        );
                    }
                }
         explosionsound.play();
         Controller.requestRecalc();
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
    public void setRelCoords(int[] relCoords) {
        this.coords = Controller.getMap().relToAbsCoords(relCoords);
    }

    @Override
    public void addVector(int[] coords) {
        setAbsCoords(new int[]{getAbsCoords()[0]+coords[0], getAbsCoords()[1]+coords[1], getAbsCoords()[2]+coords[2]});
    }
}
