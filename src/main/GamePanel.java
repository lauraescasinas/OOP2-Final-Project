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
    public int currentMap = MAP_HOUSE; // start in the house


    BufferedImage houseBg;
    BufferedImage streetBg;
    BufferedImage workshopBg;
    BufferedImage greenhouseBg;
    BufferedImage museumBg;
    BufferedImage bouquetInv;
    BufferedImage jarInv;
    BufferedImage lockedCase, clue1, clue2, clue3, passwordUI, backBtn, locketInv, unlockedCase, watchInv;
    BufferedImage openClue1, openClue2, openClue3;
    BufferedImage statueRotateScreen, statueLeft, statueBackLeft, statueBackRight, statueRight;
    BufferedImage tempBtn, listScreen1, listScreen2, listScreen3, nextBtn, prevBtn;
    BufferedImage objTab1, objTab2, objTab3, objTab4, objTab5;
    BufferedImage endScreen, againBtn;

    public boolean passwordUIOpen = false;
    public boolean locketUnlocked = false;
    public boolean clue1_Open = false;
    public boolean clue2_Open = false;
    public boolean clue3_Open = false;
    public boolean statue_Open = false;
    public boolean introPuzzleOpen = false;
    public boolean chronosWatchUnlocked = false;
    public boolean introAns1 = false, introAns2 = false, introAns3 = false, introAns4 = false;

    public int statue1State = 0; // default state: left
    public int statue2State = 3; // default state: right
    public int statue3State = 2; // default state:  back_right
    public int introPuzzlePage = 1;
    public int currentQuest = 0;


    // hitboxs
    public Rectangle glassCaseHitbox = new Rectangle(150, 250, 100, 150);
    public Rectangle backButtonHitbox = new Rectangle(50, 50, 60, 60);
    public Rectangle submitButtonHitbox = new Rectangle(560, 400, 60, 60);
    // clue hitboxes
    public Rectangle clue1Hitbox = new Rectangle(100, 400, 48, 48);
    public Rectangle clue2Hitbox = new Rectangle(300, 350, 48, 48);
    public Rectangle clue3Hitbox = new Rectangle(250, 500, 48, 48);
    // statue in map hitboxes
    public Rectangle mapStatue1Hitbox = new Rectangle(550, 350, 60, 100);
    public Rectangle mapStatue2Hitbox = new Rectangle(650, 450, 60, 100);
    public Rectangle mapStatue3Hitbox = new Rectangle(750, 350, 60, 100);
    // statue in screen hitboxes
    public Rectangle uiStatue1Hitbox = new Rectangle(120, 250, 150, 200);
    public Rectangle uiStatue2Hitbox = new Rectangle(350, 250, 150, 200);
    public Rectangle uiStatue3Hitbox = new Rectangle(580, 250, 150, 200);

    // temporary button hitbox
    public Rectangle tempBtnHitbox = new Rectangle(550, 300, 48, 48);
    // button hitboxes
    public Rectangle nextButtonHitbox = new Rectangle(650, 350, 60, 80);
    public Rectangle prevButtonHitbox = new Rectangle(50, 350, 60, 80);
    // list screen1 row1
    public Rectangle r1c1Hitbox = new Rectangle(280, 250, 60, 60); // Jar
    public Rectangle r1c2Hitbox = new Rectangle(400, 250, 60, 60); // Bouquet
    public Rectangle r1c3Hitbox = new Rectangle(520, 250, 60, 60); // Watch
    //list screen1 row2
    public Rectangle r2c1Hitbox = new Rectangle(280, 420, 60, 60); // Locket
    public Rectangle r2c2Hitbox = new Rectangle(400, 420, 60, 60); // Watch
    public Rectangle r2c3Hitbox = new Rectangle(520, 420, 60, 60); // Bouquet

    // again button -- TEMPORARY!
    public Rectangle againBtnHitbox = new Rectangle(screenWidth/2 - 80, screenHeight/2 + 80, 160, 80);

    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    public MouseHandler mouseH = new MouseHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    public SuperObject obj[] = new SuperObject[10];

    // inside doors
    public Rectangle houseDoorHitbox = new Rectangle(350, 520, 164, 100);
    public Rectangle workshopDoorHitbox = new Rectangle(350, 520, 164, 100);
    public Rectangle greenhouseDoorHitbox = new Rectangle(350, 520, 164, 100);
    public Rectangle museumDoorHitbox = new Rectangle(350, 520, 164, 100);

    // outside doors
    public Rectangle streetHouseDoorHitbox = new Rectangle(230, 170, 85, 45);
    public Rectangle streetMuseumDoorHitbox = new Rectangle(550, 170, 85, 45);
    public Rectangle streetWorkshopDoorHitbox = new Rectangle(230, 480, 85, 45);
    public Rectangle streetGreenhouseDoorHitbox = new Rectangle(550, 480, 85, 45);

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.setFocusable(true);
        loadBackgrounds();
    }

    public void setupGame(){
        // blackroses in greenhouse
        obj[0] = new BlackroseObject();
        obj[0].x = 150; obj[0].y = 450;
        obj[0].hitbox.x = 150; obj[0].hitbox.y = 450;

        obj[1] = new BlackroseObject();
        obj[1].x = 250; obj[1].y = 300;
        obj[1].hitbox.x = 250; obj[1].hitbox.y = 300;

        obj[2] = new BlackroseObject();
        obj[2].x = 600; obj[2].y = 300;
        obj[2].hitbox.x = 600; obj[2].hitbox.y = 300;

        obj[3] = new BlackroseObject();
        obj[3].x = 700; obj[3].y = 400;
        obj[3].hitbox.x = 700; obj[3].hitbox.y = 400;

        obj[4] = new BlackroseObject();
        obj[4].x = 650; obj[4].y = 520;
        obj[4].hitbox.x = 650; obj[4].hitbox.y = 520;

        // glass eyes in workshop
        obj[5] = new GlasseyeObject();
        obj[5].x = 100; obj[5].y = 200;
        obj[5].hitbox.x = 100; obj[5].hitbox.y = 200;

        obj[6] = new GlasseyeObject();
        obj[6].x = 200; obj[6].y = 450;
        obj[6].hitbox.x = 200; obj[6].hitbox.y = 450;

        obj[7] = new GlasseyeObject();
        obj[7].x = 550; obj[7].y = 150;
        obj[7].hitbox.x = 550; obj[7].hitbox.y = 150;

        obj[8] = new GlasseyeObject();
        obj[8].x = 650; obj[8].y = 350;
        obj[8].hitbox.x = 650; obj[8].hitbox.y = 350;

        obj[9] = new GlasseyeObject();
        obj[9].x = 750; obj[9].y = 500;
        obj[9].hitbox.x = 750; obj[9].hitbox.y = 500;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // backgrounds
    public void loadBackgrounds() {
        try {
            houseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/House_bg.png"));
            streetBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg.png"));
            workshopBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg.png"));
            greenhouseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg.png"));
            museumBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Museum_bg.png"));

            // for intro list
            tempBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/temporary_button.png"));
            listScreen1 = ImageIO.read(getClass().getResourceAsStream("/Objects/list_screen1.png"));
            nextBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/next_button.png"));


            // bouquet roses, glass eyes, locket, watch in inventory once collected || quest items loaded
            bouquetInv = ImageIO.read(getClass().getResourceAsStream("/Objects/bouquet_roses.png"));
            jarInv = ImageIO.read(getClass().getResourceAsStream("/Objects/jar_eyes.png"));
            locketInv = ImageIO.read(getClass().getResourceAsStream("/Objects/memento_locket.png"));
            watchInv = ImageIO.read(getClass().getResourceAsStream("/Objects/chronos_watch.png"));


            lockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/locked_GlassCase.png"));
            passwordUI = ImageIO.read(getClass().getResourceAsStream("/Objects/password_input.png"));
            backBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/back_button.png"));
            unlockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/unlocked_GlassCase.png"));

            // clues
            clue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue1.png"));
            clue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue2.png"));
            clue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/clue3.png"));
            openClue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue1.png"));
            openClue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue2.png"));
            openClue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/open_clue3.png"));

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getStatueImage(int state) {
        if (state == 0) return statueLeft;
        if (state == 1) return statueBackLeft;
        if (state == 2) return statueBackRight;
        if (state == 3) return statueRight;
        return null;
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (currentMap == MAP_HOUSE && houseBg != null) {
            g2.drawImage(houseBg, 0, 0, screenWidth, screenHeight, null);
            // temporary button
            if (tempBtn != null) g2.drawImage(tempBtn, tempBtnHitbox.x, tempBtnHitbox.y, tempBtnHitbox.width, tempBtnHitbox.height, null);

            // debug hitbox for temporary button
            g2.setColor(new Color(0, 0, 255, 100)); // Blue
            g2.fillRect(tempBtnHitbox.x, tempBtnHitbox.y, tempBtnHitbox.width, tempBtnHitbox.height);

        } else if (currentMap == MAP_STREET && streetBg != null) {
            g2.drawImage(streetBg, 0, 0, screenWidth, screenHeight, null);
        } else if (currentMap == MAP_WORKSHOP && workshopBg != null){
            g2.drawImage(workshopBg, 0, 0, screenWidth, screenHeight, null);
            //scatter the glass eyes in workshop
            for (int i = 0; i < obj.length; i++){
                if (obj[i] != null && obj[i].name.equals("Glass Eye")){
                    obj[i].draw(g2, this);
                    //visible hitboxes for debugging
                    g2.setColor(new Color(255, 255, 0, 150));
                    g2.fillRect(obj[i].hitbox.x, obj[i].hitbox.y, obj[i].hitbox.width, obj[i].hitbox.height);
                }
            }
        } else if (currentMap == MAP_GREENHOUSE && greenhouseBg != null){
            g2.drawImage(greenhouseBg, 0, 0, screenWidth, screenHeight, null);
            // scatter the roses in greenhoues
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].name.equals("Black Rose")) {
                    obj[i].draw(g2, this);
                    //visible hitboxes for debugging
                    g2.setColor(new Color(255, 255, 0, 150));
                    g2.fillRect(obj[i].hitbox.x, obj[i].hitbox.y, obj[i].hitbox.width, obj[i].hitbox.height);
                }
            }
        } else if (currentMap == MAP_MUSEUM && museumBg != null){
            g2.drawImage(museumBg, 0, 0, screenWidth, screenHeight, null);
            if (clue1 != null) g2.drawImage(clue1, 100, 400, tileSize, tileSize, null);
            if (clue2 != null) g2.drawImage(clue2, 300, 350, tileSize, tileSize, null);
            if (clue3 != null) g2.drawImage(clue3, 250, 500, tileSize, tileSize, null);

            // clue hitboxes
            g2.setColor(new Color(0, 0, 255, 100)); // Blue debug boxes
            g2.fillRect(clue1Hitbox.x, clue1Hitbox.y, clue1Hitbox.width, clue1Hitbox.height);
            g2.fillRect(clue2Hitbox.x, clue2Hitbox.y, clue2Hitbox.width, clue2Hitbox.height);
            g2.fillRect(clue3Hitbox.x, clue3Hitbox.y, clue3Hitbox.width, clue3Hitbox.height);

            // statue hitboxes
            g2.setColor(new Color(255, 100, 0, 100)); // Orange
            g2.fillRect(mapStatue1Hitbox.x, mapStatue1Hitbox.y, mapStatue1Hitbox.width, mapStatue1Hitbox.height);
            g2.fillRect(mapStatue2Hitbox.x, mapStatue2Hitbox.y, mapStatue2Hitbox.width, mapStatue2Hitbox.height);
            g2.fillRect(mapStatue3Hitbox.x, mapStatue3Hitbox.y, mapStatue3Hitbox.width, mapStatue3Hitbox.height);


            g2.drawImage(getStatueImage(statue1State), 550, 350, 80, 120, null);
            g2.drawImage(getStatueImage(statue2State), 650, 450, 80, 120, null);
            g2.drawImage(getStatueImage(statue3State), 750, 350, 80, 120, null);

            if (locketUnlocked == false) {
                if (lockedCase != null) g2.drawImage(lockedCase, 150, 250, tileSize*2, tileSize*3, null);

                // for debug locked glass case
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fillRect(glassCaseHitbox.x, glassCaseHitbox.y, glassCaseHitbox.width, glassCaseHitbox.height);
            } else {
                // replace locked glass case with unlocked glass case
                if (unlockedCase != null) g2.drawImage(unlockedCase, 150, 250, tileSize*2, tileSize*3, null);
            }
        }


        // temporary: visible door hitboxes for debugging nyahahhaa
        g2.setColor(new Color(255, 0, 0, 100));
        if (currentMap == MAP_HOUSE) {
            g2.fillRect(houseDoorHitbox.x, houseDoorHitbox.y, houseDoorHitbox.width, houseDoorHitbox.height);
        } else if (currentMap == MAP_STREET) {
            g2.fillRect(streetHouseDoorHitbox.x, streetHouseDoorHitbox.y, streetHouseDoorHitbox.width, streetHouseDoorHitbox.height);
            g2.fillRect(streetMuseumDoorHitbox.x, streetMuseumDoorHitbox.y, streetMuseumDoorHitbox.width, streetMuseumDoorHitbox.height);
            g2.fillRect(streetWorkshopDoorHitbox.x, streetWorkshopDoorHitbox.y, streetWorkshopDoorHitbox.width, streetWorkshopDoorHitbox.height);
            g2.fillRect(streetGreenhouseDoorHitbox.x, streetGreenhouseDoorHitbox.y, streetGreenhouseDoorHitbox.width, streetGreenhouseDoorHitbox.height);
        } else if(currentMap == MAP_WORKSHOP){
            g2.fillRect(workshopDoorHitbox.x, workshopDoorHitbox.y, workshopDoorHitbox.width, workshopDoorHitbox.height );
        } else if (currentMap == MAP_GREENHOUSE){
            g2.fillRect(greenhouseDoorHitbox.x, greenhouseDoorHitbox.y, greenhouseDoorHitbox.width, greenhouseDoorHitbox.height );
        } else if(currentMap == MAP_MUSEUM){
            g2.fillRect(museumDoorHitbox.x, museumDoorHitbox.y, museumDoorHitbox.width, museumDoorHitbox.height );
        }

        player.draw(g2);
        // bouquet appears after all 5 roses are collected
        if (player.blackRosesCollected >= 5 && bouquetInv != null){
            g2.drawImage(bouquetInv, 20, 300, tileSize, tileSize, null);
        }
        // jar appears after all 5 glass eyes are collected
        if (player.glassEyesCollected >= 5 && jarInv != null){
            g2.drawImage(jarInv, 20, 300 + tileSize + 10, tileSize, tileSize, null);
        }

        if (locketUnlocked == true && locketInv != null){
            // Drawn below the jar of eyes
            g2.drawImage(locketInv, 20, 300 + (tileSize*2) + 20, tileSize, tileSize, null);
        }

        if (chronosWatchUnlocked == true && watchInv != null){
            // Drawn below the locket (Notice it's tileSize*3 and +30 to keep the exact same spacing!)
            g2.drawImage(watchInv, 20, 300 + (tileSize*3) + 30, tileSize, tileSize, null);
        }

        if (passwordUIOpen == true) {
            // low opacity black bg to dim background
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // Draw the UI centered (adjust coordinates if needed)
            int uiX = screenWidth/2 - 250;
            int uiY = screenHeight/2 - 200;
            if (passwordUI != null) g2.drawImage(passwordUI, uiX, uiY, 500, 400, null);
            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            //  typed text inside the white box
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            g2.setColor(Color.BLACK);

            // adjust text input box
            int textX = uiX + 100;
            int textY = uiY + 300;
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

        if (clue1_Open || clue2_Open || clue3_Open) {
            // low opacity bg
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            int uiX = screenWidth/2 - 250;
            int uiY = screenHeight/2 - 200;

            if (clue1_Open && openClue1 != null) g2.drawImage(openClue1, uiX, uiY, 500, 400, null);
            if (clue2_Open && openClue2 != null) g2.drawImage(openClue2, uiX, uiY, 500, 400, null);
            if (clue3_Open && openClue3 != null) g2.drawImage(openClue3, uiX, uiY, 500, 400, null);

            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

            // back button hitbox
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(backButtonHitbox.x, backButtonHitbox.y, backButtonHitbox.width, backButtonHitbox.height);
        }

        if (statue_Open == true) {

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // bg screen
            if (statueRotateScreen != null) {
                g2.drawImage(statueRotateScreen, screenWidth/2 - 350, screenHeight/2 - 250, 700, 500, null);
            }

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
                g2.drawImage(listScreen1, 0, 0, screenWidth, screenHeight, null);

                // Row 1 (Desc 1): Jar, Bouquet, Watch
                if (jarInv != null) g2.drawImage(jarInv, r1c1Hitbox.x, r1c1Hitbox.y, r1c1Hitbox.width, r1c1Hitbox.height, null);
                if (bouquetInv != null) g2.drawImage(bouquetInv, r1c2Hitbox.x, r1c2Hitbox.y, r1c2Hitbox.width, r1c2Hitbox.height, null);
                if (watchInv != null) g2.drawImage(watchInv, r1c3Hitbox.x, r1c3Hitbox.y, r1c3Hitbox.width, r1c3Hitbox.height, null);

                // Row 2 (Desc 2): Locket, Watch, Bouquet
                if (locketInv != null) g2.drawImage(locketInv, r2c1Hitbox.x, r2c1Hitbox.y, r2c1Hitbox.width, r2c1Hitbox.height, null);
                if (watchInv != null) g2.drawImage(watchInv, r2c2Hitbox.x, r2c2Hitbox.y, r2c2Hitbox.width, r2c2Hitbox.height, null);
                if (bouquetInv != null) g2.drawImage(bouquetInv, r2c3Hitbox.x, r2c3Hitbox.y, r2c3Hitbox.width, r2c3Hitbox.height, null);

                // Navigation (Only Next on Page 1)
                if (nextBtn != null) g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }
            // PAGE 2
            else if (introPuzzlePage == 2 && listScreen2 != null) {
                g2.drawImage(listScreen2, 0, 0, screenWidth, screenHeight, null);

                // Row 1 (Desc 3): Watch, Locket, Bouquet
                if (watchInv != null) g2.drawImage(watchInv, r1c1Hitbox.x, r1c1Hitbox.y, r1c1Hitbox.width, r1c1Hitbox.height, null);
                if (locketInv != null) g2.drawImage(locketInv, r1c2Hitbox.x, r1c2Hitbox.y, r1c2Hitbox.width, r1c2Hitbox.height, null);
                if (bouquetInv != null) g2.drawImage(bouquetInv, r1c3Hitbox.x, r1c3Hitbox.y, r1c3Hitbox.width, r1c3Hitbox.height, null);

                // Row 2 (Desc 4): Jar, Bouquet, Locket
                if (jarInv != null) g2.drawImage(jarInv, r2c1Hitbox.x, r2c1Hitbox.y, r2c1Hitbox.width, r2c1Hitbox.height, null);
                if (bouquetInv != null) g2.drawImage(bouquetInv, r2c2Hitbox.x, r2c2Hitbox.y, r2c2Hitbox.width, r2c2Hitbox.height, null);
                if (locketInv != null) g2.drawImage(locketInv, r2c3Hitbox.x, r2c3Hitbox.y, r2c3Hitbox.width, r2c3Hitbox.height, null);

                // Navigation (Both on Page 2)
                if (prevBtn != null) g2.drawImage(prevBtn, prevButtonHitbox.x, prevButtonHitbox.y, prevButtonHitbox.width, prevButtonHitbox.height, null);
                if (nextBtn != null) g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }
            // PAGE 3
            else if (introPuzzlePage == 3 && listScreen3 != null) {
                g2.drawImage(listScreen3, 0, 0, screenWidth, screenHeight, null);

                // Navigation (Both on Page 3)
                if (prevBtn != null) g2.drawImage(prevBtn, prevButtonHitbox.x, prevButtonHitbox.y, prevButtonHitbox.width, prevButtonHitbox.height, null);
                if (nextBtn != null) g2.drawImage(nextBtn, nextButtonHitbox.x, nextButtonHitbox.y, nextButtonHitbox.width, nextButtonHitbox.height, null);
            }

            if (backBtn != null) g2.drawImage(backBtn, 50, 50, 60, 60, null);

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
            int tabX = screenWidth - 180; // Top right corner
            int tabY = 20;

            if (currentQuest == 1 && objTab1 != null) g2.drawImage(objTab1, tabX, tabY, 150, 150, null);
            else if (currentQuest == 2 && objTab2 != null) g2.drawImage(objTab2, tabX, tabY, 150, 150, null);
            else if (currentQuest == 3 && objTab3 != null) g2.drawImage(objTab3, tabX, tabY, 150, 150, null);
            else if (currentQuest == 4 && objTab4 != null) g2.drawImage(objTab4, tabX, tabY, 150, 150, null);
            else if (currentQuest == 5 && objTab5 != null) g2.drawImage(objTab5, tabX, tabY, 150, 150, null);
        }

        // --- NEW: DRAW END SCREEN ---
        if (currentQuest == 6) {
            // Dim background heavily
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // Draw End Screen and Again Button
            if (endScreen != null) g2.drawImage(endScreen, screenWidth/2 - 250, screenHeight/2 - 150, 500, 200, null);
            if (againBtn != null) g2.drawImage(againBtn, againBtnHitbox.x, againBtnHitbox.y, againBtnHitbox.width, againBtnHitbox.height, null);

            // Debug Hitbox for Again Button
            g2.setColor(new Color(255, 255, 0, 150));
            g2.fillRect(againBtnHitbox.x, againBtnHitbox.y, againBtnHitbox.width, againBtnHitbox.height);
        }

        g2.dispose();


    }
}
