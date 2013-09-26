package com.BombingGames.EngineCore;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.Map.Map;
import com.BombingGames.Game.Gameobjects.Block;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import org.lwjgl.opengl.GL11;

/**
 * The View manages everything what should be drawn.
 * @author Benedikt
 */
public class View {
    /**
     * The virtual render width (resolution).
     * Every resolution smaller than this get's scaled down and every resolution bigger scaled up. 
     */
    public static final int RENDER_RESOLUTION_WIDTH = 1280;

    private static BitmapFont font;
    
    private SpriteBatch batch;    
    private ShapeRenderer shapeRenderer;
    
    private float equalizationScale;
    private Controller controller;
    
    private int drawmode;
    
    private OrthographicCamera hudCamera;
    
    /**
     * 
     * @param controller 
     */
    public void init(Controller controller){
        this.controller = controller;
        font = new BitmapFont(Gdx.files.internal("com/BombingGames/EngineCore/arial.fnt"), true);
        font.setColor(Color.GREEN);
        font.scale(-0.5f);
        
        //default rendering size is FullHD
        equalizationScale = Gdx.graphics.getWidth() / (float) RENDER_RESOLUTION_WIDTH;
        Gdx.app.log("DEBUG","Scale is:" + Float.toString(equalizationScale));
 
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        Block.loadSheet();
    }
    
    /**
     *
     * @param delta
     */
    public void update(int delta){
    }
    
    /**
     * Main method which is called every time and renders everything.
     */
    public void render(){       
        //Gdx.gl10.glViewport(0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        //Gdx.gl10.glClear(GL10.GL_DEPTH_BUFFER_BIT); //clearing the screen is ~5-10% slower than without.
        
        //render every camera
        for (WECamera camera : controller.getCameras()) {
            camera.render(this, camera);
        }
        
        
        //render HUD
        // hudCamera.zoom = 1/equalizationScale;
        hudCamera.update();
        hudCamera.apply(Gdx.gl10);
         
        batch.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        
        Gdx.gl.glViewport(
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        
        drawString("FPS:"+ Gdx.graphics.getFramesPerSecond(), 10, 10);
        
        //scale to fit
        //hudCamera.zoom = 1/equalizationScale;
        
        if (Controller.getLightengine() != null)
            Controller.getLightengine().render(this);
        
        if (controller.getMinimap() != null)
            controller.getMinimap().render(this); 
        
        GameplayScreen.msgSystem().render(this);
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
     * @param camera 
     * @return the relative game coordinate
     */
    public int ScreenXtoGame(int x, WECamera camera){
        return (int) ((x - camera.getViewportPosX()) / camera.getTotalScale()+ camera.getGamePosX());
    }
    
   /**
     * Reverts the perspective and transforms it into a coordiante which can be used in the game logic.
     * @param y the y position on the screen
     * @param camera 
     * @return the relative game coordinate
     */
    public int ScreenYtoGame(int y, WECamera camera){
        return (int) ((y - camera.getViewportPosY()) / camera.getTotalScale() + camera.getGamePosY()) * 2;
    }
    
    /**
     * Returns the coordinates belonging to a point on the screen.
     * @param x the x position on the screen
     * @param y the y position on the screen
     * @return the relative map coordinates
     */
    public Coordinate ScreenToGameCoords(int x, int y){
        int i = 0;

        //find camera
         WECamera camera;
         do {          
            camera = controller.getCameras().get(i);
            i++;
        } while (i < controller.getCameras().size()
            && !(x > camera.getViewportPosX() && x < camera.getViewportPosX()+camera.getViewportWidth()
                && y > camera.getViewportPosY() && y < camera.getViewportPosY()+camera.getViewportHeight()));
 
        
        //reverse y to game niveau, first the zoom:
        x = ScreenXtoGame(x, camera);
        y = ScreenYtoGame(y, camera);
        
        
        //find out where the click went
        Coordinate coords = new Coordinate(
                       x / Block.SCREEN_WIDTH -1,
                       2*y / (Block.SCREEN_DEPTH*2) -1,
                       Map.getBlocksZ()-1,
                        true
        );
       
        //find the specific coordinate
        Coordinate specificCoords = Coordinate.neighbourSidetoCoords(
                            coords,
                            Coordinate.getNeighbourSide(x % Block.SCREEN_WIDTH, y % Block.SCREEN_DEPTH*2)
        );
        coords.setRelX(specificCoords.getRelX());
        coords.setRelY(specificCoords.getRelY() + coords.getZ()*2);
        
        //if selection is not found by that specify it
        if (coords.getBlock().isHidden()){
            //trace ray down to bottom
            do {
                coords.setRelY(coords.getRelY()-2);
                coords.setZ(coords.getZ()-1);
            } while (coords.getBlock().isHidden() && coords.getZ()>0);
        }
        
        return coords;
    }

    /**
     * 
     * @return
     */
    public BitmapFont getFont() {
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
            Gdx.gl11.glTexEnvi(Gdx.gl11.GL_TEXTURE_ENV, Gdx.gl11.GL_TEXTURE_ENV_MODE, drawmode);
            //GameObject.getSpritesheet().getFullImage().startUse();
            batch.begin();
        }
    }

    /**
     *Draw a string using the last active color.
     * @param msg
     * @param xPos
     * @param yPos
     */
    public void drawString(String msg, int xPos, int yPos) {
        Gdx.gl11.glTexEnvi(Gdx.gl11.GL_TEXTURE_ENV, Gdx.gl11.GL_TEXTURE_ENV_MODE, Gdx.gl11.GL_MODULATE);
        batch.begin();
        font.draw(batch, msg, xPos, yPos);
        batch.end();
    }
    
    /**
     *Draw a string in a color.
     * @param msg
     * @param xPos
     * @param yPos
     * @param color
     */
    public void drawString(String msg, int xPos, int yPos, Color color) {
        Gdx.gl11.glTexEnvi(Gdx.gl11.GL_TEXTURE_ENV, Gdx.gl11.GL_TEXTURE_ENV_MODE, Gdx.gl11.GL_MODULATE);
        font.setColor(color);
        batch.begin();
        font.draw(batch, msg, xPos, yPos);
        batch.end();
    }
    
    /**
     *Draw multi-lines with this method
     * @param text
     * @param xPos space from left
     * @param yPos space from top
     * @param color the colro of the text.
     */
    public void drawText(String text, int xPos, int yPos, Color color){
        Gdx.gl11.glTexEnvi(Gdx.gl11.GL_TEXTURE_ENV, Gdx.gl11.GL_TEXTURE_ENV_MODE, Gdx.gl11.GL_MODULATE);
        font.setColor(Color.BLACK);
        font.setScale(0.51f);
        batch.begin();
        font.drawMultiLine(batch, text, xPos, yPos);
        batch.end();
        
        font.setColor(Color.WHITE);
        font.setScale(0.5f);
        batch.begin();
        font.drawMultiLine(batch, text, xPos, yPos);
        batch.end();
    }
    
    /**
     *
     * @return
     */
    public OrthographicCamera getHudCamera() {
        return hudCamera;
    } 

    /**
     *
     * @return
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     *
     * @return
     */
    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}