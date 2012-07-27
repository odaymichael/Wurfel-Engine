/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Benedikt
 */
/**
 * 
 * Dies ist ein Javadoc test
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
