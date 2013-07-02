package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Coordinate;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;
import com.BombingGames.Game.Lighting.LightEngine;
import com.BombingGames.Game.View;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

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
     * @param absCoords the absolute coordinates where the bloks should be created. Only SelfAware Blocks should use this.
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
            case 9: block = new Block(id); //water
                    block.setTransparent(true);
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
    public static Image getBlockSprite(int id, int value, int side) {
        return getSpritesheet().getSprite(id+"-"+value+"-"+side);
    }
    
   /**
     * Returns a color representing the block. Picks from the sprite image.
     * @param id id of the Block
     * @param value the value of the block.
     * @return a color representing the block
     */
    public static Color getRepresentingColor(int id, int value){
        if (colorlist[id][value] == null){ //if not in list, add it to the list
            if (Block.getInstance(id,0,new Coordinate(0,0,0,false)).hasSides)
                colorlist[id][value] = getBlockSprite(id, value, 1).getColor(DIM2, DIM4);
            else
                colorlist[id][value] = getSprite(id, value).getColor(DIM2, DIM2);
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
     * @param object The object of wich you want the position
     * @param coords  The relative coordinates where the object is rendered 
     * @return The screen X-position in pixels.
     */
   @Override
    public int getScreenPosX(Coordinate coords) {
        return coords.getRelX() * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (coords.getRelY() % 2) * DIM2 //y-coordinate multiplied by it's dimension in this direction
               + (int) (coords.getCellOffset()[0]); //add the objects position inside this coordinate
    }

    /**
     * Get the screen y-position where the object is rendered without regarding the camera.
     * @param object The object of wich you want the position
     * @param coords The coordinates where the object is rendered 
     * @return The screen Y-position in pixels.
     */
   @Override
    public int getScreenPosY(Coordinate coords) {
        return coords.getRelY() * DIM4 //x-coordinate * the tile's size
               + (int) (coords.getCellOffset()[1] / 2) //add the objects position inside this coordinate
               - (int) (coords.getCellOffset()[2] / Math.sqrt(2)) //add the objects position inside this coordinate
               - (int) (coords.getHeight() / Math.sqrt(2)); //take axis shortening into account
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
    public void render(Graphics g, View view, Coordinate coords) {
        if (!isHidden() && isVisible()) {
            
            if (Controller.lightEngine != null){
                if (hasSides) {
                    if (renderTop)
                        renderSide(g, view, coords, Block.TOPSIDE, LightEngine.getBrightness(Block.TOPSIDE));
                    if (renderLeft)
                        renderSide(g, view, coords, Block.LEFTSIDE, LightEngine.getBrightness(Block.LEFTSIDE));
                    if (renderRight)
                        renderSide(g, view, coords, Block.RIGHTSIDE, LightEngine.getBrightness(Block.RIGHTSIDE));
                } else super.render(g, view, coords, LightEngine.getBrightness());
            } else 
                if (hasSides){
                    if (renderTop)
                        renderSide(g, view, coords, Block.TOPSIDE, getLightlevel());
                    if (renderLeft)
                        renderSide(g, view, coords, Block.LEFTSIDE, getLightlevel());
                    if (renderRight)
                        renderSide(g, view, coords, Block.RIGHTSIDE, getLightlevel());
                } else super.render(g, view, coords, getLightlevel());
        }
    }


    
    /**
     * Draws a side of a block
     * @param coords the coordinates where to render 
     * @param sidenumb The number of the side. 0 =  left, 1=top, 2= right
     */
    protected void renderSide(Graphics g, View view, Coordinate coords, int sidenumb, int brightness){
        Image image = getBlockSprite(getId(), getValue(), sidenumb);
        
        //right side is  half a block more to the right
        int xPos = getScreenPosX(coords) + ( sidenumb == 2 ? DIM2 : 0);
        
        //the top is drawn a quarter blocks higher
        int yPos = getScreenPosY(coords) + (sidenumb != 1 ? DIM4 : 0);
        
        brightness -= 127-getLightlevel();
            
        Color filter;
        if (brightness <= 127){
            view.setDrawmode(GL11.GL_MODULATE);
            filter = new Color(brightness/127f, brightness/127f, brightness/127f);
        } else {
            view.setDrawmode(GL11.GL_ADD);
            filter = new Color((brightness-127)/127f, (brightness-127)/127f, (brightness-127)/127f);
        }
            
        if (Controller.lightEngine != null){
            filter = filter.multiply(Controller.lightEngine.getLightColor());
        } else {
            //calc  verticalGradient
            float verticalGradient = getLightlevel();

            image.setColor(0, verticalGradient,verticalGradient,verticalGradient);
            image.setColor(1, verticalGradient,verticalGradient, verticalGradient);

            if (sidenumb != 1) verticalGradient -= .3f;

            image.setColor(2, verticalGradient, verticalGradient, verticalGradient);
            image.setColor(3, verticalGradient, verticalGradient, verticalGradient);
        }
        
        image.drawEmbedded(xPos, yPos, xPos+image.getWidth(), yPos+image.getHeight(), 0, 0, image.getWidth(), image.getHeight(), filter);
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public int getDepth(Coordinate coords){
        return (int) (
            DIMENSION * coords.getRelY()
            + (coords.getRelY() % 2) * DIM2
            + coords.getCellOffset()[1]
            + coords.getHeight()
            + (getDimensionY() - 1) * DIM4
        );
    }
}