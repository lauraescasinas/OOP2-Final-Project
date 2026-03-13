package main;

import java.awt.Graphics;
import javax.swing.JPanel;

import main.game.GameState;
import main.game.KeyHandler;
import main.ui.MainMenu;
import main.ui.CharacterCreation;
import main.scene.HomeScene;
import main.game.Camera;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel implements MouseListener, KeyListener, Runnable {
    int gameState = GameState.MENU;
    MainMenu menu = new MainMenu();
    CharacterCreation characterCreation = new CharacterCreation();
    KeyHandler keyH = new KeyHandler();
    Camera camera = new Camera(1920, 1080);
    HomeScene homeScene = new HomeScene(keyH, camera);
    Thread gameThread;
    int FPS = 60;
//    int gameState = GameState.MENU;

    public GamePanel() {

        addMouseListener(this);
        addKeyListener(this);   //para menu type
        addKeyListener(keyH);   // para player movement
        setFocusable(true);
    }

    public void startGameMethod() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {

                update();
                repaint();

                delta--;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(gameState == GameState.MENU) {
            menu.draw(g, getWidth());
        }

        if(gameState == GameState.CHARACTER_CREATION) {
            characterCreation.draw(g, getWidth());
        }

        if(gameState == GameState.HOME) {
            homeScene.draw(g);
        }
    }

    public void update() {

        if(gameState == GameState.HOME) {
            homeScene.update();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int mouseX = e.getX();
        int mouseY = e.getY();

        //start button
        if(mouseX > menu.startX &&
                mouseX < menu.startX + menu.startButton.getWidth() &&
                mouseY > menu.startY &&
                mouseY < menu.startY + menu.startButton.getHeight()) {

            gameState = GameState.CHARACTER_CREATION;
            repaint();
        }

        //exit button
        if(mouseX > menu.exitX &&
                mouseX < menu.exitX + menu.exitButton.getWidth() &&
                mouseY > menu.exitY &&
                mouseY < menu.exitY + menu.exitButton.getHeight()) {

            System.exit(0);
        }
        //enter ang game
        if(gameState == GameState.CHARACTER_CREATION) {

            if(mouseX > characterCreation.enterX &&
                    mouseX < characterCreation.enterX + characterCreation.enterWidth &&
                    mouseY > characterCreation.enterY &&
                    mouseY < characterCreation.enterY + characterCreation.enterHeight) {

                if(!characterCreation.playerName.isEmpty()) {
                    gameState = GameState.HOME;
                    repaint();
                }

            }

        }
    }

    //required empty methods
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void keyTyped(KeyEvent e){

        if(gameState == GameState.CHARACTER_CREATION) {

            char c = e.getKeyChar();

            if(Character.isLetterOrDigit(c) && characterCreation.playerName.length() < 12) {
                characterCreation.playerName += c;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE &&
                characterCreation.playerName.length() > 0) {

            characterCreation.playerName =
                    characterCreation.playerName.substring(0,
                    characterCreation.playerName.length() - 1);

            repaint();
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER &&
                gameState == GameState.CHARACTER_CREATION &&
                !characterCreation.playerName.isEmpty()) {

            gameState = GameState.HOME;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
