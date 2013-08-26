package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Coordinate;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;
import static com.BombingGames.Game.Gameobjects.GameObject.GAMEDIMENSION;
import com.BombingGames.EngineCore.Map;
import com.BombingGames.EngineCore.View;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends GameObject implements IsSelfAware {
   private Coordinate coords;//the position in the map-grid
   private float offsetX = DIM2; //the horizontal offset
   private float offsetY = DIM2;

   
    /**
     * Create an abstractEntity. You should use Block.getInstance(int) 
     * @param id 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    protected AbstractEntity(int id){
        super(id);
    }
    
    /**
     * Create an entity.
     * @param id the object id of the entity.
     * @param value The value at start.
     * @param coords The coordiantes where you place it.
     * @return the entity.
     */
    public static AbstractEntity getInstance(int id, int value, Coordinate coords){
        AbstractEntity entity = null;
        //define the default SideSprites
        switch (id){
            case 40:
                entity = new Player(id);

                    break;
            case 41: 
                    entity = new AnimatedEntity(
                                id,
                                value,
                                new int[]{700,2000},
                                true,
                                false
                            );//explosion
                    break;
            default: entity = new SimpleEntity(id);
        }
        
        entity.setCoords(coords);

        entity.setValue(value);
        entity.setVisible(true);
        return entity;
    }
    
    @Override
    public int getDepth(Coordinate coords){
        return (int) (
            coords.getRelY() * (DIM4+1)//Y
            + getOffsetY()
            + coords.getHeight() / Math.sqrt(2) /2//Z
            + (getDimensionY() - 1) * DIM4 / Math.sqrt(2) /2
        );
    }
    
    //IsSelfAware implementation
    @Override
    public Coordinate getCoords() {
        return this.coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }
    
    
    
    public void setHeight(float height) {
        coords.setHeight(height);
    }
    
    
    
  
    /**
     * Is the entity laying/standing on the ground?
     * @return true when on the ground
     */
    public boolean onGround(){
        if (getCoords().getHeight() <= 0) return true; //if entity is under the map
        
        //check if one pixel deeper is on ground.
        int z = (int) ((getCoords().getHeight()-1)/GAMEDIMENSION);
        if (z > Map.getBlocksZ()-1) z = Map.getBlocksZ()-1;
        
        return new Coordinate(coords.getRelX(), coords.getRelY(), z, true).getBlock().isObstacle();
    }
    

        
     /**
     * Get the screen x-position where the object is rendered without regarding the camera.
     * @param coords  this parameter get's ignored because entitys know their own coordinates. You can pass <i>null</i> here.
     * @return The screen X-position in pixels.
     */
   @Override
    public int get2DPosX(Coordinate coords) {
        return this.coords.getRelX() * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (this.coords.getRelY() % 2) * DIM2 //y-coordinate multiplied by it's dimension in this direction
               + (int) (offsetX); //add the objects position inside this coordinate
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param coords  this parameter get's ignored because entitys know their own coordinates. You can pass <i>null</i> here.
     * @return The screen Y-position in pixels.
     */
   @Override
    public int get2DPosY(Coordinate coords) {
        return this.coords.getRelY() * DIM4 //y-coordinate * the tile's size
               + (int) (offsetY / 2) //add the objects position inside this coordinate
               - (int) (this.coords.getHeight() / Math.sqrt(2)); //take axis shortening into account
    }
    

    
    /**
     * add this entity to the map-> let it exist
     */
    public void exist(){
        Controller.getMap().getEntitylist().add(this);
    }
    
    /**
     * Returns the side with the current offset.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return Block.getSideID(offsetX, offsetY);
    }  

    /**
     *
     * @return
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     *
     * @param offsetX
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    /**
     *
     * @return
     */
    public float getOffsetY() {
        return offsetY;
    }

    /**
     *
     * @param offsetY
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}