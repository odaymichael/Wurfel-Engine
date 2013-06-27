package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Coordinate;

/**
 *A simple basic entity. You can use it for simple images like an effect.
 * @author  Benedikt Vogler
 */
class SimpleEntity extends AbstractEntity {
    public SimpleEntity(int id) {
        super(id);
    }

    @Override
    public void update(int delta) {
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
