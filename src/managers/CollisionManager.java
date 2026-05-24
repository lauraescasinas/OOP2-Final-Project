package managers;

import main.GamePanel;
import main.MapBarriers;

import java.awt.Rectangle;

public class CollisionManager {
    GamePanel gp;
    public MapBarriers mapBarrier = new MapBarriers();
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
        if (currentMap == gp.MAP_ROOM) walls = mapBarrier.roomWalls;
        else if (currentMap == gp.MAP_HOUSE) walls = mapBarrier.houseWalls;
        else if (currentMap == gp.MAP_STREET) walls = mapBarrier.streetWalls;
        else if (currentMap == gp.MAP_WORKSHOP) walls = mapBarrier.workshopWalls;
        else if (currentMap == gp.MAP_GREENHOUSE) walls = mapBarrier.greenhouseWalls;
        else if (currentMap == gp.MAP_MUSEUM) walls = mapBarrier.museumWalls;

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