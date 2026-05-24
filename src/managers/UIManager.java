package managers;

import main.Constants;
import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UIManager {
    GamePanel gp;

    public UIManager(GamePanel gp) {
        this.gp = gp;
    }

    public void drawAllUI(Graphics2D g2) {
        drawInventory(g2);
        drawInteractables(g2);
        drawCinematics(g2);
        drawSettings(g2);
    }

    private void drawInventory(Graphics2D g2) {
        if (gp.player.glassEyesCollected >= 5 && gp.asManager.inventoryBox != null) {
            g2.drawImage(gp.asManager.inventoryBox, 10, 290, Constants.tileSize + 20, (Constants.tileSize * 4) + 50, null);
        }
        if (gp.player.blackRosesCollected >= 5 && gp.asManager.bouquetInv != null) {
            g2.drawImage(gp.asManager.bouquetInv, 20, 300, Constants.tileSize, Constants.tileSize, null);
        }
        if (gp.player.glassEyesCollected >= 5 && gp.asManager.jarInv != null) {
            g2.drawImage(gp.asManager.jarInv, 20, 300 + Constants.tileSize + 10, Constants.tileSize, Constants.tileSize, null);
        }
        if (gp.gsManager.isLocketUnlocked() && gp.asManager.locketInv != null) {
            g2.drawImage(gp.asManager.locketInv, 20, 300 + (Constants.tileSize * 2) + 20, Constants.tileSize, Constants.tileSize, null);
        }
        if (gp.gsManager.isChronosWatchUnlocked() && gp.asManager.watchInv != null) {
            g2.drawImage(gp.asManager.watchInv, 20, 300 + (Constants.tileSize * 3) + 30, Constants.tileSize, Constants.tileSize, null);
        }

        if (gp.gsManager.getCurrentQuest() >= 1 && gp.gsManager.getCurrentQuest() <= 5) {
            int tabX = Constants.screenWidth - 230;
            int tabY = 30;
            if (gp.gsManager.getCurrentQuest() == 1 && gp.asManager.objTab1 != null) g2.drawImage(gp.asManager.objTab1, tabX, tabY, 200, 120, null);
            else if (gp.gsManager.getCurrentQuest() == 2 && gp.asManager.objTab2 != null) g2.drawImage(gp.asManager.objTab2, tabX, tabY, 200, 120, null);
            else if (gp.gsManager.getCurrentQuest() == 3 && gp.asManager.objTab3 != null) g2.drawImage(gp.asManager.objTab3, tabX, tabY, 200, 120, null);
            else if (gp.gsManager.getCurrentQuest() == 4 && gp.asManager.objTab4 != null) g2.drawImage(gp.asManager.objTab4, tabX, tabY, 200, 120, null);
            else if (gp.gsManager.getCurrentQuest() == 5 && gp.asManager.objTab5 != null) g2.drawImage(gp.asManager.objTab5, tabX, tabY, 200, 120, null);
        }
    }

    private void drawInteractables(Graphics2D g2) {
        // Password Screen
        if (gp.gsManager.isPasswordUIOpen()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            int uiX = Constants.screenWidth / 2 - 250;
            int uiY = Constants.screenHeight / 2 - 200;
            if (gp.asManager.passwordUI != null) g2.drawImage(gp.asManager.passwordUI, uiX, uiY, 500, 400, null);
            if (gp.asManager.backBtn != null) g2.drawImage(gp.asManager.backBtn, 50, 50, 60, 60, null);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            g2.setColor(Color.WHITE);
            int textX = uiX + 100;
            int textY = uiY + 260;
            g2.drawString(gp.keyH.currentInput, textX, textY);
            g2.setColor(Color.MAGENTA);
            g2.fillPolygon(new int[]{textX, textX - 15, textX + 15}, new int[]{textY, textY + 20, textY + 20}, 3);

            if (gp.keyH.showDebug) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.fillRect(gp.submitButtonHitbox.x, gp.submitButtonHitbox.y, gp.submitButtonHitbox.width, gp.submitButtonHitbox.height);
                g2.fillRect(gp.backButtonHitbox.x, gp.backButtonHitbox.y, gp.backButtonHitbox.width, gp.backButtonHitbox.height);
            }
        }

        // Clue Screens
        if (gp.gsManager.isClueOpen(1) || gp.gsManager.isClueOpen(2) || gp.gsManager.isClueOpen(3) || gp.gsManager.isClueOpen(0)) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            int uiX = Constants.screenWidth / 2 - 250;
            int uiY = Constants.screenHeight / 2 - 200;
            if (gp.gsManager.isClueOpen(1) && gp.asManager.openClue1 != null) g2.drawImage(gp.asManager.openClue1, uiX + 50, uiY + 70, 440, 220, null);
            if (gp.gsManager.isClueOpen(2) && gp.asManager.openClue2 != null) g2.drawImage(gp.asManager.openClue2, uiX + 110, uiY + 70, 270, 295, null);
            if (gp.gsManager.isClueOpen(3) && gp.asManager.openClue3 != null) g2.drawImage(gp.asManager.openClue3, uiX + 150, uiY + 70, 200, 330, null);
            if (gp.gsManager.isClueOpen(0) && gp.asManager.openClue0 != null) g2.drawImage(gp.asManager.openClue0, uiX + 50, uiY + 10, 420, 480, null);
            if (gp.asManager.backBtn != null) g2.drawImage(gp.asManager.backBtn, 50, 50, 60, 60, null);
            if (gp.keyH.showDebug) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.fillRect(gp.backButtonHitbox.x, gp.backButtonHitbox.y, gp.backButtonHitbox.width, gp.backButtonHitbox.height);
            }
        }

        // Statue Screen
        if (gp.gsManager.isStatue_Open()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            g2.drawImage(gp.getStatueImage(gp.gsManager.getStatueState(0)), gp.uiStatue1Hitbox.x, gp.uiStatue1Hitbox.y, gp.uiStatue1Hitbox.width, gp.uiStatue1Hitbox.height, null);
            g2.drawImage(gp.getStatueImage(gp.gsManager.getStatueState(1)), gp.uiStatue2Hitbox.x, gp.uiStatue2Hitbox.y, gp.uiStatue2Hitbox.width, gp.uiStatue2Hitbox.height, null);
            g2.drawImage(gp.getStatueImage(gp.gsManager.getStatueState(2)), gp.uiStatue3Hitbox.x, gp.uiStatue3Hitbox.y, gp.uiStatue3Hitbox.width, gp.uiStatue3Hitbox.height, null);
            if (gp.asManager.backBtn != null) g2.drawImage(gp.asManager.backBtn, 50, 50, 60, 60, null);
            if (gp.keyH.showDebug) {
                g2.setColor(new Color(0, 255, 0, 100));
                g2.fillRect(gp.uiStatue1Hitbox.x, gp.uiStatue1Hitbox.y, gp.uiStatue1Hitbox.width, gp.uiStatue1Hitbox.height);
                g2.fillRect(gp.uiStatue2Hitbox.x, gp.uiStatue2Hitbox.y, gp.uiStatue2Hitbox.width, gp.uiStatue2Hitbox.height);
                g2.fillRect(gp.uiStatue3Hitbox.x, gp.uiStatue3Hitbox.y, gp.uiStatue3Hitbox.width, gp.uiStatue3Hitbox.height);
            }
        }

        // Intro Puzzle Screen
        if (gp.gsManager.isIntroPuzzleOpen()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            if (gp.gsManager.getIntroPuzzlePage() == 1 && gp.asManager.listScreen1 != null) {
                g2.drawImage(gp.asManager.listScreen1, 230, 100, 420, 480, null);
                if (gp.asManager.nextBtn != null) g2.drawImage(gp.asManager.nextBtn, gp.nextButtonHitbox.x, gp.nextButtonHitbox.y, gp.nextButtonHitbox.width, gp.nextButtonHitbox.height, null);
            } else if (gp.gsManager.getIntroPuzzlePage() == 2 && gp.asManager.listScreen2 != null) {
                g2.drawImage(gp.asManager.listScreen2, 230, 100, 420, 480, null);
                if (gp.asManager.prevBtn != null) g2.drawImage(gp.asManager.prevBtn, gp.prevButtonHitbox.x, gp.prevButtonHitbox.y, gp.prevButtonHitbox.width, gp.prevButtonHitbox.height, null);
                if (gp.asManager.nextBtn != null) g2.drawImage(gp.asManager.nextBtn, gp.nextButtonHitbox.x, gp.nextButtonHitbox.y, gp.nextButtonHitbox.width, gp.nextButtonHitbox.height, null);
            } else if (gp.gsManager.getIntroPuzzlePage() == 3 && gp.asManager.gibberishFrames[gp.gibberishFrameIndex] != null) {
                g2.drawImage(gp.asManager.gibberishFrames[gp.gibberishFrameIndex], 230, 100, 420, 480, null);
                if (gp.asManager.prevBtn != null) g2.drawImage(gp.asManager.prevBtn, gp.prevButtonHitbox.x, gp.prevButtonHitbox.y, gp.prevButtonHitbox.width, gp.prevButtonHitbox.height, null);
                if (gp.asManager.nextBtn != null) g2.drawImage(gp.asManager.nextBtn, gp.nextButtonHitbox.x, gp.nextButtonHitbox.y, gp.nextButtonHitbox.width, gp.nextButtonHitbox.height, null);
            }
            if (gp.keyH.showDebug) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.fillRect(gp.r1c1Hitbox.x, gp.r1c1Hitbox.y, gp.r1c1Hitbox.width, gp.r1c1Hitbox.height);
                g2.fillRect(gp.r1c2Hitbox.x, gp.r1c2Hitbox.y, gp.r1c2Hitbox.width, gp.r1c2Hitbox.height);
                g2.fillRect(gp.r1c3Hitbox.x, gp.r1c3Hitbox.y, gp.r1c3Hitbox.width, gp.r1c3Hitbox.height);
                g2.fillRect(gp.r2c1Hitbox.x, gp.r2c1Hitbox.y, gp.r2c1Hitbox.width, gp.r2c1Hitbox.height);
                g2.fillRect(gp.r2c2Hitbox.x, gp.r2c2Hitbox.y, gp.r2c2Hitbox.width, gp.r2c2Hitbox.height);
                g2.fillRect(gp.r2c3Hitbox.x, gp.r2c3Hitbox.y, gp.r2c3Hitbox.width, gp.r2c3Hitbox.height);
                g2.fillRect(gp.nextButtonHitbox.x, gp.nextButtonHitbox.y, gp.nextButtonHitbox.width, gp.nextButtonHitbox.height);
            }
        }
    }

    private void drawCinematics(Graphics2D g2) {
        // End Quest (Game Over) Screen
        if (gp.gsManager.getCurrentQuest() == 6) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            if (gp.asManager.endScreen != null) g2.drawImage(gp.asManager.endScreen, Constants.screenWidth / 2 - 250, Constants.screenHeight / 2 - 150, 500, 200, null);
            if (gp.asManager.againBtn != null) g2.drawImage(gp.asManager.againBtn, gp.againBtnHitbox.x, gp.againBtnHitbox.y, gp.againBtnHitbox.width, gp.againBtnHitbox.height, null);
            if (gp.keyH.showDebug) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.fillRect(gp.againBtnHitbox.x, gp.againBtnHitbox.y, gp.againBtnHitbox.width, gp.againBtnHitbox.height);
            }
        }

        // Dialogue Box
        if (gp.dlgManager.isDialogueActive) {
            g2.setColor(new Color(40, 40, 40, 220));
            g2.fillRect(0, Constants.screenHeight - 185, 864, 185);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 22));

            if ((gp.dlgManager.currentDialogueArray == gp.dlgManager.houseDialogue2 && gp.dlgManager.currentDialogueListIndex == 9) ||
                    (gp.dlgManager.currentDialogueArray == gp.dlgManager.houseDialogue3 && (gp.dlgManager.currentDialogueListIndex == 4 || gp.dlgManager.currentDialogueListIndex == 8))) {
                g2.setColor(new Color(220, 50, 50));
                g2.setFont(new Font("Arial", Font.BOLD, 24));
            }

            int textX = 40;
            int textY = Constants.screenHeight - 130;
            for (String line : gp.dlgManager.currentDialogue.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += g2.getFontMetrics().getHeight() + 8;
            }
            if (gp.asManager.nextBtn != null) g2.drawImage(gp.asManager.nextBtn, gp.dialogueNextHitbox.x, gp.dialogueNextHitbox.y, gp.dialogueNextHitbox.width, gp.dialogueNextHitbox.height, null);
        }

        // Ending Gibberish Transition
        if (gp.gsManager.isShowEndingGibberish()) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            if (gp.asManager.endingGibberishFrames[gp.endingGibberishIndex] != null) {
                g2.drawImage(gp.asManager.endingGibberishFrames[gp.endingGibberishIndex], 230, 100, 420, 480, null);
            }
            if (gp.asManager.backBtn != null) g2.drawImage(gp.asManager.backBtn, 50, 50, 60, 60, null);
        }

        // Final Choice Screen
        if (gp.gsManager.isShowChoiceScreen()) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            if (gp.asManager.acceptBtn != null) g2.drawImage(gp.asManager.acceptBtn, gp.acceptHitbox.x, gp.acceptHitbox.y, gp.acceptHitbox.width, gp.acceptHitbox.height, null);
            if (gp.asManager.rejectBtn != null) g2.drawImage(gp.asManager.rejectBtn, gp.rejectHitbox.x, gp.rejectHitbox.y, gp.rejectHitbox.width, gp.rejectHitbox.height, null);
        }

        // Cutscene Player
        if (gp.gsManager.isPlayingAcceptCutscene() || gp.gsManager.isPlayingRejectCutscene()) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
            BufferedImage currentFrame = null;
            if (gp.gsManager.isPlayingAcceptCutscene() && gp.asManager.acceptCutscene[gp.cutsceneFrameIndex] != null) {
                currentFrame = gp.asManager.acceptCutscene[gp.cutsceneFrameIndex];
            } else if (gp.gsManager.isPlayingRejectCutscene() && gp.asManager.rejectCutscene[gp.cutsceneFrameIndex] != null) {
                currentFrame = gp.asManager.rejectCutscene[gp.cutsceneFrameIndex];
            }
            if (currentFrame != null) {
                g2.drawImage(currentFrame, 0, 0, Constants.screenWidth, Constants.screenHeight, null);
            }
            if (gp.gsManager.isShowTheEndText()) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 48));
                g2.drawString(gp.currentTheEndText, Constants.screenWidth / 2 + 100, Constants.screenHeight / 2);
            }
            if (gp.gsManager.isShowMenuButton() && gp.asManager.menuBtn != null) {
                g2.drawImage(gp.asManager.menuBtn, gp.menuBtnHitbox.x, gp.menuBtnHitbox.y, gp.menuBtnHitbox.width, gp.menuBtnHitbox.height, null);
            }
        }
    }

    private void drawSettings(Graphics2D g2) {
        if (gp.gsManager.getCurrentMap() != gp.MAP_MAIN_MENU && !gp.gsManager.isPlayingAcceptCutscene() && !gp.gsManager.isPlayingRejectCutscene() && !gp.gsManager.isShowTheEndText()) {
            if (gp.asManager.settingsBtn != null) {
                g2.drawImage(gp.asManager.settingsBtn, gp.settingsBtnHitbox.x, gp.settingsBtnHitbox.y, gp.settingsBtnHitbox.width, gp.settingsBtnHitbox.height, null);
            }
            if (gp.gsManager.isSettingsOpen()) {
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, Constants.screenWidth, Constants.screenHeight);
                if (gp.asManager.settingsWindow != null) {
                    g2.drawImage(gp.asManager.settingsWindow, 232, 186, 400, 300, null);
                }
                if (gp.keyH.showDebug) {
                    g2.setColor(new Color(255, 255, 0, 100));
                    g2.fillRect(gp.resumeHitbox.x, gp.resumeHitbox.y, gp.resumeHitbox.width, gp.resumeHitbox.height);
                    g2.fillRect(gp.exitMenuHitbox.x, gp.exitMenuHitbox.y, gp.exitMenuHitbox.width, gp.exitMenuHitbox.height);
                }
            }
        }
    }
}