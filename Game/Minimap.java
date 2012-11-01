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
    
    public Minimap() {
        X = Gameplay.gc.getWidth() - 400;
        Y = 10;
    } 
            
    public void draw(Graphics g) {
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
                        Controller.map.data[x][y][Gameplay.controller.player.coordZ-1].spriteX[0],
                        Controller.map.data[x][y][Gameplay.controller.player.coordZ-1].spriteY[0]
                    ).getColor(
                        Block.displWidth/2,
                        Block.displHeight/4
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
            Controller.map.getCoordlistX(pos) +" | "+ Controller.map.getCoordlistY(pos) ,
            Color.black
        );
    }

    //camera rectangle
    g.setColor(Color.green);
    g.drawRect(
            X + scalefactor * Gameplay.view.cameraX * Chunk.BlocksX / Chunk.SizeX,
            Y + scalefactor * Gameplay.view.cameraY * (Chunk.BlocksY/2+Chunk.BlocksZ)/2 /(Chunk.SizeY + Chunk.SizeZ),
            Gameplay.view.cameraWidth * (Chunk.BlocksX*scalefactor / Chunk.SizeX),
            Gameplay.view.cameraHeight * ((Chunk.BlocksY*scalefactor/4)/Chunk.SizeY)
    );

    //player coord and pos
    g.setColor(Color.blue);
    g.drawRect(
        X+ scalefactor * Gameplay.controller.player.getRelCoordX(),
        Y+ scalefactor * (Gameplay.controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
        scalefactor,
        scalefactor
    );

    //player coord
    g.setColor(Color.blue);
    View.tTFont.drawString(
        X+ scalefactor * Gameplay.controller.player.getRelCoordX(),
        Y+ scalefactor * (Gameplay.controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2),
        Gameplay.controller.player.getRelCoordX() +" | "+ Gameplay.controller.player.getRelCoordY() +" | "+ Gameplay.controller.player.coordZ,
        Color.blue
    );

    //player pos
    View.tTFont.drawString(
        X+ scalefactor * Gameplay.controller.player.getRelCoordX(),
        Y+ scalefactor * (Gameplay.controller.player.getRelCoordY()/2 - Chunk.BlocksZ/2)+20,
        Gameplay.controller.player.posX +" | "+ Gameplay.controller.player.posY +" | "+ Gameplay.controller.player.posZ,
        Color.red
    );

    //camera pos
    View.tTFont.drawString(
            Gameplay.gc.getWidth()- 100,
            15,
            Gameplay.view.cameraX +" | "+ Gameplay.view.cameraY,
            Color.white
        );
    }
}
