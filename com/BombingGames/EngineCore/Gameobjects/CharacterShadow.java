/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game.Gameobjects;

import com.BombingGames.EngineCore.Map.Coordinate;
import com.BombingGames.EngineCore.View;
import com.BombingGames.EngineCore.WECamera;
import com.BombingGames.EngineCore.LightEngine.PseudoGrey;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author Benedikt Vogler
 */
class CharacterShadow extends AbstractEntity {
    private AbstractCharacter character;

    protected CharacterShadow(int id) {
        super(id);
    }

    @Override
    public void update(float delta) {
    }
    
    public void update(float delta, AbstractCharacter character){
        this.character = character;
        Coordinate tmpCoords = character.getCoords().cpy();
        tmpCoords.setZ(tmpCoords.getZ());
        while (tmpCoords.getZ() > 0 && tmpCoords.addVectorCpy(0, 0, -1).getBlock().isTransparent())
            tmpCoords.addVector(0, 0, -1);
        this.setCoords(tmpCoords);
        this.setPositionX(character.getPositionX());
        this.setPositionY(character.getPositionY());
    }

    @Override
    public void render(View view, WECamera camera, Coordinate coords) {
        Color color = PseudoGrey.toColor(
                (character.getCoords().getHeight() - getCoords().getZ()*Block.GAMEDIMENSION)/Block.GAMEDIMENSION
                );
        super.render(view, camera, coords,color);
    }
    
    
    
}
