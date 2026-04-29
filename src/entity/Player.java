package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_2.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){

        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                System.out.println("up pressed");
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed == true) {
                System.out.println("down pressed");
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed == true) {
                System.out.println("left pressed");
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed == true) {
                System.out.println("right pressed");
                direction = "right";
                x += speed;
            }
            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        Rectangle playerHitbox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        //  exit house
        if (gp.currentMap == gp.MAP_HOUSE) {
            // check if player hitbox touches the house door hitbox
            if (playerHitbox.intersects(gp.houseDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET; // Change Map

                // teleport player to stand right below the street (outside) house door
                x = gp.streetHouseDoorHitbox.x;
                y = gp.streetHouseDoorHitbox.y + gp.streetHouseDoorHitbox.height + 5;
            }
        // exit workshop
        } else if (gp.currentMap == gp.MAP_WORKSHOP){
            if (playerHitbox.intersects(gp.workshopDoorHitbox)){
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetWorkshopDoorHitbox.x;
                y = gp.streetWorkshopDoorHitbox.y - gp.tileSize - 20;
            }
        // exit greenhouse
        } else if (gp.currentMap == gp.MAP_GREENHOUSE){
            if (playerHitbox.intersects(gp.greenhouseDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetGreenhouseDoorHitbox.x;
                y = gp.streetGreenhouseDoorHitbox.y - gp.tileSize - 20;
            }
        // exit museum
        } else if (gp.currentMap == gp.MAP_MUSEUM){
            if (playerHitbox.intersects(gp.museumDoorHitbox)){
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetMuseumDoorHitbox.x;
                y = gp.streetMuseumDoorHitbox.y + gp.streetMuseumDoorHitbox.height + 5;
            }
        } else if (gp.currentMap == gp.MAP_STREET) {

            // enter house
            if (playerHitbox.intersects(gp.streetHouseDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE;
                x = gp.houseDoorHitbox.x + (gp.houseDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.houseDoorHitbox.y - gp.tileSize - 5;
            }
            // enter museum
            else if (playerHitbox.intersects(gp.streetMuseumDoorHitbox)) {
                gp.currentMap = gp.MAP_MUSEUM;
                x = gp.museumDoorHitbox.x + (gp.museumDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.museumDoorHitbox.y - gp.tileSize - 5;
            }
            // enter workshop
            else if (playerHitbox.intersects(gp.streetWorkshopDoorHitbox)) {
                gp.currentMap = gp.MAP_WORKSHOP;
                x = gp.workshopDoorHitbox.x + (gp.workshopDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.workshopDoorHitbox.y - gp.tileSize - 20;
            }
            // enter greenhouse
            else if (playerHitbox.intersects(gp.streetGreenhouseDoorHitbox)) {
                gp.currentMap = gp.MAP_GREENHOUSE;
                x = gp.greenhouseDoorHitbox.x + (gp.greenhouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.greenhouseDoorHitbox.y - gp.tileSize - 20;
            }
        }

        // boundaries to prevvent walking off screen
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > gp.screenWidth - gp.tileSize){
            x = gp.screenWidth - gp.tileSize;
        }
        // for interior maps - allow player to walk to the bottom of the screen
        if (y > gp.screenHeight - gp.tileSize && gp.currentMap == gp.MAP_STREET){
            y = gp.screenHeight - gp.tileSize;
        }
    }

    public void draw(Graphics2D g2){
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
