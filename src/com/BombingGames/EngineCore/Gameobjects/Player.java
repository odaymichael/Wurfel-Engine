package com.BombingGames.EngineCore.Gameobjects;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.badlogic.gdx.Gdx;


/**
 *The Player is a character who can walk.
 * @author Benedikt
 */
public class Player extends AbstractCharacter{   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param id 
     * @param coord 
     * @see com.BombingGames.EngineCore.Gameobjects.Block#getInstance(int) 
     */
    public Player(int id, Coordinate coord) {
        super(id, 1, coord);
        setFallingSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/wind.ogg")));
        setRunningSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/victorcenusa_running.ogg")));
        setJumpingSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/jump_man.wav")));
        setLandingSound(Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/Game/Sounds/landing.wav")));
        
        setTransparent(true);
        setObstacle(true);
        setDimensionZ(2);
    }   

    /**
     * Jumps the player
     */
    @Override
    public void jump() {
        super.jump(5);
    }
}