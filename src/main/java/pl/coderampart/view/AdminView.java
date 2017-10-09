package pl.coderampart.view;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminView extends View{

    public void displayAdminMenu(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Manage mentor's section",
                "Manage codecooler section",
                "Manage experience level section"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayManagingMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new mentor.",
                "Edit mentor.",
                "Display all mentors.",
                "Delete mentor."));

        displayOptions(options);
        this.output(("\n0. Back to main menu"));
    }

    public void displayCodecoolerManagingMenu() {

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new group.",
                "Edit group.",
                "Display all groups.",
                "Delete group."));

        displayOptions(options);
        this.output(("\n0. Back to main menu"));
    }

    public void displayLevelManagingMenu() {

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new experience level.",
                "Edit experience level",
                "Display all experience levels.",
                "Delete experience level."));

        displayOptions(options);
        this.output(("\n0. Back to main menu"));
    }

    public String[] getLevelData(){
        String rank = getInput("Rank: ");
        String requiredExperience = getInput("Required experience: ");
        String description = getInput("Description: ");

        String[] levelData = new String[] {rank, requiredExperience, description};

        return levelData;
    }


    public String[] getGroupData() {
        String name = getInput("Name: ");

        String[] groupData = new String[] {name};

        return groupData;
    }
}
