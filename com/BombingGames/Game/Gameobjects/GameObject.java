package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import static com.BombingGames.Game.Gameobjects.Block.CATEGORY;
import com.BombingGames.Game.Lighting.PseudoGrey;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import org.lwjgl.opengl.GL11;

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
    public static final int GAMEDIMENSION = (int) (DIMENSION / Math.sqrt(2));
        
    /**The sprite texture which contains every object texture*/
    private static TextureAtlas spritesheet;
    private static Pixmap pixmap;
    
    private final int id; 
    private int value;
    private boolean obstacle, transparent, visible, hidden; 
    private float lightlevel = 0.5f;
    private int dimensionY = 1;    
    
    /**
     * Creates an object. Use getInterface() to create blocks or entitys.
     * @param id the id of the object
     * @param value 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected GameObject(int id, int value) {
        this.id = id;
        this.value = value;
    }
    
    /**
     * Updates the logic of the object.
     * @param delta time since last update
     */
    public abstract void update(float delta);
    
    public abstract char getCategory();
    /**
     * Place you static update methods here.
     * @param delta 
     */
    public static void updateStaticUpdates(float delta){
        Sea.staticUpdate(delta);
    }
        
    
    /**
     *
     * @param coords
     * @return
     */
    public abstract int get2DPosX(Coordinate coords);
    
    /**
     *
     * @param coords
     * @return
     */
    public abstract int get2DPosY(Coordinate coords);
    
    /**
     * Draws an object.
     * @param coords the relative coordinates
     * @param camera 
     * @param view  
     */
    public void render(View view, WECamera camera, Coordinate coords) {
        Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getGlobalLight();
            }
        render(view, camera, coords, color.mul(lightlevel));
    }
    
     /**
     * Draws an object.
     * @param coords the relative coordinates
     * @param camera 
     * @param view 
     * @param color 
     */
    public void render(View view, WECamera camera, Coordinate coords, Color color) {
        //draw the object except not visible ones
        if (!hidden && visible) {             
            Sprite sprite = new Sprite(getSprite(getCategory(), id, value));
             
            int xPos = get2DPosX(coords) + getOffsetX();
            int yPos = get2DPosY(coords) - (dimensionY - 1) * DIM2 + getOffsetY();
            sprite.setPosition(xPos, yPos);
            
            prepareColor(view, color);

            sprite.setColor(color);
            sprite.draw(view.getBatch());
        }
    } 
    
    /**
     * Changes the color that it works with the blending.
     * @param view
     * @param color 
     */
    public void prepareColor(View view, Color color){
        float brightness = PseudoGrey.toFloat(color);
        
        if (brightness < 0.5f){
            view.setDrawmode(GL11.GL_MODULATE);
            color.mul(2);
        } else {
            view.setDrawmode(GL11.GL_ADD);
            color.r -= .5f;
            color.g -= .5f;
            color.b -= .5f;
        }
        color.clamp();
        color.a = 1;
    }
    
    /**
     * Load the spritesheet from memory.
     */
    public static void loadSheet()  {
        spritesheet = new TextureAtlas(Gdx.files.internal("com/BombingGames/Game/Blockimages/Spritesheet.txt"), true);
        pixmap = new Pixmap(Gdx.files.internal("com/BombingGames/Game/Blockimages/Spritesheet.png"));
        Gdx.app.debug("DEBUG","Spritesheet loaded");
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
     * Returns a sprite texture of non-block texture
     * @param category 
     * @param id
     * @param value
     * @return
     */
    public static AtlasRegion getSprite(char category, int id, int value) {
        AtlasRegion sprite = spritesheet.findRegion(category+Integer.toString(id)+"-"+value);
        if (sprite == null){ //if there is no sprite show the default "sprite not found sprite" for this category
            sprite = getSpritesheet().findRegion(CATEGORY+"0-0");
            if (sprite==null) {//load generic error sprite if category sprite failed
                sprite = getSpritesheet().findRegion("error");
                if (sprite==null) throw new NullPointerException("Sprite and category error not found and even the generic error sprite could not be found. Something with the sprites is fucked up.");
            }
        }
        return sprite;
    }


    //getter & setter
    
     /**
     * Returns the spritesheet used for rendering.
     * @return the spritesheet used by the objects
     */
    public static TextureAtlas getSpritesheet() {
        return spritesheet;
    }

    /**
     *
     * @return
     */
    public static Pixmap getPixmap() {
        return pixmap;
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
     * The lightlevel is a number between 0 and 1. 1 is full bright. 0 is black. Default is .5.
     * @return
     */
    public float getLightlevel() {
        return lightlevel;
    }

    /**
     * Returns the depth of the object. The depth is an int value wich is needed for producing the list of the renderorder. The higher the value the later it will be drawn.
     * @param coords 
     * @return the depth
     */
    public abstract int getDepth(Coordinate coords);
    
    /**
     * Returns the name of the object
     * @return the name of the object
     */
    public abstract String getName();
    
    public abstract int getOffsetX();
    
    public abstract int getOffsetY();

    /**
     * Get the value. It is like a sub-id and can identify the status.
     * @return
     */
    public int getValue() {
        return value;
    }
    
        /**
     * 
     * @return
     */
    public int getDimensionY() {
        return dimensionY;
    }

    /**
     * Returns true, when set as hidden. Hidden objects are not rendered even when they are visible ("visible" by the meaning of the raytracing).
     * @return if the object is invisible
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
     * The lightlevel is a number between 0 and 1. 1 is full bright. 0 is black.
     * @param lightlevel
     */
    public void setLightlevel(float lightlevel) {
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
     * Has the object transparent areas?
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
     * Hide this object and prevent it from beeing rendered. Don't use this to hide objects. This data is only for rendering data and view specific not for gameworld information. This should be just used for setting during the rendering process.
     * @param visible Sets the visibility.
     * @see com.BombingGames.Game.Gameobjects.GameObject#setHidden(boolean) 
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Hide an object. It won't be rendered even if it is visible.
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
}