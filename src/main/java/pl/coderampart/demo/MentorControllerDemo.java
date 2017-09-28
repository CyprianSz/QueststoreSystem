package pl.coderampart.demo;


import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import java.lang.NumberFormatException;

import pl.coderampart.model.Mentor;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Item;
import pl.coderampart.model.Quest;
import pl.coderampart.model.Artifact;

import java.time.LocalDate;

public class MentorControllerDemo implements Bootable {

    private View view = new View();

    // TODO: implement enums here
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
        String password = "samplePassword";
        LocalDate dateOfBirth = LocalDate.parse(userData[3]);
        Codecooler codecooler = new Codecooler(name, surname, email, password, dateOfBirth);

        // Demo:
        view.output("You've created a new Codecooler: " + codecooler);
    }

    public void addQuest() {
        try {
            String[] questData = view.getQuestData();
            String name = questData[0];
            Integer reward = Integer.valueOf(questData[1]);
            Quest quest = new Quest(name, reward);
            // Demo:
            view.output("You create new quest: " + quest);
        }
        catch (NumberFormatException e) {
            view.output("Following error occured: " + e);
            view.output("Reward must be number !");
        }
    }

    public void updateQuest() {
//        Quest quest = chooseQuest();
//        int option = view.chooseEdit();
//
//        if (option == 1) {  //edit name
//            String name = view.getInput("New name: ");
//            quest.setName(name);
//
//        } else if (option == 2) {   // edit coolcoins
//            int coolcoins = view.getUserChoice();
//            Integer reward = new Integer(coolcoins);
//            quest.setReward(reward);
//
//        } else {
//            view.printErrorMessage();
//        }
//
//        // Demo:
//        view.output("\nUpdate: done");
    }

    public void setQuestCategory() {
        // basic or extra
        Quest quest = chooseQuest();
        String category = view.chooseQuestCategory();
        quest.setCategory(category);

        // Demo
        view.enterToContinue();
        view.output("Quest category: " + category);
    }

    public Quest chooseQuest() {
        // QuestDao: readAll -> questList
        // choose specific quest and return it

        // Demo:
        view.output("\nChoose Quest:\nBasic\n1. Finishing Teamwork\nExtra\n2. Master of mornings \n3. Recruiting some n00bz");
        view.enterToContinue();
        view.output("\nYour choice: Master of mornings");

        Quest quest = new Quest("Master of mornings", 100);
        return quest;
    }

    public void markQuest() {
        // Demo:
        view.output("Codecooler: Anna Nowak; Quest: Master of mornings");
        view.output("Confirm? (y/n)");
        view.enterToContinue();
        view.output("Marked");
    }

    public void addArtifact() {
        try {
            String[] artifactData = view.getArtifactData();
            String name = artifactData[0];
            String type = "normal";
            Integer value = Integer.valueOf(artifactData[1]);
            Artifact artifact = new Artifact(name, type, value);
            // Demo:
            view.output("You create new artifact: " + artifact);
        }
        catch (NumberFormatException e) {
            view.output("Following error occured: " + e);
            view.output("Reward must be number !");
        }
    }

    public void updateArtifact() {
//        Artifact artifact = chooseArtifact();
//        int option = view.chooseEdit();
//
//        if (option == 1) {  //edit name
//            String name = view.getInput("New name: ");
//            artifact.setName(name);
//
//        } else if (option == 2) {   // edit coolcoins
//            int coolcoins = view.getUserChoice();
//            Integer value = new Integer(coolcoins);
//            artifact.setValue(value);
//
//        } else {
//            view.printErrorMessage();
//        }
//
//        // Demo:
//        view.output("\nUpdate: done");
    }

    public void setArtifactCategory() {
        // basic or extra
        Artifact artifact = chooseArtifact();
        String type = view.chooseArtifactType();
        artifact.setType(type);

        // Demo:
        view.enterToContinue();
        view.output("Artifact category: " + type);
    }

    public Artifact chooseArtifact() {
        // ArtifactDao: readAll -> artifactList
        // choose specific artifact and return it

        // Demo:
        view.output("\nChoose an artifact:\nBasic\n1. Private mentoring \n2. Day in home office\nMagic\n3. Workshop 60min.");
        view.enterToContinue();
        view.output("\nYour choice: Private Mentoring");
        String type = "normal";
        Artifact artifact = new Artifact("Private Mentoring", type, 50);
        return artifact;
    }

    public void markArtifact() {
        // Demo:
        view.output("Codecooler: Jan Kowalski wants to buy an artifact: day in home");
        view.output("Confirm? (y/n)");
        view.enterToContinue();
        view.output("Marked");
    }

    public void displayWalletDetails() {
        // Demo:
        view.output("Wallet details:");
        view.output("\nJan Kowalski\nBalance: 200 coolcoins\nBought artifacts: Day in home");
        view.output("\nAnna Nowak\nBalance: 100 coolcoins\nBought artifacts: Private Mentoring");
    }
}
