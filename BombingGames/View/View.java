package BombingGames.View;

import BombingGames.Controller.GameController;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

//import com.BombingGames.View.Blockimages;

public class View extends javax.swing.JFrame {
    public GameController GameController;
    static JFrame window = new JFrame();
    public static JButton[] menubutton = new JButton[5]; 
    BufferStrategy bufferStrategy;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static Insets insets;
    
    
    //Konstruktor
    public View(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        window.setSize(400,400);
        
        //alternative. Lässt startposition vom System verwalten
        //window.setLocationByPlatform(true);
        window.setLocationRelativeTo(null);
        
        window.getContentPane().setLayout(new FlowLayout());
        
        menubutton[0] = new JButton("Neue Map");
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
        
        window.setVisible(true);       
    }

    private static int window_width(){
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
   }
}