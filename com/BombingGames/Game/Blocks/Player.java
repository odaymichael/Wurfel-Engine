package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Camera;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends MovingBlock{
   private Sound fallsound = new Sound("com/BombingGames/Game/Sounds/wind.wav");
   private Sound runningsound = new Sound("com/BombingGames/Game/Sounds/victorcenusa_running.wav");
   private String controlls = "WASD";
   private Blockpointer topblock; 
   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param X Absolute X-Pos of lower half
     * @param Y Absolute Y-Pos of lower half
     * @param Z Absolute Z-Pos of lower half
     * @throws SlickException
     */
    public Player(int x, int y, int z) throws SlickException {
        super(40,1,x,y,z);
        //creates the top of the player
        topblock = new Blockpointer(this, 0, 0, 1);
        topblock.setBlock(new Block(40));
    }
    
  
    /**
     * Set the controlls.
     * @param controlls either "arrows" or "WASD".
     */
    public void setControlls(String controlls){
        if ("arrows".equals(controlls) || "WASD".equals(controlls))
            this.controlls = controlls;
    }
    
    /**
     * Returns the Controls
     * @return either "arrows" or "WASD".
     */
    public String getControlls(){
        return controlls;
    }
        

    /**
     * Jumps the player
     */
    @Override
    public void jump() {
        super.jump(8);
    }
   
    /**
     * 
     * @param delta
     */
    public void update(int delta) {
        /*if (speed > 0.5f){
            if (!runningsound.playing()) runningsound.play();
        }  else runningsound.stop();*/

        super.update(delta, topblock);
    }
    
     @Override
     public void render(int x, int y, int z, Camera camera){
        float[] dir = getDirectionVector();
        if (dir[0] < -Math.sin(Math.PI/3)){
            //west
            setValue(2);
        } else {
            if (dir[0] < - 0.5){
                //y
                if (dir[1]<0){
                    //north-west
                    setValue(3);
                } else {
                    //south-east
                    setValue(1);
                }
            } else {
                if (dir[0] <  0.5){
                    //y
                    if (dir[1]<0){
                            //north
                            setValue(4);
                    }else{
                            //south
                            setValue(8);
                            }
                }else {
                    if (dir[0] < Math.sin(Math.PI/3)) {
                        //y
                        if (dir[1] < 0){
                            //north-east
                            setValue(5);
                        } else{
                            //sout-east
                            setValue(7);
                        }
                    } else{
                        //east
                        setValue(6);
                    }
                }
            }
        }
        super.render(x, y, z, camera);
        //this line causes massive rendering problems
        //Gameplay.view.g.fillRect(500, 500, 900, 600);
    }
     
    @Override
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        super.walk(up, down, left, right, walkingspeed, delta, topblock);
    }
}
