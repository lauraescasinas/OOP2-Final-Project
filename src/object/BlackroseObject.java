package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class BlackroseObject extends SuperObject {
    public BlackroseObject(){
        name = "Black Rose";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/black_rose.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
