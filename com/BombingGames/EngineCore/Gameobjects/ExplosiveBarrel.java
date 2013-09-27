package com.BombingGames.EngineCore.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *An example for a special block: barrel block which can explode
 * @author Benedikt
 */
public class ExplosiveBarrel extends Block implements IsSelfAware {
    /**Defines the radius of the explosion.*/
    public static final int RADIUS = 2;
    private Coordinate coords;
    private static Sound explosionsound;

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
        if (explosionsound == null) explosionsound = Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/explosion2.ogg"));
    }
    
    /**
     * Explodes the barrel.
     */
    public void explode(){
        for (int x=-RADIUS; x<RADIUS; x++)
            for (int y=-RADIUS*2; y<RADIUS*2; y++)
                for (int z=-RADIUS; z<RADIUS; z++){
                    //place air
                    Controller.getMap().setDataSafe(
                        coords.addVectorCpy(x, y, z) , Block.getInstance(0)
                    );
                    
                    //spawn effect
                    if (x*x + y*y >= RADIUS*RADIUS){
                        AbstractEntity.getInstance(41, 0,
                                coords.addVectorCpy(x, y, z) 
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
