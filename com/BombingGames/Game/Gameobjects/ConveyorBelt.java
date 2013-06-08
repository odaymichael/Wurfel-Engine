package com.BombingGames.Game.Gameobjects;

import com.BombingGames.Game.Controller;

/**
 *
 * @author Benedikt Vogler
 */
public class ConveyorBelt extends Block implements IsSelfAware{
    int[] coords;
    
    protected ConveyorBelt(int id, int[] absCoords){
        super(id);
        this.coords = absCoords;
        setObstacle(true);
    }

    @Override
    public void update(int delta) {
        int[] relCoords= Controller.getMap().absToRelCoords(coords);
        Block topblock = Controller.getMapData(relCoords[0], relCoords[1], relCoords[2]+1);
        if (topblock.getId() != 0){
            topblock.setPos(2, topblock.getPos()[2]+delta/8f);
            //Controller.requestRecalc();
        }
    }
    
    
    @Override
    public int[] getAbsCoords() {
      return coords; 
    }

    @Override
    public void setAbsCoords(int[] coords) {
        this.coords = coords;
    }

    @Override
    public int[] getRelCoords() {
        return Controller.getMap().absToRelCoords(coords);
    }
    
    @Override
    public void setRelCoords(int[] relCoords) {
        this.coords = Controller.getMap().relToAbsCoords(relCoords);
    }

    @Override
    public void addVector(int[] coords) {
        setAbsCoords(new int[]{getAbsCoords()[0]+coords[0], getAbsCoords()[1]+coords[1], getAbsCoords()[2]+coords[2]});
    }
    
}
