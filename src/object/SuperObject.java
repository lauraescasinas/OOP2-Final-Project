package object;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public int x, y;
    public int width = 0;
    public int height = 0;

    public Rectangle hitbox = new Rectangle(0, 0, 48, 48);
    public void draw(Graphics2D g2, GamePanel gp){
        int drawWidth;
        if (width > 0) {
            drawWidth = width;
        } else {
            drawWidth = gp.tileSize;
        }

        int drawHeight;
        if (height > 0) {
            drawHeight = height;
        } else {
            drawHeight = gp.tileSize;
        }
        g2.drawImage(image, x, y, drawWidth, drawHeight, null);
    }
}
