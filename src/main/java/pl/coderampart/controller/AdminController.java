package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.AdminView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminController implements Bootable<Admin> {

    private AdminView adminView = new AdminView();
    private LevelDAO levelDAO;
    private GroupDAO groupDAO;
    private MentorDAO mentorDAO;
    private Connection connection;

    public AdminController(Connection connectionToDB) {

        connection = connectionToDB;
        mentorDAO = new MentorDAO(connection);
        groupDAO = new GroupDAO(connection);
        levelDAO = new LevelDAO(connection);
    }

    public boolean start(Admin admin) throws SQLException {

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

    public boolean startMentorManagementMenu() throws SQLException {

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
        return true;

    }

    public boolean startGroupManagementMenu() throws SQLException {

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
        return true;
    }

    public boolean startLevelManagementMenu() throws SQLException {

        adminView.displayLevelManagingMenu();
        int userChoice = adminView.getUserChoice();

        LevelSectionOption levelSectionOption = LevelSectionOption.values()[userChoice];
        adminView.clearTerminal();

        switch (levelSectionOption) {
            case CREATE_LEVEL:
                createLevel();
                break;
            case EDIT_LEVEL:
                editLevel();
                break;
            case DISPLAY_LEVELS:
                displayLevels();
                break;
            case DELETE_LEVEL:
                deleteLevel();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        return true;
    }


    public void createMentor() throws SQLException{
        String chosenGroupName;

        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupsNames = new ArrayList<>();
        String[] mentorData = adminView.getUserData();

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], adminView.stringToDate(mentorData[2]),
                                      mentorData[3], mentorData[4]);
        this.displayGroups();


        for (Group group: allGroups){
            String groupName = group.getName();
            groupsNames.add(groupName);
        }

        do {
            chosenGroupName = adminView.getInput("Enter name of a group you wish to assign this mentor to: ");
        } while (!groupsNames.contains(chosenGroupName));

        for (Group group : allGroups) {
            if (group.getName().equals(chosenGroupName)) {
                newMentor.setGroup( group );

            }
        }
        mentorDAO.create(newMentor);
    }

    public void createGroup() throws SQLException {
        String[] groupData = adminView.getGroupData();

        Group newGroup = new Group(groupData[0]);
        groupDAO.create(newGroup);

    }

    public void createLevel() throws SQLException {
        this.displayLevels();
        String[] levelData = adminView.getLevelData();

        Level newLevel = new Level(Integer.parseInt(levelData[0]), Integer.parseInt(levelData[1]), levelData[2]);
        levelDAO.create(newLevel);

    }

    public void editMentor() throws SQLException {
        this.displayMentors();

        Mentor changedMentor = null;
        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = adminView.getInput("Enter email of a mentor you wish to edit: ");
        for (Mentor mentor: allMentors) {
            if (chosenMentorEmail.equals(mentor.getEmail())) {
                changedMentor = mentor;
                break;
            }
        }
        changeConcreteElementOfMentor(changedMentor);
    }


    private boolean changeConcreteElementOfMentor(Mentor changedMentor) throws SQLException {
        if (!changedMentor.equals(null)) {

            ArrayList<String> editMentorOptions = new ArrayList<>(Arrays.asList("Edit first name", "Edit last name,",
                    "Edit email", "Edit password",
                    "Edit birthdate"));

            adminView.displayOptions(editMentorOptions);
            int userChoice = adminView.getUserChoice();
            AdminEditingMentorChoice mentorChoice = AdminEditingMentorChoice.values()[userChoice];
            adminView.clearTerminal();

            switch (mentorChoice) {
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
                case BACK_TO_MAIN_MENU:
                    return false;
            }
            mentorDAO.update(changedMentor);
        }
        return true;
    }

    public void editGroup() throws SQLException {
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


    public void editLevel() throws SQLException {
        this.displayLevels();

        Level changedLevel = null;

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = adminView.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                changedLevel = level;
            }
        }
        changeConcreteElementOfLevel(changedLevel);
    }


    private boolean changeConcreteElementOfLevel(Level changedLevel) throws SQLException {
        if (changedLevel != null) {

            ArrayList<String> editLevelOptions = new ArrayList<>(Arrays.asList("Edit rank", "Edit required experience,",
                                                                                "Edit description"));
            adminView.displayOptions(editLevelOptions);
            int userChoice = adminView.getUserChoice();
            AdminEditingLevelOption adminEditingOption = AdminEditingLevelOption.values()[userChoice];
            adminView.clearTerminal();

            switch(adminEditingOption){
                case EDIT_RANK:
                    changedLevel.setRank(Integer.parseInt(adminView.getInput("Enter new rank: ")));
                    break;
                case EDIT_REQEXP:
                    changedLevel.setRequiredExperience(Integer.parseInt(adminView.getInput("Enter new required experience: ")));
                    break;
                case EDIT_DESCRIPTION:
                    changedLevel.setDescription(adminView.getInput("Enter new description: "));
                    break;
                case BACK_TO_MAIN_MENU:
                    return false;
            }
            levelDAO.update(changedLevel);
        }
        return true;
    }

    public void displayMentors() throws SQLException {

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        ArrayList<String> mentorData = new ArrayList<>();

        for (Mentor mentor: allMentors){
            mentorData.add(mentor.toString());
        }

        adminView.outputTable(mentorData);
    }

    public void displayGroups() throws SQLException {

        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupStrings = new ArrayList<String>();

        for (Group group: allGroups){
            groupStrings.add(group.toString());
        }

        adminView.outputTable(groupStrings);

    }

    public void displayLevels() throws SQLException {

        ArrayList<Level> allLevels = levelDAO.readAll();
        ArrayList<String> levelStrings = new ArrayList<String>();

        for (Level level : allLevels) {
            levelStrings.add(level.toString());
        }

        adminView.outputTable(levelStrings);
    }

    public void deleteMentor() throws SQLException {

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

    public void deleteGroup() throws SQLException {

        this.displayGroups();
        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = adminView.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                groupDAO.delete(group);
            }
        }
    }

    public void deleteLevel() throws SQLException {
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
