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


    public String[] getQuestData() {
        String name = getInput("Name: ");
        String description = getInput("Description: ");
        String reward = getInput("Reward (coolcoins): ");

        String[] questData = new String[] {name, description, reward};

        return questData;
    }

    public String[] getArtifactData() {
        String name = getInput("Name: ");
        String value = getInput("Value (coolcoins): ");

        String[] artifactData = new String[] {name, value};

        return artifactData;
    }



}
