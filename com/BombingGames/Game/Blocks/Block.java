package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Gameplay;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

/**
 * A Block is a wonderfull piece of information and a geometrical object.
 * @author Benedikt
 */
public class Block {
    private final int id;
    
    /**
        * the value which can be used for storing information about a sub version of the object
        */
    private int value = 0;
    /**
       * The Name of the Block.
       */
    
        /**
     * Can light travel throug block?
     */
    private boolean transparent;

    /**
        * Is this Block an obstacle or can you pass through?
        */
    private boolean obstacle;
    /**
     * 
     */
    public final String name;

    /**
       * WIDTH of the image
       */
    public static final int WIDTH = 160;
    /**
       *HEIGHT of the image 
       */
    public static final int HEIGHT = 80;
    
    /**
     * The size the block is rendered at. You should only use this at rendering!
     */
    public static int displWidth;
    /**
     * The HEIGHT the block is rendered at. You should only use this at rendering!
     */
    public static int displHeight;
    
    /**
     * How much bigger is the WIDTH than the hight (block HEIGHT not image HEIGHT) of a block?
     */
    public static final float aspectRatio;
    /**
     * The X positon of the Block sprite
     */
    public int[] spriteX = new int[9];
    /**
     * The Y position of the Block sprite
     */
    public int[] spriteY = new int[9];
    
    /**
     * Has the positions of the sprites for rendering with sides
     * 1. Dimension id
     * 2. Dimension: value
     * 3. Dimension: Side
     * 4. Dimension: X- or Y-coordinate
     */
    public static final int[][][][] SidesSprites = new int[99][9][3][2];
    
    private int lightlevel = 50;
    /**
     * The offset of the image
     */
    private int offsetX, offsetY;
    
    /**
     * The sprite image which contains every block image
     */
    public static SpriteSheet Blocksheet;
    
    /**
     * Changes the order the block is rendered. When renderorder = 1 the Block is drawn in front of the right block. When it is -1 it is draw behind the left block. 0 is default.
     */
    public int renderorder = 0;
    
    /**
     * Render top side?
     */
    private boolean renderTop = false;
    /**
     * Render Left Side?
     */
    private boolean renderLeft = false;
    /**
     * Render Right Side
     */
    private boolean renderRight = false;
    private boolean visible;
    
    /**
        * How much <b>h</b>ealth <b>p</b>oints has the block?
        * When it is 0 it get's destroyed.
        */
    private int hp = 100;
    
    
    static {
        aspectRatio = WIDTH/HEIGHT;
        Log.debug("Aspect ratio of blocks: "+ Float.toString(aspectRatio));
        SidesSprites[1][0][0][0] = 3;
        SidesSprites[1][0][0][1] = 0;
        SidesSprites[1][0][1][0] = 4;
        SidesSprites[1][0][1][1] = 0;
        SidesSprites[1][0][2][0] = 5;
        SidesSprites[1][0][2][1] = 0;
        SidesSprites[2][0][0][0] = 0;
        SidesSprites[2][0][0][1] = 1;
        SidesSprites[2][0][1][0] = 1;
        SidesSprites[2][0][1][1] = 1;
        SidesSprites[2][0][2][0] = 2;
        SidesSprites[2][0][2][1] = 1;
        //asphalt
        SidesSprites[4][0][0][0] = 1;
        SidesSprites[4][0][0][1] = 3;
        SidesSprites[4][0][1][0] = 2;
        SidesSprites[4][0][1][1] = 3;
        SidesSprites[4][0][2][0] = 3;
        SidesSprites[4][0][2][1] = 3;
        //concrete
        SidesSprites[7][0][0][0] = 4;
        SidesSprites[7][0][0][1] = 4;
        SidesSprites[7][0][1][0] = 5;
        SidesSprites[7][0][1][1] = 4;
        SidesSprites[7][0][2][0] = 0;
        SidesSprites[7][0][2][1] = 5;
        SidesSprites[8][0][0][0] = 2;
        SidesSprites[8][0][0][1] = 5;
        SidesSprites[8][0][1][0] = 3;
        SidesSprites[8][0][1][1] = 5;
        SidesSprites[8][0][2][0] = 0;
        SidesSprites[8][0][2][1] = 6;
        SidesSprites[8][1][0][0] = 1;
        SidesSprites[8][1][0][1] = 5;
        SidesSprites[8][1][1][0] = 5;
        SidesSprites[8][1][1][1] = 5;
        SidesSprites[8][1][2][0] = 2;
        SidesSprites[8][1][2][1] = 6;
        SidesSprites[8][2][0][0] = 5;
        SidesSprites[8][2][0][1] = 0;
        SidesSprites[8][2][1][0] = 4;
        SidesSprites[8][2][1][1] = 5;
        SidesSprites[8][2][2][0] = 1;
        SidesSprites[8][2][2][1] = 6;
        SidesSprites[9][0][0][0] = 3;
        SidesSprites[9][0][0][1] = 6;
        SidesSprites[9][0][1][0] = 4;
        SidesSprites[9][0][1][1] = 6;
        SidesSprites[9][0][2][0] = 5;
        SidesSprites[9][0][2][1] = 6;
        SidesSprites[40][0][0][0] = 3;
        SidesSprites[40][0][0][1] = 1;
        SidesSprites[40][0][1][0] = 4;
        SidesSprites[40][0][1][1] = 1;
        SidesSprites[40][0][2][0] = 5;
        SidesSprites[40][0][2][1] = 1;
        SidesSprites[40][1][0][0] = 3;
        SidesSprites[40][1][0][1] = 1;
        SidesSprites[40][1][1][0] = 4;
        SidesSprites[40][1][1][1] = 1;
        SidesSprites[40][1][2][0] = 5;
        SidesSprites[40][1][2][1] = 1;
        
        //pack-u-like
//        SidesSprites[0][0][0] = "0-1.png";
//        SidesSprites[0][0][1]= "0-1.png";
//        SidesSprites[0][0][2]= "0-2.png";
//        SidesSprites[1][0][0] = "1-0.png";
//        SidesSprites[1][0][1]= "1-1.png";
//        SidesSprites[1][0][2] = "1-2.png";
//        SidesSprites[2][0][0] = "2-0.png";
//        SidesSprites[2][0][1] = "2-1.png";
//        SidesSprites[2][0][2] = "2-2.png";
//        SidesSprites[4][0][0] = "4-0.png";
//        SidesSprites[4][0][1] = "4-1.png";
//        SidesSprites[4][0][2] = "4-2.png";
//        SidesSprites[7][0][0] = "7-0.png";
//        SidesSprites[7][0][1] = "7-1.png";
//        SidesSprites[7][0][2] = "7-2.png";
//        SidesSprites[8][0][0] = "8-0.png";
//        SidesSprites[8][0][1] = "8-1-0.png";
//        SidesSprites[8][0][2] = "8-2-0.png";
//        SidesSprites[8][1][0] = "8-1-1.png";
//        SidesSprites[8][1][1] = "8-1-1.png";
//        SidesSprites[8][1][2] = "8-1-2.png";
//        SidesSprites[8][2][0] = "8-2-0.png";
//        SidesSprites[8][2][1] = "8-2-1.png";
//        SidesSprites[8][2][2] = "8-2-2.png";
//        SidesSprites[9][0][0] = "9-0.png";
//        SidesSprites[9][0][1] = "9-1.png";
//        SidesSprites[9][0][2] = "9-2.png";
//        SidesSprites[40][0][0] = "20-0.png";
//        SidesSprites[40][0][1] = "20-1.png";
//        SidesSprites[40][0][2] = "20-2.png";
//        SidesSprites[40][1][0] = "20-0.png";
//        SidesSprites[40][1][1] = "20-1.png";
//        SidesSprites[40][1][2] = "20-2.png";
    }

    /**
     * Creates an air block. 
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
     * @param id 
     * @param value 
     */    
    public Block(int id, int value){
        this.id = id;
        this.value = value;
        
        //define the default SideSprites
        switch (id){
            case 0: name = "air";
                    transparent = true;
                    obstacle = false;
                    break;
            case 1: name = "gras";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=0;
                    spriteY[0]=1;
                    break;
            case 2: name = "dirt";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=0;
                    spriteY[0]=2;
                    break;
            case 3: name = "stone";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=1;
                    spriteY[0]=1;
                    break;
            case 4: name = "asphalt";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=1;
                    spriteY[0]=2;
                    break;
            case 5: name = "cobblestone";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=2;
                    spriteY[0]=1;
                    break;
            case 6: name = "pavement";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=2;
                    spriteY[0]=2;
                    break;
            case 7: name = "concrete";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=3;
                    spriteY[0]=0;
                    break;
            case 8: name = "sand";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=3;
                    spriteY[0]=2;
                    break;      
            case 9: name = "water";
                    transparent = false;
                    obstacle = false;
                    break;    
            case 20:name = "red brick wall";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=1;
                    spriteY[0]=0;
                    break;
            case 40:name = "player";
                    transparent = true;
                    obstacle = false;
                    spriteX[0]=2;
                    spriteY[0]=0;
                    break;
            case 50:name = "strewbed";
                    transparent = true;
                    obstacle = false;
                    break;
            case 70:name = "campfire";
                    transparent = true;
                    obstacle = false;
                    spriteX[0]=4;
                    spriteY[0]=0;
                    break;
            default:name = "undefined";
                    transparent = true;
                    obstacle = true;
                    spriteX[0]=4;
                    spriteY[0]=0;
                    break; 
        }
    }
    

    
    /**
     * returns the id of a block
     * @return getId
     */
    public int getId(){
        return this.id;
    }
    
    
    /**
     * 
     * @return
     */
    public int getOffsetX(){
        return offsetX;
    }
    
    /**
     * 
     * @return
     */
    public int getOffsetY(){
        return offsetY;
    }
    
    /**
     * 
     * @param x
     * @param y
     */
    public void setOffset(int x, int y){
       offsetX = x;
       offsetY = y;
       if (offsetY > 0)
            renderorder = 1;
       else if (offsetX < 0 && offsetY < 0)
                renderorder = -1;
            else renderorder = 0;
    }
    
    /**
     * 
     * @param side Which side? (0 - 2)
     * @return the X-Coodinate of the Sprite
     */
    public int getSideSpritePosX(int side){
        return SidesSprites[id][value][side][0];        
    }
    
    /**
     * 
     * @param side Which side? (0 - 2)
     * @return the Y-Coodinate of the Sprite
     */
    public int getSideSpritePosY(int side){
        return SidesSprites[id][value][side][1];        
    }
    
    /**
     *  
     * @param side Which side? (0 - 2)
     * @return a image ot the side
     */
    public Image getSideSprite(int side){
        return Blocksheet.getSubImage(SidesSprites[id][value][side][0], SidesSprites[id][value][side][1]);
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
     * 
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
     * 
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
    public void setValue(int value) {
        this.value = value;
    }

        /** How bright is the block?
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black.
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
     * Draws a block
     * @param x
     * @param y
     * @param z
     */
    public void draw(int x,int y, int z) {
        //draw every block except air
        if (id != 0){
            //System.out.println("X: "+x+" Y:"+y+" Z: "+z);
            //Block renderBlock = Controller.map.data[x][y][z]; 
            
            if (Gameplay.controller.renderSides()){ 
                if (renderTop) drawSide(x,y,z, 1);
                if (renderLeft) drawSide(x,y,z, 0);
                if (renderRight) drawSide(x,y,z, 2);

            } else {
                Image temp = Block.Blocksheet.getSubImage(spriteX[0], spriteY[0]);

                //calc  brightness
                float brightness = lightlevel / 100f;
                //System.out.println("Lightlevel " + Controller.map.data[x][y][z].lightlevel + "-> "+lightlevel);
                
                //or paint whole block with :
                //int brightness = renderBlock.lightlevel * 255 / 100;
                //new Color(brightness,brightness,brightness).bind(); 
                
                temp.setColor(0, brightness,brightness,brightness);
                temp.setColor(1, brightness,brightness, brightness);

                brightness -= .1f;
                //System.out.println(lightlevel);

                temp.setColor(2, brightness, brightness, brightness);
                temp.setColor(3, brightness, brightness, brightness);
                
                temp.drawEmbedded(
                    -Gameplay.view.camera.getX()
                    + x*Block.WIDTH
                    + (y%2) * (int) (Block.WIDTH/2)
                    + getOffsetX()
                    ,
                    -Gameplay.view.camera.getY()/2
                    + y*Block.HEIGHT/2
                    - z*Block.HEIGHT
                    + getOffsetY() * (1/Block.aspectRatio)
                );
                
//                Block.Blocksheet.renderInUse(
//                    (int) (zoom*Controller.map.posX) + x*Block.displWidth + (y%2) * (int) (Block.displWidth/2) + renderBlock.getOffsetX(),
//                    (int) (zoom*Controller.map.posY / 2) + y*Block.displHeight/4 - z*Block.displHeight/2 + renderBlock.getOffsetY(),
//                    renderBlock.spritex,
//                    renderBlock.spritey
//                );
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
    private void drawSide(int x, int y, int z,int sidenumb){
        Image sideimage = getSideSprite(sidenumb);
        
        if (Gameplay.controller.hasGoodGraphics()){
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
                
        sideimage.setColor(0, brightness,brightness,brightness);
        sideimage.setColor(1, brightness,brightness, brightness);

        if (sidenumb!=1) brightness -= .3f;

        sideimage.setColor(2, brightness, brightness, brightness);
        sideimage.setColor(3, brightness, brightness, brightness);
        
        sideimage.drawEmbedded(
            -  Gameplay.view.camera.getX()
            + x*Block.WIDTH
            + (y%2) * (int) (Block.WIDTH/2)
            + getOffsetX()
            ,            
            - Gameplay.view.camera.getY()
            + y*Block.HEIGHT/2
            - z*Block.HEIGHT
            + ( sidenumb == 1 ? -Block.HEIGHT/2:0)//the top is drawn /4 Blocks higher
            + getOffsetY() * (1/Block.aspectRatio)
        );
    }
        
  /**
     * creates the new sprite image at a specific zoom factor. Also calculates displWidth and displHeight which change with zooming.
     * @param zoom the zoom factor of the new image
     */
    public static void reloadSprites(float zoom) {
        displWidth = (int) (WIDTH*zoom);
        displHeight = (int) (HEIGHT*zoom);
        try {
            if (Gameplay.controller.renderSides()){//single sides
                Blocksheet = new SpriteSheet("com/BombingGames/Game/Blockimages/SideSprite.png", WIDTH, (int) (HEIGHT*1.5f));
            
                Gameplay.MSGSYSTEM.add("displWidth: "+displWidth);
                Log.debug("displWidth: "+displWidth);
                Gameplay.MSGSYSTEM.add("displHeight: "+displHeight);
                Log.debug("displHeight: "+displHeight);

            } else {//whole Blocks
                Blocksheet = new SpriteSheet("com/BombingGames/Game/Blockimages/Blocksprite.png", WIDTH, HEIGHT*2, 4);
                
                Gameplay.MSGSYSTEM.add("BlockWidth"+displWidth);
            }
        } catch (SlickException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Returns the field where the coordiantes are in. Counts clockwise startin with the top 0.
     * 701
     * 682
     * 543
     * @param x value in pixels
     * @param y value in pixels
     * @return Returns the corner of the coordinates. 8 is self.
     * @see com.BombingGames.Game.Blocks.SelfAwareBlock#getNeighbourBlock(int, int) 
     */
    protected int getCorner(int x, int y) {
        if (x+y <= Block.WIDTH /2)
            return 7;//top left
        else if (x-y >= Block.WIDTH /2) 
                return 1; //top right
             else if (x+y >= 3*Block.WIDTH /2)
                    return 3;//bottom right
                else if (-x+y >= Block.WIDTH /2)
                        return 5;//bottom left
                    else return 8;//the middle
    }

    /**
     * has the block an offset? if x or y is !=0 it is true.
     * @return 
     */
    public boolean hasOffset() {
        return offsetX!=0 || offsetY!=0;
    }
}
