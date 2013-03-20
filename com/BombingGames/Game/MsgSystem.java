package com.BombingGames.Game;

import com.BombingGames.Launcher;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A message is put into the MsgSystem. It contains the message, the sender and the importance.
 * @author Benedikt
 */
class Msg {
    private String fmessage;
    private String sender = "System";
    private int importance = 1;
    
    
    protected Msg(String pmessage, String psender, int imp) {
        fmessage = pmessage;
        sender = psender;
        importance = imp;
    }
    
    /**
     * 
     * @return
     */
    public String getMessage(){
        return fmessage;    
    }
    
    /**
     * 
     * @return
     */
    public String getSender(){
        return sender;
    }
    
    /**
     * 
     * @return
     */
    public int getImportance(){
        return importance;
    }
  
    /**
     * Sets the importance
     * @param imp
     */
    public void setImportance(int imp){
        if ((imp>=0) && (imp<=100))
            importance = imp;    
    }
}

/**
 *The message system can manage&show messages (Msg).
 * @author Benedikt
 */
public class MsgSystem extends ArrayList<Msg> {
    private int timelastupdate = 0;
    private boolean waitforinput = false;
    private int xPos, yPos;    

    /**
     * 
     * @param xPos
     * @param yPos
     */
    public MsgSystem(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
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
                Msg temp = get(i);
                if (temp.getImportance()>0)
                    temp.setImportance(temp.getImportance()-1); 
                    else remove(i);
            }
        }
    }
    
    /**
     * Draws the Messages
     * @param g The graphics context.
     */
    public void render(Graphics g){
        if (waitforinput) g.drawString("MSG:", xPos, yPos);
        
        for (int i=0; i < size(); i++){
            Msg msg = get(i);
            Color clr = Color.blue;
            if ("System".equals(msg.getSender())) clr = Color.green;
                else if ("Warning".equals(msg.getSender())) clr = Color.red;
            
            //draw
            View.getFont().drawString(
                10,
                50+i*20,
                msg.getMessage(),
                clr
            );
        }
    }

    /**
     * 
     * @param input
     */
    public void listenForInput(boolean input) {
        waitforinput = input;
    }
    
    /**
     * 
     * @return
     */
    public boolean isListeningForInput() {
        return waitforinput;
    }
}