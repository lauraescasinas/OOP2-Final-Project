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

            // Idle states
            frontImage = ImageIO.read(getClass().getResourceAsStream("/Player/FrontProfile.png"));
            backImage  = ImageIO.read(getClass().getResourceAsStream("/Player/BackProfile.png"));
            leftImage  = ImageIO.read(getClass().getResourceAsStream("/Player/LeftSideProfile.png"));
            rightImage = ImageIO.read(getClass().getResourceAsStream("/Player/RightSideProfile.png"));

            // Walking frames
            frontWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/FrontWalk_1.png"));
            frontWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/FrontWalk_2.png"));
            backWalk1  = ImageIO.read(getClass().getResourceAsStream("/Player/BackWalk_1.png"));
            backWalk2  = ImageIO.read(getClass().getResourceAsStream("/Player/BackProfile_2.png"));
            leftWalk1  = ImageIO.read(getClass().getResourceAsStream("/Player/LeftWalk_1.png"));
            leftWalk2  = ImageIO.read(getClass().getResourceAsStream("/Player/LeftWalk_2.png"));
            rightWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/RightWalk_1.png"));
            rightWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/RightWalk_2.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){

        // ── End screen: Again button is now handled entirely by GamePanel.fullReset()
        //    Player just freezes here while currentQuest == 6 ─────────────────────
        if (gp.currentQuest == 6) {
            gp.mouseH.leftClicked = false;
            return;
        }

        // ── Dialogue: advance / skip typewriter ───────────────────────────────
        if (gp.isDialogueActive) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                if (mouseHitbox.intersects(gp.dialogueNextHitbox)) {
                    if (gp.dialogueCharIndex < gp.fullDialogue.length()) {
                        // Instantly finish typing
                        gp.currentDialogue = gp.fullDialogue;
                        gp.dialogueCharIndex = gp.fullDialogue.length();
                    } else {
                        gp.currentDialogueListIndex++;
                        if (gp.currentDialogueListIndex < gp.currentDialogueArray.length) {
                            gp.fullDialogue = gp.currentDialogueArray[gp.currentDialogueListIndex];
                            gp.currentDialogue = "";
                            gp.dialogueCharIndex = 0;
                        } else {
                            gp.isDialogueActive = false;

                            // Only advance house-event states while actually inside the house.
                            // This prevents DemonAppears.wav (and related SFX) from firing if
                            // a dialogue somehow ends while the player is on any other map.
                            if (gp.currentMap == gp.MAP_HOUSE) {
                                if      (gp.houseEventState == 1) gp.houseEventState = 2;
                                else if (gp.houseEventState == 4) gp.houseEventState = 5;
                                else if (gp.houseEventState == 7) gp.houseEventState = 8;
                            }
                        }
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return; // freeze player during dialogue
        }


        if (gp.passwordUIOpen || gp.clue1_Open || gp.clue2_Open || gp.clue3_Open || gp.clue0_Open || gp.statue_Open || gp.introPuzzleOpen) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                // back button closes all UIs
                if (mouseHitbox.intersects(gp.backButtonHitbox)) {
                    gp.passwordUIOpen = false;
                    gp.clue1_Open = false;
                    gp.clue2_Open = false;
                    gp.clue3_Open = false;
                    gp.clue0_Open = false;
                    gp.statue_Open = false;
                    gp.introPuzzleOpen = false;
                    gp.introPuzzlePage = 1;
                }
                else if (gp.statue_Open && gp.chronosWatchUnlocked == false) {
                    // Rotate clockwise on click (wraps 0-3)
                    if (mouseHitbox.intersects(gp.uiStatue1Hitbox)) {
                        gp.statue1State = (gp.statue1State + 1) % 4;
                        gp.soundManager.playSFX("/Music/StatueMove.wav");
                    } else if (mouseHitbox.intersects(gp.uiStatue2Hitbox)) {
                        gp.statue2State = (gp.statue2State + 1) % 4;
                        gp.soundManager.playSFX("/Music/StatueMove.wav");
                    } else if (mouseHitbox.intersects(gp.uiStatue3Hitbox)) {
                        gp.statue3State = (gp.statue3State + 1) % 4;
                        gp.soundManager.playSFX("/Music/StatueMove.wav");
                    }

                    // Winning condition: Right (3), Back-Left (1), Left (0)
                    if (gp.statue1State == 3 && gp.statue2State == 1 && gp.statue3State == 0) {
                        if (gp.chronosWatchUnlocked == false) {
                            gp.chronosWatchUnlocked = true;
                            gp.statue_Open = false;
                            if (gp.currentQuest == 3) gp.currentQuest = 4;
                        }
                        System.out.println("Success! Chronos Watch Unlocked.");
                    }
                }
                // submit button only works if password UI is open
                else if (gp.passwordUIOpen && mouseHitbox.intersects(gp.submitButtonHitbox)) {
                    if (keyH.currentInput.equals("Password123")) {
                        gp.locketUnlocked = true;
                        gp.passwordUIOpen = false;
                        if (gp.currentQuest == 4) gp.currentQuest = 5;
                        System.out.println("Success! Locket Unlocked.");
                    } else {
                        System.out.println("Access Denied. Wrong Password.");
                        keyH.currentInput = "";
                    }
                }
                else if (gp.introPuzzleOpen) {
                    if (gp.introPuzzlePage == 1) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.introPuzzlePage = 2;
                        }
                        else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            gp.introAns1 = true;
                            System.out.println("Desc 1: Correct! (Jar of Eyes)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 1: Incorrect.");
                        }
                        else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.introAns2 = true;
                            System.out.println("Desc 2: Correct! (Black Baccara Roses)");
                        } else if (mouseHitbox.intersects(gp.r2c1Hitbox) || mouseHitbox.intersects(gp.r2c2Hitbox)) {
                            System.out.println("Desc 2: Incorrect.");
                        }
                    }
                    else if (gp.introPuzzlePage == 2) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.introPuzzlePage = 3;
                        } else if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.introPuzzlePage = 1;
                        }
                        else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            gp.introAns3 = true;
                            System.out.println("Desc 3: Correct! (Chronos Watch)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 3: Incorrect.");
                        }
                        else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.introAns4 = true;
                            System.out.println("Desc 4: Correct! (Memento Locket)");
                        } else if (mouseHitbox.intersects(gp.r2c1Hitbox) || mouseHitbox.intersects(gp.r2c2Hitbox)) {
                            System.out.println("Desc 4: Incorrect.");
                        }
                    }
                    else if (gp.introPuzzlePage == 3) {
                        if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.introPuzzlePage = 2;
                        }
                    }

                    if (gp.introAns1 && gp.introAns2 && gp.introAns3 && gp.introAns4 && gp.currentQuest == 0) {
                        gp.currentQuest = 1;
                        gp.introPuzzleOpen = false;
                        System.out.println("Intro Complete! You may now leave the house.");
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return;
        }

        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed == true) {
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed == true) {
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed == true) {
                direction = "right";
                x += speed;
            }
            spriteCounter++;
            if (spriteCounter > 25) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        Rectangle playerHitbox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        if (gp.currentMap == gp.MAP_HOUSE) {
            if (gp.currentQuest == 5 && gp.houseEventState < 7) {
                // ── Ending event trigger ──────────────────────────────────────
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.houseEventState = 7;
                    gp.demonVisible = true;
                    // Demon2 music: plays during the final confrontation
                    gp.soundManager.playBGM("/Music/Demon2.wav");
                    gp.soundManager.playSFX("/Music/DemonAppears.wav");
                    gp.startDialogue(gp.houseDialogue3);
                }
            } else if (gp.houseEventState == 0) {
                // ── Intro event trigger ───────────────────────────────────────
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.houseEventState = 1;
                    gp.startDialogue(gp.houseDialogue1);
                }
            }
        }

        if (gp.currentMap == gp.MAP_ROOM) {
            if (playerHitbox.intersects(gp.bedroomDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE;
                x = gp.outsideBedroomDoorHitbox.x + gp.outsideBedroomDoorHitbox.width + 10;
                y = gp.outsideBedroomDoorHitbox.y + (gp.outsideBedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "right";
            }
        }

        // exit house
        else if (gp.currentMap == gp.MAP_HOUSE) {
            if (playerHitbox.intersects(gp.houseDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_STREET;
                    x = gp.streetHouseDoorHitbox.x + (gp.streetHouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.streetHouseDoorHitbox.y + gp.streetHouseDoorHitbox.height + 10;
                    direction = "down";
                } else {
                    System.out.println("The door is locked. I must finish the list first.");
                    y -= speed * 2;
                }
            } else if (playerHitbox.intersects(gp.outsideBedroomDoorHitbox)) {
                    gp.currentMap = gp.MAP_ROOM;
                    x = gp.bedroomDoorHitbox.x - gp.tileSize - 10;
                    y = gp.bedroomDoorHitbox.y + (gp.bedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
                    direction = "left";
                }

        // exit workshop
        } else if (gp.currentMap == gp.MAP_WORKSHOP){
            if (playerHitbox.intersects(gp.workshopDoorHitbox)){
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetWorkshopDoorHitbox.x + gp.streetWorkshopDoorHitbox.width + 10;
                y = gp.streetWorkshopDoorHitbox.y + (gp.streetWorkshopDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "left";
            }

        // exit greenhouse
        } else if (gp.currentMap == gp.MAP_GREENHOUSE){
            if (playerHitbox.intersects(gp.greenhouseDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetGreenhouseDoorHitbox.x - gp.tileSize - 10;
                y = gp.streetGreenhouseDoorHitbox.y + (gp.streetGreenhouseDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "right";
            }

        // exit museum
        } else if (gp.currentMap == gp.MAP_MUSEUM){
            if (playerHitbox.intersects(gp.museumDoorHitbox)){
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetMuseumDoorHitbox.x + (gp.streetMuseumDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.streetMuseumDoorHitbox.y + gp.streetMuseumDoorHitbox.height + 10;
                direction = "down";
            }
        } else if (gp.currentMap == gp.MAP_STREET) {
            // enter house
            if (playerHitbox.intersects(gp.streetHouseDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE;
                x = gp.houseDoorHitbox.x - gp.tileSize - 10;
                y = gp.houseDoorHitbox.y + (gp.houseDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "up";
            }

            // enter museum
            else if (playerHitbox.intersects(gp.streetMuseumDoorHitbox)) {
                if (gp.currentQuest >= 3) {
                    gp.currentMap = gp.MAP_MUSEUM;
                    x = gp.museumDoorHitbox.x + (gp.museumDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.museumDoorHitbox.y - gp.tileSize - 5;
                    direction = "up";
                } else {
                    y += speed * 2;
                }
            }

            // enter workshop
            else if (playerHitbox.intersects(gp.streetWorkshopDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_WORKSHOP;
                    x = gp.workshopDoorHitbox.x + (gp.workshopDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.workshopDoorHitbox.y - gp.tileSize - 10;
                    direction = "up";
                }
            }

            // enter greenhouse
            else if (playerHitbox.intersects(gp.streetGreenhouseDoorHitbox)) {
                if (gp.currentQuest >= 2) {
                    gp.currentMap = gp.MAP_GREENHOUSE;
                    x = gp.greenhouseDoorHitbox.x + (gp.greenhouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.greenhouseDoorHitbox.y - gp.tileSize - 10;
                    direction = "up";
                } else {
                    y -= speed * 2;
                }
            }
        }

        // screen boundary clamps
        if (x < 0) x = 0;
        if (y < 0) y = 0;

        if (x > gp.screenWidth - gp.tileSize){
            x = gp.screenWidth - gp.tileSize;
        }

        if (y > gp.screenHeight - gp.tileSize && gp.currentMap == gp.MAP_STREET){
            y = gp.screenHeight - gp.tileSize;
        }

        if (gp.mouseH.leftClicked){
            Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

            if (gp.currentMap == gp.MAP_HOUSE) {
                if (mouseHitbox.intersects(gp.debugIntroHitbox)) {
                    gp.introPuzzleOpen = true;
                    gp.introPuzzlePage = 1;
                }
            } else if(gp.currentMap == gp.MAP_GREENHOUSE){
                for (int i = 0; i < gp.obj.length; i++){
                    if (gp.obj[i] != null){
                        if (mouseHitbox.intersects(gp.obj[i].hitbox)){
                            gp.obj[i] = null;
                            blackRosesCollected++;
                            gp.soundManager.playSFX("/Music/Rose.wav");
                            if (blackRosesCollected >= 5 && gp.currentQuest == 2) {
                                gp.currentQuest = 3;
                            }
                        }
                    }
                }
            } else if (gp.currentMap == gp.MAP_WORKSHOP){
                for (int i = 0; i < gp.obj.length; i++){
                    if (gp.obj[i] != null && gp.obj[i].name.equals("Glass Eye")){
                        if (mouseHitbox.intersects(gp.obj[i].hitbox)){
                            gp.obj[i] = null;
                            glassEyesCollected++;
                            // Glass eye pickup SFX
                            gp.soundManager.playSFX("/Music/GlassEye.wav");
                            if (glassEyesCollected >= 5 && gp.currentQuest == 1) {
                                gp.currentQuest = 2;
                            }
                        }
                    }
                }
            } else if (gp.currentMap == gp.MAP_MUSEUM){
                // ── Glass case: open password UI (SFX handled by GamePanel) ─
                if (gp.locketUnlocked == false) {
                    if (mouseHitbox.intersects(gp.glassCaseHitbox)){
                        gp.passwordUIOpen = true;
                        keyH.currentInput = "";
                    }
                }
                // ── Clue papers: open with paper-rustle SFX ──────────────────
                if (mouseHitbox.intersects(gp.clue1Hitbox)) {
                    gp.clue1_Open = true;
                    gp.soundManager.playSFX("/Music/CluePaper.wav");
                } else if (mouseHitbox.intersects(gp.clue2Hitbox)) {
                    gp.clue2_Open = true;
                    gp.soundManager.playSFX("/Music/CluePaper.wav");
                } else if (mouseHitbox.intersects(gp.clue3Hitbox)) {
                    gp.clue3_Open = true;
                    gp.soundManager.playSFX("/Music/CluePaper.wav");
                } else if (mouseHitbox.intersects(gp.clue0Hitbox)) {
                    gp.clue0_Open = true;
                    gp.soundManager.playSFX("/Music/CluePaper.wav");
                }
                // ── Statues ───────────────────────────────────────────────────
                if (gp.chronosWatchUnlocked == false) {
                    if (mouseHitbox.intersects(gp.mapStatue1Hitbox) ||
                            mouseHitbox.intersects(gp.mapStatue2Hitbox) ||
                            mouseHitbox.intersects(gp.mapStatue3Hitbox)) {
                        gp.statue_Open = true;
                    }
                }
            }
            gp.mouseH.leftClicked = false;
        }
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
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}