package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *This COntrolelr is the extended Controller from the engine. Put game code here.
 * @author Benedikt
 */
public class GameController extends Controller {
    
    /**
     * 
     * @param container
     * @param game
     * @throws SlickException
     */
    public GameController(GameContainer container, StateBasedGame game) throws SlickException{
        super(container, game);
        setPlayer(new Player((int) (Chunk.BLOCKS_X*1.5),(int) (Chunk.BLOCKS_Y*1.5),Chunk.BLOCKS_Z-2));
        getMap().setData((int) (Chunk.BLOCKS_X*1.5), (int) (Chunk.BLOCKS_Y*1.5), Chunk.BLOCKS_Z-2, getPlayer());
    }
    
}