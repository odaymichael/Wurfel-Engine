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
    private final float size= 8;
    private GameContainer gc;
    
    Minimap(GameContainer gc) {
        this.gc = gc;
        X = gc.getScreenWidth() - 400;
        Y = 10;
    } 
            
    void draw(Graphics g) {
        for (int pos=0;pos < 9;pos++){
            g.setColor(Color.white);
            g.fillRect(
                X + pos%3 *(Chunk.BlocksX*size),
                Y +pos/3*(Chunk.BlocksY*size/4),
                Chunk.BlocksX*size,
                Chunk.BlocksY*size/4
            );
            
            g.setColor(Color.black);
            g.drawRect(
                X + pos%3 *(Chunk.BlocksX*size),
                Y +pos/3*(Chunk.BlocksY*size/4),
                Chunk.BlocksX*size,
                Chunk.BlocksY*size/4
            );
            
            View.tTFont.drawString(
                X + 10 + pos%3*Chunk.BlocksX*size,
                Y +10 + pos/3*(Chunk.BlocksY*size/4),
                Controller.map.coordlistX[pos] +" | "+ Controller.map.coordlistY[pos],
                Color.black
            );
        }
        
        //camera rectangle
        g.setColor(Color.green);
        g.drawRect(
                X + size * GameplayState.View.cameraX * Chunk.BlocksX / Chunk.SizeX,
                Y + size * GameplayState.View.cameraY * (Chunk.BlocksY/2+Chunk.BlocksZ)/2 /(Chunk.SizeY + Chunk.SizeZ),
                GameplayState.View.cameraWidth * (Chunk.BlocksX*size / Chunk.SizeX),
                GameplayState.View.cameraHeight * ((Chunk.BlocksY*size/4)/Chunk.SizeY)
        );
        
        //player pos
        g.setColor(Color.blue);
        g.drawRect(
            X+ size * GameplayState.Controller.player.getRelCoordX(),
            Y+ size * (GameplayState.Controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
            size,
            size
        );
        
        //player pos
        g.setColor(Color.blue);
        View.tTFont.drawString(
            X+ size * GameplayState.Controller.player.getRelCoordX(),
            Y+ size * (GameplayState.Controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
            GameplayState.Controller.player.getRelCoordX() +" | "+ GameplayState.Controller.player.getRelCoordY(),
            Color.blue
        );
        
        //camera pos
        View.tTFont.drawString(
                gc.getScreenWidth()- 100,
                15,
                GameplayState.View.cameraX +" | "+ GameplayState.View.cameraY,
                Color.white
            );
            
        
    }
 
    
}
