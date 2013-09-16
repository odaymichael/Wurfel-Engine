package com.BombingGames.EngineCore.Map;

import com.BombingGames.Game.Gameobjects.Block;

/**
 * A cell is field in the map containing a block. It can have an offset.
 * @author Benedikt Vogler
 */
public class Cell {
    private Block block;
    private float[] cellOffset;

    public Cell() {
        block = Block.getInstance(0);
        cellOffset = new float[]{Block.DIM2, Block.DIM2,0};
    }
    
    public void newBlock(int id){
        this.block = Block.getInstance(id);
    }
    
    public void newBlock(int id, int value){
       this.block = Block.getInstance(id, value); 
    }
    
    public void newBlock(int id, int value, Coordinate coords){
       this.block = Block.getInstance(id, value, coords); 
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public float[] getCellOffset() {
        return cellOffset;
    }

    public void setCellOffset(float[] cellOffset) {
        this.cellOffset = cellOffset;
    }

    public void setCellOffset(int field, float offset) {
        this.cellOffset[field] = offset;
    }

}
