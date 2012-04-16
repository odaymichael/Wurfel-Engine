import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class OutThere2 extends javax.swing.JFrame {
    final int tilewidth = 80; //alernativ tile.getWidth(null);
    final int tileheight = 40; //alternativ tile.getHeight(null);
    public Chunk chunklist[] = new Chunk[9];
    Insets insets;
    int clickstartX,clickstartY;
    int tilesizeX = 80;
    int tilesizeY = 40;
    public JButton b1; 
    JFrame window = new JFrame("Das Fenster zur Welt"); 
    
    public static void main(String[] args) {
        OutThere2 outThere2 = new OutThere2();
    } 
        
    public OutThere2() {
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); 
        window.setSize( 800, 400 );
        //alternative. Lässt startposition vom System verwalten
        //window.setLocationByPlatform(true);
        window.setLocationRelativeTo(null);
       
        //window.setUndecorated(true);
        window.getContentPane().setLayout(new FlowLayout());
        
        b1 = new JButton("Beschriftung");
        b1.addActionListener(
            new ActionListener() {
            @Override
                    public void actionPerformed(ActionEvent e){
                        draw_all_Chunks();  
                    }
                 }
        );
        
        window.getContentPane().add(b1);
        class mouseEventHandler extends MouseAdapter{         
        
            @Override
             public void mousePressed(MouseEvent e){ // Why is this method never called ?{
                System.out.println("Es wurde geklickt.");
                clickstartX = e.getX();
                clickstartY = e.getY();
            }
        } 

        class mouseMovedEventHandler extends MouseMotionAdapter{           
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("Es wurde gedraggt.");
                for (int i=0;i<9;i++){
                    chunklist[i].posX -= clickstartX - e.getX();
                    chunklist[i].posY -= clickstartY - e.getY();
                }
                clickstartX = e.getX();
                clickstartY = e.getY();
                draw_all_Chunks();
            }
        } 
        
        window.addMouseListener(new mouseEventHandler()); 
        window.addMouseMotionListener(new mouseMovedEventHandler());
        
        window.setVisible( true ); 
        
        chunklist[0] = new Chunk(-1,1);
        chunklist[1] = new Chunk(0,1);
        chunklist[2] = new Chunk(1,1);
        chunklist[3] = new Chunk(-1,0);
        chunklist[4] = new Chunk(0,0);
        chunklist[5] = new Chunk(1,0);
        chunklist[6] = new Chunk(-1,-1);
        chunklist[7] = new Chunk(0,-1);
        chunklist[8] = new Chunk(1,-1); 
    }
    

    

    public class Chunk {
        public int coordX, coordY, posX, posY;

        //Konstruktor
        public Chunk(int X, int Y){
            //die größe der Chunks, hängt von der Auflösung ab
            coordX = X;
            coordY = Y;
            posX = X*window_width();
            posY = -Y*window_height();    
        }
        
       /* public static void draw(){
             hier hätte ich gerne eine Methode die mit einem Aufruf von Chunk.daw()
             in einer Schleife neun mal die drawChunk ausführt
         }*/
        
        //draw this chunk
        public void drawChunk() {
            Graphics2D g2d = (Graphics2D) window.getGraphics();
            BasicStroke stokestyle = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            g2d.setStroke(stokestyle);

            g2d.setColor(new Color(coordX*100+155,coordY*100+155,150));
            GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment(). 
            getDefaultScreenDevice().getDefaultConfiguration(); 
            BufferedImage image = gfxConf.createCompatibleImage(window_width(), window_height());
            BufferedImage red = new BufferedImage(50, 100, BufferedImage.TYPE_INT_RGB);
            red.getGraphics().setColor(Color.red);
            red.getGraphics().fillRect(0, 0, 50, 100);
           // g2d.drawImage(red, 0, 0,this);
            //System.out.println(window_width());
            //g2d.fillRect(posX,posY,window_width(),window_height());            
            g2d.setColor(Color.blue);
            
            Image tile = new ImageIcon(this.getClass().getResource("grastile.png")).getImage();
            
            for (int i=-1; i*tilesizeY/2 < window_height()+tilesizeY/2; i++)//vertikal
                for (int j=-1; j*tilesizeX < window_width(); j++)//horizontal
                  g2d.drawImage(tile, j*tilesizeX+i % 2*tilesizeX/2+tilesizeX/2+posX, i*tilesizeY/2+posY, null);

            //26.56° Steigung der Pixel da m=0,5 und atan(0,5) = 26,565° 
            //int startpunkt = (int) (Math.sin(Math.atan(0.5))*window_width);
            //int gegenkathe = (int)(window_width/Math.tan(1.571-Math.atan(0.5)));

            //Komponenten der Punkte
            int start1X = posX;
            int start1Y = posY;
            int end1X = posX;
            int end1Y = posY;
            int start2X = posX;
            int start2Y = posY;
            int end2X = posX + window_width();
            int end2Y = posY;
            //zählervariablen
            int s1x = 0;int s1y = 0;int e1x = 0;int e1y = 0;
            int s2x = 0;int s2y = 0;int e2x = 0;int e2y = 0;
            
            for (int i=0; i < window_width()/tilesizeX+window_height()/tilesizeY; i++){
                //g2d.setColor(new Color(i*5+20,i*5+20,0));
                
                //Linie 1
                if (s1y*tilesizeY > window_height()){
                    s1x++;
                    start1X = posX + s1x*tilesizeX;                    
                } else {
                    s1y++;
                    start1Y = posY + s1y*tilesizeY;       
                }
                
                if (e1x*tilesizeX > window_width()){
                   e1y++;
                   end1Y = posY + e1y*tilesizeY;
                } else{
                   e1x++; 
                   end1X = posX + e1x*tilesizeX;                    
                }
                
                g2d.drawLine(start1X,start1Y,end1X,end1Y);
                
                //Linie 2          
                if (s2x*tilesizeX < window_width()){
                    s2x++;
                    start2X = posX + window_width() - s2x*tilesizeX;
                } else {
                    s2y++;
                    start2Y = posY + s2y*tilesizeY;
                }
                
                if (e2y*tilesizeY < window_height()){
                    e2y++;
                    end2Y = posY + e2y*tilesizeY;                    
                } else {
                    e2x++;
                    end2X = posX + window_width() - e2x*tilesizeX;                   
                }
                               
                g2d.drawLine(start2X,start2Y,end2X,end2Y);
            }
            
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 50));
            g2d.drawString(String.valueOf(coordX)+","+String.valueOf(coordY), posX+window_width()/2-40, posY+window_height()/2-40);
        }
    }      
            
    public void draw_all_Chunks(){
       for (int i=0;i<9;i++){
           chunklist[i].drawChunk();
       }
   };
   
    public int window_width(){
        Dimension size = window.getSize();
        insets = window.getInsets();
        return size.width - insets.left - insets.right;
   }
   
    public int window_height(){
        Dimension size = window.getSize();
        insets = window.getInsets();
        return size.height - insets.top - insets.bottom;
   }
    
}
