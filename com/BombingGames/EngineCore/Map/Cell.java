package com.BombingGames.EngineCore.Map;

import com.BombingGames.EngineCore.Gameobjects.Block;

/**
 * A cell is field in the map containing a block. It can have an offset.
 * @author Benedikt Vogler
 */
public class Cell {
    private Block block;
    private float[] cellOffset;

    /**
     *Create a cell containing air-
     */
    public Cell() {
        block = Block.getInstance(0);
        cellOffset = new float[]{0, 0,0};
    }
    
    /**
     *
     * @param id
     */
    public void newBlock(int id){
        this.block = Block.getInstance(id);
    }
    
    /**
     *
     * @param id
     * @param value
     */
    public void newBlock(int id, int value){
       this.block = Block.getInstance(id, value); 
    }
    
    /**
     *
     * @param id
     * @param value
     * @param coords
     */
    public void newBlock(int id, int value, Coordinate coords){
       this.block = Block.getInstance(id, value, coords); 
    }

    /**
     *
     * @return
     */
    public Block getBlock() {
        return block;
    }

    /**
     *
     * @param block
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     *The cell offset has it's center in the top left corner.
     * @return
     */
    public float[] getCellOffset() {
        return cellOffset;
    }

    /**
     *The cell offset has it's center in the top left corner.
     * @param cellOffset
     */
    public void setCellOffset(float[] cellOffset) {
        this.cellOffset = cellOffset;
    }

    /**
     *
     * @param field
     * @param offset
     */
    public void setCellOffset(int field, float offset) {
        this.cellOffset[field] = offset;
    }

}
