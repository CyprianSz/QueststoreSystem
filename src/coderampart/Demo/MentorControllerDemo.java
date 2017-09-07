package coderampart.Demo;

import coderampart.services.Bootable;

import coderampart.model.Mentor;
import coderampart.model.Quest;
import coderampart.model.Artifact;

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
        System.out.println("You create new Mentor: " + mentor);
    } 

    public static void addQuest() {
        String[] questData = View.getQuestData();

        String name = questData[0];
        Integer reward = questData[1];

        Quest quest = new Quest(name, reward);

        // Demo:
        System.out.println("You create new quest: " + quest);
    }

    public static void updateQuest() {

    }

    public static void setQuestCategory() {
        // basic or extra
        Quest quest = chooseQuest();
        String category = View.chooseQuestCategory();
        quest.category = category;

        // Demo 
        System.out.println("Quest category: " + category);
    }

    public static Quest chooseQuest() {
        // QuestDao: readAll -> questList
        // choose specific quest and return it 
        
        // Demo:
        Quest quest = new Quest("Master of mornings", 100);
        return quest;
    }

    public static void markQuest() {

    }

    public static void addArtifact() {
        String[] artifactData = View.getArtifactData();

        String name = artifactData[0];
        Integer value = artifactData[1];

        Artifact artifact = new Artifact(name, value);

        // Demo:
        System.out.println("You create new artifact: " + artifact);
    }

    public static void updateArtifact() {
        
    }

    public static void setArtifactCategory() {
        // basic or extra
        Artifact artifact = chooseArtifact();
        String artifact = View.chooseArtifactCategory();
        artifact.category = category;

        // Demo:
        System.out.println("Artifact category: " + artifact);
    }

    public static Artifact chooseArtifact() {
        // ArtifactDao: readAll -> artifactList
        // choose specific artifact and return it 
        
        // Demo:
        Artifact artifact = new Artifact("Private Mentoring", 50);
        return artifact;
    }

    public static void markArtifact() {
        
    }

    public static void displayWalletDetails() {
        // Demo:
        System.out.println("Wallet details:");
        System.out.println("\nJan Kowalski\nBalanse: 200 coolcoins\nAlreadyBought: Private mentoring");
        System.out.println("\nAnna Nowak\nBalanse: 100 coolcoins\nAlreadyBought: Day in office");
    }



    
}