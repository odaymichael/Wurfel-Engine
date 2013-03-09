package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Camera;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *An object is something wich can be found in the game world.
 * @author Benedikt
 */
public abstract class GameObject {
        /**
     * Screen DIMENSION of the Block in pixels. This is the length of the side, when you cut the block in the middle
     */
    public static final int DIMENSION = 160;
    /**
     * The half (2) of DIMENSION. The short form of DIMENSION/2
     */
    public static final int DIM2 = Block.DIMENSION / 2;
    /**
     * A quarter (4) of DIMENSION. The short form of DIMENSION/4
     */
    public static final int DIM4 = Block.DIMENSION / 4;
    /**
     * The real game world dimension in pixel. Usually DIMENSION should be enough becaue of the map format. The value is DIMENSION/sqrt(2).
     */
    public static int GAMEDIMENSION = (int) (Block.DIMENSION / Math.sqrt(2));
    /**
     * Has the positions of the sprites for rendering with sides
     * 1. Dimension id
     * 2. Dimension: value
     * 3. Dimension: Side
     * 4. Dimension: X- or Y-coordinate
     */
    public static final int[][][][] SPRITEPOS = new int[99][9][3][2];

     /**
     * Containts the names of the blocks. index=id
     */
    public static final String[] NAMELIST = new String[99];   
    
    /**
     * The sprite image which contains every block image
     */
    private static Image spritesheet;
    
    private final int id; 
    private int value;
    private float[] pos = {Block.DIM2, Block.DIM2, 0};
    private boolean obstacle, transparent, visible, hidden; 
    private int lightlevel = 50;
    private int dimensionY = 1;    
    
    static {
        NAMELIST[0] = "air";
        
        NAMELIST[1] = "grass";
        SPRITEPOS[1][0][0][0] = 0;
        SPRITEPOS[1][0][0][1] = 0;
        SPRITEPOS[1][0][1][0] = 80;
        SPRITEPOS[1][0][1][1] = 0;
        SPRITEPOS[1][0][2][0] = 240;
        SPRITEPOS[1][0][2][1] = 0;
        
        NAMELIST[2] = "dirt";
        SPRITEPOS[2][0][0][0] = 0;
        SPRITEPOS[2][0][0][1] = 0;
        SPRITEPOS[2][0][1][0] = 400;
        SPRITEPOS[2][0][1][1] = 0;
        SPRITEPOS[2][0][2][0] = 560;
        SPRITEPOS[2][0][2][1] = 0;
        
        NAMELIST[3] = "stone";
        SPRITEPOS[3][0][0][0] = 0;
        SPRITEPOS[3][0][0][1] = 120;
        SPRITEPOS[3][0][1][0] = 80;
        SPRITEPOS[3][0][1][1] = 120;
        SPRITEPOS[3][0][2][0] = 240;
        SPRITEPOS[3][0][2][1] = 120;
        
        NAMELIST[4] = "asphalt";
        SPRITEPOS[4][0][0][0] = 320;
        SPRITEPOS[4][0][0][1] = 120;
        SPRITEPOS[4][0][1][0] = 400;
        SPRITEPOS[4][0][1][1] = 120;
        SPRITEPOS[4][0][2][0] = 560;
        SPRITEPOS[4][0][2][1] = 120;
       
        NAMELIST[5] = "cobblestone";
        SPRITEPOS[5][0][0][0] = 0;
        SPRITEPOS[5][0][0][1] = 600;
        SPRITEPOS[5][0][1][0] = 80;
        SPRITEPOS[5][0][1][1] = 600;
        SPRITEPOS[5][0][2][0] = 240;
        SPRITEPOS[5][0][2][1] = 600;

        NAMELIST[6] = "pavement";
        SPRITEPOS[6][0][0][0] = 320;
        SPRITEPOS[6][0][0][1] = 600;
        SPRITEPOS[6][0][1][0] = 400;
        SPRITEPOS[6][0][1][1] = 600;
        SPRITEPOS[6][0][2][0] = 560;
        SPRITEPOS[6][0][2][1] = 600;
       
        NAMELIST[7] = "concrete";
        SPRITEPOS[7][0][0][0] = 640;
        SPRITEPOS[7][0][0][1] = 600;
        SPRITEPOS[7][0][1][0] = 720;
        SPRITEPOS[7][0][1][1] = 600;
        SPRITEPOS[7][0][2][0] = 880;
        SPRITEPOS[7][0][2][1] = 600;
        
        NAMELIST[8] = "sand";
        SPRITEPOS[8][0][0][0] = 320;
        SPRITEPOS[8][0][0][1] = 720;
        SPRITEPOS[8][0][1][0] = 400;
        SPRITEPOS[8][0][1][1] = 720;
        SPRITEPOS[8][0][2][0] = 560;
        SPRITEPOS[8][0][2][1] = 720;
        
        NAMELIST[9] = "water";
        SPRITEPOS[9][0][0][0] = 640;
        SPRITEPOS[9][0][0][1] = 720;
        SPRITEPOS[9][0][1][0] = 720;
        SPRITEPOS[9][0][1][1] = 720;
        SPRITEPOS[9][0][2][0] = 880;
        SPRITEPOS[9][0][2][1] = 720;
        
        
        NAMELIST[40] = "player";
        //sw
        SPRITEPOS[40][1][0][0] = 640;
        SPRITEPOS[40][1][0][1] = 120;
        //w
        SPRITEPOS[40][2][0][0] = 800;
        SPRITEPOS[40][2][0][1] = 120;
        //nw
        SPRITEPOS[40][3][0][0] = 0;
        SPRITEPOS[40][3][0][1] = 360;
        //n
        SPRITEPOS[40][4][0][0] = 160;
        SPRITEPOS[40][4][0][1] = 360;
        //ne
        SPRITEPOS[40][5][0][0] = 320;
        SPRITEPOS[40][5][0][1] = 360;
        //e
        SPRITEPOS[40][6][0][0] = 480;
        SPRITEPOS[40][6][0][1] = 360;
        //se
        SPRITEPOS[40][7][0][0] = 640;
        SPRITEPOS[40][7][0][1] = 360;
        //s
        SPRITEPOS[40][8][0][0] = 800;
        SPRITEPOS[40][8][0][1] = 360;
        
        NAMELIST[50] = "smoke test";
        SPRITEPOS[50][0][0][0] = 0;
        SPRITEPOS[50][0][0][1] = 120;
        SPRITEPOS[50][0][1][0] = 80;
        SPRITEPOS[50][0][1][1] = 120;
        SPRITEPOS[50][0][2][0] = 240;
        SPRITEPOS[50][0][2][1] = 120;
        SPRITEPOS[50][1][0][0] = 0;
        SPRITEPOS[50][1][0][1] = 0;
        SPRITEPOS[50][1][1][0] = 80;
        SPRITEPOS[50][1][1][1] = 0;
        SPRITEPOS[50][1][2][0] = 240;
        SPRITEPOS[50][1][2][1] = 0;
        
        NAMELIST[70] = "fire";
        SPRITEPOS[70][0][0][0] = 0;
        SPRITEPOS[70][0][0][1] = 720;
        
        NAMELIST[71] = "explosive barrel";
        SPRITEPOS[71][0][0][0] = 160;
        SPRITEPOS[71][0][0][1] = 720;
        
        NAMELIST[72] = "animation test";
        SPRITEPOS[72][0][0][0] = 0;
        SPRITEPOS[72][0][0][1] = 120;
        SPRITEPOS[72][0][1][0] = 80;
        SPRITEPOS[72][0][1][1] = 120;
        SPRITEPOS[72][0][2][0] = 240;
        SPRITEPOS[72][0][2][1] = 120;
        SPRITEPOS[72][1][0][0] = 0;
        SPRITEPOS[72][1][0][1] = 0;
        SPRITEPOS[72][1][1][0] = 80;
        SPRITEPOS[72][1][1][1] = 0;
        SPRITEPOS[72][1][2][0] = 240;
        SPRITEPOS[72][1][2][1] = 0;
    }
    
    /**
     * Creates an object. Use getInterface().
     * @param id
     */
    protected GameObject(int id) {
        this.id = id;
    }

    /**
     * Creates an object. Use getInterface().
     * @param id
     * @param value
     */
    protected GameObject(int id, int value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 
     * @param id
     * @param value
     * @param obstacle
     * @param transparent
     * @param visible
     * @param hidden
     * @param dimensionY
     */
    protected GameObject(int id, int value, boolean obstacle, boolean transparent, boolean visible, boolean hidden, int dimensionY) {
        this.id = id;
        this.value = value;
        this.obstacle = obstacle;
        this.transparent = transparent;
        this.visible = visible;
        this.hidden = hidden;
        this.dimensionY = dimensionY;
    }
    
    /**
     * updates the logic of the object.
     * @param delta time since last update
     */
    public abstract void update(int delta);
    


    /**
     * Returns the spritesheet used for rendering
     * @return the spritesheet used by the objects
     */
    public static Image getSpritesheet() {
        return spritesheet;
    }
    

    /**
     * Get the screen x-position where the block is rendered without regarding the camera.
     * @param object the object of wich you want the position
     * @param coords  the coordinates where the object is rendered 
     * @return the screen X-position in pixels
     */
    public static int getScreenPosX(GameObject object, int[] coords) {
        return coords[0] * DIMENSION + (coords[1] % 2) * DIM2 + (int) (object.pos[0]);
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param object the object of wich you want the position
     * @param coords the coordinates where the object is rendered 
     * @return the screen Y-position in pixels
     */
    public static int getScreenPosY(GameObject object, int[] coords) {
        return coords[1] * DIM4 - coords[2] * DIM2 + (int) (object.pos[1] / 2) - (int) (object.pos[2] / Math.sqrt(2));
    }

    /**
     * Returns a sprite image of non-block image
     * @param id
     * @param value
     * @param dimY
     * @return
     */
    public static Image getSprite(int id, int value, int dimY) {
        return spritesheet.getSubImage(SPRITEPOS[id][value][0][0], SPRITEPOS[id][value][0][1], DIMENSION, dimY * DIM2 + DIM2);
    }

    /**
     * Load the spritesheet from memory.
     * @throws SlickException
     */
    public static void loadSheet() throws SlickException {
        spritesheet = new SpriteSheet("com/BombingGames/Game/Blockimages/Spritesheet.png", DIMENSION, (int) (DIM2 * 1.5));
    }

    /**
     * Returns the field-number where the coordiantes are in in relation to the current Block. Counts clockwise startin with the top 0.
     * If you want to get the neighbour you have to use a SelfAwareBlock and the method getNeighbourBlock
     * 701
     * 682
     * 543
     * @param x value in pixels
     * @param y value in pixels
     * @return Returns the fieldnumber of the coordinates. 8 is self.
     * @see com.BombingGames.Game.Blocks.SelfAwareBlock#getNeighbourBlock(int, int)
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
     * Get the neighbour coordinates of the neighbour of the coords you give
     * @param coords
     * @param sidenumb the side number of the given coordinates
     * @return coordinates of the neighbour
     */
    public static int[] sideIDtoNeighbourCoords(int[] coords, int sidenumb) {
        int[] result = new int[3];
        switch (sidenumb) {
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
            + DIMENSION * z
            + pos[1]
            + pos[2]
            + (dimensionY - 1) * DIMENSION
            );
    }

    //getter & setter
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
        return pos[0] != 80 || pos[1] != 80 || pos[2] != 0;
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
     * Make the Block transparent
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
     * Draws a block
     * @param coords 
     */
    public void render(int[] coords) {
        //draw every visible block except not visible ones
        if (!hidden && visible) {
            Image image = getSprite(id, value, dimensionY);
            //calc  brightness
            float brightness = lightlevel / 100.0F;
            //System.out.println("Lightlevel " + Controller.map.data[x][y][z].lightlevel + "-> "+lightlevel);
            //or paint whole block with :
            //int brightness = renderBlock.lightlevel * 255 / 100;
            //new Color(brightness,brightness,brightness).bind();
            image.setColor(0, brightness, brightness, brightness);
            image.setColor(1, brightness, brightness, brightness);
            brightness -= 0.1F;
            image.setColor(2, brightness, brightness, brightness);
            image.setColor(3, brightness, brightness, brightness);
            int xpos = getScreenPosX(this, coords);
            int ypos = getScreenPosY(this, coords) - (dimensionY - 1) * Block.DIM2;
            image.drawEmbedded(xpos, ypos);
        }
    } 
}
