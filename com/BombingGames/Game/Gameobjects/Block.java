package com.BombingGames.Game.Gameobjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/**
 * A Block is a wonderful piece of information and a geometrical object.
 * @author Benedikt
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
    public static Block getInstance(int id, int value, int[] absCoords){
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
            case 34: block = new Block(id); //water
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 35: block = new Block(id); //water
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;       
            case 40://already reserverd
                    break;
            case 70:block = new Block(id); 
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 71:block = new ExplosiveBarrel(id,absCoords);
                    block.setObstacle(true);
                    block.hasSides = false;
                    break;
            case 72:block = new AnimatedBlock(id, new int[]{1000,1000},true, true);//animation test
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
     * @param id
     * @param side Which side? (0 - 2)
     * @param value
     * @return an image of the side
     */
    public static Image getBlockSprite(int id, int value, int side) {
        if (side == 1) {
            return getSpritesheet().getSubImage(SPRITEPOS[id][value][side][0], SPRITEPOS[id][value][side][1], DIMENSION, DIM2);
        } else {
            return getSpritesheet().getSubImage(SPRITEPOS[id][value][side][0], SPRITEPOS[id][value][side][1], DIM2, (int) (DIM2 * 3 / 2));
        }
    }
    
        /**
     * Returns a color representing the block. Picks from the sprite image.
     * @param id id of the Block
     * @param value the value of the block.
     * @return a color representing the block
     */
    public static Color getRepresentingColor(int id, int value){
        if (colorlist[id][value] == null){
            colorlist[id][value] = getBlockSprite(id, value,1).getColor(DIM2, DIM4);
            return colorlist[id][value]; 
        } else return colorlist[id][value];
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
     * The block hides the pst block when it has sides and is not transparent (like normal block)
     * @return true when hiding the past Block
     */
    public boolean hidingPastBlock(){
        return (hasSides && ! isTransparent());
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
    public void render(int[] coords) {
        if (!isHidden() && isVisible()) {
            if (hasSides) {
                    if (renderTop) {
                        renderSide(coords, Block.TOPSIDE);
                    }
                    if (renderLeft) {
                        renderSide(coords, Block.LEFTSIDE);
                    }
                    if (renderRight) {
                        renderSide(coords, Block.RIGHTSIDE);
                    }
                } else super.render(coords);
            }
    }
    
    /**
     * Draws a side of a block
     * @param coords the coordinates where to render 
     * @param sidenumb The number of the side. 0 =  left, 1=top, 2= right
     */
    protected void renderSide(int[] coords, int sidenumb){
        Image image = getBlockSprite(getId(), getValue(), sidenumb);
        
//        if (Gameplay.getView().hasGoodGraphics()){
//                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MULT);
//        
//            if (sidenumb == 0){
//                int brightness = getLightlevel() * 255 / 100;
//                new Color(brightness,brightness,brightness).bind();
//            } else {
//                Color.black.bind();
//            }
        //}
        
        //calc  brightness
        float brightness = getLightlevel() / 50f;
                
        image.setColor(0, brightness,brightness,brightness);
        image.setColor(1, brightness,brightness, brightness);

        if (sidenumb != 1) brightness -= .3f;

        image.setColor(2, brightness, brightness, brightness);
        image.setColor(3, brightness, brightness, brightness);
        
        //right side is  half a block more to the right
        int xpos = getScreenPosX(this, coords) + ( sidenumb == 2 ? DIM2 : 0);
        
        //the top is drawn a quarter blocks higher
        int ypos = getScreenPosY(this, coords) + (sidenumb != 1 ? DIM4 : 0);
        
        image.drawEmbedded(xpos, ypos);
    }

    @Override
    public void update(int delta) {
    }
}