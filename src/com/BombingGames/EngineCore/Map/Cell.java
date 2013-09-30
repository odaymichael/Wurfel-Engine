package com.BombingGames.EngineCore.Map;

import com.BombingGames.EngineCore.Gameobjects.Block;

/**
 * A cell is field in the map containing a block. It can have an offset.
 * @author Benedikt Vogler
 */
public class Cell {
    private Block block;
    private float[] cellOffset = new float[]{0, 0,0};

    /**
     *Create a new cell containing air.
     */
    public Cell() {
        block = Block.getInstance(0);
    }
    
    /**
     *Create a new block in this cell.
     * @param id
     */
    public Cell(int id){
        block = Block.getInstance(id);
    }
    
    /**
     *Create a new block in this cell.
     * @param id
     * @param value
     */
    public Cell(int id, int value){
       this.block = Block.getInstance(id, value); 
    }
    
    /**
     *Create a new block in this cell.
     * @param id
     * @param value
     * @param coords
     */
    public Cell(int id, int value, Coordinate coords){
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
     *Set the block inside this cell. The offset stays the same.
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
