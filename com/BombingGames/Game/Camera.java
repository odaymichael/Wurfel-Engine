package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import com.BombingGames.Game.Blocks.Blockpointer;
import com.BombingGames.Game.Blocks.Player;
import com.BombingGames.Wurfelengine;
import java.util.ArrayList;

/**
 *The camera locks to the player by default. It can be changed with <i>focusblock()</i>.
 * @author Benedikt
 */
public class Camera {
    private int xPos, yPos, gameWidth, gameHeight;
    private int leftborder, topborder, rightborder, bottomborder;
    
    private boolean focus = false;
    private Blockpointer focusblock;
    
    private float zoom = 1;
    
   private static class Renderblock {
        protected int x,y,z;
        protected int depth;

        public Renderblock(int x, int y, int z, int depth) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
        }
    }
        
    private final int screenX, screenY, screenWidth, screenHeight;
    private ArrayList<Renderblock> depthsort = new ArrayList();

    /**
     * Creates a camera. Screen does refer to the output of the camera not the real size on the display.
     * @param xPos The screen coordinates
     * @param yPos The screen coordinate
     * @param gameWidth The screen gameWidth of the camera
     * @param gameHeight The screen gameHeight of the camera
     * @param scale The zoom factor.
     */
    public Camera(int x, int y,int width, int height, float scale) {
        screenX = x;
        screenY = y;
        //to achieve the wanted size it must be scaled in the other direction
        screenWidth = (int) (width / scale);
        screenHeight = (int) (height / scale);
        
        gameWidth = (int) (screenWidth / zoom);
        gameHeight = (int) (screenHeight / zoom);
        Wurfelengine.getGraphics().setWorldClip(x, y, screenWidth, screenHeight);
    } 
       
    /**
     * Updates the camera
     */
    public void update() {
        if (focus) {//focus on block
             xPos = focusblock.getX() * Block.WIDTH
                + Block.WIDTH / 2 *(focusblock.getY() % 2)
                + focusblock.getBlock().getOffsetX()
                - gameWidth / 2;
            
            yPos = (int) (
                (focusblock.getY()/2f - focusblock.getZ()) * Block.HEIGHT
                - gameHeight/2
                + focusblock.getBlock().getOffsetY() * (1/Block.ASPECTRATIO)
                );
            
        } else {//focus on player
            Player player = Gameplay.getController().getPlayer();
            xPos = player.getCoordX() * Block.WIDTH
                + Block.WIDTH / 2 *(player.getCoordY() % 2)
                + player.getOffsetX()
                - gameWidth / 2;
            
            yPos = (int) (
                (player.getCoordY()/2f - player.getCoordZ()) * Block.HEIGHT
                - gameHeight/2
                + player.getOffsetY() * (1/Block.ASPECTRATIO)
                );
        }
        
        //update borders
        leftborder = xPos/Block.WIDTH -1;
        if (leftborder < 0) leftborder= 0;
        
        rightborder = (xPos+gameWidth)/Block.WIDTH+2;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;
        
        topborder = yPos / (Block.HEIGHT/2)-1;
        if (topborder < 0) topborder= 0;
        
        bottomborder = (yPos+gameHeight) / (Block.HEIGHT/2) + Chunk.getBlocksZ()*2;
        if (bottomborder >= Map.getBlocksY()) bottomborder = Map.getBlocksY()-1;
        
    }
    
    public void draw() {
        createSortedDepthList();
        Wurfelengine.getGraphics().scale(getZoom(), getZoom());
        Controller.getMap().draw(this);
        
        Wurfelengine.getGraphics().scale(1/getZoom(), 1/getZoom());
        //GUI
        if (Controller.getMap().getMinimap() != null)
            Controller.getMap().getMinimap().draw(); 
    }
    
    /**
     * Set the zoom factor and regenerates the sprites.
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
        gameWidth = (int) (screenWidth / zoom);
        gameHeight = (int) (screenHeight / zoom);
    }
    
    /**
     * Returns the zoomfactor.
     * @return
     */
    public float getZoom() {
        return zoom;
    }
    
    /**
     * Returns the zoom multiplied by the EqualizationScale
     * @return 
     */
    public float getAbsZoom() {
        return zoom*Gameplay.getView().getEqualizationScale();
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
        return leftborder;
    }
    
    /**
     * Returns the right border of the visible area.
     * @return
     */
    public int getRightBorder(){
        return rightborder;
    }
    
    /**
     * Returns the top seight border of the deepest block
     * @return measured in blocks
     */
    public int getTopBorder(){
        return topborder;
    }
    
     /**
     * Returns the bottom seight border of the highest block
     * @return measured in blocks
     */
    public int getBottomBorder(){
        return bottomborder;
    }
    
  /**
     * The Camera Position in the game world.
     * @return 
     */
    public int getX() {
        return xPos;
    }

    /**
     * The Camera Position in the game world.
     * @param xPos
     */
    public void setX(int x) {
        this.xPos = x;
    }

    /**
     * The Camera Position in the game world.
     * @return
     */
    public int getY() {
        return yPos;
    }

    /**
     * The Camera Position in the game world.
     * @param yPos
     */
    public void setY(int y) {
        this.yPos = y;
    }

   /**
    * The amount of pixel which are visible in Y direction (game pixels). For screen pixels use <i>ScreenHeight()</i>.
    * @return
    */
   public int getGroundHeight() {
        return gameHeight;
    }


    /**
     * The amount of pixel which are visible in xPos direction (game pixels). For screen pixels use <i>ScreenWidth()</i>.
     * @return
     */
    public int getWidth() {
        return gameWidth;
    }


    /**
     * Returns the amount of (game) pixels visible in Y direction. Ground level+ the gameWidth of the slope.
     * @return
     */
    public int getTotalHeight() {
        return gameHeight + Block.HEIGHT*Chunk.getBlocksZ();
    }

    
    /**
     * False= Focus on player, true= Focus is on a block
     * @return
     */
    public boolean getFocus(){
        return focus;
    }

    
    /**
     * Returns the height of the camera output. To get the real value multiply it with scale value.
     * @return the value before scaling
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Returns the width of the camera output before scale. To get the real value multiply it with scale value.
     * @return the value before scaling
     */
    public int getScreenWidth() {
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
    
    /**
     * Returns the pixel-coordinates (screen) of a block's center.
     * @param xPos
     * @param yPos
     * @param z
     * @return an array with [0]-xPos and [1]-yPos 
     */
    public int[] getCenterofBlock(int x, int y, int z){
        int result[] = new int[2];
        result[0] = -getX()+x*Block.WIDTH + (y%2) * (int) (Block.WIDTH/2) + Controller.getMapData(x, y, z).getOffsetX();
        result[1] = (int) (-getY()+y*Block.HEIGHT/2 - z*Block.HEIGHT + Controller.getMapData(x, y, z).getOffsetY() * (1/Block.ASPECTRATIO));
        return result;
    }
    
     /**
     * Fills the map into a list and sorts it, called the depthlist.
     */
    private void createSortedDepthList() {
        depthsort.clear();
        for (int x = getLeftBorder(); x < getRightBorder();x++)
            for (int y = getTopBorder(); y < getBottomBorder();y++)
                for (int z=0; z < Map.getBlocksZ(); z++){
                    
                    Block block = Controller.getMapData(x, y, z); 
                    if (!block.isInvisible() && block.isVisible()) {
                        depthsort.add(new Renderblock(x, y, z, block.getDepth(y,z)));
                    }
                    
                }
        sortDepthList(0, depthsort.size()-1);
    }
    
    /**
     * Using Quicksort to sort. From big to small values.
     * @param low the lower border
     * @param high the higher border
     */
    private void sortDepthList(int low, int high) {
        int left = low;
        int right = high;
        int middle = depthsort.get((low+high)/2).depth;

        while (left <= right){    
            while(depthsort.get(left).depth < middle) left++; 
            while(depthsort.get(right).depth > middle) right--;

            if (left <= right) {
                Renderblock tmp = depthsort.set(left, depthsort.get(right));
                depthsort.set(right, tmp);
                left++; 
                right--;
            }
        }

        if(low < right) sortDepthList(low, right);
        if(left < high) sortDepthList(left, high);
    }
    
        /**
     * Returns a coordiante triple of an ranking for the rendering order
     * @param index the index
     * @return the coordinate triple with x,y,z
     */
    public int[] getDepthsortCoord(int index) {
        Renderblock item = depthsort.get(index);
        int[] triple = {item.x, item.y, item.z};
        return triple;
    }
    
    /**
     * Returns the lenght of list of ranking for the rendering order
     * @return length of the render list
     */
    public int getDepthsortlistSize(){
        return depthsort.size();
    }
}
