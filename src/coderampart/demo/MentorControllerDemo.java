package coderampart.demo;

import coderampart.services.Bootable;
import coderampart.view.View;
import java.lang.NumberFormatException;

import coderampart.model.Mentor;
import coderampart.model.Codecooler;
import coderampart.model.Item;
import coderampart.model.Quest;
import coderampart.model.Artifact;

import java.time.LocalDate;

public class MentorControllerDemo implements Bootable {

    private View view = new View();

    public static final int CREATE_CODECOOLER = 1;
    public static final int ADD_QUEST = 2;
    public static final int UPDATE_QUEST = 3;
    public static final int SET_QUEST_CATEGORY = 4;
    public static final int MARK_QUEST = 5;
    public static final int ADD_ARTIFACT = 6;
    public static final int UPDATE_ARTIFACT = 7;
    public static final int SET_ARTIFACT_CATEGORY = 8;
    public static final int MARK_ARTIFACT = 9;
    public static final int DISPLAY_WALLET_DETAILS = 10;
    public static final int EXIT = 0;

    public boolean start() {
        view.displayMentorMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();

        switch(userChoice) {
            case CREATE_CODECOOLER: createCodecooler();
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
            case EXIT:
                return false;
        }

        view.enterToContinue();

        return true;
    }

    public void createCodecooler() {
        String[] userData = view.getUserData();

        String name = userData[0];
        String surname = userData[1];
        String email = userData[2];
        LocalDate dateOfBirth = LocalDate.parse(userData[3]);
        Codecooler codecooler = new Codecooler(name, surname, email, dateOfBirth);

        // Demo:
        System.out.println("You create new Codecooler: " + codecooler);
    }

    public void addQuest() {
        try {
            String[] questData = view.getQuestData();
            String name = questData[0];
            Integer reward = Integer.valueOf(questData[1]);
            Quest quest = new Quest(name, reward);
            // Demo:
            System.out.println("You create new quest: " + quest);
        }
        catch (NumberFormatException e) {
            System.out.println("Following error occured: " + e);
            System.out.println("Reward must be number !");
        }
    }

    public void updateQuest() {
        Quest quest = chooseQuest();
        int option = view.chooseEdit();

        if (option == 1) {  //edit name
            String name = view.takeInput("New name: ");
            quest.setName(name);

        } else if (option == 2) {   // edit coolcoins
            int coolcoins = view.getUserChoice();
            Integer reward = new Integer(coolcoins);
            quest.setReward(reward);

        } else {
            view.printErrorMessage();
        }

        // Demo:
        System.out.println("\nUpdate: done");
    }

    public void setQuestCategory() {
        // basic or extra
        Quest quest = chooseQuest();
        String category = view.chooseQuestCategory();
        quest.setCategory(category);

        // Demo
        view.enterToContinue();
        System.out.println("Quest category: " + category);
    }

    public Quest chooseQuest() {
        // QuestDao: readAll -> questList
        // choose specific quest and return it

        // Demo:
        System.out.println("\nChoose Quest:\nBasic\n1. Finishing Teamwork\nExtra\n2. Master of mornings \n3. Recruiting some n00bs");
        view.enterToContinue();
        System.out.println("\nYour choice: Master of mornings");

        Quest quest = new Quest("Master of mornings", 100);
        return quest;
    }

    public void markQuest() {
        // Demo:
        System.out.println("Codecooler: Anna Nowak; Quest: Master of mornings");
        System.out.println("Confirm? (y/n)");
        view.enterToContinue();
        System.out.println("Marked");
    }

    public void addArtifact() {
        try {
            String[] artifactData = view.getArtifactData();
            String name = artifactData[0];
            Integer value = Integer.valueOf(artifactData[1]);
            Artifact artifact = new Artifact(name, value);
            // Demo:
            System.out.println("You create new artifact: " + artifact);
        }
        catch (NumberFormatException e) {
            System.out.println("Following error occured: " + e);
            System.out.println("Reward must be number !");
        }
    }

    public void updateArtifact() {
        Artifact artifact = chooseArtifact();
        int option = view.chooseEdit();

        if (option == 1) {  //edit name
            String name = view.takeInput("New name: ");
            artifact.setName(name);

        } else if (option == 2) {   // edit coolcoins
            int coolcoins = view.getUserChoice();
            Integer value = new Integer(coolcoins);
            artifact.setValue(value);

        } else {
            view.printErrorMessage();
        }

        // Demo:
        System.out.println("\nUpdate: done");
    }

    public void setArtifactCategory() {
        // basic or extra
        Artifact artifact = chooseArtifact();
        String type = view.chooseArtifactType();
        artifact.setType(type);

        // Demo:
        view.enterToContinue();
        System.out.println("Artifact category: " + type);
    }

    public Artifact chooseArtifact() {
        // ArtifactDao: readAll -> artifactList
        // choose specific artifact and return it

        // Demo:
        System.out.println("\nChoose artifact:\nBasic\n1. Private mentoring \n2. Day in home office\nMagic\n3. Workshop 60min.");
        view.enterToContinue();
        System.out.println("\nYour choice: Private Mentoring");
        Artifact artifact = new Artifact("Private Mentoring", 50);
        return artifact;
    }

    public void markArtifact() {
        // Demo:
        System.out.println("Codecooler: Jan Kowalski want to buy artifact: day in home");
        System.out.println("Confirm? (y/n)");
        view.enterToContinue();
        System.out.println("Marked");
    }

    public void displayWalletDetails() {
        // Demo:
        System.out.println("Wallet details:");
        System.out.println("\nJan Kowalski\nBalanse: 200 coolcoins\nAlreadyBought: Day in home");
        System.out.println("\nAnna Nowak\nBalanse: 100 coolcoins\nAlreadyBought: Private Mentoring");
    }
}
