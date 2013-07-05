package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Lighting.LightEngine;
import com.BombingGames.MainMenu.MainMenuState;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *A controller manages the map and the game data.
 * @author Benedikt Vogler
 */
public class Controller {
    private final boolean ENABLECHUNKSWITCH = true;
    public static LightEngine lightEngine;
    private static Map map;
    private static boolean recalcRequested;
    private AbstractCharacter player;
    private View view;
    private ArrayList<Camera> cameras = new ArrayList();
    private Minimap minimap;

    
    
    /**
     * Constructor is called when entering the gamemode.
     * @param gc The gameContainer containing this class.
     * @param game The StateBasedGame containing this class.
     * @throws SlickException
     */
    public Controller(GameContainer gc, StateBasedGame game) throws SlickException{  
        newMap();
        lightEngine = new LightEngine();
        
        recalcRequested = true;
    }
    
     /**
     * Main method which is called every refresh.
     * @param delta time since last call
     * @throws SlickException
     */
    public void update(int delta) throws SlickException{
        if (lightEngine != null) lightEngine.update(delta);
        if (ENABLECHUNKSWITCH){
            //earth to right
            if (cameras.get(0).getLeftBorder() <= 0)
            map.setCenter(3);
            else //earth to the left
                if (cameras.get(0).getRightBorder() >= Map.getBlocksX()-1) 
                    map.setCenter(5);

        //scroll up, earth down            
            if (cameras.get(0).getTopBorder() <= 0)
                map.setCenter(1);
            else //scroll down, earth up
                if (cameras.get(0).getBottomBorder() >= Map.getBlocksY()-1)
                map.setCenter(7);
        }
        //update every block on the map
        Block[][][] mapdata = map.getData();
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Map.getBlocksZ(); z++)
                    mapdata[x][y][z].update(delta);
        
        //update every entity
        for (AbstractEntity entity : map.getEntitylist())
            entity.update(delta);
        
        //recalculates the light if requested
        recalcIfRequested(cameras.get(0));      
       
        for (int i = 0; i < cameras.size(); i++) {
            cameras.get(i).update();
        }
                
        //update the log
        Gameplay.msgSystem().update(delta);
    }

    /**
     * Creates a new Map.
     */
    public static void newMap(){
        map = new Map(MainMenuState.shouldLoadMap());
    }
    
    /**
     * Returns the currently loaded map.
     * @return the map
     */
    public static Map getMap() {
        return map;
    }
    
    
    /**
     * Returns a block inside the map. The same as "getMap().getDataSafe(x,y,z)"
     * @param x
     * @param y
     * @param z
     * @return the wanted block
     * @see com.BombingGames.Game.Map#getDataSafe(int, int, int) 
     */
    public static Block getMapDataSafe(int x, int y, int z){
        return map.getDataSafe(x, y, z);
    }
    
    /**
     * 
     * @param coords
     * @return
     */
    public static Block getMapDataSafe(Coordinate coords) {
        return map.getDataSafe(coords);
    }
    
    /**
     * Same as "Map.getDataSafe(int, int, int)". This is a shortcut.
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
     * Shortcut to "Map.getDataSafe(int, int, int)"
     * @param coords the coordinates in an array (vector)
     * @return the wanted block
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public static Block getMapData(Coordinate coords){
        return map.getData(coords);
    }
    
    /**
     * Shortcut to  "Map.setDataSafe(int, int, int, Block block)"
     * @param x
     * @param y
     * @param z
     * @param block 
     * 
     */
    public static void setMapData(int x, int y, int z, Block block){
        map.setData(x, y, z, block);
    }
    
    /**
     * Shortcut to  "Map.getDataSafe(int, int, int)"
     * @param x
     * @param y
     * @param z
     * @param block
     * @deprecated 
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public static void setMapDataSafe(int x, int y, int z, Block block){
        map.setDataSafe(x, y, z, block);
    }
    
     /**
     * 
     * @param relCoords
     * @param block
     */
    public static void setMapData(int[] relCoords, Block block) {
        map.setData(relCoords, block);
    }
    
    /**
     * Shortcut to "map.setDataSafe". Set a block with safety checks.
     * @param relCoords
     * @param block the block you want to set
     */
    public static void setMapDataSafe(int[] relCoords, Block block) {
        map.setDataSafe(relCoords, block);
    }

    
    /**
     * Returns the player
     * @return the player
     */
    public AbstractCharacter getPlayer() {
        return player;
    }

   /**
     * Sets a player 
     * @param player 
     */
    public void setPlayer(AbstractCharacter player) {
        this.player = player;
        player.exist();
    }

    /**
     * Returns the view.
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * Set the view.
     * @param view
     */
    public void setView(View view) {
        this.view = view;
    }
    

    /**
     * Get the neighbour block to a side
     * @param coords 
     * @param side the id of the side
     * @return the neighbour block
     */
    public static Block getNeighbourBlock(Coordinate coords, int side){
        return Controller.getMapDataSafe(Block.sideIDtoNeighbourCoords(coords, side));
    }
    
   /**
     * Informs the map that a recalc is requested. It will do it in the next update. This method exist to minimize update calls.
     */
    public static void requestRecalc(){
        recalcRequested = true;
    }
    
    /**
     * When the recalc was requested it calls raytracing and light recalculing. This method should be called every update.
     * Request a recalc with <i>reuqestRecalc()</i>. 
     * @param camera 
     */
    public void recalcIfRequested(Camera camera){
        if (recalcRequested) {
            camera.raytracing();
            map.calc_light();
            if (minimap != null) minimap.update();
            recalcRequested = false;
        }
    }
    
    /**
     * Returns the minimap.
     * @return 
     */
    public Minimap getMinimap() {
        return minimap;
    }
    
    /**
     * Returns a camera.
     * @return The virtual cameras rendering the scene
     */
    protected ArrayList<Camera> getCameras() {
        return cameras;
    }

    /**
     * Add a camera.
     * @param camera
     */
    protected void addCamera(Camera camera) {
        this.cameras.add(camera);
    }

    /**
     * Set the minimap-
     * @param minimap
     */
    protected void setMinimap(Minimap minimap) {
        this.minimap = minimap;
    }

    public LightEngine getLightengine() {
        return lightEngine;
    }
    
}