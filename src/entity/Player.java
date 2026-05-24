package entity;

import main.Constants;
import main.GamePanel;
import inputs.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;
    public int blackRosesCollected = 0;
    public int glassEyesCollected = 0;
    public BufferedImage frontImage, leftImage, rightImage, backImage;
    public BufferedImage frontWalk1, frontWalk2, backWalk1, backWalk2;
    public BufferedImage leftWalk1, leftWalk2, rightWalk1, rightWalk2;


    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 280;
        y = 380;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            frontImage = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/FrontProfile.png"));
            backImage = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/BackProfile.png"));
            leftImage = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/LeftSideProfile.png"));
            rightImage = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/RightSideProfile.png"));

            frontWalk1 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/FrontWalk_1.png"));
            frontWalk2 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/FrontWalk_2.png"));
            backWalk1 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/BackWalk_1.png"));
            backWalk2 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/BackProfile_2.png"));
            leftWalk1 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/LeftWalk_1.png"));
            leftWalk2 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/LeftWalk_2.png"));
            rightWalk1 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/RightWalk_1.png"));
            rightWalk2 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/RightWalk_2.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update() {
        // 1. Hand off all mouse interactions to the Manager.
        if (gp.iManager.checkInteractions()) {
            return;
        }

        // 2. Player WASD Movement
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            int nextX = x; int nextY = y;
            if (keyH.upPressed) { direction = "up"; nextY -= speed; }
            else if (keyH.downPressed) { direction = "down"; nextY += speed; }
            else if (keyH.leftPressed) { direction = "left"; nextX -= speed; }
            else if (keyH.rightPressed) { direction = "right"; nextX += speed; }

            Rectangle nextHitbox = new Rectangle(nextX, nextY, Constants.tileSize, Constants.tileSize);
            boolean collisionOn = false;

            if (gp.gsManager.getCurrentMap() == gp.MAP_ROOM) {
                for (Rectangle wall : gp.cManager.mapBarrier.roomWalls) {
                    if (nextHitbox.intersects(wall)) { collisionOn = true; break; }
                }
            }

            if (gp.gsManager.getCurrentMap() == gp.MAP_HOUSE) {
                for (Rectangle wall : gp.cManager.mapBarrier.houseWalls) {
                    if (nextHitbox.intersects(wall)) { collisionOn = true; break; }
                }
                if (gp.gsManager.getCurrentQuest() < 1 && nextHitbox.intersects(gp.cManager.houseDoorHitbox)) {
                    collisionOn = true;
                    System.out.println("The door is locked. I must finish the list first.");
                }
            }

            if (gp.gsManager.getCurrentMap() == gp.MAP_STREET) {
                if (gp.gsManager.getCurrentQuest() < 3 && nextHitbox.intersects(gp.cManager.streetMuseumDoorHitbox)) collisionOn = true;
                if (gp.gsManager.getCurrentQuest() < 2 && nextHitbox.intersects(gp.cManager.streetGreenhouseDoorHitbox)) collisionOn = true;
                if (gp.gsManager.getCurrentQuest() < 1 && nextHitbox.intersects(gp.cManager.streetWorkshopDoorHitbox)) collisionOn = true;
                for (Rectangle wall : gp.cManager.mapBarrier.streetWalls) { if (nextHitbox.intersects(wall)) collisionOn = true; }
            }

            if (gp.gsManager.getCurrentMap() == gp.MAP_WORKSHOP) {
                for (Rectangle wall : gp.cManager.mapBarrier.workshopWalls) {
                    if (nextHitbox.intersects(wall)) { collisionOn = true; break; }
                }
            }

            if (gp.gsManager.getCurrentMap() == gp.MAP_GREENHOUSE) {
                for (Rectangle wall : gp.cManager.mapBarrier.greenhouseWalls) {
                    if (nextHitbox.intersects(wall)) { collisionOn = true; break; }
                }
            }

            if (gp.gsManager.getCurrentMap() == gp.MAP_MUSEUM) {
                for (Rectangle wall : gp.cManager.mapBarrier.museumWalls) {
                    if (nextHitbox.intersects(wall)) { collisionOn = true; break; }
                }
            }

            if (!collisionOn) {
                x = nextX;
                y = nextY;
            }

            spriteCounter++;
            if (spriteCounter > 25) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
                gp.soundManager.playWalkSFX();
            }
        }

        // 3. Invisible Event Triggers
        Rectangle playerHitbox = new Rectangle(x, y, Constants.tileSize, Constants.tileSize);
        if (gp.gsManager.getCurrentMap() == gp.MAP_HOUSE) {
            if (gp.gsManager.getCurrentQuest() == 5 && gp.gsManager.getHouseEventState() < 7) {
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.gsManager.setHouseEventState(7);
                    gp.gsManager.setDemonVisible(true);
                    gp.dlgManager.startDialogue(gp.dlgManager.houseDialogue3);
                }
            } else if (gp.gsManager.getHouseEventState() == 0) {
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.gsManager.setHouseEventState(1);
                    gp.dlgManager.startDialogue(gp.dlgManager.houseDialogue1);
                }
            }
        }

        // 4. Map Door Transitions
        if (gp.gsManager.getCurrentMap() == gp.MAP_ROOM) {
            if (playerHitbox.intersects(gp.cManager.bedroomDoorHitbox)) {
                gp.gsManager.setCurrentMap(gp.MAP_HOUSE);
                direction = "right";
                x = gp.cManager.outsideBedroomDoorHitbox.x + gp.cManager.outsideBedroomDoorHitbox.width + 10;
                y = gp.cManager.outsideBedroomDoorHitbox.y + (gp.cManager.outsideBedroomDoorHitbox.height / 2) - (Constants.tileSize / 2);
            }
        } else if (gp.gsManager.getCurrentMap() == gp.MAP_HOUSE) {
            if (playerHitbox.intersects(gp.cManager.houseDoorHitbox) && gp.gsManager.getCurrentQuest() >= 1) {
                gp.soundManager.stopRoomMusic();
                gp.soundManager.stopFinalMusic();
                gp.soundManager.playOutdoorMusic();
                gp.gsManager.setCurrentMap(gp.MAP_STREET);
                direction = "down";
                x = gp.cManager.streetHouseDoorHitbox.x + (gp.cManager.streetHouseDoorHitbox.width / 2) - (Constants.tileSize / 2);
                y = gp.cManager.streetHouseDoorHitbox.y + gp.cManager.streetHouseDoorHitbox.height + 10;
            } else if (playerHitbox.intersects(gp.cManager.outsideBedroomDoorHitbox)) {
                gp.gsManager.setCurrentMap(gp.MAP_ROOM);
                direction = "left";
                x = gp.cManager.bedroomDoorHitbox.x - Constants.tileSize - 10;
                y = gp.cManager.bedroomDoorHitbox.y + (gp.cManager.bedroomDoorHitbox.height / 2) - (Constants.tileSize / 2);
            }
        } else if (gp.gsManager.getCurrentMap() == gp.MAP_WORKSHOP) {
            if (playerHitbox.intersects(gp.cManager.workshopDoorHitbox)) {
                gp.soundManager.stopWorkshopMusic();
                gp.soundManager.playOutdoorMusic();
                gp.gsManager.setCurrentMap(gp.MAP_STREET);
                direction = "left";
                x = gp.cManager.streetWorkshopDoorHitbox.x + gp.cManager.streetWorkshopDoorHitbox.width + 10;
                y = gp.cManager.streetWorkshopDoorHitbox.y + (gp.cManager.streetWorkshopDoorHitbox.height / 2) - (Constants.tileSize / 2);
            }
        } else if (gp.gsManager.getCurrentMap() == gp.MAP_GREENHOUSE) {
            if (playerHitbox.intersects(gp.cManager.greenhouseDoorHitbox)) {
                gp.soundManager.stopGardenMusic();
                gp.soundManager.playOutdoorMusic();
                gp.gsManager.setCurrentMap(gp.MAP_STREET);
                direction = "right";
                x = gp.cManager.streetGreenhouseDoorHitbox.x - Constants.tileSize - 10;
                y = gp.cManager.streetGreenhouseDoorHitbox.y + (gp.cManager.streetGreenhouseDoorHitbox.height / 2) - (Constants.tileSize / 2);
            }
        } else if (gp.gsManager.getCurrentMap() == gp.MAP_MUSEUM) {
            if (playerHitbox.intersects(gp.cManager.museumDoorHitbox)) {
                gp.soundManager.stopMuseumMusic();
                gp.soundManager.playOutdoorMusic();
                gp.gsManager.setCurrentMap(gp.MAP_STREET);
                direction = "down";
                x = gp.cManager.streetMuseumDoorHitbox.x + (gp.cManager.streetMuseumDoorHitbox.width / 2) - (Constants.tileSize / 2);
                y = gp.cManager.streetMuseumDoorHitbox.y + gp.cManager.streetMuseumDoorHitbox.height + 10;
            }
        } else if (gp.gsManager.getCurrentMap() == gp.MAP_STREET) {
            if (playerHitbox.intersects(gp.cManager.streetHouseDoorHitbox)) {
                gp.soundManager.stopOutdoorMusic();
                // Play Final music if quest 5, otherwise normal room music
                if (gp.gsManager.getCurrentQuest() >= 5) {
                    gp.soundManager.playFinalMusic();
                } else {
                    gp.soundManager.playRoomMusic();
                }
                gp.gsManager.setCurrentMap(gp.MAP_HOUSE);
                direction = "up";
                x = gp.cManager.houseDoorHitbox.x - Constants.tileSize - 10;
                y = gp.cManager.houseDoorHitbox.y + (gp.cManager.houseDoorHitbox.height / 2) - (Constants.tileSize / 2);
            } else if (playerHitbox.intersects(gp.cManager.streetMuseumDoorHitbox) && gp.gsManager.getCurrentQuest() >= 3) {
                gp.soundManager.stopOutdoorMusic();
                gp.soundManager.playMuseumMusic();
                gp.gsManager.setCurrentMap(gp.MAP_MUSEUM);
                direction = "up";
                x = gp.cManager.museumDoorHitbox.x + (gp.cManager.museumDoorHitbox.width / 2) - (Constants.tileSize / 2);
                y = gp.cManager.museumDoorHitbox.y - Constants.tileSize - 5;
            } else if (playerHitbox.intersects(gp.cManager.streetWorkshopDoorHitbox) && gp.gsManager.getCurrentQuest() >= 1) {
                gp.soundManager.stopOutdoorMusic();
                gp.soundManager.playWorkshopMusic();
                gp.gsManager.setCurrentMap(gp.MAP_WORKSHOP);
                direction = "up";
                x = gp.cManager.workshopDoorHitbox.x + (gp.cManager.workshopDoorHitbox.width / 2) - (Constants.tileSize / 2);
                y = gp.cManager.workshopDoorHitbox.y - Constants.tileSize - 10;
            } else if (playerHitbox.intersects(gp.cManager.streetGreenhouseDoorHitbox) && gp.gsManager.getCurrentQuest() >= 2) {
                gp.soundManager.stopOutdoorMusic();
                gp.soundManager.playGardenMusic();
                gp.gsManager.setCurrentMap(gp.MAP_GREENHOUSE);
                direction = "up";
                x = gp.cManager.greenhouseDoorHitbox.x + (gp.cManager.greenhouseDoorHitbox.width / 2) - (Constants.tileSize / 2);
                y = gp.cManager.greenhouseDoorHitbox.y - Constants.tileSize - 10;
            }
        }

        // Boundaries
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > Constants.screenWidth - Constants.tileSize) x = Constants.screenWidth - Constants.tileSize;
        if (y > Constants.screenHeight - Constants.tileSize && gp.gsManager.getCurrentMap() == gp.MAP_STREET) y = Constants.screenHeight - Constants.tileSize;
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        switch(direction){
            case "up":
                if (!isMoving) image = backImage;
                else if (spriteNum == 1) image = backWalk1;
                else image = backWalk2;
                break;
            case "down":
                if (!isMoving) image = frontImage;
                else if (spriteNum == 1) image = frontWalk1;
                else image = frontWalk2;
                break;
            case "left":
                if (!isMoving) image = leftImage;
                else if (spriteNum == 1) image = leftWalk1;
                else image = leftWalk2;
                break;
            case "right":
                if (!isMoving) image = rightImage;
                else if (spriteNum == 1) image = rightWalk1;
                else image = rightWalk2;
                break;
        }

        g2.drawImage(image, x, y, Constants.tileSize + 10, Constants.tileSize + 10, null);
    }
}