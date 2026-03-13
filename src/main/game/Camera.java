package main.game;

public class Camera {

    public int cameraX;
    public int cameraY;
    public int screenWidth;
    public  int screenHeight;

    public Camera(int screenWidth, int screenHeight) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }

    public void update(int playerX, int playerY) {

        cameraX = playerX - screenWidth / 2;
        cameraY = playerY - screenWidth / 2;

    }
}
