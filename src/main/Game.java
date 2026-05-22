package main;

import javax.swing.JFrame;

/**
 * Alternative entry point (mirrors Main.java).
 * The original file had a typo: startGameMethod() → startGameThread().
 */
public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("OOP2 Final Project");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread(); // ← was startGameMethod() — fixed
    }
}