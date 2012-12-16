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
    /**
     *The list which has all current nine chunks in it.
     */
    private static Map map;
    private Player player;   
   
    private boolean goodgraphics = false;
    private boolean renderSides = true;
    
    /**
     * 
     */
    public GameContainer gc;
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
         
//        //earth to right
//        if (Gameplay.view.getCamera().getLeftBorder() < Chunk.BLOCKS_X/3)
//           map.setCenter(3);
//        else {       
//            //earth to the left
//            if (Gameplay.view.getCamera().getRightBorder() > 8*Chunk.BLOCKS_X/3) 
//                map.setCenter(5); 
//        }
//        
//       //scroll up, earth down            
//        if (Gameplay.view.getCamera().getTopBorder()  <= 0) {
//            map.setCenter(1);
//        } else {
//            //scroll down, earth up
//            if (Gameplay.view.getCamera().getBottomBorder() > 8*Chunk.BLOCKS_Y/3)
//                map.setCenter(7);
//        }
        
        if (player != null)
            player.update(delta);
        
        Gameplay.view.getCamera().update();
        
            
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
            Gameplay.view.getCamera().setZoom((float) (3f*Math.sin(zoomx-1.5f)+3.5f));
            
            
           /* Block.width =(int) (gc.getWidth() *zoom / Chunk.BlocksX);
            Block.height = (int) (4*gc.getHeight()*zoom / Chunk.BlocksY);
            Chunk.SIZE_X = (int) (Chunk.BlocksX*Block.width*zoom);
            Chunk.SIZE_Y = (int) (Chunk.BlocksY*Block.height*zoom/2);*/
            
            Gameplay.MSGSYSTEM.add("Zoom: "+Gameplay.view.getCamera().getZoom());   
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
        }

        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy) {
            //workaround for the bug, because the event is called multiple times
            gc.getInput().consumeEvent();
            
            if (Gameplay.view.getCamera().getFocus()) {
                Gameplay.view.getCamera().setX(Gameplay.view.getCamera().getX()+newx-oldx);
                Gameplay.view.getCamera().setY(Gameplay.view.getCamera().getY()+newx-oldy);
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
     * When true every side is rendered. When false the whole block is rendered.
     * @return 
     */
    public boolean renderSides() {
        return renderSides;
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


    public boolean getRenderSides() {
        return renderSides;
    }

    protected void setRenderSides(boolean renderSides) {
        this.renderSides = renderSides;
    }

}