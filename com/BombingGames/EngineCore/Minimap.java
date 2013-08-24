package com.BombingGames.EngineCore;
   

import com.BombingGames.Game.Gameobjects.Block;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
                mapdata[x][y] = mapdata[x][y].mul(1.1f);
                mapdata[x][y] = mapdata[x][y].mul(1-(1f-z/(float)Map.getBlocksZ()));
            }
        }
    }
    
    
    /**
     * Renders the Minimap.
     * @param g The graphics context.
     * @param view the view using this render method 
     */
    public void render(View view) {
        if (visible) {
            ShapeRenderer shapeRenderer = view.getShapeRenderer();
            int renderPosX = (int) (posX/view.getEqualizationScale() - (int) (Map.getBlocksX()*scaleX));
            int renderPosY = (int) (posY/view.getEqualizationScale());
                        
            //render the map
            shapeRenderer.begin(ShapeType.FilledRectangle);
            for (int x = 0; x < Map.getBlocksX(); x++) {
                for (int y = 0; y < Map.getBlocksY(); y++) {
                    shapeRenderer.setColor(mapdata[x][y]);
                    shapeRenderer.filledRect(
                        renderPosX + (x + (y%2 == 1 ? 0.5f : 0) ) * scaleX,
                        renderPosY + y*scaleY,
                        scaleX,
                        scaleY
                    );   
                }
            }
            
            //show player position
            if (controller.getPlayer()!=null){
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.filledRect(
                    renderPosX + (controller.getPlayer().getCoords().getRelX()
                    + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX,
                    renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY,
                    scaleX,
                    scaleY
                );
            }
            
            shapeRenderer.setColor(Color.BLACK);
            for (int chunk = 0; chunk < 9; chunk++) {
                //Chunk outline
                shapeRenderer.filledRect(
                    renderPosX + chunk%3 *(Chunk.getBlocksX()*scaleX),
                    renderPosY + chunk/3*(Chunk.getBlocksY()*scaleY),
                    Chunk.getBlocksX()*scaleX,
                    Chunk.getBlocksY()*scaleY
                );
            }
            shapeRenderer.end();

            for (int chunk = 0; chunk < 9; chunk++) {
                view.drawString(
                    Controller.getMap().getChunkCoords(chunk)[0] +" | "+ Controller.getMap().getChunkCoords(chunk)[1],
                    (int) (renderPosX + 10 + chunk%3 *Chunk.getBlocksX()*scaleX),
                    (int) (posY + 10 + chunk/3 *(Chunk.getBlocksY()*scaleY)),
                    Color.BLACK
                );
            }

            //bottom getCameras() rectangle
            shapeRenderer.begin(ShapeType.Rectangle);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(
                renderPosX + scaleX * camera.getGamePosX() / Block.DIMENSION,
                renderPosY + scaleY * camera.getGamePosY() / (Block.DIM2/2),
                scaleX*camera.getOutputWidth() / Block.DIMENSION,
                scaleY*camera.getOutputHeight() / (Block.DIM2/2)
            );

            if (controller.getPlayer()!=null){
                //player level getCameras() rectangle
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(
                    renderPosX + scaleX * camera.getGamePosX() / Block.DIMENSION,
                    renderPosY + scaleY * camera.getGamePosY() / (Block.DIM2/2)
                    + scaleY *2*(controller.getPlayer().getCoords().getZ() * (Block.DIM2/2))/ (float) (Block.DIMENSION),
                    scaleX*camera.getOutputWidth() / Block.DIMENSION,
                    scaleY*camera.getOutputHeight() / (Block.DIM2/2)
                );
            }

            //top level getCameras() rectangle
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(
                renderPosX + scaleX * camera.getGamePosX() / Block.DIMENSION,
                renderPosY + scaleY * camera.getGamePosY() / (Block.DIM2/2)
                + scaleY *2*(Chunk.getBlocksZ() * Block.DIM2)/ (float) (Block.DIMENSION),
                scaleX*camera.getOutputWidth() / Block.DIMENSION,
                scaleY*camera.getOutputHeight() / (Block.DIM2/2)
            );
            shapeRenderer.end();
            
            if (controller.getPlayer()!=null){
                view.drawString(
                        camera.getRightBorder() +" | "+ camera.getBottomBorder(),
                        (int) (renderPosX + scaleX * camera.getGamePosX() / Block.DIMENSION
                        + scaleX*camera.getOutputWidth() / Block.DIMENSION),
                        (int) (renderPosY + scaleY * camera.getGamePosY() / Block.DIM2
                        + scaleY *2*(controller.getPlayer().getCoords().getZ() * Block.DIM2)/ (float) (Block.DIMENSION)
                        + scaleY*camera.getOutputHeight() / Block.DIM2),
                        Color.BLACK
                    );

                //player coord
                view.drawString(
                    controller.getPlayer().getCoords().getRelX() +" | "+ controller.getPlayer().getCoords().getRelY() +" | "+ controller.getPlayer().getCoords().getZ(),
                    (int) (renderPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20),
                    (int) (renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 30),
                    Color.BLACK
                );

                //player pos
                view.drawString(
                    (int) controller.getPlayer().getOffsetX() +" | "+ (int) controller.getPlayer().getOffsetY() +" | "+ (int) controller.getPlayer().getCoords().getHeight(),
                    (int) (renderPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20),
                    (int) (renderPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 10),
                    Color.RED
                );
            }

            //getCamera() pos
            view.drawString(
                    camera.getGamePosX() +" | "+ camera.getGamePosY(),
                    renderPosX ,
                    (int) (renderPosY + 3*Chunk.getBlocksY()*scaleY + 15),
                    Color.WHITE
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