package com.BombingGames.EngineCore;

import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lwjgl.opengl.GL11;

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

    private static BitmapFont font;
    private float equalizationScale;
    private Controller controller;
    
    private int drawmode;
    
    private SpriteBatch batch;
    
    private ShapeRenderer shapeRenderer;
    
    private OrthographicCamera hudCamera;
    
    /**
     * Creates a View.
     * @param gc
     * @param controller 
     * @throws SlickException
     */
    public View(Controller controller){
        this.controller = controller;
        font = new BitmapFont(Gdx.files.internal("com/BombingGames/EngineCore/arial.fnt"), true);
        font.setColor(Color.GREEN);
        
        //default rendering size is FullHD
        equalizationScale = Gdx.graphics.getWidth() / (float) ENGINE_RENDER_WIDTH;
        Gdx.app.log("DEBUG","Scale is:" + Float.toString(equalizationScale));
 
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        Block.loadSheet();
     }
    
    /**
     * Main method which is called every time and renders everything.
     * @param g the graphics context
     * @throws SlickException
     */
    public void render(){
        //clear & set background to black
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);        
        
        //render every camera
        for (Camera camera : controller.getCameras()) {
            camera.render(this);
        }
        
        
        //render HUD
        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        
        drawString("FPS:"+ Gdx.graphics.getFramesPerSecond(), 20, 20);
        
        //scale to fit
        //g.scale(equalizationScale, equalizationScale);
        
        
        Controller.getLightengine().render(this);
        
        if (controller.getMinimap() != null)
            controller.getMinimap().render(this); 
        
        GameplayScreen.msgSystem().render(this);
        
        //reverse scale
        //Gdx.graphics.scale(1/equalizationScale, 1/equalizationScale);
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
            + controller.getCameras().get(0).getGamePosX());
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param y the y position on the screen
     * @return the relative game coordinate
     */
    public int ScreenYtoGame(int y){
        return (int) ((y - controller.getCameras().get(0).getScreenPosY()) / controller.getCameras().get(0).getTotalScale()
            + controller.getCameras().get(0).getGamePosY()) * 2;
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
        Coordinate tmpcoords = GameObject.sideIDtoNeighbourCoords(new Coordinate(x, y, y, true),
                                GameObject.getSideID(x % Block.DIMENSION, y % Block.DIMENSION));
        coords[0] = tmpcoords.getRelX();
        coords[1] = tmpcoords.getRelY() + coords[2]*2;
        
        //if selection is not found by that specify it
        if (new Coordinate(coords[0], coords[1], coords[2], true).getBlock().isHidden()){
            //trace ray down to bottom
            do {
                coords[1] = coords[1]-2;
                coords[2] = coords[2]-1;
            } while (new Coordinate(coords[0], coords[1], coords[2], true).getBlock().isHidden() && coords[2]>0);
        }
        
        return coords;
    }

    /**
     * 
     * @return
     */
    public static BitmapFont getFont() {
        return font;
    }

    /**
     *
     * @return
     */
    public int getDrawmode() {
        return drawmode;
    }

    /**
     *
     * @param drawmode
     */
    public void setDrawmode(int drawmode) {
        if (drawmode != this.drawmode){
            this.drawmode = drawmode;
            batch.end();
            //GameObject.getSpritesheet().getFullImage().endUse();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, drawmode);
            //GameObject.getSpritesheet().getFullImage().startUse();
            batch.begin();
        }
    }

    public void drawString(String msg, int xPos, int yPos) {
        batch.begin();
        font.draw(batch, msg, xPos, yPos);
        batch.end();
    }
    
    public void drawString(String msg, int xPos, int yPos, Color color) {
        batch.begin();
        font.draw(batch, msg, xPos, yPos);
        batch.end();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
    
    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }
    
    
}