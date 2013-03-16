package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;

/**
 *An entity is a game object wich is self aware that means it knows it's position.
 * @author Benedikt
 */
public abstract class AbstractEntity extends GameObject implements IsSelfAware {
   private int[] coords;//the z-field is used as height in px not in coordinates. setting and getting the coordaintes should convert it.
   private float height;
   
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
     * @param absCoords the absolute coordiantes where the entity is.
     * @return the entity.
     */
    public static AbstractEntity getInstance(int id, int value, int[] absCoords){
        AbstractEntity entity = null;
        //define the default SideSprites
        switch (id){
            case 40:
                    try {
                        entity = new Player(id);
                        entity.setTransparent(true);
                        entity.setObstacle(true);
                        entity.setDimensionY(2);
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
        }
        
        entity.setAbsCoords(absCoords);
        entity.setValue(value);
        entity.setVisible(true);
        return entity;
    }
    
    //IsSelfAware implementation
    @Override
    public int[] getAbsCoords() {
        int z = (int) (height/GAMEDIMENSION);
        if (z >= Map.getBlocksZ()) z = Map.getBlocksZ()-1;
        return new int[]{coords[0], coords[1], z};
    }

    @Override
    public void setAbsCoords(int[] coords) {
        this.coords = new int[]{coords[0], coords[1]};
        if (coords.length >= 3) this.height = coords[2]*GAMEDIMENSION;
    }

    @Override
    public int[] getRelCoords() {
        return Controller.getMap().absToRelCoords(getAbsCoords());
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        height += z;
        setAbsCoords(new int[]{getAbsCoords()[0]+x, getAbsCoords()[1]+y});
    }

    //overrides
    @Override
    public void setPos(int i, float value) {
        if (i==2) value = height % GAMEDIMENSION;
        super.setPos(i, value);
    }

    /**
     * if you want to set the height (z-axis) use setHeight.
     * @param pos 
     */
    @Override
    public void setPos(float[] pos) {
        super.setPos(new float[]{pos[0], pos[1], height % GAMEDIMENSION});
    }

    /**
     * 
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;
        super.setPos(2, height % GAMEDIMENSION);
    }
    
    /**
     * Is the entity laying/standing on the ground?
     * @return true when on the ground
     */
    public boolean onGround(){
        int z = (int) ((height-1)/GAMEDIMENSION);
        if (z > Map.getBlocksZ()-1) z = Map.getBlocksZ()-1;
        return Controller.getMapData(
                    Controller.getMap().absToRelCoords(
                        new int[]{coords[0],coords[1],z}
                    )
                ).isObstacle();
    }
}
