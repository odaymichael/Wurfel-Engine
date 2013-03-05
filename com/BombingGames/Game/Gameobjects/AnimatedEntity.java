package com.BombingGames.Game.Gameobjects;

/**
 *
 * @author Benedikt
 */
public class AnimatedEntity extends AbstractEntity implements IAnimation{
    private int[] animationsduration;
    private int counter = 0;
    private boolean running;
    private boolean loop;
    
   /**
     * Create this Block with an array wich has the time of every animation step in ms in it.
     * @param animationinformation array wich has time in ms of each value. Example: new int[]{600,200,1000}
     * @param  autostart True when it should automatically start.
     * @param loop Set to true when it should loop, when false it stops after one time. 
     */
    protected AnimatedEntity(int id, int[] animationsinformation, boolean autostart, boolean loop){
        super(id);
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
        if (running) counter = Animator.updateAnimation(this, animationsduration, delta, counter, loop);
    }

    /**
     * Starts the animation.
     */
    @Override
    public void start() {
        running = true;
    }

    /**
     * Stops the animation.
     */
    @Override
    public void stop() {
        running = false;
    }

    @Override
    public int[] getAbsCoords() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAbsCoords(int[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getRelCoords() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToAbsCoords(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
