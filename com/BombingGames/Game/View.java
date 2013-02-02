package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * The View manages everything what should be drawn.
 * @author Benedikt
 */
public class View {
    /**
     * Contains a font
     */
    public static AngelCodeFont baseFont;

    private Camera camera;
    private float equalizationScale;    
    
    /**
     * Creates a View
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException {
        // initialise the font which CAUSES LONG LOADING TIME!!!
        //TrueTypeFont trueTypeFont;

        //startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
        //UnicodeFont startFont = new UnicodeFont("com/BombingGames/Game/Blox2.ttf", 20, false, false);
        baseFont = new AngelCodeFont("com/BombingGames/Game/Blox.fnt","com/BombingGames/Game/Blox.png");
        //baseFont = startFont.deriveFont(Font.PLAIN, 12);
        //baseFont = startFont.getFont().deriveFont(Font.PLAIN, 18);
        //tTFont = new TrueTypeFont(baseFont, true);
        
        //default rendering size is FullHD
        equalizationScale = gc.getWidth()/1920f;
        Log.debug("Scale is:" + Float.toString(equalizationScale));
        
        camera = new Camera(
            0, //top
            0, //left
            gc.getWidth(), //full width
            gc.getHeight(), //full height
            equalizationScale
        );
        
        //camera.FocusOnBlock(new Blockpointer(Chunk.getBlocksX()*3/2,Map.getBlocksY()/2,Chunk.getBlocksZ()/2));
        
        if (camera.getTotalHeight() > Chunk.getBlocksY()*Block.HEIGHT/2) {
            Gameplay.MSGSYSTEM.add("The chunks are maybe too small for this camera height/resolution to grant a stable experience", "Warning");
            Log.warn("The chunks are maybe too small for this camera height/resolution to grant a stable experience");
        }
        
     /*font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);*/
        
        //update resolution things
        Gameplay.MSGSYSTEM.add("Resolution: " + gc.getWidth() + " x " +gc.getHeight());
        
        Block.loadSpriteSheet(); 
        Gameplay.MSGSYSTEM.add("Blocks: "+ Block.WIDTH+" x "+Block.HEIGHT);
        Gameplay.MSGSYSTEM.add("Zoom: "+ camera.getZoom());
        Gameplay.MSGSYSTEM.add("AbsZoom: "+ camera.getZoom()*equalizationScale);
     }
    
    /**
     * Main method which is called every time
     * @param game
     * @param g 
     * @throws SlickException
     */
    public void render(StateBasedGame game, Graphics g) throws SlickException{
        g.scale(equalizationScale, equalizationScale);
        camera.draw();
        Gameplay.MSGSYSTEM.draw(); 
    }
       
    /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    protected void raytracing(){        
        //set visibility of every block to false, except blocks with offset
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY(); y++)
                for (int z=0; z < Chunk.getBlocksZ(); z++) {
                    
                    Block block = Controller.getMapDataUnsafe(x, y, z);
                    if (block.hasOffset()){
                        block.setVisible(true);//Blocks with offset are not in the grid, so ignore them
                        Controller.getMapData(x, y, z-1).setVisible(true);
                    } else {
                        block.setVisible(false);
                    }
                    
                }
                
        //send the rays through top of the map
        for (int x=0; x < Map.getBlocksX(); x++)
            for (int y=0; y < Map.getBlocksY() + Chunk.getBlocksZ()*2; y++)
                
                for (int side=0; side < 3; side++)
                    traceRay(
                        x,
                        y,
                        Chunk.getBlocksZ()-1,
                        side
                    );
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
            z-= (y-Map.getBlocksY())/2;
            if (y % 2 == 0)
                y=Map.getBlocksY()-1;
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
                        && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z).isTransparent())
                    break; //stop ray
                    
                    //liquid
                    if (Controller.getMapDataUnsafe(x, y, z).isLiquid()){
                        if (x > 0 && y < Map.getBlocksY()-1
                        && Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z).isLiquid())
                            liquidfilter=true;
                        if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                            leftliquid = false;
                        if (y < Map.getBlocksY()-2 &&
                            Controller.getMapDataUnsafe(x, y+2, z).isLiquid())
                            rightliquid = false;
                        if (!leftliquid && !rightliquid) liquidfilter=true;
                    } 

                    //two blocks hiding the left side
                    if (x > 0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                        && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                        left = false;
                    if (y < Map.getBlocksY()-2 &&
                        ! Controller.getMapDataUnsafe(x, y+2, z).isTransparent())
                        right = false;

                    if (left || right){ //as long one part of the side is visible save it
                        Block temp = Controller.getMapDataUnsafe(x, y, z);
                        if (!(liquidfilter && temp.isLiquid())){
                            temp.setSideVisibility(0, true);
                        }else liquidfilter=false;
                    } else break;//if side is hidden stop ray
                } else {              
                    if (side == 1) {//check top side
                        if (z < Map.getBlocksZ()-1
                            && ! Controller.getMapDataUnsafe(x, y, z+1).isTransparent())
                            break;
                        
                        //liquid
                        if (Controller.getMapDataUnsafe(x, y, z).isLiquid()){
                            if (z < Map.getBlocksZ()-1 && Controller.getMapDataUnsafe(x, y, z+1).isLiquid())
                                break;
                            if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                && Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isLiquid())
                                leftliquid = false;
                            
                            if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                &&  Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                rightliquid = false;
                            
                            if (!leftliquid && !rightliquid) liquidfilter=true;
                        }
                    
                        //two 0- and 2-sides hiding the side 1
                        if (x>0 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                            left = false;
                        
                        if (x < Map.getBlocksX()-1  && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                            && ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent())
                            right = false;
                          
                        if (left || right){ //as long one part of the side is visible save it
                            Block temp = Controller.getMapDataUnsafe(x, y, z);
                            if (!(liquidfilter && temp.isLiquid())){
                                temp.setSideVisibility(1, true);
                            }else liquidfilter=false;
                        } else break;//if side is hidden stop ray
                    } else {
                        if (side==2){
                            //block on right hiding the right side
                            if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1
                                && ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z).isTransparent())
                                break;
                            
                            //liquid
                            if (Controller.getMapDataUnsafe(x, y, z).isLiquid()){
                               if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1
                                && Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z).isLiquid())
                                    break;
                                if (y < Map.getBlocksY()-2
                                    &&
                                    Controller.getMapDataUnsafe(x, y+2, z).isLiquid())
                                    leftliquid = false;
                                
                                if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                    &&
                                    Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isLiquid())
                                    rightliquid = false;
                                
                                if (!leftliquid && !rightliquid) liquidfilter = true;
                            }

                            //two blocks hiding the right side
                            if (y < Map.getBlocksY()-2
                                &&
                                ! Controller.getMapDataUnsafe(x, y+2, z).isTransparent())
                                left = false;
                            
                            if (x < Map.getBlocksX()-1 && y < Map.getBlocksY()-1 && z < Map.getBlocksZ()-1
                                &&
                                ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent())
                                right = false;
                            
                            if (left || right){ //as long one part of the side is visible save it
                                Block temp = Controller.getMapDataUnsafe(x, y, z);
                                if (!(liquidfilter && temp.isLiquid())){
                                    temp.setSideVisibility(2, true);
                                }else liquidfilter=false;
                            } else break;//if side is hidden stop ray
                        }
                    }
                }
            } while (y >= 2 && z >= 1 && (Controller.getMapDataUnsafe(x, y, z).isTransparent() || Controller.getMapDataUnsafe(x, y, z).hasOffset()));
    }
    
    /**
     * Traces the ray to this block.
     * @param x
     * @param y
     * @param z
     * @param allsides 
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
    }
    /**
     * Calculates the light level based on the sun shining straight from the top
     */
    public void calc_light(){
        for (int x=0; x < Chunk.getBlocksX()*3; x++){
            for (int y=0; y < Map.getBlocksY(); y++) {
                
                //find top most block
                int topmost = Chunk.getBlocksZ()-1;
                while (Controller.getMapData(x, y, topmost).isTransparent() == true && topmost > 0 ){
                    topmost--;
                }
                
                if (topmost>0) {
                    //start at topmost block and go down. Every step make it a bit darker
                    for (int level=topmost; level >= 0; level--)
                        Controller.getMapData(x, y, level).setLightlevel(level*50 / topmost);
                }
            }
        }         
    }

    public float getEqualizationScale() {
        return equalizationScale;
    }

    public Camera getCamera() {
        return camera;
    }

}