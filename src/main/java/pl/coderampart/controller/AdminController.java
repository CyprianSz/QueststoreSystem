package pl.coderampart.controller;

import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import pl.coderampart.model.Group;
import java.util.ArrayList;

public class AdminController implements Bootable {

    private View view = new View();
    private MentorDAO mentorDao = new MentorDAO();
    private GroupDAO groupDao = new GroupDAO();

    public static final int CREATE_MENTOR = 1;
    public static final int CREATE_GROUP = 2;
    public static final int EDIT_MENTOR = 3;
    public static final int DISPLAY_MENTOR = 4;
    public static final int DISPLAY_GROUP = 5;
    public static final int CREATE_LEVEL = 6;
    public static final int EXIT = 0;

    public boolean start() {
        view.displayAdminMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();
        // TODO: USE ENUM
        switch(userChoice) {

            case CREATE_MENTOR: createMentor();
                break;
            case CREATE_GROUP: createGroup();
                break;
            case EDIT_MENTOR: editMentor();
                break;
            case DISPLAY_MENTOR: displayMentors();
                break;
            case DISPLAY_GROUP: displayGroups();
                break;
            case CREATE_LEVEL: createLevel();
                break;
            case EXIT:
                return false;
        }

        view.enterToContinue();

        return true;
    }

    public void createMentor(){

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

    public void createGroup(){
        String[] groupData = view.getGroupData();

        Group newGroup = new Group(groupData[0]);

        groupDao.create(newGroup);
    }

    public void createLevel(){

    }

}
