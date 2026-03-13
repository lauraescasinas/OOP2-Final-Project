package main.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MainMenu {

    public BufferedImage startButton;
    public BufferedImage exitButton;
    public int startX, startY;
    public int exitX, exitY;

    public MainMenu(){

        try{
            startButton = ImageIO.read(getClass().getResourceAsStream("/ui/StartButton.png"));
            exitButton = ImageIO.read(getClass().getResourceAsStream("/ui/ExitButton.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int screenWidth) {

        startX = screenWidth / 2 - startButton.getWidth() / 2;
        startY = 250;

        exitX = screenWidth / 2 - exitButton.getWidth() / 2;
        exitY = 550;

        g.drawString("PIX CITY", screenWidth/2 - 30, 150);

        g.drawImage(startButton, startX, startY, null);
        g.drawImage(exitButton, exitX, exitY, null);
    }
}
