package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Wurfelengine;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 * The View manages everything what should be drawn.
 * @author Benedikt
 */
public class View {
    public static final int ENGINE_RENDER_WIDTH = 1920;

    private static AngelCodeFont baseFont;
    private Camera camera,camera2;
    private float equalizationScale;    
    private boolean goodgraphics = false;
    
    /**
     * Creates a View
     * @param gc
     * @throws SlickException
     */
    public View(GameContainer gc) throws SlickException {
        // initialise the ttF font which CAUSES LONG LOADING TIME!!!
        //TrueTypeFont trueTypeFont;

        //startFont = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(this.getClass().getResourceAsStream("Blox2.ttf")));
        //UnicodeFont startFont = new UnicodeFont("com/BombingGames/Game/Blox2.ttf", 20, false, false);
        baseFont = new AngelCodeFont("com/BombingGames/Game/arial.fnt","com/BombingGames/Game/arial.png");
        //baseFont = startFont.deriveFont(Font.PLAIN, 12);
        //baseFont = startFont.getFont().deriveFont(Font.PLAIN, 18);
        //tTFont = new TrueTypeFont(baseFont, true);
        
        //default rendering size is FullHD
        equalizationScale = gc.getWidth() / (float) ENGINE_RENDER_WIDTH;
        Log.debug("Scale is:" + Float.toString(equalizationScale));
        
        camera = new Camera(
            Gameplay.getController().getPlayer(),
            0, //left
            0, //top
            800, //full width 
            600//full height
        );
        camera2 = new Camera(
            Gameplay.getController().getPlayer(),
            800, //left
            0, //top
            800, //full width 
            600//full height
        );
        
        Block.loadSheet();
     }
    
    /**
     * Main method which is called every time
     * @param g 
     * @throws SlickException
     */
    public void render(Graphics g) throws SlickException{
        camera.render();
        camera2.render();
        
        g.scale(equalizationScale, equalizationScale);
        
        //render HUD
        if (Controller.getMap().getMinimap() != null)
            Controller.getMap().getMinimap().render(
                Wurfelengine.getGc().getScreenWidth() - 10,
                10
                ); 
        
        Gameplay.MSGSYSTEM.draw(); 
    }
       

    /**
     * The equalizationScale is a factor which the image is scaled by to have the same size on different resolutions.
     * @return
     */
    public float getEqualizationScale() {
        return equalizationScale;
    }

    /**
     * Returns the camera
     * @return
     */
    public Camera getCamera() {
        return camera;
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param x the x position on the screen
     * @return game coordinate
     */
    public int ScreenXtoGame(int x){
        return (int) ((x + Gameplay.getView().getCamera().getScreenPosX()) / Gameplay.getView().getCamera().getTotalScale());
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param y the y position on the screen
     * @return game coordinate
     */
    public int ScreenYtoGame(int y){
        return (int) ((y / Gameplay.getView().getCamera().getTotalScale() + Gameplay.getView().getCamera().getScreenPosY()) * 2);
    }
    
    /**
     * Returns the coordinates belonging to a point on the screen.
     * @param x the x position on the screen
     * @param y the y position on the screen
     * @return map coordinates
     */
    public int[] ScreenToGameCoords(int x, int y){
        int[] coords = new int[3];  
        
        //reverse y to game niveau, first the zoom:
        Log.debug("ScreenToGameCoords("+x+","+y+")");
        y = ScreenYtoGame(y);
        x = ScreenXtoGame(x);
        
        //find out where the click went
        coords[0] = x / Block.DIMENSION -1;
        
        coords[1] = (int) (y / Block.DIMENSION)*2-1;
            
        coords[2] = Map.getBlocksZ()-1;
        
        //find the block
        int[] tmpcoords = Block.posToNeighbourCoords(coords, x % Block.DIMENSION, y % Block.DIMENSION);
        coords[0] = tmpcoords[0];
        coords[1] = tmpcoords[1] + coords[2]*2;
        
        //if selection is not found by that specify it
        if (Controller.getMapData(coords).isHidden()){
            //trace ray down to bottom
            do {
                coords[1] = coords[1]-2;
                coords[2] = coords[2]-1;
            } while (Controller.getMapData(coords).isHidden());
        }
        
        return coords;
    }
    
   /**
     * 
     * @param goodgraphics
     */
    protected void setGoodgraphics(boolean goodgraphics) {
        this.goodgraphics = goodgraphics;
    }
       
   /**
     * Should the graphic be a bit slower but better? Must be in Controller because is needed for e.g. the Block and there used as data
     * @return 
     */
    public boolean hasGoodGraphics() {
        return goodgraphics;
    }

    /**
     * 
     * @return
     */
    public static AngelCodeFont getFont() {
        return baseFont;
    }
    
}