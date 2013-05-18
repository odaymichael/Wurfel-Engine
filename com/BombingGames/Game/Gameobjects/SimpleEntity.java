package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *A simple basic entity. You can use it for simple images like an effect.
 * @author  Benedikt Vogler
 */
class SimpleEntity extends AbstractEntity {
    public SimpleEntity(int id) {
        super(id);
    }

    @Override
    public void update(Controller controller, int delta) {
    }
}
