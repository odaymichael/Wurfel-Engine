package com.BombingGames.Game.Gameobjects;

import com.badlogic.gdx.Gdx;


/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends AbstractCharacter{   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param id 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    public Player(int id) {
        super(id, 1);
        setFallingSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/wind.ogg")));
        setRunningSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/victorcenusa_running.ogg")));
        setTransparent(true);
        setObstacle(true);
        setDimensionY(2);
    }   

    /**
     * Jumps the player
     */
    @Override
    public void jump() {
        super.jump(5);
    }
}