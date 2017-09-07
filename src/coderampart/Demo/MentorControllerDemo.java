package coderampart.Demo;

public class MentorControllerDemo implements Bootable {

    public void start() {
        View.displayMentorMenu();
        Integer userChoice = View.getUserChoice();

        switch(userChoice) {
            case 1: createCodecooler();
                    break;
            case 2: addQuest();
                    break;
            case 3: updateQuest();
                    break;
            case 4: setQuestCategory();
                    break;
            case 5: markQuest();
                    break;
            case 6: addArtifact();
                    break;
            case 7: updateArtifact();
                    break;
            case 8: setArtifactCategory();
                    break;
            case 9: markArtifact();
                    break;
            case 10: displayWalletDetails();
                    break;
        }
    }    
}