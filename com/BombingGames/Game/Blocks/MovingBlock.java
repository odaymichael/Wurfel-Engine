package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import org.newdawn.slick.SlickException;

/**
 *A Block which can move himself around the map, therefore it must also be a SelfAwareBlock.
 * @author Benedikt
 */
public abstract class MovingBlock extends SelfAwareBlock {
    /**
     * Value in pixels
     */
   private float posX = Block.WIDTH / 2;
   private float posY = Block.WIDTH / 2;
   private float posZ = 0;
   
   /*The three values together build a vector. Always one of them must be 1 to prevent a division with 0.*/
   private float dirX = 1;
   private float dirY = 0;
   private float dirZ = 0;
   
   /**
    * provides a factor for the vector
    */
   private float speed;
   
   /**
     * These method should define what happens when the object  jumps. Shoudl call jump(int velo)
     */
    abstract void jump();
   
    MovingBlock(){
        super();
    }
    
    MovingBlock(int id){
        super(id);
    }
   
    MovingBlock(int id,int value){
        super(id, value);
    }
    
    
    /**
     * Returns the side of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return getSideNumb((int) posX,(int) posY);
    }
        
    /**
     * Lets the player walk
     * @param up
     * @param down
     * @param left
     * @param right
     * @param walkingspeed the higher the speed the bigger the steps
     * @param delta time which has passed since last call
     * @throws SlickException 
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        walk(up, down, left, right, walkingspeed, delta, null);
    }
    
    
   /**
     * Lets the player walk with a second block on top.
     * @param up 
     * @param down
     * @param left 
     *  @param right 
     * @param walkingspeed the higher the speed the bigger the steps
     *  @param delta time which has passed since last call
     * @param topblock The block who should be on top.
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta, Blockpointer topblock) throws SlickException {
        //if the player is walking then move him
        if (up || down || left || right) {
            speed = walkingspeed;
            
            //update the movement vector
            dirX = 0;
            dirY = 0;
               
            if (up)    dirY = -1;
            if (down)  dirY = 1;
            if (left)  dirX = -1;
            if (right) dirX = 1;
        
            //scale that the velocity vector is always an unit vector
            double vectorLenght = Math.sqrt(dirX*dirX+dirY*dirY);
            dirX /= vectorLenght;
            dirY /= vectorLenght;
            //veloZ /= vectorLenght;
            
            //colision check
            float oldx = posX;
            float oldy = posY;
            //calculate new position
            float newx = posX + delta * speed * dirX;
            float newy = posY + delta * speed * dirY;
            
            //check if position is okay
            boolean movementokay = true;
            
            //check for movement in x
            int sideNumber = getSideNumb((int) newx, (int) newy - Block.HEIGHT-1); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;

            sideNumber = getSideNumb((int) newx, (int) newy + Block.HEIGHT+1); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false; 
            
            //find out the direction of the movement
            if (oldx-newx > 0) {
                //check left corner
                sideNumber = getSideNumb((int) newx - Block.HEIGHT-1, (int) newy);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            } else {
                //check right corner
                sideNumber = getSideNumb((int) newx + Block.HEIGHT+1, (int) newy);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            }
            
            //check for movement in y
            sideNumber = getSideNumb((int) newx - Block.WIDTH/2-1, (int) newy); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;

            sideNumber = getSideNumb((int) newx + Block.WIDTH/2+1, (int) newy); 
            if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                movementokay = false;  
            
            if (oldy-newy > 0) {
                //check top corner
                sideNumber = getSideNumb((int) newx, (int) newy - Block.WIDTH/2-1);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            } else {
                //check bottom corner
                sideNumber = getSideNumb((int) newx, (int) newy + Block.WIDTH/2+1);
                if (sideNumber != 8 && getNeighbourBlock(sideNumber, 0).isObstacle())
                   movementokay = false;
            }
            
            //if movement allowed => move player   
            if (movementokay) {                
                posX = newx;
                posY = newy;
                
                //track the coordiante change, if there is one
                int sidennumb = getSideNumb();
                switch(sidennumb){
                    case 0:
                    case 1:
                            posY += Block.WIDTH / 2;
                            posX -= Block.WIDTH / 2;

                            makeCoordinateStep(1,-1, topblock);
                            break;
                    case 2:    
                    case 3:
                            posY -= Block.WIDTH/2;
                            posX -= Block.WIDTH/2;

                            makeCoordinateStep(1,1, topblock);
                            break;
                    case 4:
                    case 5:
                            posY -= Block.WIDTH/2;
                            posX += Block.WIDTH/2;

                            makeCoordinateStep(-1,1, topblock);
                            break;
                    case 6:
                    case 7:
                            posY += Block.WIDTH/2;
                            posX += Block.WIDTH/2;

                            makeCoordinateStep(-1,-1, topblock);
                            break;    
                }

                //if there was a coordiante change recalc map.
                if (sidennumb != 8) Controller.getMap().requestRecalc();

                //set the offset for the rendering
                setOffset((int) posX - Block.WIDTH/2, (int) posY - (int) posZ - Block.HEIGHT);
                //copy offset to topblock
                if (topblock != null) 
                    topblock.getBlock().setOffset(getOffsetX(), getOffsetY());
            }
        }
        //enable this line to see where to player stands:
        Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()-1).setLightlevel(30);
   }
    
   /**
     * Make a step
     * @param x left or right step
     * @param y the coodinate steps
     * @param topblock if you want to also move a block on top add a pointer to it. If not wanted: null.
     */
    private void makeCoordinateStep(int x, int y, Blockpointer topblock){
        selfDestroy();
        if (topblock != null) topblock.setBlock(new Block(0));
        
        setAbsCoordY(getAbsCoordY()+y);
        if (x<0){
            if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
        } else {
            if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
        }

        selfRebuild();
        if (topblock != null) topblock.setBlock(new Block(getId()));
    }
    
       /**
     * Updates the block.
     * @param delta time since last update
     * @param topblock the block on top, if there is none set it to null
     */
    protected void update(int delta, Blockpointer topblock) {
        //calculate movement
        float t = delta/1000f; //t = time in s
        dirZ += -Map.GRAVITY*t; //in m/s
        float newposZ = posZ + dirZ*Block.WIDTH*t; //m

        //land if standing in or under 0-level and there is an obstacle
        if (dirZ <= 0
            && newposZ <= 0
            && (getCoordZ() == 0 || Controller.getMapDataUnsafe(getCoordX(), getCoordY(), getCoordZ()-1).isObstacle())
        ) {
            // fallsound.stop();
            dirZ = 0;
            newposZ=0;
        }
        posZ = newposZ;
        
        //coordinate switch
        //down
        if (posZ < 0
            && getCoordZ() > 0
            && ! Controller.getMapData(getCoordX(), getCoordY(),getCoordZ()-1).isObstacle()){
          //  if (! fallsound.playing()) fallsound.play();
            
            selfDestroy();
            if (topblock != null) topblock.setBlock(new Block(0));
            setCoordZ(getCoordZ()-1);
            selfRebuild();
            if (topblock != null) topblock.setBlock(new Block(getId()));

            posZ += Block.WIDTH;
            Controller.getMap().requestRecalc();
        } else {
            //up
            if (posZ >= Block.WIDTH
                && getCoordZ() < Chunk.getBlocksZ()-2
                && !Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()+2).isObstacle()){
                //if (! fallsound.playing()) fallsound.play();

                selfDestroy();
                if (topblock != null) topblock.setBlock(new Block(0));
                setCoordZ(getCoordZ()+1);
                selfRebuild();
                if (topblock != null) topblock.setBlock(new Block(getId()));

                posZ -= Block.WIDTH;
                Controller.getMap().requestRecalc();
            } 
        }
        
        //set the offset for the rendering
        setOffset((int) (getPosX() - Block.WIDTH/2), (int) (getPosY() - posZ - Block.WIDTH/2));
        if (topblock != null) topblock.getBlock().setOffset(getOffsetX(), getOffsetY());  
    }
   
    /**
     * 
     * @return
     */
    public float getPosX() {
        return posX;
    }

    /**
     * 
     * @return
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Set the PosZ
     * @return
     */
    public float getPosZ() {
        return posZ;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

 
    
    /*
     * Returns true if the player is standing on ground.
     */
    public boolean isStanding(){
       return (dirZ == 0 && posZ == 0);
    }

    /**
     * Jumpwith a specific speed
     * @param height the velocity of the block 
     */
    public void jump(float velo) {
        if (isStanding()) dirZ = velo;
    }
    
    /**
     * Returns a normalized vector wich contains the direction of the block.
     * @return R
     */
    public float[] getDirectionVector(){
        return new float[] {dirX, dirY, dirZ};
    }
}
