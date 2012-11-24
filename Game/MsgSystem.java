/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

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
     * 
     * @param msg
     */
    public void add(String msg) {
        add(new Msg(msg, "System",100));
    }
    
    /**
     * 
     * @param msg
     * @param psender
     */
    public void add(String msg, String psender){
        add(new Msg(msg, psender,100));
    }
    
    /**
     * 
     * @param msg
     * @param psender
     * @param imp
     */
    public void add(String msg, String psender, int imp){
        if (imp>100) imp=100;
        add(new Msg(msg, psender,imp));
    }
    
    /**
     * 
     * @param delta
     */
    public void update(int delta){
       //decrease importance
       timelastupdate += delta;
       //delay 30*100 = 3000ms
       if (timelastupdate >= 30) {
            timelastupdate = 0;
            for (int i=0;i<size();i++) {
                Msg temp = (Msg) get(i);
                if (temp.getImportance()>0)
                    temp.setImportance(temp.getImportance()-1); 
                    else remove(i);
            }
        }
    }
    
    public void draw(){
        if (waitforinput) Gameplay.view.g.drawString("MSG:", Gameplay.gc.getWidth()/2, 3*Gameplay.gc.getHeight()/4);
        
        for (int i=0;i < Gameplay.msgSystem.size();i++){
            Msg msg = (Msg) Gameplay.msgSystem.get(i);
            Color clr = Color.blue;
            if ("System".equals(msg.getSender())) clr = Color.green;
            
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
