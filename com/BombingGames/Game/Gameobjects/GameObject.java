package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Coordinate;
import com.BombingGames.Game.View;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
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
    private boolean obstacle, transparent, visible, hidden; 
    private int lightlevel = 127;
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
    
    public abstract int getScreenPosX(Coordinate coords);
    
    public abstract int getScreenPosY(Coordinate coords);
    
    /**
     * Draws an object.
     * @param g 
     * @param coords the relative coordinates
     * @param view  
     */
    public void render(Graphics g,View view, Coordinate coords) {
        render(g, view, coords, lightlevel);
    }
    
     /**
     * Draws an object.
     * @param g 
     * @param coords the relative coordinates
     * @param view 
     * @param brightness  
     */
    public void render(Graphics g, View view, Coordinate coords, int brightness) {
        //draw the object except not visible ones
        if (!hidden && visible) {
            Image image = getSprite(id, value);
             
            int xPos = getScreenPosX(coords) + OFFSETLIST[id][value][0];
            int yPos = getScreenPosY(coords) - (dimensionY - 1) * DIM2 + OFFSETLIST[id][value][1];
            
            Color filter;
            if (brightness <= 127){
                view.setDrawmode(GL11.GL_MODULATE);
                filter = new Color(brightness/127f, brightness/127f, brightness/127f);
            } else {
                view.setDrawmode(GL11.GL_ADD);
                filter = new Color((brightness-127)/127f, (brightness-127)/127f, (brightness-127)/127f);
            }

            if (Controller.lightEngine != null) filter = filter.multiply(Controller.lightEngine.getLightColor());
            image.drawEmbedded(xPos, yPos, xPos+image.getWidth(), yPos+image.getHeight(), 0, 0, image.getWidth(), image.getHeight(), filter);
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
    public static Coordinate sideIDtoNeighbourCoords(Coordinate coords, int sideID) {
        int[] result = new int[3];
        switch (sideID) {
            case 0:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY() - 2;
                break;
            case 1:
                result[0] = coords.getRelX() + (coords.getRelY() % 2 == 1 ? 1 : 0);
                result[1] = coords.getRelY() - 1;
                break;
            case 2:
                result[0] = coords.getRelX() + 1;
                result[1] = coords.getRelY();
                break;
            case 3:
                result[0] = coords.getRelX() + (coords.getRelY() % 2 == 1 ? 1 : 0);
                result[1] = coords.getRelY() + 1;
                break;
            case 4:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY() + 2;
                break;
            case 5:
                result[0] = coords.getRelX() - (coords.getRelY() % 2 == 0 ? 1 : 0);
                result[1] = coords.getRelY() + 1;
                break;
            case 6:
                result[0] = coords.getRelX() - 1;
                result[1] = coords.getRelY();
                break;
            case 7:
                result[0] = coords.getRelX() - (coords.getRelY() % 2 == 0 ? 1 : 0);
                result[1] = coords.getRelY() - 1;
                break;
            default:
                result[0] = coords.getRelX();
                result[1] = coords.getRelY();
        }
        result[2] = coords.getZ();
        return new Coordinate(result[0], result[1], result[2], true);
    }
    
    /**
     * Returns the depth of the object. The depth is an int value wich is needed for producing the list of the renderorder. The higher the value the later it will be drawn.
     * @param y the y-coordinate
     * @param z  the z-coordinate
     * @return the depth
     */
    public int getDepth(Coordinate coords) {
        return (int) (
            DIMENSION * coords.getRelY()
            + (coords.getRelY() % 2) * DIM2
            + (DIM2-1) * coords.getZ()
            + coords.getCellOffset()[1]
            + coords.getCellOffset()[2]
            + (dimensionY - 1) * DIM4
        );
    }
    

    /**
     * Returns a sprite image of non-block image
     * @param id
     * @param value
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
     * The lightlevel is a number between 0 and 255. 100 is full bright. 0 is black. Default is 50.
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
     * Get the value. It is like a sub-id and can identify the status.
     * @return
     */
    public int getValue() {
        return value;
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
     * The lightlevel is a number between 0 and 255. 255 is full bright. 0 is black.
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

    /**
     * 
     * @return
     */
    public int getDimensionY() {
        return dimensionY;
    }
}