package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Game.Blocks.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *The <i>GameController</i> is for the game code. Put engine code into <i>Controller</i>.
 * @author Benedikt
 */
public class GameController extends Controller {
    private GameContainer gc;
    /**
     * 
     * @param container
     * @param game
     * @throws SlickException
     */
    public GameController(GameContainer gc, StateBasedGame game) throws SlickException{
        super(gc, game);
        this.gc = gc;
        setPlayer(new Player((int) (Chunk.getBlocksX()*1.5),(int) (Chunk.getBlocksY()*1.5), Chunk.getBlocksZ()-2));
        getMap().setData((int) (Chunk.getBlocksX()*1.5), (int) (Chunk.getBlocksY()*1.5), Chunk.getBlocksZ()-2, getPlayer());
    }
    
    @Override
    public void update(int delta) throws SlickException{
        super.update(delta);
        
        //get input and do actions
        Input input = gc.getInput();
        
        if (!Gameplay.MSGSYSTEM.isListeningForInput()) {
            if (input.isKeyDown(Input.KEY_Q)) gc.exit();

            //open menu
            if (input.isKeyPressed(Input.KEY_ESCAPE)) openmenu();

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_F)) gc.setFullscreen(!gc.isFullscreen()); 

            //toggle fullscreen
            if (input.isKeyPressed(Input.KEY_E)) getMap().earthquake(); 
            
            //pause
            if (input.isKeyDown(Input.KEY_P)) gc.setPaused(true);

            //good graphics
            if (input.isKeyPressed(Input.KEY_G)) {
                setGoodgraphics(!hasGoodGraphics());
                Gameplay.MSGSYSTEM.add("Good Graphics is now "+hasGoodGraphics());
            }

            //toggle getCamera()
            //if (input.isKeyPressed(Input.KEY_C)) 

            //restart
            if (input.isKeyPressed(Input.KEY_N)) newMap();

            //reset zoom
            if (input.isKeyPressed(Input.KEY_Z)) {
                Gameplay.getView().getCamera().setZoom(1);
                Gameplay.MSGSYSTEM.add("Zoom reset");
            }        

            //walk
            if (getPlayer() != null)
                if ("WASD".equals(getPlayer().getControlls()))
                    getPlayer().walk(
                        input.isKeyDown(Input.KEY_W),
                        input.isKeyDown(Input.KEY_S),
                        input.isKeyDown(Input.KEY_A),
                        input.isKeyDown(Input.KEY_D),
                        .25f+(input.isKeyDown(Input.KEY_LSHIFT)? 0.75f: 0),
                        delta
                    );
                if (input.isKeyPressed(Input.KEY_SPACE)) getPlayer().jump();
            
        } else {
            //fetch input and write it down
            //to-do!
        }
        
        //toggle input for msgSystem
        if (input.isKeyPressed(Input.KEY_ENTER)) Gameplay.MSGSYSTEM.listenForInput(!Gameplay.MSGSYSTEM.isListeningForInput());
    }
    
}
