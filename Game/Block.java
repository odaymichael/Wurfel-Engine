package Game;

public class Block {
    public Integer Health = 100;
    public Integer ID = -1;
    public Integer Value = 0;
    public String name = "undefined";
    boolean transparent;
    boolean obstacle;
    //Konstruktor
    public Block(Integer pID, Integer pValue){
        ID = pID;
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
            case 20: name = "red brick wall";
                     transparent = false;
                     obstacle = true;
                     break;
            case 50: name = "strohbed" ;
                     transparent = true;
                     obstacle = false;
                     break;  
            default: name = "undefined";
                     transparent = true;
                     obstacle = false;
                     break; 
        }
    }
}
