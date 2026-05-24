package main;

public class Constants {
    // Screen Settings
    public static final int originalTileSize = 16;    // 16x16 tile for characters
    public static final int scale = 3;
//
    public static final int tileSize = originalTileSize * scale;  // 48x48 tile
    public static final int maxScreenCol = 18;
    public static final int maxScreenRow = 14;
    public static final int screenWidth = tileSize * maxScreenCol;  // 864 pixels
    public static final int screenHeight = tileSize * maxScreenRow;  // 672

    // FPS Settings
    public static final int FPS = 60;

}