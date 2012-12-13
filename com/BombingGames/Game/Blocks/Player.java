package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk. absCooords are the coordiantes which are absolute to the map. Relative is relative to the currently loaded chunks (map).
 * @author Benedikt
 */
public class Player extends MovingBlock{
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
        Controller.getMap().setData(X, Y, coordZ+1, new Block(40,1));
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
        

    /**
     * Jumps the player
     */
    @Override
    public void jump(){
        if (veloZ==0 && posZ==0) veloZ = 0.8f;
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
        float acc = -(9.81f/Block.WIDTH)/(delta);// this should be g=9.81 m/s^2

        //if (delta<1000) acc = Controller.map.gravity*delta;

        //land if standing in or under ground level and there is an obstacle
        if (veloZ < 0
            && posZ <= 0
            && (
                coordZ==0
                ||
                Controller.getMapData(getRelCoordX(), getRelCoordY(), coordZ-1).isObstacle()
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
        if (posZ <= 0 && coordZ>0 && !Controller.getMapData(getRelCoordX(),getRelCoordY(),coordZ-1).isObstacle()){
            if (! fallsound.playing()) fallsound.play();
            coordZ--;
            
            Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ+2, new Block(0,0));
            Controller.getMapData(getRelCoordX(), getRelCoordY(), coordZ+1).setValue(1);
            Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ, new Block(40,0));

            posZ += Block.WIDTH;
        }

        //up
        if (posZ >= Block.HEIGHT && coordZ < Chunk.BLOCKS_Z-2 && !Controller.getMapData(getRelCoordX(), getRelCoordY(), coordZ+1).isObstacle()){
            if (! fallsound.playing()) fallsound.play();

            coordZ++;

            Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ+1, new Block(40,1));
            Controller.getMapData(getRelCoordX(),getRelCoordY(), coordZ).setValue(0);
            Controller.getMap().setData(getRelCoordX(), getRelCoordY(), coordZ-1, new Block(0,0));

            posZ -= Block.WIDTH;
        } 
   }
    
     @Override
     public void draw(int x, int y, int z){ 
        super.draw(x, y, z);
        //this line causes massive rendering problems
        //Gameplay.view.g.fillRect(500, 500, 900, 600);
    } 
}
