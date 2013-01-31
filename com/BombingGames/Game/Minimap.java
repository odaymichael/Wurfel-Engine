package com.BombingGames.Game;
   

import com.BombingGames.Game.Blocks.Block;
import org.newdawn.slick.Color;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int X, Y;
    private final float scaleX = 12;//8
    private final float scaleY = 6;//4
            
    /**
     * Draw the Minimap
     */
    public void draw() {
        //refresh position
        Y = Gameplay.getView().getCamera().getScreenY() + 10;
        X = Gameplay.getView().getCamera().getScreenX() + Gameplay.getView().getCamera().getScreenWidth() - (int) (3*Chunk.getBlocksX()*scaleX) - 10;
        int z;
        
        //if there is no player => ground level
        if (Gameplay.getController().getPlayer()!=null)
            z = Gameplay.getController().getPlayer().getCoordZ()-1;
        else z = 0;
        
        for (int pos=0;pos < 9;pos++){  
            for (int x = Chunk.getBlocksX()*(pos%3); x < Chunk.getBlocksX() * (pos%3+1); x++){
                for (int y = Chunk.getBlocksY()*(pos/3); y < Chunk.getBlocksY() * (pos/3+1); y++){
                    Block block = Controller.getMapData(x, y, z);               

                    Gameplay.getView().g.setColor(Block.getBlockColor(block.getId(), block.getValue()));
                    Gameplay.getView().g.fillRect(
                        X + (x + (y%2==1 ? 0.5f : 0) ) * scaleX,
                        Y + y*scaleY,
                        scaleX,
                        scaleY
                    );
                }
            }

            //player
            if (Gameplay.getController().getPlayer()!=null){
                Gameplay.getView().g.setColor(Color.blue);
                Gameplay.getView().g.drawRect(
                    X + (Gameplay.getController().getPlayer().getCoordX() + (Gameplay.getController().getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX,
                    Y + Gameplay.getController().getPlayer().getCoordY() * scaleY,
                    scaleX,
                    scaleY
                );
            }


            //Chunk outline
            Gameplay.getView().g.setColor(Color.black);
            Gameplay.getView().g.drawRect(
                X + pos%3 *(Chunk.getBlocksX()*scaleX),
                Y + pos/3*(Chunk.getBlocksY()*scaleY),
                Chunk.getBlocksX()*scaleX,
                Chunk.getBlocksY()*scaleY
            );

            View.baseFont.drawString(
                X + 10 + pos%3*Chunk.getBlocksX()*scaleX,
                Y +10 + pos/3*(Chunk.getBlocksY()*scaleY),
                Controller.getMap().getCoordlist(pos)[0] +" | "+ Controller.getMap().getCoordlist(pos)[1] ,
                Color.black
            );
        }

        //bottom getCamera() rectangle
        Gameplay.getView().g.setColor(Color.green);
        Gameplay.getView().g.drawRect(
            X + scaleX * Gameplay.getView().getCamera().getX() / Block.WIDTH,
            Y + scaleY * Gameplay.getView().getCamera().getY() / (Block.HEIGHT/2),
            scaleX*Gameplay.getView().getCamera().getWidth() / Block.WIDTH,
            scaleY*Gameplay.getView().getCamera().getGroundHeight() / (Block.HEIGHT/2)
        );

        if (Gameplay.getController().getPlayer()!=null){
            //player level getCamera() rectangle
            Gameplay.getView().g.setColor(Color.gray);
            Gameplay.getView().g.drawRect(
                X + scaleX * Gameplay.getView().getCamera().getX() / Block.WIDTH,
                Y + scaleY * Gameplay.getView().getCamera().getY() / (Block.HEIGHT/2)
                + scaleY *2*(Gameplay.getController().getPlayer().getCoordZ() * (Block.HEIGHT/2))/ (float) (Block.WIDTH),
                scaleX*Gameplay.getView().getCamera().getWidth() / Block.WIDTH,
                scaleY*Gameplay.getView().getCamera().getGroundHeight() / (Block.HEIGHT/2)
            );
        }

        //top level getCamera() rectangle
        Gameplay.getView().g.setColor(Color.white);
        Gameplay.getView().g.drawRect(
            X + scaleX * Gameplay.getView().getCamera().getX() / Block.WIDTH,
            Y + scaleY * Gameplay.getView().getCamera().getY() / (Block.HEIGHT/2)
            + scaleY *2*(Chunk.getBlocksZ() * Block.HEIGHT)/ (float) (Block.WIDTH),
            scaleX*Gameplay.getView().getCamera().getWidth() / Block.WIDTH,
            scaleY*Gameplay.getView().getCamera().getGroundHeight() / (Block.HEIGHT/2)
        );

        if (Gameplay.getController().getPlayer()!=null){
            View.baseFont.drawString(
                    X + scaleX * Gameplay.getView().getCamera().getX() / Block.WIDTH
                    + scaleX*Gameplay.getView().getCamera().getWidth() / Block.WIDTH,
                    Y + scaleY * Gameplay.getView().getCamera().getY() / Block.HEIGHT
                    + scaleY *2*(Gameplay.getController().getPlayer().getCoordZ() * Block.HEIGHT)/ (float) (Block.WIDTH)
                    + scaleY*Gameplay.getView().getCamera().getGroundHeight() / Block.HEIGHT,
                    Gameplay.getView().getCamera().getRightBorder() +" | "+ Gameplay.getView().getCamera().getBottomBorder() ,
                    Color.black
                );

            //player coord
            Gameplay.getView().g.setColor(Color.blue);
            View.baseFont.drawString(
                X + (Gameplay.getController().getPlayer().getCoordX() + (Gameplay.getController().getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.getController().getPlayer().getCoordY() * scaleY - 50,
                Gameplay.getController().getPlayer().getCoordX() +" | "+ Gameplay.getController().getPlayer().getCoordY() +" | "+ Gameplay.getController().getPlayer().getCoordZ(),
                Color.blue
            );
            
            //player coord
            Gameplay.getView().g.setColor(Color.blue);
            View.baseFont.drawString(
                X + (Gameplay.getController().getPlayer().getCoordX() + (Gameplay.getController().getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.getController().getPlayer().getCoordY() * scaleY - 30,
                Gameplay.getController().getPlayer().getAbsCoordX() +" | "+ Gameplay.getController().getPlayer().getAbsCoordY() +" | "+ Gameplay.getController().getPlayer().getCoordZ(),
                Color.blue
            );


            //player pos
            View.baseFont.drawString(
                X + (Gameplay.getController().getPlayer().getCoordX() + (Gameplay.getController().getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.getController().getPlayer().getCoordY() * scaleY - 10,
                Gameplay.getController().getPlayer().getPosX() +" | "+ Gameplay.getController().getPlayer().getPosY() +" | "+ Gameplay.getController().getPlayer().getPosZ(),
                Color.red
            );
        }

        //getCamera() pos
        View.baseFont.drawString(
                X ,
                Y + 3*Chunk.getBlocksY()*scaleY + 15,
                Gameplay.getView().getCamera().getX() +" | "+ Gameplay.getView().getCamera().getY(),
                Color.white
            );
        }
}
