package managers;

import main.GamePanel;

public class DialogueManager {
    GamePanel gp;

    public String[] houseDialogue1 = {
            "Before leaving for the day, Elara kneels once more to offer a prayer\nfor her father—who died just two nights ago.",
            "Elara: Please... just let him get there safely. That's all I'm asking."
    };

    public String[] houseDialogue2 = {
            "Demon: How sweet. A little prayer for the great Edmund Voss.", // 0
            "Elara: What— who are you?!", // 1
            "Demon: Someone who was looking forward to meeting your father\nfor a very long time. And look at this...", // 2
            "Demon: White lilies. A normal casket. A cross on the wall.", // 3
            "Demon: Did you even know him?", // 4
            "Elara: ...what are you trying to say—", // 5
            "Demon: A man who kept a jar of dead men's eyes on his desk. A man who\ntalked to taxidermied foxes. And you send him off like he was an accountant.", // 6
            "Elara: Shut up.", // 7
            "Demon: His soul is... restless, Elara. Unsettled. And if no one does\nanything about that—", // 8
            "Demon: I'll just take it with me.", // 9 (RED and BOLD)
            "Demon: Unless...you do something for me.", // 10
            "Demon: There are items — strange ones, specific ones — scattered around\nthis house and the places he loved. Collect them. Arrange them. And his\nsoul goes free.", // 11
            "Demon: I've been kind enough to write most of them down.", // 12
            "Elara: ...Some of these descriptions don't even make sense.", // 13
            "Demon: Your father made sense of stranger things. I'm sure you'll manage.", // 14
            "Demon: Clock's ticking, Elara. It always is." // 15
    };

    public String[] houseDialogue3 = {
            "Elara lays the last item beside the casket. Her hands are shaking, but she's\ndone it. She steps back and faces the demon.", // 0
            "Elara: That's everything. It's done.", // 1
            "The demon doesn't budge.", // 2
            "Elara: Hello?  I said I'm done. I collected all of them.", // 3
            "Demon: Didn't you see the last page of the list I gave you?", // 4 (BOLD RED)
            "Elara stands frozen—it's not that she didn't see it.", // 5
            "She wanted to ignore it.", // 6
            "You can't do this to me. I already did what you told me to do-", // 7
            "Demon: Don't you want your father's soul to be at peace?", // 8 (BOLD RED)
            "..." // 9
    };

    public String[] roomDialogue = {
            "October, 1998. The whole house smells like candle wax and old paper.\n" +
                    "Fifteen-year-old Elara stands at the side of her bed, still in yesterday’s\n" +
                    "clothes, trying to piece together the last seventy-two hours."
    };

    public boolean isDialogueActive = false;
    public String[] currentDialogueArray = null;
    public int currentDialogueListIndex = 0;
    public String fullDialogue = "";
    public String currentDialogue = "";
    public int dialogueCharIndex = 0;
    public int typewriterSpeed = 2;
    public int typewriterCounter = 0;

    public DialogueManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startDialogue(String[] dialogueList) {
        currentDialogueArray = dialogueList;
        currentDialogueListIndex = 0;
        fullDialogue = currentDialogueArray[0];
        currentDialogue = "";
        dialogueCharIndex = 0;
        isDialogueActive = true;
    }

    public void update() {
        if (isDialogueActive) {
            if (dialogueCharIndex < fullDialogue.length()) {
                typewriterCounter++;
                if (typewriterCounter >= typewriterSpeed) {
                    currentDialogue += fullDialogue.charAt(dialogueCharIndex);
                    dialogueCharIndex++;
                    typewriterCounter = 0;
                }
            }
        }
    }
}