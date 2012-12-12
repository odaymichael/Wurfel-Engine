package com.BombingGames.Game;

import com.BombingGames.Game.Blocks.Block;
import java.awt.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * The View manages everything what should be drawn.
 * @author Benedikt
 */
public class View {
    /**
     * The camera which display everything
     */
    public Camera camera;
    
    /**
     * The reference for the graphics context
     */
    public Graphics g = null; 
    private java.awt.Font font;
    /**
     * Contains a bigger font.
     */
    public static TrueTypeFont tTFont;
    /**
     * Contains a font
     */
    public static TrueTypeFont tTFont_small;
    
   
    private GameContainer gc;
    //private static Insets insets;
    private Font baseFont;

    /**
     * Creates a View
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException {
        this.gc = gc;  
       
        // initialise the font which CAUSES LONG LOADING TIME!!!
        TrueTypeFont trueTypeFont;

        //startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
        UnicodeFont startFont = new UnicodeFont("com/BombingGames/Game/Blox2.ttf", 20, false, false);
        //baseFont = startFont.deriveFont(Font.PLAIN, 12);
        baseFont = startFont.getFont().deriveFont(Font.PLAIN, 12);
        
        tTFont = new TrueTypeFont(baseFont, true);
        
        camera = new Camera(
            0,//top
            0,//left
            gc.getWidth(),//full WIDTH
            gc.getHeight(),//full HEIGHT
            gc.getHeight()*4 / (float)(Chunk.BLOCKS_Y* Block.HEIGHT*2)//zoom factor
            );
        
        if (camera.getYzHeight() > Chunk.SizeY) Gameplay.MSGSYSTEM.add("The chunks are too small for this camera height/resolution", "Warning");
        
        
        
        /*font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);*/
        
        //update resolution things
        Gameplay.MSGSYSTEM.add("Resolution: " + gc.getWidth() + " x " +gc.getHeight());
        
        Block.reloadSprites(camera.getZoom()); 
        // Block.WIDTH = Block.HEIGHT;
        Gameplay.MSGSYSTEM.add("Blocks: "+Block.WIDTH+" x "+Block.HEIGHT);
        Gameplay.MSGSYSTEM.add("Zoom: "+ camera.getZoom());
        Gameplay.MSGSYSTEM.add("chunk: "+ Chunk.SizeX+" x "+Chunk.SizeY);
        Gameplay.MSGSYSTEM.add("chunk w/ zoom: "+Chunk.SizeX*camera.getZoom()+" x "+Chunk.SizeY*camera.getZoom());
    }
    
    /**
     * Main method which is called every time
     * @param game
     * @param g 
     * @throws SlickException
     */
    public void render(StateBasedGame game, Graphics g) throws SlickException{
        this.g = g;
        camera.draw();      
    }
      
//    /**
//     * 
//     * @throws SlickException
//     */
//    public void draw_all_Chunks() throws SlickException{
//        /*                        
//        /*-------------------*/ 
//        /*---draw lines-------*/
//        /*-------------------*/ 
//
//         /*glBegin(GL_LINES)
//        glVertex2f(x1, y1): glVertex2f(x2, y2)
//        glEnd()*/
//     /*
//        //Komponenten der Punkte
//        int start1X = chunk.posX;
//        int start1Y = chunk.posY;
//        int end1X = chunk.posX;
//        int end1Y = chunk.posY;
//        
//        int start2X = chunk.posX;
//        int start2Y = chunk.posY;
//        int end2X = chunk.posX + Chunk.WIDTH;
//        int end2Y = chunk.posY;
//
//        //Countervariables
//        int s1x=0;int s1y=0;int e1x=0;int e1y=0;
//        int s2x=0;int s2y=0;int e2x=0;int e2y=0;
//
//        for (int i=0; i < (chunk.data.length)*2 + (chunk.data[0].length); i++){
//            //g2d.setColor(new Color(i*5+20,i*5+20,0));
//
//            //Linie 1 (von unten links nach oben rechts) /
//            if (s1y > chunk.data[0].length) {
//                s1x++;
//                start1X = chunk.posX + s1x*Controller.tilesizeX/2;                    
//            } else {
//                s1y++;
//                start1Y = chunk.posY + s1y*Controller.tilesizeY/2;       
//            }         
//            
//            
//            if (e1x+1 > chunk.data.length*2) {
//                e1y++;
//                end1Y = chunk.posY + e1y*Controller.tilesizeY/2;
//            } else{
//                e1x++; 
//                end1X = chunk.posX + e1x*Controller.tilesizeX/2;                    
//            }
//
//            g.drawLine(
//                start1X + (i%2)*Controller.tilesizeX/2,
//                start1Y,
//                end1X + (i%2)*Controller.tilesizeX/2,
//                end1Y
//            );
//
//
//            //Linie 2  (von oben links nach utnen rechts)  \       
//            if (s2x < chunk.data.length*2) {
//                s2x++;
//                start2X = chunk.posX + Chunk.WIDTH - s2x*Controller.tilesizeX/2;
//            } else {
//                s2y++;
//                start2Y = chunk.posY + s2y*Controller.tilesizeY/2;
//            }
//
//            if (e2y-1 < chunk.data[0].length){
//                e2y++;
//                end2Y = chunk.posY + e2y*Controller.tilesizeY/2;                    
//            } else {
//                e2x++;
//                end2X = chunk.posX + Chunk.WIDTH - e2x*Controller.tilesizeX/2;                   
//            }
//
//            g.drawLine(
//                start2X + (i%2)*Controller.tilesizeX/2,
//                start2Y,
//                end2X + (i%2)*Controller.tilesizeX/2,
//                end2Y
//            );
//        }*/
//        
//     };

    
 /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    public void raytracing(){
        //set visibility of every block to false
        for (int x=0;x < Chunk.BLOCKS_X*3;x++)
            for (int y=0;y < Chunk.BLOCKS_Y*3;y++)
                for (int z=0;z < Chunk.BLOCKS_Z;z++)
                    Controller.getMapData(x, y, z).setVisible(false);
                
        //send rays through top of the map
        for (int x=0; x < Chunk.BLOCKS_X*3; x++)
            for (int y=0; y < Chunk.BLOCKS_Y*3 + Chunk.BLOCKS_Z*2; y++)
                for (int side=0; side < 3; side++)
                    trace_ray(
                        x,
                        y,
                        Chunk.BLOCKS_Z-1,
                        side
                    );
    }

    /**
     * Traces a single ray-
     * @param x The starting x-coordinate.
     * @param y The starting y-coordinate.
     * @param z The starting z-coordinate.
     * @param side The sides ray traces
     */
    private void trace_ray(int x, int y, int z, int side){
        if (Gameplay.controller.renderSides()){//trace ray on every side
            boolean left = true;
            boolean right = true;
            
            while (y >= Chunk.BLOCKS_Y*3){
                y -= 2;
                z--;
            }
            
            y += 2;
            z++;   
            if (z > 0) 
                do {
                    y -= 2;
                    z--;

                    if (side == 0){
                    //direct neighbour block on left hiding the complete left side
                        if (x > 0 && y < Chunk.BLOCKS_Y-1
                            && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z).isTransparent())
                        break; //stop ray

                        //two blocks hiding the left side
                        if (x > 0 && y < Chunk.BLOCKS_Y-1 && z < Chunk.BLOCKS_Z-1
                            && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                            left = false;
                        if (y < Chunk.BLOCKS_Y-2 &&
                            ! Controller.getMapDataUnsafe(x, y+2, z).isTransparent())
                            right = false;

                        if (left || right) //as long one part of the side is visible save it
                            Controller.getMapDataUnsafe(x, y, z).setSideVisibility(0, true);
                        else break;//if side is hidden stop ray
                    } else                 
                        if (side == 1) {//check top side
                            if (z < Chunk.BLOCKS_Z-1
                                && ! Controller.getMapDataUnsafe(x, y, z+1).isTransparent()
                                )
                                break;   

                            //two 0- and 2-sides hiding the side 1
                            if (x>0 && y < Chunk.BLOCKS_Y-1 && z < Chunk.BLOCKS_Z-1
                                && ! Controller.getMapDataUnsafe(x - (y%2 == 0 ? 1:0), y+1, z+1).isTransparent())
                                left = false;
                            if (x < Chunk.BLOCKS_X-1  && y < Chunk.BLOCKS_Y-1 && z < Chunk.BLOCKS_Z-1
                                && ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent())
                                right = false;

                            if (left || right)
                                Controller.getMapDataUnsafe(x, y, z).setSideVisibility(1, true);
                            else break;
                        } else
                            if (side==2){
                                //block on right hiding the right side
                                if (x < Chunk.BLOCKS_X-1 && y < Chunk.BLOCKS_Y-1
                                    && ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z).isTransparent()
                                    )
                                    break;

                                //two blocks hiding the rightside
                                if (y < Chunk.BLOCKS_Y-2 &&
                                    ! Controller.getMapDataUnsafe(x, y+2, z).isTransparent()
                                    )
                                    left = false;
                                if (x < Chunk.BLOCKS_X-1 && y < Chunk.BLOCKS_Y-1 && z < Chunk.BLOCKS_Z-1
                                    &&
                                    ! Controller.getMapDataUnsafe(x + (y%2 == 0 ? 0:1), y+1, z+1).isTransparent()
                                    )
                                    right = false;

                                if (left || right)
                                    Controller.getMapDataUnsafe(x, y, z).setSideVisibility(2, true);
                                else break;
                            }
            } while (y >= 2 && z >= 1 && Controller.getMapDataUnsafe(x, y, z).isTransparent());
//           Take the last block
//            if (y >= 0 && z >= 0 && z < Chunk.BLOCKS_Z-1 && (mapdata[x][y][z+1].isTransparent())){
//                renderarray[x][y][z] = mapgetData(x, y, z);
//                renderarray[x][y][z].renderTop = true;
//            }
        } else{
            //trace ray until it found a not isTransparent() block
            boolean leftHalfHidden = false;
            boolean rightHalfHidden = false;
            while ((y >= 0) && (z >= 0) && Controller.getMapData(x, y, z).isTransparent()) {
                //check blocks hidden by 4 other halfs
                if (! Controller.getMapData(x - (x%2 == 0? 1 : 0), y-1, z).isTransparent() )
                    leftHalfHidden = true;
                if (! Controller.getMapData(x + (x%2==0?0:1), y-1, z).isTransparent())
                    rightHalfHidden = true;        

                if ((leftHalfHidden && rightHalfHidden)){
                    break;
                } else {
                    //save every isTransparent() block
                    if (Controller.getMapData(x, y, z).isTransparent())
                        Controller.getMapData(x, y, z).setVisible(true);

                    //check if it has offset, not part of the original raytracing, but checking it here saves iteration. mapdata and renderarray are for the field with x,y,z equal
                    if (Controller.getMapData(x, y, z).getOffsetY() > 0)
                        Controller.getMapData(x, y, z).renderorder = 1;
                    else if (Controller.getMapData(x, y, z).getOffsetX() < 0 && Controller.getMapData(x, y, z).getOffsetY() < 0)
                            Controller.getMapData(x, y, z).renderorder = -1;
                        else Controller.getMapData(x, y, z).renderorder = 0;
                }
                y -= 2;
                z--;                       
            }
            
            //make the last block visible when came to an end
            if ((y >= 0) && (z>=0))
                Controller.getMapData(x, y, z).setVisible(true);
       }
    }
    
    /**
     * Calculates the light level based on the sun shining straight from the top
     */
    public void calc_light(){
        for (int x=0; x < Chunk.BLOCKS_X*3; x++){
            for (int y=0; y < Chunk.BLOCKS_Y*3; y++) {
                
                //find top most Block
                int topmost = Chunk.BLOCKS_Z-1;
                while (Controller.getMapData(x, y, topmost).isTransparent() == true && topmost > 0 ){
                    topmost--;
                }
                
                //start at topmost block and go down. Every step make it a bit darker
                for (int level=topmost; level > 0; level--)
                    Controller.getMapData(x, y, level).setLightlevel(level*50 / topmost);
            }
        }         
    }
}