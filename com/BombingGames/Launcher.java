package com.BombingGames;

/**
 * The Launcher should contain a line where it creates the wurfelengine. You can show a box where you can set the resolution for example.
 * @author Benedikt Vogler
 */
public class Launcher{
    /**
     * Main method wich starts the game. For a detailed description of the arguments see Wurfelengine class.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @see Wurfelengine
     */
    public static void main(String[] args) {
        Wurfelengine game = Wurfelengine.construct("Wurfelengine V" + Wurfelengine.VERSION, args);    
    }
}