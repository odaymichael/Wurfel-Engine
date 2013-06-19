package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.View;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 *An object is something wich can be found in the game world.
 * @author Benedikt
 */
public abstract class GameObject {
    /**Screen DIMENSION of a block/object in pixels. This is the length from the left to the right border of the block.
     * In game coordinates this is also the dimension from top to bottom.*/
    public static final int DIMENSION = 160;
    /**The half (2) of DIMENSION. The short form of: DIMENSION/2*/
    public static final int DIM2 = DIMENSION / 2;
    /**A quarter (4) of DIMENSION. The short form of: DIMENSION/4*/
    public static final int DIM4 = DIMENSION / 4;
    /**the max. amount of different object types*/
    public static final int OBJECTTYPESCOUNT = 99;
    /**The real game world dimension in pixel. Usually the use of DIMENSION is fine because of the map format every coordinate center is straight.
        * The value is DIMENSION/sqrt(2).
        */
    public static int GAMEDIMENSION = (int) (DIMENSION / Math.sqrt(2));

    /** A list containing the offset of the objects. */
    public static final int[][][] OFFSETLIST = new int[OBJECTTYPESCOUNT][10][2];
    
     /**Containts the names of the objects. index=id*/
    public static final String[] NAMELIST = new String[OBJECTTYPESCOUNT];   
    
    /**The sprite image which contains every object image*/
    private static PackedSpriteSheet spritesheet;
    
    private final int id; 
    private int value;
    private float[] pos = {DIM2, DIM2, 0};
    private boolean obstacle, transparent, visible, hidden; 
    private int lightlevel = 50;
    private int dimensionY = 1;    
    
    static {
        
        NAMELIST[0] = "air";
        NAMELIST[1] = "grass";
        NAMELIST[2] = "dirt";
        NAMELIST[3] = "stone";
        NAMELIST[4] = "asphalt";
        NAMELIST[5] = "cobblestone";
        NAMELIST[6] = "pavement";
        NAMELIST[7] = "concrete";
        NAMELIST[8] = "sand";
        NAMELIST[9] = "water";
        NAMELIST[20] = "red brick wall";
        NAMELIST[30] = "fence";
        NAMELIST[32] = "sandbags";
        NAMELIST[33] = "crate";
        NAMELIST[34] = "flower";
        OFFSETLIST[34][0][0] = 71;
        OFFSETLIST[34][0][1] = 78;
        NAMELIST[35] = "round bush";
        OFFSETLIST[35][0][0] = 22;
        OFFSETLIST[35][0][1] = 2;
        NAMELIST[40] = "player";
        OFFSETLIST[40][0][0] = 54;
        OFFSETLIST[40][0][1] = 37;
        OFFSETLIST[40][1][0] = 55;
        OFFSETLIST[40][1][1] = 38;
        OFFSETLIST[40][2][0] = 53;
        OFFSETLIST[40][2][1] = 35;
        OFFSETLIST[40][3][0] = 46;
        OFFSETLIST[40][3][1] = 33;
        OFFSETLIST[40][4][0] = 53;
        OFFSETLIST[40][4][1] = 35;
        OFFSETLIST[40][5][0] = 64;
        OFFSETLIST[40][5][1] = 33;
        OFFSETLIST[40][6][0] = 53;
        OFFSETLIST[40][6][1] = 33;
        OFFSETLIST[40][7][0] = 46;
        OFFSETLIST[40][7][1] = 33;
        NAMELIST[41] = "smoke test";
        NAMELIST[50] = "strewbed";
        NAMELIST[70] = "campfire";
        NAMELIST[71] = "explosive barrel";
        NAMELIST[72] = "animation test";
    }
    
    /**
     * Creates an object. Use getInterface() to create blocks or entitys.
     * @param id the id of the object
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected GameObject(int id) {
        this.id = id;
    }
    
    /**
     * Updates the logic of the object.
     * @param delta time since last update
     */
    public abstract void update(int delta);
    
     /**
     * Draws an object.
     * @param coords the relative coordinates
     */
    public void render(Graphics g,View view, int[] coords) {
        //draw the object except not visible ones
        if (!hidden && visible) {
            Image image = getSprite(id, value);
            //calc  brightness
            float brightness = lightlevel / 50.0f;
            
            image.setColor(0, brightness, brightness, brightness);
            image.setColor(1, brightness, brightness, brightness);
            brightness -= 0.1f;
            image.setColor(2, brightness, brightness, brightness);
            image.setColor(3, brightness, brightness, brightness);
            
            int xpos = getScreenPosX(this, coords) + OFFSETLIST[id][value][0];
            int ypos = getScreenPosY(this, coords) - (dimensionY - 1) * DIM2 + OFFSETLIST[id][value][1];
            image.drawEmbedded(xpos, ypos, image.getWidth(), image.getHeight());
        }
    } 

    /**
     * Load the spritesheet from memory.
     * @throws SlickException
     */
    public static void loadSheet() throws SlickException {
        spritesheet = new PackedSpriteSheet("com/BombingGames/Game/Blockimages/Spritesheet.def");
        Log.debug("Spritesheet loaded");
    }

    /**
     * Returns the field-id where the coordiantes are inside in relation to the current field. Field id count clockwise, starting with the top with 0.
     * If you want to get the neighbour you can use sideIDtoNeighbourCoords(int[], int) with the second parameter foudn by this function.
     * The counting:<br>
     * 7 \ 0 / 1<br>
     * -------<br>
     * 6 | 8 | 2<br>
     * -------<br>
     * 5 / 4 \ 3<br>
     * @param x game-space-coordinates, value in pixels
     * @param y game-space-coordinates, value in pixels
     * @return Returns the fieldnumber of the coordinates. 8 is the field itself.
     * @see com.BombingGames.Game.Gameobjects.GameObject#sideIDtoNeighbourCoords(int[], int)
     */
    public static int getSideID(float x, float y) {
        int result = 8;
        if (x + y <= Block.DIM2) {
            result = 7;
        }
        if (x - y >= Block.DIM2) {
            if (result == 7) {
                result = 0;
            } else {
                result = 1;
            }
        }
        if (x + y >= 3 * Block.DIM2) {
            if (result == 1) {
                result = 2;
            } else {
                result = 3;
            }
        }
        if (-x + y >= Block.DIM2) {
            if (result == 3) {
                result = 4;
            } else if (result == 7) {
                result = 6;
            } else {
                result = 5;
            }
        }
        return result;
    }

    /**
     * Get the neighbour coordinates of the neighbour of the coords you give.
     * @param coords the coordinates of the field
     * @param sideID the side number of the given coordinates
     * @return The coordinates of the neighbour.
     */
    public static int[] sideIDtoNeighbourCoords(int[] coords, int sideID) {
        int[] result = new int[3];
        switch (sideID) {
            case 0:
                result[0] = coords[0];
                result[1] = coords[1] - 2;
                break;
            case 1:
                result[0] = coords[0] + (coords[1] % 2 == 1 ? 1 : 0);
                result[1] = coords[1] - 1;
                break;
            case 2:
                result[0] = coords[0] + 1;
                result[1] = coords[1];
                break;
            case 3:
                result[0] = coords[0] + (coords[1] % 2 == 1 ? 1 : 0);
                result[1] = coords[1] + 1;
                break;
            case 4:
                result[0] = coords[0];
                result[1] = coords[1] + 2;
                break;
            case 5:
                result[0] = coords[0] - (coords[1] % 2 == 0 ? 1 : 0);
                result[1] = coords[1] + 1;
                break;
            case 6:
                result[0] = coords[0] - 1;
                result[1] = coords[1];
                break;
            case 7:
                result[0] = coords[0] - (coords[1] % 2 == 0 ? 1 : 0);
                result[1] = coords[1] - 1;
                break;
            default:
                result[0] = coords[0];
                result[1] = coords[1];
        }
        result[2] = coords[2];
        return result;
    }
    
    /**
     * Returns the depth of the object. The depth is an int value wich is needed for producing the list of the renderorder. The higher the value the later it will be drawn.
     * @param y the y-coordinate
     * @param z  the z-coordinate
     * @return the depth
     */
    public int getDepth(int y, int z) {
        return (int) (
            DIMENSION * y
            + (y % 2) * DIM2
            + (DIM2-1) * z
            + pos[1]
            + pos[2]
            + (dimensionY - 1) * DIM4
        );
    }
    
  

    /**
     * Get the screen x-position where the object is rendered without regarding the camera.
     * @param object The object of wich you want the position
     * @param coords  The relative coordinates where the object is rendered 
     * @return The screen X-position in pixels.
     */
    public static int getScreenPosX(GameObject object, int[] coords) {
        return coords[0] * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (coords[1] % 2) * DIM2 //y-coordinate multiplied by it's dimension in this direction
               + (int) (object.pos[0]); //add the objects position inside this coordinate
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param object The object of wich you want the position
     * @param coords The coordinates where the object is rendered 
     * @return The screen Y-position in pixels.
     */
    public static int getScreenPosY(GameObject object, int[] coords) {
        return coords[1] * DIM4 //x-coordinate * the tile's size
               - coords[2] * DIM2 //put higher blocks higher
               + (int) (object.pos[1] / 2) //add the objects position inside this coordinate
               - (int) (object.pos[2] / Math.sqrt(2)); //take axis shortening into account
    }

    /**
     * Returns a sprite image of non-block image
     * @param id
     * @param value
     * @param dimY the height of the object
     * @return
     */
    public static Image getSprite(int id, int value) {
        return spritesheet.getSprite(id+"-"+value);
    }

    

    //getter & setter
    
     /**
     * Returns the spritesheet used for rendering.
     * @return the spritesheet used by the objects
     */
    public static PackedSpriteSheet getSpritesheet() {
        return spritesheet;
    }
    
    /**
     * returns the id of a object
     * @return getId
     */
    public int getId() {
        return this.id;
    }

    /**
     * How bright is the object?
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black. Default is 50.
     * @return
     */
    public int getLightlevel() {
        return lightlevel;
    }

    /**
     * Returns the name of the object
     * @return the name of the object
     */
    public String getName() {
        return NAMELIST[getId()];
    }

    /**
     *  Gets the positon of the object inside it's coordinate field. Coordinate system starting at bottom rear.
     * @return an array with three fields. [x,y,z]
     */
    public float[] getPos() {
        return pos;
    }

    /**
     * Get the value. It is like a sub-id and can identify the status.
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Has the object an offset (pos vector)?
     * @return when it has offset true, else false
     */
    public boolean hasOffset() {
        return pos[0] != DIM2 || pos[1] != DIM2 || pos[2] != 0;
    }

    /**
     * Returns true, when set as hidden. Hidden objects are not rendered even when they are visible ("visible" by the meaning of the raytracing).
     * @return the locked visiblity
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Is this object an obstacle or can you pass through?
     * @return
     */
    public boolean isObstacle() {
        return obstacle;
    }

    /**
     * Can light travel through object?
     * @return
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Is the object visible?
     * @return true when visible
     */
    public boolean isVisible() {
        return visible;
    }

  

    /**
     * Set the brightness of the object.
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black.
     * @param lightlevel
     */
    public void setLightlevel(int lightlevel) {
        this.lightlevel = lightlevel;
    }

    /**
     * Make the object to an obstacle or passable.
     * @param obstacle true when obstacle. False when passable.
     */
    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    /**
     * Set a whole new array containing the positon of the object. Coordinate system starting at bottom rear.
     * @param pos the new positon array
     */
    public void setPos(float[] pos) {
        this.pos = pos;
    }

    /**
     * Sets the position of the object. Coordinate system starting at bottom rear.
     * @param i Select the field you want to write 0=>x, y=>1, z=>2
     * @param value the value you want to set it to.
     */
    public void setPos(int i, float value) {
        pos[i] = value;
    }

    /**
     * Make the Object transparent
     * @param transparent
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    /**
     * Set the value of the object.
     * @param value
     */
    protected void setValue(int value) {
        this.value = value;
    }

    /**
     * Hide this object and prevent it from beeing rendered.
     * @param visible Sets the visibility. When it is false, every side will also get hidden
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * 
     * @param hidden
     */
    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    /**
     * Set the height of the object.
     * @param dimensionY
     */
    public void setDimensionY(int dimensionY) {
        this.dimensionY = dimensionY;
    }

    public int getDimensionY() {
        return dimensionY;
    }
}
