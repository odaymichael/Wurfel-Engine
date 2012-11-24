/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Blocks.Block;
import Game.Blocks.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.Log;

/**
 *The camera automatically locks to the player. It can be disabled with <i>focus</i>.
 * @author Benedikt
 */
public class Camera {
      /**
     * The Camera Position.
     */
   public int x;
    /**
     *  The Camera Position.
     */
    public int y;
    /**
     * The amount of pixel which are visible in x direction
     */
    public int width;
    /**
     * The amount of pixel which are visible in Y direction
     */
    public int height;
    /**
     * toogle between camer locked to player or not
     */ 
    public boolean focus = false;
    /**
     * the zoom factor of the map. Higher value means the zoom is higher.
     */
    private float zoom = 1;


    Camera(GameContainer gc, float zoom) {
        this.zoom = zoom;
        Log.debug("Zoom is:"+Float.toString(zoom));
        
        width = (int) (gc.getWidth() / zoom);
        height = (int) (gc.getHeight() / zoom);  
    }      
    
    /**
     * Set the zoom factor and regenerates the sprites.
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
        Block.reloadSprites(zoom);
    }
    
    /**
     * Returns the zoomfactor.
     * @return
     */
    public float getZoom() {
        return zoom;
    }

    void update() {
        Player player = Gameplay.controller.player;
        if (focus == false) {
            x = player.getRelCoordX() * Block.width
                + Block.width / 2 *(player.getRelCoordY() % 2)
                + player.getOffsetX()
                - Gameplay.view.camera.width / 2;
            
            y = (int) (
                (player.getRelCoordY()/2f - player.coordZ) * Block.height/2f
                - Gameplay.view.camera.height/2
                + player.getOffsetY() * (1/Block.aspectRatio)
                );
        } 
    }
}
