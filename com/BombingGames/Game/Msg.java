package com.BombingGames.Game;

/**
 * The Message object is put into the MsgSystem. It contains the message, the sender and the importance.
 * @author Benedikt
 */
public class Msg {
    private String fmessage;
    private String sender = "System";
    private int importance = 1;
    
    
    Msg(String pmessage, String psender, int imp) {
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
     * 
     * @param imp
     */
    public void setImportance(int imp){
        if ((imp>=0) && (imp<=100))
            importance = imp;    
    }
}
