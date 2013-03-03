package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.Player;
import com.BombingGames.MainMenu.MainMenuState;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 *A controlelr manages the map and the game data.
 * @author Benedikt
 */
public class Controller {
    private static Map map;
    private Player player;   
    
    /**
     * Constructor is called when entering the gamemode.
     * @param container
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{        
        map = new Map(MainMenuState.shouldLoadMap());
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
     * @see com.BombingGames.Game.Map#getData(int, int, int) 
     */
    public static void setMapDataSafe(int x, int y, int z, Block block){
        map.setDataSafe(x, y, z, block);
    }
    
    
    /**
     * Main method which is called every refresh
     * @param delta
     * @throws SlickException
     */
    public void update(int delta) throws SlickException{
        if (delta > 200) Log.warn("delta is too high for a stable game. d: "+delta);
         
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
        
        
        Gameplay.getView().getCamera().update();

        
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
        map.recalcIfRequested();      
       
        //update the log
        Gameplay.MSGSYSTEM.update(delta);
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
        map.getEntitylist().add(player);
    }



}