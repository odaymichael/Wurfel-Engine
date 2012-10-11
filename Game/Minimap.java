/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;
   
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int X;
    private int Y;
    private final float scalefactor= 8;
    
    Minimap() {
        X = GameplayState.gc.getWidth() - 400;
        Y = 10;
    } 
            
    void draw(Graphics g) {
        for (int pos=0;pos < 9;pos++){
                 
//            g.setColor(Color.white);
//            g.fillRect(
//                X + pos%3 *(Chunk.BlocksX*scalefactor),
//                Y +pos/3*(Chunk.BlocksY*scalefactor/4),
//                Chunk.BlocksX*scalefactor,
//                Chunk.BlocksY*scalefactor/4
//            );
//            
        for (int x = Chunk.BlocksX*(pos%3); x < Chunk.BlocksX * (pos%3+1); x++){
            for (int y = Chunk.BlocksY*(pos/3); y < Chunk.BlocksY * (pos/3+1); y++){
                Color temp = Block.Blocksheet.getSubImage(
                        Controller.map.data[x][y][Controller.player.coordZ-1].spritex,
                        Controller.map.data[x][y][Controller.player.coordZ-1].spritey
                    ).getColor(
                        Block.displBlockWidth/2,
                        Block.displBlockHeight/4
                );

                g.setColor(temp);
                g.fillRect(X + x*scalefactor,Y + y*scalefactor/4, scalefactor, scalefactor/4);
            }
        }

        g.setColor(Color.black);
        g.drawRect(
            X + pos%3 *(Chunk.BlocksX*scalefactor),
            Y + pos/3*(Chunk.BlocksY*scalefactor/4),
            Chunk.BlocksX*scalefactor,
            Chunk.BlocksY*scalefactor/4
        );

        View.tTFont.drawString(
            X + 10 + pos%3*Chunk.BlocksX*scalefactor,
            Y +10 + pos/3*(Chunk.BlocksY*scalefactor/4),
            Controller.map.coordlistX[pos] +" | "+ Controller.map.coordlistY[pos],
            Color.black
        );
    }

    //camera rectangle
    g.setColor(Color.green);
    g.drawRect(
            X + scalefactor * GameplayState.View.cameraX * Chunk.BlocksX / Chunk.SizeX,
            Y + scalefactor * GameplayState.View.cameraY * (Chunk.BlocksY/2+Chunk.BlocksZ)/2 /(Chunk.SizeY + Chunk.SizeZ),
            GameplayState.View.cameraWidth * (Chunk.BlocksX*scalefactor / Chunk.SizeX),
            GameplayState.View.cameraHeight * ((Chunk.BlocksY*scalefactor/4)/Chunk.SizeY)
    );

    //player coord and pos
    g.setColor(Color.blue);
    g.drawRect(
        X+ scalefactor * GameplayState.Controller.player.getRelCoordX(),
        Y+ scalefactor * (GameplayState.Controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
        scalefactor,
        scalefactor
    );

    //player coord
    g.setColor(Color.blue);
    View.tTFont.drawString(
        X+ scalefactor * GameplayState.Controller.player.getRelCoordX(),
        Y+ scalefactor * (GameplayState.Controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
        Controller.player.getRelCoordX() +" | "+ Controller.player.getRelCoordY() +" | "+ Controller.player.coordZ,
        Color.blue
    );

    //player pos
    View.tTFont.drawString(
        X+ scalefactor * GameplayState.Controller.player.getRelCoordX(),
        Y+ scalefactor * (GameplayState.Controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2)+20,
        Controller.player.posX +" | "+ Controller.player.posY +" | "+ Controller.player.posZ,
        Color.red
    );

    //camera pos
    View.tTFont.drawString(
            GameplayState.gc.getWidth()- 100,
            15,
            GameplayState.View.cameraX +" | "+ GameplayState.View.cameraY,
            Color.white
        );
  
    }
 
    
}
