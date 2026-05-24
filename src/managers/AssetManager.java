package managers;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AssetManager {
    GamePanel gp;

    // 1. Move all your BufferedImage declarations here from GamePanel
    public BufferedImage roomBg;
    public BufferedImage houseBg;
    public BufferedImage streetBg1, streetBg2;
    public BufferedImage workshopBg1, workshopBg2, toolboxWorkshop, sofaWorkshop;
    public BufferedImage greenhouseBg1, greenhouseBg2;
    public BufferedImage museumBg, tableMuseum, glasscaseMuseum;
    public BufferedImage bouquetInv;
    public BufferedImage jarInv;
    public BufferedImage inventoryBox;
    public BufferedImage lockedCase, clue0, clue1, clue2, clue3, passwordUI, backBtn, locketInv, unlockedCase, watchInv;
    public BufferedImage openClue1, openClue2, openClue3, openClue0;
    public BufferedImage statueRotateScreen, statueLeft, statueBackLeft, statueBackRight, statueRight;
    public BufferedImage tempBtn, listScreen1, listScreen2, listScreen3, nextBtn, prevBtn;
    public BufferedImage objTab1, objTab2, objTab3, objTab4, objTab5;
    public BufferedImage endScreen, againBtn; // Add all your images here
    public BufferedImage settingsBtn, settingsWindow;
    public BufferedImage[] acceptCutscene = new BufferedImage[8];
    public BufferedImage[] rejectCutscene = new BufferedImage[8];
    public BufferedImage menuBtn;
    public BufferedImage[] gibberishFrames = new BufferedImage[10];
    public BufferedImage[] menuFrames = new BufferedImage[4];
    public BufferedImage demon1, demon2;
    public BufferedImage[] endingGibberishFrames = new BufferedImage[11];
    public BufferedImage acceptBtn, rejectBtn;
    public BufferedImage[] introCutscene = new BufferedImage[9];

    private javax.sound.sampled.Clip menuMusic;
    private javax.sound.sampled.Clip clickSFX;

    public AssetManager(GamePanel gp) {
        this.gp = gp;
        loadImages();
    }

    private void loadImages() {
        try {
            for (int i = 0; i < 4; i++) {
                menuFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/MainMenu_" + (i + 1) + ".png"));
            }

            try {
                java.net.URL musicURL = getClass().getResource("/Music/MainMenu.wav");
                if (musicURL != null) {
                    javax.sound.sampled.AudioInputStream ais =
                            javax.sound.sampled.AudioSystem.getAudioInputStream(musicURL);
                    menuMusic = javax.sound.sampled.AudioSystem.getClip();
                    menuMusic.open(ais);
                }

                java.net.URL sfxURL = getClass().getResource("/Music/Button.wav");
                if (sfxURL != null) {
                    javax.sound.sampled.AudioInputStream ais2 =
                            javax.sound.sampled.AudioSystem.getAudioInputStream(sfxURL);
                    clickSFX = javax.sound.sampled.AudioSystem.getClip();
                    clickSFX.open(ais2);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // maps & their frames
            houseBg = ImageIO.read(getClass().getResourceAsStream("/Maps/House_bg.png"));
            streetBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg1.png"));
            streetBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Street_bg2.png"));
            workshopBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg1.png"));
            workshopBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Workshop_bg2.png"));
            greenhouseBg1 = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg1.png"));
            greenhouseBg2 = ImageIO.read(getClass().getResourceAsStream("/Maps/Greenhouse_bg2.png"));
            roomBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Room_bg.png"));
            museumBg = ImageIO.read(getClass().getResourceAsStream("/Maps/Museum_bg.png"));

            // overlay items
            toolboxWorkshop = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/toolbox_Workshop_bg.png"));
            sofaWorkshop = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/sofa_Workshop_bg.png"));
            tableMuseum = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/table_Museum_bg.png"));
            glasscaseMuseum = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/glasscase_Museum_bg.png"));

            // for intro list
            tempBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/temporary_button.png"));
            listScreen1 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/list_screen1.png"));
            nextBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/next_button.png"));
            listScreen2 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/list_screen2.png"));
            listScreen3 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/list_screen3.png"));
            prevBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/prev_button.png"));

            // demon frames
            demon1 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/Demon_1.png"));
            demon2 = ImageIO.read(getClass().getResourceAsStream("/Entity/Player/Demon_2.png"));

            // bouquet roses, glass eyes, locket, watch in inventory once collected || quest items loaded
            bouquetInv = ImageIO.read(getClass().getResourceAsStream("/Objects/Collectibles/bouquet_roses.png"));
            jarInv = ImageIO.read(getClass().getResourceAsStream("/Objects/Collectibles/jar_eyes.png"));
            locketInv = ImageIO.read(getClass().getResourceAsStream("/Objects/Collectibles/memento_locket.png"));
            watchInv = ImageIO.read(getClass().getResourceAsStream("/Objects/Collectibles/chronos_watch.png"));
            inventoryBox = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/inventory_box.png"));
            lockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/locked_GlassCase.png"));
            passwordUI = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/password_input.png"));
            backBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/back_button.png"));
            unlockedCase = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/unlocked_GlassCase.png"));

            // clues
            clue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/clue1.png"));
            clue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/clue2.png"));
            clue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/clue3.png"));
            clue0 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/clue0.png"));
            openClue1 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/open_clue1.png"));
            openClue2 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/open_clue2.png"));
            openClue3 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/open_clue3.png"));
            openClue0 = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/open_clue0.png"));

            // statues
            statueRotateScreen = ImageIO.read(getClass().getResourceAsStream("/Objects/statuerotate_Screen.png"));
            statueLeft = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/statue_left.png"));
            statueBackLeft = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/statue_back_left.png"));
            statueBackRight = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/statue_back_right.png"));
            statueRight = ImageIO.read(getClass().getResourceAsStream("/Objects/Props/statue_right.png"));

            // quest system
            objTab1 = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/objective_tab1.png"));
            objTab2 = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/objective_tab2.png"));
            objTab3 = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/objective_tab3.png"));
            objTab4 = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/objective_tab4.png"));
            objTab5 = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/objective_tab5.png"));
            endScreen = ImageIO.read(getClass().getResourceAsStream("/Ending/end_screen.png"));
            againBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/again_button.png"));

            for (int i = 0; i < 9; i++) {
                introCutscene[i] = ImageIO.read(getClass().getResourceAsStream("/Intro/Intro_" + (i + 1) + ".png"));
            }

            for (int i = 0; i < 10; i++) {
                gibberishFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Objects/ScreenFrames/Gibberish_" + (i + 1) + ".png"));
            }

            for (int i = 0; i < 11; i++) {
                endingGibberishFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Ending/gibberish_Ending/Gibberish_" + (i + 1) + ".png"));
            }

            acceptBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/accept_button.png"));
            rejectBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/reject_button.png"));
            menuBtn = ImageIO.read(getClass().getResourceAsStream("/Buttons/MenuButton.png"));

            settingsBtn = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/Settings.png"));
            settingsWindow = ImageIO.read(getClass().getResourceAsStream("/Objects/Overlays/SettingsWindow.png"));

            for (int i = 0; i < 8; i++) {
                acceptCutscene[i] = ImageIO.read(getClass().getResourceAsStream("/Ending/bad_Ending/BadEnding_" + (i + 1) + ".png"));
                rejectCutscene[i] = ImageIO.read(getClass().getResourceAsStream("/Ending/good_Ending/GoodEnding_" + (i + 1) + ".png"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (menuMusic != null) {
            menuMusic.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
            menuMusic.start();
        }
    }
}