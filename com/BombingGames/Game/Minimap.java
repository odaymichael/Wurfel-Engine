package com.BombingGames.Game;
   

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Wurfelengine;
import org.newdawn.slick.Color;

/**
 *
 * @author Benedikt
 */
public class Minimap {
    private int posX, posY;
    private final float scaleX = 12;//8
    private final float scaleY = 6;//4
    private Controller controller;

    public Minimap(Controller controller) {
        this.controller = controller;
    }
    
    
    /**
     * Draws the Minimap
     * @param screenX The distance of the right side.
     * @param screenY Distance from top.
     */
    public void render(int screenX, int screenY) {
        this.posX = screenX - (int) (Map.getBlocksX()*scaleX);
        this.posY = screenY;
        int z;        
        //if there is no player => ground level
        if (controller.getPlayer()!=null)
            z = controller.getPlayer().getRelCoords()[2]-1;
        else z = 0;
        
        //render a chunk
        for (int pos=0; pos < 9; pos++){  
            for (int x = Chunk.getBlocksX()*(pos%3); x < Chunk.getBlocksX() * (pos%3+1); x++){
                for (int y = Chunk.getBlocksY()*(pos/3); y < Chunk.getBlocksY() * (pos/3+1); y++){
                    Block block = Controller.getMapDataSafe(x, y, z);               
                    if (!block.isHidden()){
                        Wurfelengine.getGraphics().setColor(Block.getRepresentingColor(block.getId(), block.getValue()));
                        Wurfelengine.getGraphics().fillRect(
                            this.posX + (x + (y%2==1 ? 0.5f : 0) ) * scaleX,
                            this.posY + y*scaleY,
                            scaleX,
                            scaleY
                        );
                    }
                }
            }

            //player
            if (controller.getPlayer()!=null){
                Wurfelengine.getGraphics().setColor(Color.blue);
                Wurfelengine.getGraphics().drawRect(
                    posX + (controller.getPlayer().getRelCoords()[0] + (controller.getPlayer().getRelCoords()[1]%2==1?0.5f:0) ) * scaleX,
                    posY + controller.getPlayer().getRelCoords()[1] * scaleY,
                    scaleX,
                    scaleY
                );
            }


            //Chunk outline
            Wurfelengine.getGraphics().setColor(Color.black);
            Wurfelengine.getGraphics().drawRect(
                posX + pos%3 *(Chunk.getBlocksX()*scaleX),
                posY + pos/3*(Chunk.getBlocksY()*scaleY),
                Chunk.getBlocksX()*scaleX,
                Chunk.getBlocksY()*scaleY
            );

            View.getFont().drawString(
                posX + 10 + pos%3 *Chunk.getBlocksX()*scaleX,
                posY + 10 + pos/3 *(Chunk.getBlocksY()*scaleY),
                Controller.getMap().getChunkCoords(pos)[0] +" | "+ Controller.getMap().getChunkCoords(pos)[1] ,
                Color.black
            );
        }

        //bottom getCamera() rectangle
        Wurfelengine.getGraphics().setColor(Color.green);
        Wurfelengine.getGraphics().drawRect(
            posX + scaleX * Gameplay.getView().getCamera().getOutputPosX() / Block.DIMENSION,
            posY + scaleY * Gameplay.getView().getCamera().getOutputPosY() / (Block.DIM2/2),
            scaleX*Gameplay.getView().getCamera().getOutputWidth() / Block.DIMENSION,
            scaleY*Gameplay.getView().getCamera().getOutputHeight() / (Block.DIM2/2)
        );

        if (controller.getPlayer()!=null){
            //player level getCamera() rectangle
            Wurfelengine.getGraphics().setColor(Color.gray);
            Wurfelengine.getGraphics().drawRect(
                posX + scaleX * Gameplay.getView().getCamera().getOutputPosX() / Block.DIMENSION,
                posY + scaleY * Gameplay.getView().getCamera().getOutputPosY() / (Block.DIM2/2)
                + scaleY *2*(controller.getPlayer().getRelCoords()[2] * (Block.DIM2/2))/ (float) (Block.DIMENSION),
                scaleX*Gameplay.getView().getCamera().getOutputWidth() / Block.DIMENSION,
                scaleY*Gameplay.getView().getCamera().getOutputHeight() / (Block.DIM2/2)
            );
        }

        //top level getCamera() rectangle
        Wurfelengine.getGraphics().setColor(Color.white);
        Wurfelengine.getGraphics().drawRect(
            posX + scaleX * Gameplay.getView().getCamera().getOutputPosX() / Block.DIMENSION,
            posY + scaleY * Gameplay.getView().getCamera().getOutputPosY() / (Block.DIM2/2)
            + scaleY *2*(Chunk.getBlocksZ() * Block.DIM2)/ (float) (Block.DIMENSION),
            scaleX*Gameplay.getView().getCamera().getOutputWidth() / Block.DIMENSION,
            scaleY*Gameplay.getView().getCamera().getOutputHeight() / (Block.DIM2/2)
        );

        if (controller.getPlayer()!=null){
            View.getFont().drawString(
                    posX + scaleX * Gameplay.getView().getCamera().getOutputPosX() / Block.DIMENSION
                    + scaleX*Gameplay.getView().getCamera().getOutputWidth() / Block.DIMENSION,
                    posY + scaleY * Gameplay.getView().getCamera().getOutputPosY() / Block.DIM2
                    + scaleY *2*(controller.getPlayer().getRelCoords()[2] * Block.DIM2)/ (float) (Block.DIMENSION)
                    + scaleY*Gameplay.getView().getCamera().getOutputHeight() / Block.DIM2,
                    Gameplay.getView().getCamera().getRightBorder() +" | "+ Gameplay.getView().getCamera().getBottomBorder() ,
                    Color.black
                );

            //player coord
            Wurfelengine.getGraphics().setColor(Color.blue);
            View.getFont().drawString(
                posX + (controller.getPlayer().getRelCoords()[0] + (controller.getPlayer().getRelCoords()[1] %2==1?0.5f:0) ) * scaleX+20,
                posY + controller.getPlayer().getRelCoords()[1] * scaleY - 50,
                controller.getPlayer().getRelCoords()[0] +" | "+ controller.getPlayer().getRelCoords()[1] +" | "+ controller.getPlayer().getRelCoords()[2],
                Color.blue
            );
            
            //player coord
            Wurfelengine.getGraphics().setColor(Color.blue);
            View.getFont().drawString(
                posX + (controller.getPlayer().getRelCoords()[0] + (controller.getPlayer().getRelCoords()[1]%2==1?0.5f:0) ) * scaleX+20,
                posY + controller.getPlayer().getRelCoords()[1] * scaleY - 30,
                controller.getPlayer().getRelCoords()[0] +" | "+ controller.getPlayer().getRelCoords()[1] +" | "+ controller.getPlayer().getRelCoords()[2],
                Color.blue
            );


            //player pos
            View.getFont().drawString(
                posX + (controller.getPlayer().getRelCoords()[0] + (controller.getPlayer().getRelCoords()[1]%2==1?0.5f:0) ) * scaleX+20,
                posY + controller.getPlayer().getRelCoords()[1] * scaleY - 10,
                controller.getPlayer().getPos()[0] +" | "+ controller.getPlayer().getPos()[1] +" | "+ controller.getPlayer().getPos()[2],
                Color.red
            );
        }

        //getCamera() pos
        View.getFont().drawString(
                posX ,
                posY + 3*Chunk.getBlocksY()*scaleY + 15,
                Gameplay.getView().getCamera().getOutputPosX() +" | "+ Gameplay.getView().getCamera().getOutputPosY(),
                Color.white
            );
        }
}
