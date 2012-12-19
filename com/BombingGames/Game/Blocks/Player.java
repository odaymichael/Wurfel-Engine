package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
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
   private Blockpointer topblock; 
   
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
        topblock = new Blockpointer(this, 0, 0, 1);
        topblock.setBlock(new Block(40,1));
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
        if (veloZ==0 && posZ==0) veloZ = 8;
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
        float a = -Map.GRAVITY;// this should be g=9.81 m/s^2

        //if (delta<1000) acc = Controller.map.gravity*delta;

        //land if standing in or under ground level and there is an obstacle
        if (veloZ <= 0
            && posZ <= 0
            && (getCoordZ() == 0 || Controller.getMapDataUnsafe(getCoordX(), getCoordY(), getCoordZ()-1).isObstacle())
        ) {
            fallsound.stop();
            veloZ = 0;
            posZ = 0;
            a = 0;
            //player stands now
        }

        //t = time in s
        float t = delta/1000f;
        //move if delta is okay
        if (delta < 500) {
            veloZ += a*t; //in m/s
            posZ += veloZ*Block.WIDTH*t;//m
        }

        
        //coordinate switch
        //down
        if (posZ <= 0 && getCoordZ() > 0 && !Controller.getMapData(getCoordX(), getCoordY(),getCoordZ()-1).isObstacle()){
            if (! fallsound.playing()) fallsound.play();
            
            selfDestroy();
            topblock.setBlock(new Block(0));
            setCoordZ(getCoordZ()-1);
            selfRebuild();
            topblock.setBlock(new Block(40,1));

            posZ += Block.WIDTH;
            Controller.getMap().requestRecalc();
        } else {
            //up
            if (posZ >= Block.HEIGHT && getCoordZ() < Chunk.BLOCKS_Z-2 && !Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()+2).isObstacle()){
                if (! fallsound.playing()) fallsound.play();

                selfDestroy();
                topblock.setBlock(new Block(0));
                setCoordZ(getCoordZ()+1);
                selfRebuild();
                topblock.setBlock(new Block(40,1));

                posZ -= Block.WIDTH;
                Controller.getMap().requestRecalc();
            } 
        }
        
        //set the offset for the rendering
        setOffset(getPosX() - Block.WIDTH/2, posY - posZ - Block.WIDTH/2);
        topblock.getBlock().setOffset(getOffsetX(), getOffsetY());  
   }
    
     @Override
     public void draw(int x, int y, int z){ 
        super.draw(x, y, z);
        //this line causes massive rendering problems
        //Gameplay.view.g.fillRect(500, 500, 900, 600);
    }
     
    @Override
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        super.walk(up, down, left, right, walkingspeed, delta, topblock);
    }
}
