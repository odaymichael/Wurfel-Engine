package com.BombingGames.Game.Gameobjects;

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
    public static int RADIUS = 2;
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
        if (explosionsound == null) explosionsound = Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/explosion2.wav"));
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
                    Controller.getMap().setDataSafe(
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