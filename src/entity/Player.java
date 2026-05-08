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
    public int blackRosesCollected = 0;
    public int glassEyesCollected = 0;

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

        if (gp.passwordUIOpen || gp.clue1_Open || gp.clue2_Open || gp.clue3_Open || gp.statue_Open || gp.introPuzzleOpen) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                // back button closes EVERYTHING
                if (mouseHitbox.intersects(gp.backButtonHitbox)) {
                    gp.passwordUIOpen = false;
                    gp.clue1_Open = false;
                    gp.clue2_Open = false;
                    gp.clue3_Open = false;
                    gp.statue_Open = false;
                    gp.introPuzzleOpen = false;
                    gp.introPuzzlePage = 1;
                }
                else if (gp.statue_Open && gp.chronosWatchUnlocked == false) {

                    // Rotate Clockwise (Add 1, if it hits 4 it wraps back to 0)
                    if (mouseHitbox.intersects(gp.uiStatue1Hitbox)) {
                        gp.statue1State = (gp.statue1State + 1) % 4;
                    } else if (mouseHitbox.intersects(gp.uiStatue2Hitbox)) {
                        gp.statue2State = (gp.statue2State + 1) % 4;
                    } else if (mouseHitbox.intersects(gp.uiStatue3Hitbox)) {
                        gp.statue3State = (gp.statue3State + 1) % 4;
                    }

                    // Check Winning Condition: 3 (Right), 1 (Back-Left), 0 (Left)
                    if (gp.statue1State == 3 && gp.statue2State == 1 && gp.statue3State == 0) {
                        gp.chronosWatchUnlocked = true;
                        gp.statue_Open = false;
                        System.out.println("Success! Chronos Watch Unlocked.");
                    }
                }
                // submit button (green circle) ONLY works if password UI is open
                else if (gp.passwordUIOpen && mouseHitbox.intersects(gp.submitButtonHitbox)) {
                    if (keyH.currentInput.equals("Password123")) {
                        gp.locketUnlocked = true;
                        gp.passwordUIOpen = false;
                        System.out.println("Success! Locket Unlocked.");
                    } else {
                        System.out.println("Access Denied. Wrong Password.");
                    }
                }
                else if (gp.introPuzzleOpen) {
                    if (gp.introPuzzlePage == 1) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.introPuzzlePage = 2; // Go to Page 2
                        }
                        else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            System.out.println("Desc 1: Correct! (Jar of Eyes)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 1: Incorrect.");
                        }
                        else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            System.out.println("Desc 2: Correct! (Black Baccara Roses)");
                        } else if (mouseHitbox.intersects(gp.r2c1Hitbox) || mouseHitbox.intersects(gp.r2c2Hitbox)) {
                            System.out.println("Desc 2: Incorrect.");
                        }
                    }
                    // PAGE 2 CLICKS
                    else if (gp.introPuzzlePage == 2) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.introPuzzlePage = 3; // Go to Page 3
                        } else if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.introPuzzlePage = 1; // Go back to Page 1
                        }
                        // Correct for Desc 3 is Watch (r1c1)
                        else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            System.out.println("Desc 3: Correct! (Chronos Watch)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 3: Incorrect.");
                        }
                        // Correct for Desc 4 is Locket (r2c3)
                        else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            System.out.println("Desc 4: Correct! (Memento Locket)");
                        } else if (mouseHitbox.intersects(gp.r2c1Hitbox) || mouseHitbox.intersects(gp.r2c2Hitbox)) {
                            System.out.println("Desc 4: Incorrect.");
                        }
                    }
                    // PAGE 3 CLICKS
                    else if (gp.introPuzzlePage == 3) {
                        if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.introPuzzlePage = 2; // Go back to Page 2
                        }
                        // Next button does nothing here right now!
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return;
        }


        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
//                System.out.println("up pressed");
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed == true) {
//                System.out.println("down pressed");
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed == true) {
//                System.out.println("left pressed");
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed == true) {
//                System.out.println("right pressed");
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

        if (gp.mouseH.leftClicked){
            Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);


            if (gp.currentMap == gp.MAP_HOUSE) {
                if (mouseHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.introPuzzleOpen = true;
                }
            } else if(gp.currentMap == gp.MAP_GREENHOUSE){
                for (int i = 0; i < gp.obj.length; i++){
                    if (gp.obj[i] != null){
                        if (mouseHitbox.intersects(gp.obj[i].hitbox)){
                            gp.obj[i] = null; // remove clicked rose from greenhouse map
                            blackRosesCollected++; // add inventory
                            //debug print
                            System.out.println("black roses collected: " + blackRosesCollected + "/5");
                        }
                    }
                }
            } else if (gp.currentMap == gp.MAP_WORKSHOP){
                for (int i = 0; i < gp.obj.length; i++){
                    if (gp.obj[i] != null && gp.obj[i].name.equals("Glass Eye")){
                        if (mouseHitbox.intersects(gp.obj[i].hitbox)){
                            gp.obj[i] = null;
                            glassEyesCollected++;
                            //debug print
                            System.out.println("glass eyes collected: " + glassEyesCollected + "/5");
                        }
                    }
                }
            } else if (gp.currentMap == gp.MAP_MUSEUM){
                if (gp.locketUnlocked == false) {
                    // If we click the glass case, open the UI and clear the text
                    if (mouseHitbox.intersects(gp.glassCaseHitbox)){
                        gp.passwordUIOpen = true;
                        keyH.currentInput = "";
                    }
                }
                if (mouseHitbox.intersects(gp.clue1Hitbox)) {
                    gp.clue1_Open = true;
                } else if (mouseHitbox.intersects(gp.clue2Hitbox)) {
                    gp.clue2_Open = true;
                } else if (mouseHitbox.intersects(gp.clue3Hitbox)) {
                    gp.clue3_Open = true;
                }
                if (gp.chronosWatchUnlocked == false) {
                    if (mouseHitbox.intersects(gp.mapStatue1Hitbox) ||
                            mouseHitbox.intersects(gp.mapStatue2Hitbox) ||
                            mouseHitbox.intersects(gp.mapStatue3Hitbox)) {

                        gp.statue_Open = true;
                    }
                }


            }
            // reset click every collect
            gp.mouseH.leftClicked = false;
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
