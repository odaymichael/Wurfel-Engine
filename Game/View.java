package Game;

import Game.Blocks.Block;
import java.awt.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;


/**
 * 
 * @author Benedikt
 */
public class View {

    /**
     * the array which is created after analysing the Map.data
     */
   // private Block renderarray[][][] = new Block[Chunk.BlocksX*3][Chunk.BlocksY*3][Chunk.BlocksZ];
    /**
     * 
     */
    public Camera camera;
    

    public Graphics g = null; 
    private java.awt.Font font;
    /**
     * 
     */
    public static TrueTypeFont tTFont;
    /**
     * 
     */
    public static TrueTypeFont tTFont_small;
    
   
    private GameContainer gc;
    //private static Insets insets;
    private Font baseFont;

    
    /**
     * Constructor
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException {
        this.gc = gc;  
       
        // initialise the font which CAUSES LONG LOADING TIME!!!
        TrueTypeFont trueTypeFont;

        //startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
        UnicodeFont startFont = new UnicodeFont("Game/Blox2.ttf", 20, false, false);
        //baseFont = startFont.deriveFont(Font.PLAIN, 12);
        baseFont = startFont.getFont().deriveFont(Font.PLAIN, 12);
     
        tTFont = new TrueTypeFont(baseFont, true);
        
        camera = new Camera(gc,gc.getHeight()*4 / (float)(Chunk.BlocksY* Block.height));
        
        
        /*font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);*/
        
        //update resolution things
        Gameplay.iglog.add("Resolution: " + gc.getWidth() + " x " +gc.getHeight());
        
        Block.reloadSprites(camera.getZoom()); 
        // Block.width = Block.height;
        Gameplay.iglog.add("Blocks: "+Block.width+" x "+Block.height);
        Gameplay.iglog.add("Zoom: "+camera.getZoom());
        Gameplay.iglog.add("chunk: "+Chunk.SizeX+" x "+Chunk.SizeY);
        Gameplay.iglog.add("chunk w/ zoom: "+Chunk.SizeX*camera.getZoom()+" x "+Chunk.SizeY*camera.getZoom());
  
        
    }
    
    /**
     * Main method which is called every time
     * @param game
     * @param g 
     * @throws SlickException
     */
    public void render(StateBasedGame game, Graphics g) throws SlickException{
        this.g = g;
        Controller.map.draw();
        //Controller.player.draw();
        
        //GUI
        if (Controller.map.getMinimap() != null)
            Controller.map.getMinimap().draw(g);
        drawiglog();
    }
      
    /**
     * 
     * @throws SlickException
     */
    public void draw_all_Chunks() throws SlickException{
        /*                        
        /*-------------------*/ 
        /*---draw lines-------*/
        /*-------------------*/ 

         /*glBegin(GL_LINES)
        glVertex2f(x1, y1): glVertex2f(x2, y2)
        glEnd()*/
     /*
        //Komponenten der Punkte
        int start1X = chunk.posX;
        int start1Y = chunk.posY;
        int end1X = chunk.posX;
        int end1Y = chunk.posY;
        
        int start2X = chunk.posX;
        int start2Y = chunk.posY;
        int end2X = chunk.posX + Chunk.width;
        int end2Y = chunk.posY;

        //Countervariables
        int s1x=0;int s1y=0;int e1x=0;int e1y=0;
        int s2x=0;int s2y=0;int e2x=0;int e2y=0;

        for (int i=0; i < (chunk.data.length)*2 + (chunk.data[0].length); i++){
            //g2d.setColor(new Color(i*5+20,i*5+20,0));

            //Linie 1 (von unten links nach oben rechts) /
            if (s1y > chunk.data[0].length) {
                s1x++;
                start1X = chunk.posX + s1x*Controller.tilesizeX/2;                    
            } else {
                s1y++;
                start1Y = chunk.posY + s1y*Controller.tilesizeY/2;       
            }         
            
            
            if (e1x+1 > chunk.data.length*2) {
                e1y++;
                end1Y = chunk.posY + e1y*Controller.tilesizeY/2;
            } else{
                e1x++; 
                end1X = chunk.posX + e1x*Controller.tilesizeX/2;                    
            }

            g.drawLine(
                start1X + (i%2)*Controller.tilesizeX/2,
                start1Y,
                end1X + (i%2)*Controller.tilesizeX/2,
                end1Y
            );


            //Linie 2  (von oben links nach utnen rechts)  \       
            if (s2x < chunk.data.length*2) {
                s2x++;
                start2X = chunk.posX + Chunk.width - s2x*Controller.tilesizeX/2;
            } else {
                s2y++;
                start2Y = chunk.posY + s2y*Controller.tilesizeY/2;
            }

            if (e2y-1 < chunk.data[0].length){
                e2y++;
                end2Y = chunk.posY + e2y*Controller.tilesizeY/2;                    
            } else {
                e2x++;
                end2X = chunk.posX + Chunk.width - e2x*Controller.tilesizeX/2;                   
            }

            g.drawLine(
                start2X + (i%2)*Controller.tilesizeX/2,
                start2Y,
                end2X + (i%2)*Controller.tilesizeX/2,
                end2Y
            );
        }*/
        
     };

    
 /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    public void raytracing(){
        //set visibility of every block to false
        for (int x=0;x < Chunk.BlocksX*3;x++)
            for (int y=0;y < Chunk.BlocksY*3;y++)
                for (int z=0;z < Chunk.BlocksZ;z++)
                    Controller.map.data[x][y][z].setVisible(false);

        //check top of big chunk
        for (int x=0; x < Controller.map.data.length; x++)
            for (int y=0; y < Controller.map.data[0].length; y++)
                for (int side=0; side < 3; side++)
                    trace_ray(
                        Controller.map.data,
                        x,
                        y,
                        Controller.map.data[0][0].length-1,
                        side
                    );
            
        //check front side
        for (int x=0; x < Controller.map.data.length; x++)
            for (int z=0; z < Controller.map.data[0][0].length-1 ; z++)
                for (int side=0; side < 3; side++)
                trace_ray(
                    Controller.map.data,
                    x,
                    Controller.map.data[0].length-1,
                    z,
                    side
                );
        if (!Gameplay.controller.rendermethod){
            //for the shifted row again
            for (int x=0; x < Controller.map.data.length; x++)
                for (int z=0; z < Controller.map.data[0][0].length-1 ; z++)            
                    trace_ray(
                        Controller.map.data,
                        x,
                        Controller.map.data[0].length-2,
                        z,
                        0
                    );
       }    
       Controller.map.requestRecalc();
    }

    private void trace_ray(Block[][][] mapdata, int x, int y, int z, int side){
        if (Gameplay.controller.rendermethod){
            boolean leftside = false;
            boolean rightside = false;
            y += 2;
            z++;
            do {
                y -= 2;
                z--;
                if (side == 0){
                    if (mapdata[x][y][z].getId() != 0) {
                        //put every isTransparent() block with isTransparent() block on top in renderarrray, except air.
                        Controller.map.data[x][y][z].setVisible(true);
                        Controller.map.data[x][y][z].renderLeft = true;
                    }
                    
                    //block on left hiding the side
                    if (x>0 && y>0 && z>0 && ! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z-1].isTransparent()) break;
                    
                    //two blocks hiding the side
                    if (x>0 && y>0) {
                        if (! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z].isTransparent()) leftside = true;
                        if (z>0 && ! mapdata[x][y][z-1].isTransparent()) rightside = true;
                     }                    
                } else                 
                    if (side == 1) {
                        if (mapdata[x][y][z].getId() != 0 && (z == Chunk.BlocksZ-1 || mapdata[x][y][z+1].isTransparent())) {
                            //put every isTransparent() block with isTransparent() block on top in renderarrray, except air.
                            Controller.map.data[x][y][z].setVisible(true);
                            Controller.map.data[x][y][z].renderTop = true;
                        }

                       //two 0- and 2-sides hiding the side 1
                        if (x>0 && y>0) {
                            if (! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z].isTransparent()) leftside = true;
                            if (x < Chunk.BlocksX*3-1 && ! mapdata[x + (y%2 == 0 ? 0:1)][y-1][z].isTransparent()) rightside = true;
                        }

                    } else
                        if (side==2){
                            if (mapdata[x][y][z].getId() != 0) {
                                //put every isTransparent() block with isTransparent() block on top in renderarrray, except air.
                                Controller.map.data[x][y][z].setVisible(true);
                                Controller.map.data[x][y][z].renderRight = true;
                            }
                    
                            //block on right hiding the side
                            if (x < Chunk.BlocksX*3-1 && y>0 && z>0 && ! mapdata[x + (y%2 == 0 ? 0:1)][y-1][z-1].isTransparent()) break;
                    
                            //two blocks hiding the side
                            if (x>0 && z>0) {
                                if (z>0 && ! mapdata[x][y][z-1].isTransparent()) leftside = true;
                                if (x < Chunk.BlocksX*3-1 && ! mapdata[x + (y%2 == 0 ? 0:1)][y][z-1].isTransparent()) rightside = true;
                            } 
                        }
                
           } while (y > 1 && z > 0 && mapdata[x][y][z].isTransparent() && !(leftside && rightside));
//           Take the last block
//            if (y >= 0 && z >= 0 && z < Chunk.BlocksZ-1 && (mapdata[x][y][z+1].isTransparent())){
//                renderarray[x][y][z] = mapdata[x][y][z];
//                renderarray[x][y][z].renderTop = true;
//            }
        } else{
            //trace ray until it found a not isTransparent() block
            boolean leftHalfHidden = false;
            boolean rightHalfHidden = false;
            while ((y >= 0) && (z >= 0) && (mapdata[x][y][z].isTransparent())) {
                //check blocks hidden by 4 other halfs
                if ( x>0 && y>0 && ! mapdata[x - (x%2 == 0? 1 : 0)][y-1][z].isTransparent() )
                    leftHalfHidden = true;
                if (y>0 && x < Chunk.BlocksX*3-1 && ! mapdata[x + (x%2==0?0:1)][y-1][z].isTransparent())
                    rightHalfHidden = true;        

                if ((mapdata[x][y][z].getId() != 0) && ! (leftHalfHidden && rightHalfHidden))  {
                    //save every isTransparent() block
                    if (mapdata[x][y][z].isTransparent())
                        Controller.map.data[x][y][z].setVisible(true);

                    //check if it has offset, not part of the original raytracing, but checking it here saves iteration. mapdata and renderarray are for the field with x,y,z equal
                    if (mapdata[x][y][z].getOffsetY() > 0)
                        Controller.map.data[x][y][z].renderorder = 1;
                    else if (mapdata[x][y][z].getOffsetX() < 0 && mapdata[x][y][z].getOffsetY() < 0)
                            Controller.map.data[x][y][z].renderorder = -1;
                        else Controller.map.data[x][y][z].renderorder = 0;
                }
                y -= 2;
                z--;                       
            }
            //Take the last block
            if ((y >= 0) && (z>=0))
                Controller.map.data[x][y][z].setVisible(true);
       }

    }
    
    /**
     * Calculates the light level based on the sun shining straight from the top
     */
    public void calc_light(){
        for (int x=0; x < Chunk.BlocksX*3; x++){
            for (int y=0; y < Chunk.BlocksY*3; y++) {
                
                //find top most Block
                int z = Chunk.BlocksZ-1;
                while (Controller.map.data[x][y][z].isTransparent() == true && z > 0 ){
                    z--;
                }
                
                for (int level=z; level > 0; level--)
                    Controller.map.data[x][y][level].setLightlevel(level*50/z);
            }
        }         
    }
    
       
     
    private void drawiglog(){
        for (int i=0;i < Gameplay.iglog.size();i++){
            Msg msg = (Msg) Gameplay.iglog.get(i);
            Color clr = Color.blue;
            if ("System".equals(msg.getSender())) clr = Color.green;
            
            //draw
            tTFont.drawString(
                10,
                50+i*20,
                msg.getMessage(),
                clr
            );
        }
    }
}
