package main;

import java.awt.*;

public class MapBarriers {

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

}
