package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *A character is an entity wich can walk around. To control the character you have to set the controls with "setControls(String controls)".
 * @author Benedikt
 */
public abstract class AbstractCharacter extends AbstractEntity {
   private final int COLISSIONRADIUS = GameObject.DIM4;
   private float[] dir = {1, 0, 0};
   private String controls = "NPC";
      
   /**provides a factor for the vector*/
   private float speed;
   private Sound fallingSound;
   private Sound runningSound;
   
   private int walkingAnimationCounter;
   private final int SPRITESPERDIR;

   /**
    * Constructor of AbstractCharacter.
    * @param id
    */
   protected AbstractCharacter(int id, int spritesPerDir) {
        super(id);
        SPRITESPERDIR = spritesPerDir;
    }
   
   /**
     * This method should define what happens when the object  jumps. It should call super.jump(int velo)
     * @see com.BombingGames.Game.Gameobjects.AbstractCharacter#jump(float)
     */
    public abstract void jump();
    
   /**
     * Jump with a specific speed
     * @param velo the velocity in m/s
     */
    public void jump(float velo) {
        if (onGround()) dir[2] = velo;
    }
    

    
   /**
     * Lets the player walk.
     * @param up move up?
     * @param down move down?
     * @param left move left?
     *  @param right move right?
     * @param walkingspeed the higher the speed the bigger the steps. Should be in m/s.
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed) throws SlickException {
        if (up || down || left || right){
        speed = walkingspeed;

        //update the movement vector
        dir[0] = 0;
        dir[1] = 0;

        if (up)    dir[1] = -1;
        if (down)  dir[1] = 1;
        if (left)  dir[0] = -1;
        if (right) dir[0] = 1;
        } else speed=0;
   }
    
   /**
     * Make a step on the coordinate grid.
     * @param x left or right step
     * @param y the coodinate steps
     */
    private void makeCoordinateStep(int x, int y){
        //mirror the position around the center
        setPos(0, getPos()[0] -x*Block.DIM2);
        setPos(1, getPos()[1] -y*Block.DIM2);

        
        setCoords(getCoords().addVector(new int[]{0, y, 0}));
        if (x < 0){
            if (getCoords().getRelY() % 2 == 1) setCoords(getCoords().addVector(new int[]{-1, 0, 0}));
        } else {
            if (getCoords().getRelY() % 2 == 0) setCoords(getCoords().addVector(new int[]{1, 0, 0}));
        }
    }
    
   /**
     * Updates the charackter.
     * @param delta time since last update
     */
    @Override
    public void update(int delta) {
        //scale that the velocity vector is always an unit vector (only x and y)
        double vectorLenght = Math.sqrt(dir[0]*dir[0] + dir[1]*dir[1]);
        if (vectorLenght > 0){
            dir[0] /= vectorLenght;
            dir[1] /= vectorLenght;
        }
            
        float oldHeight = getCoords().getHeight();
        float[] oldPos = getPos();
 
        
        /*VERTICAL MOVEMENT*/
        //calculate new height
        float t = delta/1000f; //t = time in s
        dir[2] += -Map.GRAVITY*t; //in m/s
        getCoords().setHeight(getCoords().getHeight() + dir[2] * GAMEDIMENSION * t); //in m
        
        //check new neight for colission
        //land if standing in or under 0-level or there is an obstacle
        if ((dir[2] <= 0 && onGround()) //land when moving down and standing on ground
        ) {
            //stop sound
            if (fallingSound != null) fallingSound.stop();
            dir[2] = 0;
            //set on top of block
            getCoords().setHeight((int)(oldHeight/GAMEDIMENSION)*GAMEDIMENSION);
        }
        
        
        /*HORIZONTAL MOVEMENT*/
        //calculate new position
        float newx = getPos()[0] + delta * speed * dir[0];
        float newy = getPos()[1] + delta * speed * dir[1];

        //if movement allowed => move player   
        if (! horizontalColission(newx, newy, oldPos[0], oldPos[1])) {                
            setPos(0, newx);
            setPos(1, newy);

            //track the coordiante change, if there is one
            int sidennumb = getSideNumb();              
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
        
        if (dir[0] < -Math.sin(Math.PI/3)){
            setValue(1);//west
        } else {
            if (dir[0] < - 0.5){
                //y
                if (dir[1]<0){
                    setValue(2);//north-west
                } else {
                    setValue(0);//south-east
                }
            } else {
                if (dir[0] <  0.5){
                    //y
                    if (dir[1]<0){
                        setValue(3);//north
                    }else{
                        setValue(7);//south
                    }
                }else {
                    if (dir[0] < Math.sin(Math.PI/3)) {
                        //y
                        if (dir[1] < 0){
                            setValue(4);//north-east
                        } else{
                            setValue(6);//sout-east
                        }
                    } else{
                        setValue(5);//east
                    }
                }
            }
        }
        if (SPRITESPERDIR==3){
            //animation
            walkingAnimationCounter += delta*speed*4;
            if (walkingAnimationCounter > 1000) walkingAnimationCounter=0;    

            if (walkingAnimationCounter >750) setValue(getValue()+16);
            else if (walkingAnimationCounter >250 && walkingAnimationCounter <500) setValue(getValue()+8);
        }

        //uncomment this line to see where to player stands:
        //Controller.getMapDataSafe(getRelCoords()[0], getRelCoords()[1], getRelCoords()[2]-1).setLightlevel(30);
        
        /* SOUNDS */
        //should the runningsound be played?
        if (runningSound != null)
            if (speed > 0.5f){
                if (!runningSound.playing()) runningSound.play();
            }  else runningSound.stop();
        
        //should the fallingsound be played?
        if (fallingSound != null
            && dir[2] < -1
            && ! fallingSound.playing()
           )
            fallingSound.play();
    }
    
    /**
     * check for horizontal colission
     * @param newx the new x position
     * @param newy the new y position
     * @return 
     */
    private boolean horizontalColission(float newx, float newy, float oldx, float oldy){
        boolean validmovement = false;
        
        //check for movement in x
        //top corner
        int neighbourNumber = Block.getSideID(newx, newy - COLISSIONRADIUS); 
        if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
            validmovement = true;
        //bottom corner
        neighbourNumber = Block.getSideID(newx, newy + COLISSIONRADIUS); 
        if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
            validmovement = true;

        //find out the direction of the movement
        if (oldx - newx > 0) {
            //check left corner
            neighbourNumber = Block.getSideID(newx - COLISSIONRADIUS, newy);
            if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
                validmovement = true;
        } else {
            //check right corner
            neighbourNumber = Block.getSideID(newx + COLISSIONRADIUS, newy);
            if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
                validmovement = true;
        }

        //check for movement in y
        //left corner
        neighbourNumber = Block.getSideID(newx - COLISSIONRADIUS, newy); 
        if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
            validmovement = true;

        //right corner
        neighbourNumber = Block.getSideID(newx + COLISSIONRADIUS, newy); 
        if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
            validmovement = true; 

        if (oldy - newy > 0) {
            //check top corner
            neighbourNumber = Block.getSideID(newx, newy - COLISSIONRADIUS);
            if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
                validmovement = true;
        } else {
            //check bottom corner
            neighbourNumber = Block.getSideID(newx, newy + COLISSIONRADIUS);
            if (neighbourNumber != 8 && Controller.getNeighbourBlock(getCoords(), neighbourNumber).isObstacle())
                validmovement = true;
        }
        return validmovement;
    }
    
    /**
     * Returns a normalized vector wich contains the direction of the block.
     * @return 
     */
    public float[] getDirectionVector(){
        return dir;
    }

    /**
     * Sets the sound to be played when falling.
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
    
   /**
     * Set the controls.
     * @param controls either "arrows", "WASD" or "NPC"
     */
    public void setControls(String controls){
        if ("arrows".equals(controls) || "WASD".equals(controls) || "NPC".equals(controls))
            this.controls = controls;
    }
    
   /**
     * Returns the Controls
     * @return either "arrows", "WASD" or "NPC"
     */
    public String getControls(){
        return controls;
    }
    
    /**
     * Adds horizontal colission check to onGround().
     * @return 
     */
    @Override
    public boolean onGround() {
        getCoords().setHeight(getCoords().getHeight()-1);
        boolean colission = horizontalColission(getPos(0), getPos(1), getPos(0), getPos(1));
        getCoords().setHeight(getCoords().getHeight()+1);
        
        return (super.onGround() || colission);
    }
}
