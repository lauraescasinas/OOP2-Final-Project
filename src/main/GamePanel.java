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

    public void loadBackgrounds() {
        try {
            houseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/House_bg.png"));
            streetBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg.png"));
            workshopBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg.png"));
            greenhouseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg.png"));
            museumBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Museum_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (currentMap == MAP_HOUSE && houseBg != null) {
            g2.drawImage(houseBg, 0, 0, screenWidth, screenHeight, null);
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
        g2.dispose();


    }
}
