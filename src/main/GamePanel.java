package main;

import entity.Player;
import object.SuperObject;
import object.BlackroseObject;
import object.GlasseyeObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;    // 16x16 tile for characters
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;  // 48x48 tile
    final int maxScreenCol = 18;
    final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol;  // 864 pixels
    public final int screenHeight = tileSize * maxScreenRow;  // 672 pixels

    public final int MAP_HOUSE = 0;
    public final int MAP_STREET = 1;
    public final int MAP_WORKSHOP = 2;
    public final int MAP_GREENHOUSE = 3;
    public final int MAP_MUSEUM = 4;
    public final int MAP_ROOM = 5;
    public final int MAP_MAIN_MENU = -1;
    public int currentMap = MAP_MAIN_MENU;// start in the house

    // BARRIERS
    public Rectangle[] roomWalls = {
            new Rectangle(250, 100, 360, 180),
            new Rectangle(250, 470, 360, 150),
            new Rectangle(130, 100, 150, 500),
            new Rectangle(580, 100, 150, 280),
            new Rectangle(290, 320, 120, 40)
    };

    public Rectangle[] houseWalls = {
            new Rectangle(370, 170, 170, 80),
            new Rectangle(580, 60, 100, 400),
            new Rectangle(250, 70, 100, 400),
            new Rectangle(100, 120, 150, 250),
            new Rectangle(110, 530, 600, 100)
    };

    public Rectangle[] streetWalls = {
            new Rectangle(200, 210, 420, 80),
            new Rectangle(840, 30, 90, 600),
            new Rectangle(750, 0, 100, 280),
            new Rectangle(5, 668, 1000, 90),
            new Rectangle(30, 470, 300, 50),
            new Rectangle(290, 480, 40, 120),
            new Rectangle(-20, 250, 50, 300),
            new Rectangle(530, 500, 200, 300),
            //tree
            new Rectangle(270, 240, 100, 100)
    };

    public Rectangle[] workshopWalls = {
            new Rectangle(390, 190, 420, 50),
            new Rectangle(250, 600, 700, 50),
            new Rectangle(150, 400, 270, 80),
            new Rectangle(100, 210, 270, 60),

            new Rectangle(12, 130, 60, 430),
            new Rectangle(380, 180, 60, 250),
            new Rectangle(800, 100, 60, 550)
    };

    public Rectangle[] greenhouseWalls = {
            new Rectangle(90, 30, 60, 800),
            new Rectangle(720, 20, 60, 800),
            new Rectangle(100, 90, 600, 60),
            new Rectangle(10, 600, 320, 60),
            new Rectangle(520, 580, 320, 60)
    };

    public Rectangle[] museumWalls = {
            new Rectangle(90, 30, 60, 800),
            new Rectangle(720, 20, 60, 800),
            new Rectangle(120, 210, 600, 60),
            new Rectangle(20, 580, 320, 60),
            new Rectangle(520, 580, 320, 60)
    };

    BufferedImage roomBg;
    BufferedImage houseBg;
    BufferedImage streetBg1, streetBg2;
    BufferedImage workshopBg1, workshopBg2, toolboxWorkshop, sofaWorkshop;
    BufferedImage greenhouseBg1, greenhouseBg2;
    BufferedImage museumBg, tableMuseum, glasscaseMuseum;
    BufferedImage bouquetInv;
    BufferedImage jarInv;
    BufferedImage inventoryBox;
    BufferedImage lockedCase, clue0, clue1, clue2, clue3, passwordUI, backBtn, locketInv, unlockedCase, watchInv;
    BufferedImage openClue1, openClue2, openClue3, openClue0;
    BufferedImage statueRotateScreen, statueLeft, statueBackLeft, statueBackRight, statueRight;
    BufferedImage tempBtn, listScreen1, listScreen2, listScreen3, nextBtn, prevBtn;
    BufferedImage objTab1, objTab2, objTab3, objTab4, objTab5;
    BufferedImage endScreen, againBtn;

    public int rejectHoverCount = 0;

    public BufferedImage settingsBtn, settingsWindow;
    public boolean isSettingsOpen = false;

    // Hitboxes (Coordinates estimated based on a centered 400x300 window)
    public Rectangle settingsBtnHitbox = new Rectangle(20, 20, 48, 48); // Top left
    public Rectangle resumeHitbox = new Rectangle(332, 300, 200, 50);   // Adjust these later!
    public Rectangle exitMenuHitbox = new Rectangle(332, 390, 200, 50); // Adjust these later!

    public boolean playingAcceptCutscene = false;
    public boolean playingRejectCutscene = false;
    public BufferedImage[] acceptCutscene = new BufferedImage[8];
    public BufferedImage[] rejectCutscene = new BufferedImage[8];
    public int cutsceneFrameIndex = 0;
    public int cutsceneTimer = 0;

    public BufferedImage menuBtn;
    public Rectangle menuBtnHitbox = new Rectangle(50, 550, 235, 56); // Bottom left
    public boolean showTheEndText = false;
    public boolean showMenuButton = false;
    public String targetTheEndText = "the end.";
    public String currentTheEndText = "";
    public int theEndCharIndex = 0;
    public int theEndTimer = 0;

    public boolean passwordUIOpen = false;
    public boolean locketUnlocked = false;
    public boolean clue1_Open = false;
    public boolean clue2_Open = false;
    public boolean clue3_Open = false;
    public boolean clue0_Open = false;
    public boolean statue_Open = false;
    public boolean introPuzzleOpen = false;
    public boolean chronosWatchUnlocked = false;
    public boolean introAns1 = false, introAns2 = false, introAns3 = false, introAns4 = false;

    // map animation variables
    public int mapFrameIndex = 0; // Toggles between 0 and 1
    public int mapFrameCounter = 0;
    public int mapAnimSpeed = 45;

    // --- Animated Gibberish Variables ---
    public BufferedImage[] gibberishFrames = new BufferedImage[10];
    public int gibberishFrameIndex = 0; // Tracks which frame is currently showing
    public int gibberishCounter = 0;    // Timer counter
    public int gibberishSpeed = 6;      // Speed of animation (lower = faster)

    BufferedImage[] menuFrames = new BufferedImage[4];
    public int menuFrameIndex = 0;
    public int menuFrameCounter = 0;
    public final int menuFrameSpeed = 36; // ~600ms at 60 FPS
    public Rectangle playButtonHitbox = new Rectangle(600, 375, 100, 40);
    public Rectangle exitButtonHitbox = new Rectangle(600, 425, 100, 40);
    private javax.sound.sampled.Clip menuMusic;
    private javax.sound.sampled.Clip clickSFX;

    // --- NEW: Demon Animation & Event Variables ---
    BufferedImage demon1, demon2;
    public boolean demonVisible = false;
    public int demonFrameIndex = 0;
    public int demonCounter = 0;
    public int demonSpeed = 10; // Animation speed
    public int demonVanishTimer = 0;

    // Event State: 0=Not Started, 1=Dialog1, 2=Wait 1s, 3=Wait 2s, 4=Dialog2, 5=Wait 2s, 6=Done
    public int houseEventState = 0;
    public int eventTimer = 0;

    // --- UPDATED: Dialogue System Variables ---
    public boolean isDialogueActive = false;
    public boolean introDialogueTriggered = false;
    public int startTimer = 0;

    // Dialogue Arrays (with manual \n for text wrapping)
    public String[] houseDialogue1 = {
            "Before leaving for the day, Elara kneels once more to offer a prayer\nfor her father—who died just two nights ago.",
            "Elara: Please... just let him get there safely. That's all I'm asking."
    };

    public String[] houseDialogue2 = {
            "Demon: How sweet. A little prayer for the great Edmund Voss.", // 0
            "Elara: What— who are you?!", // 1
            "Demon: Someone who was looking forward to meeting your father\nfor a very long time. And look at this...", // 2
            "Demon: White lilies. A normal casket. A cross on the wall.", // 3
            "Demon: Did you even know him?", // 4
            "Elara: ...what are you trying to say—", // 5
            "Demon: A man who kept a jar of dead men's eyes on his desk. A man who\ntalked to taxidermied foxes. And you send him off like he was an accountant.", // 6
            "Elara: Shut up.", // 7
            "Demon: His soul is... restless, Elara. Unsettled. And if no one does\nanything about that—", // 8
            "Demon: I'll just take it with me.", // 9 (Will be styled RED and BOLD)
            "Demon: Unless...you do something for me.", // 10
            "Demon: There are items — strange ones, specific ones — scattered around\nthis house and the places he loved. Collect them. Arrange them. And his\nsoul goes free.", // 11
            "Demon: I've been kind enough to write most of them down.", // 12
            "Elara: ...Some of these descriptions don't even make sense.", // 13
            "Demon: Your father made sense of stranger things. I'm sure you'll manage.", // 14
            "Demon: Clock's ticking, Elara. It always is." // 15
    };

    public String[] houseDialogue3 = {
            "Elara lays the last item beside the casket. Her hands are shaking, but she's\ndone it. She steps back and faces the demon.", // 0
            "Elara: That's everything. It's done.", // 1
            "The demon doesn't budge.", // 2
            "Elara: Hello?  I said I'm done. I collected all of them.", // 3
            "Demon: Didn't you see the last page of the list I gave you?", // 4 (BOLD RED)
            "Elara stands frozen—it's not that she didn't see it.", // 5
            "She wanted to ignore it.", // 6
            "You can't do this to me. I already did what you told me to do-", // 7
            "Demon: Don't you want your father's soul to be at peace?", // 8 (BOLD RED)
            "..." // 9
    };

    public String[] roomDialogue = {
            "October, 1998. The whole house smells like candle wax and old paper.\n" +
                    "Fifteen-year-old Elara stands at the side of her bed, still in yesterday’s\n" +
                    "clothes, trying to piece together the last seventy-two hours."
    };

    public BufferedImage[] endingGibberishFrames = new BufferedImage[11];
    public boolean showEndingGibberish = false;
    public int endingGibberishIndex = 0;
    public int endingGibberishTimer = 0;

    public boolean showChoiceScreen = false;
    public BufferedImage acceptBtn, rejectBtn;
    public Rectangle acceptHitbox = new Rectangle(200, 300, 235, 56); // Adjust later
    public Rectangle rejectHitbox = new Rectangle(480, 300, 235, 65); // Adjust later

    public String[] currentDialogueArray = null;
    public int currentDialogueListIndex = 0;

    public String fullDialogue = "";
    public String currentDialogue = "";
    public int dialogueCharIndex = 0;
    public int typewriterSpeed = 2;
    public int typewriterCounter = 0;

    public Rectangle debugIntroHitbox = new Rectangle(780, 20, 60, 60); // NEW Debug hitbox top right

    public int statue1State = 0; // default state: left
    public int statue2State = 3; // default state: right
    public int statue3State = 2; // default state:  back_right
    public int introPuzzlePage = 1;
    public int currentQuest = 0;

    // hitboxs

    public Rectangle dialogueNextHitbox = new Rectangle(760, 580, 60, 60);

    public Rectangle glassCaseHitbox = new Rectangle(170, 280, tileSize + 2, tileSize + 2);
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

    // again button -- TEMPORARY!
    public Rectangle againBtnHitbox = new Rectangle(screenWidth / 2 - 80, screenHeight / 2 + 80, 160, 80);

    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    public MouseHandler mouseH = new MouseHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    public SuperObject obj[] = new SuperObject[10];

    // inside doors
    public Rectangle houseDoorHitbox = new Rectangle(670, 440, 164, 100);
    public Rectangle workshopDoorHitbox = new Rectangle(80, 600, 164, 100);
    public Rectangle greenhouseDoorHitbox = new Rectangle(350, 620, 164, 100);
    public Rectangle museumDoorHitbox = new Rectangle(350, 580, 164, 100);
    public Rectangle bedroomDoorHitbox = new Rectangle(585, 400, 80, 100);
    public Rectangle outsideBedroomDoorHitbox = new Rectangle(100, 400, 60, 100);

    // outside doors
    public Rectangle streetHouseDoorHitbox = new Rectangle(50, 260, 85, 45);
    public Rectangle streetMuseumDoorHitbox = new Rectangle(650, 180, 85, 45);
    public Rectangle streetWorkshopDoorHitbox = new Rectangle(200, 630, 85, 45);
    public Rectangle streetGreenhouseDoorHitbox = new Rectangle(736, 435, 85, 45);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
        loadBackgrounds();
    }

    public void setupGame() {
        // blackroses in greenhouse
        obj[0] = new BlackroseObject();
        obj[0].x = 245;
        obj[0].y = 240;
        obj[0].hitbox.x = 245;
        obj[0].hitbox.y = 240;
        obj[0].width = 24;
        obj[0].height = 24;
        obj[0].hitbox.width = 24;
        obj[0].hitbox.height = 24;

        obj[1] = new BlackroseObject();
        obj[1].x = 265;
        obj[1].y = 490;
        obj[1].hitbox.x = 265;
        obj[1].hitbox.y = 487;
        obj[1].width = 32;
        obj[1].height = 32;
        obj[1].hitbox.width = 32;
        obj[1].hitbox.height = 32;

        obj[2] = new BlackroseObject();
        obj[2].x = 606;
        obj[2].y = 510;
        obj[2].hitbox.x = 606;
        obj[2].hitbox.y = 510;
        obj[2].width = 34;
        obj[2].height = 34;
        obj[2].hitbox.width = 34;
        obj[2].hitbox.height = 34;

        obj[3] = new BlackroseObject();
        obj[3].x = 565;
        obj[3].y = 368;
        obj[3].hitbox.x = 565;
        obj[3].hitbox.y = 365;
        obj[3].width = 20;
        obj[3].height = 20;
        obj[3].hitbox.width = 20;
        obj[3].hitbox.height = 20;

        obj[4] = new BlackroseObject();
        obj[4].x = 550;
        obj[4].y = 120;
        obj[4].hitbox.x = 550;
        obj[4].hitbox.y = 120;
        obj[4].width = 38;
        obj[4].height = 38;
        obj[4].hitbox.width = 38;
        obj[4].hitbox.height = 38;

        // glass eyes in workshop
        obj[5] = new GlasseyeObject();
        obj[5].x = 102;
        obj[5].y = 265;
        obj[5].hitbox.x = 102;
        obj[5].hitbox.y = 265;
        obj[5].width = 20;
        obj[5].height = 20;
        obj[5].hitbox.width = 20;
        obj[5].hitbox.height = 20;

        obj[6] = new GlasseyeObject();
        obj[6].x = 340;
        obj[6].y = 170;
        obj[6].hitbox.x = 340;
        obj[6].hitbox.y = 170;
        obj[6].width = 26;
        obj[6].height = 26;
        obj[6].hitbox.width = 26;
        obj[6].hitbox.height = 26;

        obj[7] = new GlasseyeObject();
        obj[7].x = 563;
        obj[7].y = 146;
        obj[7].hitbox.x = 563;
        obj[7].hitbox.y = 146;
        obj[7].width = 27;
        obj[7].height = 27;
        obj[7].hitbox.width = 27;
        obj[7].hitbox.height = 27;

        obj[8] = new GlasseyeObject();
        obj[8].x = 530;
        obj[8].y = 545;
        obj[8].hitbox.x = 530;
        obj[8].hitbox.y = 545;
        obj[8].width = 20;
        obj[8].height = 20;
        obj[8].hitbox.width = 20;
        obj[8].hitbox.height = 20;

        obj[9] = new GlasseyeObject();
        obj[9].x = 789;
        obj[9].y = 546;
        obj[9].hitbox.x = 789;
        obj[9].hitbox.y = 546;
        obj[9].width = 28;
        obj[9].height = 28;
        obj[9].hitbox.width = 28;
        obj[9].hitbox.height = 28;
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
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // backgrounds
    public void loadBackgrounds() {
        try {
            for (int i = 0; i < 4; i++) {
                menuFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/MainMenu_" + (i + 1) + ".png"));
            }

            try {
                java.net.URL musicURL = getClass().getResource("/Music/MainMenu.wav");
                if (musicURL != null) {
                    javax.sound.sampled.AudioInputStream ais =
                            javax.sound.sampled.AudioSystem.getAudioInputStream(musicURL);
                    menuMusic = javax.sound.sampled.AudioSystem.getClip();
                    menuMusic.open(ais);
                }

                java.net.URL sfxURL = getClass().getResource("/Music/Button.wav");
                if (sfxURL != null) {
                    javax.sound.sampled.AudioInputStream ais2 =
                            javax.sound.sampled.AudioSystem.getAudioInputStream(sfxURL);
                    clickSFX = javax.sound.sampled.AudioSystem.getClip();
                    clickSFX.open(ais2);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            houseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/House_bg.png"));
            streetBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg1.png"));
            streetBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg2.png"));

            workshopBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg1.png"));
            workshopBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg2.png"));
            toolboxWorkshop = ImageIO.read(getClass().getResourceAsStream("/Objects/toolbox_Workshop_bg.png"));
            sofaWorkshop = ImageIO.read(getClass().getResourceAsStream("/Objects/sofa_Workshop_bg.png"));
            greenhouseBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg1.png"));
            greenhouseBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg2.png"));
            roomBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Room_bg.png"));
            houseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/House_bg.png"));
            museumBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Museum_bg.png"));
            tableMuseum = ImageIO.read(getClass().getResourceAsStream("/Objects/table_Museum_bg.png"));
            glasscaseMuseum = ImageIO.read(getClass().getResourceAsStream("/Objects/glasscase_Museum_bg.png"));

            // for intro list
            tempBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/temporary_button.png"));
            listScreen1 = ImageIO.read(getClass().getResourceAsStream("/Objects/list_screen1.png"));
            nextBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/next_button.png"));

            demon1 = ImageIO.read(getClass().getResourceAsStream("/Objects/Demon_1.png"));
            demon2 = ImageIO.read(getClass().getResourceAsStream("/Objects/Demon_2.png"));

            // bouquet roses, glass eyes, locket, watch in inventory once collected || quest items loaded
            bouquetInv = ImageIO.read(getClass().getResourceAsStream("/Objects/bouquet_roses.png"));
            jarInv = ImageIO.read(getClass().getResourceAsStream("/Objects/jar_eyes.png"));
            locketInv = ImageIO.read(getClass().getResourceAsStream("/Objects/memento_locket.png"));
            watchInv = ImageIO.read(getClass().getResourceAsStream("/Objects/chronos_watch.png"));
            inventoryBox = ImageIO.read(getClass().getResourceAsStream("/Objects/inventory_box.png"));

            lockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/locked_GlassCase.png"));
            passwordUI = ImageIO.read(getClass().getResourceAsStream("/Objects/password_input.png"));
            backBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/back_button.png"));
            unlockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/unlocked_GlassCase.png"));

            // clues
            clue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue1.png"));
            clue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue2.png"));
            clue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue3.png"));
            clue0 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue0.png"));
            openClue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue1.png"));
            openClue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue2.png"));
            openClue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue3.png"));
            openClue0 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue0.png"));

            // statues
            statueRotateScreen = ImageIO.read(getClass().getResourceAsStream("/Objects/statuerotate_Screen.png"));
            statueLeft = ImageIO.read(getClass().getResourceAsStream("/Objects/statue_left.png"));
            statueBackLeft = ImageIO.read(getClass().getResourceAsStream("/Objects/statue_back_left.png"));
            statueBackRight = ImageIO.read(getClass().getResourceAsStream("/Objects/statue_back_right.png"));
            statueRight = ImageIO.read(getClass().getResourceAsStream("/Objects/statue_right.png"));

            listScreen2 = ImageIO.read(getClass().getResourceAsStream("/Objects/list_screen2.png"));
            listScreen3 = ImageIO.read(getClass().getResourceAsStream("/Objects/list_screen3.png"));
            prevBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/prev_button.png"));

            // quest system
            objTab1 = ImageIO.read(getClass().getResourceAsStream("/Objects/objective_tab1.png"));
            objTab2 = ImageIO.read(getClass().getResourceAsStream("/Objects/objective_tab2.png"));
            objTab3 = ImageIO.read(getClass().getResourceAsStream("/Objects/objective_tab3.png"));
            objTab4 = ImageIO.read(getClass().getResourceAsStream("/Objects/objective_tab4.png"));
            objTab5 = ImageIO.read(getClass().getResourceAsStream("/Objects/objective_tab5.png"));
            endScreen = ImageIO.read(getClass().getResourceAsStream("/Objects/end_screen.png"));
            againBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/again_button.png"));

            for (int i = 0; i < 10; i++) {
                gibberishFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/Gibberish_" + (i + 1) + ".png"));
            }

            for (int i = 0; i < 11; i++) {
                endingGibberishFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/gibberish_Ending/Gibberish_" + (i + 1) + ".png"));
            }

            acceptBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/accept_button.png"));
            rejectBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/reject_button.png"));
            menuBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/MenuButton.png"));

            settingsBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/Settings.png"));
            settingsWindow = ImageIO.read(getClass().getResourceAsStream("/Objects/SettingsWindow.png"));

            for (int i = 0; i < 8; i++) {
                    acceptCutscene[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/bad_Ending/BadEnding_" + (i + 1) + ".png"));
                    rejectCutscene[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/good_Ending/GoodEnding_" + (i + 1) + ".png"));
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        if (menuMusic != null) {
            menuMusic.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
            menuMusic.start();
        }
    }

    public void startDialogue(String[] dialogueList) {
        currentDialogueArray = dialogueList;
        currentDialogueListIndex = 0;
        fullDialogue = currentDialogueArray[0];
        currentDialogue = "";
        dialogueCharIndex = 0;
        isDialogueActive = true;
    }

    public BufferedImage getStatueImage(int state) {
        if (state == 0) return statueLeft;
        if (state == 1) return statueBackLeft;
        if (state == 2) return statueBackRight;
        if (state == 3) return statueRight;
        return null;
    }

    public void update() {

        if (currentMap == MAP_MAIN_MENU) {
            menuFrameCounter++;
            if (menuFrameCounter >= menuFrameSpeed) {
                menuFrameIndex = (menuFrameIndex + 1) % 4;
                menuFrameCounter = 0;
            }
            // Handle Play button click
            if (mouseH.leftClicked) {
                Rectangle mouseHitbox = new Rectangle(mouseH.mouseX, mouseH.mouseY, 1, 1);
                if (mouseHitbox.intersects(playButtonHitbox)) {
                    if (clickSFX != null) {
                        clickSFX.setFramePosition(0); // rewind to start
                        clickSFX.start();
                    }
                    if (menuMusic != null) {
                        menuMusic.stop();
                        menuMusic.close();
                    }
                    currentMap = MAP_ROOM; // ← your actual first game map
                    mouseH.leftClicked = false;
                } else if (mouseHitbox.intersects(exitButtonHitbox)) {
                    if (clickSFX != null) clickSFX.start();
                    System.exit(0); // This command closes the application!
                }
                mouseH.leftClicked = false;
            }
            return; // skip all other update logic while on menu
        }

        mapFrameCounter++;
        if (mapFrameCounter >= mapAnimSpeed) {
            mapFrameIndex = (mapFrameIndex == 0) ? 1 : 0; // Flip back and forth between 0 and 1
            mapFrameCounter = 0;
        }

        if (currentMap == MAP_ROOM && !introDialogueTriggered) {
            startTimer++;
            // 120 frames at 60 FPS = 2 seconds
            if (startTimer >= 120) {
                // --- NEW: Use startDialogue instead of manually setting it ---
                startDialogue(roomDialogue);
                introDialogueTriggered = true;
            }
        }

        // --- NEW: Typewriter Animation Logic ---
        if (isDialogueActive) {
            if (dialogueCharIndex < fullDialogue.length()) {
                typewriterCounter++;
                if (typewriterCounter >= typewriterSpeed) {
                    currentDialogue += fullDialogue.charAt(dialogueCharIndex);
                    dialogueCharIndex++;
                    typewriterCounter = 0;
                }
            }
        }

        if (introPuzzleOpen && introPuzzlePage == 3) {
            gibberishCounter++;
            if (gibberishCounter >= gibberishSpeed) {
                gibberishFrameIndex++;
                if (gibberishFrameIndex >= 10) {
                    gibberishFrameIndex = 0; // Loop back to the first frame
                }
                gibberishCounter = 0; // Reset timer
            }
        }

        // --- NEW: Event State Machine ---
        if (houseEventState == 2) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second (60 frames)
                demonVisible = true;
                houseEventState = 3;
                eventTimer = 0;
            }
        } else if (houseEventState == 3) {
            eventTimer++;
            if (eventTimer >= 120) { // 2 seconds
                houseEventState = 4;
                startDialogue(houseDialogue2);
                eventTimer = 0;
            }
        } else if (houseEventState == 5) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second
                introPuzzleOpen = true;
                houseEventState = 6;
                eventTimer = 0;
            }
        } else if (houseEventState == 8) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second
                currentQuest = 6; // Trigger End Screen
                houseEventState = 9; // Finish state machine
                eventTimer = 0;
            }
        }

        // --- Demon Animation ---
        if (demonVisible) {
            demonCounter++;
            if (demonCounter >= demonSpeed) {
                demonFrameIndex = (demonFrameIndex + 1) % 2;
                demonCounter = 0;
            }
        }

        // demon vanishes
        if (isDialogueActive && currentDialogueArray == houseDialogue3) {
//            if (currentDialogueListIndex == 4 && demonVisible) {
//                // Start the timer ONLY after the dramatic text finishes typing out
//                if (dialogueCharIndex >= fullDialogue.length()) {
//                    demonVanishTimer++;
//                    if (demonVanishTimer >= 60) { // 1 second at 60 FPS
//                        demonVisible = false;
//                    }
//                }
//            } else if (currentDialogueListIndex >= 5) {
//                // Failsafe: if the player clicks 'Next' before the 1 second is up, force vanish!
//                demonVisible = false;
//            }
        } else if (houseEventState == 0) {
            // Reset the timer when the game restarts so it works on future playthroughs
            demonVanishTimer = 0;
        }

        if (houseEventState == 10) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second after line 6
                showEndingGibberish = true;
                endingGibberishIndex = 0;
                endingGibberishTimer = 0;
                houseEventState = 11; // Waiting for player to close gibberish
                eventTimer = 0;
            }
        } else if (houseEventState == 13) {
            eventTimer++;
            if (eventTimer >= 60) { // 1 second after line 9
                showChoiceScreen = true;
                houseEventState = 14; // Waiting for choice
                eventTimer = 0;
            }
        }

        // --- NEW: Ending Gibberish Animation (0.5s per frame, stops at 11) ---
        if (showEndingGibberish) {
            endingGibberishTimer++;
            if (endingGibberishTimer >= 30) { // 0.5s at 60 FPS
                if (endingGibberishIndex < 10) { // Max index is 10 (Gibberish_11)
                    endingGibberishIndex++;
                }
                endingGibberishTimer = 0;
            }
        }

        // --- NEW: Reject Button Hover Logic ---
        if (showChoiceScreen) {
            Rectangle mouseHitbox = new Rectangle(mouseH.mouseX, mouseH.mouseY, 1, 1);
            if (rejectHoverCount < 5 && mouseHitbox.intersects(rejectHitbox)) { // <-- Increased limit to 5
                rejectHoverCount++;
                // Move the button to predefined random locations
                if (rejectHoverCount == 1) {
                    rejectHitbox.x = 100; rejectHitbox.y = 150;
                } else if (rejectHoverCount == 2) {
                    rejectHitbox.x = 600; rejectHitbox.y = 450;
                } else if (rejectHoverCount == 3) {
                    rejectHitbox.x = 200; rejectHitbox.y = 500; // New jump spot
                } else if (rejectHoverCount == 4) {
                    rejectHitbox.x = 650; rejectHitbox.y = 100; // Another jump spot
                } else if (rejectHoverCount == 5) {
                    rejectHitbox.x = 480; rejectHitbox.y = 300; // Returns to a clickable spot
                }
            }
        }



        // --- NEW: Cutscene & Ending Typewriter Logic ---
        if (playingAcceptCutscene || playingRejectCutscene) {
            // Cutscene Frame Timer (1 second per frame = 60 frames)
            if (cutsceneFrameIndex < 7) {
                cutsceneTimer++;
                if (cutsceneTimer >= 60) {
                    cutsceneFrameIndex++;
                    cutsceneTimer = 0;
                }
            } else {
                // On the 8th frame (index 7), freeze and show text
                showTheEndText = true;
            }

            // "the end." Typewriter (0.5s per letter = 30 frames)
            if (showTheEndText && theEndCharIndex < targetTheEndText.length()) {
                theEndTimer++;
                if (theEndTimer >= 30) {
                    currentTheEndText += targetTheEndText.charAt(theEndCharIndex);
                    theEndCharIndex++;
                    theEndTimer = 0;
                }
            } else if (showTheEndText && theEndCharIndex >= targetTheEndText.length()) {
                showMenuButton = true;
            }
        }

        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (currentMap == MAP_MAIN_MENU) {
            if (menuFrames[menuFrameIndex] != null) {
                g2.drawImage(menuFrames[menuFrameIndex], 0, 0, screenWidth, screenHeight, null);
            }
            // Optional: debug hitbox for the Play button
            // g2.setColor(new Color(255, 255, 0, 100));
            // g2.fillRect(playButtonHitbox.x, playButtonHitbox.y, playButtonHitbox.width, playButtonHitbox.height);
            g2.dispose();
            return; // skip drawing everything else
        }

        if (currentMap == MAP_ROOM && roomBg != null) {
            g2.drawImage(roomBg, 0, 0, screenWidth, screenHeight, null);

            // draw room barriers
            g2.setColor(new Color(0, 0, 255, 100)); // Blue
            for (Rectangle wall : roomWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }

        } else if (currentMap == MAP_HOUSE && houseBg != null) {
            g2.drawImage(houseBg, 0, 0, screenWidth, screenHeight, null);

            if (demonVisible) {
                BufferedImage currentDemon = (demonFrameIndex == 0) ? demon1 : demon2;
                if (currentDemon != null) {
                    g2.drawImage(currentDemon, 330, 230, tileSize * 2, tileSize * 2, null);
                }
            }

            // Draw Debug Hitbox (Top Right)
            g2.setColor(new Color(255, 0, 0, 100)); // Red
            g2.fillRect(debugIntroHitbox.x, debugIntroHitbox.y, debugIntroHitbox.width, debugIntroHitbox.height);

            // draw living room barriers
            g2.setColor(new Color(0, 0, 255, 100)); // Blue
            for (Rectangle wall : houseWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }

            // debug hitbox for temporary button
            g2.setColor(new Color(0, 0, 255, 100)); // Blue
            g2.fillRect(tempBtnHitbox.x, tempBtnHitbox.y, tempBtnHitbox.width, tempBtnHitbox.height);
        } else if (currentMap == MAP_STREET) {
            BufferedImage currentStreet = (mapFrameIndex == 0) ? streetBg1 : streetBg2;
            if (currentStreet != null) g2.drawImage(currentStreet, 0, 0, screenWidth, screenHeight, null);

            g2.setColor(new Color(0, 0, 255, 100)); // Blue
            for (Rectangle wall : streetWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (currentMap == MAP_WORKSHOP) {
            BufferedImage currentWorkshop = (mapFrameIndex == 0) ? workshopBg1 : workshopBg2;
            if (currentWorkshop != null) g2.drawImage(currentWorkshop, 0, 0, screenWidth, screenHeight, null);
            //scatter the glass eyes in workshop
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].name.equals("Glass Eye")) {
                    obj[i].draw(g2, this);
                    //visible hitboxes for debugging
                    g2.setColor(new Color(255, 255, 0, 150));
                    g2.fillRect(obj[i].hitbox.x, obj[i].hitbox.y, obj[i].hitbox.width, obj[i].hitbox.height);
                }
            }

            g2.setColor(new Color(0, 0, 255, 100));
            for (Rectangle wall : workshopWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (currentMap == MAP_GREENHOUSE) {
            BufferedImage currentGreenhouse = (mapFrameIndex == 0) ? greenhouseBg1 : greenhouseBg2;
            if (currentGreenhouse != null) g2.drawImage(currentGreenhouse, 0, 0, screenWidth, screenHeight, null);
            // scatter the roses in greenhoues
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].name.equals("Black Rose")) {
                    obj[i].draw(g2, this);
                    //visible hitboxes for debugging
                    g2.setColor(new Color(255, 255, 0, 150));
                    g2.fillRect(obj[i].hitbox.x, obj[i].hitbox.y, obj[i].hitbox.width, obj[i].hitbox.height);
                }
            }

            g2.setColor(new Color(0, 0, 255, 100));
            for (Rectangle wall : greenhouseWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        } else if (currentMap == MAP_MUSEUM && museumBg != null) {
            g2.drawImage(museumBg, 0, 0, screenWidth, screenHeight, null);
            if (clue1 != null) g2.drawImage(clue1, 290, 280, tileSize - 7, tileSize - 6, null);
            if (clue2 != null) g2.drawImage(clue2, 510, 437, tileSize - 7, tileSize - 6, null);
            if (clue3 != null) g2.drawImage(clue3, 705, 320, tileSize - 10, tileSize - 9, null);
            if (clue0 != null) g2.drawImage(clue0, 230, 538, tileSize - 7 , tileSize - 6, null);


            // clue hitboxes
            g2.setColor(new Color(0, 0, 255, 100)); // Blue debug boxes
            g2.fillRect(clue1Hitbox.x, clue1Hitbox.y, clue1Hitbox.width, clue1Hitbox.height);
            g2.fillRect(clue2Hitbox.x, clue2Hitbox.y, clue2Hitbox.width, clue2Hitbox.height);
            g2.fillRect(clue3Hitbox.x, clue3Hitbox.y, clue3Hitbox.width, clue3Hitbox.height);
            g2.fillRect(clue0Hitbox.x, clue0Hitbox.y, clue0Hitbox.width, clue0Hitbox.height);
            // statue hitboxes
            g2.setColor(new Color(255, 100, 0, 100)); // Orange
            g2.fillRect(mapStatue1Hitbox.x, mapStatue1Hitbox.y, mapStatue1Hitbox.width, mapStatue1Hitbox.height);
            g2.fillRect(mapStatue2Hitbox.x, mapStatue2Hitbox.y, mapStatue2Hitbox.width, mapStatue2Hitbox.height);
            g2.fillRect(mapStatue3Hitbox.x, mapStatue3Hitbox.y, mapStatue3Hitbox.width, mapStatue3Hitbox.height);


            g2.drawImage(getStatueImage(statue1State), 560, 250, 70, 100, null);

            g2.drawImage(getStatueImage(statue2State), 630, 340, 70, 100, null);

//            g2.drawImage(glasscaseMuseum, 647, 385, tileSize * 2, tileSize * 4, null);

            g2.drawImage(getStatueImage(statue3State), 670, 228, 70, 100, null);

            if (locketUnlocked == false) {
                if (lockedCase != null) g2.drawImage(lockedCase, 170, 280, tileSize + 2, tileSize + 2, null);

                // for debug locked glass case
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fillRect(glassCaseHitbox.x, glassCaseHitbox.y, glassCaseHitbox.width, glassCaseHitbox.height);
            } else {
                // replace locked glass case with unlocked glass case* 2,
                if (unlockedCase != null) g2.drawImage(unlockedCase, 150, 280, tileSize + 2, tileSize + 2, null);
            }

            g2.setColor(new Color(0, 0, 255, 100));
            for (Rectangle wall : museumWalls) {
                g2.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        }


        // temporary: visible door hitboxes for debugging nyahahhaa
        g2.setColor(new Color(255, 0, 0, 100));
        if (currentMap == MAP_HOUSE) {
            g2.fillRect(houseDoorHitbox.x, houseDoorHitbox.y, houseDoorHitbox.width, houseDoorHitbox.height);
            g2.fillRect(outsideBedroomDoorHitbox.x, outsideBedroomDoorHitbox.y, outsideBedroomDoorHitbox.width, outsideBedroomDoorHitbox.height);
        } else if (currentMap == MAP_STREET) {
            g2.fillRect(streetHouseDoorHitbox.x, streetHouseDoorHitbox.y, streetHouseDoorHitbox.width, streetHouseDoorHitbox.height);
            g2.fillRect(streetMuseumDoorHitbox.x, streetMuseumDoorHitbox.y, streetMuseumDoorHitbox.width, streetMuseumDoorHitbox.height);
            g2.fillRect(streetWorkshopDoorHitbox.x, streetWorkshopDoorHitbox.y, streetWorkshopDoorHitbox.width, streetWorkshopDoorHitbox.height);
            g2.fillRect(streetGreenhouseDoorHitbox.x, streetGreenhouseDoorHitbox.y, streetGreenhouseDoorHitbox.width, streetGreenhouseDoorHitbox.height);
        } else if (currentMap == MAP_WORKSHOP) {
            g2.fillRect(workshopDoorHitbox.x, workshopDoorHitbox.y, workshopDoorHitbox.width, workshopDoorHitbox.height);
            g2.drawImage(toolboxWorkshop, 113, 263, 37, 23, null);
            g2.drawImage(sofaWorkshop, 509, 369, 88, 190, null);
        } else if (currentMap == MAP_GREENHOUSE) {
            g2.fillRect(greenhouseDoorHitbox.x, greenhouseDoorHitbox.y, greenhouseDoorHitbox.width, greenhouseDoorHitbox.height);
        } else if (currentMap == MAP_MUSEUM) {
            // player position behind glass case in museum
            player.draw(g2);
            g2.drawImage(glasscaseMuseum, 647, 385, tileSize * 2, tileSize * 4, null);
            g2.fillRect(museumDoorHitbox.x, museumDoorHitbox.y, museumDoorHitbox.width, museumDoorHitbox.height);
        } else if (currentMap == MAP_ROOM) {
            g2.fillRect(bedroomDoorHitbox.x, bedroomDoorHitbox.y, bedroomDoorHitbox.width, bedroomDoorHitbox.height);
        }
        // set player layer position back to normal after leaving the museum
        if (currentMap != MAP_MUSEUM) {
            player.draw(g2);
        }

        if (player.glassEyesCollected >= 5 && inventoryBox != null) {
            // These coordinates create a bounding box perfectly sized behind all 4 item slots
            g2.drawImage(inventoryBox, 10, 290, tileSize + 20, (tileSize * 4) + 50, null);
        }

        // bouquet appears after all 5 roses are collected
        if (player.blackRosesCollected >= 5 && bouquetInv != null) {
            g2.drawImage(bouquetInv, 20, 300, tileSize, tileSize, null);
        }
        // jar appears after all 5 glass eyes are collected
        if (player.glassEyesCollected >= 5 && jarInv != null) {
            g2.drawImage(jarInv, 20, 300 + tileSize + 10, tileSize, tileSize, null);
        }

        if (locketUnlocked == true && locketInv != null) {
            // Drawn below the jar of eyes
            g2.drawImage(locketInv, 20, 300 + (tileSize * 2) + 20, tileSize, tileSize, null);
        }

        if (chronosWatchUnlocked == true && watchInv != null) {
            // Drawn below the locket (Notice it's tileSize*3 and +30 to keep the exact same spacing!)
            g2.drawImage(watchInv, 20, 300 + (tileSize * 3) + 30, tileSize, tileSize, null);
        }


        if (passwordUIOpen == true) {
            // low opacity black bg to dim background
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // Draw the UI centered (adjust coordinates if needed)
            int uiX = screenWidth / 2 - 250;
            int uiY = screenHeight / 2 - 200;
            if (passwordUI != null) g2.drawImage(passwordUI, uiX, uiY, 500, 400, null);
            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            //  typed text inside the white box
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            g2.setColor(Color.WHITE);

            // adjust text input box
            int textX = uiX + 100;
            int textY = uiY + 260;
            g2.drawString(keyH.currentInput, textX, textY);

            g2.setColor(Color.MAGENTA);
            int[] xPoints = {textX, textX - 15, textX + 15}; // The 3 X coordinates of the triangle
            int[] yPoints = {textY, textY + 20, textY + 20}; // The 3 Y coordinates of the triangle
            g2.fillPolygon(xPoints, yPoints, 3);

            // debug hitboxes
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(submitButtonHitbox.x, submitButtonHitbox.y, submitButtonHitbox.width, submitButtonHitbox.height);
            g2.fillRect(backButtonHitbox.x, backButtonHitbox.y, backButtonHitbox.width, backButtonHitbox.height);
        }

        if (clue1_Open || clue2_Open || clue3_Open || clue0_Open) {
            // low opacity bg
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            int uiX = screenWidth / 2 - 250;
            int uiY = screenHeight / 2 - 200;

            if (clue1_Open && openClue1 != null) g2.drawImage(openClue1, uiX + 50, uiY + 70, 440, 220, null);
            if (clue2_Open && openClue2 != null) g2.drawImage(openClue2, uiX + 110, uiY + 70, 270, 295, null);
            if (clue3_Open && openClue3 != null) g2.drawImage(openClue3, uiX + 150, uiY + 70, 200, 330, null);
            if (clue0_Open && openClue0 != null) g2.drawImage(openClue0, uiX + 50, uiY + 10, 420, 480, null);

            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            // back button hitbox
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(backButtonHitbox.x, backButtonHitbox.y, backButtonHitbox.width, backButtonHitbox.height);
        }

        if (statue_Open == true) {

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // bg screen
//                if (statueRotateScreen != null) {
//                    g2.drawImage(statueRotateScreen, screenWidth / 2 - 350, screenHeight / 2 - 250, 700, 500, null);
//                }

            // draw 3 statues
            g2.drawImage(getStatueImage(statue1State), uiStatue1Hitbox.x, uiStatue1Hitbox.y, uiStatue1Hitbox.width, uiStatue1Hitbox.height, null);
            g2.drawImage(getStatueImage(statue2State), uiStatue2Hitbox.x, uiStatue2Hitbox.y, uiStatue2Hitbox.width, uiStatue2Hitbox.height, null);
            g2.drawImage(getStatueImage(statue3State), uiStatue3Hitbox.x, uiStatue3Hitbox.y, uiStatue3Hitbox.width, uiStatue3Hitbox.height, null);

            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            // statue hitboxes
            g2.setColor(new Color(0, 255, 0, 100)); // Green
            g2.fillRect(uiStatue1Hitbox.x, uiStatue1Hitbox.y, uiStatue1Hitbox.width, uiStatue1Hitbox.height);
            g2.fillRect(uiStatue2Hitbox.x, uiStatue2Hitbox.y, uiStatue2Hitbox.width, uiStatue2Hitbox.height);
            g2.fillRect(uiStatue3Hitbox.x, uiStatue3Hitbox.y, uiStatue3Hitbox.width, uiStatue3Hitbox.height);
        }

        if (introPuzzleOpen == true) {
            // dim bg
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // bg screen
//            int uiX = screenWidth/2 - 250;
//            int uiY = screenHeight/2 - 300;
//            if (listScreen1 != null) g2.drawImage(listScreen1, uiX, uiY, 500, 600, null);
            if (introPuzzlePage == 1 && listScreen1 != null) {
                g2.drawImage(listScreen1, 230, 100, 420, 480, null);

                // Row 1 (Desc 1): Jar, Bouquet, Watch
//                    if (jarInv != null)
//                        g2.drawImage(jarInv, r1c1Hitbox.x, r1c1Hitbox.y, r1c1Hitbox.width, r1c1Hitbox.height, null);
//                    if (bouquetInv != null)
//                        g2.drawImage(bouquetInv, r1c2Hitbox.x, r1c2Hitbox.y, r1c2Hitbox.width, r1c2Hitbox.height, null);
//                    if (watchInv != null)
//                        g2.drawImage(watchInv, r1c3Hitbox.x, r1c3Hitbox.y, r1c3Hitbox.width, r1c3Hitbox.height, null);

                // Row 2 (Desc 2): Locket, Watch, Bouquet
//                    if (locketInv != null)
//                        g2.drawImage(locketInv, r2c1Hitbox.x, r2c1Hitbox.y, r2c1Hitbox.width, r2c1Hitbox.height, null);
//                    if (watchInv != null)
//                        g2.drawImage(watchInv, r2c2Hitbox.x, r2c2Hitbox.y, r2c2Hitbox.width, r2c2Hitbox.height, null);
//                    if (bouquetInv != null)
//                        g2.drawImage(bouquetInv, r2c3Hitbox.x, r2c3Hitbox.y, r2c3Hitbox.width, r2c3Hitbox.height, null);

                // Navigation (Only Next on Page 1)
                if (nextBtn != null)
                    g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }
            // PAGE 2
            else if (introPuzzlePage == 2 && listScreen2 != null) {
                g2.drawImage(listScreen2, 230, 100, 420, 480, null);

                // Row 1 (Desc 3): Watch, Locket, Bouquet
//                    if (watchInv != null)
//                        g2.drawImage(watchInv, r1c1Hitbox.x, r1c1Hitbox.y, r1c1Hitbox.width, r1c1Hitbox.height, null);
//                    if (locketInv != null)
//                        g2.drawImage(locketInv, r1c2Hitbox.x, r1c2Hitbox.y, r1c2Hitbox.width, r1c2Hitbox.height, null);
//                    if (bouquetInv != null)
//                        g2.drawImage(bouquetInv, r1c3Hitbox.x, r1c3Hitbox.y, r1c3Hitbox.width, r1c3Hitbox.height, null);

                // Row 2 (Desc 4): Jar, Bouquet, Locket
//                    if (jarInv != null)
//                        g2.drawImage(jarInv, r2c1Hitbox.x, r2c1Hitbox.y, r2c1Hitbox.width, r2c1Hitbox.height, null);
//                    if (bouquetInv != null)
//                        g2.drawImage(bouquetInv, r2c2Hitbox.x, r2c2Hitbox.y, r2c2Hitbox.width, r2c2Hitbox.height, null);
//                    if (locketInv != null)
//                        g2.drawImage(locketInv, r2c3Hitbox.x, r2c3Hitbox.y, r2c3Hitbox.width, r2c3Hitbox.height, null);

                // Navigation (Both on Page 2)
                if (prevBtn != null)
                    g2.drawImage(prevBtn, prevButtonHitbox.x, prevButtonHitbox.y, prevButtonHitbox.width, prevButtonHitbox.height, null);
                if (nextBtn != null)
                    g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }
            // PAGE 3
            else if (introPuzzlePage == 3 && gibberishFrames[gibberishFrameIndex] != null) {

                // Draw the current animated frame
                g2.drawImage(gibberishFrames[gibberishFrameIndex], 230, 100, 420, 480, null);

                // Navigation (Both on Page 3)
                if (prevBtn != null)
                    g2.drawImage(prevBtn, prevButtonHitbox.x, prevButtonHitbox.y, prevButtonHitbox.width, prevButtonHitbox.height, null);
                if (nextBtn != null)
                    g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }

//            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            // debug hitboxes
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(r1c1Hitbox.x, r1c1Hitbox.y, r1c1Hitbox.width, r1c1Hitbox.height);
            g2.fillRect(r1c2Hitbox.x, r1c2Hitbox.y, r1c2Hitbox.width, r1c2Hitbox.height);
            g2.fillRect(r1c3Hitbox.x, r1c3Hitbox.y, r1c3Hitbox.width, r1c3Hitbox.height);
            g2.fillRect(r2c1Hitbox.x, r2c1Hitbox.y, r2c1Hitbox.width, r2c1Hitbox.height);
            g2.fillRect(r2c2Hitbox.x, r2c2Hitbox.y, r2c2Hitbox.width, r2c2Hitbox.height);
            g2.fillRect(r2c3Hitbox.x, r2c3Hitbox.y, r2c3Hitbox.width, r2c3Hitbox.height);
            g2.fillRect(nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height);
        }

        if (currentQuest >= 1 && currentQuest <= 5) {
            int tabX = screenWidth - 230; // Top right corner
            int tabY = 30;

            if (currentQuest == 1 && objTab1 != null) g2.drawImage(objTab1, tabX, tabY, 200, 120, null);
            else if (currentQuest == 2 && objTab2 != null) g2.drawImage(objTab2, tabX, tabY, 200, 120, null);
            else if (currentQuest == 3 && objTab3 != null) g2.drawImage(objTab3, tabX, tabY, 200, 120, null);
            else if (currentQuest == 4 && objTab4 != null) g2.drawImage(objTab4, tabX, tabY, 200, 120, null);
            else if (currentQuest == 5 && objTab5 != null) g2.drawImage(objTab5, tabX, tabY, 200, 120, null);
        }

        // --- NEW: DRAW END SCREEN ---
        if (currentQuest == 6) {
            // Dim background heavily
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // Draw End Screen and Again Button
            if (endScreen != null)
                g2.drawImage(endScreen, screenWidth / 2 - 250, screenHeight / 2 - 150, 500, 200, null);
            if (againBtn != null)
                g2.drawImage(againBtn, againBtnHitbox.x, againBtnHitbox.y, againBtnHitbox.width, againBtnHitbox.height, null);

            // Debug Hitbox for Again Button
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(againBtnHitbox.x, againBtnHitbox.y, againBtnHitbox.width, againBtnHitbox.height);
        }


        // DRAW DIALOGUE BOX ---
        // --- UPDATED: DRAW DIALOGUE BOX ---
        if (isDialogueActive) {
            g2.setColor(new Color(40, 40, 40, 220));
            g2.fillRect(0, screenHeight - 185, 864, 185);

            // Text Styling (Default)
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 22));

            // Special Styling for round 10 of Dialogue 2
            if ((currentDialogueArray == houseDialogue2 && currentDialogueListIndex == 9) ||
                    (currentDialogueArray == houseDialogue3 && (currentDialogueListIndex == 4 || currentDialogueListIndex == 8))) {
                g2.setColor(new Color(220, 50, 50)); // Red
                g2.setFont(new Font("Arial", Font.BOLD, 24));
            }

            int textX = 40;
            int textY = screenHeight - 130;

            for (String line : currentDialogue.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += g2.getFontMetrics().getHeight() + 8;
            }

            if (nextBtn != null) {
                g2.drawImage(nextBtn, dialogueNextHitbox.x, dialogueNextHitbox.y, dialogueNextHitbox.width, dialogueNextHitbox.height, null);
            }
        }

        if (showEndingGibberish) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            if (endingGibberishFrames[endingGibberishIndex] != null) {
                g2.drawImage(endingGibberishFrames[endingGibberishIndex], 230, 100, 420, 480, null);
            }
            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);
        }

        // --- NEW: Draw Choice Screen (Prepared) ---
        if (showChoiceScreen) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            if (acceptBtn != null) g2.drawImage(acceptBtn, acceptHitbox.x, acceptHitbox.y, acceptHitbox.width, acceptHitbox.height, null);
            if (rejectBtn != null) g2.drawImage(rejectBtn, rejectHitbox.x, rejectHitbox.y, rejectHitbox.width, rejectHitbox.height, null);
        }

        if (currentMap != MAP_MAIN_MENU && !playingAcceptCutscene && !playingRejectCutscene && !showTheEndText) {

            // Draw the eye icon in the top left
            if (settingsBtn != null) {
                g2.drawImage(settingsBtn, settingsBtnHitbox.x, settingsBtnHitbox.y, settingsBtnHitbox.width, settingsBtnHitbox.height, null);
            }

            // Draw the pop-up window if opened
            if (isSettingsOpen) {
                // Dim the background
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, screenWidth, screenHeight);

                // Draw the window (Centered 400x300)
                if (settingsWindow != null) {
                    g2.drawImage(settingsWindow, 232, 186, 400, 300, null);
                }

                // Debug hitboxes (Uncomment these to see where the buttons actually are so you can adjust the coordinates!)
                 g2.setColor(new Color(255, 255, 0, 100));
                 g2.fillRect(resumeHitbox.x, resumeHitbox.y, resumeHitbox.width, resumeHitbox.height);
                 g2.fillRect(exitMenuHitbox.x, exitMenuHitbox.y, exitMenuHitbox.width, exitMenuHitbox.height);
            }
        }

        if (playingAcceptCutscene || playingRejectCutscene) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight); // Black background

            BufferedImage currentFrame = null;
            if (playingAcceptCutscene && acceptCutscene[cutsceneFrameIndex] != null) {
                currentFrame = acceptCutscene[cutsceneFrameIndex];
            } else if (playingRejectCutscene && rejectCutscene[cutsceneFrameIndex] != null) {
                currentFrame = rejectCutscene[cutsceneFrameIndex];
            }

            if (currentFrame != null) {
                g2.drawImage(currentFrame, 0, 0, screenWidth, screenHeight, null);
            }

            if (showTheEndText) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 48));
                // Center the text
                int textX = screenWidth / 2 + 100;
                int textY = screenHeight / 2;
                g2.drawString(currentTheEndText, textX, textY);
            }

            if (showMenuButton && menuBtn != null) {
                g2.drawImage(menuBtn, menuBtnHitbox.x, menuBtnHitbox.y, menuBtnHitbox.width, menuBtnHitbox.height, null);
            }
        }

            g2.dispose();
        }
    }
