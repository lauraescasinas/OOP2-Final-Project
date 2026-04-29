package main.scene;

import java.awt.Graphics;
import main.entity.Player;
import main.game.KeyHandler;
import main.game.Camera;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class HomeScene {

    Player player;
    KeyHandler keyH;
    Camera camera;
    BufferedImage background;

    public HomeScene(KeyHandler keyH, Camera camera) {
        this.keyH = keyH;
        this.camera = camera;
        player = new Player();

        try {
            // Change "Wall.png" to "HomeScene.png"
            background = ImageIO.read(getClass().getResourceAsStream("/assets/home/HomeScene_1.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // These numbers define the "Box" the player is trapped in
        int minX = 510;   // Left wall
        int maxX = 1350;  // Right wall (1410 minus player width)
        int minY = 90;    // Top wall
        int maxY = 850;   // Bottom wall (990 minus player height)

        if(keyH.up) {
            if(player.y > minY) player.y -= player.speed;
            player.updateAnimation();
            player.currentImage = (player.spriteNum == 1) ? player.backWalk1 : player.backWalk2;
        }
        else if(keyH.down) {
            if(player.y < maxY) player.y += player.speed;
            player.updateAnimation();
            player.currentImage = (player.spriteNum == 1) ? player.frontWalk1 : player.frontWalk2;
        }
        else if(keyH.left) {
            if(player.x > minX) player.x -= player.speed;
            player.updateAnimation();
            player.currentImage = (player.spriteNum == 1) ? player.leftWalk1 : player.leftWalk2;
        }
        else if(keyH.right) {
            if(player.x < maxX) player.x += player.speed;
            player.updateAnimation();
            player.currentImage = (player.spriteNum == 1) ? player.rightWalk1 : player.rightWalk2;
        }

        // ... keep your idle reset logic here ...
    }

    public void draw(Graphics g) {
        // 1. Fill the void with black
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, 1920, 1080);

        // 2. Set square dimensions (e.g., 900x900)
        int size = 900;

        // 3. Center the square on the screen
        int drawX = (1920 - size) / 2;
        int drawY = (1080 - size) / 2;

        if(background != null) {
            // Drawing it with the same width and height makes it a square
            g.drawImage(background, drawX, drawY, size, size, null);
        }

        // 4. Draw player
        player.draw(g);
    }
}