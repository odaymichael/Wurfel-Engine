/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Controller;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

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
   
   /**
     * These method should define how the object can jump.
     */
    abstract void jump();
   
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
     * Returns the side of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return getSideNumb(posX,posY);
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
        
            //scale that absolute value of x and y is always the same so sqt(x^2+y^2)=1
            veloX /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            veloY /= Math.sqrt(Math.abs(veloX) + Math.abs(veloY));
            
            
            //colision check
            int oldx = posX;
            int oldy = posY;
            int newx = (int) (posX + delta * speed * veloX);
            int newy = (int) (posY + delta * speed * veloY);
            
            
            boolean movementokay=true;
            int sideNumber;
            
            //Gegenkraft berechnen udn diese zum Richtungsvektor adddieren. Gibt neuen Richtungsvektor.
            //immer bei movementokay = false;
            if (oldx-newx > 0) {
                //check left corner
                sideNumber = getSideNumb(newx - Block.WIDTH/2-1, newy);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            } else {
                //check right corner
                sideNumber = getSideNumb(newx + Block.WIDTH/2+1, newy);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            }
            
            sideNumber = getSideNumb(newx, newy - Block.WIDTH/2-1); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;

            sideNumber = getSideNumb(newx, newy + Block.WIDTH/2+1); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false; 
                
            if (oldy-newy > 0) {
                //check top corner
                sideNumber = getSideNumb(newx, newy - Block.WIDTH/2-1);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            } else {
                //check bottom corner
                sideNumber = getSideNumb(newx, newy + Block.WIDTH/2+1);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            }
            
            sideNumber = getSideNumb(newx - Block.WIDTH/2-1, newy); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;

            sideNumber = getSideNumb(newx + Block.WIDTH/2+1, newy); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;  
               
            if (movementokay) {                
                //movement allowed => move player
                posX = newx;
                posY = newy;
                
                //track the coordiante change, if there is one
                int sidennumb = getSideNumb();
                switch(sidennumb){
                    case 0:
                    case 1:
                            posY += Block.WIDTH / 2;
                            posX -= Block.WIDTH / 2;

                            makeCoordinateStep(1,-1, topblock);
                            break;
                    case 2:    
                    case 3:
                            posY -= Block.WIDTH/2;
                            posX -= Block.WIDTH/2;

                            makeCoordinateStep(1,1, topblock);
                            break;
                    case 4:
                    case 5:
                            posY -= Block.WIDTH/2;
                            posX += Block.WIDTH/2;

                            makeCoordinateStep(-1,1, topblock);
                            break;
                    case 6:
                    case 7:
                            posY += Block.WIDTH/2;
                            posX += Block.WIDTH/2;

                            makeCoordinateStep(-1,-1, topblock);
                            break;    
                }

                //if there was a coordiante change recalc map.
                if (sidennumb != 8) Controller.getMap().requestRecalc();

                //set the offset for the rendering
                setOffset(posX - Block.WIDTH/2, posY - posZ - Block.WIDTH/2);
                //copy offset to topblock
                if (topblock != null) 
                    topblock.getBlock().setOffset(getOffsetX(), getOffsetY());
            }
        }
        //enable this line to see where to player stands:
        Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()-1).setLightlevel(30);
   }
   
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
    
    /**
     * Make a step
     * @param x left or right step
     * @param y the coodinate steps
     * @param topblock if you want to also move a block on top add a pointer to it. If not wanted null.
     */
    private void makeCoordinateStep(int x, int y, Blockpointer topblock){
        selfDestroy();
        if (topblock != null) topblock.setBlock(new Block(0));
        
        setAbsCoordY(getAbsCoordY()+y);
        if (x<0){
            if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
        } else {
            if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
        }

        selfRebuild();
        if (topblock != null) topblock.setBlock(new Block(getId(),1));
        
    }
}
