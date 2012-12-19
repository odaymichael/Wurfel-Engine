/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Controller;
import org.newdawn.slick.SlickException;

/**
 *A Block which can move himself around the map therefore it must also be  a SelfAwareBlock.
 * @author Benedikt
 */
public abstract class MovingBlock extends SelfAwareBlock {
    /**
     * Value in pixels
     */
    private int posX = Block.WIDTH / 2;
   /**
    * Value in pixels
    */
   protected int posY = Block.WIDTH / 2;
   /**
    * Value in pixels
    */
   protected int posZ = 0;
   
   /*The three values together build a vector. Always one of them must be 1 to prevent a division with 0.*/
   /**
    * 
    */
   protected float veloX = 1;
   /**
    * 
    */
   protected float veloY = 0;
   /**
    * 
    */
   protected float veloZ = 0;
   
   /**
    * provides a factor for the vector
    */
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
    
    /**
     * Returns the coodinates of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getCorner(int, int) 
     */
    protected int getCorner() {
        return getCorner(posX,posY);
    }
        
    /**
     * Lets the player walk
     * @param up
     * @param down
     * @param left
     * @param right
     * @param walkingspeed the higher the speed the bigger the steps
     * @param delta time which has passed since last call
     * @throws SlickException 
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        walk(up, down, left, right, walkingspeed,  delta, null);
    }
    
    
   /**
     * Lets the player walk with a second block on top.
     * @param up 
     * @param down
     * @param left 
     *  @param right 
     * @param walkingspeed the higher the speed the bigger the steps
     *  @param delta time which has passed since last call
     * @param topblock The block who should be on top.
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta, Blockpointer topblock) throws SlickException {
        //if the player is walking then move him
        if (up || down || left || right) {
            speed = walkingspeed;
            
            //update the movement vector
            veloX = 0;
            veloY = 0;
               
            if (up)    veloY = -1;
            if (down)  veloY = 1;
            if (left)  veloX = -1;
            if (right) veloX = 1;
        
            //scale that sqt(x^2+y^2)=1
            veloX /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            veloY /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            
            int newx = (int) (posX + delta*speed * veloX);
            int newy = (int) (posY + delta*speed * veloY);
            
            //check if movement is allowed
            int corner = getCorner(newx, newy);
            
            //goal of step is free?
            if (!getNeighbourBlock(corner, 0).isObstacle() && !getNeighbourBlock(corner, 1).isObstacle()) {                
                //move player by speed*vector
                posX = newx;
                posY = newy;
            }
            
            //track the coordiante change
            if (getCorner() == 7){
                posY += Block.WIDTH/2;
                posX += Block.WIDTH/2;

                selfDestroy();
                if (topblock != null) topblock.setBlock(new Block(0));
                setAbsCoordY(getAbsCoordY()-1);
                if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
                selfRebuild();
                if (topblock != null) topblock.setBlock(new Block(40,1));

                Controller.getMap().requestRecalc();
            } else {
                if (getCorner() == 1) {
                    posY += Block.WIDTH / 2;
                    posX -= Block.WIDTH / 2;

                    selfDestroy();
                    if (topblock != null) topblock.setBlock(new Block(0));
                    setAbsCoordY(getAbsCoordY()-1);
                    if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
                    selfRebuild();
                    if (topblock != null) topblock.setBlock(new Block(40,1));

                    Controller.getMap().requestRecalc();
                } else {
                    if (getCorner() == 5) {
                        posY -= Block.WIDTH/2;
                        posX += Block.WIDTH/2;

                        selfDestroy();
                        if (topblock != null) topblock.setBlock(new Block(0));
                        setAbsCoordY(getAbsCoordY()+1);
                        if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
                        selfRebuild();
                        if (topblock != null) topblock.setBlock(new Block(40,1));

                        Controller.getMap().requestRecalc();
                    } else {
                        if (getCorner() == 3) {
                            posY -= Block.WIDTH/2;
                            posX -= Block.WIDTH/2;

                            selfDestroy();
                            if (topblock != null) topblock.setBlock(new Block(0));
                            setAbsCoordY(getAbsCoordY()+1);
                            if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
                            selfRebuild();
                            if (topblock != null) topblock.setBlock(new Block(40,1));

                            Controller.getMap().requestRecalc();
                        }
                    }
                }  
            }
             //set the offset for the rendering
            setOffset(posX - Block.WIDTH/2, posY - posZ - Block.WIDTH/2);
            if (topblock != null) topblock.getBlock().setOffset(getOffsetX(), getOffsetY());
        }
        //enable this line to see where to player stands:
        Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()-1).setLightlevel(30);
   }
   
    
    
    /**
     * These method should define how the object can jump.
     */
    abstract void jump();
    
    /**
     * 
     * @return
     */
    public int getPosX() {
        return posX;
    }

    /**
     * 
     * @return
     */
    public int getPosY() {
        return posY;
    }

    /**
     * 
     * @return
     */
    public int getPosZ() {
        return posZ;
    }      
}
