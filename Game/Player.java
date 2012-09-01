/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Benedikt
 */
public class Player {
   public int posX = 0;
   public int posY = 0;
   public int posZ = 0;
   private int absCoordX;
   private int absCoordY;
   public int coordZ;
   private int relCoordX;
   private int relCoordY;
   public int ChunkCoordX;
   public int ChunkCoordY;
   private Image picture;
   Sound fallsound;
   
    /**Creates a player
     *
     * @param X X Pos
     * @param Y Y Pos
     * @param Z Z Pos
     * @throws SlickException
     */
    public Player(int X, int Y, int Z) throws SlickException{
        picture = new Image("Game/Images/Player.PNG");
        fallsound = new Sound("Game/Sounds/wind.wav");
        setAbsCoords(X,Y,Z);
        //if Z is too high set to highes possible position
        if (coordZ > Chunk.BlocksZ-2) coordZ = Chunk.BlocksZ -2;
        
        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,1);
    }
    
    private void setAbsCoordX(int X){
      absCoordX = X;
      relCoordX = absCoordX - Controller.map.coordlistX[4]*Chunk.BlocksX;
    }
    
    private void setAbsCoordY(int Y){
      absCoordY = Y;
      relCoordY = absCoordY - Controller.map.coordlistY[4]*Chunk.BlocksY;
    }    
   
    private void setAbsCoords(int X, int Y, int Z){
      setAbsCoordX(X);
      setAbsCoordY(Y);
      coordZ = Z;
    }
    
    public int relCoordX(){
        return relCoordX;
    }
    
    public int relCoordY(){
        return relCoordY;
    }
       
    public void walk(int dir){
        int chunk = 4;
        switch (dir) {
            case 1:
                if (relCoordY > 0) {
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordY(absCoordY-1);
              
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);  
                    Controller.map.changes = true;
                }else {
                     GameplayState.iglog.add("Border: "+dir);
                }
           break;
               
           case 3:
               if (relCoordX > 0) {
                Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                setAbsCoordX(absCoordX-1);
                
                
                Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                Controller.map.changes = true;
               }else {
                    GameplayState.iglog.add("Border: "+dir);  
               }
           break;
               
           case 5: 
                if (relCoordX < Chunk.BlocksX*3-1) {
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordX(absCoordX+1);
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                    Controller.map.changes = true;
                } else {
                    GameplayState.iglog.add("Border: "+dir);     
                }
           break;        
           
           case 7:
                if (relCoordY < Chunk.BlocksY*3-1) {
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordY(absCoordY+1);
                
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                    Controller.map.changes = true;
                }else {
                     GameplayState.iglog.add("Border: "+dir);
                }
           break;
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
               
   }
   
   public void jump(){
       if (coordZ<Chunk.BlocksZ-2){
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
           coordZ++;
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
           
           Controller.map.changes = true;
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
   }
   
   public void update(){
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
