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
 *
 * @author Benedikt
 */
public class Controller {
    private static Map map;
    private Player player;   
    private ArrayList<AbstractEntity> entitylist = new ArrayList<AbstractEntity>();
    
    /**
     * Constructor is called when entering the gamemode.
     * @param container
     * @param game
     * @throws SlickException
     */
    public Controller(GameContainer container, StateBasedGame game) throws SlickException{        
        map = new Map(MainMenuState.loadmap);
    }

    /**
     * Creates a new Map.
     */
    public static void newMap(){
        map = new Map(MainMenuState.loadmap);
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
     * Same as "Map.getDataSafe(int, int, int)"
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
        
        if (player != null)
            player.update(delta);
        
        Gameplay.getView().getCamera().update();
        
            
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
        entitylist.add(player);
    }



    public ArrayList<AbstractEntity> getEntitylist() {
        return entitylist;
    }
}