package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *An entitiy wich is animated.
 * @author Benedikt
 */
public class AnimatedEntity extends AbstractEntity implements Animatable{
    private int[] coords;
    private int[] animationsduration;
    private int counter = 0;
    private boolean running;
    private boolean loop;
    
   /**
     * Create this Block with an array wich has the time of every animation step in ms in it.
     * @param id The id of the object
     * @param value the starting value
     * @param  autostart True when it should automatically start.
     * @param coords the coordinates where the animation should be
     * @param loop Set to true when it should loop, when false it stops after one time.
     * @param animationsinformation  
     */
    protected AnimatedEntity(int id, int value, int[] coords, int[] animationsinformation, boolean autostart, boolean loop){
        super(id);
        this.coords = coords;
        this.animationsduration = animationsinformation;
        this.running = autostart;
        this.loop = loop;
    }
    
   /**
     * updates the entity and the animation.
     * @param delta the time wich has passed since last update
     */
    @Override
    public void update(int delta) {
        if (running) {
        counter += delta;
            if (counter >= animationsduration[getValue()]){
                setValue(getValue()+1);
                counter=0;
            }
            
            if (getValue() >= animationsduration.length) {
                if (loop)
                    setValue(0);
                else running = false;
            }
        }
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public int[] getAbsCoords() {
      return coords; 
    }

    @Override
    public void setAbsCoords(int[] coords) {
        this.coords = coords;
    }

    @Override
    public int[] getRelCoords() {
        return Controller.getMap().absoluteToRelativeCoords(coords);
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        setAbsCoords(new int[]{getAbsCoords()[0]+x, getAbsCoords()[1]+y, getAbsCoords()[2]+z});
    }
}
