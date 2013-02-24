package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Camera;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends AbstractEntity{
   private Sound fallsound = new Sound("com/BombingGames/Game/Sounds/wind.wav");
   private Sound runningsound = new Sound("com/BombingGames/Game/Sounds/victorcenusa_running.wav");
   private String controlls = "WASD";
   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param X Absolute X-Pos of lower half
     * @param Y Absolute Y-Pos of lower half
     * @param Z Absolute Z-Pos of lower half
     * @throws SlickException
     */
    protected Player(int x, int y, int z) throws SlickException {
        super(x,y,z);
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
    @Override
    public void update(int delta) {
        /*if (speed > 0.5f){
            if (!runningsound.playing()) runningsound.play();
        }  else runningsound.stop();*/

        super.update(delta);
    }
    
     @Override
     public void render(int x, int y, int z, Camera camera){
        float[] dir = getDirectionVector();
        if (dir[0] < -Math.sin(Math.PI/3)){
            setValue(2);//west
        } else {
            if (dir[0] < - 0.5){
                //y
                if (dir[1]<0){
                    setValue(3);//north-west
                } else {
                    setValue(1);//south-east
                }
            } else {
                if (dir[0] <  0.5){
                    //y
                    if (dir[1]<0){
                            setValue(4);//north
                    }else{
                        setValue(8);//south
                         }
                }else {
                    if (dir[0] < Math.sin(Math.PI/3)) {
                        //y
                        if (dir[1] < 0){
                            setValue(5);//north-east
                        } else{
                            setValue(7);//sout-east
                        }
                    } else{
                        setValue(6);//east
                    }
                }
            }
        }
        super.render(x, y, z, camera);
        //this line causes massive rendering problems
        //Gameplay.view.g.fillRect(500, 500, 900, 600);
    }
}
