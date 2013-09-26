package com.BombingGames.EngineCore;

import com.BombingGames.EngineCore.Map.Cell;
import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.EngineCore.Gameobjects.AbstractEntity;
import com.BombingGames.EngineCore.Gameobjects.Block;
import com.BombingGames.EngineCore.Gameobjects.AbstractGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *Creates a virtual camera wich displays the game world on the viewport.  
 * @author Benedikt Vogler
 */
public class WECamera extends Camera {
    /**
     *
     */
    public static final boolean[][] DEEPEST_LAYER_VISIVBILITY = new boolean[Map.getBlocksX()][Map.getBlocksY()];
    
    /** the position on the screen*/
    private final int viewportPosX, viewportPosY;
    
    private int outputPosX, outputPosY;
    private int leftborder, topborder, rightborder, bottomborder;
    private float zoom = 1;
    private float equalizationScale = 1;
    
    private Coordinate focusCoordinates;
    private AbstractEntity focusentity;
    private final ArrayList<Renderobject> depthsort = new ArrayList<Renderobject>();
    
    private final Block groundBlock;
    

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
        
        groundBlock = Block.getInstance(44);//set the ground level groundBlock
        groundBlock.setSideClipping(0, true);
        groundBlock.setSideClipping(2, true);
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
        GameplayScreen.msgSystem().add("Creating new camera which is focusing a coordinate");
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
        GameplayScreen.msgSystem().add("Creating new camera which is focusing an entity: "+focusentity.getName());
        this.focusentity = focusentity;
        this.focusCoordinates = null;
    }
    
     /**
     * Updates the camera.
     */
    @Override
    public void update() {       
        //refrehs the camera's position in the game world
        if (focusCoordinates != null) {
            outputPosX = focusCoordinates.getBlock().get2DPosX(focusCoordinates) - get2DWidth() / 2 - AbstractGameObject.SCREEN_DEPTH2;
            outputPosY = focusCoordinates.getBlock().get2DPosY(focusCoordinates) - get2DHeight() / 2;
        } else if (focusentity != null ){
            outputPosX = focusentity.get2DPosX(null) - get2DWidth()/2 + AbstractGameObject.SCREEN_DEPTH2;            
            outputPosY = focusentity.get2DPosY(null) - get2DHeight()/2 ;
        }
        
        position.set(outputPosX+ get2DWidth()/2 , outputPosY+ get2DHeight()/2 , 0); 
        view.setToLookAt(position, new Vector3(position).add(direction), up);//move camera to the focus 
       
        //orthographic camera, libgdx stuff
        projection.setToOrtho(
            1/(zoom*equalizationScale) * -viewportWidth / 2,
            1/(zoom*equalizationScale) * viewportWidth / 2,
            1/(zoom*equalizationScale) * -viewportHeight / 2,
            1/(zoom*equalizationScale) * viewportHeight / 2,
            0,
            Math.abs(far)
        );
        
        //set up projection matrices
        combined.set(projection);
        Matrix4.mul(combined.val, view.val);

        invProjectionView.set(combined);
        Matrix4.inv(invProjectionView.val);
        frustum.update(invProjectionView);
        apply(Gdx.gl10);//don't know what this does
    }
    
   @Override
    public void update(boolean updateFrustum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Renders the viewport
     * @param view
     * @param camera  
     */
    public void render(View view, WECamera camera) {
        if (Controller.getMap() != null) {  
            
            view.getBatch().setProjectionMatrix(combined);
             
            //set up the viewport
            Gdx.gl.glViewport(
                viewportPosX,
                (int) (Gdx.graphics.getHeight()-viewportHeight-viewportPosY),//the parameter for the posY is a bit strange because the y-axis is turned
                (int) viewportWidth,
                (int) viewportHeight
            );
            
            view.getBatch().begin();
            view.setDrawmode(GL11.GL_MODULATE);
            
            //render last layer tiles if visible
            for (int x = 0; x < Map.getBlocksX(); x++) {
                for (int y = 0; y < Map.getBlocksY(); y++) {
                    if (DEEPEST_LAYER_VISIVBILITY[x][y]){
                        int xPos = new Coordinate(x, y, -1, true).get2DPosX();//right side is  half a block more to the right
                        int yPos = new Coordinate(x, y, -1, true).get2DPosY();//the top is drawn a quarter blocks higher
                        groundBlock.renderSideAt(view, xPos, yPos, 1);
                    }
                }
            }
            
            //render map
            createDepthList();
            
            //render vom bottom to top
            for (Renderobject renderobject : depthsort) {
                renderobject.getObject().render(view, camera, renderobject.getCoords()); 
            }
            
            view.getBatch().end();
        }
    }
  
     /**
     * Fills the map into a list and sorts it in the order of the rendering, called the "depthlist".
     */
    protected void createDepthList() {
        depthsort.clear();
        
        for (int x = leftborder; x < rightborder; x++)
            for (int y = topborder; y < bottomborder; y++){
                
                //add blocks
                for (int z=0; z < Map.getBlocksZ(); z++){
                    
                    Coordinate coord = new Coordinate(x, y, z, true); 
                    if (! coord.getBlock().isHidden()
                        && !coord.getBlock().isClipped()
                        && 
                            coord.getBlock().get2DPosY(coord)
                        <
                            outputPosY + get2DHeight()
                    ) {
                        depthsort.add(new Renderobject(Controller.getMapData(coord), coord));
                    }
                }
            }
        
        //add entitys
        for (int i=0; i< Controller.getMap().getEntitys().size(); i++) {
            AbstractEntity entity = Controller.getMap().getEntitys().get(i);
            if (!entity.isHidden() && !entity.isClipped()
                && 
                entity.get2DPosY(entity.getCoords())
                <
                outputPosY + get2DHeight()
                )
                    depthsort.add(
                        new Renderobject(entity, entity.getCoords())
                    );
        }
        //sort the list
        sortDepthList(0, depthsort.size()-1);
    }
    
    /**
     * Using Quicksort to sort.
     * From small to big values.
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
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    protected static void raytracing(){ 
        //set visibility of every groundBlock to false, except blocks with offset
        Cell[][][] mapdata = Controller.getMap().getData();
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Map.getBlocksZ(); z++) {
                    Block block = mapdata[x][y][z].getBlock();
                    
                    boolean notAnalyzable = !block.hasSides()
                        || new Coordinate(x,y,z, true).hasOffset();//Blocks with offset are not in the grid, so can not be analysed => always visible
                    block.setClipped(!notAnalyzable);
                }
                
        //send the rays through top of the map
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY() + Map.getBlocksZ()*2; y++){
                traceRay(x,y, 0);
                traceRay(x,y, 1);
                traceRay(x,y, 2);
            }     
    }
    
    /**
    * Traces a single ray.
    * This costs less performance than a whole raytracing.
     * @param x The starting x-coordinate.
     * @param y The starting y-coordinate.
     * @param side The side the ray should check
     */
    private static void traceRay(int x, int y, int side){
        int z = Map.getBlocksZ()-1;//start always from top
        
        boolean left = true;
        boolean right = true;
        boolean leftliquid = false;
        boolean rightliquid = false;
        boolean liquidfilter = false;

        //bring ray to start position
        if (y > Map.getBlocksY()-1) {
            z -= (y-Map.getBlocksY())/2;
            if (y % 2 == 0)
                y = Map.getBlocksY()-1;
            else
                y = Map.getBlocksY()-2;
        }

        y += 2;
        z++;  
        do {
            y -= 2;
            z--;

            if (side == 0){
                //direct neighbour groundBlock on left hiding the complete left side
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

            } else if (side == 1) {//check top side
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

            } else if (side==2){
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
                    new Coordinate(x, y+2, z, true).hidingPastBlock()
                )
                    left = false;

                if (x+1 < Map.getBlocksX() && y+1 < Map.getBlocksY() && z+1 < Map.getBlocksZ()
                    &&
                    new Coordinate(x + (y%2 == 0 ? 0:1), y+1, z+1, true).hidingPastBlock()
                )
                    right = false;
            }

            if ((left || right) && !(liquidfilter && Controller.getMapData(x, y, z).isLiquid())){ //unless both sides are clipped don't clip the whole groundBlock
                liquidfilter = false;
                Controller.getMapData(x, y, z).setSideClipping(side, false);                            
            }                
        } while (y > 1 && z > 0 //not on bottom of map
            && (left || right) //left or right still visible
            && (!new Coordinate(x, y, z, true).hidingPastBlock() || new Coordinate(x, y, z, true).hasOffset()));
        
        //check last layer
        if ((z <= 0)
            && (left || right) //left or right still visible
            && (!new Coordinate(x, y, z, true).hidingPastBlock() || new Coordinate(x, y, z, true).hasOffset())
            ) {
            DEEPEST_LAYER_VISIVBILITY[x][y] = true;
            //Gdx.app.log("DEBUG", "Ray hit ground at:["+x+"|"+y+"] "+left+":"+right);
        } else
            DEEPEST_LAYER_VISIVBILITY[x][y]=false;
    }
    
    /**
     * Traces the ray to a specific groundBlock. This is like the raytracing but only a single ray.
     * @param coord The coordinate where the ray should point to.
     * @param neighbours True when neighbours groundBlock also should be scanned
     */
    public static void traceRayTo(Coordinate coord, boolean neighbours){
        Block block = coord.getBlock();
        int[] coords = coord.getRel();
                    
        //Blocks with offset are not in the grid, so can not be calculated => always visible
        block.setClipped(
            !block.hasSides() || coord.hasOffset()
        );
                    
       //find start position
        while (coords[2] < Map.getBlocksZ()-1){
            coords[1] += 2;
            coords[2]++;
        }
        
        //trace rays
        if (neighbours){
            traceRay(coords[0] - (coords[1]%2 == 0 ? 1:0), coords[1]-1, Block.RIGHTSIDE);
            traceRay(coords[0] + (coords[1]%2 == 0 ? 0:1), coords[1]-1, Block.LEFTSIDE);
            traceRay(coords[0], coords[1]+2, Block.TOPSIDE);
        }
        traceRay(coords[0], coords[1], Block.LEFTSIDE);
        traceRay(coords[0], coords[1], Block.TOPSIDE);             
        traceRay(coords[0], coords[1], Block.RIGHTSIDE);
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
     * Use this if you want to focus on a special groundBlock.
     * @param coord the coordaintes of the groundBlock.
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
        leftborder = outputPosX / AbstractGameObject.SCREEN_WIDTH - 1;
        if (leftborder < 0) leftborder= 0;
        
        return leftborder;
    }
    
    /**
     * Returns the right border of the visible area.
     * @return
     */
    public int getRightBorder(){
        rightborder = (outputPosX + get2DWidth()) / AbstractGameObject.SCREEN_WIDTH + 1;
        if (rightborder >= Map.getBlocksX()) rightborder = Map.getBlocksX()-1;

        return rightborder;
    }
    
    /**
     * Returns the top seight border of the deepest groundBlock
     * @return measured in blocks
     */
    public int getTopBorder(){    
        topborder = outputPosY / AbstractGameObject.SCREEN_DEPTH2 - 3;
        if (topborder < 0) topborder= 0;
        
        return topborder;
    }
    
     /**
     * Returns the bottom seight border of the highest groundBlock
     * @return measured in blocks
     */
    public int getBottomBorder(){
        bottomborder = (outputPosY+get2DHeight()) / AbstractGameObject.SCREEN_DEPTH2 + Map.getBlocksZ()*2;
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
}