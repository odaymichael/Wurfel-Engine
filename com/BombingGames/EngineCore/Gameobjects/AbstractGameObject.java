package com.BombingGames.EngineCore.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import com.BombingGames.EngineCore.LightEngine.PseudoGrey;
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
public abstract class AbstractGameObject {
    /**Screen SCREEN_DEPTH of a block/object sprite in pixels. This is the length from the top to the middle border of the block.
     * In game coordinates this is also the dimension from top to bottom.*/
    public static final int SCREEN_DEPTH = 80;
    /**The half (2) of SCREEN_DEPTH. The short form of: SCREEN_DEPTH/2*/
    public static final int SCREEN_DEPTH2 = SCREEN_DEPTH / 2;
    /**A quarter (4) of SCREEN_DEPTH. The short form of: SCREEN_DEPTH/4*/
    public static final int SCREEN_DEPTH4 = SCREEN_DEPTH / 4;
    
    /**
     * The width (x-axis) of the sprite size
     */
    public static final int SCREEN_WIDTH = 160;
    /**The half (2) of SCREEN_WIDTH. The short form of: SCREEN_WIDTH/2*/
    public static final int SCREEN_WIDTH2 = SCREEN_WIDTH / 2;
    /**A quarter (4) of SCREEN_WIDTH. The short form of: SCREEN_WIDTH/4*/
    public static final int SCREEN_WIDTH4 = SCREEN_WIDTH / 4;
    
    /**
     * The width (x-axis) of the sprite size
     */
    public static final int SCREEN_HEIGHT = 80;
    /**The half (2) of SCREEN_WIDTH. The short form of: SCREEN_WIDTH/2*/
    public static final int SCREEN_HEIGHT2 = SCREEN_HEIGHT / 2;
    /**A quarter (4) of SCREEN_WIDTH. The short form of: SCREEN_WIDTH/4*/
    public static final int SCREEN_HEIGHT4 = SCREEN_HEIGHT / 4;
    
    /**the max. amount of different object types*/
    public static final int OBJECTTYPESCOUNT = 99;
      /**the max. amount of different values*/
    public static final int VALUESCOUNT = 25;
    

    
    /**The real game world dimension in pixel. Usually the use of SCREEN_DEPTH is fine because of the map format every coordinate center is straight.
        * The value is SCREEN_DEPTH/sqrt(2).
        */
    public static final int GAMEDIMENSION = (int) (SCREEN_HEIGHT*2 / Math.sqrt(2));
        
    /**The sprite texture which contains every object texture*/
    private static TextureAtlas spritesheet;
    private static Pixmap pixmap;
    private static AtlasRegion[][][] sprites = new AtlasRegion[(int) 'z'][OBJECTTYPESCOUNT][VALUESCOUNT];//{category}{id}{value}
    
    private final int id; 
    private int value;
    private boolean obstacle, transparent, clipped, hidden; 
    private float lightlevel = 0.5f;
    private int dimensionZ = 1;  

    
    /**
     * Creates an object. Use getInterface() to create blocks or entitys.
     * @param id the id of the object
     * @param value 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected AbstractGameObject(int id, int value) {
        this.id = id;
        this.value = value;
    }
    
    /**
     * Updates the logic of the object.
     * @param delta time since last update
     */
    public abstract void update(float delta);
    
    /**
     *
     * @return
     */
    public abstract char getCategory();
    /**
     * Place you static update methods here.
     * @param delta 
     */
    public static void updateStaticUpdates(float delta){
        Sea.staticUpdate(delta);
    }
        
    
    /**
     *Get the screen position of this object.
     * @param coords
     * @return
     */
    public abstract int get2DPosX(Coordinate coords);
    
    /**
     *Get the screen position of this object.
     * @param coords
     * @return
     */
    public abstract int get2DPosY(Coordinate coords);
    
    /**
     * Draws an object in the color of the light engine and with the lightlevel. Only draws if not hidden and not clipped.
     * @param coords the coordinates where the object should be rendered
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     */
    public void render(View view, WECamera camera, Coordinate coords) {
        Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getGlobalLight();
        }
        render(view, camera, coords, color.mul(lightlevel));
    }
    
     /**
     * Draws an object if it is not hidden and not clipped.
     * @param coords the coordinates where the object is rendered
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param color  custom blending color
     */
    public void render(View view, WECamera camera, Coordinate coords, Color color) {
        //draw the object except not clipped ones
        if (!hidden && !clipped) {             
             
            int xPos = get2DPosX(coords) + getOffsetX();
            int yPos = get2DPosY(coords) - (dimensionZ - 1) * SCREEN_HEIGHT + getOffsetY();
            
            renderAt(view, xPos, yPos, color);
        }
    }
    
        /**
     * Renders at a custom position with the global light.
     * @param view the view using this render method
     * @param xPos rendering position
     * @param yPos rendering position
     */
    public void renderAt(View view, int xPos, int yPos) {
        Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getGlobalLight();
        }
        renderAt(view, xPos, yPos, color);
    }
    
    /**
     * Renders at a custom position with a custom light.
     * @param view
     * @param xPos rendering position
     * @param yPos rendering position
     * @param color  custom blending color
     */
    public void renderAt(View view, int xPos, int yPos, Color color) {
        Sprite sprite = new Sprite(getSprite(getCategory(), id, value));
        sprite.setPosition(xPos, yPos);
        
        prepareColor(view, color);

        sprite.setColor(color);
        sprite.draw(view.getBatch());
    }
    
    /**
     * Changes the color that it works with the blending. Sets the blending mode.
     * @param view
     * @param color a tint in which the sprite should be rendered
     */
    public void prepareColor(View view, Color color){
        float brightness = PseudoGrey.toFloat(color);
        //float brightness = (color.r+color.g+color.b)/3;
        
        if (brightness > 0.5f){
            view.setDrawmode(GL11.GL_ADD);
            color.r -= .5f;
            color.g -= .5f;
            color.b -= .5f;
        } else {
            view.setDrawmode(GL11.GL_MODULATE);
            color.mul(2);
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
        Gdx.app.log("DEBUG","Spritesheet loaded");
    }

    /**
     * Returns a sprite texture. You may use your own method like in <i>Block</i>.
     * @param category the category of the sprite e.g. "b" for blocks
     * @param id the id of the object
     * @param value the value of the object
     * @return 
     */
    public static AtlasRegion getSprite(char category, int id, int value) {
        if (sprites[category][id][value] == null){ //load if not already loaded
            AtlasRegion sprite = spritesheet.findRegion(category+Integer.toString(id)+"-"+value);
            if (sprite == null){ //if there is no sprite show the default "sprite not found sprite" for this category
                Gdx.app.log("debug", category+Integer.toString(id)+"-"+value + " not found");
                sprite = getSpritesheet().findRegion(category+"0-0");
                if (sprite == null) {//load generic error sprite if category sprite failed
                    sprite = getSpritesheet().findRegion("error");
                    if (sprite == null) throw new NullPointerException("Sprite and category error not found and even the generic error sprite could not be found. Something with the sprites is fucked up.");
                }
            }
            sprites[category][id][value] = sprite;
            return sprite;
        } else {
            return sprites[category][id][value];
        }
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
     * @return the depth in game size
     */
    public abstract int getDepth(Coordinate coords);
    
    /**
     * Returns the name of the object
     * @return the name of the object
     */
    public abstract String getName();
    
    /**
     *Get the offset of the sprite. Center is top left corner.
     * @return
     */
    public abstract int getOffsetX();
    
    /**
     *Get the offset of the sprite. Center is top left corner.
     * @return
     */
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
    public int getDimensionZ() {
        return dimensionZ;
    }

    /**
     * Returns true, when set as hidden. Hidden objects are not rendered even when they are clipped ("clipped" by the meaning of the raytracing).
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
     * Is the object clipped?
     * @return true when clipped
     */
    public boolean isClipped() {
        return clipped;
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
     * @param clipped Sets the visibility.
     * @see com.BombingGames.Game.Gameobjects.AbstractGameObject#setHidden(boolean) 
     */
    public void setClipped(boolean clipped) {
        this.clipped = clipped;
    }

    /**
     * Hide an object. It won't be rendered even if it is clipped.
     * @param hidden
     */
    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    /**
     * Set the height of the object.
     * @param dimensionZ
     */
    public void setDimensionZ(int dimensionZ) {
        this.dimensionZ = dimensionZ;
    }

    /**
     *
     * @return
     */
    public static AtlasRegion[][][] getSprites() {
        return sprites;
    }
}