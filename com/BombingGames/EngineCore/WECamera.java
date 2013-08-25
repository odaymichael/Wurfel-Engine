package com.BombingGames.EngineCore;

import com.BombingGames.Game.Gameobjects.AbstractEntity;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;

/**
 *Creates a virtual camera wich displays the game world on the viewport.  
 * @author Benedikt Vogler
 */
public class WECamera extends Camera {
    private final int viewportPosX, viewportPosY;
    
    private int outputPosX, outputPosY;
    private int leftborder, topborder, rightborder, bottomborder;
    private float zoom = 1;
    private float equalizationScale = 1;
    
    private Coordinate focusCoordinates;
    private AbstractEntity focusentity;
    private ArrayList<Renderobject> depthsort = new ArrayList<Renderobject>();
    

    /**
     * Creates a camera pointing at the middle of the map.
     * @param x the position in the application window
     * @param y the position in the application window
     * @param width The width of the image the camera creates on the application window (viewport)
     * @param height The height of the image the camera creates on the application window (viewport)
     */
    public WECamera(int x, int y, int width, int height){
        viewportWidth = width;
	viewportHeight = height;
        viewportPosX = x;
        viewportPosY = y;
        
        equalizationScale = viewportWidth / (float) View.RENDER_RESOLUTION_WIDTH;
                
	near = 0;
	up.set(0, -1, 0);
	direction.set(0, 0, 1);
	position.set(equalizationScale*zoom * viewportWidth / 2.0f, equalizationScale*zoom * viewportHeight / 2.0f, 0);        
        

        
        //set the camera's focus to the center of the map
        outputPosX = Coordinate.getMapCenter().get2DPosX() - get2DWidth() / 2;
        outputPosY = Coordinate.getMapCenter().get2DPosY() - get2DHeight() / 2;
    }
    
   /**
     * The camera locks to the player by default. It can be changed with <i>focusCoordinates()</i>. Screen size does refer to the output of the camera not the real size on the display.
     * @param focus the coordiante where teh camera focuses
     * @param x the position of the output
     * @param y the position of the output
     * @param width the width of the output. it can be different than the output on the display because it gets scaled later again.
     * @param height the height of the output. it can be different than the output on the display because it gets scaled later again.
     */
    public WECamera(Coordinate focus, int x, int y, int width, int height) {
        this(x, y, width, height);   
        GameplayScreen.msgSystem().add("Camera is focusing a coordinate");
        this.focusCoordinates = focus;
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
    public WECamera(AbstractEntity focusentity, int x, int y, int width, int height) {
        this(x,y,width,height);
        if (focusentity == null)
            throw new NullPointerException("Parameter 'focusentity' is null");
        GameplayScreen.msgSystem().add("Camera is focusing an entity: "+focusentity.getName());
        this.focusentity = focusentity;
        this.focusCoordinates = null;
    }
    
     /**
     * Updates the camera.
     */
    @Override
    public void update() {
        //orthographic camera, libgdx stuff
        projection.setToOrtho(
            1/zoom/equalizationScale * -viewportWidth / 2,
            1/zoom/equalizationScale * viewportWidth / 2,
            1/zoom/equalizationScale * -viewportHeight / 2,
            1/zoom/equalizationScale * viewportHeight / 2,
            0,
            Math.abs(far)
        );
        
        Vector3 tmp = new Vector3();
        view.setToLookAt(position, tmp.set(position).add(direction), up);
        combined.set(projection);
        Matrix4.mul(combined.val, view.val);

        invProjectionView.set(combined);
        Matrix4.inv(invProjectionView.val);
        frustum.update(invProjectionView);
        
        //refrehs the camera's position in the game world
        if (focusCoordinates != null) {
            outputPosX = focusCoordinates.getBlock().get2DPosX(focusCoordinates) - get2DWidth() / 2 - GameObject.DIM2;
            outputPosY = focusCoordinates.getBlock().get2DPosY(focusCoordinates) - get2DHeight() / 2;
        } else if (focusentity != null ){
            outputPosX = focusentity.get2DPosX(null) - get2DWidth()/2 - GameObject.DIM2;            
            outputPosY = focusentity.get2DPosY(null) - get2DHeight()/2 ;
        }
        
        //maybe unneccessary and can be done when the getter is called.
        //update borders once every update
        leftborder = outputPosX / GameObject.DIMENSION - 1;
        if (leftborder < 0) leftborder= 0;
        
        rightborder = (outputPosX + get2DWidth()) / GameObject.DIMENSION + 2;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;
        
        topborder = outputPosY / GameObject.DIM4 - 3;
        if (topborder < 0) topborder= 0;
        
        bottomborder = (outputPosY+get2DHeight()) / GameObject.DIM4 + Map.getBlocksZ()*2;
        if (bottomborder >= Map.getBlocksY()) bottomborder = Map.getBlocksY()-1;
        
       apply(Gdx.gl10);//don't know what this does
    }
    
    /**
     * Renders the viewport
     * @param g
     * @param view  
     */
    public void render(View view) {
        if (Controller.getMap() != null) {  
            
            view.getBatch().setProjectionMatrix(combined);
             
            //the parameter for the posY  is a bit strange because the y-axis is turned
            Gdx.gl.glViewport((int) viewportPosX, (int) (Gdx.graphics.getHeight()-viewportHeight-viewportPosY),
                          (int) viewportWidth, (int) (viewportHeight));
            
            //move the viewport    
            translate(new Vector3(viewportPosX, viewportPosY, 0));
            position.set(new Vector3(outputPosX+ get2DWidth()/2 , outputPosY+ get2DHeight()/2 , 0));
                        
            

            //render map
            createDepthList();
            Controller.getMap().render(view, this);

                                    
            //reverse both translations
            //move the viewport           
            translate(new Vector3(-viewportPosX, -viewportPosY, 0));
        }
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
    public void focusOnCoords(Coordinate coord){
        focusCoordinates = coord;
        focusentity = null;
    }
    
    
    /**
     * Returns the left border of the visible area.
     * @return 
     */
    public int getLeftBorder(){
        leftborder = outputPosX / GameObject.DIMENSION - 1;
        if (leftborder < 0) leftborder= 0;
        
        return leftborder;
    }
    
    /**
     * Returns the right border of the visible area.
     * @return
     */
    public int getRightBorder(){
        rightborder = (outputPosX + get2DWidth()) / GameObject.DIMENSION + 1;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;

        return rightborder;
    }
    
    /**
     * Returns the top seight border of the deepest block
     * @return measured in blocks
     */
    public int getTopBorder(){    
        topborder = outputPosY / GameObject.DIM4 - 3;
        if (topborder < 0) topborder= 0;
        
        return topborder;
    }
    
     /**
     * Returns the bottom seight border of the highest block
     * @return measured in blocks
     */
    public int getBottomBorder(){
        bottomborder = (outputPosY+get2DHeight()) / GameObject.DIM4 + Map.getBlocksZ()*2;
        if (bottomborder >= Map.getBlocksY()) bottomborder = Map.getBlocksY()-1;
        return bottomborder;
    }
    
  /**
     * The Camera Position in the game world.
     * @return in pixels
     */
    public int getGamePosX() {
        return outputPosX;
    }

    /**
     * The Camera left Position in the game world.
     * @param x in pixels
     */
    public void setGamePosX(int x) {
        this.outputPosX = x;
    }

    /**
     * The Camera top-position in the game world.
     * @return in camera position game space
     */
    public int getGamePosY() {
        return outputPosY;
    }

    /**
     * The Camera top-position in the game world.
     * @param y in game space
     */
    public void setGamePosY(int y) {
        this.outputPosY = y;
    }

    /**
     * The amount of pixel which are visible in Y direction (game pixels). It should be equal View.RENDER_RESOLUTION_WIDTH
     * For screen pixels use <i>ViewportWidth()</i>.
     * @return in pixels
     */
    public final int get2DWidth() {
        return (int) (viewportWidth / getTotalScale());
    }
    
  /**
    * The amount of pixel which are visible in Y direction (game pixels). For screen pixels use <i>ViewportHeight()</i>.
    * @return  in pixels
    */
   public final int get2DHeight() {
        return (int) (viewportHeight / getTotalScale());
    }

    /**
     * Returns the position of the cameras output (on the screen)
     * @return  in pixels
     */
    public int getViewportPosX() {
        return viewportPosX;
    }

    /**
     * Returns the position of the camera (on the screen)
     * @return
     */
    public int getViewportPosY() {
        return viewportPosY;
    }
    
    /**
     * Returns the height of the camera output.
     * To get the real display size multiply it with scale values.
     * @return the value before scaling
     */
    public float getViewportHeight() {
        return viewportHeight;
    }

    /**
     * Returns the width of the camera output before scale.
     * To get the real display size multiply it with scale value.
     * @return the value before scaling
     */
    public float getViewportWidth() {
        return viewportWidth;
    }


   
    
     /**
     * Fills the map into a list and sorts it in the order of the rendering, called the "depthlist".
     */
    private void createDepthList() {
        depthsort.clear();
        for (int x = leftborder; x < rightborder;x++)
            for (int y = topborder; y < bottomborder;y++)
                for (int z=0; z < Map.getBlocksZ(); z++){
                    
                    Coordinate coord = new Coordinate(x, y, z, true); 
                    if (! coord.getBlock().isHidden()
                        && coord.getBlock().isVisible()
                        && 
                            coord.getBlock().get2DPosY(coord)
                        <
                            outputPosY + get2DHeight()
                    ) {
                        depthsort.add(new Renderobject(coord, -1));
                    }
                }
        
        //add entitys
        for (int i=0; i< Controller.getMap().getEntitylist().size(); i++) {
            AbstractEntity entity = Controller.getMap().getEntitylist().get(i);
            if (!entity.isHidden() && entity.isVisible()
                        && 
                            entity.get2DPosY(
                                entity.getCoords()
                            )
                        <
                            outputPosY + get2DHeight()
                    )
                    depthsort.add(
                        new Renderobject(entity, i)
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
    protected Coordinate getDepthsortCoord(int index) {
        Renderobject item = depthsort.get(index);
        Coordinate triple = item.getCoords();
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
                    boolean notCalculatable = !block.hasSides() || new Coordinate(x,y,z, true).hasOffset();
                    block.setVisible(notCalculatable);
                    if (notCalculatable) {
                        block.setSideVisibility(0, true);
                        block.setSideVisibility(1, true);
                        block.setSideVisibility(2, true);
                    }
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
                        && new Coordinate(x - (y%2 == 0 ? 1:0), y+1, z, true).hidingPastBlock())
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
                        && new Coordinate(x - (y%2 == 0 ? 1:0), y+1, z+1, true).hidingPastBlock())
                        left = false;
                    if (y < Map.getBlocksY()-2
                        && new Coordinate(x, y+2, z, true).hidingPastBlock()
                        )
                        right = false;
                    
                } else {              
                    if (side == 1) {//check top side
                        if (Controller.getMapData(x, y, z).hasSides()//block on top
                            && z+1 < Map.getBlocksZ()
                            && new Coordinate(x, y, z+1, true).hidingPastBlock())
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
                            && new Coordinate(x - (y%2 == 0 ? 1:0), y+1, z+1, true).hidingPastBlock())
                            left = false;
                        
                        if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && new Coordinate(x + (y%2 == 0 ? 0:1), y+1, z+1, true).hidingPastBlock()
                            )
                            right = false;
                          
                    } else {
                        if (side==2){
                            //block on right hiding the whole right side
                            if (Controller.getMapData(x, y, z).hasSides()//block on top
                                && x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY()
                                && new Coordinate(x + (y%2 == 0 ? 0:1), y+1, z, true).hidingPastBlock()
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
                                new Coordinate(x, y+2, z, true).hidingPastBlock())
                                left = false;
                            
                            if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                                &&
                                new Coordinate(x + (y%2 == 0 ? 0:1), y+1, z+1, true).hidingPastBlock())
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
                && (!new Coordinate(x, y, z, true).hidingPastBlock() || new Coordinate(x, y, z, true).hasOffset()));
    }
    
    /**
     * Traces the ray to a specific block.
     * @param coord The coordinate where the ray should point to.
     * @param neighbours True when neighbours block also should be scanned
     */
    public void traceRayTo(Coordinate coord, boolean neighbours){
        Block block = coord.getBlock();
        int[] coords = coord.getRel();
                    
        //Blocks with offset are not in the grid, so can not be calculated => always visible
        block.setVisible(
            !block.hasSides() || coord.hasOffset()
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
                Controller.getMapData(coords[0], coords[1], level).setLightlevel((255 * level) / topmost);
            }
        }
    }

    @Override
    public void update(boolean updateFrustum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
