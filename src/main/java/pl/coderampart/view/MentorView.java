package pl.coderampart.view;

import java.util.ArrayList;
import java.util.Arrays;

public class MentorView extends View{


    public void displayMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Add quest", "Update quest",
                "Set quest category", "Mark quest", "Add artifact", "Update artifact",
                "Set artifact type", "Mark artifact", "Display wallet details"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayCodecoolerMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Edit a Codecooler",
                "Display all Codecoolers", "createAchievement", "markItem", "displayWallet"));

        displayOptions(options);
        this.output("\n0. Back to main menu");
    }

    public void displayQuestMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new quest", "Edit a quest",
                "Display all quests", "Delete a quest"));

        displayOptions(options);
        this.output("\n0. Back to main menu");
    }

    public String[] getQuestData() {
        String name = getInput("Name: ");
        String description = getInput("Description: ");
        String reward = getInput("Reward (coolcoins): ");

        String[] questData = new String[] {name, description, reward};

        return questData;
    }

    public String[] getArtifactData() {
        String name = getInput("Name: ");
        String description = getInput("Description: ");
        String type = getInput("Type (single/group): ");
        String value = getInput("Value (coolcoins): ");

        String[] artifactData = new String[] {name, value};

        return artifactData;
    }



}
