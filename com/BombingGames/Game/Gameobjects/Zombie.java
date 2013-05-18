/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Benedikt
 */
public class Zombie extends AbstractCharacter{

    public Zombie(int id) {
        super(id, 3);
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
            //GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
           // Color.green.bind();
            image.setImageColor(0, 0, 1);
            //image.set
            //g.;
            //calc  brightness
            float brightness = getLightlevel() / 50.0f;
            
            image.setColor(0, brightness, brightness, brightness);
            image.setColor(1, brightness, brightness, brightness);
            brightness -= 0.1f;
            image.setColor(2, brightness, brightness, brightness);
            image.setColor(3, brightness, brightness, brightness);
            
            int xpos = getScreenPosX(this, getRelCoords()) + OFFSETLIST[40][getValue()][0];
            int ypos = getScreenPosY(this, getRelCoords()) - (getDimensionY() - 1) * DIM2 + OFFSETLIST[40][getValue()][1];
            image.drawEmbedded(xpos, ypos);
            //GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        }
    }

    @Override
    public void update(int delta) {
        walk(true, false, true, false, 0.25f);
        super.update(delta);
    }
    
    
    
}
