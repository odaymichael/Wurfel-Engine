package com.BombingGames.Game;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Gameobjects.AbstractCharacter;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import com.badlogic.gdx.graphics.Color;
import java.util.Arrays;

/**
 *A zombie which can follow a character.
 * @author Benedikt Vogler
 */
public class Zombie extends AbstractCharacter{
    private AbstractCharacter target;
    private int runningagainstwallCounter = 0;
    private float[] lastPos;
    private Coordinate lastCoord;
    
    /**
     * Zombie constructor. Use AbstractEntitiy.getInstance to create an zombie.
     * @param id
     * @param coords  
     */
    public Zombie(int id, Coordinate coords) {
        super(id, 3, coords);
        setTransparent(true);
        setObstacle(true);
        setDimensionZ(2);
    }

    @Override
    public void jump() {
        super.jump(5);
    }

    @Override
    public void render(View view, WECamera camera, Coordinate coords) {
        getSprites()[CATEGORY][43][getValue()] = getSprites()[CATEGORY][40][getValue()];//reference player sprite
        Color color = Color.GRAY.cpy();
        if (Controller.getLightengine() != null){
            color = Controller.getLightengine().getGlobalLight();
        }
        render(view, camera, coords, color.mul(Color.GREEN));
    }

    @Override
    public void update(float delta) {
        //follow the target
        walk(
            (target.getCoords().getAbsY()<getCoords().getAbsY()),
            (target.getCoords().getAbsY()>getCoords().getAbsY()),
            (target.getCoords().getAbsX()<getCoords().getAbsX()),
            (target.getCoords().getAbsX()>getCoords().getAbsX()),
            0.35f
        );
        
        //update as usual
        super.update(delta);
        
        //if standing on same position as in last update
        if (Arrays.equals(new float[]{getPositionX(),getPositionY()}, lastPos) && getCoords().equals(lastCoord))
            runningagainstwallCounter += delta;
        else {
            runningagainstwallCounter=0;
            lastPos = new float[]{getPositionX(),getPositionY()};
            lastCoord = getCoords().cpy();
        }
        
        //jump after one second
        if (runningagainstwallCounter > 50) {
            jump();
            runningagainstwallCounter=0;
        }
    }

    /**
     * Set the target which the zombie follows.
     * @param target an character
     */
    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }
}