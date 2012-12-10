package com.BombingGames.Game;

import java.util.ArrayList;
import org.newdawn.slick.Color;

/**
 *The message system can show messages.
 * @author Benedikt
 */
public class MsgSystem extends ArrayList {
    private int timelastupdate = 0;
    private boolean waitforinput = false;
        
    /**
     * 
     */
    public MsgSystem(){
    }
        
    /**
     * Adds a message with the sender "System"
     * @param message
     */
    public void add(String message) {
        add(new Msg(message, "System", 100));
    }
    
    /**
     * 
     * @param message
     * @param sender
     */
    public void add(String message, String sender){
        add(new Msg(message, sender, 100));
    }
    
    /**
     * 
     * @param message
     * @param sender
     * @param importance
     */
    public void add(String message, String sender, int importance){
        add(new Msg(message, sender, importance));
    }
    
    /**
     * Updates the Message System
     * @param delta
     */
    public void update(int delta){
       //decrease importance
       timelastupdate += delta;
       //delay 30*100 = 3000ms
       if (timelastupdate >= 30) {
            timelastupdate = 0;
            for (int i=0; i < size(); i++) {
                Msg temp = (Msg) get(i);
                if (temp.getImportance()>0)
                    temp.setImportance(temp.getImportance()-1); 
                    else remove(i);
            }
        }
    }
    
    /**
     * Draws the Messages
     */
    public void draw(){
        if (waitforinput) Gameplay.view.g.drawString("MSG:", Gameplay.gc.getWidth()/2, 3*Gameplay.gc.getHeight()/4);
        
        for (int i=0; i < size(); i++){
            Msg msg = (Msg) get(i);
            Color clr = Color.blue;
            if ("System".equals(msg.getSender())) clr = Color.green;
                else if ("Warning".equals(msg.getSender())) clr = Color.red;
            
            //draw
            View.tTFont.drawString(
                10,
                50+i*20,
                msg.getMessage(),
                clr
            );
        }
    }

    void listenForInput(boolean input) {
        waitforinput = input;
    }
    
    boolean isListeningForInput() {
        return waitforinput;
    }
    
    
}