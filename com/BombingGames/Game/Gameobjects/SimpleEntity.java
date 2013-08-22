package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Coordinate;

/**
 *A simple basic entity. You can use it for simple images like an effect.
 * @author  Benedikt Vogler
 */
class SimpleEntity extends AbstractEntity {
    public SimpleEntity(int id) {
        super(id);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public Coordinate getCoords() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCoords(Coordinate coords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
