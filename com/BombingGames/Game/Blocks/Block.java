package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Camera;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Gameplay;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * A Block is a wonderfull piece of information and a geometrical object.
 * @author Benedikt
 */
public class Block {
    /**
       * Screen DIMENSION of the Block in pixels. This is the length of the side, when you cut the block in the middle
       */
    public static final int DIMENSION = 160;
    
    /**
     * The half of DIMENSION.
     */
    public static final int DIM2 = DIMENSION/2;
    
    /**
     * A quarter of DIMENSION.
     */
    public static final int DIM4 = DIMENSION/4;

    
    /**
     * The real game world dimension in pixel It is Dimension/sqrt(2)
     */
    public static int GAMEDIMENSION = (int) (DIMENSION/Math.sqrt(2));

    
    /**
     * Has the positions of the sprites for rendering with sides
     * 1. Dimension id
     * 2. Dimension: value
     * 3. Dimension: Side
     * 4. Dimension: X- or Y-coordinate
     */
    public static final int[][][][] SPRITEPOS = new int[99][9][3][2];
    
    private static Color[][] colorlist = new Color[99][9];
    
    /**
     * The sprite image which contains every block image
     */
    private static Image spritesheet;
    
    private final int id;
    private final String name;    
    private int value;
    private float[] pos = {Block.DIM2, Block.DIM2, 0};
    private boolean obstacle, transparent, visible, renderRight, renderTop, renderLeft, invisible, liquid; 
    private boolean isBlock = true;
    private int lightlevel = 50;
    private int dimensionY = 1;    
    
    static {
        //grass
        SPRITEPOS[1][0][0][0] = 0;
        SPRITEPOS[1][0][0][1] = 0;
        SPRITEPOS[1][0][1][0] = 80;
        SPRITEPOS[1][0][1][1] = 0;
        SPRITEPOS[1][0][2][0] = 240;
        SPRITEPOS[1][0][2][1] = 0;
        
        //dirt
        SPRITEPOS[2][0][0][0] = 0;
        SPRITEPOS[2][0][0][1] = 0;
        SPRITEPOS[2][0][1][0] = 400;
        SPRITEPOS[2][0][1][1] = 0;
        SPRITEPOS[2][0][2][0] = 560;
        SPRITEPOS[2][0][2][1] = 0;
        
        //stone
        SPRITEPOS[3][0][0][0] = 0;
        SPRITEPOS[3][0][0][1] = 120;
        SPRITEPOS[3][0][1][0] = 80;
        SPRITEPOS[3][0][1][1] = 120;
        SPRITEPOS[3][0][2][0] = 240;
        SPRITEPOS[3][0][2][1] = 120;
        
        //asphalt
        SPRITEPOS[4][0][0][0] = 320;
        SPRITEPOS[4][0][0][1] = 120;
        SPRITEPOS[4][0][1][0] = 400;
        SPRITEPOS[4][0][1][1] = 120;
        SPRITEPOS[4][0][2][0] = 560;
        SPRITEPOS[4][0][2][1] = 120;
       
        //cobblestone
        SPRITEPOS[5][0][0][0] = 0;
        SPRITEPOS[5][0][0][1] = 600;
        SPRITEPOS[5][0][1][0] = 80;
        SPRITEPOS[5][0][1][1] = 600;
        SPRITEPOS[5][0][2][0] = 240;
        SPRITEPOS[5][0][2][1] = 600;

        
        //pavement
        SPRITEPOS[6][0][0][0] = 240;
        SPRITEPOS[6][0][0][1] = 600;
        SPRITEPOS[6][0][1][0] = 320;
        SPRITEPOS[6][0][1][1] = 600;
        SPRITEPOS[6][0][2][0] = 560;
        SPRITEPOS[6][0][2][1] = 600;
       
        //concrete
        SPRITEPOS[7][0][0][0] = 640;
        SPRITEPOS[7][0][0][1] = 600;
        SPRITEPOS[7][0][1][0] = 720;
        SPRITEPOS[7][0][1][1] = 600;
        SPRITEPOS[7][0][2][0] = 880;
        SPRITEPOS[7][0][2][1] = 600;
        
        //sand
        SPRITEPOS[8][0][0][0] = 160;
        SPRITEPOS[8][0][0][1] = 720;
        SPRITEPOS[8][0][1][0] = 240;
        SPRITEPOS[8][0][1][1] = 720;
        SPRITEPOS[8][0][2][0] = 400;
        SPRITEPOS[8][0][2][1] = 720;
        
        //water
        SPRITEPOS[9][0][0][0] = 480;
        SPRITEPOS[9][0][0][1] = 720;
        SPRITEPOS[9][0][1][0] = 560;
        SPRITEPOS[9][0][1][1] = 720;
        SPRITEPOS[9][0][2][0] = 720;
        SPRITEPOS[9][0][2][1] = 720;
        
        
        //player
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
        
        //fire
        SPRITEPOS[70][0][0][0] = 0;
        SPRITEPOS[70][0][0][1] = 720;
        
        try {
            spritesheet = new SpriteSheet("com/BombingGames/Game/Blockimages/SideSprite.png", DIMENSION, (int) (DIM2*1.5f));
        } catch (SlickException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a block of air. 
     */ 
    public Block(){
        this(0,0);
    }
    
    /**
     * Creates a block (id) with value 0. 
     *  @param id 
     */ 
    public Block(int id){
        this(id,0);
    }    
    
    /**
     * Creates a block (id) with value (value).
     * @param id the id of the block
     * @param value the sub-id called  value
     */    
    public Block(int id, int value){
        this.id = id;
        this.value = value;
        
        //define the default SideSprites
        switch (id){
            case 0: name = "air";
                    transparent = true;
                    obstacle = false;
                    invisible = true;
                    break;
            case 1: name = "gras";
                    transparent = false;
                    obstacle = true;
                    break;
            case 2: name = "dirt";
                    transparent = false;
                    obstacle = true;
                    break;
            case 3: name = "stone";
                    transparent = false;
                    obstacle = true;
                    break;
            case 4: name = "asphalt";
                    transparent = false;
                    obstacle = true;
                    break;
            case 5: name = "cobblestone";
                    transparent = false;
                    obstacle = true;
                    break;
            case 6: name = "pavement";
                    transparent = false;
                    obstacle = true;
                    break;
            case 7: name = "concrete";
                    transparent = false;
                    obstacle = true;
                    break;
            case 8: name = "sand";
                    transparent = false;
                    obstacle = true;
                    break;      
            case 9: name = "water";
                    transparent = true;
                    obstacle = false;
                    liquid=true;
                    break;    
            case 20:name = "red brick wall";
                    transparent = false;
                    obstacle = true;
                    break;
            case 40:name = "player";
                    transparent = true;
                    obstacle = true;
                    isBlock = false;
                    dimensionY=2;
                    if (value==0)
                        invisible = true;
                    break;
            case 50:name = "strewbed";
                    transparent = true;
                    obstacle = false;
                    break;
            case 70:name = "campfire";
                    transparent = true;
                    obstacle = false;
                    isBlock = false;
                    break;
            default:name = "undefined";
                    transparent = true;
                    obstacle = true;
                    break; 
        }
    }
    
      /**
     * Returns the spritesheet used for rendering
     * @return the spritesheet used by the blocks
     */
    public static Image getBlocksheet() {
        return spritesheet;
    }
    
   /**
     * Returns a sprite image of non-block image
     * @param id
     * @param value
     * @param dimY 
     * @return 
     */    
    public static Image getSprite(int id, int value, int dimY) {
        return spritesheet.getSubImage(SPRITEPOS[id][value][0][0], SPRITEPOS[id][value][0][1], DIMENSION, dimY*DIM2+DIM2);   
    }
        
    /**
     *  Returns a sprite image of a specific side of the block
     * @param id 
     * @param side Which side? (0 - 2)
     * @param value 
     * @return an image of the side
     */
    public static Image getBlockSprite(int id, int value, int side){
        if (side == 1)
            return spritesheet.getSubImage(SPRITEPOS[id][value][side][0], SPRITEPOS[id][value][side][1], DIMENSION, DIM2);
        else
            return spritesheet.getSubImage(SPRITEPOS[id][value][side][0], SPRITEPOS[id][value][side][1], DIM2, (int) (DIM2*3/2));    
    }
    
   /**
     * Get the screen X-position where to block is rendered
     * @param x block coord
     * @param y block coord
     * @param z block coord
     * @param camera the camera which renders the scene
     * @return the screen X-position in pixels
     */
    public static int getScreenPosX(int x, int y, int z, Camera camera){
        return -camera.getX()
               + x*DIMENSION
               + (y%2) * DIM2
               + (int) (Controller.getMapData(x, y, z).getPos(0));
    }
    
    /**
     * Get the screen Y-position where to block is rendered
     * @param x block coord
     * @param y block coord
     * @param z block coord
     * @param camera the camera which renders the scene
     * @return the screen Y-position in pixels
     */
    public static int getScreenPosY(int x, int y, int z, Camera camera){
        return - camera.getY()
            + y*DIM4
            - z*DIM2
            + (int) (Controller.getMapData(x, y, z).getPos(1)/2)
            - (int) (Controller.getMapData(x, y, z).getPos(2)/Math.sqrt(2));   
    }
    
    
   /**
     * Returns a color representing the block. Picks from the sprite image.
     * @param id id of the Block
     * @param value the value of the block.
     * @return a color representing the block
     */
    public static Color getBlockColor(int id, int value){
        if (colorlist[id][value] == null){
            colorlist[id][value] = getBlockSprite(id, value,1).getColor(DIM2, DIM4);
            return colorlist[id][value]; 
        } else return colorlist[id][value];
    }
        
    

    //getter & setter
    
   /**
     * returns the id of a block
     * @return getId
     */
    public int getId(){
        return this.id;
    }

    /**
     *  Gets the positon of the block. Coordinate system starting at bottom rear.
     * @return an array with three fields. [x,y,z]
     */
    public float[] getPos() {
        return pos;
    }
    
    /**
     *  Get a coordinate value. Coordinate system starting at bottom rear.
     * @param i the index of the field,  0=>x, y=>1, z=>2
     * @return the position
     */
    public float getPos(int i) {
        return pos[i];
    }

    /**
     * Set a whole new array containing the positon of the block. Coordinate system starting at bottom rear.
     * @param pos the new positon array
     */
    public void setPos(float[] pos) {
        this.pos = pos;
    }
    
    /**
     * Sets the position of the block. Coordinate system starting at bottom rear.
     * @param i Select the field you want to write 0=>x, y=>1, z=>2
     * @param value the value you want to set it to.
     */
    public void setPos(int i, float value) {
        pos[i]= value;
    }
   
    
   /**
     * Has the block an offset (pos vector)? 
     * @return when it has offset true, else false
     */
    public boolean hasOffset() {
        return (pos[0] != 0 || pos[1] != 0 || pos[2] != 0);
    }
    
    

    /**
     * Hide this block and prevent it from beeing rendered.
     * @param visible Sets the visibility. When it is false, every side will also get invisible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!this.visible){
            renderLeft = false;
            renderTop = false;
            renderRight = false;
        }
    }
    
    /**
     * Is the Block visible?
     * @return
     */
    public boolean isVisible(){
        return visible;
    }
    
    /**
     * Is this Block an obstacle or can you pass through?
     * @return
     */
    public boolean isObstacle() {
        return obstacle;
    }

    /**
     * Make the block to an obstacle
     * @param obstacle
     */
    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }
    
    /**
     * Can light travel through block?
     * @return
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Make the Block transparent
     * @param transparent
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    /**
     * Get the value.
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the block. 
     * @param value
     */
    protected void setValue(int value) {
        this.value = value;
    }

    /** How bright is the block?
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black. Default 50.
     * @return 
     */
    public int getLightlevel() {
        return lightlevel;
    }

   /** Set the brightness of the Block.
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black.
     * @param lightlevel 
     */
    public void setLightlevel(int lightlevel) {
        this.lightlevel = lightlevel;
    }
    
   /**
     * Returns true, when invisible. Invisible blocks are not rendered.
     * @return 
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * Check if the block is liquid.
     * @return true if liquid, false if not 
     */
    public boolean isLiquid() {
        return liquid;
    } 
    
    /**
     * Is the block a true block or represents it another thing?
     * @return 
     */
    public boolean isBlock() {
        return isBlock;
    }   
    
    /**
     * Make a side (in)visible. If one side is visible, the whole block is visible.
     * @param side 0 = left, 1 = top, 2 = right
     * @param visible The value
     */
    public void setSideVisibility(int side, boolean visible) {
        if (visible) this.visible = true;
        
        if (side==0)
            renderLeft = visible;
        else if (side==1)
            renderTop = visible;
                else if (side==2)
                    renderRight = visible;
    }
    
  /**
     * Returns the field where the coordiantes are in in relation to the current Block. Counts clockwise startin with the top 0.
     * If you want to get the neighbour you have to use a SelfAwareBlock and the method getNeighbourBlock
     * 701
     * 682
     * 543
     * @param x value in pixels
     * @param y value in pixels
     * @return Returns the fieldnumber of the coordinates. 8 is self.
     * @see com.BombingGames.Game.Blocks.SelfAwareBlock#getNeighbourBlock(int, int) 
     */
    protected int getSideNumb(int x, int y) {
        int result = 8;
        if (x+y <= DIM2)//top left
            result = 7;
        if (x-y >= DIM2) //top right
            if (result==7) result=0; else result = 1;
        if (x+y >= 3*DIM2)//bottom right
            if (result==1) result=2; else result = 3;
        if (-x+y >= DIM2) //bottom left
            if (result==3) result=4; else if (result==7) result = 6; else result = 5;
        return result;
    }

    /**
     * Returns the name of the block
     * @return the name of the block
     */
    public String getName() {
        return name;
    }
 

    /**
     * Returns the depth of the block. The depth is an int value wich is needed for producing the list of the renderorder. The higher the value the later it will be drawn.
     * @param y 
     * @param z 
     * @return the depth
     */
    public int getDepth(int y, int z) {
        return (int) (DIMENSION*y +(y % 2)*DIM2 + DIMENSION*z + pos[0] + (dimensionY-1)*DIMENSION);
    }
    
    /**
     * Draws a block
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @param camera  
     */
    public void render(int x, int y, int z, Camera camera) {
        //draw every visible block except air
        if (id != 0 && visible){            
            if (isBlock){
                if (renderTop) renderSide(x,y,z, 1, camera);
                if (renderLeft) renderSide(x,y,z, 0, camera);
                if (renderRight) renderSide(x,y,z, 2, camera);
            } else {
                Image image = getSprite(id, value,dimensionY);

                //calc  brightness
                float brightness = lightlevel / 100f;
                //System.out.println("Lightlevel " + Controller.map.data[x][y][z].lightlevel + "-> "+lightlevel);
                
                //or paint whole block with :
                //int brightness = renderBlock.lightlevel * 255 / 100;
                //new Color(brightness,brightness,brightness).bind(); 
                
                image.setColor(0, brightness,brightness,brightness);
                image.setColor(1, brightness,brightness, brightness);

                brightness -= .1f;

                image.setColor(2, brightness, brightness, brightness);
                image.setColor(3, brightness, brightness, brightness);
                
                int xpos = getScreenPosX(x,y,z,camera);
                
                int ypos = getScreenPosY(x,y,z,camera) - (dimensionY-1)*DIM2;
                
                image.drawEmbedded(xpos, ypos);
            }
        }
    }
    /**
     * Draws a side of a block
     * @param x
     * @param y
     * @param z
     * @param sidenumb The number of the side. 0 left, 1 top 2, right
     * @param renderBlock The block which gets rendered
     */
    private void renderSide(int x, int y, int z, int sidenumb, Camera camera){
        Image image = getBlockSprite(id,value,sidenumb);
        
        if (Gameplay.getController().hasGoodGraphics()){
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MULT);
        
            if (sidenumb == 0){
                int brightness = lightlevel * 255 / 100;
                new Color(brightness,brightness,brightness).bind();
            } else {
                Color.black.bind();
            }
        }
        
        //calc  brightness
        float brightness = lightlevel / 50f;
                
        image.setColor(0, brightness,brightness,brightness);
        image.setColor(1, brightness,brightness, brightness);

        if (sidenumb != 1) brightness -= .3f;

        image.setColor(2, brightness, brightness, brightness);
        image.setColor(3, brightness, brightness, brightness);
        
        //right side is  half a block more to the right
        int xpos = getScreenPosX(x,y,z,camera) + ( sidenumb == 2 ? DIM2 : 0);
        
        //the top is drawn a quarter blocks higher
        int ypos = getScreenPosY(x,y,z,camera) + (sidenumb != 1 ? DIM4 : 0);
        
        image.drawEmbedded(xpos, ypos);
    }
}