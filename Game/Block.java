package Game;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * A Block is a wonderfull piece of information.
 * @author Benedikt
 */
public class Block {
    /**
     * How much health points has the block? When 0 it get's destroyed.
     */
    public Integer HP = 100;
    private Integer ID = -1;
    /**
     * the value which can be used for storing information about a sub version of the object
     */
    public Integer Value = 0;
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
    
    public static int displBlockWidth = width;
    public static int displBlockHeight = height;
    
    public int spritex = 0;
    public int spritey = 0;
    
    public int lightlevel = 100;
    public int offsetX = 0;
    public int offsetY = 0;
    
    public static SpriteSheet Blocksheet;
    
    
    
    
    /**
     * Creates an air block. 
     */ 
    public Block(){
        this(0,0);
    }
    
    /**
     * Creates a block (ID) with Value 0. 
     *  @param pValue Which block (ID)?
     */ 
    public Block(Integer ID){
        this(ID,0);
    }    
    
    /**
     * Creates a block (ID) with Value (Value).
     * @param pID Which block (ID)?
     * @param pValue With wich Value?
     */    
    public Block(Integer ID, Integer pValue){
        this.ID = ID;
        Value = pValue;
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
            case 40: 
                    name = "player";
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
     * 
     * @throws SlickException
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

    
    
}
