/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BombingGames.Game;

/**
 *
 * @author Benedikt
 */
public class Renderblock {
        protected final int x,y,z;
        protected final int depth;
        protected final int entityindex;

        protected Renderblock(int x, int y, int z, int depth, int entitynumber) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
            this.entityindex = entitynumber;
        }
    }
