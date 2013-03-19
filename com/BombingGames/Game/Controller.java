package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.AbstractCharacter;
import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.MainMenu.MainMenuState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *A controller manages the map and the game data.
 * @author Benedikt
 */
public class Controller {
    private static Map map;
    private static boolean recalcRequested;
    private AbstractCharacter player;
    private View view;
    private Camera camera,camera2;
    private Minimap minimap;
    
    /**
     * Constructor is called when entering the gamemode.
     * @param gc
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer gc, StateBasedGame game) throws SlickException{        
        map = new Map(MainMenuState.shouldLoadMap());
        
        camera2 = new Camera(
            new int[]{Map.getBlocksX()/2,Map.getBlocksY()/2,Map.getBlocksZ()/2},
            800, //left
            0, //top
            400, //full width 
            400//full height
        );
        
        recalcRequested = true;
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
    public static Block getMapDataSafe(int[] coords) {
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
    public static Block getMapData(int[] coords){
        return map.getData(coords[0], coords[1], coords[2]);
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
     * @param coords
     * @param block
     */
    public static void setMapData(int[] coords, Block block) {
        map.setData(coords, block);
    }
    
    /**
     * 
     * @param coords
     * @param block
     */
    public static void setMapDataSafe(int[] coords, Block block) {
        map.setDataSafe(coords, block);
    }
    
    
    /**
     * Main method which is called every refresh.
     * @param delta
     * @throws SlickException
     */
    public void update(int delta) throws SlickException{
        //earth to right
        if (camera.getLeftBorder() <= 0)
           map.setCenter(3);
        else //earth to the left
            if (camera.getRightBorder() >= Map.getBlocksX()-1) 
                map.setCenter(5);
        
       //scroll up, earth down            
        if (camera.getTopBorder() <= 0)
            map.setCenter(1);
        else //scroll down, earth up
            if (camera.getBottomBorder() >= Map.getBlocksY()-1)
                map.setCenter(7);
        
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
        recalcIfRequested(camera);      
       
        camera.update();
                
        //update the log
        Gameplay.MSGSYSTEM.update(delta);
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
        map.getEntitylist().add(player);
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
    public static Block getNeighbourBlock(int[] coords, int side){
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
    
    public Minimap getMinimap() {
        return minimap;
    }
    
    /**
     * Returns the camera
     * @return
     */
    protected Camera getCamera() {
        return camera;
    }

    protected void setCamera(Camera camera) {
        this.camera = camera;
    }

    protected void setMinimap(Minimap minimap) {
        this.minimap = minimap;
    }
}