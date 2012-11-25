/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Blocks.Block;
import Game.Blocks.Blockpointer;
import Game.Blocks.Player;
import org.newdawn.slick.util.Log;

/**
 *The camera locks to the player by default. It can be changed with <i>focusblock()</i>.
 * @author Benedikt
 */
public class Camera {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean focus = false;
    private Blockpointer focusblock;
    private float zoom = 1;
    private final int screenWidth;
    private final int screenHeight;
    private final int screenX;
    private final int screenY;

    /**
     * Creates a camera.
     * @param x The screen coordinates
     * @param y The screen coordinate
     * @param width The screen width of the camera
     * @param height The screeb height of the camera
     * @param zoom The zoom factor.
     */
    Camera(int x, int y,int width, int height,float zoom) {
        screenX = x;
        screenY = y;
        screenWidth = width;
        screenHeight = height;
        this.zoom = zoom;
        Log.debug("Zoom is:"+Float.toString(zoom));
        this.width = (int) (screenWidth / zoom);
        this.height = (int) (screenHeight / zoom);
    } 
       
    /**
     * 
     */
    public void update() {
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
     * @return 
     */
    public int getLeftBorder(){
        int tmp = x/Block.width -1;
        if (tmp < 0) return 0;
        return tmp;
    }
    
    /**
     * 
     * @return
     */
    public int getRightBorder(){
        int tmp = (x+width)/Block.width+2;
        if (tmp >= Chunk.BlocksX*3) return Chunk.BlocksX*3-1;
        return tmp;
    }
    
    /**
     * Returns the top seight border of the deepest block
     * @return measured in blocks
     */
    public int getTopBorder(){
        int tmp = 2*y/Block.height;
        if (tmp < 0) return 0;
        return tmp;
    }
    
     /**
     * Returns the bottom seight border of the highest block
     * @return measured in blocks
     */
    public int getBottomBorder(){
        int tmp = (y+height)/(Block.height/2) + Chunk.BlocksZ*2;
        if (tmp >= Chunk.BlocksY*3) return Chunk.BlocksY*3-1;
        return tmp;
    }
    
 

   /**
    * The amount of pixel which are visible in Y direction (game pixels). For screen pixels use <i>ScreenHeight()</i>.
    * @return
    */
   public int getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * The amount of pixel which are visible in x direction (game pixels). For screen pixels use <i>ScreenWidth()</i>.
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

       /**
     * The Camera Position.
     * @return 
     */
    public int getX() {
        return x;
    }

    /**
     * 
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * 
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    
    /**
     * False=player, true=block
     * @return
     */
    public boolean getFocus(){
        return focus;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
    
    
}
