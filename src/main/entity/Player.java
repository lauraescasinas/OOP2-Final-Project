package main.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import main.game.Camera;

public class Player {

    public int x = 400;
    public int y = 300;
    public int speed = 4;

    // Profiles (Idle)
    public BufferedImage frontImage, leftImage, rightImage, backImage, currentImage;

    // Walk Frames
    public BufferedImage frontWalk1, frontWalk2, backWalk1, backWalk2;
    public BufferedImage leftWalk1, leftWalk2, rightWalk1, rightWalk2;

    // Animation Logic
    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Player() {
        try {
            // Loading Profiles
            frontImage = ImageIO.read(getClass().getResourceAsStream("/assets/player/FrontProfile.png"));
            backImage = ImageIO.read(getClass().getResourceAsStream("/assets/player/BackProfile.png"));
            leftImage = ImageIO.read(getClass().getResourceAsStream("/assets/player/LeftSideProfile.png"));
            rightImage = ImageIO.read(getClass().getResourceAsStream("/assets/player/RightSideProfile.png"));

            // Loading Walking Frames (Matching your file names)
            frontWalk1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/FrontWalk_1.png"));
            frontWalk2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/FrontWalk_2.png"));
            backWalk1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/BackWalk_1.png"));
            backWalk2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/BackProfile_2.png")); // Using your _2 file
            leftWalk1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/LeftWalk_1.png"));
            leftWalk2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/LeftWalk_2.png"));
            rightWalk1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/RightWalk_1.png"));
            rightWalk2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/RightWalk_2.png"));

            currentImage = frontImage;

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // This handles the timing of the steps
    public void updateAnimation() {
        spriteCounter++;
        if(spriteCounter > 12) { // Change 12 to a higher number to walk slower
            if(spriteNum == 1) spriteNum = 2;
            else spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics g) {
        // Keeps your original size
        g.drawImage(currentImage, x, y, 210, 250, null);
    }

    public void draw(Graphics g, Camera camera) {
        int screenX = camera.screenWidth / 2 - 20;
        int screenY = camera.screenHeight / 2 - 20;
        g.drawImage(currentImage, screenX, screenY, 64, 64, null);
    }
}