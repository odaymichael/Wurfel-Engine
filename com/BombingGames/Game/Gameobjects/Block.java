package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Map.Chunk;
import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Map.Map;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import com.BombingGames.Game.Lighting.LightEngine;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import static com.BombingGames.Game.Gameobjects.GameObject.getPixmap;
import static com.BombingGames.Game.Gameobjects.GameObject.getSprite;
import static com.BombingGames.Game.Gameobjects.GameObject.getSpritesheet;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import org.lwjgl.opengl.GL11;

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
    
    private boolean liquid, renderRight, renderTop, renderLeft;
    private boolean hasSides = true;
    
    /**
     * Don't use this constructor to get a new block. Use the static <i>getInstance</i> methods instead.
     * @param id
     *  @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected Block(int id){
        super(id);
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
     * Create a block. If the block needs to know it's position you have to use this method and give the coordinates.
     * @param id the id of the block
     * @param value the value of the block, which is like a sub-id
     * @param coord 
     * @return the Block
     */
    public static Block getInstance(int id, int value, Coordinate coord){
        Block block = null;
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
            case 9: block = new Sea(id, coord); //water
                    block.liquid = true;
                    break;
            case 20: block = new Block(id);
                    block.setObstacle(true);
                    break;
            case 34: block = new Block(id); //water
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 35: block = new Block(id); //bush
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;       
            case 40://already reserverd
                    break;
            case 41://already reserverd
                    break;    
            case 70:block = new Block(id); 
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 71:block = new ExplosiveBarrel(id, coord);
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
     *  Returns a sprite image of a specific side of the block
     * @param id the id of the block
     * @param value the value of teh block
     * @param side Which side? (0 - 2)
     * @return an image of the side
     */
    public static TextureAtlas.AtlasRegion getBlockSprite(int id, int value, int side) {
        AtlasRegion sprite = getSpritesheet().findRegion(id+"-"+value+"-"+side);
        if (sprite == null)
            return getSpritesheet().findRegion(0+"-"+0+"-"+side);
            else return sprite;
    }
    
   /**
     * Returns a color representing the block. Picks from the sprite image.
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
                    (int) (texture.getRegionX()+DIM2),
                    (int) (texture.getRegionY()-DIM4)
                );
            } else {
                AtlasRegion texture = getSprite(id, value);
                if (texture == null) return new Color();
                colorInt = getPixmap().getPixel(
                    (int) (texture.getRegionX()+DIM2),
                    (int) (texture.getRegionY()-DIM2)
                );
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
        if (!isHidden() && isVisible()) {
            
            if (Controller.getLightengine() != null){
                if (hasSides) {
                    if (renderTop)
                        renderSide(view, camera, coords, Block.TOPSIDE, LightEngine.getBrightness(Block.TOPSIDE));
                    if (renderLeft)
                        renderSide(view, camera, coords, Block.LEFTSIDE, LightEngine.getBrightness(Block.LEFTSIDE));
                    if (renderRight)
                        renderSide(view, camera, coords, Block.RIGHTSIDE, LightEngine.getBrightness(Block.RIGHTSIDE));
                } else super.render(view, camera, coords, LightEngine.getBrightness());
            } else 
                if (hasSides){
                    if (renderTop)
                        renderSide(view, camera, coords, Block.TOPSIDE, getLightlevel());
                    if (renderLeft)
                        renderSide(view, camera, coords, Block.LEFTSIDE, getLightlevel());
                    if (renderRight)
                        renderSide(view, camera, coords, Block.RIGHTSIDE, getLightlevel());
                } else super.render(view, camera, coords, getLightlevel());
        }
    }


    
    /**
     * Draws a side of a block
     * @param g The graphics context where the side is rendered at.
     * @param view the view using this render method
     * @param coords the coordinates where to render 
     * @param sidenumb The number of the side. 0 =  left, 1=top, 2= right
     * @param brightness  The brightness of the side. Value between 0  and 255.
     */
    protected void renderSide(final View view, WECamera camera, Coordinate coords, final int sidenumb, int brightness){
        Sprite image = new Sprite(getBlockSprite(getId(), getValue(), sidenumb));
        
        //right side is  half a block more to the right
        int xPos = get2DPosX(coords) + ( sidenumb == 2 ? DIM2 : 0);
        
        //the top is drawn a quarter blocks higher
        int yPos = get2DPosY(coords) + (sidenumb != 1 ? DIM4 : 0);
        
        brightness -= 127-getLightlevel();
            
        Color filter;
        if (brightness <= 127){
            view.setDrawmode(GL11.GL_MODULATE);
            filter = new Color(brightness/127f, brightness/127f, brightness/127f, 1);
        } else {
            view.setDrawmode(GL11.GL_ADD);
            filter = new Color((brightness-127)/127f, (brightness-127)/127f, (brightness-127)/127f, 1);
        }
            
        if (Controller.getLightengine() != null){
            //uncomment these two lines to add a depth-effect (note that it is very dark)
            filter = filter.mul(Controller.getLightengine().getLightColor());
        }
        //filter.mul((coords.getRelY()-camera.getTopBorder())/
           // (camera.getBottomBorder()-camera.getTopBorder()));
        //filter.g *= (coords.getRelY()-camera.getTopBorder())
        //   /(camera.getBottomBorder()-camera.getTopBorder());
        //calc  verticalGradient
        float verticalGradient = getLightlevel()/100f;
        
        Color verticeColor = filter.cpy().mul(verticalGradient);
        verticeColor.a = 1; 
        image.getVertices()[SpriteBatch.C4] = verticeColor.toFloatBits();
        image.getVertices()[SpriteBatch.C1] = verticeColor.toFloatBits();

        if (sidenumb != 1) verticalGradient -= .3f;
        
        verticeColor = filter.cpy().mul(verticalGradient);
        verticeColor.a = 1; 

        image.getVertices()[SpriteBatch.C2] = verticeColor.cpy().toFloatBits();
        image.getVertices()[SpriteBatch.C3] = verticeColor.cpy().toFloatBits();
        
        image.setPosition(xPos, yPos);
        image.draw(view.getBatch());
    }

    @Override
    public void update(int delta) {
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
}