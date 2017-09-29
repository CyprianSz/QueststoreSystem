package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminController implements Bootable<Admin> {

    private View view = new View();
    private MentorDAO mentorDAO = new MentorDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private LevelDAO levelDAO = new LevelDAO();

    private static final int CREATE_MENTOR = 1;
    private static final int CREATE_GROUP = 2;
    private static final int CREATE_LEVEL = 3;
    private static final int EDIT_MENTOR = 4;
    private static final int EDIT_GROUP = 5;
    private static final int EDIT_LEVEL = 6;
    private static final int DISPLAY_MENTORS = 7;
    private static final int DISPLAY_GROUPS = 8;
    private static final int DISPLAY_LEVELS = 9;
    private static final int DELETE_MENTOR = 10;
    private static final int DELETE_GROUP = 11;
    private static final int DELETE_LEVEL = 12;
    private static final int EXIT = 0;

    public boolean start(Admin admin) {
        view.displayAdminMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();
        // TODO: USE ENUM, DECLARE SUBMENUS FOR EACH CLASS
        switch(userChoice) {

            case CREATE_MENTOR: createMentor();
                break;
            case CREATE_GROUP: createGroup();
                break;
            case CREATE_LEVEL: createLevel();
                break;
            case EDIT_MENTOR: editMentor();
                break;
            case EDIT_GROUP: editGroup();
                break;
            case EDIT_LEVEL: editLevel();
                break;
            case DISPLAY_MENTORS: displayMentors();
                break;
            case DISPLAY_GROUPS: displayGroups();
                break;
            case DISPLAY_LEVELS: displayLevels();
                break;
            case DELETE_MENTOR: deleteMentor();
                break;
            case DELETE_GROUP: deleteGroup();
                break;
            case DELETE_LEVEL: deleteLevel();
                break;
            case EXIT:
                return false;
        }

        view.enterToContinue();
        return true;
    }

    public void createMentor(){

        this.displayMentors();

        String[] mentorData = view.getUserData();

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], view.stringToDate(mentorData[2]),
                                      mentorData[3], mentorData[4]);

        this.displayGroups();
        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = view.getInput("Enter name of a group you wish to assign this mentor to: ");
        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                newMentor.setGroup(group);
            }
        }
        mentorDAO.create(newMentor);
    }

    public void createGroup(){
        this.displayGroups();
        String[] groupData = view.getGroupData();

        Group newGroup = new Group(groupData[0]);

        groupDAO.create(newGroup);
    }

    public void createLevel(){
        this.displayLevels();
        String[] levelData = view.getLevelData();

        Level newLevel = new Level(Integer.parseInt(levelData[0]), Integer.parseInt(levelData[1]), levelData[2]);

        levelDAO.create(newLevel);
    }

    public void editMentor(){
        this.displayMentors();

        Mentor changedMentor = null;

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = view.getInput("Enter email of a mentor you wish to edit: ");

        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                changedMentor = mentor;
            } else {
                view.output("No such mentor.");
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
            view.displayOptions(editMentorOptions);
            int userChoice = view.getUserChoice();
            view.clearTerminal();

            switch(userChoice){
                case EDIT_FIRSTNAME:
                    changedMentor.setFirstName(view.getInput("Enter new name: "));
                    break;
                case EDIT_LASTNAME:
                    changedMentor.setLastName(view.getInput("Enter new name: "));
                    break;
                case EDIT_EMAIL:
                    changedMentor.setEmail(view.getRegExInput(View.emailRegEx, "Enter new email: "));
                    break;
                case EDIT_PASSWORD:
                    changedMentor.setPassword(view.getInput("Enter new password: "));
                    break;
                case EDIT_BIRTHDATE:
                    changedMentor.setDateOfBirth(view.stringToDate(view.getRegExInput(View.dateRegEx,
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
        String chosenGroupName = view.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                changedGroup = group;
            } else {
                view.output("No such group.");
                break;
            }
        }

        if(!changedGroup.equals(null)){
            changedGroup.setName(view.getInput("Enter new name: "));
        }

        groupDAO.update(changedGroup);
    }

    public void editLevel(){
        this.displayLevels();

        Level changedLevel = null;

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = view.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                changedLevel = level;
            } else {
                view.output("No such level.");
                break;
            }
        }

        if (!changedLevel.equals(null)) {
            final int EDIT_RANK = 1;
            final int EDIT_REQEXP = 2;
            final int EDIT_DESCRIPTION = 3;

            ArrayList<String> editLevelOptions = new ArrayList<>(Arrays.asList("Edit rank", "Edit required experience,",
                                                                                "Edit description"));
            view.displayOptions(editLevelOptions);
            int userChoice = view.getUserChoice();
            view.clearTerminal();

            switch(userChoice){
                case EDIT_RANK:
                    changedLevel.setRank(Integer.parseInt(view.getInput("Enter new rank: ")));
                    break;
                case EDIT_REQEXP:
                    changedLevel.setRequiredExperience(Integer.parseInt(view.getInput("Enter new value: ")));
                    break;
                case EDIT_DESCRIPTION:
                    changedLevel.setDescription(view.getInput("Enter new email: "));
                    break;
            }
            levelDAO.update(changedLevel);
        }
    }

    public void displayMentors(){
        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        ArrayList<String> mentorStrings = new ArrayList<String>();

        for (Mentor mentor: allMentors){
            mentorStrings.add(mentor.toString());
        }

        view.outputTable(mentorStrings);
    }

    public void displayGroups(){
        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupStrings = new ArrayList<String>();

        for (Group group: allGroups){
            groupStrings.add(group.toString());
        }

        view.outputTable(groupStrings);
    }

    public void displayLevels(){
        ArrayList<Level> allLevels = levelDAO.readAll();
        ArrayList<String> levelStrings = new ArrayList<String>();

        for (Level level: allLevels){
            levelStrings.add(level.toString());
        }

        view.outputTable(levelStrings);
    }

    public void deleteMentor(){
        this.displayMentors();

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = view.getInput("Enter email of a mentor you wish to delete: ");

        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                mentorDAO.delete(mentor);
            }
        }
    }

    public void deleteGroup(){
        this.displayGroups();

        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = view.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                groupDAO.delete(group);
            }
        }
    }

    public void deleteLevel(){
        this.displayLevels();

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = view.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                levelDAO.delete(level);
            }
        }
    }
}
