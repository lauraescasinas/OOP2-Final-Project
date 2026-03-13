package main.ui;

import java.awt.Graphics;
import java.awt.Color;

public class CharacterCreation {

    public String playerName = "";
    public int enterX;
    public int enterY;
    public int enterWidth = 100;
    public int enterHeight = 40;

    public CharacterCreation() {

    }

    public void draw(Graphics g, int screenWidth) {

        g.setColor(Color.BLACK);

        g.drawString("CHARACTER CREATION", screenWidth / 2 - 80, 150);

        g.drawString("Enter name: ", screenWidth / 2 - 60, 250);

        //fake textbox for now
        g.drawRect(screenWidth / 2 - 100, 270, 200, 40);

        g.drawString(playerName, screenWidth / 2 - 90, 295);

        //enter button sa name
        enterX = screenWidth/2 - 50;
        enterY = 340;

        g.drawRect(enterX, enterY, enterWidth, enterHeight);
        g.drawString("ENTER", enterX + 25, enterY + 25);
    }

}
