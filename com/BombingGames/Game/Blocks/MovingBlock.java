/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameplay;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Benedikt
 */
public abstract class MovingBlock extends SelfAwareBlock {
     /*Value in pixels*/
   protected int posX = Block.width / 2;
   protected int posY = Block.width / 2;
   protected int posZ = 0;
   
   /*The three values together build a vector. Always one of them must be 1 to prevent a division with 0.*/
   protected float veloX = 1;
   protected float veloY = 0;
   protected float veloZ = 0;
   
   //provides a factor for the vector
   protected float speed;
   
   
    MovingBlock(){
        super();
    }
    
    MovingBlock(int id){
        super(id);
    }
   
    MovingBlock(int id,int value){
        super(id, value);
    }
    
    protected int getCorner() {
        return getCorner(posX,posY);
    }
        
    protected int getCorner(int x, int y) {
        if (x+y <= Block.width /2 && getRelCoordX() > 0)
            return 7;//top left
        else if (x-y >= Block.width /2 && getRelCoordX() > 0) 
                return 1; //top right
             else if (x+y >= 3*Block.width /2 && getRelCoordY() < Chunk.BlocksY*3-1)
                    return 3;//bottom right
                else if (-x+y >= Block.width /2 && getRelCoordY() < Chunk.BlocksY*3-1)
                        return 5;//bottom left
                    else return 8;//the middle
    }
    
        /**
     * Lets the player walk.
     * @param up 
     * @param down 
     * @param delta time which has passed since last call
     * @param left 
     * @param walkingspeed 
     * @param right 
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        //if the player is walking move him
        if (up || down || left || right) {
            speed = walkingspeed;
            
            veloX = 0;
            veloY = 0;
               
            if (up)    veloY = -1;
            if (down)  veloY = 1;
            if (left)  veloX = -1;
            if (right) veloX = 1;
        
            //scale that sqt(x^2+y^2)=1
            veloX /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            veloY /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            
            //check if movement is allowed
            int corner = getCorner((int) (posX + delta*speed * veloX), (int) (posY + delta*speed * veloY));
            if (!getNeighbourBlock(corner, 0).isObstacle() && !getNeighbourBlock(corner, 1).isObstacle()) {                
                //move player by speed*vector
                posX += delta*speed * veloX;
                posY += delta*speed * veloY;
            }
        }
        

        //track the coordiante change
        if (getCorner() == 7){
            Gameplay.msgSystem.add("top left");
            posY += Block.width/2;
            posX += Block.width/2;
            
            selfDestroy();
            setAbsCoordY(getAbsCoordY()-1);
            if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
            selfRebuild();
            
            Controller.map.requestRecalc();
        } else {
            if (getCorner() == 1) {
                Gameplay.msgSystem.add("top right");
                posY += Block.width / 2;
                posX -= Block.width / 2;

                selfDestroy();
                setAbsCoordY(getAbsCoordY()-1);
                if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
                selfRebuild(); 
                
                Controller.map.requestRecalc();
            } else {
                if (getCorner() == 5) {
                    Gameplay.msgSystem.add("bottom left");
                    posY -= Block.width/2;
                    posX += Block.width/2;

                    selfDestroy();
                    setAbsCoordY(getAbsCoordY()+1);
                    if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
                    selfRebuild();
                    
                    Controller.map.requestRecalc();
                } else {
                    if (getCorner() == 3) {
                        Gameplay.msgSystem.add("bottom right");
                        posY -= Block.width/2;
                        posX -= Block.width/2;

                        selfDestroy();
                        setAbsCoordY(getAbsCoordY()+1);
                        if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
                        selfRebuild();
                        
                        Controller.map.requestRecalc();
                    }
                }
            }  
        }
        //enable this line to see where to player stands
        Controller.map.getData(getRelCoordX(), getRelCoordY(), coordZ-1).setLightlevel(40);

        //set the offset for the rendering
        setOffset(posX - Block.width/2, posY - posZ - Block.width/2);
        Controller.map.getData(getRelCoordX(), getRelCoordY(), coordZ+1).setOffset(getOffsetX(), getOffsetY());

       //GameplayState.iglog.add(getRelCoordX()+":"+getRelCoordY()+":"+coordZ);
        //System.out.println(getRelCoordX()+":"+getRelCoordY()+":"+coordZ);    
   }
   
    abstract void jump();
    
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }      
}
