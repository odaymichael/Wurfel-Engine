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
        super(id, 3);
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
}
