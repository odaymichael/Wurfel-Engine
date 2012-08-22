package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Block {
    public Integer Health = 100;
    private Integer fID = -1;
    public Integer Value = 0;
    public String name = "undefined";
    public boolean transparent;
    public boolean obstacle;
    public static int width = 160;
    public static int height = 160;
    public static Image images[] = new Image[75];
    
    //Konstruktor
    
    public Block(Integer pID, Integer pValue){
        fID = pID;
        Value = pValue;
        switch (fID){
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
            case 9:  name = "water";
                     transparent = true;
                     obstacle = true;
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
                     obstacle = false;
                     break; 
        }
    }
    
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
    public int ID(){
        return fID;
    }
    
}
