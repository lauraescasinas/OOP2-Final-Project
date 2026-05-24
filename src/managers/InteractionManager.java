package managers;

import main.GamePanel;

import java.awt.Rectangle;

public class InteractionManager {
    GamePanel gp;

    public InteractionManager(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkInteractions() {
        Rectangle mouseHitbox = new Rectangle(gp.mouseH.mouseX, gp.mouseH.mouseY, 1, 1);

        if (gp.gsManager.getCurrentMap() != gp.MAP_MAIN_MENU && !gp.gsManager.isPlayingAcceptCutscene() && !gp.gsManager.isPlayingRejectCutscene() && !gp.gsManager.isShowTheEndText()) {
            if (gp.mouseH.leftClicked) {
                if (gp.gsManager.isSettingsOpen()) {
                    if (mouseHitbox.intersects(gp.resumeHitbox)) {
                        gp.soundManager.playClickSFX(); // resume
                        gp.gsManager.setSettingsOpen(false);
                    } else if (mouseHitbox.intersects(gp.exitMenuHitbox)) {
                        gp.soundManager.playClickSFX(); // back to menu
                        resetToMainMenu();
                    }
                    gp.mouseH.leftClicked = false;
                    return true;
                } else if (mouseHitbox.intersects(gp.settingsBtnHitbox)) {
                    gp.soundManager.playClickSFX(); // settings button
                    gp.gsManager.setSettingsOpen(true);
                    gp.mouseH.leftClicked = false;
                    return true;
                }
            }
            if (gp.gsManager.isSettingsOpen()) return true;
        }

        if (gp.gsManager.getCurrentQuest() == 6) {
            if (gp.mouseH.leftClicked && mouseHitbox.intersects(gp.againBtnHitbox)) {
                gp.soundManager.playClickSFX(); // again button
                resetGame();
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.dlgManager.isDialogueActive) {
            if (gp.mouseH.leftClicked && mouseHitbox.intersects(gp.dialogueNextHitbox)) {
                gp.soundManager.playClickSFX(); // dialogue next
                if (gp.dlgManager.dialogueCharIndex < gp.dlgManager.fullDialogue.length()) {
                    gp.dlgManager.currentDialogue = gp.dlgManager.fullDialogue;
                    gp.dlgManager.dialogueCharIndex = gp.dlgManager.fullDialogue.length();
                } else {
                    if (gp.dlgManager.currentDialogueArray == gp.dlgManager.houseDialogue3 && gp.dlgManager.currentDialogueListIndex == 6) {
                        gp.dlgManager.isDialogueActive = false; gp.gsManager.setHouseEventState(10);
                    } else if (gp.dlgManager.currentDialogueArray == gp.dlgManager.houseDialogue3 && gp.dlgManager.currentDialogueListIndex == 9) {
                        gp.dlgManager.isDialogueActive = false; gp.gsManager.setHouseEventState(13);
                    } else {
                        gp.dlgManager.currentDialogueListIndex++;
                        if (gp.dlgManager.currentDialogueListIndex < gp.dlgManager.currentDialogueArray.length) {
                            gp.dlgManager.fullDialogue = gp.dlgManager.currentDialogueArray[gp.dlgManager.currentDialogueListIndex];
                            gp.dlgManager.currentDialogue = ""; gp.dlgManager.dialogueCharIndex = 0;
                        } else {
                            gp.dlgManager.isDialogueActive = false;
                            if (gp.gsManager.getHouseEventState() == 1) gp.gsManager.setHouseEventState(2);
                            else if (gp.gsManager.getHouseEventState() == 4) gp.gsManager.setHouseEventState(5);
                        }
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.gsManager.isShowEndingGibberish()) {
            if (gp.mouseH.leftClicked && mouseHitbox.intersects(gp.backButtonHitbox)) {
                gp.soundManager.playClickSFX(); // back button on ending gibberish
                gp.gsManager.setShowEndingGibberish(false); gp.gsManager.setHouseEventState(12); gp.dlgManager.isDialogueActive = true;
                gp.dlgManager.currentDialogueListIndex = 7; gp.dlgManager.fullDialogue = gp.dlgManager.houseDialogue3[7];
                gp.dlgManager.currentDialogue = ""; gp.dlgManager.dialogueCharIndex = 0;
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.gsManager.isShowChoiceScreen()) {
            if (gp.mouseH.leftClicked) {
                if (mouseHitbox.intersects(gp.acceptHitbox)) {
                    gp.soundManager.playClickSFX(); // accept
                    gp.gsManager.setShowChoiceScreen(false); gp.gsManager.setPlayingAcceptCutscene(true);
                    gp.cutsceneFrameIndex = 0; gp.cutsceneTimer = 0;
                } else if (mouseHitbox.intersects(gp.rejectHitbox) && gp.rejectHoverCount >= 5) {
                    gp.soundManager.playClickSFX(); // reject
                    gp.gsManager.setShowChoiceScreen(false); gp.gsManager.setPlayingRejectCutscene(true);
                    gp.cutsceneFrameIndex = 0; gp.cutsceneTimer = 0;
                }
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.gsManager.isPlayingAcceptCutscene() || gp.gsManager.isPlayingRejectCutscene()) {
            if (gp.gsManager.isShowMenuButton() && gp.mouseH.leftClicked && mouseHitbox.intersects(gp.menuBtnHitbox)) {
                gp.soundManager.playClickSFX(); // menu button after ending
                gp.gsManager.setPlayingAcceptCutscene(false); gp.gsManager.setPlayingRejectCutscene(false);
                gp.gsManager.setShowTheEndText(false); gp.gsManager.setShowMenuButton(false);
                gp.currentTheEndText = ""; gp.theEndCharIndex = 0;
                gp.rejectHoverCount = 0; gp.rejectHitbox.x = 480; gp.rejectHitbox.y = 300;
                resetToMainMenu();
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.gsManager.isPasswordUIOpen() || gp.gsManager.isClueOpen(0) || gp.gsManager.isClueOpen(1) || gp.gsManager.isClueOpen(2) || gp.gsManager.isClueOpen(3) || gp.gsManager.isStatue_Open() || gp.gsManager.isIntroPuzzleOpen()) {
            if (gp.mouseH.leftClicked) {
                if (mouseHitbox.intersects(gp.backButtonHitbox)) {
                    gp.soundManager.playClickSFX(); // back button on puzzle/clue/statue screens
                    gp.gsManager.setPasswordUIOpen(false);
                    gp.gsManager.setClueOpen(0, false); gp.gsManager.setClueOpen(1, false);
                    gp.gsManager.setClueOpen(2, false); gp.gsManager.setClueOpen(3, false);
                    gp.gsManager.setStatue_Open(false);
                    gp.gsManager.setIntroPuzzleOpen(false); gp.gsManager.setIntroPuzzlePage(1);
                } else if (gp.gsManager.isStatue_Open() && !gp.gsManager.isChronosWatchUnlocked()) {
                    if (mouseHitbox.intersects(gp.uiStatue1Hitbox)) { gp.gsManager.setStatueState(0, (gp.gsManager.getStatueState(0) + 1) % 4); gp.soundManager.playStatueRotationSFX(); }
                    else if (mouseHitbox.intersects(gp.uiStatue2Hitbox)) { gp.gsManager.setStatueState(1, (gp.gsManager.getStatueState(1) + 1) % 4); gp.soundManager.playStatueRotationSFX(); }
                    else if (mouseHitbox.intersects(gp.uiStatue3Hitbox)) { gp.gsManager.setStatueState(2, (gp.gsManager.getStatueState(2) + 1) % 4); gp.soundManager.playStatueRotationSFX(); }

                    if (gp.gsManager.getStatueState(0) == 3 && gp.gsManager.getStatueState(1) == 1 && gp.gsManager.getStatueState(2) == 0) {
                        gp.gsManager.setChronosWatchUnlocked(true); gp.gsManager.setStatue_Open(false);
                        if (gp.gsManager.getCurrentQuest() == 3) gp.gsManager.setCurrentQuest(4);
                        System.out.println("Success! Chronos Watch Unlocked.");
                    }
                } else if (gp.gsManager.isPasswordUIOpen() && mouseHitbox.intersects(gp.submitButtonHitbox)) {
                    if (gp.keyH.currentInput.equals("1984")) {
                        gp.soundManager.playCorrectPasswordSFX();
                        gp.gsManager.setLocketUnlocked(true); gp.gsManager.setPasswordUIOpen(false);
                        if (gp.gsManager.getCurrentQuest() == 4) gp.gsManager.setCurrentQuest(5);
                        System.out.println("Success! Locket Unlocked.");
                    } else {
                        gp.soundManager.playErrorSFX();
                        System.out.println("Access Denied. Wrong Password."); gp.keyH.currentInput = "";
                    }
                } else if (gp.gsManager.isIntroPuzzleOpen()) {
                    if (gp.gsManager.getIntroPuzzlePage() == 1) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.soundManager.playClickSFX(); // next page
                            gp.gsManager.setIntroPuzzlePage(2);
                        } else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            gp.soundManager.playClickSFX(); // choice
                            gp.gsManager.setIntroAns(0, true);
                        } else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.soundManager.playClickSFX(); // choice
                            gp.gsManager.setIntroAns(1, true);
                        }
                    } else if (gp.gsManager.getIntroPuzzlePage() == 2) {
                        if (mouseHitbox.intersects(gp.nextButtonHitbox)) {
                            gp.soundManager.playClickSFX(); // next page
                            gp.gsManager.setIntroPuzzlePage(3);
                        } else if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.soundManager.playClickSFX(); // prev page
                            gp.gsManager.setIntroPuzzlePage(1);
                        } else if (mouseHitbox.intersects(gp.r1c1Hitbox)) {
                            gp.soundManager.playClickSFX(); // choice
                            gp.gsManager.setIntroAns(2, true);
                        } else if (mouseHitbox.intersects(gp.r2c3Hitbox)) {
                            gp.soundManager.playClickSFX(); // choice
                            gp.gsManager.setIntroAns(3, true);
                        }
                    } else if (gp.gsManager.getIntroPuzzlePage() == 3) {
                        if (mouseHitbox.intersects(gp.prevButtonHitbox)) {
                            gp.soundManager.playClickSFX(); // prev page
                            gp.gsManager.setIntroPuzzlePage(2);
                        }
                    }
                    if (gp.gsManager.isIntroAns(0) && gp.gsManager.isIntroAns(1) && gp.gsManager.isIntroAns(2) && gp.gsManager.isIntroAns(3) && gp.gsManager.getCurrentQuest() == 0) {
                        gp.gsManager.setCurrentQuest(1); gp.gsManager.setIntroPuzzleOpen(false);
                        System.out.println("Intro Complete!");
                    }
                }
                gp.mouseH.leftClicked = false;
            }
            return true;
        }

        if (gp.mouseH.leftClicked) {
            if (gp.gsManager.getCurrentMap() == gp.MAP_HOUSE) {
                if (mouseHitbox.intersects(gp.debugIntroHitbox)) { gp.gsManager.setIntroPuzzleOpen(true); gp.gsManager.setIntroPuzzlePage(1); }
            } else if (gp.gsManager.getCurrentMap() == gp.MAP_GREENHOUSE) {
                for (int i = 0; i < gp.objManager.obj.length; i++) {
                    if (gp.objManager.obj[i] != null && mouseHitbox.intersects(gp.objManager.obj[i].hitbox)) {
                        System.out.println("Rose picked up!");
                        gp.objManager.obj[i] = null; gp.player.blackRosesCollected++;
                        gp.soundManager.playRoseSFX();
                        if (gp.player.blackRosesCollected >= 5 && gp.gsManager.getCurrentQuest() == 2) {
                            gp.gsManager.setCurrentQuest(3);
                            System.out.println("Black Roses collected! Quest updated to 3.");
                        }
                    }
                }
            } else if (gp.gsManager.getCurrentMap() == gp.MAP_WORKSHOP) {
                for (int i = 0; i < gp.objManager.obj.length; i++) {
                    if (gp.objManager.obj[i] != null && gp.objManager.obj[i].name.equals("Glass Eye") && mouseHitbox.intersects(gp.objManager.obj[i].hitbox)) {
                        gp.objManager.obj[i] = null; gp.player.glassEyesCollected++;
                        gp.soundManager.playGlassEyeSFX();
                        if (gp.player.glassEyesCollected >= 5 && gp.gsManager.getCurrentQuest() == 1) {
                            gp.gsManager.setCurrentQuest(2);
                            System.out.println("Glass Eyes collected! Quest updated to 2.");
                        }
                    }
                }
            } else if (gp.gsManager.getCurrentMap() == gp.MAP_MUSEUM) {
                if (!gp.gsManager.isLocketUnlocked() && gp.gsManager.isChronosWatchUnlocked()) {
                    gp.gsManager.setPasswordUIOpen(true); gp.keyH.currentInput = "";
                }
                if (mouseHitbox.intersects(gp.clue1Hitbox)) gp.gsManager.setClueOpen(1, true);
                else if (mouseHitbox.intersects(gp.clue2Hitbox)) gp.gsManager.setClueOpen(2, true);
                else if (mouseHitbox.intersects(gp.clue3Hitbox)) gp.gsManager.setClueOpen(3, true);
                else if (mouseHitbox.intersects(gp.clue0Hitbox)) gp.gsManager.setClueOpen(0, true);

                if (!gp.gsManager.isChronosWatchUnlocked() && (mouseHitbox.intersects(gp.mapStatue1Hitbox) || mouseHitbox.intersects(gp.mapStatue2Hitbox) || mouseHitbox.intersects(gp.mapStatue3Hitbox))) {
                    gp.gsManager.setStatue_Open(true);
                }
            }
            gp.mouseH.leftClicked = false;
        }
        return false;
    }

    private void resetGame() {
        gp.gsManager.setCurrentQuest(0);
        for(int i = 0; i < 4; i++) gp.gsManager.setIntroAns(i, false);
        gp.player.blackRosesCollected = 0; gp.player.glassEyesCollected = 0;
        gp.gsManager.setStatueState(0, 0); gp.gsManager.setStatueState(1, 3); gp.gsManager.setStatueState(2, 2);
        gp.gsManager.setChronosWatchUnlocked(false); gp.gsManager.setLocketUnlocked(false);
        gp.gsManager.setHouseEventState(0); gp.gsManager.setDemonVisible(false); gp.gsManager.setIntroDialogueTriggered(false);
        gp.objManager.setObjects(); gp.player.setDefaultValues(); gp.gsManager.setCurrentMap(gp.MAP_ROOM);
    }

    private void resetToMainMenu() {
        gp.gsManager.setSettingsOpen(false); gp.gsManager.setCurrentQuest(0);
        for(int i = 0; i < 4; i++) gp.gsManager.setIntroAns(i, false);
        gp.player.blackRosesCollected = 0; gp.player.glassEyesCollected = 0;
        gp.gsManager.setStatueState(0, 0); gp.gsManager.setStatueState(1, 3); gp.gsManager.setStatueState(2, 2);
        gp.gsManager.setChronosWatchUnlocked(false); gp.gsManager.setLocketUnlocked(false); gp.gsManager.setHouseEventState(0);
        gp.gsManager.setDemonVisible(false); gp.gsManager.setIntroDialogueTriggered(false); gp.dlgManager.isDialogueActive = false;
        gp.gsManager.setIntroPuzzleOpen(false); gp.gsManager.setPasswordUIOpen(false); gp.gsManager.setStatue_Open(false);
        gp.gsManager.setShowEndingGibberish(false); gp.gsManager.setShowChoiceScreen(false);
        gp.objManager.setObjects(); gp.player.setDefaultValues(); gp.gsManager.setCurrentMap(gp.MAP_MAIN_MENU);

        gp.soundManager.stopOutdoorMusic();
        gp.soundManager.stopRoomMusic();
        gp.soundManager.loadAudio();
        gp.soundManager.playMenuMusic();
    }
}