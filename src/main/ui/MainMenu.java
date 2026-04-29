package main.ui;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MainMenu {

    public BufferedImage startButton, exitButton;
    public BufferedImage[] frames = new BufferedImage[4];

    // Animation timing
    public int spriteCounter = 0;
    public int spriteNum = 0;

    public int startX, startY, exitX, exitY;

    public MainMenu(){
        try {
            // Reverted these to your working paths
            startButton = ImageIO.read(getClass().getResourceAsStream("/ui/StartButton.png"));
            exitButton = ImageIO.read(getClass().getResourceAsStream("/ui/ExitButton.png"));

            // Loading Animation Frames from assets
            for(int i = 0; i < 4; i++) {
                String path = "/assets/main/MainScreen_" + (i+1) + ".png";
                frames[i] = ImageIO.read(getClass().getResourceAsStream(path));
            }
        } catch(Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }

    public void update() {
        // 60 FPS * 0.8 seconds = 48
        spriteCounter++;
        if(spriteCounter > 48) {
            spriteNum++;
            if(spriteNum >= 4) spriteNum = 0;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics g, int screenWidth, int screenHeight) {
        // 1. Draw Black Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        // 2. Draw Animation Square (Centered 900x900)
        int size = 900;
        int x = (screenWidth - size) / 2;
        int y = (screenHeight - size) / 2;

        if(frames[spriteNum] != null) {
            g.drawImage(frames[spriteNum], x, y, size, size, null);
        }

        // 3. Draw Buttons (Centered 200x60)
        int btnW = 150;
        int btnH = 60;

        startX = (screenWidth - btnW) / 2;
        startY = y + 680;

        exitX = (screenWidth - btnW) / 2;
        exitY = startY + 80;

        if(startButton != null) g.drawImage(startButton, startX, startY, btnW, btnH, null);
        if(exitButton != null) g.drawImage(exitButton, exitX, exitY, btnW, btnH, null);

        // PIX CITY text removed
    }
}