/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Benedikt
 */
public abstract class AbstractCharacter extends AbstractEntity{
   /* Always one of them must be 1 to prevent a division with 0.*/
   private float[] dir = {1,0,0};
   
   /**
    * provides a factor for the vector
    */
   private float speed;
   
   private Sound fallingSound;
   private Sound runningSound;
   
   /**
     * These method should define what happens when the object  jumps. It should call super.jump(int velo)
     * @see com.BombingGames.Game.Gameobjects.AbstractCharacter#jump(float)
     */
    abstract void jump();
    
    /**
     * 
     * @param x
     * @param y
     * @param z
     * @see com.BombingGames.Game.Gameobjects.Block#create(int) 
     */
    protected AbstractCharacter(int x,int y, int z){
        super(x,y,z);
    }
    
    /**
     * Returns the side of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return Block.sideNumb(getPos()[0], getPos()[1]);
    }  
    
   /**
     * Lets the player walk with a second block on top.
     * @param up 
     * @param down
     * @param left 
     *  @param right 
     * @param walkingspeed the higher the speed the bigger the steps
     *  @param delta time which has passed since last call
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        //if the player is walking then move him
        if (up || down || left || right) {
            speed = walkingspeed;
            
            //update the movement vector
            dir[0] = 0;
            dir[1] = 0;
               
            if (up)    dir[1] = -1;
            if (down)  dir[1] = 1;
            if (left)  dir[0] = -1;
            if (right) dir[0] = 1;
        
            //scale that the velocity vector is always an unit vector (only x and y)
            double vectorLenght = Math.sqrt(dir[0]*dir[0]+dir[1]*dir[1]);
            dir[0] /= vectorLenght;
            dir[1] /= vectorLenght;
            //veloZ /= vectorLenght;
            
            //colision check
            float[] oldpos = getPos();
            //calculate new position
            float newx = getPos()[0] + delta * speed * dir[0];
            float newy = getPos()[1] + delta * speed * dir[1];
            
            //check if position is okay
            boolean validmovement = true;
            
            //check for movement in x
            //top corner
            int neighbourNumber = Block.sideNumb(newx, newy - Block.DIM2); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                validmovement = false;
            //bottom corner
            neighbourNumber = Block.sideNumb(newx, newy + Block.DIM2); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                validmovement = false; 
            
            //find out the direction of the movement
            if (oldpos[0] - newx > 0) {
                //check left corner
                neighbourNumber = Block.sideNumb(newx - Block.DIM2, newy);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                   validmovement = false;
            } else {
                //check right corner
                neighbourNumber = Block.sideNumb(newx + Block.DIM2, newy);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                   validmovement = false;
            }
            
            //check for movement in y
            //left corner
            neighbourNumber = Block.sideNumb(newx - Block.DIM2, newy); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                validmovement = false;

            //right corner
            neighbourNumber = Block.sideNumb(newx + Block.DIM2, newy); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                validmovement = false;  
            
            if (oldpos[1] - newy > 0) {
                //check top corner
                neighbourNumber = Block.sideNumb(newx, newy - Block.DIM2);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                   validmovement = false;
            } else {
                //check bottom corner
                neighbourNumber = Block.sideNumb(newx, newy + Block.GAMEDIMENSION/2);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber).isObstacle())
                   validmovement = false;
            }
            
            //if movement allowed => move player   
            if (validmovement) {                
                setPos(0, newx);
                setPos(1, newy);
                
                //track the coordiante change, if there is one
                int sidennumb = getSideNumb();
                if (sidennumb != 8){                
                    switch(sidennumb) {
                        case 0:
                        case 1:
                                makeCoordinateStep(1, -1);
                                break;
                        case 2:    
                        case 3:
                                makeCoordinateStep(1, 1);
                                break;
                        case 4:
                        case 5:
                                makeCoordinateStep(-1, 1);
                                break;
                        case 6:
                        case 7:
                                makeCoordinateStep(-1, -1);
                                break;    
                    }
                }
            }
        }
        //enable this line to see where to player stands:
        Controller.getMapDataSafe(getCoordX(), getCoordY(), getCoordZ()-1).setLightlevel(30);
   }
    
   /**
     * Make a step on the coordinate grid.
     * @param x left or right step
     * @param y the coodinate steps
     */
    private void makeCoordinateStep(int x, int y){
        //mirror the position around the center
        setPos(1, getPos()[1] -1*y*Block.DIM2);
        setPos(0, getPos()[0] -1*x*Block.DIM2);
        
        
        setAbsCoordY(getAbsCoordY()+y);
        if (x<0){
            if (getCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
        } else {
            if (getCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
        }
         
        
        //if there was a coordiante change recalc map.
        Controller.getMap().requestRecalc();
    }
    
   /**
     * Updates the block.
     * @param delta time since last update
     */
    @Override
    public void update(int delta) {
        if (runningSound != null)
            if (speed > 0.5f){
                if (!runningSound.playing()) runningSound.play();
            }  else runningSound.stop();
        
        //calculate movement
        float t = delta/1000f; //t = time in s
        dir[2] += -Map.GRAVITY*t; //in m/s
        float newposZ = getPos()[2] + dir[2]*Block.GAMEDIMENSION*t; //m

        //land if standing in or under 0-level and there is an obstacle
        if (dir[2] <= 0
            && newposZ <= 0
            && (getCoordZ() == 0 || Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()-1).isObstacle())
        ) {
            if (fallingSound != null) fallingSound.stop();
            dir[2] = 0;
            newposZ=0;
        }
        setPos(2, newposZ);
        
        if (fallingSound != null && dir[2]<-1f &&! fallingSound.playing()) fallingSound.play();
        
        //coordinate switch
        //down
        if (getPos()[2] < 0
            && getCoordZ() > 0
            && ! Controller.getMapDataSafe(getCoordX(), getCoordY(),getCoordZ()-1).isObstacle()){
            
          
            setCoordZ(getCoordZ()-1);

            setPos(2, getPos()[2]+ Block.GAMEDIMENSION);
            Controller.getMap().requestRecalc();
        } else {
            //up
            if (getPos()[2] >= Block.GAMEDIMENSION
                && getCoordZ() < Chunk.getBlocksZ()-2
                && !Controller.getMapDataSafe(getCoordX(), getCoordY(), getCoordZ()+2).isObstacle()){
                

               
                setCoordZ(getCoordZ()+1);

                setPos(2, getPos()[2]- Block.GAMEDIMENSION);
                Controller.getMap().requestRecalc();
            } 
        }
    }
   
    /**
     * Returns true if the player is standing on ground.
     * @return
     */
    public boolean isStanding(){
       return (dir[2] == 0 && getPos()[2] == 0);
    }

    /**
     * Jumpwith a specific speed
     * @param velo 
     */
    public void jump(float velo) {
        if (isStanding()) dir[2] = velo;
    }
    
    /**
     * Returns a normalized vector wich contains the direction of the block.
     * @return R
     */
    public float[] getDirectionVector(){
        return dir;
    }

    /**
     * Sets the sound to be played when falling
     * @param fallingSound
     */
    public void setFallingSound(Sound fallingSound) {
        this.fallingSound = fallingSound;
    }

    /**
     * Set the sound to be played when running.
     * @param runningSound
     */
    public void setRunningSound(Sound runningSound) {
        this.runningSound = runningSound;
    }
}
