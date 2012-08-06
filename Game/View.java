package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;




public class View {
    private Controller Controller;
    Image blockimages[] = new Image[10];
    private Graphics g = null; 
    private java.awt.Font font;
    private TrueTypeFont tTFont;
    private TrueTypeFont tTFont_small;
    private GameContainer container;
    private MsgSystem iglog;
    //private static Insets insets;
    
    //Konstruktor 
    public View() throws SlickException {
        //Create a list with all the images in it.
        for (int i=0;i<blockimages.length;i++){
            try{
            blockimages[i] = new Image("Game/Blockimages/"+ i +"-0.png");
             }
                catch(RuntimeException e) {
                    System.out.println("Block "+i+" not found.");
                }   
        }
            
       
        
        // initialise the font which CAUSES LONG LOADING TIME!!!
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 12);
        tTFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 8);
        tTFont_small = new TrueTypeFont(font, true);
    }
    
     public void render(Controller pController,GameContainer pcontainer, StateBasedGame game, Graphics pg) throws SlickException{
        Controller = pController;
        g = pg;
        container = pcontainer;
        
        draw_all_Chunks();
        drawchunklist();
        drawiglog();
    }
    
    /*private void initGL() {
        //2D Initialization
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
    }
    
    private void resizeGL() {
        //2D Scene
        glViewport(0,0,800,800);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f,800,0.0f,800);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
        
        
        glLoadIdentity();
         glTranslatef(-1.5f,0.0f,-6.0f);
         glBegin(GL_QUADS); // Ein Viereck zeichnen
    glColor3f(1.0f,0.0f,0.0f); // es soll rot werden
    glVertex3f(-1.0f, 1.0f, 0.0f); // oben links
    glVertex3f( 1.0f, 1.0f, 0.0f); // oben rechts
    glVertex3f( 1.0f,-1.0f, 0.0f); // unten rechts
    glVertex3f(-1.0f,-1.0f, 0.0f); // unten links
  glEnd(); // Zeichenaktion beenden
     }*/
    
     public void draw_all_Chunks() throws SlickException{
            /*
        BasicStroke stokestyle = new BasicStroke(
            1,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL
        );
        
        //g2d.setStroke(stokestyle);


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        // Create an image that supports transparent pixels
        //BufferedImage bimage = gc.createCompatibleImage(window_width(), window_height(), Transparency.BITMASK);

        //Zeile wird irgendwie ignoriert
        //bimage.getGraphics().setColor(Color.red);
        //bimage.getGraphics().fillRect(0, 0, bimage.getWidth(), bimage.getHeight());
        //g2d.drawImage(bimage, null, posX, posY);


        //g2d.fillRect(posX,posY,window_width(),window_height());            
        //g2d.setColor(Color.yellow);
        
        

        BufferedImage tile = gc.createCompatibleImage(Controller.tilesizeX, Controller.tilesizeY, Transparency.BITMASK);
        tile.getGraphics().drawImage(tilefile,
                0,//dx1
                0,//dy1
                Controller.tilesizeX,//width
                Controller.tilesizeY,//height
                null);
        */
         
         
        for (int y=0; y < Controller.renderarray[0].length; y++)//vertikal
            for (int x=0; x < Controller.renderarray.length; x++)//horizontal
                for (int z=0; z < Controller.renderarray[0][0].length; z++)
                    if (Controller.renderarray[x][y][z].ID != 0){
                        //draw the block except air
                        //System.out.println("X: "+x+" Y:"+y+" Z: "+z);
                        blockimages[Controller.renderarray[x][y][z].ID].draw(
                            Controller.chunklist[0].posX + x*Block.width + (y%2) * Block.width/2,
                            Controller.chunklist[0].posY + y*Block.height/4 - z*Block.height/2,
                            Block.width,
                            Block.height
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

       
     public void drawchunklist(){
         Rectangle rectangle = new Rectangle(20,20,80,80);
          
        //trueTypeFont.drawString(20.0f, 20.0f, "Slick displaying True Type Fonts", Color.green);
         
         //java.awt.Font font = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12);
         //UnicodeFont uFont = new UnicodeFont(font , 20, false, false);
         //custom fonts makes them dissapear. y u no work?
         //g.setFont(uFont);
         
        for (int i=0;i < Controller.chunklist.length;i++){
            g.setColor(Color.white);
            g.fillRect(
                    i%3*80+700,
                    i/3*80+10,
                    80,
                    80
                );
                g.setColor(Color.black);
                g.drawRect(
                    i%3*80+700,
                    i/3*80+10,
                    80,
                    80
                );
                g.setColor(Color.black);
                tTFont.drawString(
                    i%3*80+720,
                    i/3*80+20,
                    Controller.chunklist[i].coordX +" | "+ Controller.chunklist[i].coordY,
                    Color.black
                );
                 tTFont_small.drawString(
                    i%3*80+720,
                    i/3*80+70,
                    Controller.chunklist[i].posX +" | "+ Controller.chunklist[i].posY,
                    Color.black
                );
        }
        
    } 
     
    private void drawiglog(){
        for (int i=0;i < GameplayState.iglog.size();i++){
            Msg msg = (Msg) GameplayState.iglog.get(i);
            Color clr= Color.blue;
            if ("System".equals(msg.getSender())) clr = Color.green;
            
            //draw
            tTFont.drawString(
                900,
                50+i*20,
                msg.getMessage(),
                clr
            );
        }
    }
}
