package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_DEPTH2;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_DEPTH;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.GAMEDIMENSION;
import com.BombingGames.EngineCore.Map.Map;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.OBJECTTYPESCOUNT;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_WIDTH;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends AbstractGameObject implements IsSelfAware {
   private Coordinate coords;//the position in the map-grid
   private float positionX = 0; //the horizontal offset
   private float positionY = 0;
   
   public static final char CATEGORY = 'e';
   
    /**Containts the names of the objects. index=id*/
    public static final String[] NAMELIST = new String[OBJECTTYPESCOUNT]; 
    
    /** A list containing the offset of the objects. */
    public static final int[][][] OFFSET = new int[OBJECTTYPESCOUNT][10][2];
    
    static {
        NAMELIST[40] = "player";
        OFFSET[40][0][0] = 54;
        OFFSET[40][0][1] = 37;
        OFFSET[40][1][0] = 55;
        OFFSET[40][1][1] = 38;
        OFFSET[40][2][0] = 53;
        OFFSET[40][2][1] = 35;
        OFFSET[40][3][0] = 46;
        OFFSET[40][3][1] = 33;
        OFFSET[40][4][0] = 53;
        OFFSET[40][4][1] = 35;
        OFFSET[40][5][0] = 64;
        OFFSET[40][5][1] = 33;
        OFFSET[40][6][0] = 53;
        OFFSET[40][6][1] = 33;
        OFFSET[40][7][0] = 46;
        OFFSET[40][7][1] = 33;
        NAMELIST[41] = "smoke test";
    }
    
    private boolean destroy;
   
    /**
     * Create an abstractEntity. You should use Block.getInstance(int) 
     * @param id 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    protected AbstractEntity(int id){
        super(id,0);
    }
    
    /**
     * Create an entity through this factory method..
     * @param id the object id of the entity.
     * @param value The value at start.
     * @param coords The coordiantes where you place it.
     * @return the entity.
     */
    public static AbstractEntity getInstance(int id, int value, Coordinate coords){
        AbstractEntity entity;
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
        entity.setClipped(true);
        return entity;
    }
    
    @Override
    public int getDepth(Coordinate coords){
        return (int) (
            coords.getRelY() * (SCREEN_DEPTH+1)//Y
            + getPositionY()
            + coords.getHeight()/Math.sqrt(2)//Z
            + (getDimensionZ() - 1) * GAMEDIMENSION/6/Math.sqrt(2)
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
    
    
    /**
     * 
     * @param height 
     */
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
        return this.coords.getRelX() * SCREEN_WIDTH //x-coordinate multiplied by it's dimension in this direction
               + (this.coords.getRelY() % 2) * SCREEN_DEPTH //y-coordinate multiplied by it's dimension in this direction
               + (int) (positionX); //add the objects position inside this coordinate
    }

    /**
     * Get the 2D-projection y-position where the object is rendered without regarding the camera.
     * @param coords  this parameter get's ignored because entitys know their own coordinates. You can pass <i>null</i> here.
     * @return The 2D-projection Y-position in pixels.
     */
   @Override
    public int get2DPosY(Coordinate coords) {
        return this.coords.getRelY() * SCREEN_DEPTH2 //y-coordinate * the tile's size
               + (int) (positionY / 2) //add the objects position inside this coordinate
               - (int) (this.coords.getHeight() / Math.sqrt(2)); //take axis shortening into account
    }
        
    /**
     * add this entity to the map-> let it exist
     */
    public void exist(){
        Controller.getMap().getEntitys().add(this);
    }
    
    /**
     * Returns the side with the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return Coordinate.getNeighbourSide(positionX, positionY);
    }  

    /**
     *The positon coordiante system has it's center in the top left corner of the cell, like the offset.
     * @return
     */
    public float getPositionX() {
        return positionX;
    }

    /**
     *The positon coordiante system has it's center in the top left corner of the cell, like the offset.
     * @param offsetX
     */
    public void setPositionX(float offsetX) {
        this.positionX = offsetX;
    }

    /**
     *The positon coordiante system has it's center in the top left corner of the cell, like the offset.
     * @return
     */
    public float getPositionY() {
        return positionY;
    }

    /**
     *The positon coordiante system has it's center in the top left corner of the cell, like the offset.
     * @param offsetY
     */
    public void setPositionY(float offsetY) {
        this.positionY = offsetY;
    }

    @Override
    public char getCategory() {
        return CATEGORY;
    } 
    
    @Override
    public String getName() {
        return NAMELIST[getId()];
    }
    
    @Override
    public int getOffsetX() {
        return OFFSET[getId()][getValue()][0];
    }

    @Override
    public int getOffsetY() {
        return OFFSET[getId()][getValue()][1];
    } 
    
   /**
     * Deletes the object from the map. The opposite to exist();
     */
    public void destroy(){
        destroy=true;
    }

    public boolean shouldBeDestroyed() {
        return destroy;
    }
}