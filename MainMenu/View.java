package MainMenu;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//import com.BombingGames.View.Blockimages;

public class View {
    float startGameScale = 1;
    float exitScale = 1;
    Image background = null;
    Image startGameOption = null;
    Image exitOption = null;
    /*public GameController GameController;
    public static JButton[] menubutton = new JButton[5]; 
    BufferStrategy bufferStrategy;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static Insets insets;*/
    
    
    //Konstruktor
    public View() throws SlickException{
        background = new Image("MainMenu/Images/bg.png");
 
        // load the menu images
        Image menuOptions = new Image("MainMenu/Images/MainMenu.png");
 
        startGameOption = menuOptions.getSubImage(0, 0, 400, 50);
 
        exitOption = menuOptions.getSubImage(0, 50, 400, 50);
        /*menubutton[0] = new JButton("Neue Map");
        menubutton[0].addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                try {
                    GameController GameController = new GameController(false);
                    window.remove(menubutton[0]);
                    window.remove(menubutton[1]);
                    window.remove(menubutton[2]);
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
        );
        
        menubutton[1] = new JButton("Map laden");
        menubutton[1].addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                try {
                    GameController GameController = new GameController(true);
                    window.remove(menubutton[0]);
                    window.remove(menubutton[1]);
                    window.remove(menubutton[2]);
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
        );
        
        menubutton[2] = new JButton("Beenden");
        menubutton[2].addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    executorService.shutdown();
                    System.exit(0);  
                }
            }
        );
        
        window.getContentPane().add(menubutton[0]);  
        window.getContentPane().add(menubutton[1]); 
        window.getContentPane().add(menubutton[2]); 
         */      
    }

    public void render(Controller Controller){
        // render the background
        background.draw(0,0);
 
        // Draw menu
        startGameOption.draw(50, 50, startGameScale);
 
        exitOption.draw(300, 300, exitScale);
    }
    /*private static int window_width(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.width - insets.left - insets.right;
        //Controller.ChunkSizeX = value ;
        return value;
   }
   
    private static int window_height(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.height - insets.top - insets.bottom;
        //Controller.ChunkSizeY = value ;
        return value;
   }*/
}