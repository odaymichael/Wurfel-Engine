package Game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


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
    public int cameraX;
    /**
     * 
     */
    public int cameraY;
    /**
     * The amount of pixel which are visible in X direction
     */
    public int cameraWidth;
    /**
     * The amount of pixel which are visible in Y direction
     */
    public int cameraHeight;
    /**
     * toogle between camer locked to player or not
     */
    public boolean cameramode = false;
    /**
     * the zoom factor of the map. Higher value means the zoom is higher.
     */
    private float zoom = 1;
    private Graphics g = null; 
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
     * @param pController
     * @param pgc
     * @throws SlickException
     */
    public View(GameContainer pgc) throws SlickException {
        gc = pgc;  
       
        // initialise the font which CAUSES LONG LOADING TIME!!!
        TrueTypeFont trueTypeFont;
        Font startFont;
        try {
            startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
            baseFont = startFont.deriveFont(Font.PLAIN, 12);
        } catch (FontFormatException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
        tTFont = new TrueTypeFont(baseFont, true);
        
        
        /*font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);*/
        
        //update resolution things
        GameplayState.iglog.add("Resolution: " + gc.getWidth() + " x " +gc.getHeight());
        
        zoom = gc.getHeight()*4 / (float)(Chunk.BlocksY* Block.height);
        System.out.println(zoom);
        Block.reloadSprites(zoom); 
        // Block.width = Block.height;
        GameplayState.iglog.add("Blocks: "+Block.width+" x "+Block.height);
        GameplayState.iglog.add("Zoom: "+zoom);
        GameplayState.iglog.add("chunk: "+Chunk.SizeX+" x "+Chunk.SizeY);
        GameplayState.iglog.add("chunk w/ zoom: "+Chunk.SizeX*zoom+" x "+Chunk.SizeY*zoom);
  
        
        cameraWidth = (int) (gc.getWidth() / zoom);
        cameraHeight= (int) (gc.getHeight() / zoom);
    }
    
    /**
     * Main method which is called every time
     * @param game
     * @param pg
     * @throws SlickException
     */
    public void render(StateBasedGame game, Graphics g) throws SlickException{
        this.g = g;
        draw_all_Chunks();
        //Controller.player.draw();
        
        //GUI
        if (GameplayState.Controller.map.minimap != null) GameplayState.Controller.map.minimap.draw(g);
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
        
       Block.Blocksheet.startUse();
       
        for (int z=0; z < Chunk.BlocksZ; z++) {
            for (int y=0; y < Chunk.BlocksY*3; y++) {//vertikal
                for (int x=0; x < Chunk.BlocksX*3; x++){//horizontal
                    if (
                        (x < Chunk.BlocksX*3-1 && GameplayState.Controller.map.data[x+1][y][z].renderorder == -1)
                        || GameplayState.Controller.map.data[x][y][z].renderorder == 1
                        ) {
                        renderblock(x+1,y,z);
                        renderblock(x,y,z);
                        x++;
                    } else renderblock(x,y,z);
                }
            }
       }
       Block.Blocksheet.endUse(); 
     };

    private void renderblock(int x,int y, int z) {
        //draw every block except air
        if (GameplayState.Controller.map.data[x][y][z].getID() != 0){
            //System.out.println("X: "+x+" Y:"+y+" Z: "+z);
            Block renderBlock = Controller.map.data[x][y][z]; 
            
            if (GameplayState.Controller.goodgraphics){ 
                if (renderBlock.renderTop) renderSide(x,y,z, 1, renderBlock);
                if (renderBlock.renderLeft) renderSide(x,y,z, 0, renderBlock);
                if (renderBlock.renderRight) renderSide(x,y,z, 2, renderBlock);

            } else {
                Image temp = Block.Blocksheet.getSubImage(renderBlock.spriteX[0], renderBlock.spriteY[0]);

                //calc  brightness
                float brightness = renderBlock.lightlevel / 100f;
                //System.out.println("Lightlevel " + Controller.map.data[x][y][z].lightlevel + "-> "+lightlevel);
                
                //or paint whole block with :
                //int brightness = renderBlock.lightlevel * 255 / 100;
                //new Color(brightness,brightness,brightness).bind(); 
                
                temp.setColor(0, brightness,brightness,brightness);
                temp.setColor(1, brightness,brightness, brightness);

                brightness -= .1f;
                //System.out.println(lightlevel);

                temp.setColor(2, brightness, brightness, brightness);
                temp.setColor(3, brightness, brightness, brightness);
                
                temp.drawEmbedded(
                    (int) (zoom*Controller.map.posX) + x*Block.displWidth + (y%2) * (int) (Block.displWidth/2) + renderBlock.getOffsetX(),
                    (int) (zoom*Controller.map.posY / 2) + y*Block.displHeight/4 - z*Block.displHeight/2 + renderBlock.getOffsetY()
                );
                
//                Block.Blocksheet.renderInUse(
//                    (int) (zoom*Controller.map.posX) + x*Block.displWidth + (y%2) * (int) (Block.displWidth/2) + renderBlock.getOffsetX(),
//                    (int) (zoom*Controller.map.posY / 2) + y*Block.displHeight/4 - z*Block.displHeight/2 + renderBlock.getOffsetY(),
//                    renderBlock.spritex,
//                    renderBlock.spritey
//                );
            }
        }
    }
    
    private void renderSide(int x, int y, int z,int side, Block renderBlock){
        //int brightness = (renderBlock.lightlevel - side*25) * 255 / 100;
        //new Color(brightness,brightness,brightness).bind();
        
        Image temp = renderBlock.getSideSprite(side);
        
        //calc  brightness
        float brightness = (renderBlock.lightlevel - side*25) / 100f;
                
        temp.setColor(0, brightness,brightness,brightness);
        temp.setColor(1, brightness,brightness, brightness);

        if (side!=1) brightness -= .3f;

        temp.setColor(2, brightness, brightness, brightness);
        temp.setColor(3, brightness, brightness, brightness);
        
        temp.drawEmbedded(
            (int) (zoom*Controller.map.posX)
            + x*Block.displWidth
            + (y%2) * (int) (Block.displWidth/2)
            + renderBlock.getOffsetX(),
            
            (int) (zoom*Controller.map.posY / 2)
            + y*Block.displHeight/4
            - z*Block.displHeight/2
            + ( side != 1 ? Block.displHeight/4:0)
            + renderBlock.getOffsetY()
        );
    }
    
    
    /**
     * Filters every Block (and side) wich is not visible. Boosts rendering speed.
     */
    public void raytracing(){
        //set visibility of every block to false
        for (int x=0;x < Chunk.BlocksX*3;x++)
            for (int y=0;y < Chunk.BlocksY*3;y++)
                for (int z=0;z < Chunk.BlocksZ;z++)
                    GameplayState.Controller.map.data[x][y][z].setVisible(false);

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
        if (!GameplayState.Controller.goodgraphics){
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
                
        Controller.map.changes = false;
    }

    private void trace_ray(Block[][][] mapdata, int x, int y, int z, int side){
        if (GameplayState.Controller.goodgraphics){
            boolean leftside = false;
            boolean rightside = false;
            y += 2;
            z++;
            do {
                y -= 2;
                z--;
                if (side == 0){
                    if (mapdata[x][y][z].getID() != 0) {
                        //put every transparent block with transparent block on top in renderarrray, except air.
                        GameplayState.Controller.map.data[x][y][z].setVisible(true);
                        GameplayState.Controller.map.data[x][y][z].renderLeft = true;
                    }
                    
                    //block on left hiding the side
                    if (x>0 && y>0 && z>0 && ! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z-1].transparent) break;
                    
                    //two blocks hiding the side
                    if (x>0 && y>0) {
                        if (! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z].transparent) leftside = true;
                        if (z>0 && ! mapdata[x][y][z-1].transparent) rightside = true;
                     }                    
                } else                 
                    if (side == 1) {
                        if (mapdata[x][y][z].getID() != 0 && (mapdata[x][y][z+1].transparent || z == Chunk.BlocksZ-1)) {
                            //put every transparent block with transparent block on top in renderarrray, except air.
                            GameplayState.Controller.map.data[x][y][z].setVisible(true);
                            GameplayState.Controller.map.data[x][y][z].renderTop = true;
                        }

                       //two 0- and 2-sides hiding the side 1
                        if (x>0 && y>0) {
                            if (! mapdata[x - (y%2 == 0 ? 1:0)][y-1][z].transparent) leftside = true;
                            if (x < Chunk.BlocksX*3-1 && ! mapdata[x + (y%2 == 0 ? 0:1)][y-1][z].transparent) rightside = true;
                        }

                    } else
                        if (side==2){
                            if (mapdata[x][y][z].getID() != 0) {
                                //put every transparent block with transparent block on top in renderarrray, except air.
                                GameplayState.Controller.map.data[x][y][z].setVisible(true);
                                GameplayState.Controller.map.data[x][y][z].renderRight = true;
                            }
                    
                            //block on right hiding the side
                            if (x < Chunk.BlocksX*3-1 && y>0 && z>0 && ! mapdata[x + (y%2 == 0 ? 0:1)][y-1][z-1].transparent) break;
                    
                            //two blocks hiding the side
                            if (x>0 && z>0) {
                                if (z>0 && ! mapdata[x][y][z-1].transparent) leftside = true;
                                if (x < Chunk.BlocksX*3-1 && ! mapdata[x + (y%2 == 0 ? 0:1)][y][z-1].transparent) rightside = true;
                            } 
                        }
                
           } while (y > 1 && z > 0 && mapdata[x][y][z].transparent && !(leftside && rightside));
//           Take the last block
//            if (y >= 0 && z >= 0 && z < Chunk.BlocksZ-1 && (mapdata[x][y][z+1].transparent)){
//                renderarray[x][y][z] = mapdata[x][y][z];
//                renderarray[x][y][z].renderTop = true;
//            }
        } else{
            //trace ray until it found a not transparent block
            boolean leftHalfHidden = false;
            boolean rightHalfHidden = false;
            while ((y >= 0) && (z >= 0) && (mapdata[x][y][z].transparent)) {
                //check blocks hidden by 4 other halfs
                if ( x>0 && y>0 && ! mapdata[x - (x%2 == 0? 1 : 0)][y-1][z].transparent )
                    leftHalfHidden = true;
                if (y>0 && x < Chunk.BlocksX*3-1 && ! mapdata[x + (x%2==0?0:1)][y-1][z].transparent)
                    rightHalfHidden = true;        

                if ((mapdata[x][y][z].getID() != 0) && ! (leftHalfHidden && rightHalfHidden))  {
                    //save every transparent block
                    if (mapdata[x][y][z].transparent)
                        GameplayState.Controller.map.data[x][y][z].setVisible(true);

                    //check if it has offset, not part of the original raytracing, but checking it here saves iteration. mapdata and renderarray are for the field with x,y,z equal
                    if (mapdata[x][y][z].getOffsetY() > 0)
                        GameplayState.Controller.map.data[x][y][z].renderorder = 1;
                    else if (mapdata[x][y][z].getOffsetX() < 0 && mapdata[x][y][z].getOffsetY() < 0)
                            GameplayState.Controller.map.data[x][y][z].renderorder = -1;
                        else GameplayState.Controller.map.data[x][y][z].renderorder = 0;
                }
                y -= 2;
                z--;                       
            }
            //Take the last block
            if ((y >= 0) && (z>=0))
                GameplayState.Controller.map.data[x][y][z].setVisible(true);
       }

    }
    
    /**
     * Calculates the light level based on the sun with an angle of 90 deg
     */
    public void calc_light(){
        for (int x=0; x < Chunk.BlocksX*3; x++){
            for (int y=0; y < Chunk.BlocksY*3; y++) {
                //find top most Block
                int z = Chunk.BlocksZ-1;
                while (GameplayState.Controller.map.data[x][y][z].transparent == true && z > 0 ){
                    z--;
                }
                for (int level=z; level > 0; level--)
                    GameplayState.Controller.map.data[x][y][level].lightlevel = level*100/z;
            }
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
       
     
    private void drawiglog(){
        for (int i=0;i < GameplayState.iglog.size();i++){
            Msg msg = (Msg) GameplayState.iglog.get(i);
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
