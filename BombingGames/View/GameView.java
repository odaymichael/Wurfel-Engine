package BombingGames.View;

import BombingGames.Controller.Chunk;
import BombingGames.Controller.GameController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class GameView extends javax.swing.JFrame {
    public static JButton b1; 
    public static JButton b2;
    public static JButton b3;
    BufferStrategy bufferStrategy;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static Insets insets;
    
    //Konstruktor 
    public GameView() {
        System.out.println("GameView");
        //setIgnoreRepaint(true);
        GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
        //graphicsDevice.setFullScreenWindow(this);
        //graphicsDevice.setDisplayMode(new DisplayMode(1920, 1080, 32, 60));
        View.window.setSize(800,800);
           
        //Canvas gui = new Canvas();
        //window.getContentPane().add(gui);
        
        b1 = new JButton("Beenden");
        b1.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    executorService.shutdown();
                    System.exit(0);  
                }
            }
        );
        
        View.window.getContentPane().add(b1);
        
        b2 = new JButton("Draw Chunk 4");
        b2.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    drawChunk(GameController.chunklist[4]); 
                }
            }
        );
         View.window.getContentPane().add(b2);
        
        b3 = new JButton("Chunklist");
        b3.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    drawchunklist(); 
                }
            }
        );
         View.window.getContentPane().add(b3);

         View.window.createBufferStrategy(2);
         bufferStrategy = View.window.getBufferStrategy();     
    }
    
       public void draw_all_chunks(){
            for (int i = 0;i < GameController.chunklist.length;i++)
                drawChunk(GameController.chunklist[i]);
       };
       
       public void drawchunklist(){
            Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 12));
            for (int i=0;i < GameController.chunklist.length;i++){
                g2d.setColor(Color.white);
                g2d.fillRect(
                    i%3*40+10,
                    i/3*40+10,
                    40,
                    40
                );
                g2d.setColor(Color.black);
                g2d.drawRect(
                    i%3*40+10,
                    i/3*40+10,
                    40,
                    40
                );
                g2d.drawString(
                    GameController.chunklist[i].coordX +" | "+ GameController.chunklist[i].coordY,
                    i%3*40+15,
                    i/3*40+40
                );
        }
        g2d.dispose();
        bufferStrategy.show();
        Toolkit.getDefaultToolkit().sync();
    }
    
    
    //draw the chunk
    public void drawChunk(Chunk chunk) {
        Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        //Graphics2D g2d = bimage.createGraphics();            
        //Graphics2D g2d = (Graphics2D) window.getGraphics();

        BasicStroke stokestyle = new BasicStroke(
            1,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL
        );
        
        g2d.setStroke(stokestyle);


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
        g2d.setColor(Color.yellow);
        
        

        /*BufferedImage tile = gc.createCompatibleImage(Controller.tilesizeX, Controller.tilesizeY, Transparency.BITMASK);
        tile.getGraphics().drawImage(tilefile,
                0,//dx1
                0,//dy1
                Controller.tilesizeX,//width
                Controller.tilesizeY,//height
                null);*/


        //int amountX = window_width()/Controller.tilesizeX;
        //int amountY = window_height()/Controller.tilesizeY;
        
        
        Integer lastID = -1;
        
        //es macht irgendiwe keinen Unterschied, ob View.View oder View.Blockimages nutzt, da er nicht in das package geht.
      
        URL temp = BombingGames.View.View.class.getResource("Blockimages/0-0.png");
        for (int y=0; y < chunk.chunkdata[0].length; y++)//vertikal
            for (int x=0; x < chunk.chunkdata.length; x++)//horizontal
                for (int z=0; z < GameController.ChunkSizeZ; z++) {
                    if (chunk.chunkdata[x][y][z].ID != 0){
                        //load only file, if block changed
                        if (chunk.chunkdata[x][y][z].ID != lastID){
                            temp = BombingGames.View.View.class.getResource("Blockimages/" + chunk.chunkdata[x][y][z].ID + "-0.png");
                            lastID = chunk.chunkdata[x][y][z].ID;
                        }
                        //draw the block
                        g2d.drawImage(
                            new ImageIcon(temp).getImage(),
                            chunk.posX + Math.abs((y%2)*GameController.tilesizeX/2) + x*GameController.tilesizeX ,
                            chunk.posY + y*GameController.tilesizeY/2,
                            GameController.tilesizeX,
                            2*GameController.tilesizeY,
                            null
                        );
                    }
                }
            
        /*Ansatz mit Zeichnen der halben tiles
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

        for (int i=0; i < (chunk.chunkdata.length)*2 + (chunk.chunkdata[0].length); i++){
            //g2d.setColor(new Color(i*5+20,i*5+20,0));

            //Linie 1 (von unten links nach oben rechts) /
            if (s1y > chunk.chunkdata[0].length) {
                s1x++;
                start1X = chunk.posX + s1x*GameController.tilesizeX/2;                    
            } else {
                s1y++;
                start1Y = chunk.posY + s1y*GameController.tilesizeY/2;       
            }         
            
            
            if (e1x+1 > chunk.chunkdata.length*2) {
                e1y++;
                end1Y = chunk.posY + e1y*GameController.tilesizeY/2;
            } else{
                e1x++; 
                end1X = chunk.posX + e1x*GameController.tilesizeX/2;                    
            }

            g2d.drawLine(
                start1X + (i%2)*GameController.tilesizeX/2,
                start1Y,
                end1X + (i%2)*GameController.tilesizeX/2,
                end1Y
            );


            //Linie 2  (von oben links nach utnen rechts)  \       
            if (s2x < chunk.chunkdata.length*2) {
                s2x++;
                start2X = chunk.posX + Chunk.width - s2x*GameController.tilesizeX/2;
            } else {
                s2y++;
                start2Y = chunk.posY + s2y*GameController.tilesizeY/2;
            }

            if (e2y-1 < chunk.chunkdata[0].length){
                e2y++;
                end2Y = chunk.posY + e2y*GameController.tilesizeY/2;                    
            } else {
                e2x++;
                end2X = chunk.posX + Chunk.width - e2x*GameController.tilesizeX/2;                   
            }

            g2d.drawLine(
                start2X + (i%2)*GameController.tilesizeX/2,
                start2Y,
                end2X + (i%2)*GameController.tilesizeX/2,
                end2Y
            );
        }
            
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 50));
            g2d.drawString(
                String.valueOf(chunk.coordX)+","+String.valueOf(chunk.coordY),
                chunk.posX + Chunk.width/2 - 40,
                chunk.posY + Chunk.height/2 - 40
            );
           
            
            //wohl wie flush(), irgendwie für den garbage collector
            g2d.dispose();
            
            //anzeigen
            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
            drawchunklist();
        }
    
    public void setMousePressedListener(MouseListener Listener){
         View.window.addMouseListener(Listener);
    }

    public void setMouseDraggedListener(MouseMotionListener Listener){
         View.window.addMouseMotionListener(Listener);    
    }
    
}
