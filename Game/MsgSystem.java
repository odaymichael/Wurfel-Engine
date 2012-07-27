/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 *
 * @author Benedikt
 */
public class MsgSystem extends ArrayList {

    public MsgSystem(){
    }
        
    public void add(String msg) {
        add(new Msg(msg, "System",100));
    }
    
    public void add(String msg, String psender){
        add(new Msg(msg, psender,100));
    }
    
    public void add(String msg, String psender, int imp){
        if (imp>100) imp=100;
        add(new Msg(msg, psender,imp));
    }
    
    public void update(){
        //decrease importance
        for (int i=0;i<size();i++){
            Msg temp = (Msg) get(i);
            if (temp.getImportance()>0)
                temp.setImportance(temp.getImportance()-1); 
            else remove(i);
        }
        
    }
}
