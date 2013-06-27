package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Coordinate;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.GAMEDIMENSION;
import com.BombingGames.Game.Map;
import com.BombingGames.Game.View;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends GameObject implements IsSelfAware {
   private Coordinate coords;
   private float[] pos = {DIM2, DIM2};
   
    /**
     * Create an abstractEntity. You should use Block.getInstance(int) 
     * @param id 
     * @see com.BombingGames.Game.Gameobjects.Block#getInstance(int) 
     */
    protected AbstractEntity(int id){
        super(id);
    }
    
    /**
     * Create an entity.
     * @param id the object id of the entity.
     * @param value The value at start.
     * @param absCoords the coordiantes where the entity is.
     * @return the entity.
     */
    public static AbstractEntity getInstance(int id, int value, Coordinate coords){
        AbstractEntity entity = null;
        //define the default SideSprites
        switch (id){
            case 40:
                    try {
                        entity = new Player(id);
                    } catch (SlickException ex) {
                        Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            case 41: 
                    entity = new AnimatedEntity(
                                id,
                                value,
                                new int[]{700,2000},
                                true,
                                false
                            );//explosion
                    break;
            default: entity = new SimpleEntity(id);
        }
        
        entity.setCoords(coords);

        entity.setValue(value);
        entity.setVisible(true);
        return entity;
    }
    
    //IsSelfAware implementation
    @Override
    public Coordinate getCoords() {
        return this.coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }
    
    
    
//    public int[] getAbsCoords() {
//        int z = (int) (height/GAMEDIMENSION);
//        if (z >= Map.getBlocksZ()) z = Map.getBlocksZ()-1;
//        return new int[]{coords[0], coords[1], z};
//    }

//    @Override
//    public void setAbsCoords(int[] coords) {
//        this.coords = new int[]{coords[0], coords[1]};
//        if (coords.length >= 3) this.height = coords[2]*GAMEDIMENSION;
//    }
//
//    @Override
//    public int[] getRelCoords() {
//        return Controller.getMap().absToRelCoords(getAbsCoords());
//    }
//    
//    @Override
//    public void setRelCoords(int[] relCoords) {
//        this.coords = Controller.getMap().relToAbsCoords(relCoords);
//        if (coords.length >= 3) this.height = coords[2]*GAMEDIMENSION;
//    }
//
//    @Override
//    public void addVector(int[] coords) {
//        height += coords[2];
//        setAbsCoords(new int[]{getAbsCoords()[0]+coords[0], getAbsCoords()[1]+coords[1]});
//    }
    
        
    /**
     * Is the entity laying/standing on the ground?
     * @return true when on the ground
     */
    public boolean onGround(){
        if (getCoords().getHeight() <= 0) return true;
                   
        int z = (int) ((getCoords().getHeight()-1)/GAMEDIMENSION);
        if (z > Map.getBlocksZ()-1) z = Map.getBlocksZ()-1;
        
        return new Coordinate(coords.getRelX(), coords.getRelY(), z, true).getBlock().isObstacle();
    }
    
    /**
     * Renders the entity
     **/
    public void render(Graphics g, View view){
        this.render(g, view, getCoords());
    }
    
    /**
     * add this entity to the map-> let it exist
     */
    public void exist(){
        Controller.getMap().getEntitylist().add(this);
    }
    
        /**
     * Returns the side of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return Block.getSideID(pos[0], pos[1]);
    }  

    public float[] getPos() {
        return pos;
    }
    
    public float getPos(int i) {
        return pos[i];
    }

    public void setPos(float[] pos) {
        this.pos = pos;
    }
    
    public void setPos(int i, float pos) {
        this.pos[i] = pos;
    }
}