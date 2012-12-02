package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Game.Blocks.Blockpointer;
import com.BombingGames.Game.Blocks.Player;
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
    
    private final int screenX;
    private final int screenY;
    private final int screenWidth;
    private final int screenHeight;

    /**
     * Creates a camera.
     * @param x The screen coordinates
     * @param y The screen coordinate
     * @param width The screen width of the camera
     * @param height The screen height of the camera
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
     * Updates the camera
     */
    public void update() {
        if (focus) {//focus on block
             x = focusblock.getX() * Block.width
                + Block.width / 2 *(focusblock.getY() % 2)
                + focusblock.getBlock().getOffsetX()
                - Gameplay.view.camera.width / 2;
            
            y = (int) (
                (focusblock.getY()/2f - focusblock.getZ()) * Block.height
                - Gameplay.view.camera.height/2
                + focusblock.getBlock().getOffsetY() * (1/Block.aspectRatio)
                );
            
        } else {//focus on player
            Player player = Gameplay.controller.player;
            x = player.getRelCoordX() * Block.width
                + Block.width / 2 *(player.getRelCoordY() % 2)
                + player.getOffsetX()
                - Gameplay.view.camera.width / 2;
            
            y = (int) (
                (player.getRelCoordY()/2f - player.coordZ) * Block.height
                - Gameplay.view.camera.height/2
                + player.getOffsetY() * (1/Block.aspectRatio)
                );
           
        }
    }
    
    /**
     * Set the zoom factor and regenerates the sprites.
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
        width = (int) (screenWidth / zoom);
        height = (int) (screenHeight / zoom);
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
     * Returns the left border of the visible area.
     * @return 
     */
    public int getLeftBorder(){
        int tmp = x/Block.width -1;
        if (tmp < 0) return 0;
        return tmp;
    }
    
    /**
     * Returns the right border of the visible area.
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
     * The Camera Position in the game world.
     * @return 
     */
    public int getX() {
        return x;
    }

    /**
     * The Camera Position in the game world.
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * The Camera Position in the game world.
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * The Camera Position in the game world.
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

   /**
    * The amount of pixel which are visible in Y direction (game pixels). For screen pixels use <i>ScreenHeight()</i>.
    * @return
    */
   public int getHeight() {
        return height;
    }


    /**
     * The amount of pixel which are visible in x direction (game pixels). For screen pixels use <i>ScreenWidth()</i>.
     * @return
     */
    public int getWidth() {
        return width;
    }


    /**
     * Returns the amount of (game) pixels visible in Y direction. Ground level+ slope width.
     * @return
     */
    public int getYzHeight() {
        return height + Block.height*Chunk.BlocksZ;
    }

    
    /**
     * False= Focus on player, true= Focus is on a block
     * @return
     */
    public boolean getFocus(){
        return focus;
    }

    
    /**
     * Returns the height of the camera output.
     * @return
     */
    public float getScreenHeight() {
        return screenHeight;
    }

    /**
     * Returns the width of the camera output.
     * @return
     */
    public float getScreenWidth() {
        return screenWidth;
    }

    /**
     * Returns the position of the cameras output.
     * @return
     */
    public int getScreenX() {
        return screenX;
    }

    /**
     * Returns the position of the camera
     * @return
     */
    public int getScreenY() {
        return screenY;
    }
    
    
}
