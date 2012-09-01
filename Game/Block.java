package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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
     * the value which can be used for storing information about a subversion of the object
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
    public static int width = 160;
    /**
     *height of the image 
     */
    public static int height = 160;
    /**
     * the list wich contains all images
     */
    public static Image images[] = new Image[75];
    
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
                     break;
            case 2:  name = "dirt";
                     transparent = false;
                     obstacle = true;
                     break;
            case 3:  name = "stone";
                     transparent = false;
                     obstacle = true;
                     break;
            case 4:  name = "asphalt";
                     transparent = false;
                     obstacle = true;
                     break;
            case 5:  name = "cobblestone";
                     transparent = false;
                     obstacle = true;
                     break;
            case 6:  name = "pavement";
                     transparent = false;
                     obstacle = true;
                     break;
            case 7:  name = "concrete";
                     transparent = false;
                     obstacle = true;
                     break;
            case 8:  name = "sand";
                     transparent = false;
                     obstacle = true;
                     break;      
            case 9:  name = "water";
                     transparent = true;
                     obstacle = false;
                     break;    
            case 20: name = "red brick wall";
                     transparent = false;
                     obstacle = true;
                     break;
            case 40: name = "player";
                     transparent = true;
                     obstacle = false;
                     break;
            case 50: name = "strewbed";
                     transparent = true;
                     obstacle = false;
                     break;
            case 70: name = "campfire";
                     transparent = true;
                     obstacle = false;
                     break;
            default: name = "undefined";
                     transparent = true;
                     obstacle = true;
                     break; 
        }
    }
    
    /**
     * 
     * @throws SlickException
     */
    public static void listImages() throws SlickException{
        for (int i=0; i<images.length; i++){
                try {
                    images[i] = new Image("Game/Blockimages/"+ i +"-0.png");
                    //images[i].setColor(1, 0, 0, 0, 1);
                    //images[i].setColor(1, 1, 1, 1, 1);
                 } catch(RuntimeException e) {
                    System.out.println("Block "+i+" not found. Using air instead.");
                    images[i] = new Image("Game/Blockimages/0-0.png");
                 }   
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
