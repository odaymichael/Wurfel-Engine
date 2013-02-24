package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.Blockpointer;
import com.BombingGames.Wurfelengine;
import java.util.ArrayList;

/**
 *The camera locks to the player by default. It can be changed with <i>focusblock()</i>.
 * @author Benedikt
 */
public class Camera {
    private final int screenX, screenY, screenWidth, screenHeight;
    private int xPos, yPos, gameWidth, gameHeight;
    private int leftborder, topborder, rightborder, bottomborder;
    private float zoom = 1;
    
    private enum Focustype {
        BLOCK, ENTITY
    } 
    
    private Focustype focus;
    private Blockpointer focusblock;
    private AbstractEntity focusentity;
    
    private static class Renderblock {
        protected final int x,y,z;
        protected final int depth;
        protected final int entityindex;

        protected Renderblock(int x, int y, int z, int depth, int entitynumber) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
            this.entityindex = entitynumber;
        }
    }
    
    private ArrayList<Renderblock> depthsort = new ArrayList<Renderblock>();
    

    
     /**
     * Creates a camera. Screen does refer to the output of the camera not the real size on the display.
     * @param focusblock the block in the focus
     * @param x the position of the camera
     * @param y the position of the camera
     * @param width the screen width
     * @param height the screen width
     * @param scale the zoom factor.
     */
    public Camera(Blockpointer focusblock, int x, int y,int width, int height, float scale) {
        screenX = x;
        screenY = y;
        //to achieve the wanted size it must be scaled in the other direction
        screenWidth = (int) (width / scale);
        screenHeight = (int) (height / scale);
        
        focus = Focustype.BLOCK;
        this.focusblock = focusblock;
        
        gameWidth = (int) (screenWidth / zoom);
        gameHeight = (int) (screenHeight / zoom);
        Wurfelengine.getGraphics().setWorldClip(x, y, screenWidth, screenHeight);
        
        update();
    }
    
   /**
     * Creates a camera. Screen does refer to the output of the camera not the real size on the display.
     * @param focusentity 
     * @param x the position of the camera
     * @param y the position of the camera
     * @param width the screen width
     * @param height the screen width
     * @param scale the zoom factor.
     */
    public Camera(AbstractEntity focusentity, int x, int y,int width, int height, float scale) {
        screenX = x;
        screenY = y;
        //to achieve the wanted size it must be scaled in the other direction
        screenWidth = (int) (width / scale);
        screenHeight = (int) (height / scale);
        
        focus = Focustype.ENTITY;
        this.focusentity = focusentity;
        
        gameWidth = (int) (screenWidth / zoom);
        gameHeight = (int) (screenHeight / zoom);
        Wurfelengine.getGraphics().setWorldClip(x, y, screenWidth, screenHeight);
        
        update();
    }

    
    /**
     * Updates the camera
     */
    public final void update() {
        if (focus == Focustype.BLOCK) {
            xPos = Block.getScreenPosX(focusblock.getBlock(), focusblock.getCoordX(), focusblock.getCoordY(), focusblock.getCoordZ(), null) - gameWidth / 2;            
            yPos = Block.getScreenPosY(focusblock.getBlock(), focusblock.getCoordX(), focusblock.getCoordY(), focusblock.getCoordZ(), null) - gameHeight / 2 ;
        } else {
            xPos = Block.getScreenPosX(focusentity, focusentity.getCoordX(), focusentity.getCoordY(), focusentity.getCoordZ(), null) - gameWidth / 2;            
            yPos = Block.getScreenPosY(focusentity, focusentity.getCoordX(), focusentity.getCoordY(), focusentity.getCoordZ(), null) - gameHeight / 2 ;
        }
        
        //update borders
        leftborder = xPos / Block.DIMENSION - 1;
        if (leftborder < 0) leftborder= 0;
        
        rightborder = (xPos + gameWidth) / Block.DIMENSION + 2;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;
        
        topborder = yPos / Block.DIM4 - 3;
        if (topborder < 0) topborder= 0;
        
        bottomborder = (yPos+gameHeight) / Block.DIM4 + Map.getBlocksZ()*2;
        if (bottomborder >= Map.getBlocksY()) bottomborder = Map.getBlocksY()-1;
    }
    
    /**
     * Renders the viewport
     */
    public void render() {
        if (Controller.getMap() != null) {
        
            Wurfelengine.getGraphics().scale(getZoom(), getZoom());
            
            createSortedDepthList();
            Controller.getMap().render(this);
        
            Wurfelengine.getGraphics().scale(1/getZoom(), 1/getZoom());
        }
        //GUI
        if (Controller.getMap().getMinimap() != null)
            Controller.getMap().getMinimap().render(); 
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
    public void focusOnBlock(Blockpointer blockpointer){
       focus = Focustype.BLOCK;
        focusblock = blockpointer;
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
     * @param x 
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
     * @param y 
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
        return gameHeight + Block.DIM2*Map.getBlocksZ();
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
     * Fills the map into a list and sorts it, called the depthlist.
     */
    private void createSortedDepthList() {
        depthsort.clear();
        for (int x = getLeftBorder(); x < getRightBorder();x++)
            for (int y = getTopBorder(); y < getBottomBorder();y++)
                for (int z=0; z < Map.getBlocksZ(); z++){
                    
                    Block block = Controller.getMapData(x, y, z); 
                    if (!block.isInvisible() && block.isVisible()) {
                        depthsort.add(new Renderblock(x, y, z, block.getDepth(y,z),-1));
                    }
                }
        
        //add entitys
        for (int i=0; i<Gameplay.getController().getEntitylist().size(); i++){
            AbstractEntity entity = Gameplay.getController().getEntitylist().get(i);
            depthsort.add(
                new Renderblock(
                    entity.getCoordX(),
                    entity.getCoordY(),
                    entity.getCoordZ(),
                    entity.getDepth(entity.getCoordY(), entity.getCoordZ()),
                    i
                )
             );
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
     * Depthsort list
     */
    
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
                * Returns the entetyindex of a element i in the depthsort list.
                * @param i index of the depthsort list
                * @return the entityindex
                */
        public int getEntityIndex(int i) {
            return depthsort.get(i).entityindex;
        }

        /**
                * Returns the lenght of list of ranking for the rendering order
                * @return length of the render list
                */
        public int depthsortlistSize(){
            return depthsort.size();
        }
    
    
    
    
    /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    protected void raytracing(){        
        //set visibility of every block to false, except blocks with offset
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Map.getBlocksZ(); z++) {
                    
                    Block block = Controller.getMapData(x, y, z);
                    
                    //Blocks with offset are not in the grid, so can not be calculated => always visible
                    if (block.hasOffset()){
                        block.setVisible(true);
                    } else {
                        block.setVisible(false);
                    }
                }
                
        //send the rays through top of the map
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY() + Map.getBlocksZ()*2; y++){
                traceRay(x, y, Map.getBlocksZ()-1, 0);
                traceRay(x, y, Map.getBlocksZ()-1, 1);
                traceRay(x, y, Map.getBlocksZ()-1, 2);
            }     
    }
    
    /**
    * Traces a single ray-
     * @param x The starting x-coordinate.
     * @param y The starting y-coordinate.
     * @param z The starting z-coordinate.
     * @param side The side the ray traces
     */
    private void traceRay(int x, int y, int z, int side){
        boolean left = true;
        boolean right = true;
        boolean leftliquid = true;
        boolean rightliquid = true;
        boolean liquidfilter = false;

        //bring ray to start position
        if (y >= Map.getBlocksY()) {
            z -= (y-Map.getBlocksY())/2;
            if (y % 2 == 0)
                y = Map.getBlocksY()-1;
            else
                y = Map.getBlocksY()-2;
        }

        y += 2;
        z++;  
        if (z > 0) 
            do {
                y -= 2;
                z--;

                if (side == 0){
                    //direct neighbour block on left hiding the complete left side
                    if (x > 0 && y < Map.getBlocksY()-1
                        && ! Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z).isTransparent())
                        break; //stop ray
                    
                    //liquid
                    if (Controller.getMapData(x, y, z).isLiquid()){
                        if (x > 0 && y+1 < Map.getBlocksY()
                        && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z).isLiquid())
                            liquidfilter = true;
                        
                        if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                            leftliquid = false;
                        
                        if (y < Map.getBlocksY()-2 &&
                            Controller.getMapData(x, y+2, z).isLiquid())
                            rightliquid = false;
                        
                        if (!leftliquid && !rightliquid) liquidfilter=true;
                    } 

                    //two blocks hiding the left side
                    if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                        && ! Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                        left = false;
                    if (y < Map.getBlocksY()-2
                        && ! Controller.getMapData(x, y+2, z).isTransparent()
                        )
                        right = false;
                    
                } else {              
                    if (side == 1) {//check top side
                        if (z < Map.getBlocksZ()-1
                            && ! Controller.getMapData(x, y, z+1).isTransparent())
                            break;
                        
                        //liquid
                        if (Controller.getMapData(x, y, z).isLiquid()){
                            if (z < Map.getBlocksZ()-1 && Controller.getMapData(x, y, z+1).isLiquid())
                                liquidfilter = true;
                            
                            if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                                leftliquid = false;
                            
                            if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                &&  Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                rightliquid = false;
                            
                            if (!leftliquid && !rightliquid) liquidfilter = true;
                        }
                    
                        //two 0- and 2-sides hiding the side 1
                        if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && ! Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                            left = false;
                        
                        if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && ! Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent()
                            )
                            right = false;
                          
                    } else {
                        if (side==2){
                            //block on right hiding the whole right side
                            if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY()
                                && ! Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z).isTransparent()
                                ) break;
                            
                            //liquid
                            if (Controller.getMapData(x, y, z).isLiquid()){
                               if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1
                                    && Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z).isLiquid()
                                   ) liquidfilter = true;
                               
                                if (y+2 < Map.getBlocksY()
                                    &&
                                    Controller.getMapData(x, y+2, z).isLiquid())
                                    leftliquid = false;
                                
                                if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                                    &&
                                    Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                    rightliquid = false;
                                
                                if (!leftliquid && !rightliquid) liquidfilter=true;
                            }

                            //two blocks hiding the right side
                            if (y+2 < Map.getBlocksY()
                                &&
                                ! Controller.getMapData(x, y+2, z).isTransparent())
                                left = false;
                            
                            if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                                &&
                                ! Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent())
                                right = false;
                        }
                    }
                }
                
                if (left || right){ //as long one part of the side is visible save it
                    Block temp = Controller.getMapData(x, y, z);

                    if (!(liquidfilter && temp.isLiquid())){
                        liquidfilter = false;
                        temp.setSideVisibility(side, true);  
                    }                                
                }                
            } while (y >= 2 && z >= 1  && (left || right) && (Controller.getMapData(x, y, z).isTransparent() || Controller.getMapData(x, y, z).hasOffset()));
    }
    
    /**
     * Traces the ray to this block.
     * @param x
     * @param y
     * @param z
     * @param allsides True when every side should be traced?
     */
    public void traceRayTo(int x, int y, int z, boolean allsides){
       //find start position
        while (z < Map.getBlocksZ()-1){
            y += 2;
            z++;
        }
        
        if (allsides){
             traceRay(x,y,z,0);
             traceRay(x,y,z,2);
        }
        traceRay(x,y,z,1);
        //find top most block
        int topmost = Chunk.getBlocksZ()-1;
        while (Controller.getMapData(x, y, topmost).isTransparent() == true && topmost > 0 ){
            topmost--;
        }
                
        if (topmost>0) {
            //start at topmost block and go down. Every step make it a bit darker
            for (int level = topmost; level > 0; level--)
                Controller.getMapData(x, y, level).setLightlevel(50* level / topmost);
        }
    }
}
