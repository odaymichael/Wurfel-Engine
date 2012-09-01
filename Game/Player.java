/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Benedikt
 */
public class Player {
   int posX = 0;
   int posY = 0;
   int posZ = 0;
   int coordX;
   int coordY;
   int coordZ;
   int ChunkCoordX;
   int ChunkCoordY;
   private Image picture;
   
    /**Konstruktor
     *
     * @param X
     * @param Y
     * @param Z
     * @throws SlickException
     */
    public Player(int X, int Y, int Z) throws SlickException{
     //chunk is always 0,0 at the moment
     picture = new Image("Game/Images/Player.PNG");
     coordX = X;
     coordY = Y;
     coordZ = Z;
     if (coordZ > Chunk.BlocksZ-2) coordZ = Chunk.BlocksZ -2;
     Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(40,0);
     Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(40,1);
   }
   
   public void draw(){
        picture.draw(
                Controller.chunklist[4].posX + coordX*Block.width + (coordY%2) * Block.width/2 + posX,
                Controller.chunklist[4].posY + coordY*Block.height/4 - coordZ*Block.height/2 + posY + posZ,
                View.zoom
                );
   }
   
    public void walk(int dir){
        int chunk = 4;
        switch (dir) {
            case 1:
                Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
                Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
                coordY--;
                //border of chunk reached
                if (coordY < 0) {
                    GameplayState.iglog.add("Border: "+dir);
                    coordY = Chunk.BlocksY-1;
                    ChunkCoordY = Controller.chunklist[4].coordY+1;
                    chunk = 1;
                }
                 
                Controller.chunklist[chunk].data[coordX][coordY][coordZ] = new Block(40,0);
                Controller.chunklist[chunk].data[coordX][coordY][coordZ+1] = new Block(40,1);  
                Controller.changes = true;
           break;
               
           case 3: 
                Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
                Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
                coordX--;
                
                if (coordX<0) {
                    GameplayState.iglog.add("Border: "+dir);
                    coordX = Chunk.BlocksX-1;
                    ChunkCoordX = Controller.chunklist[4].coordX-1;
                    chunk = 3; 
                }
                
                Controller.chunklist[chunk].data[coordX][coordY][coordZ] = new Block(40,0);
                Controller.chunklist[chunk].data[coordX][coordY][coordZ+1] = new Block(40,1);
                Controller.changes = true;
           break;
               
           case 5: 
                Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
                Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
                coordX++;
                
                if (coordX > Chunk.BlocksX-1) {
                    GameplayState.iglog.add("Border: "+dir);
                    coordX = 0;
                    ChunkCoordX = Controller.chunklist[4].coordX+1;
                    chunk = 5;
                }
                
                Controller.chunklist[chunk].data[coordX][coordY][coordZ] = new Block(40,0);
                Controller.chunklist[chunk].data[coordX][coordY][coordZ+1] = new Block(40,1);
                Controller.changes = true;
           break;        
           
           case 7: 
                Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
                Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
                coordY++;
                
                if (coordY > Chunk.BlocksY-1) {
                    GameplayState.iglog.add("Border: "+dir);
                    coordY = Chunk.BlocksY-1;
                    ChunkCoordY = Controller.chunklist[4].coordY-1;
                    chunk = 7; 
                }
                
                Controller.chunklist[chunk].data[coordX][coordY][coordZ] = new Block(40,0);
                Controller.chunklist[chunk].data[coordX][coordY][coordZ+1] = new Block(40,1);
                Controller.changes = true;
           break;
       }
       GameplayState.iglog.add(coordX+":"+coordY+":"+coordZ);
               
   }
   
   public void jump(){
       if (coordZ<Chunk.BlocksZ-2){
           Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
           Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
           coordZ++;
           Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(40,0);
           Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(40,1);
           
           Controller.changes = true;
       }
       GameplayState.iglog.add(coordX+":"+coordY+":"+coordZ);
   }
   
   public void update(){
       if (Controller.chunklist[4].data[coordX][coordY][coordZ-1].ID() == 0 && coordZ>1){
           Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(0,0);
           Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(0,0);
           coordZ--;
           Controller.chunklist[4].data[coordX][coordY][coordZ] = new Block(40,0);
           Controller.chunklist[4].data[coordX][coordY][coordZ+1] = new Block(40,1);
           GameplayState.iglog.add(coordX+":"+coordY+":"+coordZ);
       }
   }
           
}
