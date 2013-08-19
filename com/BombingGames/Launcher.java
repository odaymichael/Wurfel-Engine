package com.BombingGames;


/**
 * The main class of the Launcher.
 * @author  Benedikt Vogler
 */
public class Launcher{
    /**
     * Main method wich starts the game. For a detailed description of the arguments see Wurfelengine class.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @throws SlickException
     * @see Wurfelengine
     */
    public static void main(String[] args) {
        Wurfelengine game = new Wurfelengine("Wurfelengine V" + Wurfelengine.VERSION, args);    
    }
}