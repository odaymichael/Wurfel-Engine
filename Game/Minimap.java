/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;
   
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int X;
    private int Y;
    private GameContainer gc;
    
    Minimap(GameContainer gc) {
     this.gc = gc;   
    } 
            
    void draw(Graphics g) {
        for (int pos=0;pos < 9;pos++){
            g.setColor(Color.white);
            g.fillRect(
                    gc.getScreenWidth() - 300 + pos%3 *60,
                    pos/3*60+10,
                    60,
                    60
                );
            
            g.setColor(Color.black);
            g.drawRect(
                gc.getScreenWidth() - 300 + pos%3 *60,
                pos/3*60+10,
                60,
                60
            );

            g.setColor(Color.black);
            View.tTFont.drawString(
                gc.getScreenWidth()- 290 + pos%3*60,
                pos/3*60+20,
                Controller.map.coordlistX[pos] +" | "+ Controller.map.coordlistY[pos],
                Color.black
            );
            g.setColor(Color.red);
            g.drawRect(
                gc.getScreenWidth() - 300 + (View.cameraX * (60f/Chunk.SizeX)),
                20 + (View.cameraY * (60f/(Chunk.SizeY + Chunk.SizeZ))),
                View.cameraWidth * (60f/Chunk.SizeX),
                -View.cameraHeight * (60f/Chunk.SizeY)
            );      
        }
    }
 
    
}
