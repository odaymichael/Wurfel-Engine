package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import com.BombingGames.EngineCore.View;
import static com.BombingGames.Game.Gameobjects.GameObject.OBJECTTYPESCOUNT;
import static com.BombingGames.Game.Gameobjects.GameObject.getPixmap;
import static com.BombingGames.Game.Gameobjects.GameObject.getSpritesheet;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * A Block is a wonderful piece of information and a geometrical object.
 * @author Benedikt Vogler
 */
public class Block extends GameObject {
    private static Color[][] colorlist = new Color[OBJECTTYPESCOUNT][9];
    /**The id of the left side of a block.*/
    public static final int LEFTSIDE=0;
    /**The id of the top side of a block.*/
    public static final int TOPSIDE=1;
    /**The id of the right side of a block.*/
    public static final int RIGHTSIDE=2;
    
    public static final char CATEGORY = 'b';
    
    /**Containts the names of the objects. index=id*/
    public static final String[] NAMELIST = new String[OBJECTTYPESCOUNT];
    
       /** A list containing the offset of the objects. */
    public static final int[][][] OFFSET = new int[OBJECTTYPESCOUNT][10][2];
    
    private boolean liquid, renderRight, renderTop, renderLeft;
    private boolean hasSides = true;
    
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
                    block.setHidden(false);
                    break; 
        }
        block.setValue(value);
        return block;
    }  
    
    
     /**
     *  Returns a sprite sprite of a specific side of the block
     * @param id the id of the block
     * @param value the value of teh block
     * @param side Which side? (0 - 2)
     * @return an sprite of the side
     */
    public static TextureAtlas.AtlasRegion getBlockSprite(int id, int value, int side) {
        AtlasRegion sprite = getSpritesheet().findRegion(CATEGORY+Integer.toString(id)+"-"+value+"-"+side);
        if (sprite == null){ //if there is no sprite show the default "sprite not found sprite"
            sprite = getSpritesheet().findRegion(CATEGORY+"0-0-"+side);
            if (sprite==null)
                throw new NullPointerException("Sprite not found but even the default error sprite could not be found:"+CATEGORY+"0-0-"+side);
        }
        return sprite;
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
                    texture.getRegionX()+DIM2, texture.getRegionY()-DIM4);
            } else {
                AtlasRegion texture = getSprite(CATEGORY, id, value);
                if (texture == null) return new Color();
                colorInt = getPixmap().getPixel(
                    texture.getRegionX()+DIM2, texture.getRegionY()-DIM2);
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
     * Make a side (in)visible. If one side is visible, the whole block is visible.
     * @param side 0 = left, 1 = top, 2 = right
     * @param visible The value
     */
    public void setSideVisibility(int side, boolean visible) {
        if (visible) this.setVisible(true);
        
        if (side==0)
            renderLeft = visible;
        else if (side==1)
            renderTop = visible;
                else if (side==2)
                    renderRight = visible;
    }
    
       /**
     * Get the screen x-position where the object is rendered without regarding the camera.
     * @param coords  The relative coordinates where the object is rendered 
     * @return The screen X-position in pixels.
     */
   @Override
    public int get2DPosX(Coordinate coords) {
        return coords.get2DPosX() + (int) (coords.getCellOffset()[0]); //add the objects position inside this coordinate
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param coords The coordinates where the object is rendered 
     * @return The screen Y-position in pixels.
     */
   @Override
    public int get2DPosY(Coordinate coords) {
        return coords.get2DPosY()
               + (int) (coords.getCellOffset()[1] / 2) //add the objects position inside this coordinate
               - (int) (coords.getCellOffset()[2] / Math.sqrt(2)); //add the objects position inside this coordinate
    }

    /**
     * 
     * @param visible When it is set to false, every side will also get hidden.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            renderLeft = false;
            renderTop = false;
            renderRight = false;
        }
    }
    
    @Override
    public void render(View view, WECamera camera, Coordinate coords) {
        if (isVisible() && !isHidden()) {
            Color color = Color.GRAY;
            if (Controller.getLightengine() != null){
                color = Controller.getLightengine().getGlobalLight();
            }
            if (Controller.getLightengine() != null){
                if (hasSides) {
                    if (renderTop)
                        renderSide(view, camera, coords, Block.TOPSIDE, Controller.getLightengine().getColorOfSide(Block.TOPSIDE));
                    if (renderLeft)
                        renderSide(view, camera, coords, Block.LEFTSIDE, Controller.getLightengine().getColorOfSide(Block.LEFTSIDE));
                    if (renderRight)
                        renderSide(view, camera, coords, Block.RIGHTSIDE, Controller.getLightengine().getColorOfSide(Block.RIGHTSIDE));
                } else super.render(view, camera, coords, color.mul(getLightlevel()));
            } else 
                if (hasSides){
                    if (renderTop)
                        renderSide(view, camera, coords, Block.TOPSIDE, color);
                    if (renderLeft)
                        renderSide(view, camera, coords, Block.LEFTSIDE, color);
                    if (renderRight)
                        renderSide(view, camera, coords, Block.RIGHTSIDE, color);
                } else super.render(view, camera, coords);
        }
    }
    

    /**
     * Draws a side of a block
     * @param view the view using this render method
     * @param camera The camera rendering the scene
     * @param coords the coordinates where to render 
     * @param sidenumb The number of the side. 0 =  left, 1=top, 2= right
     * @param color  a tint in which the sprite get's rendered
     */
    protected void renderSide(final View view, WECamera camera, Coordinate coords, final int sidenumb, Color color){
        Sprite sprite = new Sprite(getBlockSprite(getId(), getValue(), sidenumb));
        
        int xPos = get2DPosX(coords) + ( sidenumb == 2 ? DIM2 : 0);//right side is  half a block more to the right
        int yPos = get2DPosY(coords) + (sidenumb != 1 ? DIM4 : 0);//the top is drawn a quarter blocks higher
        sprite.setPosition(xPos, yPos);
        
        //brightness += getLightlevel()-0.5f;
            
            
        //uncomment these two lines to add a depth-effect (note that it is very dark)
//        filter.mul((coords.getRelY()-camera.getTopBorder())/
//            (camera.getBottomBorder()-camera.getTopBorder()));
//        filter.g *= (coords.getRelY()-camera.getTopBorder())
//           /(camera.getBottomBorder()-camera.getTopBorder());
        
        color.mul(getLightlevel()*2);
        //Color verticeColor = color;
        color.a = 1; 
        
        prepareColor(view, color);
        sprite.getVertices()[SpriteBatch.C4] = color.toFloatBits();
        sprite.getVertices()[SpriteBatch.C1] = color.toFloatBits();

        color.mul(getLightlevel()*2-((sidenumb != 1)?0.3f:0));
        color.a = 1; 

        sprite.getVertices()[SpriteBatch.C2] = color.toFloatBits();
        sprite.getVertices()[SpriteBatch.C3] = color.toFloatBits();
 
        sprite.draw(view.getBatch());
    }

    @Override
    public void update(float delta) {
    }
    

    @Override
    public int getDepth(Coordinate coords){
        return (int) (
            coords.getRelY() *(Block.DIM4+1)//Y
            + coords.getCellOffset()[1]
            + coords.getZ()*Block.DIM4//Z
            + coords.getCellOffset()[2] / Math.sqrt(2) /2
            + (getDimensionY() - 1) * DIM4
        );
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
}