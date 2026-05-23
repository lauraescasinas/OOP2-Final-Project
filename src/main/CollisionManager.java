package main;

import java.awt.Rectangle;

public class CollisionManager {
    GamePanel gp;


    public Rectangle[] roomWalls = {
            new Rectangle(250, 100, 360, 180), new Rectangle(250, 470, 360, 150),
            new Rectangle(130, 100, 150, 500), new Rectangle(580, 100, 150, 280), new Rectangle(290, 320, 120, 40)
    };
    public Rectangle[] houseWalls = {
            new Rectangle(370, 170, 170, 80), new Rectangle(580, 60, 100, 400),
            new Rectangle(250, 70, 100, 400), new Rectangle(100, 120, 150, 250), new Rectangle(110, 530, 600, 100)
    };
    public Rectangle[] streetWalls = {
            new Rectangle(200, 210, 420, 80), new Rectangle(840, 30, 90, 600), new Rectangle(750, 0, 100, 280),
            new Rectangle(5, 668, 1000, 90), new Rectangle(30, 470, 300, 50), new Rectangle(290, 480, 40, 120),
            new Rectangle(-20, 250, 50, 300), new Rectangle(530, 500, 200, 300), new Rectangle(270, 240, 100, 100)
    };
    public Rectangle[] workshopWalls = {
            new Rectangle(390, 190, 420, 50), new Rectangle(250, 600, 700, 50), new Rectangle(150, 400, 270, 80),
            new Rectangle(100, 210, 270, 60), new Rectangle(12, 130, 60, 430), new Rectangle(380, 180, 60, 250),
            new Rectangle(800, 100, 60, 550)
    };
    public Rectangle[] greenhouseWalls = {
            new Rectangle(90, 30, 60, 800), new Rectangle(720, 20, 60, 800), new Rectangle(100, 90, 600, 60),
            new Rectangle(10, 600, 320, 60), new Rectangle(520, 580, 320, 60)
    };
    public Rectangle[] museumWalls = {
            new Rectangle(90, 30, 60, 800), new Rectangle(720, 20, 60, 800), new Rectangle(120, 210, 600, 60),
            new Rectangle(20, 580, 320, 60), new Rectangle(520, 580, 320, 60)
    };

    // inside the maps (except street)
    public Rectangle houseDoorHitbox = new Rectangle(670, 440, 164, 100);
    public Rectangle workshopDoorHitbox = new Rectangle(80, 600, 164, 100);
    public Rectangle greenhouseDoorHitbox = new Rectangle(350, 620, 164, 100);
    public Rectangle museumDoorHitbox = new Rectangle(350, 580, 164, 100);
    public Rectangle bedroomDoorHitbox = new Rectangle(585, 400, 80, 100);
    public Rectangle outsideBedroomDoorHitbox = new Rectangle(100, 400, 60, 100);

    // outside (in street map)
    public Rectangle streetHouseDoorHitbox = new Rectangle(50, 260, 85, 45);
    public Rectangle streetMuseumDoorHitbox = new Rectangle(650, 180, 85, 45);
    public Rectangle streetWorkshopDoorHitbox = new Rectangle(200, 630, 85, 45);
    public Rectangle streetGreenhouseDoorHitbox = new Rectangle(736, 435, 85, 45);

    public CollisionManager(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkWallCollision(Rectangle nextHitbox, int currentMap) {
        Rectangle[] walls = null;
        if (currentMap == gp.MAP_ROOM) walls = roomWalls;
        else if (currentMap == gp.MAP_HOUSE) walls = houseWalls;
        else if (currentMap == gp.MAP_STREET) walls = streetWalls;
        else if (currentMap == gp.MAP_WORKSHOP) walls = workshopWalls;
        else if (currentMap == gp.MAP_GREENHOUSE) walls = greenhouseWalls;
        else if (currentMap == gp.MAP_MUSEUM) walls = museumWalls;

        if (walls != null) {
            for (Rectangle wall : walls) {
                if (nextHitbox.intersects(wall)) return true;
            }
        }
        return false;
    }

    public boolean checkLockedDoors(Rectangle nextHitbox, int currentMap, int currentQuest) {
        if (currentMap == gp.MAP_HOUSE && currentQuest < 1 && nextHitbox.intersects(houseDoorHitbox)) {
            System.out.println("The door is locked. I must finish the list first.");
            return true;
        }
        if (currentMap == gp.MAP_STREET) {
            if (currentQuest < 3 && nextHitbox.intersects(streetMuseumDoorHitbox)) return true;
            if (currentQuest < 2 && nextHitbox.intersects(streetGreenhouseDoorHitbox)) return true;
            if (currentQuest < 1 && nextHitbox.intersects(streetWorkshopDoorHitbox)) return true;
        }
        return false;
    }
}