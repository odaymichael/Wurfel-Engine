package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


/**
 * 
 * @author Benedikt
 */
public class View {
    private Controller Controller;
    /**
     * the array which is created after analysing the Map.data
     */
    private Block renderarray[][][] = new Block[Chunk.BlocksX*3][Chunk.BlocksY*3][Chunk.BlocksZ];
    /**
     * 
     */
    public static int cameraX;
    /**
     * 
     */
    public static int cameraY;
    /**
     * Width of camera
     */
    public static int cameraWidth;
    /**
     * Height of camera
     */
    public static int cameraHeight;
    /**
     * is the player centered or ???
     */
    public boolean cameramode = false;
    /**
     * the factor with the map is zoomed
     */
    public static float zoom = 1;
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
    private MsgSystem iglog;
    //private static Insets insets;
    
    //Konstruktor 
    /**
     * 
     * @param pController
     * @param pgc
     * @throws SlickException
     */
    public View(Controller pController,GameContainer pgc) throws SlickException {
        Controller = pController;
        gc = pgc;
        Block.listImages();        
       
        // initialise the font which CAUSES LONG LOADING TIME!!!
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);
        
        //update resolution things
        GameplayState.iglog.add("Resolution: "+gc.getScreenWidth()+" x "+gc.getScreenHeight());
        //Block.width = gc.getScreenWidth() / Chunk.BlocksX;
        //Block.height = gc.getScreenHeight() / ((Chunk.BlocksY-1)/4);
        zoom = (float) ((float) gc.getScreenHeight()*4/((Chunk.BlocksY)* Block.height));
        // Block.width = Block.height;
        GameplayState.iglog.add("Blocks: "+Block.width+" x "+Block.height);
        GameplayState.iglog.add("Zoom: "+zoom);
        GameplayState.iglog.add("chunk: "+Chunk.SizeX+" x "+Chunk.SizeY);
        GameplayState.iglog.add("chunk w/ zoom: "+Chunk.SizeX*zoom+" x "+Chunk.SizeY*zoom);
  
        
        cameraWidth = (int) (gc.getScreenWidth()*zoom);
        cameraHeight= (int) (gc.getScreenHeight()*zoom);
    }
    
    /**
     * Main method which is called every time
     * @param game
     * @param pg
     * @throws SlickException
     */
    public void render(StateBasedGame game, Graphics pg) throws SlickException{
        g = pg;
        draw_all_Chunks();
        //Controller.player.draw();
        
        //GUI
        Controller.minimap.draw(pg);
        drawiglog();
    }
     
    /**
     * 
     * @throws SlickException
     */
    public void draw_all_Chunks() throws SlickException{
       /*
        BasicStroke stokestyle = new BasicStroke(
            1,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL
        );
        
        //g2d.setStroke(stokestyle);

        BufferedImage tile = gc.createCompatibleImage(Controller.tilesizeX, Controller.tilesizeY, Transparency.BITMASK);
        tile.getGraphics().drawImage(tilefile,
                0,//dx1
                0,//dy1
                Controller.tilesizeX,//width
                Controller.tilesizeY,//height
                null);
        */  

       for (int y=0; y < renderarray[0].length; y++)//vertikal
            for (int x=0; x < renderarray.length; x++)//horizontal
                for (int z=0; z < renderarray[0][0].length; z++)
                    //draw the block except air
                    if (renderarray[x][y][z].ID() != 0){
                        //System.out.println("X: "+x+" Y:"+y+" Z: "+z);
                        if (Controller.goodgraphics){
                            int zbottom=Chunk.BlocksZ-1;
                            while (Controller.map.data[x][y][zbottom].transparent == true && zbottom > 0 ){
                                zbottom--; 
                            }
                                float brigthness = (float) z/zbottom; 
                                Block.images[renderarray[x][y][z].ID()].setColor(0, brigthness, brigthness, brigthness, 1);
                                Block.images[renderarray[x][y][z].ID()].setColor(1, brigthness, brigthness, brigthness, 1);
                                Block.images[renderarray[x][y][z].ID()].setColor(2, brigthness, brigthness, brigthness, 1);
                                Block.images[renderarray[x][y][z].ID()].setColor(3, brigthness, brigthness, brigthness, 1);
                        }
                        Block.images[renderarray[x][y][z].ID()].draw(
                            (Controller.map.posX + x*Block.width + (y%2) * Block.width/2)*zoom,
                            (Controller.map.posY + y*Block.height/2 - z*Block.height)*zoom / 2,
                            zoom
                        );
                    }
                
        /*Ansatz mit Zeichnen der halben tiles
        //int amountX = window_width()/Controller.tilesizeX;
        //int amountY = window_height()/Controller.tilesizeY;
        for (int v=0; v < amountY*2; v++)//vertikal
        for (int h=0; h < amountX; h++)//horizontal
            g2d.drawImage(tilefile,
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
        }
            
            //g.setFont(new java.awt.Font("Helvetica", ava.awt.Font.PLAIN, 50));
            g.drawString(
                String.valueOf(chunk.coordX)+","+String.valueOf(chunk.coordY),
                chunk.posX + Chunk.width/2 - 40,
                chunk.posY + Chunk.height/2 - 40
            );*/
     };

    /**
     * 
     */
    public void raytracing(){
        //fill renderarray with air
        for (int x=0;x <Chunk.BlocksX*3;x++)
            for (int y=0;y <Chunk.BlocksY*3;y++)
                for (int z=0;z <Chunk.BlocksZ;z++)
                    renderarray[x][y][z] = new Block(0,0);
            

        
        //generate array (renderarray) which has render information in it. It filters every non visible block

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

    private void trace_ray(Block bigchunk[][][],int x, int y, int z){
        //do raytracing until it found a not transparent block
       while ((y >= 0) && (z>=0) && (bigchunk[x][y][z].transparent)) {
           //save every block which is not air
           if ((bigchunk[x][y][z].transparent) && (bigchunk[x][y][z].ID() != 0))
              renderarray[x][y][z] = bigchunk[x][y][z]; 
           y -= 2;
           z--;                       
        }   
                    
        //save the found block in renderarray. If there was none do nothing
        if ((y >= 0) && (z>=0))
           renderarray[x][y][z] = bigchunk[x][y][z];
    }
       
    /**
     * 
     */
    public void drawchunklist(){
         //Rectangle rectangle = new Rectangle(20,20,80,80);
        //trueTypeFont.drawString(20.0f, 20.0f, "Slick displaying True Type Fonts", Color.green);
         
         //java.awt.Font font = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12);
         //UnicodeFont uFont = new UnicodeFont(font , 20, false, false);
         //custom fonts makes them dissapear. y u no work?
         //g.setFont(uFont);
        
        
        tTFont_small.drawString(
            gc.getScreenWidth()-290,
            250,
            cameraX +" | "+ cameraY,
            Color.black
       );
        
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
