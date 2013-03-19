package com.BombingGames.Game;
   

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Wurfelengine;
import org.newdawn.slick.Color;

/**
 *A minimap is a view that draws the map from top in a small window.
 * @author Benedikt
 */
public class Minimap {
    private int posX, posY;
    private final float scaleX = 12;//8
    private final float scaleY = 6;//4
    private Controller controller;
    private Camera camera;
    private Color[][] mapdata = new Color[Map.getBlocksX()][Map.getBlocksY()];
    private boolean visible;

    /**
     * Create a camera
     * @param controller
     * @param camera the camera wich should be represented on the minimap
     */
    public Minimap(Controller controller, Camera camera) {
        //if (controller == null || camera == null) throw new NullPointerException("Parameter controller or camera is null");
        this.controller = controller;
        this.camera = camera;
    }
    
    public void update(){
        for (int x = 0; x < Map.getBlocksX(); x++) {
            for (int y = 0; y < Map.getBlocksY(); y++) {
                int z = Map.getBlocksZ() -1;
                Block block = Controller.getMapData(new int[]{x,y,z});
                while ( z>0 && (block = Controller.getMapData(new int[]{x,y,z})).isHidden() ) {
                    z--;
                }
                mapdata[x][y] = Block.getRepresentingColor(block.getId(), block.getValue());
            }
        }
    }
    
    
    /**
     * Draws the Minimap
     * @param screenX The distance of the right side.
     * @param screenY Distance from top.
     */
    public void render(int screenX, int screenY) {
        this.posX = screenX - (int) (Map.getBlocksX()*scaleX);
        this.posY = screenY;
        
        Wurfelengine.getGraphics().setAntiAlias(false);
        
        //render the map
        for (int x = 0; x < Map.getBlocksX(); x++) {
            for (int y = 0; y < Map.getBlocksY(); y++) {
                Wurfelengine.getGraphics().setColor(mapdata[x][y]);
                Wurfelengine.getGraphics().fillRect(
                    this.posX + (x + (y%2 == 1 ? 0.5f : 0) ) * scaleX,
                    this.posY + y*scaleY,
                    scaleX,
                    scaleY
                );   
            }
        }
                
        for (int pos = 0; pos < 9; pos++) {
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
            posX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
            posY + scaleY * camera.getOutputPosY() / (Block.DIM2/2),
            scaleX*camera.getOutputWidth() / Block.DIMENSION,
            scaleY*camera.getOutputHeight() / (Block.DIM2/2)
        );

        if (controller.getPlayer()!=null){
            //player level getCamera() rectangle
            Wurfelengine.getGraphics().setColor(Color.gray);
            Wurfelengine.getGraphics().drawRect(
                posX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
                posY + scaleY * camera.getOutputPosY() / (Block.DIM2/2)
                + scaleY *2*(controller.getPlayer().getRelCoords()[2] * (Block.DIM2/2))/ (float) (Block.DIMENSION),
                scaleX*camera.getOutputWidth() / Block.DIMENSION,
                scaleY*camera.getOutputHeight() / (Block.DIM2/2)
            );
        }

        //top level getCamera() rectangle
        Wurfelengine.getGraphics().setColor(Color.white);
        Wurfelengine.getGraphics().drawRect(
            posX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
            posY + scaleY * camera.getOutputPosY() / (Block.DIM2/2)
            + scaleY *2*(Chunk.getBlocksZ() * Block.DIM2)/ (float) (Block.DIMENSION),
            scaleX*camera.getOutputWidth() / Block.DIMENSION,
            scaleY*camera.getOutputHeight() / (Block.DIM2/2)
        );

        if (controller.getPlayer()!=null){
            View.getFont().drawString(
                    posX + scaleX * camera.getOutputPosX() / Block.DIMENSION
                    + scaleX*camera.getOutputWidth() / Block.DIMENSION,
                    posY + scaleY * camera.getOutputPosY() / Block.DIM2
                    + scaleY *2*(controller.getPlayer().getRelCoords()[2] * Block.DIM2)/ (float) (Block.DIMENSION)
                    + scaleY*camera.getOutputHeight() / Block.DIM2,
                    camera.getRightBorder() +" | "+ camera.getBottomBorder() ,
                    Color.black
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
                (int) controller.getPlayer().getPos()[0] +" | "+ (int) controller.getPlayer().getPos()[1] +" | "+ (int) controller.getPlayer().getPos()[2],
                Color.red
            );
        }

        //getCamera() pos
        View.getFont().drawString(
                posX ,
                posY + 3*Chunk.getBlocksY()*scaleY + 15,
                camera.getOutputPosX() +" | "+ camera.getOutputPosY(),
                Color.white
            );
    }
    
     public boolean toggleVisibility(){
        visible = !visible;
        return visible;
    }
}
