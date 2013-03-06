package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Camera;

/**
 * A Block is a wonderful piece of information and a geometrical object.
 * @author Benedikt
 */
public class Block extends Object {

    public static final int LEFTSIDE=0;
    public static final int TOPSIDE=1;
    public static final int RIGHTSIDE=2;
    
    private boolean liquid, renderRight, renderTop, renderLeft;
    private boolean hasSides = true;
   
    /**
     *Don't use this constructor to get a new block. Use the static <i>getInstance</i> methods instead.
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance() 
     */
    protected Block(){
        super(0);
    }
    
    protected Block(int id){
        super(id);
    }
    
    protected Block(int id, int value){
        super(id, value);
    }
    
   
    
    /**
     * Creates an air block.
     * @return a block of air.
     */
    public static Block getInstance(){
        return getInstance(0,0,0,0,0);
    }
    
    /**
     *  Create a block. If the block needs to know it's position you have to use <i>getInstance(int id, int value,int x, int y, int z)</i>
     * @param id the block's id
     * @return the wanted block.
     */
    public static Block getInstance(int id){
        return getInstance(id,0,0,0,0);
    }
    
    /**
     * Create a block. If the block needs to know it's position you have to use <i>getInstance(int id, int value,int x, int y, int z)</i>
     * @param id the block's id
     * @param value it's value
     * @return the wanted block.
     */
    public static Block getInstance(int id, int value){
        return getInstance(id,value,0,0,0);
    }
    
    /**
     * Create a block. If the block needs to know it's position you have to use this method and give the coordinates.
     * @param id the id of the block
     * @param value the value of the block, which is like a sub-id
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return the Block
     */
    public static Block getInstance(int id, int value,int x, int y, int z){
        Block block = null;
        //define the default SideSprites
        switch (id){
            case 0: 
                    block = new Block(0);//air
                    block.setTransparent(true);
                    block.setHidden(true);
                    break;
            case 1: block = new Block(1); //grass
                    block.setObstacle(true);
                    break;
            case 2: block = new Block(2); //dirt
                    block.setObstacle(true);
                    break;
            case 3: block = new Block(3); 
                    block.setTransparent(false);
                    block.setObstacle(true);
                    break;
            case 4: block = new Block(4); 
                    block.setObstacle(true);
                    break;
            case 5: block = new Block(5); 
                    block.setObstacle(true);
                    break;
            case 6: block = new Block(6); 
                    block.setObstacle(true);
                    break;
            case 7: block = new Block(7); 
                    block.setObstacle(true);
                    break;
            case 8: block = new Block(8); //sand
                    block.setObstacle(true);
                    break;      
            case 9: block = new Block(9); //water
                    block.setTransparent(true);
                    block.liquid = true;
                    break;    
            case 20:block = new Block(20); 
                    block.setObstacle(true);
                    break;
            case 40://already reserverd
                    break;
            case 70:block = new Block(70); 
                    block.setTransparent(true);
                    block.hasSides = false;
                    break;
            case 71:block = new ExplosiveBarrel(71);
                    ((ExplosiveBarrel) block).setAbsCoords(new int[]{x,y,z});
                    block.setObstacle(true);
                    block.hasSides = false;
                    break;
            case 72:block = new AnimatedBlock(new int[]{1000,1000},true, true);//animation test
                    block.setObstacle(true);
                    block.hasSides = true;
                    break;
            default:
                    block = new Block(0); 
                    block.setTransparent(true);
                    block.setHidden(true);
                    break; 
        }
        block.setValue(value);
        return block;
    } 
    
 
    /**
     * Get the coordinates correct coordiantes when you have coordiantes and a position laying outside this field.
     * @param coords 
     *  @param xpos The x-position inside/outside this field
     * @param ypos The y-position inside/outside this field 
     * @return The neighbour block or itself
     */
    public static int[] posToNeighbourCoords (int[] coords, int xpos, int ypos){
        return sideNumbToNeighbourCoords(coords, sideNumb(xpos, ypos));
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
    public void render(int[] coords, Camera camera) {
        if (!isHidden() && isVisible()) {
            if (hasSides) {
                    if (renderTop) {
                        renderSide(coords, Block.TOPSIDE, camera);
                    }
                    if (renderLeft) {
                        renderSide(coords, Block.LEFTSIDE, camera);
                    }
                    if (renderRight) {
                        renderSide(coords, Block.RIGHTSIDE, camera);
                    }
                } else super.render(coords, camera);
            }
    }
}