package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Camera;
import com.BombingGames.Game.Controller;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends AbstractCharacter{
   private int[] coords;
   
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
        super.jump(8);
    }
    
     @Override
     public void render(int[] coords){
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
        super.render(coords);
    }

    @Override
    public int[] getAbsCoords() {
      return coords; 
    }

    @Override
    public void setAbsCoords(int[] coords) {
        this.coords = coords;
    }

    @Override
    public int[] getRelCoords() {
        return Controller.getMap().absoluteToRelativeCoords(coords);
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        setAbsCoords(new int[]{getAbsCoords()[0]+x, getAbsCoords()[1]+y, getAbsCoords()[2]+z});
    }
}
