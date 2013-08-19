package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Coordinate;
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
    private Coordinate coords;
    private Sound explosionsound;

    /**
     * Create a explosive barrel.
     * @param id the id of the explosive barrel
     * @param coords  The coordinates where this object get's placed.
     */
    protected ExplosiveBarrel(int id, Coordinate coords){
        super(id);
        if (coords == null) throw new NullPointerException("No coordinates given to ExplosiveBarrel during creation."); 
        this.coords = coords;
        setObstacle(true);
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
        int[] relcoords = coords.getRel();
        for (int x=-RADIUS; x<RADIUS; x++)
            for (int y=-RADIUS*2; y<RADIUS*2; y++)
                for (int z=-RADIUS; z<RADIUS; z++){
                    //place air
                    Controller.setMapDataSafe(
                        new int[]{relcoords[0]+x, relcoords[1]+y, relcoords[2]+z}, Block.getInstance(0)
                    );
                    
                    //spawn effect
                    if (x*x + y*y >= RADIUS*RADIUS){
                        AbstractEntity.getInstance(41, 0,
                                coords.addVector(new int[]{x,y,z}) 
                            ).exist();
                    }
                }
         explosionsound.play();
         Controller.requestRecalc();
    }

    @Override
    public Coordinate getCoords() {
        return coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }


}
