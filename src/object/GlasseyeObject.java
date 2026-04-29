package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class GlasseyeObject extends SuperObject{
    public GlasseyeObject(){
        name = "Glass Eye";

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/glass_eyes.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
