package View;

import Controller.Controller;
import Controller.Controller.Chunk;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class View extends javax.swing.JFrame {
    static JFrame window = new JFrame();
    public static JButton b1; 
    public static JButton b2; 
    BufferStrategy bufferStrategy;
    final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static Insets insets;
    
    public View(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        setUndecorated(true);
        setIgnoreRepaint(true);
        GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
        //graphicsDevice.setFullScreenWindow(this);
        //graphicsDevice.setDisplayMode(new DisplayMode(640, 480, 16, 60));
        window.setSize( 800, 800 );
        
        //alternative. Lässt startposition vom System verwalten
        //window.setLocationByPlatform(true);
        
        window.setLocationRelativeTo(null);
       
        window.setUndecorated(true);
        window.getContentPane().setLayout(new FlowLayout());
        
        Canvas gui = new Canvas();
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
        
        window.getContentPane().add(b1);
        
        b2 = new JButton("Draw Chunk 4");
        b2.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    drawChunk(Controller.chunklist[4]); 
                }
            }
        );
        
        window.getContentPane().add(b2);
        
       
        window.setVisible(true);
        window.createBufferStrategy(2);
        bufferStrategy = window.getBufferStrategy();  
    }
       
    public void draw_all_chunks(){
        for (int i = 0;i<Controller.chunklist.length;i++) drawChunk(Controller.chunklist[i]);
    };
   
    private static int window_width(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.width - insets.left - insets.right;
        Controller.ChunkSizeX = value ;
        return value;
   }
   
    private static int window_height(){
        int value;
        Dimension size = window.getSize();
        insets = window.getInsets();
        value = size.height - insets.top - insets.bottom;
        Controller.ChunkSizeY = value ;
        return value;
   }
    
    
    //draw the chunk
    public void drawChunk(Chunk chunk) {
        Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        //Graphics2D g2d = bimage.createGraphics();            
        //Graphics2D g2d = (Graphics2D) window.getGraphics();

        BasicStroke stokestyle = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
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
        g2d.setColor(Color.blue);
        
        Image tilefile = new ImageIcon(this.getClass().getResource("grastile.png")).getImage();
        BufferedImage tile = gc.createCompatibleImage(Controller.tilesizeX, Controller.tilesizeY, Transparency.BITMASK);
        tile.getGraphics().drawImage(tilefile,
                0,//dx1
                0,//dy1
                Controller.tilesizeX,//width
                Controller.tilesizeY,//height
                null);

        int amountX = window_width()/Controller.tilesizeX;
        int amountY = window_height()/Controller.tilesizeY;

            for (int y=0; y < amountY*2; y++)//vertikal
            for (int x=0; x < amountX; x++)//horizontal
                g2d.drawImage(tilefile,
                    chunk.posX + Math.abs((y%2)*Controller.tilesizeX/2) + x*Controller.tilesizeX ,
                    chunk.posY + y*Controller.tilesizeY/2,
                    Controller.tilesizeX,
                    Controller.tilesizeY,
                    null
                );
            
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
        /*---Linien zeichen--*/
        /*-------------------*/ 

        //Komponenten der Punkte
        int start1X = chunk.posX;
        int start1Y = chunk.posY;
        int end1X = chunk.posX;
        int end1Y = chunk.posY;
        int start2X = chunk.posX;
        int start2Y = chunk.posY;
        int end2X = chunk.posX + window_width();
        int end2Y = chunk.posY;

        //zählervariablen
        int s1x = 0;int s1y = 0;int e1x = 0;int e1y = 0;
        int s2x = 0;int s2y = 0;int e2x = 0;int e2y = 0;

        for (int i=0; i/2 < window_width()/Controller.tilesizeX + window_height()/Controller.tilesizeY; i++){
            //g2d.setColor(new Color(i*5+20,i*5+20,0));

            //Linie 1 (von unten links nach oben rechts)
            if (s1y*Controller.tilesizeY/2 > window_height()){
                s1x++;
                start1X = chunk.posX + s1x*Controller.tilesizeX/2;                    
            } else {
                s1y++;
                start1Y = chunk.posY + s1y*Controller.tilesizeY/2;       
            }                

            if ((e1x+1)*Controller.tilesizeX/2 > window_width()){
                e1y++;
                end1Y = chunk.posY + e1y*Controller.tilesizeY/2;
            } else{
                e1x++; 
                end1X = chunk.posX + e1x*Controller.tilesizeX/2;                    
            }

            g2d.drawLine(start1X + (i%2)*Controller.tilesizeX/2,start1Y,end1X + (i%2)*Controller.tilesizeX/2,end1Y);


            //Linie 2  (von oben links nach utnen rechts)         
            if (s2x < amountX*2){
                s2x++;
                start2X = chunk.posX + window_width() - s2x*Controller.tilesizeX/2;
            } else {
                s2y++;
                start2Y = chunk.posY + s2y*Controller.tilesizeY/2;
            }

            if (e2y < amountY*2){
                e2y++;
                end2Y = chunk.posY + e2y*Controller.tilesizeY/2;                    
            } else {
                e2x++;
                end2X = chunk.posX + window_width() - e2x*Controller.tilesizeX/2;                   
            }

            g2d.drawLine(start2X + (i%2)*Controller.tilesizeX/2,start2Y,end2X + (i%2)*Controller.tilesizeX/2,end2Y);
        }
            
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 50));
            g2d.drawString(String.valueOf(chunk.coordX)+","+String.valueOf(chunk.coordY), chunk.posX+window_width()/2-40, chunk.posY+window_height()/2-40);
           
            
            //wohl wie flush(), irgendwie für den garbage collector
            g2d.dispose();
            
            //anzeigen
            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        }
    
    public void setMousePressedListener(MouseListener Listener){
        window.addMouseListener(Listener);
    }

    public void setMouseDraggedListener(MouseMotionListener Listener){
        window.addMouseMotionListener(Listener);    
    }
}




