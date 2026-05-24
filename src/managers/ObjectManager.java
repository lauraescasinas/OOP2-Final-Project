package managers;

import main.GamePanel;
import object.BlackroseObject;
import object.GlasseyeObject;
import object.SuperObject;

public class ObjectManager {
    GamePanel gp;
    public SuperObject[] obj = new SuperObject[10];

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setObjects() {
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
}
