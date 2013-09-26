package com.BombingGames.EngineCore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

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
    private String input = "";

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
        Gdx.app.log("DEBUG",message);
    }
    
    /**
     * 
     * @param message
     * @param sender
     */
    public void add(String message, String sender){
        add(new Msg(message, sender, 100));
        Gdx.app.log("DEBUG",message);
    }
    
    /**
     * 
     * @param message
     * @param sender
     * @param importance
     */
    public void add(String message, String sender, int importance){
        add(new Msg(message, sender, importance));
        Gdx.app.log("DEBUG",message);
    }
    
    /**
     * Updates the Message System
     * @param delta
     */
    public void update(float delta){
       timelastupdate += delta;
       
       //derease importance every 30ms
       if (timelastupdate >= 30) {
            timelastupdate = 0;
            for (int i=0; i < size(); i++) {
                Msg temp = get(i);
                if (temp.getImportance() > 0)
                    temp.setImportance(temp.getImportance()-1); 
                else remove(i);
            }
        }
    }
    
    /**
     * Draws the Messages
     * @param view 
     */
    public void render(View view){
        if (waitforinput) view.drawString("MSG:"+input, xPos, yPos);
        
        view.getBatch().begin();
        for (int i=0; i < size(); i++){
            Msg msg = get(i);
            Color color = Color.BLUE;
            if ("System".equals(msg.getSender())) color = Color.GREEN;
                else if ("Warning".equals(msg.getSender())) color = Color.RED;
            
            //draw
            view.getBatch().setColor(color);
            view.getFont().draw(view.getBatch(), msg.getMessage(), 10,50+i*20);
        }
         view.getBatch().end();
    }

    /**
     * 
     * @param listen
     */
    public void listenForInput(boolean listen) {
        if (listen != waitforinput && !"".equals(input)) {
            add(input);
            input = "";
        }
        waitforinput = listen;
    }
    
    /**
     * 
     * @return
     */
    public boolean isListeningForInput() {
        return waitforinput;
    }
    
    /**
     *
     * @param characterInput
     */
    public void getInput(char characterInput){
        input += String.valueOf(characterInput);
    }
}