package com.BombingGames;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * The main class of the Launcher.
 * @author  Benedikt Vogler
 */
public class Launcher{
    /**
     * Main method wich starts the game
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer game = new Wurfelengine(
            new StateBasedEngine("Wurfelengine V" + Wurfelengine.VERSION), args);    
    }
}