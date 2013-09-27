package com.BombingGames.EngineCore.Gameobjects;

/**
 *A block who has an animation.
 * @author Benedikt
 */
public class AnimatedBlock extends Block implements Animatable{
    private final int[] animationsduration;
    private int counter = 0;
    private boolean running;
    private final boolean loop;
    
    /**
     * Create this Block with an array wich has the time of every animation step in ms in it.
     * @param id the id of the block.
     * @param animationsinformation  an array wich has the duration of every animationstep inside
     * @param  autostart True when it should automatically start.
     * @param loop Set to true when it should loop, when false it stops after one time. 
     */
    protected AnimatedBlock(int id, int[] animationsinformation, boolean autostart, boolean loop){
        super(id);
        this.animationsduration = animationsinformation;
        this.running = autostart;
        this.loop = loop;
    }
    
   /**
     * updates the block and the animation.
     * @param delta the time wich has passed since last update
     */
    @Override
    public void update(float delta) {
        if (running) {
            counter += delta;
            if (counter >= animationsduration[getValue()]){
                setValue(getValue()+1);
                counter=0;
                if (getValue() >= animationsduration.length)//if over animation array
                    if (loop)
                        setValue(0);
                    else{
                        running = false;
                        setValue(getValue()-1);
                    }
            }
        }
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
}
