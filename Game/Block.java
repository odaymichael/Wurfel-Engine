package Game;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * A Block is a wonderfull piece of information and a geometrical object.
 * @author Benedikt
 */
public class Block {
    private int ID = -1;
    
    /**
        * How much <b>h</b>ealth <b>p</b>oints has the block?
        * When it is 0 it get's destroyed.
        */
    public int HP = 100;
    /**
        * the value which can be used for storing information about a sub version of the object
        */
    public int Value = 0;
    /**
       * The Name of the Block.
       */
    public String name = "undefined";
    /**
     * Can light travel throug block?
     */
    public boolean transparent;
    /**
        * Is this Block an obstacle or can you pass through?
        */
    public boolean obstacle;
    /**
       * width of the image
       */
    public static final int width = 160;
    /**
       *height of the image 
       */
    public static final int height = 160;
    
    /**
     * The size the block is rendered at.
     */
    public static int displWidth = width;
    /**
     * The height the block is rendered at.
     */
    public static int displHeight = height;
    
    /**
     * The X positon of the sprite
     */
    public int[] spriteX = new int[9];
    /**
     * The Y position of the sprite
     */
    public int[] spriteY = new int[9];
    
    /**
     * 1. Layer: Value
     * 2. Layer: Side
     * 3. Layer: X- or Y-coordinate
     */
    private int[][][] SidesSprites = new int[2][3][2];
    
    /** How bright is the block?
     * The lightlevel is a number between 0 and 100. 100 is full bright. 0 is black.
     */
    public int lightlevel = 100;
    /**
     * The offset of the image in X direction
     */
    private int offsetX = 0;
    /**
     * The offset of the image in Y direction
     */
    private int offsetY = 0;
    
    /**
     * The sprite image which contains every block image
     */
    public static SpriteSheet Blocksheet;
    
    /**
     * Changes the order the block is rendered. When renderorder = 1 the Block is drawn in front of the right block. When it is -1 it is draw behind the left block. 0 is default.
     */
    public int renderorder = 0;
    
    public boolean renderTop = false;
    public boolean renderLeft = false;
    public boolean renderRight = false;
    private boolean visible;
    
    

    /**
     * Creates an air block. 
     */ 
    public Block(){
        this(0,0);
    }
    
    /**
     * Creates a block (getID) with Value 0. 
     *  @param ID 
     */ 
    public Block(int ID){
        this(ID,0);
    }    
    
    /**
     * Creates a block (getID) with Value (Value).
     * @param ID 
     * @param Value 
     */    
    public Block(int ID, int Value){
        this.ID = ID;
        this.Value = Value;
        
        //define the default SideSprites
        SidesSprites[0][0][0] = 0;
        SidesSprites[0][0][1] = 0;
        SidesSprites[0][1][0] = 1;
        SidesSprites[0][1][1] = 0;
        SidesSprites[0][2][0] = 2;
        SidesSprites[0][2][1] = 0;
        
        switch (ID){
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
                    SidesSprites[0][0][0] = 0;
                    SidesSprites[0][0][1] = 1;
                    SidesSprites[0][1][0] = 1;
                    SidesSprites[0][1][1] = 1;
                    SidesSprites[0][2][0] = 2;
                    SidesSprites[0][2][1] = 1;
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
                    SidesSprites[0][0][0] = 0;
                    SidesSprites[0][0][1] = 2;
                    SidesSprites[0][1][0] = 1;
                    SidesSprites[0][1][1] = 2;
                    SidesSprites[0][2][0] = 2;
                    SidesSprites[0][2][1] = 2;
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
                    SidesSprites[0][0][0] = 0;
                    SidesSprites[0][0][1] = 3;
                    SidesSprites[0][1][0] = 1;
                    SidesSprites[0][1][1] = 3;
                    SidesSprites[0][2][0] = 2;
                    SidesSprites[0][2][1] = 3;
                    break;
            case 8: name = "sand";
                    transparent = false;
                    obstacle = true;
                    spriteX[0]=3;
                    spriteY[0]=2;
                    SidesSprites[0][0][0] = 0;
                    SidesSprites[0][0][1] = 4;
                    SidesSprites[0][1][0] = 1;
                    SidesSprites[0][1][1] = 4;
                    SidesSprites[0][2][0] = 2;
                    SidesSprites[0][2][1] = 4;
                    break;      
            case 9: name = "water";
                    transparent = true;
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
     * returns the ID of a block
     * @return getID
     */
    public int getID(){
        return this.ID;
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
        return SidesSprites[Value][side][0];        
    }
    
    /**
     * 
     * @param side Which side? (0 - 2)
     * @return the Y-Coodinate of the Sprite
     */
    public int getSideSpritePosY(int side){
        return SidesSprites[Value][side][1];        
    }
    
    /**
     *  
     * @param side Which side? (0 - 2)
     * @return a image ot the side
     */
    public Image getSideSprite(int side){
        return Blocksheet.getSubImage(SidesSprites[Value][side][0], SidesSprites[Value][side][1]);
    }
    
      /**
     * creates the new sprite image at a specific zoom factor. Also calculates displWidth and displHeight which change with zooming.
     * @param zoom the zoom factor of the new image
     */
    public static void reloadSprites(float zoom) {
        try {
            if (GameplayState.Controller.goodgraphics){
                SpriteSheet srcBlockSheet = new SpriteSheet("Game/Blockimages/SideSprite.png", width, (int) (height*0.75));
            
                Image scaledBlockSheet = srcBlockSheet.getScaledCopy(zoom);
                //int spacing = ((scaledBlockSheet.getWidth()) /(int)(srcBlockSheet.getWidth()/width) - (int) (width*zoom)) ; // calculate spacing by dividing by amount of blocks in a row and later gettin the rest
                //int spacing = (int) (4*zoom);
                //displWidth = (int) ((scaledBlockSheet.getWidth()+4)/5 - spacing);
                displWidth = (int) (width*zoom);
                displHeight = (int) (height*zoom);

                //GameplayState.iglog.add("Spacing:"+spacing);
                GameplayState.iglog.add("BlockWidth"+displWidth);

                Blocksheet = new SpriteSheet(
                    scaledBlockSheet,
                    displWidth,
                    (int) (displHeight*0.75)
                );
            } else {
                SpriteSheet srcBlockSheet = new SpriteSheet("Game/Blockimages/Blocksprite.png", width, height, 4);
            
                Image scaledBlockSheet = srcBlockSheet.getScaledCopy(zoom);
                int spacing = ((scaledBlockSheet.getWidth()) /5 - (int) (width*zoom)) ; // divide by amount of blocks in a row
                //int spacing = (int) (4*zoom);
                //displWidth = (int) ((scaledBlockSheet.getWidth()+4)/5 - spacing);
                displWidth = (int) (width*zoom);
                displHeight = (int) (height*zoom);

                GameplayState.iglog.add("Spacing:"+spacing);
                GameplayState.iglog.add("BlockWidth"+displWidth);

                Blocksheet = new SpriteSheet(
                    scaledBlockSheet,
                    displWidth,
                    displHeight,
                    spacing
                );
            }
        } catch (SlickException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean getVisible(){
        return visible;
    }
    
}
