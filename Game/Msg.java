/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

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
    
    public String getMessage(){
        return fmessage;    
    }
    
    public String getSender(){
        return sender;
    }
    
    public int getImportance(){
        return importance;
    }
  
    public void setImportance(int imp){
        if ((imp>=0) && (imp<=100))
            importance = imp;    
    }
}
