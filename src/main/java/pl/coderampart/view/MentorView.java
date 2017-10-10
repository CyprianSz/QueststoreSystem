package pl.coderampart.view;

import pl.coderampart.model.Item;

import java.util.ArrayList;
import java.util.Arrays;

public class MentorView extends View{

    public void displayMentorManagementMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Codecooler Management Menu", "Quest Management Menu",
                "Artifact Management Menu", "Team Management Menu"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayCodecoolerMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Edit a Codecooler",
                "Display all Codecoolers", "Reward an achievement", "Mark an item", "Display wallet"));

        displayOptions(options);
        this.output("\n0. Back to main menu");
    }

    public void displayQuestMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new quest", "Edit a quest",
                "Display all quests", "Delete a quest"));

        displayOptions(options);
        this.output("\n0. Back to main menu");
    }

    public void displayArtifactMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new artifact", "Edit an artifact",
                "Display all artifacts", "Delete an artifact"));

        displayOptions(options);
        this.output("\n0. Back to main menu");
    }

    public void displayTeamMM(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new team", "Edit a team",
                "Display all teams", "Delete a team"));

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

        String[] artifactData = new String[] {name, description, type, value};

        return artifactData;
    }

    public void displayUserItems(ArrayList<Item> userItems) {

        this.output("\nCodecooler items:");
        int itemIndex = 0;
        for (Item item: userItems) {
            this.output(String.valueOf(itemIndex) + ". " + item.getArtifact().toString());
            this.output("Date: " + item.dateToString() );
            itemIndex ++;
        }
    }



}
