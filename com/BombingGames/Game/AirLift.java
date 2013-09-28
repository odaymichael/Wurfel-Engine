package com.BombingGames.Game;

import com.BombingGames.EngineCore.Gameobjects.Block;
import com.BombingGames.EngineCore.Gameobjects.IsSelfAware;
import com.BombingGames.EngineCore.Map.Coordinate;


/**
 *
 * @author Benedikt Vogler
 */
public class AirLift extends Block implements IsSelfAware {
    private Coordinate coords;

    public AirLift(Coordinate coords, int id) {
        super(id);
        this.coords = coords;
        setObstacle(true);
    }

    @Override
    public void update(float delta) {
        Coordinate topblock = coords.addVectorCpy(0, 0, 1);
        if (topblock.getBlock().getId() != 0)
            topblock.setCellOffsetZ(topblock.getCellOffset()[2]+delta/8f);
    }
    

    @Override
    public Coordinate getCoords() {
        return coords;
    }

    @Override
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }
}
