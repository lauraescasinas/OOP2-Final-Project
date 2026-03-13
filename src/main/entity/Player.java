package main.entity;

import java.awt.Graphics;
import java.awt.Color;
import main.game.Camera;

public class Player {

    public int x = 400;
    public int y = 300;
    public int speed = 4;

    public Player() {


    }

    public void draw(Graphics g, Camera camera) {

        g.setColor(Color.BLUE);
        g.fillRect(
                x - camera.cameraX,
                y - camera.cameraY,
                40,
                40
        );
    }
}
