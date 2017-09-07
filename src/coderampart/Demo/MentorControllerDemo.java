package coderampart.Demo;

import coderampart.model.Mentor;

import java.util.Date;

public class MentorControllerDemo implements Bootable {

    public final Integer CREATE_MENTOR = 1;
    public final Integer ADD_QUEST = 2;
    public final Integer UPDATE_QUEST = 3;
    public final Integer SET_QUEST_CATEGORY = 4;
    public final Integer MARK_QUEST = 5;
    public final Integer ADD_ARTIFACT = 6;
    public final Integer UPDATE_ARTIFACT = 7;
    public final Integer SET_ARTIFACT_CATEGORY = 8;
    public final Integer MARK_ARTIFACT = 9;
    public final Integer DISPLAY_WALLET_DETAILS = 10;

    public void start() {
        View.displayMentorMenu();
        Integer userChoice = View.getUserChoice();

        switch(userChoice) {
            case CREATE_MENTOR: createMentor();
                    break;
            case ADD_QUEST: addQuest();
                    break;
            case UPDATE_QUEST: updateQuest();
                    break;
            case SET_QUEST_CATEGORY: setQuestCategory();
                    break;
            case MARK_QUEST: markQuest();
                    break;
            case ADD_ARTIFACT: addArtifact();
                    break;
            case UPDATE_ARTIFACT: updateArtifact();
                    break;
            case SET_ARTIFACT_CATEGORY: setArtifactCategory();
                    break;
            case MARK_ARTIFACT: markArtifact();
                    break;
            case DISPLAY_WALLET_DETAILS: displayWalletDetails();
                    break;
        }
    }

    public static void createMentor() {
        String[] userData = View.getUserData();

        String name = userData[0];
        String surname = userData[1];
        String email = userData[2];
        Date dateOfBirth = userData[3];
        Mentor mentor = new Codecooler(name, surname, email, dateOfBirth);

        // Demo:
        System.out.println("New Mentor: " + mentor);

    } 

    public static void addQuest() {

    }
}