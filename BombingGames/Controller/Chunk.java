package BombingGames.Controller;


public class Chunk {
        public int coordX, coordY, posX, posY;
        public Block chunkdata[][][] = new Block[GameController.ChunkSizeX][GameController.ChunkSizeY][GameController.ChunkSizeZ];
        public static final int width = GameController.ChunkSizeX*GameController.tilesizeX;
        public static final int height = GameController.ChunkSizeY*GameController.tilesizeY/2;

        //Konstruktor
        public Chunk(int X, int Y, int startposX, int startposY){
            coordX = X;
            coordY = Y;
            posX = startposX;
            posY = startposY;
            
            //chunkdata will contain the blocks and objects
            //alternative to chunkdata.length ChunkSize
            for (int i=0; i < chunkdata.length; i++)
                for (int j=0; j < chunkdata[0].length; j++){
                    //Erde
                    for (int k=0; k < chunkdata[0][0].length / 2; k++)
                        chunkdata[i][j][k] = new Block(2,0);
                    
                    //Gras
                    chunkdata[i][j][chunkdata[0][0].length / 2] = new Block(1,0);
                    
                    //Luft
                    for (int k=chunkdata[0][0].length / 2; k < chunkdata[0][0].length; k++)
                        chunkdata[i][j][k] = new Block(0,0);
                }
        }
        
       public void loadchunk(){

       }
}
