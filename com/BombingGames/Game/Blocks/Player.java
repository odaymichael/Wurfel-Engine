package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameplay;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk. absCooords are the coordiantes which are absolute to the map. Relative is relative to the currently loaded chunks (map).
 * @author Benedikt
 */
public class Player extends SelfAwareBlock{
    /**
     * The X Position of the center of the player. value in pixel.
     */
    public int posX = Block.width / 2;
   /**
    * The Y Position on a block. value in pixel.
    */
   public int posY = Block.width / 2;
   
   /**
    * The Z Position on a block. value in pixel.
    */
   public int posZ = 0;
   

   
   /*The three values together build a vector. Always one of them must be  to prevent a division with 0.*/
   private float veloX = 1;
   private float veloY = 0;
   private float veloZ = 0;
   
   //provides a factor for the vector
   private float speed;
   

   private Sound fallsound = new Sound("com/BombingGames/Game/Sounds/wind.wav");
   private Sound runningsound = new Sound("com/BombingGames/Game/Sounds/victorcenusa_running.wav");
   private String controls = "WASD";
   
   
   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param X Absolute X-Pos of lower half
     * @param Y Absolute Y-Pos of lower half
     * @param Z Absolute Z-Pos of lower half
     * @throws SlickException
     */
    public Player(int X, int Y, int Z) throws SlickException {
        super(40,0);
        setAbsCoords(X,Y,Z);
        //creates the top of the player
        Controller.map.setData(X, Y, coordZ+1, new Block(40,1));
    }
    
  
    /**
     * Set the controls.
     * @param controls either "arrows" or "WASD".
     */
    public void setControls(String controls){
        if ("arrows".equals(controls) || "WASD".equals(controls))
            this.controls = controls;
    }
    
    /**
     * Returns the Controls
     * @return either "arrows" or "WASD".
     */
    public String getControls(){
        return controls;
    }
      
    private int getCorner() {
        return getCorner(posX,posY);
    }
        
    private int getCorner(int x, int y) {
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
           this.speed = walkingspeed;
            
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
   
    /**
     * Jumps the player
     */
    public void jump(){
        if (veloZ==0 && posZ==0) veloZ = 0.8f;
        
//       if (coordZ<Chunk.BlocksZ-2){
//           Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ] = new Block(0,0);
//           Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ+1] = new Block(0,0);
//           coordZ++;
//           Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ] = new Block(40,0);
//           Controller.map.data[getRelCoordX()][getRelCoordY()][coordZ+1] = new Block(40,1);
//           
//           Controller.map.requestRecalc();
//       }
       //GameplayState.iglog.add(getRelCoordX()+":"+getRelCoordY()+":"+coordZ);
    }
   
    /**
     * 
     * @param delta
     */
    public void update(int delta){
        if (speed > 0.5f){
            if (!runningsound.playing()) runningsound.play();
        }  else runningsound.stop();

        //Gravity
        float acc = -(9.81f/Block.width)/(delta);// this should be g=9.81 m/s^2

        //if (delta<1000) acc = Controller.map.gravity*delta;

        //land if standing in or under ground level and there is an obstacle
        if (veloZ < 0
            && posZ <= 0
            && (
                coordZ==0
                ||
                Controller.map.getData(getRelCoordX(), getRelCoordY(), coordZ-1).isObstacle()
            )
        ){
            veloZ = 0;
            fallsound.stop();
            posZ = 0;
            acc = 0;
        }

        //move if delta is okay
        if (delta < 500) {
            veloZ += acc;
            posZ += veloZ*delta;
        }


        //coordinate switch
        //down
        if (posZ <= 0 && coordZ>0 && !Controller.map.getData(getRelCoordX(),getRelCoordY(),coordZ-1).isObstacle()){
            if (! fallsound.playing()) fallsound.play();
            coordZ--;
            
            Controller.map.setData(getRelCoordX(), getRelCoordY(), coordZ+2, new Block(0,0));
            Controller.map.getData(getRelCoordX(), getRelCoordY(), coordZ+1).setValue(1);
            Controller.map.setData(getRelCoordX(), getRelCoordY(), coordZ, new Block(40,0));

            posZ += Block.width;
        }

        //up
        if (posZ >= Block.height && coordZ < Chunk.BlocksZ-2 && !Controller.map.getData(getRelCoordX(), getRelCoordY(), coordZ+1).isObstacle()){
            if (! fallsound.playing()) fallsound.play();

            coordZ++;

            Controller.map.setData(getRelCoordX(), getRelCoordY(), coordZ+1, new Block(40,1));
            Controller.map.getData(getRelCoordX(),getRelCoordY(), coordZ).setValue(0);
            Controller.map.setData(getRelCoordX(), getRelCoordY(), coordZ-1, new Block(0,0));

            posZ -= Block.width;
        } 
   }
    
     @Override
     public void draw(int x, int y, int z){ 
        super.draw(x, y, z);
        //this line causes massive rendering problems
        //Gameplay.view.g.fillRect(500, 500, 900, 600);
    }
           
}
