package managers;

import main.GamePanel;

public class GameStateManager {
    GamePanel gp;

//    public final int MAP_HOUSE = 0;
//    public final int MAP_STREET = 1;
//    public final int MAP_WORKSHOP = 2;
//    public final int MAP_GREENHOUSE = 3;
//    public final int MAP_MUSEUM = 4;
//    public final int MAP_ROOM = 5;
//    public final int MAP_MAIN_MENU = -1;

    // map
    private int currentMap = -1;
    private int currentQuest = 0;
    private int houseEventState = 0;

    // ui / menu
    private boolean isSettingsOpen = false;
    private boolean passwordUIOpen = false;
    private boolean statue_Open = false;
    private boolean introPuzzleOpen = false;
    private boolean showEndingGibberish = false;
    private boolean showChoiceScreen = false;
    private boolean showTheEndText = false;
    private boolean showMenuButton = false;

    // gameplay
    private boolean locketUnlocked = false;
    private boolean chronosWatchUnlocked = false;
    private boolean demonVisible = false;
    private boolean introDialogueTriggered = false;
    private boolean playingAcceptCutscene = false;
    private boolean playingRejectCutscene = false;
    private boolean playingIntroCutscene = false;

    // array states
    // index 0=Clue0, 1=Clue1, 2=Clue2, 3=Clue3
    private boolean[] clueOpen = new boolean[4];

    // index 0=Statue1, 1=Statue2, 2=Statue3. (Initial states: 0, 3, 2)
    private int[] statueState = {0, 3, 2};

    // index 0=Ans1, 1=Ans2, 2=Ans3, 3=Ans4
    private boolean[] introAns = new boolean[4];
    private int introPuzzlePage = 1;

    public GameStateManager(GamePanel gp) {
        this.gp = gp;
    }


    public boolean isClueOpen(int index) { return clueOpen[index]; }
    public void setClueOpen(int index, boolean isOpen) { this.clueOpen[index] = isOpen; }

    public int getStatueState(int index) { return statueState[index]; }
    public void setStatueState(int index, int state) { this.statueState[index] = state; }

    public boolean isIntroAns(int index) { return introAns[index]; }
    public void setIntroAns(int index, boolean isAns) { this.introAns[index] = isAns; }


    public int getCurrentMap() { return currentMap; }
    public void setCurrentMap(int map) { if (map >= -1 && map <= 5) this.currentMap = map; else System.out.println("Warning: Invalid map ID attempted: " + map); }

    public int getCurrentQuest() { return currentQuest; }
    public void setCurrentQuest(int currentQuest) { this.currentQuest = currentQuest; }

    public int getHouseEventState() { return houseEventState; }
    public void setHouseEventState(int houseEventState) { this.houseEventState = houseEventState; }

    public boolean isSettingsOpen() { return isSettingsOpen; }
    public void setSettingsOpen(boolean settingsOpen) { this.isSettingsOpen = settingsOpen; }

    public boolean isPasswordUIOpen() { return passwordUIOpen; }
    public void setPasswordUIOpen(boolean passwordUIOpen) { this.passwordUIOpen = passwordUIOpen; }

    public boolean isStatue_Open() { return statue_Open; }
    public void setStatue_Open(boolean statue_Open) { this.statue_Open = statue_Open; }

    public boolean isIntroPuzzleOpen() { return introPuzzleOpen; }
    public void setIntroPuzzleOpen(boolean introPuzzleOpen) { this.introPuzzleOpen = introPuzzleOpen; }

    public boolean isShowEndingGibberish() { return showEndingGibberish; }
    public void setShowEndingGibberish(boolean showEndingGibberish) { this.showEndingGibberish = showEndingGibberish; }

    public boolean isShowChoiceScreen() { return showChoiceScreen; }
    public void setShowChoiceScreen(boolean showChoiceScreen) { this.showChoiceScreen = showChoiceScreen; }

    public boolean isShowTheEndText() { return showTheEndText; }
    public void setShowTheEndText(boolean showTheEndText) { this.showTheEndText = showTheEndText; }

    public boolean isShowMenuButton() { return showMenuButton; }
    public void setShowMenuButton(boolean showMenuButton) { this.showMenuButton = showMenuButton; }

    public boolean isLocketUnlocked() { return locketUnlocked; }
    public void setLocketUnlocked(boolean locketUnlocked) { this.locketUnlocked = locketUnlocked; }

    public boolean isChronosWatchUnlocked() { return chronosWatchUnlocked; }
    public void setChronosWatchUnlocked(boolean chronosWatchUnlocked) { this.chronosWatchUnlocked = chronosWatchUnlocked; }

    public boolean isDemonVisible() { return demonVisible; }
    public void setDemonVisible(boolean demonVisible) { this.demonVisible = demonVisible; }

    public boolean isIntroDialogueTriggered() { return introDialogueTriggered; }
    public void setIntroDialogueTriggered(boolean introDialogueTriggered) { this.introDialogueTriggered = introDialogueTriggered; }

    public boolean isPlayingAcceptCutscene() { return playingAcceptCutscene; }
    public void setPlayingAcceptCutscene(boolean playingAcceptCutscene) { this.playingAcceptCutscene = playingAcceptCutscene; }

    public boolean isPlayingRejectCutscene() { return playingRejectCutscene; }
    public void setPlayingRejectCutscene(boolean playingRejectCutscene) { this.playingRejectCutscene = playingRejectCutscene; }

    public int getIntroPuzzlePage() { return introPuzzlePage; }
    public void setIntroPuzzlePage(int introPuzzlePage) { this.introPuzzlePage = introPuzzlePage; }

    public boolean isPlayingIntroCutscene() { return playingIntroCutscene; }
    public void setPlayingIntroCutscene(boolean playingIntroCutscene) { this.playingIntroCutscene = playingIntroCutscene; }

}