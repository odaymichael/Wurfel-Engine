package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_DEPTH2;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_DEPTH4;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.GAMEDIMENSION;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.OBJECTTYPESCOUNT;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_WIDTH2;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.SCREEN_WIDTH4;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.VALUESCOUNT;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.getPixmap;
import static com.BombingGames.Game.Gameobjects.AbstractGameObject.getSpritesheet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * A Block is a wonderful piece of information and a geometrical object.
 * @author Benedikt Vogler
 */
public class Block extends AbstractGameObject {
    private static Color[][] colorlist = new Color[OBJECTTYPESCOUNT][VALUESCOUNT];
    /**The id of the left side of a block.*/
    public static final int LEFTSIDE=0;
    /**The id of the top side of a block.*/
    public static final int TOPSIDE=1;
    /**The id of the right side of a block.*/
    public static final int RIGHTSIDE=2;
    
    /**
     *
     */
    public static final char CATEGORY = 'b';
    
    /**Containts the names of the objects. index=id*/
    public static final String[] NAMELIST = new String[OBJECTTYPESCOUNT];
    
       /** A list containing the offset of the objects. */
    public static final int[][][] OFFSET = new int[OBJECTTYPESCOUNT][VALUESCOUNT][2];
    
    private boolean liquid;
    private boolean hasSides = true;
    private boolean clippedRight = false;
    private boolean clippedTop = false;
    private boolean clippedLeft = false;
    
    private static AtlasRegion[][][] blocksprites = new AtlasRegion[OBJECTTYPESCOUNT][VALUESCOUNT][3];//{id}{value}{side}
    
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
        NAMELIST[35] = "round bush";
        OFFSET[34][0][0] = 71;
        OFFSET[34][0][1] = 78;
        OFFSET[35][0][0] = 22;
        OFFSET[35][0][1] = 2;
        NAMELIST[50] = "strewbed";
        NAMELIST[70] = "campfire";
        NAMELIST[71] = "explosive barrel";
        OFFSET[71][0][0] = 39;
        OFFSET[71][0][1] = 19;
        OFFSET[71][1][0] = 35;
        OFFSET[71][1][1] = 16;
        NAMELIST[72] = "animation test";
    }
    
    /**
     * Don't use this constructor to get a new block. Use the static <i>getInstance</i> methods instead.
     * @param id
     *  @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected Block(int id){
        super(id,0);
    } 
    
    /**
     *  Create a block. If the block needs to know it's position you have to use <i>getInstance(int id, int value,int x, int y, int z)</i>
     * @param id the block's id
     * @return the wanted block.
     */
    public static Block getInstance(int id){
        return getInstance(id,0,null);
    }
    
    /**
     * Create a block. If the block needs to know it's position you have to use <i>getInstance(int id, int value,int x, int y, int z)</i>
     * @param id the block's id
     * @param value it's value
     * @return the wanted block.
     */
    public static Block getInstance(int id, int value){
        return getInstance(id,value,null);
    }
    
    /**
     * Create a block through this factory method. If the block needs to know it's position you have to use this method and give the coordinates.
     * @param id the id of the block
     * @param value the value of the block, which is like a sub-id
     * @param coords the coordinates where the block is going to be places. If the block does not need this information it can be null.
     * @return the Block
     */
    public static Block getInstance(int id, int value, Coordinate coords){
        Block block;
        //define the default SideSprites
        switch (id){
            case 0: 
                    block = new Block(id);//air
                    block.setTransparent(true);
                    block.setHidden(true);
                    break;
            case 1: block = new Block(id); //grass
                    block.setObstacle(true);
                    break;
            case 2: block = new Block(id); //dirt
                    block.setObstacle(true);
                    break;
            case 3: block = new Block(id); 
                    block.setTransparent(false);
                    block.setObstacle(true);
                    break;
            case 4: block = new Block(id); 
                    block.setObstacle(true);
                    break;
            case 5: block = new Block(id); 
                    block.setObstacle(true);
                    break;
            case 6: block = new Block(id); 
                    block.setObstacle(true);
                    break;
            case 7: block = new Block(id); 
                    block.setObstacle(true);
                    break;
            case 8: block = new Block(id); //sand
                    block.setObstacle(true);
                    break;      
            case 9: block = new Block(id); //water
                    block.liquid = true;
                    block.setTransparent(true);
                    break;
            case 20: block = new Block(id);
                    block.setObstacle(true);
                    break;
            case 34: block = new Block(id); //flower
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 35: block = new Block(id); //bush
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;     
            case 40: block = new EntitySpawner(id, coords);
                    block.hasSides = true;
                    break;
            case 44: block = new Block(id); //textureless
                    block.hasSides = true;
                    block.setObstacle(true);
                    break;      
            case 70:block = new Block(id); 
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 71:block = new ExplosiveBarrel(id, coords);
                    block.hasSides = false;
                    break;
            case 72:block = new AnimatedBlock(id, new int[]{1000,1000},true, true);//animation lighting
                    block.setObstacle(true);
                    block.hasSides = true;
                    break;
            default:
                    block = new Block(id); 
                    block.setTransparent(true);
                    break; 
        }
        block.setValue(value);
        block.setClipped(true);
        return block;
    }  
    
    
     /**
     *  Returns a sprite sprite of a specific side of the block
     * @param id the id of the block
     * @param value the value of teh block
     * @param side Which side? (0 - 2)
     * @return an sprite of the side
     */
    public static AtlasRegion getBlockSprite(int id, int value, int side) {
        if (blocksprites[id][value][side] == null){ //load if not already loaded
            AtlasRegion sprite = getSpritesheet().findRegion(CATEGORY+Integer.toString(id)+"-"+value+"-"+side);
            if (sprite == null){ //if there is no sprite show the default "sprite not found sprite" for this category
                Gdx.app.log("debug", CATEGORY+Integer.toString(id)+"-"+value +"-"+ side +" not found");
                sprite = getSpritesheet().findRegion(CATEGORY+"0-0-"+side);
                if (sprite == null) {//load generic error sprite if category sprite failed
                    sprite = getSpritesheet().findRegion("error");
                    if (sprite == null) throw new NullPointerException("Sprite and category error not found and even the generic error sprite could not be found. Something with the sprites is fucked up.");
                }
            }
            blocksprites[id][value][side] = sprite;
            return sprite;
        } else {
            return blocksprites[id][value][side];
        }
    }
    

    
   /**
     * Returns a color representing the block. Picks from the sprite sprite.
     * @param id id of the Block
     * @param value the value of the block.
     * @return a color representing the block
     */
    public static Color getRepresentingColor(int id, int value){
        if (colorlist[id][value] == null){ //if not in list, add it to the list
            colorlist[id][value] = new Color();
            int colorInt;
            
            if (Block.getInstance(id,value, new Coordinate(0,0,0,false)).hasSides){    
                AtlasRegion texture = getBlockSprite(id, value, 1);
                if (texture == null) return new Color();
                colorInt = getPixmap().getPixel(
                    texture.getRegionX()+SCREEN_DEPTH2, texture.getRegionY()-SCREEN_DEPTH4);
            } else {
                AtlasRegion texture = getSprite(CATEGORY, id, value);
                if (texture == null) return new Color();
                colorInt = getPixmap().getPixel(
                    texture.getRegionX()+SCREEN_DEPTH2, texture.getRegionY()-SCREEN_DEPTH2);
            }
            Color.rgba8888ToColor(colorlist[id][value], colorInt);
            return colorlist[id][value]; 
        } else return colorlist[id][value]; //return value when in list
    }

    /**
     * Check if the block is liquid.
     * @return true if liquid, false if not 
     */
    public boolean isLiquid() {
        return liquid;
    } 
    
    /**
     * Is the block a true block with sides or represents it another thing like a flower?
     * @return 
     */
    public boolean hasSides() {
        return hasSides;
    } 
    
        /**
     * 
     * @param clipped When it is set to false, every side will also get clipped..
     */
    @Override
    public void setClipped(boolean clipped) {
        super.setClipped(clipped);
        if (clipped) {
            clippedLeft = true;
            clippedTop = true;
            clippedRight = true;
        }
    }
    
    /**
     * Make a side (in)clipping. If one side is clipping, the whole block is clipping.
     * @param side 0 = left, 1 = top, 2 = right
     * @param clipping true when it should be clipped.
     */
    public void setSideClipping(int side, boolean clipping) {
        if (!clipping) this.setClipped(false);
        
        if (side==0)
            clippedLeft = clipping;
        else if (side==1)
            clippedTop = clipping;
                else if (side==2)
                    clippedRight = clipping;
    }
    
       /**
     * Get the screen x-position where the object is rendered without regarding the camera.
     * @param coords  The relative coordinates where the object is rendered 
     * @return The screen X-position in pixels.
     */
   @Override
    public int get2DPosX(Coordinate coords) {
        return coords.get2DPosX();
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param coords The coordinates where the object is rendered 
     * @return The screen Y-position in pixels.
     */
   @Override
    public int get2DPosY(Coordinate coords) {
        return coords.get2DPosY();
    }


    
    @Override
    public void render(final View view, final WECamera camera, Coordinate coords) {
        if (!isClipped() && !isHidden()) {
            if (hasSides) {
                if (!clippedTop)
                    renderSide(view, camera, coords, Block.TOPSIDE);
                if (!clippedLeft)
                    renderSide(view, camera, coords, Block.LEFTSIDE);
                if (!clippedRight)
                    renderSide(view, camera, coords, Block.RIGHTSIDE);
            } else super.render(view, camera, coords);
        }
    }
    
    /**
     * Render the whole block at a custom position and checks for clipping and hidden.
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param xPos rendering position
     * @param yPos rendering position
     */
    @Override
    public void renderAt(final View view, final WECamera camera, int xPos, int yPos) {
        if (!isClipped() && !isHidden()) {
            if (hasSides) {
                if (!clippedTop)
                    renderSideAt(view, camera, xPos, yPos, Block.TOPSIDE);
                if (!clippedLeft)
                    renderSideAt(view, camera, xPos, yPos, Block.LEFTSIDE);
                if (!clippedRight)
                    renderSideAt(view, camera, xPos, yPos, Block.RIGHTSIDE);
                } else super.renderAt(view, camera, xPos, yPos);
        }
    }
     
    /**
     * Render a side of a block at the position of the coordinates.
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param coords the coordinates where the side is rendered 
     * @param sidenumb The number identifying the side. 0=left, 1=top, 2=right
     */
    public void renderSide(final View view, final WECamera camera, Coordinate coords, final int sidenumb){
        Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getColorOfSide(sidenumb);
        }
        renderSide(view, camera, coords, sidenumb, color);
    }

    /**
     * Render a side of a block at the position of the coordinates.
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param coords the coordinates where to render 
     * @param sidenumb The number identifying the side. 0=left, 1=top, 2=right
     * @param color a tint in which the sprite gets rendered
     */
    public void renderSide(final View view, final WECamera camera, Coordinate coords, final int sidenumb, Color color){
        int xPos = get2DPosX(coords) + ( sidenumb == 2 ? SCREEN_WIDTH2 : 0);//right side is  half a block more to the right
        int yPos = get2DPosY(coords) + (sidenumb != 1 ? SCREEN_WIDTH4 : 0);//the top is drawn a quarter blocks higher
        renderSideAt(view, camera, xPos, yPos, sidenumb, color);
    }
    
    /**
     * 
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param xPos rendering position
     * @param yPos rendering position
     * @param sidenumb The number identifying the side. 0=left, 1=top, 2=right
     */
    public void renderSideAt(final View view, final WECamera camera, int xPos, int yPos, final int sidenumb){
        Color color = Color.GRAY;
        if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getColorOfSide(sidenumb);
        }
        renderSideAt(view, camera, xPos, yPos, sidenumb, color);
    }
    /**
     * Draws a side of a block at a custom position. Apllies color before rendering and takes the lightlevel into account.
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param xPos rendering position
     * @param yPos rendering position
     * @param sidenumb The number identifying the side. 0=left, 1=top, 2=right
     * @param color a tint in which the sprite gets rendered
     */
    public void renderSideAt(final View view, final WECamera camera, int xPos, int yPos, final int sidenumb, Color color){
        Sprite sprite = new Sprite(getBlockSprite(getId(), getValue(), sidenumb));
        sprite.setPosition(xPos, yPos);
        
        //uncomment these two lines to add a depth-effect (note that it is very dark and still a prototype)
//        color.mul((camera.getBottomBorder()-coords.getRelY())
//            /
//            (float)(camera.getBottomBorder()-camera.getTopBorder())
//            );
        
        color.mul(getLightlevel()*2);
        
        prepareColor(view, color);
        
        sprite.getVertices()[SpriteBatch.C4] = color.toFloatBits();//top right
        
        //color.mul(getLightlevel()*2-((sidenumb == 2)?0.01f:0));
        //color.a = 1; 
        sprite.getVertices()[SpriteBatch.C1] = color.toFloatBits();//top left

        
//        if (sidenumb == 2)
//            color.mul(0.93f);
//        else if (sidenumb == 0)
//            color.mul(0.92f);
//        color.a = 1; 

        sprite.getVertices()[SpriteBatch.C2] = color.toFloatBits();//bottom left
        
//        if (sidenumb == 2)
//            color.mul(0.97f);
//        else if (sidenumb == 0) color.mul(1);
//        color.a = 1; 
        sprite.getVertices()[SpriteBatch.C3] = color.toFloatBits();//bottom right
 
        sprite.draw(view.BATCH);
    
    }

    @Override
    public void update(float delta) {
    }
    

    @Override
    public int getDepth(Coordinate coords){
        return (int) (
            coords.getRelY() *(Block.SCREEN_DEPTH+1)//Y
            + coords.getCellOffset()[1]
            + coords.getHeight()/Math.sqrt(2)//Z
            + coords.getCellOffset()[2]/Math.sqrt(2)
            + (getDimensionZ() - 1) *GAMEDIMENSION/Math.sqrt(2)
        );
    }

    /**
     *
     * @return
     */
    @Override
    public char getCategory() {
        return CATEGORY;
    }

    @Override
    public String getName() {
        return NAMELIST[getId()];
    }

    /**
     *Returning the
     * @return
     */
    @Override
    public int getOffsetX() {
        return OFFSET[getId()][getValue()][0];
    }

    /**
     *
     * @return
     */
    @Override
    public int getOffsetY() {
        return OFFSET[getId()][getValue()][1];
    } 

    /**
     *
     * @return
     */
    public static AtlasRegion[][][] getBlocksprites() {
        return blocksprites;
    }
}