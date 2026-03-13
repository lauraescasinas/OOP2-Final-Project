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

        int screenX = camera.screenWidth / 2 - 20;
        int screenY = camera.screenHeight / 2 - 20;

        g.fillRect(screenX, screenY, 40, 40);
    }
}
