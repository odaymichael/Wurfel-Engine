package BombingGames.Controller;

public class Block {
    Integer Health = 100;
    public Integer ID = -1;
    Integer Value = 0;
    String name = "undefined";
    boolean transparent;
    boolean obstacle;
    //Konstruktor
    public Block(Integer pID, Integer pValue){
        ID = pID;
        Value = pValue;
        switch (ID){
            case 0: {
                     name = "air";
                     transparent = true;
                     obstacle = false;
                };
            case 1: {
                     name = "gras";
                     transparent = false;
                     obstacle = true;
                };
            case 2: {
                     name = "dirt";
                     transparent = false;
                     obstacle = true;
                };
            case 3: {
                     name = "stone";
                     transparent = false;
                     obstacle = true;
                };
            case 4: {
                     name = "asphalt";
                     transparent = false;
                     obstacle = true;
                };
            case 5: {
                     name = "cobblestone";
                     transparent = false;
                     obstacle = true;
                };
            case 20: {
                     name = "red brick wall";
                     transparent = false;
                     obstacle = true;
                };
            case 50: {
                      name = "strohbed" ;
                      transparent = true;
                      obstacle = false;
                 };   
            default: {
                     name = "undefined";
                     transparent = true;
                     obstacle = false;
                  };  
        }
    }
}
