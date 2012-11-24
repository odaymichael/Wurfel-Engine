package Game;
   
import Game.Blocks.Block;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int X;
    private int Y;
    private final float scaleX = 8;
    private final float scaleY = 8;
    
    public Minimap() {
        X = Gameplay.gc.getWidth() - 400;
        Y = 10;
    } 
            
    public void draw(Graphics g) {
        for (int pos=0;pos < 9;pos++){
                 
//            g.setColor(Color.white);
//            g.fillRect(
//                x + pos%3 *(Chunk.BlocksX*scalefactor),
//                Y +pos/3*(Chunk.BlocksY*scalefactor/4),
//                Chunk.BlocksX*scalefactor,
//                Chunk.BlocksY*scalefactor/4
//            );
//            
        Color temp;
        for (int x = Chunk.BlocksX*(pos%3); x < Chunk.BlocksX * (pos%3+1); x++){
            for (int y = Chunk.BlocksY*(pos/3); y < Chunk.BlocksY * (pos/3+1); y++){
                if (Gameplay.controller.rendermethod){
                    Block block = Controller.map.data[x][y][Gameplay.controller.player.coordZ-1];
                    temp = Block.Blocksheet.getSubImage(
                            Block.SidesSprites[block.getId()][block.getValue()][1][0],
                            Block.SidesSprites[block.getId()][block.getValue()][1][1]
                        ).getColor(
                            Block.displWidth/2,
                            Block.displHeight/2
                    );                
                } else
                    temp = Block.Blocksheet.getSubImage(
                        Controller.map.data[x][y][Gameplay.controller.player.coordZ-1].spriteX[0],
                        Controller.map.data[x][y][Gameplay.controller.player.coordZ-1].spriteY[0]
                    ).getColor(
                        Block.displWidth/2,
                        Block.displHeight/4
                );

                g.setColor(temp);
                g.fillRect(
                    X + (x + (y%2==1?0.5f:0) ) * scaleX,
                    Y + y*scaleY,
                    scaleX,
                    scaleY
                );
            }
        }
        
        //player
        g.setColor(Color.blue);
        g.drawRect(
            X + (Gameplay.controller.player.getRelCoordX() + (Gameplay.controller.player.getRelCoordY()%2==1?0.5f:0) ) * scaleX,
            Y + Gameplay.controller.player.getRelCoordY() * scaleY,
            scaleX,
            scaleY
        );
    

        //Chunk outline
        g.setColor(Color.black);
        g.drawRect(
            X + pos%3 *(Chunk.BlocksX*scaleX),
            Y + pos/3*(Chunk.BlocksY*scaleY),
            Chunk.BlocksX*scaleX,
            Chunk.BlocksY*scaleY
        );

        View.tTFont.drawString(
            X + 10 + pos%3*Chunk.BlocksX*scaleX,
            Y +10 + pos/3*(Chunk.BlocksY*scaleY),
            Controller.map.getCoordlistX(pos) +" | "+ Controller.map.getCoordlistY(pos) ,
            Color.black
        );
    }

    //bottom camera rectangle
    g.setColor(Color.green);
    g.drawRect(
        X + scaleX * Gameplay.view.camera.x * Chunk.BlocksX / Chunk.SizeX,
        Y + scaleY * Gameplay.view.camera.y * (Chunk.BlocksY/2 + Chunk.BlocksZ) /(Chunk.SizeY + Chunk.SizeZ),
        Gameplay.view.camera.width * (Chunk.BlocksX*scaleX / Chunk.SizeX),
        Gameplay.view.camera.height * ((Chunk.BlocksY*scaleY)/Chunk.SizeY)
    );
    
    //player level camera rectangle
    g.setColor(Color.white);
    g.drawRect(
        X + scaleX * Gameplay.view.camera.x * Chunk.BlocksX / Chunk.SizeX,
        Y + scaleY * Gameplay.view.camera.y * (Chunk.BlocksY/2 + Chunk.BlocksZ) /(Chunk.SizeY + Chunk.SizeZ)
          + scaleY *(Gameplay.controller.player.coordZ * Block.height) * (Chunk.BlocksZ / (float) Chunk.SizeZ),
        Gameplay.view.camera.width * (Chunk.BlocksX*scaleX / Chunk.SizeX),
        Gameplay.view.camera.height * ((Chunk.BlocksY*scaleY)/Chunk.SizeY)
    );


    //player coord
    g.setColor(Color.blue);
    View.tTFont.drawString(
        X + (Gameplay.controller.player.getRelCoordX() + (Gameplay.controller.player.getRelCoordY()%2==1?0.5f:0) ) * scaleX+20,
        Y + Gameplay.controller.player.getRelCoordY() * scaleY - 50,
        Gameplay.controller.player.getRelCoordX() +" | "+ Gameplay.controller.player.getRelCoordY() +" | "+ Gameplay.controller.player.coordZ,
        Color.blue
    );

    //player pos
    View.tTFont.drawString(
        X + (Gameplay.controller.player.getRelCoordX() + (Gameplay.controller.player.getRelCoordY()%2==1?0.5f:0) ) * scaleX+20,
        Y + Gameplay.controller.player.getRelCoordY() * scaleY - 30,
        Gameplay.controller.player.posX +" | "+ Gameplay.controller.player.posY +" | "+ Gameplay.controller.player.posZ,
        Color.red
    );

    //camera pos
    View.tTFont.drawString(
            Gameplay.gc.getWidth()- 100,
            15,
            Gameplay.view.camera.x +" | "+ Gameplay.view.camera.y,
            Color.white
        );
    }
}
