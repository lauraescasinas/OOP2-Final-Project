package main;

import main.GamePanel;
public class Game {

    private GameWindow gameWindow;
    private GamePanel gamePanel;

    public Game() {

        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);

        gamePanel.startGameMethod(); // start game loop

    }
}