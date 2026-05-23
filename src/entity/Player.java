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
//        x = 500;
//        y = 500;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            // Loading Profiles (Idle States)
            frontImage = ImageIO.read(getClass().getResourceAsStream("/Player/FrontProfile.png"));
            backImage = ImageIO.read(getClass().getResourceAsStream("/Player/BackProfile.png"));
            leftImage = ImageIO.read(getClass().getResourceAsStream("/Player/LeftSideProfile.png"));
            rightImage = ImageIO.read(getClass().getResourceAsStream("/Player/RightSideProfile.png"));

            // Loading Walking Frames
            frontWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/FrontWalk_1.png"));
            frontWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/FrontWalk_2.png"));
            backWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/BackWalk_1.png"));
            backWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/BackProfile_2.png"));
            leftWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/LeftWalk_1.png"));
            leftWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/LeftWalk_2.png"));
            rightWalk1 = ImageIO.read(getClass().getResourceAsStream("/Player/RightWalk_1.png"));
            rightWalk2 = ImageIO.read(getClass().getResourceAsStream("/Player/RightWalk_2.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update() {

        if (gp.currentMap != gp.MAP_MAIN_MENU && !gp.playingAcceptCutscene && !gp.playingRejectCutscene && !gp.showTheEndText) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                if (gp.isSettingsOpen) {
                    // Inside the Settings Menu
                    if (mouseHitbox.intersects(gp.resumeHitbox)) {
                        gp.isSettingsOpen = false; // Close settings
                    } else if (mouseHitbox.intersects(gp.exitMenuHitbox)) {
                        // --- TOTAL RESET TO MAIN MENU ---
                        gp.isSettingsOpen = false;
                        gp.currentQuest = 0;
                        gp.introAns1 = false; gp.introAns2 = false; gp.introAns3 = false; gp.introAns4 = false;
                        blackRosesCollected = 0; glassEyesCollected = 0;
                        gp.statue1State = 0; gp.statue2State = 3; gp.statue3State = 2;
                        gp.chronosWatchUnlocked = false; gp.locketUnlocked = false;
                        gp.houseEventState = 0;
                        gp.demonVisible = false;
                        gp.introDialogueTriggered = false;
                        gp.isDialogueActive = false;
                        gp.introPuzzleOpen = false;
                        gp.passwordUIOpen = false;
                        gp.statue_Open = false;
                        gp.showEndingGibberish = false;
                        gp.showChoiceScreen = false;

                        gp.setupGame(); // Respawn items
                        setDefaultValues(); // Teleport back to room defaults
                        gp.currentMap = gp.MAP_MAIN_MENU; // Go to title screen
                    }
                    gp.mouseH.leftClicked = false;
                    return; // Freeze the game while settings is open!

                } else if (mouseHitbox.intersects(gp.settingsBtnHitbox)) {
                    // Open Settings Menu
                    gp.isSettingsOpen = true;
                    gp.mouseH.leftClicked = false;
                    return;
                }
            }

            // If settings is open, stop the rest of the update method from running!
            if (gp.isSettingsOpen) return;
        }

        if (gp.currentQuest == 6) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);
                // Restart the Game!
                if (mouseHitbox.intersects(gp.againBtnHitbox)) {
                    gp.currentQuest = 0;
                    gp.introAns1 = false;
                    gp.introAns2 = false;
                    gp.introAns3 = false;
                    gp.introAns4 = false;
                    blackRosesCollected = 0;
                    glassEyesCollected = 0;
                    gp.statue1State = 0;
                    gp.statue2State = 3;
                    gp.statue3State = 2;
                    gp.chronosWatchUnlocked = false;
                    gp.locketUnlocked = false;

                    // --- UPDATED: Reset Event States & Teleport to Room ---
                    gp.houseEventState = 0;
                    gp.demonVisible = false;
                    gp.introDialogueTriggered = false; // Triggers room dialogue again

                    gp.setupGame(); // Respawns items
                    setDefaultValues(); // Teleport to start
                    gp.currentMap = gp.MAP_ROOM; // Back to the bedroom!
                }
                gp.mouseH.leftClicked = false;
            }
            return; // Freeze player
        }

        if (gp.isDialogueActive) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                if (mouseHitbox.intersects(gp.dialogueNextHitbox)) {
                    if (gp.dialogueCharIndex < gp.fullDialogue.length()) {
                        // Instantly finish typing
                        gp.currentDialogue = gp.fullDialogue;
                        gp.dialogueCharIndex = gp.fullDialogue.length();
                    } else {
                        // --- NEW: Intercept specific lines in the Final Dialogue ---
                        if (gp.currentDialogueArray == gp.houseDialogue3 && gp.currentDialogueListIndex == 6) {
                            gp.isDialogueActive = false;
                            gp.houseEventState = 10; // Trigger 1s delay before Gibberish
                        } else if (gp.currentDialogueArray == gp.houseDialogue3 && gp.currentDialogueListIndex == 9) {
                            gp.isDialogueActive = false;
                            gp.houseEventState = 13; // Trigger 1s delay before Choice Screen
                        } else {
                            // Normal progression
                            gp.currentDialogueListIndex++;
                            if (gp.currentDialogueListIndex < gp.currentDialogueArray.length) {
                                gp.fullDialogue = gp.currentDialogueArray[gp.currentDialogueListIndex];
                                gp.currentDialogue = "";
                                gp.dialogueCharIndex = 0;
                            } else {
                                gp.isDialogueActive = false;
                                if (gp.houseEventState == 1) gp.houseEventState = 2;
                                else if (gp.houseEventState == 4) gp.houseEventState = 5;
                            }
                        }
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return; // Freeze player
        }

        if (gp.showEndingGibberish) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);
                // When Back is clicked, close screen and resume dialogue at line 7
                if (mouseHitbox.intersects(gp.backButtonHitbox)) {
                    gp.showEndingGibberish = false;
                    gp.houseEventState = 12;
                    gp.isDialogueActive = true;
                    gp.currentDialogueListIndex = 7;
                    gp.fullDialogue = gp.houseDialogue3[7];
                    gp.currentDialogue = "";
                    gp.dialogueCharIndex = 0;
                }
                gp.mouseH.leftClicked = false;
            }
            return;
        }

        // --- NEW: Choice Screen Click Logic ---
        if (gp.showChoiceScreen) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                if (mouseHitbox.intersects(gp.acceptHitbox)) {
                    gp.showChoiceScreen = false;
                    gp.playingAcceptCutscene = true;
                    gp.cutsceneFrameIndex = 0;
                    gp.cutsceneTimer = 0;
                } else if (mouseHitbox.intersects(gp.rejectHitbox) && gp.rejectHoverCount >= 5) {
                    // Only allows click if it has jumped 3 times!
                    gp.showChoiceScreen = false;
                    gp.playingRejectCutscene = true;
                    gp.cutsceneFrameIndex = 0;
                    gp.cutsceneTimer = 0;
                }
                gp.mouseH.leftClicked = false;
            }
            return; // Freeze player
        }

        // --- NEW: Final Menu Button Reset Logic ---
        if (gp.playingAcceptCutscene || gp.playingRejectCutscene) {
            if (gp.showMenuButton && gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);
                if (mouseHitbox.intersects(gp.menuBtnHitbox)) {
                    // Turn off cutscene variables
                    gp.playingAcceptCutscene = false;
                    gp.playingRejectCutscene = false;
                    gp.showTheEndText = false;
                    gp.showMenuButton = false;
                    gp.currentTheEndText = "";
                    gp.theEndCharIndex = 0;

                    // Reset the jumping button
                    gp.rejectHoverCount = 0;
                    gp.rejectHitbox.x = 450;
                    gp.rejectHitbox.y = 300;

                    // Reset core gameplay variables
                    gp.currentQuest = 0;
                    gp.introAns1 = false; gp.introAns2 = false; gp.introAns3 = false; gp.introAns4 = false;
                    blackRosesCollected = 0; glassEyesCollected = 0;
                    gp.statue1State = 0; gp.statue2State = 3; gp.statue3State = 2;
                    gp.chronosWatchUnlocked = false; gp.locketUnlocked = false;
                    gp.houseEventState = 0;
                    gp.demonVisible = false;
                    gp.introDialogueTriggered = false;

                    gp.setupGame();
                    setDefaultValues();
                    gp.currentMap = gp.MAP_MAIN_MENU; // Teleport to the Main Menu!
                }
                gp.mouseH.leftClicked = false;
            }
            return; // Freeze player
        }


        if (gp.showChoiceScreen) {
            // Prepared to intercept Accept/Reject clicks later!
            return; // Freeze player
        }

        if (gp.passwordUIOpen || gp.clue1_Open || gp.clue2_Open || gp.clue3_Open || gp.clue0_Open || gp.statue_Open || gp.introPuzzleOpen) {
            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

                // back button closes EVERYTHING
                if (mouseHitbox.intersects(gp.backButtonHitbox)) {
                    gp.passwordUIOpen = false;
                    gp.clue1_Open = false;
                    gp.clue2_Open = false;
                    gp.clue3_Open = false;
                    gp.clue0_Open = false;
                    gp.statue_Open = false;
                    gp.introPuzzleOpen = false;
                    gp.introPuzzlePage = 1;
                } else if (gp.statue_Open && gp.chronosWatchUnlocked == false) {

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
                        if (gp.chronosWatchUnlocked == false) {
                            gp.chronosWatchUnlocked = true;
                            gp.statue_Open = false;
                            if (gp.currentQuest == 3) gp.currentQuest = 4; // Advance to Quest 4
                        }
                        System.out.println("Success! Chronos Watch Unlocked.");
                    }
                }
                // submit button (green circle) ONLY works if password UI is open
                else if (gp.passwordUIOpen && mouseHitbox.intersects(gp.submitButtonHitbox)) {
                    if (keyH.currentInput.equals("1984")) {
                        gp.locketUnlocked = true;
                        gp.passwordUIOpen = false;
                        if (gp.currentQuest == 4) gp.currentQuest = 5;
                        System.out.println("Success! Locket Unlocked.");
                    } else {
                        System.out.println("Access Denied. Wrong Password.");
                        keyH.currentInput = "";
                    }
                } else if (gp.introPuzzleOpen) {
                    if (gp.introPuzzlePage == 1) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.introPuzzlePage = 2; // Go to Page 2
                        } else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            gp.introAns1 = true;
                            System.out.println("Desc 1: Correct! (Jar of Eyes)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 1: Incorrect.");
                        } else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.introAns2 = true;
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
                            gp.introAns3 = true;
                            System.out.println("Desc 3: Correct! (Chronos Watch)");
                        } else if (mouseHitbox.intersects(gp.r1c2Hitbox) || mouseHitbox.intersects(gp.r1c3Hitbox)) {
                            System.out.println("Desc 3: Incorrect.");
                        }
                        // Correct for Desc 4 is Locket (r2c3)
                        else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.introAns4 = true;
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

                    }

                    if (gp.introAns1 && gp.introAns2 && gp.introAns3 && gp.introAns4 && gp.currentQuest == 0) {
                        gp.currentQuest = 1; // Unlock Quest 1!
                        gp.introPuzzleOpen = false; // Close UI automatically
                        System.out.println("Intro Complete! You may now leave the house.");
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return;
        }


        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            int nextX = x; int nextY = y;
            if (keyH.upPressed == true) { direction = "up"; nextY -= speed; }
            else if (keyH.downPressed == true) { direction = "down"; nextY += speed; }
            else if (keyH.leftPressed == true) { direction = "left"; nextX -= speed; }
            else if (keyH.rightPressed == true) { direction = "right"; nextX += speed; }

            Rectangle nextHitbox = new Rectangle(nextX, nextY, gp.tileSize, gp.tileSize);

            // --- NEW: Using the CollisionManager! ---
            boolean collisionOn = gp.cManager.checkWallCollision(nextHitbox, gp.currentMap);
            if (!collisionOn) {
                collisionOn = gp.cManager.checkLockedDoors(nextHitbox, gp.currentMap, gp.currentQuest);
            }

            if (collisionOn == false) { x = nextX; y = nextY; }

            spriteCounter++;
            if (spriteCounter > 25) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        Rectangle playerHitbox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        if (gp.currentMap == gp.MAP_HOUSE) {
            if (gp.currentQuest == 5 && gp.houseEventState < 7) {
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.houseEventState = 7; gp.demonVisible = true; gp.startDialogue(gp.houseDialogue3);
                }
            } else if (gp.houseEventState == 0) {
                if (playerHitbox.intersects(gp.tempBtnHitbox)) {
                    gp.houseEventState = 1; gp.startDialogue(gp.houseDialogue1);
                }
            }
        }

        // ----------------------------------------------------
        // --- MAP DOOR TRANSITIONS (Using cManager) ---
        // ----------------------------------------------------
        if (gp.currentMap == gp.MAP_ROOM) {
            if (playerHitbox.intersects(gp.cManager.bedroomDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE; direction = "right";
                x = gp.cManager.outsideBedroomDoorHitbox.x + gp.cManager.outsideBedroomDoorHitbox.width + 10;
                y = gp.cManager.outsideBedroomDoorHitbox.y + (gp.cManager.outsideBedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
            }
        } else if (gp.currentMap == gp.MAP_HOUSE) {
            if (playerHitbox.intersects(gp.cManager.houseDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_STREET; direction = "down";
                    x = gp.cManager.streetHouseDoorHitbox.x + (gp.cManager.streetHouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.cManager.streetHouseDoorHitbox.y + gp.cManager.streetHouseDoorHitbox.height + 10;
                }
            } else if (playerHitbox.intersects(gp.cManager.outsideBedroomDoorHitbox)) {
                gp.currentMap = gp.MAP_ROOM; direction = "left";
                x = gp.cManager.bedroomDoorHitbox.x - gp.tileSize - 10;
                y = gp.cManager.bedroomDoorHitbox.y + (gp.cManager.bedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
            }
        } else if (gp.currentMap == gp.MAP_WORKSHOP) {
            if (playerHitbox.intersects(gp.cManager.workshopDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET; direction = "left";
                x = gp.cManager.streetWorkshopDoorHitbox.x + gp.cManager.streetWorkshopDoorHitbox.width + 10;
                y = gp.cManager.streetWorkshopDoorHitbox.y + (gp.cManager.streetWorkshopDoorHitbox.height / 2) - (gp.tileSize / 2);
            }
        } else if (gp.currentMap == gp.MAP_GREENHOUSE) {
            if (playerHitbox.intersects(gp.cManager.greenhouseDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET; direction = "right";
                x = gp.cManager.streetGreenhouseDoorHitbox.x - gp.tileSize - 10;
                y = gp.cManager.streetGreenhouseDoorHitbox.y + (gp.cManager.streetGreenhouseDoorHitbox.height / 2) - (gp.tileSize / 2);
            }
        } else if (gp.currentMap == gp.MAP_MUSEUM) {
            if (playerHitbox.intersects(gp.cManager.museumDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET; direction = "down";
                x = gp.cManager.streetMuseumDoorHitbox.x + (gp.cManager.streetMuseumDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.cManager.streetMuseumDoorHitbox.y + gp.cManager.streetMuseumDoorHitbox.height + 10;
            }
        } else if (gp.currentMap == gp.MAP_STREET) {
            if (playerHitbox.intersects(gp.cManager.streetHouseDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE; direction = "up";
                x = gp.cManager.houseDoorHitbox.x - gp.tileSize - 10;
                y = gp.cManager.houseDoorHitbox.y + (gp.cManager.houseDoorHitbox.height / 2) - (gp.tileSize / 2);
            } else if (playerHitbox.intersects(gp.cManager.streetMuseumDoorHitbox)) {
                if (gp.currentQuest >= 3) {
                    gp.currentMap = gp.MAP_MUSEUM; direction = "up";
                    x = gp.cManager.museumDoorHitbox.x + (gp.cManager.museumDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.cManager.museumDoorHitbox.y - gp.tileSize - 5;
                }
            } else if (playerHitbox.intersects(gp.cManager.streetWorkshopDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_WORKSHOP; direction = "up";
                    x = gp.cManager.workshopDoorHitbox.x + (gp.cManager.workshopDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.cManager.workshopDoorHitbox.y - gp.tileSize - 10;
                }
            } else if (playerHitbox.intersects(gp.cManager.streetGreenhouseDoorHitbox)) {
                if (gp.currentQuest >= 2) {
                    gp.currentMap = gp.MAP_GREENHOUSE; direction = "up";
                    x = gp.cManager.greenhouseDoorHitbox.x + (gp.cManager.greenhouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.cManager.greenhouseDoorHitbox.y - gp.tileSize - 10;
                }
            }
        }

        // ----------------------------------------------------
        // --- MAP DOOR TRANSITIONS ---
        // ----------------------------------------------------

        if (gp.currentMap == gp.MAP_ROOM) {
            if (playerHitbox.intersects(gp.bedroomDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE;
                x = gp.outsideBedroomDoorHitbox.x + gp.outsideBedroomDoorHitbox.width + 10;
                y = gp.outsideBedroomDoorHitbox.y + (gp.outsideBedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "right";
            }
        }
        else if (gp.currentMap == gp.MAP_HOUSE) {
            if (playerHitbox.intersects(gp.houseDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_STREET;
                    x = gp.streetHouseDoorHitbox.x + (gp.streetHouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.streetHouseDoorHitbox.y + gp.streetHouseDoorHitbox.height + 10;
                    direction = "down";
                }
            }
            else if (playerHitbox.intersects(gp.outsideBedroomDoorHitbox)) {
                gp.currentMap = gp.MAP_ROOM;
                x = gp.bedroomDoorHitbox.x - gp.tileSize - 10;
                y = gp.bedroomDoorHitbox.y + (gp.bedroomDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "left";
            }
        }
        else if (gp.currentMap == gp.MAP_WORKSHOP) {
            if (playerHitbox.intersects(gp.workshopDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetWorkshopDoorHitbox.x + gp.streetWorkshopDoorHitbox.width + 10;
                y = gp.streetWorkshopDoorHitbox.y + (gp.streetWorkshopDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "left";
            }
        }
        else if (gp.currentMap == gp.MAP_GREENHOUSE) {
            if (playerHitbox.intersects(gp.greenhouseDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetGreenhouseDoorHitbox.x - gp.tileSize - 10;
                y = gp.streetGreenhouseDoorHitbox.y + (gp.streetGreenhouseDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "right";
            }
        }
        else if (gp.currentMap == gp.MAP_MUSEUM) {
            if (playerHitbox.intersects(gp.museumDoorHitbox)) {
                gp.currentMap = gp.MAP_STREET;
                x = gp.streetMuseumDoorHitbox.x + (gp.streetMuseumDoorHitbox.width / 2) - (gp.tileSize / 2);
                y = gp.streetMuseumDoorHitbox.y + gp.streetMuseumDoorHitbox.height + 10;
                direction = "down";
            }
        }
        else if (gp.currentMap == gp.MAP_STREET) {
            if (playerHitbox.intersects(gp.streetHouseDoorHitbox)) {
                gp.currentMap = gp.MAP_HOUSE;
                x = gp.houseDoorHitbox.x - gp.tileSize - 10;
                y = gp.houseDoorHitbox.y + (gp.houseDoorHitbox.height / 2) - (gp.tileSize / 2);
                direction = "up";
            }
            else if (playerHitbox.intersects(gp.streetMuseumDoorHitbox)) {
                if (gp.currentQuest >= 3) {
                    gp.currentMap = gp.MAP_MUSEUM;
                    x = gp.museumDoorHitbox.x + (gp.museumDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.museumDoorHitbox.y - gp.tileSize - 5;
                    direction = "up";
                }
            }
            else if (playerHitbox.intersects(gp.streetWorkshopDoorHitbox)) {
                if (gp.currentQuest >= 1) {
                    gp.currentMap = gp.MAP_WORKSHOP;
                    x = gp.workshopDoorHitbox.x + (gp.workshopDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.workshopDoorHitbox.y - gp.tileSize - 10;
                    direction = "up";
                }
            }
            else if (playerHitbox.intersects(gp.streetGreenhouseDoorHitbox)) {
                if (gp.currentQuest >= 2) {
                    gp.currentMap = gp.MAP_GREENHOUSE;
                    x = gp.greenhouseDoorHitbox.x + (gp.greenhouseDoorHitbox.width / 2) - (gp.tileSize / 2);
                    y = gp.greenhouseDoorHitbox.y - gp.tileSize - 10;
                    direction = "up";
                }
            }
        }

            // boundaries to prevvent walking off screen
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x > gp.screenWidth - gp.tileSize) {
                x = gp.screenWidth - gp.tileSize;
            }
            // for interior maps - allow player to walk to the bottom of the screen
            if (y > gp.screenHeight - gp.tileSize && gp.currentMap == gp.MAP_STREET) {
                y = gp.screenHeight - gp.tileSize;
            }

            if (gp.mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);


                if (gp.currentMap == gp.MAP_HOUSE) {
//                if (mouseHitbox.intersects(gp.tempBtnHitbox)) {
//                    gp.introPuzzleOpen = true;
//                }
                    if (mouseHitbox.intersects(gp.debugIntroHitbox)) {
                        gp.introPuzzleOpen = true;
                        gp.introPuzzlePage = 1;
                    }
                } else if (gp.currentMap == gp.MAP_GREENHOUSE) {
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] != null) {
                            if (mouseHitbox.intersects(gp.obj[i].hitbox)) {
                                gp.obj[i] = null;
                                blackRosesCollected++;
                                if (blackRosesCollected >= 5 && gp.currentQuest == 2) {
                                    gp.currentQuest = 3; // Advance to Quest 3!
                                }
                            }
                        }
                    }
                } else if (gp.currentMap == gp.MAP_WORKSHOP) {
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] != null && gp.obj[i].name.equals("Glass Eye")) {
                            if (mouseHitbox.intersects(gp.obj[i].hitbox)) {
                                gp.obj[i] = null;
                                glassEyesCollected++;
                                if (glassEyesCollected >= 5 && gp.currentQuest == 1) {
                                    gp.currentQuest = 2; // Advance to Quest 2!
                                }
                            }
                        }
                    }
                } else if (gp.currentMap == gp.MAP_MUSEUM) {
                    if (gp.locketUnlocked == false) {

                        if (gp.chronosWatchUnlocked == true) {
                            gp.passwordUIOpen = true;
                            keyH.currentInput = "";
                        } else {
                            System.out.println("investigate statues first.");
                        }
                    }
                    if (mouseHitbox.intersects(gp.clue1Hitbox)) {
                        gp.clue1_Open = true;
                    } else if (mouseHitbox.intersects(gp.clue2Hitbox)) {
                        gp.clue2_Open = true;
                    } else if (mouseHitbox.intersects(gp.clue3Hitbox)) {
                        gp.clue3_Open = true;
                    } else if (mouseHitbox.intersects(gp.clue0Hitbox)) {
                        gp.clue0_Open = true;
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
        boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        switch(direction){
            case "up":
                if (!isMoving) image = backImage; // Standing still
                else if (spriteNum == 1) image = backWalk1;
                else image = backWalk2;
                break;
            case "down":
                if (!isMoving) image = frontImage; // Standing still
                else if (spriteNum == 1) image = frontWalk1;
                else image = frontWalk2;
                break;
            case "left":
                if (!isMoving) image = leftImage; // Standing still
                else if (spriteNum == 1) image = leftWalk1;
                else image = leftWalk2;
                break;
            case "right":
                if (!isMoving) image = rightImage; // Standing still
                else if (spriteNum == 1) image = rightWalk1;
                else image = rightWalk2;
                break;
        }

        g2.drawImage(image, x, y, gp.tileSize + 10, gp.tileSize + 10, null);
    }
}
