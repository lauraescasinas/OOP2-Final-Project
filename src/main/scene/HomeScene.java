package main.scene;

import java.awt.Graphics;

import main.entity.Player;
import main.game.KeyHandler;
import main.game.Camera;

public class HomeScene {

    Player player;
    KeyHandler keyH;
    Camera camera;

    public HomeScene(KeyHandler keyH, Camera camera) {
        this.keyH = keyH;
        this.camera = camera;
        player = new Player();
    }

    public void update() {

        if(keyH.up) player.y -= player.speed;
        if(keyH.down) player.y += player.speed;
        if(keyH.left) player.x -= player.speed;
        if(keyH.right) player.x += player.speed;

        camera.update(player.x, player.y);
    }

    public void draw(Graphics g) {

        // world background
        g.setColor(java.awt.Color.LIGHT_GRAY);
        g.fillRect(-camera.cameraX, -camera.cameraY, 2000, 2000);

        // player
        player.draw(g, camera);

        //just a grid
        for(int i = 0; i < 2000; i += 100) {
            g.drawLine(i - camera.cameraX, -camera.cameraY,
                    i - camera.cameraX, 2000 - camera.cameraY);

            g.drawLine(-camera.cameraX, i - camera.cameraY,
                    2000 - camera.cameraX, i - camera.cameraY);
        }
    }
}
