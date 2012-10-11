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
    public static int displBlockWidth = width;
    /**
     * The height the block is rendered at.
     */
    public static int displBlockHeight = height;
    
    /**
     * The X positon of the sprite
     */
    public int spritex = 0;
    /**
     * The Y position of the sprite
     */
    public int spritey = 0;
    
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
    
    

    /**
     * Creates an air block. 
     */ 
    public Block(){
        this(0,0);
    }
    
    /**
     * Creates a block (ID) with Value 0. 
     *  @param ID 
     */ 
    public Block(Integer ID){
        this(ID,0);
    }    
    
    /**
     * Creates a block (ID) with Value (Value).
     * @param ID 
     * @param Value 
     */    
    public Block(Integer ID, Integer Value){
        this.ID = ID;
        this.Value = Value;
        switch (ID){
            case 0:  name = "air";
                     transparent = true;
                     obstacle = false;
                     break;
            case 1:  name = "gras";
                     transparent = false;
                     obstacle = true;
                     spritex=0;
                     spritey=1;
                     break;
            case 2:  name = "dirt";
                     transparent = false;
                     obstacle = true;
                     spritex=0;
                     spritey=2;
                     break;
            case 3:  name = "stone";
                     transparent = false;
                     obstacle = true;
                     spritex=1;
                     spritey=1;
                     break;
            case 4:  name = "asphalt";
                     transparent = false;
                     obstacle = true;
                     spritex=1;
                     spritey=2;
                     break;
            case 5:  name = "cobblestone";
                     transparent = false;
                     obstacle = true;
                     spritex=2;
                     spritey=1;
                     break;
            case 6:  name = "pavement";
                     transparent = false;
                     obstacle = true;
                     spritex=2;
                     spritey=2;
                     break;
            case 7:  name = "concrete";
                     transparent = false;
                     obstacle = true;
                     spritex=3;
                     spritey=0;
                     break;
            case 8:  name = "sand";
                     transparent = false;
                     obstacle = true;
                     spritex=3;
                     spritey=2;
                     break;      
            case 9:  name = "water";
                     transparent = true;
                     obstacle = false;
                     break;    
            case 20: name = "red brick wall";
                     transparent = false;
                     obstacle = true;
                     spritex=1;
                     spritey=0;
                     break;
            case 40: name = "player";
                     transparent = true;
                     obstacle = false;
                     spritex=2;
                     spritey=0;
                     break;
            case 50: name = "strewbed";
                     transparent = true;
                     obstacle = false;
                     break;
            case 70: name = "campfire";
                     transparent = true;
                     obstacle = false;
                     spritex=4;
                     spritey=0;
                     break;
            default: name = "undefined";
                     transparent = true;
                     obstacle = true;
                     spritex=4;
                     spritey=0;
                     break; 
        }
    }
    
    /**
     * creates the new sprite image at a specific zoom factor. Also calculates displBlockWidth and displBlockHeight which change with zooming.
     * @param zoom the zoom factor of the new image
     */
    public static void listImages(float zoom) {
        try {
            SpriteSheet srcBlockSheet = new SpriteSheet("Game/Blockimages/Blocksprite.png", width, height, 4);
            Image scaledBlockSheet = srcBlockSheet.getScaledCopy(zoom);
            int spacing = ((scaledBlockSheet.getWidth()) /5 - (int) (width*zoom)) ; // divide by amount of blocks in a row
            //int spacing = (int) (4*zoom);
            //displBlockWidth = (int) ((scaledBlockSheet.getWidth()+4)/5 - spacing);
            displBlockWidth = (int) (width*zoom);
            displBlockHeight = (int) (height*zoom);
            
            GameplayState.iglog.add("Spacing:"+spacing);
            GameplayState.iglog.add("BlockWidth"+displBlockWidth);
            
            Blocksheet = new SpriteSheet(
                scaledBlockSheet,
                displBlockWidth,
                displBlockHeight,
                spacing
            );
        } catch (SlickException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * returns ID
     * @return ID
     */
    public int ID(){
        return this.ID;
    }
    
    public void setOffset(int x, int y){
       offsetX = x;
       offsetY = y;
       if (offsetY > 0)
                renderorder = 1;
                else if (offsetX < 0 && offsetY < 0)
                        renderorder = -1;
                     else renderorder=0;
    }
    
    public int getOffsetX(){
        return offsetX;
    }
    
    public int getOffsetY(){
        return offsetY;
    }
    
    
}
