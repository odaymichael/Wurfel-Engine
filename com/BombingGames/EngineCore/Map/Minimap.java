package com.BombingGames.EngineCore.Map;
   
import com.BombingGames.EngineCore.Controller;
import com.BombingGames.EngineCore.Gameobjects.Block;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
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
    private WECamera camera;
    private final Color[][] mapdata = new Color[Map.getBlocksX()][Map.getBlocksY()];
    private boolean visible;

    /**
     * Create a minimap.
     * @param controller the controller wich should be represented
     * @param camera the camera wich should be represented on the minimap
     * @param outputX the output-position of the minimap (distance to left)
     * @param outputY  the output-position of the minimap (distance to top)
     */
    public Minimap(Controller controller, WECamera camera, int outputX, int outputY) {
        if (controller == null || camera == null) throw new NullPointerException("Parameter controller or camera is null");
        this.posX = outputX;
        this.posY = outputY;
        this.controller = controller;
        this.camera = camera;
        
        for (int x = 0; x < Map.getBlocksX(); x++) {
            for (int y = 0; y < Map.getBlocksY(); y++) {
                mapdata[x][y] = new Color();
            }
        }
    }
    
    /**
     * Updates the minimap- Should only be done after changing the map.
     */
    public void update(){
        for (int x = 0; x < Map.getBlocksX(); x++) {
            for (int y = 0; y < Map.getBlocksY(); y++) {
                int z = Map.getBlocksZ() -1;//start at top
                Block block = new Coordinate(x, y, z, true).getBlock();
                while ( z>0 && block.getId() ==0 ) {
                    z--;//find topmost block
                    block = new Coordinate(x, y, z, true).getBlock();
                }
                mapdata[x][y] = Block.getRepresentingColor(block.getId(), block.getValue()).cpy();
                mapdata[x][y].a = 1;
                mapdata[x][y].mul(1.3f).mul((float)z/(float)Map.getBlocksZ());
            }
        }
    }
    
    
    /**
     * Renders the Minimap.
     * @param view the view using this render method 
     */
    public void render(View view) {
        if (visible) {
            int viewportPosX = posX;
            int viewportPosY = posY;
            
            ShapeRenderer shapeRenderer = view.getShapeRenderer();
                        
            //render the map
            shapeRenderer.begin(ShapeType.FilledRectangle);
            for (int x = 0; x < Map.getBlocksX(); x++) {
                for (int y = 0; y < Map.getBlocksY(); y++) {
                    shapeRenderer.setColor(mapdata[x][y]);
                    shapeRenderer.filledRect(
                        viewportPosX + (x + (y%2 == 1 ? 0.5f : 0) ) * scaleX,
                        viewportPosY + y*scaleY,
                        scaleX,
                        scaleY
                    );   
                }
            }
            
            //show player position
            if (controller.getPlayer()!=null){
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.filledRect(
                    viewportPosX + (controller.getPlayer().getCoords().getRelX()
                    + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX,
                    viewportPosY + controller.getPlayer().getCoords().getRelY() * scaleY,
                    scaleX,
                    scaleY
                );
            }
            shapeRenderer.end();
            
            //Chunk outline
            shapeRenderer.begin(ShapeType.Rectangle);
            shapeRenderer.setColor(Color.BLACK);
            for (int chunk = 0; chunk < 9; chunk++) {
                shapeRenderer.rect(
                    viewportPosX + chunk%3 *(Chunk.getBlocksX()*scaleX),
                    viewportPosY + chunk/3*(Chunk.getBlocksY()*scaleY),
                    Chunk.getBlocksX()*scaleX,
                    Chunk.getBlocksY()*scaleY
                );
            }
            shapeRenderer.end();

            for (int chunk = 0; chunk < 9; chunk++) {
                view.drawString(
                    Controller.getMap().getChunkCoords(chunk)[0] +" | "+ Controller.getMap().getChunkCoords(chunk)[1],
                    (int) (viewportPosX + 10 + chunk%3 *Chunk.getBlocksX()*scaleX),
                    (int) (posY + 10 + chunk/3 *(Chunk.getBlocksY()*scaleY)),
                    Color.BLACK
                );
            }

            //bottom getCameras() rectangle
            shapeRenderer.begin(ShapeType.Rectangle);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(
                viewportPosX + scaleX * camera.getGamePosX() / Block.SCREEN_WIDTH,
                viewportPosY + scaleY * camera.getGamePosY() / Block.SCREEN_DEPTH2,
                scaleX*camera.get2DWidth() / Block.SCREEN_WIDTH,
                scaleY*camera.get2DHeight() / Block.SCREEN_DEPTH2
            );

            if (controller.getPlayer()!=null){
                //player level getCameras() rectangle
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(
                    viewportPosX + scaleX * camera.getGamePosX() / Block.SCREEN_WIDTH,
                    viewportPosY + scaleY * camera.getGamePosY() / Block.SCREEN_DEPTH2
                    + scaleY *2*(controller.getPlayer().getCoords().getZ() * Block.SCREEN_HEIGHT2)/ (float) (Block.SCREEN_DEPTH),
                    scaleX*camera.get2DWidth() / Block.SCREEN_WIDTH,
                    scaleY*camera.get2DHeight() / Block.SCREEN_DEPTH2
                );
            }

            //top level getCameras() rectangle
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(
                viewportPosX + scaleX * camera.getGamePosX() / Block.SCREEN_WIDTH,
                viewportPosY + scaleY * camera.getGamePosY() / Block.SCREEN_DEPTH2
                + scaleY *2*(Chunk.getBlocksZ() * Block.SCREEN_DEPTH2)/ (float) (Block.SCREEN_DEPTH),
                scaleX*camera.get2DWidth() / Block.SCREEN_WIDTH,
                scaleY*camera.get2DHeight() / Block.SCREEN_DEPTH2
            );
            shapeRenderer.end();
            
            if (controller.getPlayer()!=null){
                view.drawString(
                        camera.getRightBorder() +" | "+ camera.getBottomBorder(),
                        (int) (viewportPosX + scaleX * camera.getGamePosX() / Block.SCREEN_WIDTH
                        + scaleX*camera.get2DWidth() / Block.SCREEN_WIDTH),
                        (int) (viewportPosY + scaleY * camera.getGamePosY() / Block.SCREEN_DEPTH2
                        + scaleY *2*(controller.getPlayer().getCoords().getZ() * Block.SCREEN_DEPTH2)/ (float) (Block.SCREEN_DEPTH)
                        + scaleY*camera.get2DHeight() / Block.SCREEN_DEPTH2),
                        Color.BLACK
                    );

                //player coord
                view.drawString(
                    controller.getPlayer().getCoords().getRelX() +" | "+ controller.getPlayer().getCoords().getRelY() +" | "+ controller.getPlayer().getCoords().getZ(),
                    (int) (viewportPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20),
                    (int) (viewportPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 30),
                    Color.BLACK
                );

                //player pos
                view.drawString(
                    (int) controller.getPlayer().getPositionX() +" | "+ (int) controller.getPlayer().getPositionY() +" | "+ (int) controller.getPlayer().getCoords().getHeight(),
                    (int) (viewportPosX + (controller.getPlayer().getCoords().getRelX() + (controller.getPlayer().getCoords().getRelY()%2==1?0.5f:0) ) * scaleX+20),
                    (int) (viewportPosY + controller.getPlayer().getCoords().getRelY() * scaleY - 10),
                    Color.RED
                );
            }

            //getCamera() pos
            view.drawString(
                    camera.getGamePosX() +" | "+ camera.getGamePosY(),
                    viewportPosX ,
                    (int) (viewportPosY + 3*Chunk.getBlocksY()*scaleY + 15),
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