package com.BombingGames.Game.Gameobjects;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends AbstractCharacter{   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param id 
     * @throws SlickException
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    public Player(int id) throws SlickException {
        super(id);
        setFallingSound(new Sound("com/BombingGames/Game/Sounds/wind.wav"));
        setRunningSound(new Sound("com/BombingGames/Game/Sounds/victorcenusa_running.wav"));
    }   

    /**
     * Jumps the player
     */
    @Override
    public void jump() {
        super.jump(5);
    }
    
     @Override
     public void render(int[] coords){
        float[] dir = getDirectionVector();
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
        super.render(coords);
    }
}
