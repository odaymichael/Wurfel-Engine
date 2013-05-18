package com.BombingGames.Game.Gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *A zombie which can follow a character
 * @author Benedikt Vogler
 */
public class Zombie extends AbstractCharacter{
    private AbstractCharacter target;
    
    /**
     * Zombie constructor. Use AbstractEntitiy.getInstance to create an zombie.
     * @param id
     */
    protected Zombie(int id) {
        super(id, 3);
        setTransparent(true);
        setObstacle(true);
        setDimensionY(2);
    }

    @Override
    public void jump() {
        super.jump(5);
    }

    @Override
    public void render(Graphics g) {
        //draw the object except not visible ones
        if (!isHidden() && isVisible()) {
            //get the player sprite
            Image image = getSprite(40, getValue());
            //color it green
            image.setImageColor(0, 0.5f, 0);
            
            int xpos = getScreenPosX(this, getRelCoords()) + OFFSETLIST[40][getValue()][0];
            int ypos = getScreenPosY(this, getRelCoords()) - (getDimensionY() - 1) * DIM2 + OFFSETLIST[40][getValue()][1];
            image.drawEmbedded(xpos, ypos);
        }
    }

    @Override
    public void update(int delta) {
        //follow the target
        walk(
            target.getRelCoords()[1]<getRelCoords()[1]?true:false,
            target.getRelCoords()[1]>getRelCoords()[1]?true:false,
            target.getRelCoords()[0]<getRelCoords()[0]?true:false,
            target.getRelCoords()[0]>getRelCoords()[0]?true:false,
            0.35f);
        //update as usual
        super.update(delta);
    }

    /**
     * Set the target which the zombie follows.
     * @param target an character
     */
    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }
}