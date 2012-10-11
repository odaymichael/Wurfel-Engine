package Game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector3d;
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
    private Block renderarray[][][] = new Block[Chunk.BlocksX*3][Chunk.BlocksY*3][Chunk.BlocksZ];
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
    
    private ArrayList offsetBlock = new ArrayList<Vector3d>();
    private ArrayList leftBlockInFront = new ArrayList();
    
    /**
     * Konstruktor 
     * @param pController
     * @param pgc
     * @throws SlickException
     */
    public View(Controller pController,GameContainer pgc) throws SlickException {
        GameplayState.Controller = pController;
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
        Block.listImages(zoom); 
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
        GameplayState.Controller.map.minimap.draw(g);
        drawiglog();
    }
      
    /**
     * 
     * @throws SlickException
     */
    public void draw_all_Chunks() throws SlickException{
        /*Ansatz mit Zeichnen der halben tiles
        //int amountX = window_width()/Controller.tilesizeX;
        //int amountY = window_height()/Controller.tilesizeY;
        for (int v=0; v < amountY*2; v++)//vertikal
        for (int h=0; h < amountX; h++)//horizontal
            g2d.drawImage(
                    tilefile,
                    //Position
                    h==0 && v%2!=2//dx1
                    ? (v%2)*tilesizeX/2+posX
                    : tilesizeX*h - tilesizeX/2 + (v%2)*tilesizeX/2 + posX,
                    v==0//dy1
                    ? posY
                    : (v-1)*tilesizeY/2 + posY,
                    h==0 && v%2!=2//dx2
                    ? tilesizeX/2 + (v%2)*tilesizeX/2 + posX
                    : tilesizeX*h - tilesizeX/2 + tilesizeX + (v%2)*tilesizeX/2 + posX,
                    v==0//dy2
                    ? tilesizeY + posY
                    : (v-1)*tilesizeY/2 + posY + tilesizeY,

                    //Source
                    h==0 && v%2!=2 ? tilesizeX/2:0,//sx1
                    v==0//sy2
                    ? tilesizeY/2
                    : 0,
                    h==amountX && v%2!=2 
                    ? tilesizeX/2
                    : tilesizeX,//sx2
                    tilesizeY,//sy2
                    null);
                    */
                        
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
       
        for (int z=0; z < renderarray[0][0].length; z++) {
            for (int y=0; y < renderarray[0].length; y++) {//vertikal
                for (int x=0; x < renderarray.length; x++){//horizontal
                    if ((x < renderarray.length-1 && renderarray[x+1][y][z].renderorder == -1) || renderarray[x][y][z].renderorder == 1) {
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
        if (renderarray[x][y][z].ID() != 0){
            //System.out.println("X: "+x+" Y:"+y+" Z: "+z);
            Block renderBlock = Controller.map.data[x][y][z]; 
            
            if (GameplayState.Controller.goodgraphics){                           
                //new Color(lightlevel,lightlevel,lightlevel).bind(); 
 
                Image temp = Block.Blocksheet.getSubImage(renderBlock.spritex, renderBlock.spritey);

                //calc  brightness
                float lightlevel = renderBlock.lightlevel / 100f;
                //System.out.println("Lightlevel " + Controller.map.data[x][y][z].lightlevel + "-> "+lightlevel);

                temp.setColor(0,lightlevel,lightlevel,lightlevel);
                temp.setColor(1, lightlevel,lightlevel, lightlevel);

                lightlevel -= .1f;
                //System.out.println(lightlevel);

                temp.setColor(2, lightlevel,lightlevel, lightlevel);
                temp.setColor(3, lightlevel,lightlevel, lightlevel);

                temp.drawEmbedded(
                    (int) (zoom*Controller.map.posX) + x*Block.displBlockWidth + (y%2) * (int) (Block.displBlockWidth/2) + renderBlock.getOffsetX(),
                    (int) (zoom*Controller.map.posY / 2) + y*Block.displBlockHeight/4 - z*Block.displBlockHeight/2 + renderBlock.getOffsetY()
                );

            } else {
                //calc  brightness
                int brightness = renderBlock.lightlevel * 255 / 100;
                new Color(brightness,brightness,brightness).bind();     

                Block.Blocksheet.renderInUse(
                    (int) (zoom*Controller.map.posX) + x*Block.displBlockWidth + (y%2) * (int) (Block.displBlockWidth/2) + renderBlock.getOffsetX(),
                    (int) (zoom*Controller.map.posY / 2) + y*Block.displBlockHeight/4 - z*Block.displBlockHeight/2 + renderBlock.getOffsetY(),
                    renderBlock.spritex,
                    renderBlock.spritey
                );
            }
        }
    }
    
    
    /**
     * Filters every Block wich is not visible
     */
    public void raytracing(){
        //fill renderarray with air
        for (int x=0;x <Chunk.BlocksX*3;x++)
            for (int y=0;y <Chunk.BlocksY*3;y++)
                for (int z=0;z <Chunk.BlocksZ;z++)
                    renderarray[x][y][z] = new Block(0,0);

        //check top of big chunk
        for (int x=0; x < Controller.map.data.length; x++)
            for (int y=0; y < Controller.map.data[0].length; y++)
                trace_ray(
                    Controller.map.data,
                    x,
                    y,
                    Controller.map.data[0][0].length-1
                );
            
        //check front side
        for (int x=0; x < Controller.map.data.length; x++)
            for (int z=0; z < Controller.map.data[0][0].length-1 ; z++)            
                trace_ray(
                    Controller.map.data,
                    x,
                    Controller.map.data[0].length-1,
                    z
                );
        for (int x=0; x < Controller.map.data.length; x++)
            for (int z=0; z < Controller.map.data[0][0].length-1 ; z++)            
                trace_ray(
                    Controller.map.data,
                    x,
                    Controller.map.data[0].length-2,
                    z
                );
                
        Controller.map.changes = false;
    }

    private void trace_ray(Block mapdata[][][],int x, int y, int z){
        //trace ray until it found a not transparent block
       while ((y >= 0) && (z>=0) && (mapdata[x][y][z].transparent)) {
           //save every transparent block which is not air
           if ((mapdata[x][y][z].transparent) && (mapdata[x][y][z].ID() != 0))
              renderarray[x][y][z] = mapdata[x][y][z]; 
           
           //check if it has offset, not part of the original raytracing, but checking it here saves iteration. mapdata and renderarray are for the field with x,y,z equal
           if (mapdata[x][y][z].getOffsetY() > 0)
               renderarray[x][y][z].renderorder = 1;
           else if (mapdata[x][y][z].getOffsetX() < 0 && mapdata[x][y][z].getOffsetY() < 0)
                renderarray[x][y][z].renderorder = -1;
                else renderarray[x][y][z].renderorder = 0;
           
           y -= 2;
           z--;                       
        }   
                    
        //Take the first
        if ((y >= 0) && (z>=0))
           renderarray[x][y][z] = mapdata[x][y][z];
    }
    
    /*
     * Calculates the light level based on the sun with an angle of 90 deg
     */
    /**
     * 
     */
    public void calc_light(){
        for (int x=0; x < Chunk.BlocksX*3; x++){
            for (int y=0; y < Chunk.BlocksY*3; y++){
                //find top most Block
                int z = Chunk.BlocksZ-1;
                while (renderarray[x][y][z].transparent == true && z > 0 ){
                    z--;
                }
                for (int level=z; level > 0; level--)
                    renderarray[x][y][level].lightlevel = level*100/z;
                
            }
        }         
    }
    
    /**
     * Set the zoom factor and regenerates the sprites.
     * @param zoom
     */
    public void setzoom(float zoom) {
        this.zoom = zoom;
        Block.listImages(zoom);
    }
    
    /**
     * Returns the zoomfactor.
     * @return
     */
    public float getzoom() {
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
