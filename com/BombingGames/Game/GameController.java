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
        player = new Player((int) (Chunk.BlocksX*1.5),(int) (Chunk.BlocksY*1.5),Chunk.BlocksZ-2);
        map.setData((int) (Chunk.BlocksX*1.5), (int) (Chunk.BlocksY*1.5), Chunk.BlocksZ-2, player);
    }
    
}
