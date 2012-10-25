package Game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *The Player is a character who can walk. absCooords are the coordiantes which are absolute to the map. Relative is relative to the currently loaded chunks (map).
 * @author Benedikt
 */
public class Player extends Block{
    /**
     * The X Position of the center of the player. value in pixel.
     */
    public int posX = Block.width / 2;
   /**
    * The Y Position on a block. value in pixel.
    */
   public int posY = Block.height / 4;
   
   /**
    * The Z Position on a block. value in pixel.
    */
   public int posZ = 0;
   private int absCoordX;
   private int absCoordY;
   
   /**
    * CoordZ is always absolute and relative at the same time.
    */
   public int coordZ;
   private int relCoordX;
   private int relCoordY;
   private Sound fallsound = new Sound("Game/Sounds/wind.wav");
   private int timetillmove;
   private String controls = "WASD";
   
   
   
    /**
     * Creates a player. The parameters are for the lower half of the player. The constructor automatically creates a block on top of it.
     * @param X Absolute X-Pos of lower half
     * @param Y Absolute Y-Pos of lower half
     * @param Z Absolute Z-Pos of lower half
     * @throws SlickException
     */
    public Player(int X, int Y, int Z) throws SlickException{
        super(40,0);
        setAbsCoords(X,Y,Z);
        Controller.map.data[X][Y][coordZ+1] = new Block(40,1);
    }
    
    /**
     * Sets the absolute X and relative X coord.
     * @param X 
     */
    private void setAbsCoordX(int X){
        absCoordX = X;
        setRelCoordX(X - Controller.map.getCoordlistX(4)  * Chunk.BlocksX);
    }
    

    private void setAbsCoordY(int Y){
        absCoordY = Y;
        //da das Map Coordinatensystem in y-Richtung in zwei unterschiedliche Richtungen geht (hier "+" ???)
        setRelCoordY(Y + Controller.map.getCoordlistY(4) *Chunk.BlocksY);
    }    
   
    private void setAbsCoords(int X, int Y, int Z){
        setAbsCoordX(X);
        setAbsCoordY(Y);
        coordZ = Z;
        //if Z is too high set to highes possible position
        if (coordZ > Chunk.BlocksZ-2) coordZ = Chunk.BlocksZ -2;
        
    }
    
    /**
     * Set the relative X Coordinate.
     * @param X
     */
    public void setRelCoordX(int X){
        if (X < Chunk.BlocksX*3){
            relCoordX = X;
        } else {
            this.relCoordX = 3*Chunk.BlocksX-1;
            GameplayState.iglog.add("RelativeCoordX ist too high:"+X);
            System.out.println("RelativeCoordX ist too high:"+X);
        }
        
        if (X >= 0) {
            relCoordX = X;
        } else {
            relCoordX = 0;
            GameplayState.iglog.add("RelativeCoordX ist too low:"+X);
            System.out.println("RelativeCoordX ist too low:"+X);
        }
    }
    
    /**
     * Get the relative X Coordinate of the player
     * @return X coordinate
     */
    public int getRelCoordX(){
        return relCoordX;
    }
    
    /**
     * Set the relative Y Coordinate
     * @param Y
     */
    public void setRelCoordY(int Y){
        if (Y < Chunk.BlocksY*3){
            relCoordY = Y;
        }else {
            relCoordY = 3*Chunk.BlocksY-1;
            GameplayState.iglog.add("RelativeCoordY ist too high: "+Y);
            System.out.println("RelativeCoordY ist too high: "+Y);
        }
        
        if (Y >= 0) {
            relCoordY = Y;
        }else {
            relCoordY = 0;
            GameplayState.iglog.add("RelativeCoordY ist too low: "+Y);
            System.out.println("RelativeCoordY ist too low: "+Y);
        }
    }
    
    /**
     * 
     * @return
     */
    public int getRelCoordY(){
        return relCoordY;
    }
    
    public void setControls(String controls){
        if ("arrows".equals(controls) || "WASD".equals(controls))
            this.controls = controls;
    }
    
    public String getControls(){
        return controls;
    }
       
    /**
     * Let the player walk
     * @param dir 1 up, 3: left, 5: right, 7: down
     * @param delta time which has passed since last call
     * @throws SlickException
     */
    public void walk(int dir, int delta) throws SlickException {
        timetillmove += delta;
        if (timetillmove > 10){
            switch (dir) {
                case 1://up
                    posY -= 3+Block.height/4;
                    if (    (
                                inCorner(0)
                                && ((Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY-1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY-1][coordZ+1].obstacle))
                            )
                        || 
                            (
                                inCorner(1)
                                && ((Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY-1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY-1][coordZ+1].obstacle))
                            )
                        )
                        posY+=3;
                    posY += Block.height/4;
                    
                break;

                case 3: //left
                    posX-=3+Block.width/2;
                    if (    (
                                inCorner(0) 
                                && ((Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY-1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY-1][coordZ+1].obstacle))
                            )
                        ||
                            (
                                inCorner(3)
                                && ((Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY+1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY+1][coordZ+1].obstacle))
                            )
                       )
                            posX += 3;
                    posX += Block.width/2;
                    
                break;

                case 5: //right
                    posX += 3+Block.width/2;
                    if (    (
                                inCorner(1)
                                && ((Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY-1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY-1][coordZ+1].obstacle))
                            ) 
                        ||
                            (
                                inCorner(2)
                                && (Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY+1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY+1][coordZ+1].obstacle))
                        )
                            posX-=3;
                    posX -= Block.width/2;
                break;        

                case 7://down
                    posY += 3+Block.height/4;
                    if (    (
                                inCorner(2)
                                && ((Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY+1][coordZ].obstacle)
                                || (Controller.map.data[relCoordX +(absCoordY % 2 == 1 ? 1 : 0)][relCoordY+1][coordZ+1].obstacle))
                            ) 
                        ||
                            (
                            inCorner(3)
                            && (Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY+1][coordZ].obstacle)
                            || (Controller.map.data[relCoordX -(absCoordY % 2 == 0 ? 1 : 0)][relCoordY+1][coordZ+1].obstacle))
                        )
                            posY-=3;
                    posY -= Block.height/4;
                break;
            }
            
            //check the coordiante movement
            if (inCorner(0)){
                GameplayState.iglog.add("oben links");
                posY = posY + Block.height/4;
                posX = posX + Block.width/2;
                
                
                Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                setAbsCoordY(absCoordY-1);
                if (absCoordY % 2 == 1) setAbsCoordX(absCoordX-1);

                Controller.map.data[relCoordX][relCoordY][coordZ] = this;
                Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                Controller.map.requestRecalc();
            } else {
                if (inCorner(1)) {
                    GameplayState.iglog.add("oben rechts");
                    posY = posY + Block.height / 4;
                    posX = posX - Block.width / 2;
                    
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordY(absCoordY-1);
                    if (absCoordY % 2 == 0) setAbsCoordX(absCoordX+1);
                    
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);  
                    Controller.map.requestRecalc();
                } else {
                    if (inCorner(3)) {
                        GameplayState.iglog.add("unten links");
                        posY = posY - Block.height/4;
                        posX = posX + Block.width/2;
                        
                        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                        Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                        setAbsCoordY(absCoordY+1);
                        if (absCoordY % 2 == 1) setAbsCoordX(absCoordX-1);

                        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                        Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                        Controller.map.requestRecalc();
                    } else {
                        if (inCorner(2)) {
                            GameplayState.iglog.add("unten rechts");
                            posY = posY - Block.height/4;
                            posX = posX - Block.width/2;
                            
                            Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                            Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                            setAbsCoordY(absCoordY+1);
                            if (absCoordY % 2 == 0) setAbsCoordX(absCoordX+1);
                            
                            Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                            Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                            Controller.map.requestRecalc();
                        }
                   }
                }
            }
            
            setOffset(posX -Block.width/2, posY + posZ - Block.height/4);
            
            //Top block of player
            Controller.map.data[relCoordX][relCoordY][coordZ+1].setOffset(getOffsetX(), getOffsetY());
                 
           timetillmove = 0;
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
        //System.out.println(relCoordX+":"+relCoordY+":"+coordZ);
              
   }
    
    private boolean inCorner(int corner) {
        switch (corner){
            case 0://top left
                return (posX+2*posY <= Block.width /2 && relCoordY < Chunk.BlocksY*3-1);
            case 1://top right
                return (-posX + 2*posY < -Block.height/2 && relCoordY < Chunk.BlocksY*3-1);
            case 2://bottom right
                return (posX + 2*posY > Block.width /2 + Block.height && relCoordX > 0);
            case 3://bottom left
                return (-posX + 2*posY> Block.height /2 && relCoordX > 0);
            default: return false;
        }
    }
   
    /**
     * 
     */
    public void jump(){
       if (coordZ<Chunk.BlocksZ-2){
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
           coordZ++;
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
           
           Controller.map.requestRecalc();
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
   }
   
    /**
     * 
     * @param delta
     */
    public void update(int delta){
       
       //Gravitation
       if (Controller.map.data[relCoordX][relCoordY][coordZ-1].obstacle==false && coordZ>1){
           if (! fallsound.playing()) fallsound.play();
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
           coordZ--;
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
           //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
       } else{
            fallsound.stop();
       }
   }
           
}
