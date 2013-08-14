package com.BombingGames.EngineCore;
   

import com.BombingGames.Game.Gameobjects.Block;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
     * Create a minimap.
     * @param controller the controller wich should be represented
     * @param camera the camera wich should be represented on the minimap
     * @param screenX the screen-position of the minimap
     * @param screenY  the screen-position of the minimap
     */
    public Minimap(Controller controller, Camera camera, int screenX, int screenY) {
        if (controller == null || camera == null) throw new NullPointerException("Parameter controller or camera is null");
        this.posX = screenX;
        this.posY = screenY;
        this.controller = controller;
        this.camera = camera;
    }
    
    /**
     * Updates the minimap- Should only be done after changing the map.
     */
    public void update(){
        for (int x = 0; x < Map.getBlocksX(); x++) {
            for (int y = 0; y < Map.getBlocksY(); y++) {
                int z = Map.getBlocksZ() -1;
                Block block = new Coordinate(x, y, z, true).getBlock();
                while ( z>0 && (block = new Coordinate(x, y, z, true).getBlock()).isHidden() ) {
                    z--;
                }
                mapdata[x][y] = Block.getRepresentingColor(block.getId(), block.getValue());
                mapdata[x][y].a = 1;
                mapdata[x][y] = mapdata[x][y].brighter(0.1f);
                mapdata[x][y] = mapdata[x][y].darker(1f-z/(float)Map.getBlocksZ());
            }
        }
    }
    
    
    /**
     * Renders the Minimap.
     * @param g The graphics context.
     * @param view the view using this render method 
     */
    public void render(Graphics g, View view) {
        if (visible) {
            int renderPosX = (int) (posX/view.getEqualizationScale() - (int) (Map.getBlocksX()*scaleX));
            int renderPosY = (int) (posY/view.getEqualizationScale());
            
            //maybe this speeds up rendering the minimap
            g.setAntiAlias(false);

            //render the map
            for (int x = 0; x < Map.getBlocksX(); x++) {
                for (int y = 0; y < Map.getBlocksY(); y++) {
                    g.setColor(mapdata[x][y]);
                    g.fillRect(
                        renderPosX + (x + (y%2 == 1 ? 0.5f : 0) ) * scaleX,
                        renderPosY + y*scaleY,
                        scaleX,
                        scaleY
                    );   
                }
            }

            for (int pos = 0; pos < 9; pos++) {
                //player
                if (controller.getPlayer()!=null){
                    g.setColor(Color.blue);
                    g.drawRect(
                        renderPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX,
                        renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY,
                        scaleX,
                        scaleY
                    );
                }


                //Chunk outline
                g.setColor(Color.black);
                g.drawRect(
                    renderPosX + pos%3 *(Chunk.getBlocksX()*scaleX),
                    renderPosY + pos/3*(Chunk.getBlocksY()*scaleY),
                    Chunk.getBlocksX()*scaleX,
                    Chunk.getBlocksY()*scaleY
                );

                View.getFont().drawString(
                    renderPosX + 10 + pos%3 *Chunk.getBlocksX()*scaleX,
                    posY + 10 + pos/3 *(Chunk.getBlocksY()*scaleY),
                    Controller.getMap().getChunkCoords(pos)[0] +" | "+ Controller.getMap().getChunkCoords(pos)[1] ,
                    Color.black
                );
            }

            //bottom getCameras() rectangle
            g.setColor(Color.green);
            g.drawRect(
                renderPosX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
                renderPosY + scaleY * camera.getOutputPosY() / (Block.DIM2/2),
                scaleX*camera.getOutputWidth() / Block.DIMENSION,
                scaleY*camera.getOutputHeight() / (Block.DIM2/2)
            );

            if (controller.getPlayer()!=null){
                //player level getCameras() rectangle
                g.setColor(Color.gray);
                g.drawRect(
                    renderPosX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
                    renderPosY + scaleY * camera.getOutputPosY() / (Block.DIM2/2)
                    + scaleY *2*(controller.getPlayer().getCoords().getZ() * (Block.DIM2/2))/ (float) (Block.DIMENSION),
                    scaleX*camera.getOutputWidth() / Block.DIMENSION,
                    scaleY*camera.getOutputHeight() / (Block.DIM2/2)
                );
            }

            //top level getCameras() rectangle
            g.setColor(Color.white);
            g.drawRect(
                renderPosX + scaleX * camera.getOutputPosX() / Block.DIMENSION,
                renderPosY + scaleY * camera.getOutputPosY() / (Block.DIM2/2)
                + scaleY *2*(Chunk.getBlocksZ() * Block.DIM2)/ (float) (Block.DIMENSION),
                scaleX*camera.getOutputWidth() / Block.DIMENSION,
                scaleY*camera.getOutputHeight() / (Block.DIM2/2)
            );

            if (controller.getPlayer()!=null){
                View.getFont().drawString(
                        renderPosX + scaleX * camera.getOutputPosX() / Block.DIMENSION
                        + scaleX*camera.getOutputWidth() / Block.DIMENSION,
                        renderPosY + scaleY * camera.getOutputPosY() / Block.DIM2
                        + scaleY *2*(controller.getPlayer().getCoords().getZ() * Block.DIM2)/ (float) (Block.DIMENSION)
                        + scaleY*camera.getOutputHeight() / Block.DIM2,
                        camera.getRightBorder() +" | "+ camera.getBottomBorder() ,
                        Color.black
                    );

                //player coord
                g.setColor(Color.blue);
                View.getFont().drawString(
                    renderPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20,
                    renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 30,
                    controller.getPlayer().getCoords().getRelX() +" | "+ controller.getPlayer().getCoords().getRelY() +" | "+ controller.getPlayer().getCoords().getZ(),
                    Color.blue
                );


                //player pos
                View.getFont().drawString(
                    renderPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20,
                    renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 10,
                    (int) controller.getPlayer().getOffsetX() +" | "+ (int) controller.getPlayer().getOffsetY() +" | "+ (int) controller.getPlayer().getCoords().getHeight(),
                    Color.red
                );
            }

            //getCamera() pos
            View.getFont().drawString(
                    renderPosX ,
                    renderPosY + 3*Chunk.getBlocksY()*scaleY + 15,
                    camera.getOutputPosX() +" | "+ camera.getOutputPosY(),
                    Color.white
            );
        }
    }
    
    /**
     * Toggle between visible and invisible.
     * @return The new visibility of the minimap. True= visible.
     */
    public boolean toggleVisibility(){
        visible = !visible;
        return visible;
    }
}