package pl.coderampart.controller;

import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.model.Level;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import pl.coderampart.model.Group;
import java.util.ArrayList;

public class AdminController implements Bootable {

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

    public boolean start() {
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

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], mentorData[2], mentorData[3],
                                      view.stringToDate(mentorData[4]));

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

        ArrayList<Mentor> allMentors = mentorDAO.readAll();
        String chosenMentorEmail = view.getInput("Enter email of a mentor you wish to edit: ");

        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                mentorDAO.update(mentor);
            }
        }
    }

    public void editGroup(){
        this.displayGroups();

        ArrayList<Group> allGroups = groupDAO.readAll();
        String chosenGroupName = view.getInput("Enter name of a group you wish to edit: ");

        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                groupDAO.update(group);
            }
        }
    }

    public void editLevel(){
        this.displayLevels();

        ArrayList<Level> allLevels = levelDAO.readAll();
        String chosenLevelRank = view.getInput("Enter rank of a level you wish to edit: ");

        for (Level level: allLevels){
            if (chosenLevelRank.equals(Integer.toString(level.getRank()))){
                groupDAO.update(level);
            }
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
                groupDAO.delete(level);
            }
        }
    }
}
