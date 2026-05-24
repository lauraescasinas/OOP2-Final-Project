package main;

import managers.AssetManager;
import entity.Player;
import inputs.KeyHandler;
import inputs.MouseHandler;
import managers.*;
import managers.UIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public final int MAP_HOUSE = 0;
    public final int MAP_STREET = 1;
    public final int MAP_WORKSHOP = 2;
    public final int MAP_GREENHOUSE = 3;
    public final int MAP_MUSEUM = 4;
    public final int MAP_ROOM = 5;
    public final int MAP_MAIN_MENU = -1;

    public CollisionManager cManager = new CollisionManager(this);
    public managers.UIManager uiManager = new UIManager(this);
    public AssetManager asManager = new AssetManager(this);
    public ObjectManager objManager = new ObjectManager(this);
    public DialogueManager dlgManager = new DialogueManager(this);
    public GameStateManager gsManager = new GameStateManager(this);
    public SoundManager soundManager = new SoundManager();

    public int introCutsceneFrameIndex = 0;
    public int introCutsceneTimer = 0;
    public int cutsceneFrameIndex = 0;
    public int cutsceneTimer = 0;

    public int rejectHoverCount = 0;

    // hitboxes
    public Rectangle settingsBtnHitbox = new Rectangle(20, 20, 48, 48);
    public Rectangle resumeHitbox = new Rectangle(332, 300, 200, 50);
    public Rectangle exitMenuHitbox = new Rectangle(332, 390, 200, 50);
    public Rectangle menuBtnHitbox = new Rectangle(50, 550, 235, 56);

    // ending text
    public String targetTheEndText = "the end.";
    public String currentTheEndText = "";
    public int theEndCharIndex = 0;
    public int theEndTimer = 0;

    // map animation variables
    public int mapFrameIndex = 0;
    public int mapFrameCounter = 0;
    public int mapAnimSpeed = 45;

    // gibberish
    public int gibberishFrameIndex = 0;
    public int gibberishCounter = 0;
    public int gibberishSpeed = 6;

    // main menu
    public int menuFrameIndex = 0;
    public int menuFrameCounter = 0;
    public final int menuFrameSpeed = 36; // ~600ms at 60 FPS
    public Rectangle playButtonHitbox = new Rectangle(600, 375, 100, 40);
    public Rectangle exitButtonHitbox = new Rectangle(600, 425, 100, 40);

    // demon animation
    public int demonFrameIndex = 0;
    public int demonCounter = 0;
    public int demonSpeed = 10;
    public int demonVanishTimer = 0;

    // event state: 0=Not Started, 1=Dialog1, 2=Wait 1s, 3=Wait 2s, 4=Dialog2, 5=Wait 2s, 6=Done
    public int eventTimer = 0;
    public int startTimer = 0;

    public int endingGibberishIndex = 0;
    public int endingGibberishTimer = 0;

    public Rectangle acceptHitbox = new Rectangle(200, 300, 235, 56);
    public Rectangle rejectHitbox = new Rectangle(480, 300, 235, 65);
    public Rectangle debugIntroHitbox = new Rectangle(780, 20, 60, 60);

    // hitboxs
    public Rectangle dialogueNextHitbox = new Rectangle(760, 580, 60, 60);

    public Rectangle glassCaseHitbox = new Rectangle(170, 280, Constants.tileSize + 2, Constants.tileSize + 2);
    public Rectangle backButtonHitbox = new Rectangle(50, 50, 60, 60);
    public Rectangle submitButtonHitbox = new Rectangle(560, 360, 60, 60);
    // clue hitboxes
    public Rectangle clue1Hitbox = new Rectangle(290, 280, 48, 48);
    public Rectangle clue2Hitbox = new Rectangle(510, 430, 48, 48);
    public Rectangle clue3Hitbox = new Rectangle(695, 320, 48, 48);
    public Rectangle clue0Hitbox = new Rectangle(230, 538, 48, 48);
    // statue in map hitboxes
    public Rectangle mapStatue1Hitbox = new Rectangle(560, 250, 70, 100);
    public Rectangle mapStatue2Hitbox = new Rectangle(630, 340, 70, 100);
    public Rectangle mapStatue3Hitbox = new Rectangle(670, 220, 70, 100);
    // statue in screen hitboxes
    public Rectangle uiStatue1Hitbox = new Rectangle(120, 250, 150, 200);
    public Rectangle uiStatue2Hitbox = new Rectangle(350, 250, 150, 200);
    public Rectangle uiStatue3Hitbox = new Rectangle(580, 250, 150, 200);

    // temporary button hitbox
    public Rectangle tempBtnHitbox = new Rectangle(416, 300, 48, 48);
    // button hitboxes
    public Rectangle nextButtonHitbox = new Rectangle(680, 350, 60, 80);
    public Rectangle prevButtonHitbox = new Rectangle(140, 350, 60, 80);
    // list screen1 row1
    public Rectangle r1c1Hitbox = new Rectangle(293, 280, 60, 60); // Jar
    public Rectangle r1c2Hitbox = new Rectangle(413, 280, 60, 60); // Bouquet
    public Rectangle r1c3Hitbox = new Rectangle(533, 280, 60, 60); // Watch
    //list screen1 row2
    public Rectangle r2c1Hitbox = new Rectangle(293, 435, 60, 60); // Locket
    public Rectangle r2c2Hitbox = new Rectangle(413, 435, 60, 60); // Watch
    public Rectangle r2c3Hitbox = new Rectangle(533, 435, 60, 60); // Bouquet


    int FPS = 60;
    public KeyHandler keyH = new KeyHandler();
    public MouseHandler mouseH = new MouseHandler();
    public Thread gameThread;
    public Player player = new Player(this, keyH);
    public InteractionManager iManager = new InteractionManager(this);


    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.screenWidth, Constants.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
        objManager.setObjects();
        soundManager.loadAudio();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public BufferedImage getStatueImage(int state) {
        if (state == 0) return asManager.statueLeft;
        if (state == 1) return asManager.statueBackLeft;
        if (state == 2) return asManager.statueBackRight;
        if (state == 3) return asManager.statueRight;
        return null;
    }

    public void update() {
        dlgManager.update();

        if (gsManager.getCurrentMap() == MAP_MAIN_MENU) {
            menuFrameCounter++;
            if (menuFrameCounter >= menuFrameSpeed) {
                menuFrameIndex = (menuFrameIndex + 1) % 4;
                menuFrameCounter = 0;
            }
            // play button
            if (mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(mouseH.mouseX, mouseH.mouseY, 1, 1);
                if (mouseHitbox.intersects(playButtonHitbox)) {
                    if (soundManager.clickSFX != null) {
                        soundManager.clickSFX.setFramePosition(0);
                        soundManager.clickSFX.start();
                    }
                    if (soundManager.menuMusic != null) {
                        soundManager.menuMusic.stop();
                        soundManager.menuMusic.close();
                    }
                    gsManager.setPlayingIntroCutscene(true);
                    gsManager.setCurrentMap(MAP_ROOM);
                    mouseH.leftClicked = false;
                } else if (mouseHitbox.intersects(exitButtonHitbox)) {
                    if (soundManager.clickSFX != null) soundManager.clickSFX.start();
                    System.exit(0); // exit
                }
                mouseH.leftClicked = false;
            }
            return;
        }
        // intro scene
        if (gsManager.isPlayingIntroCutscene()) {
            introCutsceneTimer++;
            if (introCutsceneTimer >= 80) { // 60 frames = 1 second at 60 FPS
                introCutsceneFrameIndex++;
                introCutsceneTimer = 0;
            }

            if (introCutsceneFrameIndex >= 9) {
                gsManager.setPlayingIntroCutscene(false);
                gsManager.setCurrentMap(MAP_ROOM);
            }
            return;
        }

        mapFrameCounter++;
        if (mapFrameCounter >= mapAnimSpeed) {
            mapFrameIndex = (mapFrameIndex == 0) ? 1 : 0;
            mapFrameCounter = 0;
        }

        if (gsManager.getCurrentMap() == MAP_ROOM && !gsManager.isIntroDialogueTriggered()) {
            startTimer++;
            // 120 frames at 60 FPS = 2 seconds
            if (startTimer >= 120) {
                dlgManager.startDialogue(dlgManager.roomDialogue);
                gsManager.setIntroDialogueTriggered(true);
            }
        }

        // typewriter anim
        if (dlgManager.isDialogueActive) {
            if (dlgManager.dialogueCharIndex < dlgManager.fullDialogue.length()) {
                dlgManager.typewriterCounter++;
                if (dlgManager.typewriterCounter >= dlgManager.typewriterSpeed) {
                    dlgManager.currentDialogue += dlgManager.fullDialogue.charAt(dlgManager.dialogueCharIndex);
                    dlgManager.dialogueCharIndex++;
                    dlgManager.typewriterCounter = 0;
                }
            }
        }

        if (gsManager.isIntroPuzzleOpen() && gsManager.getIntroPuzzlePage() == 3) {
            gibberishCounter++;
            if (gibberishCounter >= gibberishSpeed) {
                gibberishFrameIndex++;
                if (gibberishFrameIndex >= 10) {
                    gibberishFrameIndex = 0;
                }
                gibberishCounter = 0;
            }
        }

        // event states
        if (gsManager.getHouseEventState() == 2) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second (60 frames)
                gsManager.setDemonVisible(true);
                gsManager.setHouseEventState(3);
                eventTimer = 0;
            }
        } else if (gsManager.getHouseEventState() == 3) {
            eventTimer++;
            if (eventTimer >= 120) { // 2 seconds
                gsManager.setHouseEventState(4);
                dlgManager.startDialogue(dlgManager.houseDialogue2);
                eventTimer = 0;
            }
        } else if (gsManager.getHouseEventState() == 5) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second
                gsManager.setIntroPuzzleOpen(true);
                gsManager.setHouseEventState(6);
                eventTimer = 0;
            }
        } else if (gsManager.getHouseEventState() == 8) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second
                gsManager.setCurrentQuest(6);
                gsManager.setHouseEventState(9);
                eventTimer = 0;
            }
        }

        // demon animation
        if (gsManager.isDemonVisible()) {
            demonCounter++;
            if (demonCounter >= demonSpeed) {
                demonFrameIndex = (demonFrameIndex + 1) % 2;
                demonCounter = 0;
            }
        }

        // demon vanishes
        if (dlgManager.isDialogueActive && dlgManager.currentDialogueArray == dlgManager.houseDialogue3) {
        } else if (gsManager.getHouseEventState() == 0) {
            demonVanishTimer = 0;
        }

        if (gsManager.getHouseEventState() == 10) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second after line 6
                gsManager.setShowEndingGibberish(true);
                endingGibberishIndex = 0;
                endingGibberishTimer = 0;
                gsManager.setHouseEventState(11);
                eventTimer = 0;
            }
        } else if (gsManager.getHouseEventState() == 13) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second after line 9
                gsManager.setShowChoiceScreen(true);
                gsManager.setHouseEventState(14);
                eventTimer = 0;
            }
        }

        // gibberish screen at ending: 0.5s per frame, stops at 11
        if (gsManager.isShowEndingGibberish()) {
            endingGibberishTimer++;
            if (endingGibberishTimer >= 30) { // 0.5s at 60 FPS
                if (endingGibberishIndex < 10) {
                    endingGibberishIndex++;
                }
                endingGibberishTimer = 0;
            }
        }

        // reject button changing positions every cursor hover
        if (gsManager.isShowChoiceScreen()) {
            Rectangle mouseHitbox = new Rectangle(mouseH.mouseX, mouseH.mouseY, 1, 1);
            if (rejectHoverCount < 5 && mouseHitbox.intersects(rejectHitbox)) {
                rejectHoverCount++;
                if (rejectHoverCount == 1) {
                    rejectHitbox.x = 100;
                    rejectHitbox.y = 150;
                } else if (rejectHoverCount == 2) {
                    rejectHitbox.x = 600;
                    rejectHitbox.y = 450;
                } else if (rejectHoverCount == 3) {
                    rejectHitbox.x = 200;
                    rejectHitbox.y = 500;
                } else if (rejectHoverCount == 4) {
                    rejectHitbox.x = 650;
                    rejectHitbox.y = 100;
                } else if (rejectHoverCount == 5) {
                    rejectHitbox.x = 480;
                    rejectHitbox.y = 300;
                }
            }
        }

        // ending cutscene timer
        if (gsManager.isPlayingAcceptCutscene() || gsManager.isPlayingRejectCutscene()) {
            if (cutsceneFrameIndex < 7) {
                cutsceneTimer++;
                if (cutsceneTimer >= 60) {
                    cutsceneFrameIndex++;
                    cutsceneTimer = 0;
                }
            } else {
                gsManager.setShowTheEndText(true);
            }

            // "the end." typewriter
            if (gsManager.isShowTheEndText() && theEndCharIndex < targetTheEndText.length()) {
                theEndTimer++;
                if (theEndTimer >= 30) {
                    currentTheEndText += targetTheEndText.charAt(theEndCharIndex);
                    theEndCharIndex++;
                    theEndTimer = 0;
                }
            } else if (gsManager.isShowTheEndText() && theEndCharIndex >= targetTheEndText.length()) {
                gsManager.setShowMenuButton(true);
            }
        }

        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gsManager.getCurrentMap() == MAP_MAIN_MENU) {
            if (asManager.menuFrames[menuFrameIndex] != null)
                g2.drawImage(asManager.menuFrames[menuFrameIndex], 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            if (keyH.showDebug) {
                g2.setColor(new Color(255, 255, 0, 100));
                g2.fillRect(playButtonHitbox.x, playButtonHitbox.y, playButtonHitbox.width, playButtonHitbox.height);
                g2.fillRect(exitButtonHitbox.x, exitButtonHitbox.y, exitButtonHitbox.width, exitButtonHitbox.height);
            }
            g2.dispose();
            return;
        }

        if (gsManager.isPlayingIntroCutscene()) {
            if (introCutsceneFrameIndex < 9 && asManager.introCutscene[introCutsceneFrameIndex] != null) {
                g2.drawImage(asManager.introCutscene[introCutsceneFrameIndex], 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            }
            g2.dispose();
            return;
        }

        if (gsManager.getCurrentMap() == MAP_ROOM && asManager.roomBg != null) {
            g2.drawImage(asManager.roomBg, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            if (keyH.showDebug) {
                g2.setColor(new Color(0, 0, 255, 100));
                for (Rectangle wall : cManager.mapBarrier.roomWalls) g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (gsManager.getCurrentMap() == MAP_HOUSE && asManager.houseBg != null) {
            g2.drawImage(asManager.houseBg, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            if (gsManager.isDemonVisible()) {
                BufferedImage currentDemon = (demonFrameIndex == 0) ? asManager.demon1 : asManager.demon2;
                if (currentDemon != null) g2.drawImage(currentDemon, 330, 230, Constants.tileSize * 2, Constants.tileSize * 2, null);
            }
            if (keyH.showDebug) {
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fillRect(debugIntroHitbox.x, debugIntroHitbox.y, debugIntroHitbox.width, debugIntroHitbox.height);
                g2.setColor(new Color(0, 0, 255, 100));
                for (Rectangle wall : cManager.mapBarrier.houseWalls) g2.fillRect(wall.x, wall.y, wall.width, wall.height);
                g2.fillRect(tempBtnHitbox.x, tempBtnHitbox.y, tempBtnHitbox.width, tempBtnHitbox.height);
            }
        } else if (gsManager.getCurrentMap() == MAP_STREET) {
            BufferedImage currentStreet = (mapFrameIndex == 0) ? asManager.streetBg1 : asManager.streetBg2;
            if (currentStreet != null) g2.drawImage(currentStreet, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            if (keyH.showDebug) {
                g2.setColor(new Color(0, 0, 255, 100));
                for (Rectangle wall : cManager.mapBarrier.streetWalls) g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (gsManager.getCurrentMap() == MAP_WORKSHOP) {
            BufferedImage currentWorkshop = (mapFrameIndex == 0) ? asManager.workshopBg1 : asManager.workshopBg2;
            if (currentWorkshop != null) g2.drawImage(currentWorkshop, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            for (int i = 0; i < objManager.obj.length; i++) {
                if (objManager.obj[i] != null && objManager.obj[i].name.equals("Glass Eye")) {
                    objManager.obj[i].draw(g2, this);
                    if (keyH.showDebug) {
                        g2.setColor(new Color(255, 255, 0, 150));
                        g2.fillRect(objManager.obj[i].hitbox.x, objManager.obj[i].hitbox.y, objManager.obj[i].hitbox.width, objManager.obj[i].hitbox.height);
                    }
                }
            }
            if (keyH.showDebug) {
                g2.setColor(new Color(0, 0, 255, 100));
                for (Rectangle wall : cManager.mapBarrier.workshopWalls) g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (gsManager.getCurrentMap() == MAP_GREENHOUSE) {
            BufferedImage currentGreenhouse = (mapFrameIndex == 0) ? asManager.greenhouseBg1 : asManager.greenhouseBg2;
            if (currentGreenhouse != null) g2.drawImage(currentGreenhouse, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            for (int i = 0; i < objManager.obj.length; i++) {
                if (objManager.obj[i] != null && objManager.obj[i].name.equals("Black Rose")) {
                    objManager.obj[i].draw(g2, this);
                    if (keyH.showDebug) {
                        g2.setColor(new Color(255, 255, 0, 150));
                        g2.fillRect(objManager.obj[i].hitbox.x, objManager.obj[i].hitbox.y, objManager.obj[i].hitbox.width, objManager.obj[i].hitbox.height);
                    }
                }
            }
            if (keyH.showDebug) {
                g2.setColor(new Color(0, 0, 255, 100));
                for (Rectangle wall : cManager.mapBarrier.greenhouseWalls)
                    g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (gsManager.getCurrentMap() == MAP_MUSEUM && asManager.museumBg != null) {
            g2.drawImage(asManager.museumBg, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            if (asManager.clue1 != null) g2.drawImage(asManager.clue1, 290, 280, Constants.tileSize - 7, Constants.tileSize - 6, null);
            if (asManager.clue2 != null) g2.drawImage(asManager.clue2, 510, 437, Constants.tileSize - 7, Constants.tileSize - 6, null);
            if (asManager.clue3 != null) g2.drawImage(asManager.clue3, 705, 320, Constants.tileSize - 10, Constants.tileSize - 9, null);
            if (asManager.clue0 != null) g2.drawImage(asManager.clue0, 230, 538, Constants.tileSize - 7, Constants.tileSize - 6, null);


            g2.drawImage(getStatueImage(gsManager.getStatueState(0)), 560, 250, 70, 100, null);
            g2.drawImage(getStatueImage(gsManager.getStatueState(1)), 630, 340, 70, 100, null);
            g2.drawImage(getStatueImage(gsManager.getStatueState(2)), 670, 228, 70, 100, null);

            player.draw(g2);
            g2.drawImage(asManager.glasscaseMuseum, 647, 385, Constants.tileSize * 2, Constants.tileSize * 4, null);

            if (!gsManager.isLocketUnlocked()) {
                if (asManager.lockedCase != null) g2.drawImage(asManager.lockedCase, 170, 280, Constants.tileSize + 2, Constants.tileSize + 2, null);
            } else {
                if (asManager.unlockedCase != null) g2.drawImage(asManager.unlockedCase, 150, 280, Constants.tileSize + 2, Constants.tileSize + 2, null);
            }

            if (keyH.showDebug) {
                g2.setColor(new Color(0, 0, 255, 100));
                g2.fillRect(clue1Hitbox.x, clue1Hitbox.y, clue1Hitbox.width, clue1Hitbox.height);
                g2.fillRect(clue2Hitbox.x, clue2Hitbox.y, clue2Hitbox.width, clue2Hitbox.height);
                g2.fillRect(clue3Hitbox.x, clue3Hitbox.y, clue3Hitbox.width, clue3Hitbox.height);
                g2.fillRect(clue0Hitbox.x, clue0Hitbox.y, clue0Hitbox.width, clue0Hitbox.height);
                for (Rectangle wall : cManager.mapBarrier.museumWalls) g2.fillRect(wall.x, wall.y, wall.width, wall.height);
                g2.setColor(new Color(255, 100, 0, 100));
                g2.fillRect(mapStatue1Hitbox.x, mapStatue1Hitbox.y, mapStatue1Hitbox.width, mapStatue1Hitbox.height);
                g2.fillRect(mapStatue2Hitbox.x, mapStatue2Hitbox.y, mapStatue2Hitbox.width, mapStatue2Hitbox.height);
                g2.fillRect(mapStatue3Hitbox.x, mapStatue3Hitbox.y, mapStatue3Hitbox.width, mapStatue3Hitbox.height);
                if (!gsManager.isLocketUnlocked()) {
                    g2.setColor(new Color(255, 0, 0, 100));
                    g2.fillRect(glassCaseHitbox.x, glassCaseHitbox.y, glassCaseHitbox.width, glassCaseHitbox.height);
                }
            }
        }

        if (keyH.showDebug) {
            g2.setColor(new Color(255, 0, 0, 100));
            if (gsManager.getCurrentMap() == MAP_HOUSE) {
                g2.fillRect(cManager.houseDoorHitbox.x, cManager.houseDoorHitbox.y, cManager.houseDoorHitbox.width, cManager.houseDoorHitbox.height);
                g2.fillRect(cManager.outsideBedroomDoorHitbox.x, cManager.outsideBedroomDoorHitbox.y, cManager.outsideBedroomDoorHitbox.width, cManager.outsideBedroomDoorHitbox.height);
            } else if (gsManager.getCurrentMap() == MAP_STREET) {
                g2.fillRect(cManager.streetHouseDoorHitbox.x, cManager.streetHouseDoorHitbox.y, cManager.streetHouseDoorHitbox.width, cManager.streetHouseDoorHitbox.height);
                g2.fillRect(cManager.streetMuseumDoorHitbox.x, cManager.streetMuseumDoorHitbox.y, cManager.streetMuseumDoorHitbox.width, cManager.streetMuseumDoorHitbox.height);
                g2.fillRect(cManager.streetWorkshopDoorHitbox.x, cManager.streetWorkshopDoorHitbox.y, cManager.streetWorkshopDoorHitbox.width, cManager.streetWorkshopDoorHitbox.height);
                g2.fillRect(cManager.streetGreenhouseDoorHitbox.x, cManager.streetGreenhouseDoorHitbox.y, cManager.streetGreenhouseDoorHitbox.width, cManager.streetGreenhouseDoorHitbox.height);
            } else if (gsManager.getCurrentMap() == MAP_WORKSHOP) {
                g2.fillRect(cManager.workshopDoorHitbox.x, cManager.workshopDoorHitbox.y, cManager.workshopDoorHitbox.width, cManager.workshopDoorHitbox.height);
            } else if (gsManager.getCurrentMap() == MAP_GREENHOUSE) {
                g2.fillRect(cManager.greenhouseDoorHitbox.x, cManager.greenhouseDoorHitbox.y, cManager.greenhouseDoorHitbox.width, cManager.greenhouseDoorHitbox.height);
            } else if (gsManager.getCurrentMap() == MAP_MUSEUM) {
                g2.fillRect(cManager.museumDoorHitbox.x, cManager.museumDoorHitbox.y, cManager.museumDoorHitbox.width, cManager.museumDoorHitbox.height);
            } else if (gsManager.getCurrentMap() == MAP_ROOM) {
                g2.fillRect(cManager.bedroomDoorHitbox.x, cManager.bedroomDoorHitbox.y, cManager.bedroomDoorHitbox.width, cManager.bedroomDoorHitbox.height);
            }
        }

        if (gsManager.getCurrentMap() != MAP_MUSEUM) player.draw(g2);

        uiManager.drawAllUI(g2);
        if (gsManager.getCurrentMap() == MAP_WORKSHOP){
            g2.drawImage(asManager.toolboxWorkshop, 113, 263, 37, 23, null);
            g2.drawImage(asManager.sofaWorkshop, 509, 369, 88, 190, null);
            player.draw(g2);
            g2.drawImage(asManager.counterWorkshop, 75, 384, 71, 63, null);
        }


        g2.dispose();
    }
}