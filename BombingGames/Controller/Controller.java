package BombingGames.Controller;

import BombingGames.View.View;

public class Controller {
    public static View View;

    public Controller(){
        System.out.println("Controller");
        Controller.View = new View();
    }
   
}
