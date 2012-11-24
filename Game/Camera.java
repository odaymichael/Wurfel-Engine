/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Blocks.Block;
import Game.Blocks.Blockpointer;
import Game.Blocks.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.Log;

/**
 *The camera locks to the player by default. It can be changed with <i>focusblock()</i>.
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

    private Blockpointer focusblock;

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
                (player.getRelCoordY()/2f - player.coordZ) * Block.height
                - Gameplay.view.camera.height/2
                + player.getOffsetY() * (1/Block.aspectRatio)
                );
        } else {
            x = focusblock.getX() * Block.width
                + Block.width / 2 *(focusblock.getY() % 2)
                + focusblock.getBlock().getOffsetX()
                - Gameplay.view.camera.width / 2;
            
            y = (int) (
                (focusblock.getY()/2f - focusblock.getZ()) * Block.height
                - Gameplay.view.camera.height/2
                + focusblock.getBlock().getOffsetY() * (1/Block.aspectRatio)
                );
        }
    }
    
    /**
     * Use this if you want to focus on a special block
     * @param blockpointer
     */
    public void FocusOnBlock(Blockpointer blockpointer){
        focus = true;
        focusblock = blockpointer;        
    }
    
    /**
     * The camera now follows the player
     */
    public void FocusOnPlayer(){
        focus = false;
    }
    
    /**
     * 
     */
    public int getLeftBorder(){
        int tmp = x/Block.width -1;
        if (tmp < 0) return 0;
        return tmp;
    }
    
    public int getRightBorder(){
        int tmp = (x+width)/Block.width+2;
        if (tmp >= Chunk.BlocksX*3) return Chunk.BlocksX*3-1;
        return tmp;
    }
    
    public int getTopBorder(){
        int tmp = 2*y/Block.height;
        if (tmp < 0) return 0;
        return tmp;
    }
    
    public int getBottomBorder(){
        int tmp = (2*y+height)/Block.height;
        if (tmp >= Chunk.BlocksY*3) return Chunk.BlocksY*3-1;
        return tmp;
    }
}
