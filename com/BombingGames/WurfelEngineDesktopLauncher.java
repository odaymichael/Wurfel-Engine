package com.BombingGames;

/**
 * The Launcher should contain a line where it creates the wurfelengine. You can show a box where you can set the resolution for example.
 * @author Benedikt Vogler
 */
public class WurfelEngineDesktopLauncher{
    /**
     * Main method wich starts the game. For a detailed description of the arguments see WurfelEngine class.
     * @param args custom display resolution: [0] width, [1] height, [2] fullscreen
     * @see WurfelEngine
     */
    public static void main(String[] args) {
        WurfelEngine.construct("Wurfelengine V" + WurfelEngine.VERSION, args); 
    }
}