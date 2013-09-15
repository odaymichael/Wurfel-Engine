package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;
import static com.BombingGames.Game.Gameobjects.GameObject.GAMEDIMENSION;
import com.BombingGames.EngineCore.Map.Map;
import static com.BombingGames.Game.Gameobjects.GameObject.OBJECTTYPESCOUNT;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends GameObject implements IsSelfAware {
   private Coordinate coords;//the position in the map-grid
   private float positionX = DIM2; //the horizontal offset
   private float positionY = DIM2;
   
   public static final char CATEGORY = 'e';
   
    /**Containts the names of the objects. index=id*/
    public static final String[] NAMELIST = new String[OBJECTTYPESCOUNT]; 
    
    /** A list containing the offset of the objects. */
    public static final int[][][] OFFSET = new int[OBJECTTYPESCOUNT][24][2];
    
    static {
        NAMELIST[40] = "player";
        OFFSET[40][0][0] = 24+21;
        OFFSET[40][0][1] = 30+20;
        OFFSET[40][1][0] = 39+21;
        OFFSET[40][1][1] = 34+20;
        OFFSET[40][2][0] = 24+21;
        OFFSET[40][2][1] = 30+20;
        OFFSET[40][3][0] = 20+21;
        OFFSET[40][3][1] = 34+20;
        OFFSET[40][4][0] = 24+21;
        OFFSET[40][4][1] = 30+20;
        OFFSET[40][5][0] = 40+21;
        OFFSET[40][5][1] = 34+20;
        OFFSET[40][6][0] = 24+21;
        OFFSET[40][6][1] = 30+20;
        OFFSET[40][7][0] = 20+21;
        OFFSET[40][7][1] = 34+20;
        OFFSET[40][8][0] = 15+21;
        OFFSET[40][8][1] = 30+20;
        OFFSET[40][9][0] = 17+21;
        OFFSET[40][9][1] = 34+20;
        OFFSET[40][10][0] = 5+21;
        OFFSET[40][10][1] = 30+20;
        OFFSET[40][11][0] = 20+21;
        OFFSET[40][11][1] = 34+20;
        OFFSET[40][12][0] = 16+21;
        OFFSET[40][12][1] = 30+20;
        OFFSET[40][13][0] = 18+21;
        OFFSET[40][13][1] = 34+20;
        OFFSET[40][14][0] = 5+21;
        OFFSET[40][14][1] = 30+20;
        OFFSET[40][15][0] = 20+21;
        OFFSET[40][15][1] = 34+20;
        OFFSET[40][16][0] = 6+21;
        OFFSET[40][16][1] = 30+20;
        OFFSET[40][17][0] = 17+21;
        OFFSET[40][17][1] = 34+20;
        OFFSET[40][18][0] = 15+21;
        OFFSET[40][18][1] = 30+20;
        OFFSET[40][19][0] = 20+21;
        OFFSET[40][19][1] = 34+20;
        OFFSET[40][20][0] = 5+21;
        OFFSET[40][20][1] = 30+20;
        OFFSET[40][21][0] = 18+21;
        OFFSET[40][21][1] = 34+20;
        OFFSET[40][22][0] = 16+21;
        OFFSET[40][22][1] = 30+20;
        OFFSET[40][23][0] = 20+21;
        OFFSET[40][23][1] = 34+20;
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
            case 41: //explosion
                    entity = new AnimatedEntity(
                                id,
                                value,
                                new int[]{700,2000},
                                true,
                                false
                            );
                    break;
            case 42: 
                    entity = new Zombie(id);
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
            + getPositionY()
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
               + (int) (positionX); //add the objects position inside this coordinate
    }

    /**
     * Get the 2D-projection y-position where the object is rendered without regarding the camera.
     * @param coords  this parameter get's ignored because entitys know their own coordinates. You can pass <i>null</i> here.
     * @return The 2D-projection Y-position in pixels.
     */
   @Override
    public int get2DPosY(Coordinate coords) {
        return this.coords.getRelY() * DIM4 //y-coordinate * the tile's size
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
     * Returns the side with the current offset.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return Block.getSideID(positionX, positionY);
    }  

    /**
     *
     * @return
     */
    public float getPositionX() {
        return positionX;
    }

    /**
     *
     * @param offsetX
     */
    public void setPositionX(float offsetX) {
        this.positionX = offsetX;
    }

    /**
     *
     * @return
     */
    public float getPositionY() {
        return positionY;
    }

    /**
     *
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
