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
    private MentorDAO mentorDao = new MentorDAO();
    private GroupDAO groupDao = new GroupDAO();
    private LevelDAO levelDao = new LevelDAO();

    public static final int CREATE_MENTOR = 1;
    public static final int CREATE_GROUP = 2;
    public static final int CREATE_LEVEL = 3;
    public static final int EDIT_MENTOR = 4;
    public static final int EDIT_GROUP = 5;
    public static final int EDIT_LEVEL = 6;
    public static final int DISPLAY_MENTORS = 7;
    public static final int DISPLAY_GROUPS = 8;
    public static final int DISPLAY_LEVELS = 9;
    public static final int DELETE_MENTOR = 10;
    public static final int DELETE_GROUP = 11;
    public static final int DELETE_LEVEL = 12;


    public static final int EXIT = 0;

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
        ArrayList<Group> allGroups = groupDao.readAll();
        String chosenGroupName = view.getInput("Enter name of a group you wish to assign this mentor to: ");
        for (Group group: allGroups){
            if (chosenGroupName.equals(group.getName())){
                newMentor.setGroup(group);
            }
        }
        mentorDao.create(newMentor);
    }

    public void createGroup(){
        this.displayGroups();
        String[] groupData = view.getGroupData();

        Group newGroup = new Group(groupData[0]);

        groupDao.create(newGroup);
    }

    public void createLevel(){
        this.displayLevels();
        String[] levelData = view.getLevelData();

        Level newLevel = new Level(Integer.parseInt(levelData[0]), Integer.parseInt(levelData[1]), levelData[2]);

        levelDao.create(newLevel);
    }

    public void editMentor(){
        this.displayMentors();
        ArrayList<Mentor> allMentors = mentorDao.readAll();
        String chosenMentorEmail = view.getInput("Enter email of a mentor you wish to edit:");
        for (Mentor mentor: allMentors){
            if (chosenMentorEmail.equals(mentor.getEmail())){
                mentorDao.update(mentor);
            }
        }
    }

    public void editGroup(){

    }

    public void editLevel(){

    }

    public void displayMentors(){
        ArrayList<Mentor> allMentors = mentorDao.readAll();
        ArrayList<String> mentorStrings = new ArrayList<String>();

        for (Mentor mentor: allMentors){
            mentorStrings.add(mentor.toString());
        }

        view.outputTable(mentorStrings);
    }

    public void displayGroups(){
        ArrayList<Group> allGroups = groupDao.readAll();
        ArrayList<String> groupStrings = new ArrayList<String>();

        for (Group group: allGroups){
            groupStrings.add(group.toString());
        }

        view.outputTable(groupStrings);
    }

    public void displayLevels(){
        ArrayList<Level> allLevels = levelDao.readAll();
        ArrayList<String> levelStrings = new ArrayList<String>();

        for (Level level: allLevels){
            levelStrings.add(level.toString());
        }

        view.outputTable(levelStrings);
    }

    public void deleteMentor(){

    }

    public void deleteGroup(){

    }

    public void deleteLevel(){

    }
}
