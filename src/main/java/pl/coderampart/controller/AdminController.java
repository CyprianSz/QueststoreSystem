package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.GroupSectionOption;
import pl.coderampart.enums.LevelSectionOption;
import pl.coderampart.enums.MentorSectionOption;
import pl.coderampart.enums.AdminSubmenuOption;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.AdminView;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminController implements Bootable<Admin> {

    private AdminView adminView = new AdminView();
    private MentorDAO mentorDAO = new MentorDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private LevelDAO levelDAO = new LevelDAO();


    public boolean start(Admin admin) {

        adminView.displayAdminMenu();
        int userChoice = adminView.getUserChoice();

        AdminSubmenuOption adminSubmenuOption = AdminSubmenuOption.values()[userChoice];
        adminView.clearTerminal();
        switch (adminSubmenuOption) {

            case DISPLAY_MENTOR_MANAGEMENT_MENU:
                startMentorManagementMenu();
                break;
            case DISPLAY_CODECOOLER_MANAGEMENT_MENU:
                startGroupManagementMenu();
                break;
            case DISPLAY_LEVEL_MANAGEMENT_MENU:
                startLevelManagementMenu();
                break;
            case EXIT:
                return false;

        }
        adminView.enterToContinue();
        return true;
    }

    public boolean startMentorManagementMenu() {

        adminView.displayManagingMentorMenu();
        int userChoice = adminView.getUserChoice();

        MentorSectionOption mentorSectionOption = MentorSectionOption.values()[userChoice];
        adminView.clearTerminal();
        switch (mentorSectionOption) {
            case CREATE_MENTOR:
                createMentor();
                break;
            case DISPLAY_MENTORS:
                displayMentors();
                break;
            case EDIT_MENTOR:
                editMentor();
                break;
            case DELETE_MENTOR:
                deleteMentor();
                break;
            case BACK_TO_MAIN_MENU:
                return false;

        }
        adminView.enterToContinue();
        return true;

    }

    public boolean startGroupManagementMenu() {

        adminView.displayCodecoolerManagingMenu();
        int userChoice = adminView.getUserChoice();

        GroupSectionOption groupSectionOption = GroupSectionOption.values()[userChoice];
        adminView.clearTerminal();

        switch (groupSectionOption) {
            case CREATE_GROUP:
                createGroup();
                break;
            case EDIT_GROUP:
                editGroup();
                break;
            case DISPLAY_GROUPS:
                displayGroups();
                break;
            case DELETE_GROUP:
                deleteGroup();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        adminView.enterToContinue();
        return true;
    }

    public boolean startLevelManagementMenu() {

        adminView.displayLevelManagingMenu();
        int userChoice = adminView.getUserChoice();

        LevelSectionOption levelSectionOption = LevelSectionOption.values()[userChoice];
        adminView.clearTerminal();

        switch (levelSectionOption) {
            case CREATE_LEVEL:
                createGroup();
                break;
            case EDIT_LEVEL:
                editGroup();
                break;
            case DISPLAY_LEVELS:
                displayGroups();
                break;
            case DELETE_LEVEL:
                deleteGroup();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        adminView.enterToContinue();
        return true;
    }


    public void createMentor(){

        this.displayMentors();

        String[] mentorData = adminView.getUserData();

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], adminView.stringToDate(mentorData[2]),
                                      mentorData[3], mentorData[4]);

        this.displayGroups();
        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = adminView.getInput("Enter name of a group you wish to assign this mentor to: ");
        for (Group group: allGroups){
            String groupName = group.getName();

            if (groupName.equals(chosenGroupName)){
                newMentor.setGroup(group);
            }
        }

        mentorDAO.create(newMentor);
    }

    public void createGroup(){
        this.displayGroups();
        String[] groupData = adminView.getGroupData();

        Group newGroup = new Group(groupData[0]);

        groupDAO.create(newGroup);
    }

    public void createLevel(){
        this.displayLevels();
        String[] levelData = adminView.getLevelData();

        Level newLevel = new Level(Integer.parseInt(levelData[0]), Integer.parseInt(levelData[1]), levelData[2]);

        levelDAO.create(newLevel);
    }

    public void editMentor(){
        this.displayMentors();

        Mentor changedMentor = null;

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = adminView.getInput("Enter email of a mentor you wish to edit: ");

        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                changedMentor = mentor;
                break;
            }
        }

        if (!changedMentor.equals(null)) {
            final int EDIT_FIRSTNAME = 1;
            final int EDIT_LASTNAME = 2;
            final int EDIT_EMAIL = 3;
            final int EDIT_PASSWORD = 4;
            final int EDIT_BIRTHDATE = 5;

            ArrayList<String> editMentorOptions = new ArrayList<>(Arrays.asList("Edit first name", "Edit last name,",
                                                                                "Edit email", "Edit password",
                                                                                "Edit birthdate"));
            adminView.displayOptions(editMentorOptions);
            int userChoice = adminView.getUserChoice();
            adminView.clearTerminal();

            switch(userChoice){
                case EDIT_FIRSTNAME:
                    changedMentor.setFirstName(adminView.getInput("Enter new name: "));
                    break;
                case EDIT_LASTNAME:
                    changedMentor.setLastName(adminView.getInput("Enter new name: "));
                    break;
                case EDIT_EMAIL:
                    changedMentor.setEmail(adminView.getRegExInput(adminView.emailRegEx, "Enter new email: "));
                    break;
                case EDIT_PASSWORD:
                    changedMentor.setPassword(adminView.getInput("Enter new password: "));
                    break;
                case EDIT_BIRTHDATE:
                    changedMentor.setDateOfBirth(adminView.stringToDate(adminView.getRegExInput(adminView.dateRegEx,
                                                                                      "Enter new date")));
                    break;
            }
            mentorDAO.update(changedMentor);

        }
    }

    public void editGroup(){
        this.displayGroups();

        Group changedGroup = null;

        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = adminView.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                changedGroup = group;
            }
        }

        if(!changedGroup.equals(null)){
            changedGroup.setName(adminView.getInput("Enter new name: "));
        }

        groupDAO.update(changedGroup);
    }

    public void editLevel(){
        this.displayLevels();

        Level changedLevel = null;

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = adminView.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                changedLevel = level;
            }
        }

        if (!changedLevel.equals(null)) {
            final int EDIT_RANK = 1;
            final int EDIT_REQEXP = 2;
            final int EDIT_DESCRIPTION = 3;

            ArrayList<String> editLevelOptions = new ArrayList<>(Arrays.asList("Edit rank", "Edit required experience,",
                                                                                "Edit description"));
            adminView.displayOptions(editLevelOptions);
            int userChoice = adminView.getUserChoice();
            adminView.clearTerminal();

            switch(userChoice){
                case EDIT_RANK:
                    changedLevel.setRank(Integer.parseInt(adminView.getInput("Enter new rank: ")));
                    break;
                case EDIT_REQEXP:
                    changedLevel.setRequiredExperience(Integer.parseInt(adminView.getInput("Enter new value: ")));
                    break;
                case EDIT_DESCRIPTION:
                    changedLevel.setDescription(adminView.getInput("Enter new email: "));
                    break;
            }
            levelDAO.update(changedLevel);
        }
    }

    public void displayMentors(){
        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        ArrayList<String> mentorData = new ArrayList<>();

        for (Mentor mentor: allMentors){
            mentorData.add(mentor.toString());
        }

        adminView.outputTable(mentorData);
    }

    public void displayGroups(){
        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupStrings = new ArrayList<String>();

        for (Group group: allGroups){
            groupStrings.add(group.toString());
        }

        adminView.outputTable(groupStrings);
    }

    public void displayLevels(){
        ArrayList<Level> allLevels = levelDAO.readAll();
        ArrayList<String> levelStrings = new ArrayList<String>();

        for (Level level: allLevels){
            levelStrings.add(level.toString());
        }

        adminView.outputTable(levelStrings);
    }

    public void deleteMentor() {
        this.displayMentors();

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = adminView.getInput("Enter email of a mentor you wish to delete: ");

        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                mentorDAO.delete(mentor);
                break;
            }
        }
    }

    public void deleteGroup(){
        this.displayGroups();

        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = adminView.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                groupDAO.delete(group);
            }
        }
    }

    public void deleteLevel(){
        this.displayLevels();

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = adminView.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                levelDAO.delete(level);
            }
        }
    }
}
