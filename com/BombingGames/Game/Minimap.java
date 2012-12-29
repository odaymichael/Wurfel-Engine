package com.BombingGames.Game;
   

import com.BombingGames.Game.Blocks.Block;
import org.newdawn.slick.Color;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int X;
    private int Y;
    private final float scaleX = 12;//8
    private final float scaleY = 6;//4
    
    /**
     * Creates a Minimap. At the momen it is primary for development purposes.
     */
    public Minimap() {
    } 
            
    /**
     * Draw the Minimap
     */
    public void draw() {
        Y = Gameplay.view.getCamera().getScreenY() + 10;
        X = Gameplay.view.getCamera().getScreenX() + Gameplay.view.getCamera().getScreenWidth() - (int) (3*Chunk.getBlocksX()*scaleX) - 10;
        int z;
        if (Gameplay.controller.getPlayer()!=null)
            z=Gameplay.controller.getPlayer().getCoordZ()-1;
            else z=0;
        
        for (int pos=0;pos < 9;pos++){  
            Color temp;
            for (int x = Chunk.getBlocksX()*(pos%3); x < Chunk.getBlocksX() * (pos%3+1); x++){
                for (int y = Chunk.getBlocksY()*(pos/3); y < Chunk.getBlocksY() * (pos/3+1); y++){
                    if (Gameplay.controller.renderSides()){
                        Block block = Controller.getMapData(x, y, z);
                        temp = Block.getBlocksheet().getSubImage(
                                Block.SidesSprites[block.getId()][block.getValue()][1][0],
                                Block.SidesSprites[block.getId()][block.getValue()][1][1]
                            ).getColor(
                                Block.WIDTH/2,
                                Block.HEIGHT/2
                        );                
                    } else
                        temp = Block.getBlocksheet().getSubImage(
                            Controller.getMapData(x, y, z).spriteX[0],
                            Controller.getMapData(x, y, z).spriteY[0]
                        ).getColor(
                            Block.WIDTH/2,
                            Block.HEIGHT/2
                    );

                    Gameplay.view.g.setColor(temp);
                    Gameplay.view.g.fillRect(
                        X + (x + (y%2==1?0.5f:0) ) * scaleX,
                        Y + y*scaleY,
                        scaleX,
                        scaleY
                    );
                }
            }

            //player
            if (Gameplay.controller.getPlayer()!=null){
                Gameplay.view.g.setColor(Color.blue);
                Gameplay.view.g.drawRect(
                    X + (Gameplay.controller.getPlayer().getCoordX() + (Gameplay.controller.getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX,
                    Y + Gameplay.controller.getPlayer().getCoordY() * scaleY,
                    scaleX,
                    scaleY
                );
            }


            //Chunk outline
            Gameplay.view.g.setColor(Color.black);
            Gameplay.view.g.drawRect(
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
        Gameplay.view.g.setColor(Color.green);
        Gameplay.view.g.drawRect(
            X + scaleX * Gameplay.view.getCamera().getX() / Block.WIDTH,
            Y + scaleY * Gameplay.view.getCamera().getY() / Block.HEIGHT,
            scaleX*Gameplay.view.getCamera().getWidth() / Block.WIDTH,
            scaleY*Gameplay.view.getCamera().getHeight() / Block.HEIGHT
        );

        if (Gameplay.controller.getPlayer()!=null){
            //player level getCamera() rectangle
            Gameplay.view.g.setColor(Color.gray);
            Gameplay.view.g.drawRect(
                X + scaleX * Gameplay.view.getCamera().getX() / Block.WIDTH,
                Y + scaleY * Gameplay.view.getCamera().getY() / Block.HEIGHT
                + scaleY *2*(Gameplay.controller.getPlayer().getCoordZ() * Block.HEIGHT)/ (float) (Block.WIDTH),
                scaleX*Gameplay.view.getCamera().getWidth() / Block.WIDTH,
                scaleY*Gameplay.view.getCamera().getHeight() / Block.HEIGHT
            );
        }

        //top level getCamera() rectangle
        Gameplay.view.g.setColor(Color.white);
        Gameplay.view.g.drawRect(
            X + scaleX * Gameplay.view.getCamera().getX() / Block.WIDTH,
            Y + scaleY * Gameplay.view.getCamera().getY() / Block.HEIGHT
            + scaleY *2*(Chunk.getBlocksZ() * Block.HEIGHT)/ (float) (Block.WIDTH),
            scaleX*Gameplay.view.getCamera().getWidth() / Block.WIDTH,
            scaleY*Gameplay.view.getCamera().getHeight() / Block.HEIGHT
        );

        if (Gameplay.controller.getPlayer()!=null){
            View.baseFont.drawString(
                    X + scaleX * Gameplay.view.getCamera().getX() / Block.WIDTH
                    + scaleX*Gameplay.view.getCamera().getWidth() / Block.WIDTH,
                    Y + scaleY * Gameplay.view.getCamera().getY() / Block.HEIGHT
                    + scaleY *2*(Gameplay.controller.getPlayer().getCoordZ() * Block.HEIGHT)/ (float) (Block.WIDTH)
                    + scaleY*Gameplay.view.getCamera().getHeight() / Block.HEIGHT,
                    Gameplay.view.getCamera().getRightBorder() +" | "+ Gameplay.view.getCamera().getBottomBorder() ,
                    Color.black
                );

            //player coord
            Gameplay.view.g.setColor(Color.blue);
            View.baseFont.drawString(
                X + (Gameplay.controller.getPlayer().getCoordX() + (Gameplay.controller.getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.controller.getPlayer().getCoordY() * scaleY - 50,
                Gameplay.controller.getPlayer().getCoordX() +" | "+ Gameplay.controller.getPlayer().getCoordY() +" | "+ Gameplay.controller.getPlayer().getCoordZ(),
                Color.blue
            );
            
            //player coord
            Gameplay.view.g.setColor(Color.blue);
            View.baseFont.drawString(
                X + (Gameplay.controller.getPlayer().getCoordX() + (Gameplay.controller.getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.controller.getPlayer().getCoordY() * scaleY - 30,
                Gameplay.controller.getPlayer().getAbsCoordX() +" | "+ Gameplay.controller.getPlayer().getAbsCoordY() +" | "+ Gameplay.controller.getPlayer().getCoordZ(),
                Color.blue
            );


            //player pos
            View.baseFont.drawString(
                X + (Gameplay.controller.getPlayer().getCoordX() + (Gameplay.controller.getPlayer().getCoordY()%2==1?0.5f:0) ) * scaleX+20,
                Y + Gameplay.controller.getPlayer().getCoordY() * scaleY - 10,
                Gameplay.controller.getPlayer().getPosX() +" | "+ Gameplay.controller.getPlayer().getPosY() +" | "+ Gameplay.controller.getPlayer().getPosZ(),
                Color.red
            );
        }

        //getCamera() pos
        View.baseFont.drawString(
                X ,
                Y + 3*Chunk.getBlocksY()*scaleY + 15,
                Gameplay.view.getCamera().getX() +" | "+ Gameplay.view.getCamera().getY(),
                Color.white
            );
        }
}
