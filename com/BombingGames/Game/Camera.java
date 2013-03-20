package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.GameObject;
import com.BombingGames.Wurfelengine;
import java.util.ArrayList;

/**
 *Creates a virtual camera wich displays the game world.  
 * @author Benedikt
 */
public class Camera {
    private final int screenPosX, screenPosY, screenWidth, screenHeight;
    private int outputPosX, outputPosY;
    private int leftborder, topborder, rightborder, bottomborder;
    private float zoom = 1;
    private float equalizationScale = 1;
    
    private int[] focusblock;
    private AbstractEntity focusentity;
    private ArrayList<Renderobject> depthsort = new ArrayList<Renderobject>();
    

    private Camera(int x, int y, int width, int height){
        screenPosX = x;
        screenPosY = y;
        screenWidth = width;
        screenHeight = height;
        
        equalizationScale = screenWidth / (float) View.ENGINE_RENDER_WIDTH;
    }
    
   /**
     * The camera locks to the player by default. It can be changed with <i>focusblock()</i>. Screen size does refer to the output of the camera not the real size on the display.
     * @param focus 
     * @param x the position of the output
     * @param y the position of the output
     * @param width the width of the output. it can be different than the output on the display because it gets scaled later again.
     * @param height the height of the output. it can be different than the output on the display because it gets scaled later again.
     */
    public Camera(int[] focus, int x, int y, int width, int height) {
        this(x, y, width, height);   
        Gameplay.MSGSYSTEM.add("Camera is focusing a coordinate");
        this.focusblock = focus;
        this.focusentity = null;         
    }
    
   /**
     * Creates a camera.
     * The values are sceen-size and do refer to the output of the camera not the real display size.
     * @param focusentity the entity wich the camera focuses
     * @param x the position of the output
     * @param y the position of the output
     * @param width the screen width
     * @param height the screen width
     */
    public Camera(AbstractEntity focusentity, int x, int y, int width, int height) {
        this(x,y,width,height);
        if (focusentity == null)
            throw new NullPointerException("Parameter 'focusentity' is null");
        Gameplay.MSGSYSTEM.add("Camera is focusing an entity");
        this.focusentity = focusentity;
        this.focusblock = null;
    }
  
    /**
     * Set the zoom factor and regenerates the sprites.
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
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
    public float getTotalScale() {
        return zoom*equalizationScale;
    }

    
    /**
     * Use this if you want to focus on a special block.
     * @param coord the coordaintes of the block.
     */
    public void focusOnCoords(int[] coord){
        focusblock = coord;
        focusentity = null;
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
     * The Camera Position from in the game world.
     * @return in pixels
     */
    public int getOutputPosX() {
        return outputPosX;
    }

    /**
     * The Camera left Position in the game world.
     * @param x in pixels
     */
    public void setOutputPosX(int x) {
        this.outputPosX = x;
    }

    /**
     * The Camera top-position in the game world.
     * @return in camera position game space
     */
    public int getOutputPosY() {
        return outputPosY;
    }

    /**
     * The Camera top-position in the game world.
     * @param y in game space
     */
    public void setOutputPosY(int y) {
        this.outputPosY = y;
    }

    /**
     * The amount of pixel which are visible in Y direction (game pixels).
     * For screen pixels use <i>ScreenWidth()</i>.
     * @return in pixels
     */
    public int getOutputWidth() {
        return (int) (screenWidth / getTotalScale());
    }
    
  /**
    * The amount of pixel which are visible in Y direction (game pixels). For screen pixels use <i>ScreenHeight()</i>.
    * @return  in pixels
    */
   public int getOutputHeight() {
        return (int) (screenHeight / getTotalScale());
    }

    /**
     * Returns the position of the cameras output.
     * @return  in pixels
     */
    public int getScreenPosX() {
        return screenPosX;
    }

    /**
     * Returns the position of the camera
     * @return
     */
    public int getScreenPosY() {
        return screenPosY;
    }
    
    /**
     * Returns the height of the camera output.
     * To get the real display size multiply it with scale values.
     * @return the value before scaling
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Returns the width of the camera output before scale.
     * To get the real value multiply it with scale value.
     * @return the value before scaling
     */
    public int getScreenWidth() {
        return screenWidth;
    }


    /**
     * Updates the camera.
     */
    public void update() {
        if (focusblock != null) {
            outputPosX = GameObject.getScreenPosX(
                Controller.getMapData(focusblock), focusblock
            ) - getOutputWidth() / 2 - GameObject.DIM2;
            
            outputPosY = GameObject.getScreenPosY(
                Controller.getMapData(focusblock), focusblock
            ) - getOutputHeight() / 2;
        } else {
            outputPosX = GameObject.getScreenPosX(focusentity, focusentity.getRelCoords()) - getOutputWidth() / 2;            
            outputPosY = GameObject.getScreenPosY(focusentity, focusentity.getRelCoords()) - getOutputHeight() / 2 ;
        }
        
        //maybe unneccessary and can be done when the getter is called.
        //update borders once every update
        leftborder = outputPosX / GameObject.DIMENSION - 1;
        if (leftborder < 0) leftborder= 0;
        
        rightborder = (outputPosX + getOutputWidth()) / GameObject.DIMENSION + 2;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;
        
        topborder = outputPosY / GameObject.DIM4 - 3;
        if (topborder < 0) topborder= 0;
        
        bottomborder = (outputPosY+getOutputHeight()) / GameObject.DIM4 + Map.getBlocksZ()*2;
        if (bottomborder >= Map.getBlocksY()) bottomborder = Map.getBlocksY()-1;
    }
    
    /**
     * Renders the viewport
     */
    public void render() {
        if (Controller.getMap() != null) {     
            //move the camera (graphic context)
           
            Wurfelengine.getGraphics().translate(screenPosX, screenPosY);
            
            Wurfelengine.getGraphics().scale(getTotalScale(), getTotalScale());
            
            Wurfelengine.getGraphics().translate(-outputPosX, -outputPosY);
            
            Wurfelengine.getGraphics().setClip(screenPosX, screenPosY, screenWidth, screenHeight);

            
            //render map
            createDepthList();
            Controller.getMap().render(this);

            //reset clipping
            Wurfelengine.getGraphics().clearClip();            
            //reverse scale
            Wurfelengine.getGraphics().scale(1/getTotalScale(), 1/getTotalScale());
                        
            //reverse translate
            Wurfelengine.getGraphics().translate(outputPosX*getTotalScale() - screenPosX, outputPosY*getTotalScale() - screenPosY);
            
        }
    }
    
     /**
     * Fills the map into a list and sorts it in the order of the rendering, called the depthlist.
     */
    private void createDepthList() {
        depthsort.clear();
        for (int x = leftborder; x < rightborder;x++)
            for (int y = topborder; y < bottomborder;y++)
                for (int z=0; z < Map.getBlocksZ(); z++){
                    
                    Block block = Controller.getMapData(x, y, z); 
                    if (!block.isHidden() && block.isVisible()
                        && 
                            GameObject.getScreenPosY(
                                block,
                                new int[]{x, y, z}
                            )
                        <
                            outputPosY + getOutputHeight()
                    ) {
                        depthsort.add(
                            new Renderobject(
                                new int[]{x, y, z},
                                block.getDepth(y,z),
                                -1
                            )
                        );
                    }
                }
        
        //add entitys
        for (int i=0; i< Controller.getMap().getEntitylist().size(); i++) {
            AbstractEntity entity = Controller.getMap().getEntitylist().get(i);
            if (!entity.isHidden() && entity.isVisible()
                        && 
                            GameObject.getScreenPosY(
                                entity,
                                entity.getRelCoords()
                            )
                        <
                            outputPosY + getOutputHeight()
                    )
                    depthsort.add(
                        new Renderobject(
                            entity.getRelCoords(),
                            entity.getDepth(entity.getRelCoords()[1], entity.getRelCoords()[2]),
                            i
                        )
                    );
        }
        //sort the list
        sortDepthList(0, depthsort.size()-1);
    }
    
    /**
     * Using Quicksort to sort.
     * From big to small values.
     * @param low the lower border
     * @param high the higher border
     */
    private void sortDepthList(int low, int high) {
        int left = low;
        int right = high;
        int middle = depthsort.get((low+high)/2).getDepth();

        while (left <= right){    
            while(depthsort.get(left).getDepth() < middle) left++; 
            while(depthsort.get(right).getDepth() > middle) right--;

            if (left <= right) {
                Renderobject tmp = depthsort.set(left, depthsort.get(right));
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
        protected int[] getDepthsortCoord(int index) {
            Renderobject item = depthsort.get(index);
            int[] triple = item.getCoords();
            return triple;
        }

        /**
        * Returns the entityindex of a element i in the depthsort list.
        * @param i index of the depthsort list
        * @return the entityindex
        */
        protected int getEntityIndex(int i) {
            return depthsort.get(i).getEntityindex();
        }

        /**
        * Returns the lenght of list of ranking for the rendering order
        * @return length of the render list
        */
        protected int depthsortlistSize(){
            return depthsort.size();
        }
    
    
    /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    protected void raytracing(){ 
        //set visibility of every block to false, except blocks with offset
        Block[][][] mapdata = Controller.getMap().getData();
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Map.getBlocksZ(); z++) {
                    Block block = mapdata[x][y][z];
                    
                    //Blocks with offset are not in the grid, so can not be calculated => always visible
                    block.setVisible(
                       !block.hasSides() || block.hasOffset()
                       );
                }
                
        //send the rays through top of the map
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY() + Map.getBlocksZ()*2; y++){
                traceRay(new int[]{x,y, Map.getBlocksZ()-1}, 0);
                traceRay(new int[]{x,y, Map.getBlocksZ()-1}, 1);
                traceRay(new int[]{x,y, Map.getBlocksZ()-1}, 2);
            }     
    }
    
    /**
    * Traces a single ray.
    * This costs less performance than a whole raytracing.
     * @param x The starting x-coordinate.
     * @param y The starting y-coordinate.
     * @param z The starting z-coordinate.
     * @param side The side the ray should check
     */
    private void traceRay(int[] coords, int side){
        int x = coords[0];
        int y = coords[1];
        int z = coords[2];
        
        boolean left = true;
        boolean right = true;
        boolean leftliquid = false;
        boolean rightliquid = false;
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
                    if (Controller.getMapData(x, y, z).hasSides()//block on top
                        && x > 0 && y < Map.getBlocksY()-1
                        && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z).hidingPastBlock())
                        break; //stop ray
                    
                    //liquid
                    if (Controller.getMapData(x, y, z).isLiquid()){
                        if (x > 0 && y+1 < Map.getBlocksY()
                        && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z).isLiquid())
                            liquidfilter = true;
                        
                        if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                            leftliquid = true;
                        
                        if (y < Map.getBlocksY()-2 &&
                            Controller.getMapData(x, y+2, z).isLiquid())
                            rightliquid = true;
                        
                        if (leftliquid && rightliquid) liquidfilter = true;
                    } 

                    //two blocks hiding the left side
                    if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                        && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).hidingPastBlock())
                        left = false;
                    if (y < Map.getBlocksY()-2
                        && Controller.getMapData(x, y+2, z).hidingPastBlock()
                        )
                        right = false;
                    
                } else {              
                    if (side == 1) {//check top side
                        if (Controller.getMapData(x, y, z).hasSides()//block on top
                            && z+1 < Map.getBlocksZ()
                            && Controller.getMapData(x, y, z+1).hidingPastBlock())
                            break;
                        
                        //liquid
                        if (Controller.getMapData(x, y, z).isLiquid()){
                            if (z < Map.getBlocksZ()-1 && Controller.getMapData(x, y, z+1).isLiquid())
                                liquidfilter = true;
                            
                            if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                                leftliquid = true;
                            
                            if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                &&  Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                rightliquid = true;
                            
                            if (leftliquid && rightliquid) liquidfilter = true;
                        }
                    
                        //two 0- and 2-sides hiding the side 1
                        if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && Controller.getMapData(x - (y%2 == 0 ? 1:0), y+1, z+1).hidingPastBlock())
                            left = false;
                        
                        if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).hidingPastBlock()
                            )
                            right = false;
                          
                    } else {
                        if (side==2){
                            //block on right hiding the whole right side
                            if (Controller.getMapData(x, y, z).hasSides()//block on top
                                && x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY()
                                && Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z).hidingPastBlock()
                                ) break;
                            
                            //liquid
                            if (Controller.getMapData(x, y, z).isLiquid()){
                               if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1
                                    && Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z).isLiquid()
                                   ) liquidfilter = true;
                               
                                if (y+2 < Map.getBlocksY()
                                    &&
                                    Controller.getMapData(x, y+2, z).isLiquid())
                                    leftliquid = true;
                                
                                if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                                    &&
                                    Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                    rightliquid = true;
                                
                                if (leftliquid && rightliquid) liquidfilter = true;
                            }

                            //two blocks hiding the right side
                            if (y+2 < Map.getBlocksY()
                                &&
                                Controller.getMapData(x, y+2, z).hidingPastBlock())
                                left = false;
                            
                            if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                                &&
                                Controller.getMapData(x + (y%2 == 0 ? 0:1), y+1, z+1).hidingPastBlock())
                                right = false;
                        } else {
//                            if (side == 4) {//check top side
//                                if (Controller.getMapData(x, y, z).hasSides()//block on top
//                                    && z+1 < Map.getBlocksZ()
//                                    && Controller.getMapData(x, y, z+1).hidingPastBlock())
//                                    break;
//                                }
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
            } while (y >= 2 && z >= 1 //not on bottom of map
                && (left || right) //left and right still visible
                && (!Controller.getMapData(x, y, z).hidingPastBlock() || Controller.getMapData(x, y, z).hasOffset()));
    }
    
    /**
     * Traces the ray to a specific block.
     * @param coords
     * @param neighbours True when neighbours block also should be scanned
     */
    public void traceRayTo(int[] coords, boolean neighbours){
        Block block = Controller.getMapData(coords);
                    
        //Blocks with offset are not in the grid, so can not be calculated => always visible
        block.setVisible(
            !block.hasSides() || block.hasOffset()
        );
                    
       //find start position
        while (coords[2] < Map.getBlocksZ()-1){
            coords[1] += 2;
            coords[2]++;
        }
        
        //trace rays
        if (neighbours){
            traceRay(new int[]{coords[0] - (coords[1]%2 == 0 ? 1:0), coords[1]-1, coords[2]}, Block.RIGHTSIDE);
            traceRay(new int[]{coords[0] + (coords[1]%2 == 0 ? 0:1), coords[1]-1, coords[2]}, Block.LEFTSIDE);
            traceRay(new int[]{coords[0], coords[1], coords[2]-1}, Block.TOPSIDE);
        }
        traceRay(coords, Block.LEFTSIDE);
        traceRay(coords, Block.TOPSIDE);             
        traceRay(coords, Block.RIGHTSIDE);

        
        //calculate light
        //find top most renderobject
        int topmost = Chunk.getBlocksZ()-1;
        while (Controller.getMapData(coords[0], coords[1], topmost).isTransparent() == true && topmost > 0 ){
            topmost--;
        }
        
        if (topmost>0) {
            //start at topmost renderobject and go down. Every step make it a bit darker
            for (int level = topmost; level > -1; level--){
                Controller.getMapData(coords[0], coords[1], level).setLightlevel((50 * level) / topmost);
            }
        }
    }
}
