package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Game.Blocks.Player;
import com.BombingGames.MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 *
 * @author Benedikt
 */
public class Controller {
    private GameContainer gc;
    /**
     *The list which has all current nine chunks in it.
     */
    private static Map map;
    private Player player;   
   
    private boolean goodgraphics = false;
    

    private float zoomx = 1;
    
    /**
     * Constructor is called when entering the gamemode.
     * @param container
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{
        gc = container;
          
        gc.getInput().addMouseListener(new MouseDraggedListener());
        map = new Map(MainMenuState.loadmap);
    }

    /**
     * Main method which is called the whole time
     * @param delta
     * @throws SlickException
     */
    public void update(int delta) throws SlickException{
        if (delta > 200) Log.warn("delta is too high to stay stable. d: "+delta);
         
       //earth to right
        if (Gameplay.getView().getCamera().getLeftBorder() <= 0)
           map.setCenter(3);
        else {       
            //earth to the left
            if (Gameplay.getView().getCamera().getRightBorder() >= Map.getBlocksX()-1) 
                map.setCenter(5); 
        }
        
       //scroll up, earth down            
        if (Gameplay.getView().getCamera().getTopBorder()  <= 0) {
            map.setCenter(1);
        } else {
            //scroll down, earth up
            if (Gameplay.getView().getCamera().getBottomBorder() >= Map.getBlocksY()-1)
                map.setCenter(7);
        }
        
        if (player != null)
            player.update(delta);
        
        Gameplay.getView().getCamera().update();
        
            
        //recalculates the light if requested
        map.recalcIfRequested();      
       
        //update the log
        Gameplay.MSGSYSTEM.update(delta);
    }
    
    
  
    class MouseDraggedListener implements MouseListener{
        @Override
        public void mouseWheelMoved(int change) {
            gc.getInput().consumeEvent();
            
            zoomx = zoomx + change/1000f;
            Gameplay.getView().getCamera().setZoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            
           /* Block.width =(int) (gc.getWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getGroundHeight()*zoom / Chunk.BlocksY);
            Chunk.SIZE_X = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SIZE_Y = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            Gameplay.MSGSYSTEM.add("Zoom: "+Gameplay.getView().getCamera().getZoom());   
        }

        @Override
        public void mouseClicked(int button, int x, int y, int clickCount) {
        }

        @Override
        public void mousePressed(int button, int x, int y) {
        }

        @Override
        public void mouseReleased(int button, int x, int y) {   
        }

        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) {
            Log.info(
                    Double.toString(Math.atan(
                        Math.abs(Gameplay.getView().getCamera().getCenterofBlock(player.getCoordX(), player.getCoordY(), player.getCoordZ())[1 ]- newy * Gameplay.getView().getEqualizationScale()) /
                        (float) Math.abs(Gameplay.getView().getCamera().getCenterofBlock(player.getCoordX(), player.getCoordY(), player.getCoordZ())[0] - newx * Gameplay.getView().getEqualizationScale())
                    )*180/Math.PI)+"°"
                );
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            if (Gameplay.getView().getCamera().getFocus()) {
                Gameplay.getView().getCamera().setX(Gameplay.getView().getCamera().getX()+newx-oldx);
                Gameplay.getView().getCamera().setY(Gameplay.getView().getCamera().getY()+newx-oldy);
            }
            /*for (int i=0;i<9;i++){
                chunklist[i].posX += newx - oldx;
                chunklist[i].posY += newy - oldy ;
            }*/
            
            //if the middle chunk is scrolled down over the middle line then 
            //GameplayState.iglog.add("Chunk.SIZE_X: "+String.valueOf(Chunk.SIZE_X));
            //GameplayState.iglog.add("chunk: "+String.valueOf(chunklist[4].posX));    
        }

        @Override
        public void setInput(Input input) {
        }

        @Override
        public boolean isAcceptingInput() {
            return true;
        }

        @Override
        public void inputEnded() {
          
        }

        @Override
        public void inputStarted() {
        }
    }
    
    protected void openmenu(){
        boolean openmenu = true;
    }

    /**
     * Returns the player
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets a player 
     * @param player 
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    protected void setGoodgraphics(boolean goodgraphics) {
        this.goodgraphics = goodgraphics;
    }
       
   /**
     * Should the graphic be a bit slower but better? Must be in Controller because is needed for e.g. the Block and there used as data
     * @return 
     */
    public boolean hasGoodGraphics() {
        return goodgraphics;
    }

    /**
     * Returns the currently loaded map.
     * @return the map
     */
    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        Controller.map = map;
    }
    
    /**
     * Returns a block inside the map. The same as "getMap().getData(x,y,z)"
     * @param x
     * @param y
     * @param z
     * @return the wanted block
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public static Block getMapData(int x, int y, int z){
        return map.getData(x, y, z);
    }
    
    /**
     * Same as "Map.getDataUnsafe(int, int, int)"
     * 
     * @param x
     * @param y
     * @param z
     * @return the wanted block
     * @see com.BombingGames.Game.Map#getDataUnsafe(int, int, int) 
     */
    public static Block getMapDataUnsafe(int x, int y, int z){
        return map.getDataUnsafe(x, y, z);
    }
}