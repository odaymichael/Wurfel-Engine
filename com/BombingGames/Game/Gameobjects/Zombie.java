package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import static com.BombingGames.Game.Gameobjects.AbstractEntity.CATEGORY;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.getReferencObject;
import com.badlogic.gdx.graphics.Color;

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
     */
    protected Zombie(int id) {
        super(id, 3);
        setTransparent(true);
        setObstacle(true);
        setDimensionY(2);
        for (int i = 0; i < VALUESCOUNT; i++) {
            getReferencObject()[CATEGORY][id][i] = new int[]{CATEGORY,40,i};    
        }

    }

    @Override
    public void jump() {
        super.jump(5);
    }

    @Override
    public void render(View view, WECamera camera, Coordinate coords) {
       Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getGlobalLight();
            }
        render(view, camera, coords, color.mul(Color.GREEN));
    }

    @Override
    public void update(float delta) {
        //follow the target
        walk(
            target.getCoords().getAbsY()<getCoords().getAbsY()?true:false,
            target.getCoords().getAbsY()>getCoords().getAbsY()?true:false,
            target.getCoords().getAbsX()<getCoords().getAbsX()?true:false,
            target.getCoords().getAbsX()>getCoords().getAbsX()?true:false,
            0.35f);
        
        //update as usual
        super.update(delta);
        
        //if standing on same position as in last update
//        if (Arrays.equals(new int[]{getPositionX(),getPositionY()}, lastPos) && getCoords().equals(lastCoord))
//            runningagainstwallCounter += delta;
//        else {
//            runningagainstwallCounter=0;
//            lastPos = getPos().clone();
//            lastCoord = getCoords().cpy();
//        }
        
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