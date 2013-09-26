package com.BombingGames.EngineCore;

import com.BombingGames.EngineCore.Map.Cell;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.Map.Minimap;
import com.BombingGames.EngineCore.Gameobjects.AbstractCharacter;
import com.BombingGames.EngineCore.Gameobjects.AbstractEntity;
import com.BombingGames.EngineCore.Gameobjects.Block;
import com.BombingGames.EngineCore.Gameobjects.AbstractGameObject;
import com.BombingGames.EngineCore.LightEngine.LightEngine;
import com.BombingGames.MainMenu.MainMenuScreen;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *A controller manages the map and the game data.
 * @author Benedikt Vogler
 */
public class Controller {
    private final boolean ENABLECHUNKSWITCH = true;
    private static LightEngine lightEngine;
    private static Map map;
    private static boolean recalcRequested;
    
    private View view;
        
    private final ArrayList<WECamera> cameras = new ArrayList();
    private Minimap minimap;
    /** The speed of time. 1 = real time;*/
    private float timespeed = 1;
    private AbstractCharacter player;   

    /**
     * This method works like a constructor. Everything is loaded. Set you custom chunk generator before calling this method.
     */
    public void init(){
        newMap();
        lightEngine = new LightEngine(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        
        recalcRequested = true;
    }
    
     /**
     * Main method which is called every refresh.
     * @param delta time since last call
     */
    public void update(float delta) {
        delta *= timespeed;
        if (lightEngine != null) lightEngine.update(delta);
        
         //update the log
        GameplayScreen.msgSystem().update(delta);
        
                
        if (ENABLECHUNKSWITCH && cameras.size() >0){
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
        
        //update every static update method
        AbstractGameObject.updateStaticUpdates(delta);
        
        //update every block on the map
        Cell[][][] mapdata = map.getData();
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Map.getBlocksZ(); z++)
                    mapdata[x][y][z].getBlock().update(delta);
        
        //update every entity
        for (AbstractEntity entity : map.getEntitys())
            entity.update(delta);
       
        for (int i = map.getEntitys().size()-1; i >= 0; i--) {
            if (map.getEntitys().get(i).shouldBeDestroyed())
                map.getEntitys().remove(i);
        }
        
        for (WECamera camera : cameras) {
            camera.update();
        }
                
        //recalculates the light if requested
        recalcIfRequested();      
    }

    
     /**
     * Informs the map that a recalc is requested. It will do it in the next update. This method  to limit update calls to to per frame
     */
    public static void requestRecalc(){
        Gdx.app.log("DEBUG", "A recalc was requested.");
        recalcRequested = true;
    }
    
    /**
     * When the recalc was requested it calls raytracing and light recalculing. This method should be called every update.
     * Request a recalc with <i>reuqestRecalc()</i>. 
     */
    public void recalcIfRequested(){
        if (recalcRequested) {
            Gdx.app.log("DEBUG", "Recalcing.");
            WECamera.raytracing();
            LightEngine.calcSimpleLight();
            if (minimap != null) minimap.update();
            recalcRequested = false;
        }
    }
    
    /**
     * Creates a new Map.
     */
    public static void newMap(){
        map = new Map(!MainMenuScreen.shouldLoadMap(),-45);
        map.fillWithBlocks();
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
     * 
     * @param coords 
     * @param block
     */
    public static void setMapData(Coordinate coords, Block block) {
        map.setData(coords, block);
    }
    
    /**
     * Shortcut to "map.setDataSafe". Set a block with safety checks.
     * @param coords 
     * @param block the block you want to set
     */
    public static void setMapDataSafe(Coordinate coords, Block block) {
        map.setDataSafe(coords, block);
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
     * Get the neighbour block to a side
     * @param coords 
     * @param side the id of the side
     * @return the neighbour block
     */
    public static Block getNeighbourBlock(Coordinate coords, int side){
        return Controller.getMapDataSafe(Coordinate.neighbourSidetoCoords(coords, side));
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
    protected ArrayList<WECamera> getCameras() {
        return cameras;
    }

    /**
     * Add a camera.
     * @param camera
     */
    protected void addCamera(WECamera camera) {
        this.cameras.add(camera);
    }

    /**
     * Set the minimap-
     * @param minimap
     */
    protected void setMinimap(Minimap minimap) {
        this.minimap = minimap;
    }

    /**
     *
     * @return
     */
    public static LightEngine getLightengine() {
        return lightEngine;
    }

    /**
     *
     * @return
     */
    public float getTimespeed() {
        return timespeed;
    }

    /**
     *
     */
    public void setTimespeed() {
        JFrame frame = new JFrame("InputDialog Example #2");
        try {
            this.timespeed = Float.parseFloat(JOptionPane.showInputDialog(frame, "Use dot as separator.", "Set the speed of time", JOptionPane.QUESTION_MESSAGE));
        } catch(NumberFormatException e) {
            this.timespeed = 1;
            Gdx.app.log("Error", "Invalid nubmer entered: "+e.toString());
        } catch(NullPointerException e){
            Gdx.app.log("DEBUG", "Canceled: "+e.toString());
        }
    }
    
    /**
     *
     * @param timespeed
     */
    public void setTimespeed(float timespeed) {
        this.timespeed = timespeed;
    }
    
    /**
     * Set the coressponging main view.
     *
     * @param view new value of view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     *Returns the coressponging main view.
     * @return
     */
    public View getView() {
        return view;
    }
}