package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.GameObject;
import org.lwjgl.opengl.GL11;
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
    /**
     * The default render width.
     * Every resolution smaller than this get's scaled down and every resolution bigger scaled up. 
     */
    public static final int ENGINE_RENDER_WIDTH = 1920;

    private static AngelCodeFont baseFont;
    private float equalizationScale;
    private Controller controller;
    
    private int drawmode;
    
    /**
     * Creates a View.
     * @param gc
     * @param controller 
     * @throws SlickException
     */
    public View(GameContainer gc, Controller controller) throws SlickException {
        this.controller = controller;
        baseFont = new AngelCodeFont("com/BombingGames/Game/arial.fnt","com/BombingGames/Game/arial.png");
        
        //default rendering size is FullHD
        equalizationScale = gc.getWidth() / (float) ENGINE_RENDER_WIDTH;
        Log.debug("Scale is:" + Float.toString(equalizationScale));
 
        Block.loadSheet();
     }
    
    /**
     * Main method which is called every time and renders everything.
     * @param g the graphics context
     * @throws SlickException
     */
    public void render(Graphics g) throws SlickException {
        //render every camera
        for (Camera camera : controller.getCameras()) {
            camera.render(g, this);
        }
        
        //render HUD
        
        //scale to fit
        g.scale(equalizationScale, equalizationScale);
        
        if (controller.getMinimap() != null)
            controller.getMinimap().render(g, this); 
        
        Gameplay.msgSystem().render(g);
        
        //reverse scale
        g.scale(1/equalizationScale, 1/equalizationScale);
    }
       

    /**
     * The equalizationScale is a factor which the image is scaled by to have the same size on different resolutions.
     * @return the scale factor
     */
    public float getEqualizationScale() {
        return equalizationScale;
    }

    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param x the x position on the screen
     * @return the relative game coordinate
     */
    public int ScreenXtoGame(int x){
        return (int) ((x - controller.getCameras().get(0).getScreenPosX()) / controller.getCameras().get(0).getTotalScale()
            + controller.getCameras().get(0).getOutputPosX());
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param y the y position on the screen
     * @return the relative game coordinate
     */
    public int ScreenYtoGame(int y){
        return (int) ((y - controller.getCameras().get(0).getScreenPosY()) / controller.getCameras().get(0).getTotalScale()
            + controller.getCameras().get(0).getOutputPosY()) * 2;
    }
    
    /**
     * Returns the coordinates belonging to a point on the screen.
     * @param x the x position on the screen
     * @param y the y position on the screen
     * @return the relative map coordinates
     */
    public int[] ScreenToGameCoords(int x, int y){
        int[] coords = new int[3];  
        
        //reverse y to game niveau, first the zoom:
        x = ScreenXtoGame(x);
        y = ScreenYtoGame(y);
        
        //find out where the click went
        coords[0] = x / Block.DIMENSION -1;
        
        coords[1] = (int) (y / Block.DIMENSION)*2-1;
            
        coords[2] = Map.getBlocksZ()-1;
        
        //find the block
        int[] tmpcoords = GameObject.sideIDtoNeighbourCoords(coords, GameObject.getSideID(x % Block.DIMENSION, y % Block.DIMENSION));
        coords[0] = tmpcoords[0];
        coords[1] = tmpcoords[1] + coords[2]*2;
        
        //if selection is not found by that specify it
        if (Controller.getMapData(coords).isHidden()){
            //trace ray down to bottom
            do {
                coords[1] = coords[1]-2;
                coords[2] = coords[2]-1;
            } while (Controller.getMapData(coords).isHidden() && coords[2]>0);
        }
        
        return coords;
    }

    /**
     * 
     * @return
     */
    public static AngelCodeFont getFont() {
        return baseFont;
    }

    public int getDrawmode() {
        return drawmode;
    }

    public void setDrawmode(int drawmode) {
        if (drawmode != this.drawmode){
            this.drawmode = drawmode;
            GameObject.getSpritesheet().getFullImage().endUse();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, drawmode);
            GameObject.getSpritesheet().getFullImage().startUse();
        }
    }
    
    
}