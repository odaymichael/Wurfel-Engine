package com.BombingGames.Game.Gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *A zombie which follows the player
 * @author Benedikt
 */
public class Zombie extends AbstractCharacter{
    private AbstractCharacter target;
    
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
            Image image = getSprite(40, getValue());
            image.setImageColor(0, 0.5f, 0);
            
            int xpos = getScreenPosX(this, getRelCoords()) + OFFSETLIST[40][getValue()][0];
            int ypos = getScreenPosY(this, getRelCoords()) - (getDimensionY() - 1) * DIM2 + OFFSETLIST[40][getValue()][1];
            image.drawEmbedded(xpos, ypos);
        }
    }

    @Override
    public void update(int delta) {
        walk(
            target.getRelCoords()[1]<getRelCoords()[1]?true:false,
            target.getRelCoords()[1]>getRelCoords()[1]?true:false,
            target.getRelCoords()[0]<getRelCoords()[0]?true:false,
            target.getRelCoords()[0]>getRelCoords()[0]?true:false,
            0.35f);
        super.update(delta);
    }

    public AbstractCharacter getTarget() {
        return target;
    }

    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }
}
