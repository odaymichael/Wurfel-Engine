package com.BombingGames.Game;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.Game.Gameobjects.Block;
import com.BombingGames.Game.Gameobjects.IsSelfAware;


/**
 *
 * @author Benedikt Vogler
 */
public class AirLift extends Block implements IsSelfAware {
    Coordinate coords;

    public AirLift(Coordinate coords, int id) {
        super(id);
        this.coords = coords;
        setObstacle(true);
    }

    @Override
    public void update(float delta) {
        if (coords.getBlock().getId() != 0)
            coords.setHeight(coords.getHeight()+delta/8f);
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
