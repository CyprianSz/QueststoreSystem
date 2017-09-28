package pl.coderampart.controller;

import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.model.Admin;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import java.util.ArrayList;
import java.util.Arrays;
// import pl.coderampart.DAO.AdminDAO;

public class AdminController implements Bootable {

    private View view = new View();
    private MentorDAO mentorDao = new MentorDAO();

    public static final int CREATE_MENTOR = 1;
    public static final int CREATE_GROUP = 2;
    public static final int EDIT_MENTOR = 3;
    public static final int DISPLAY_MENTOR = 4;
    public static final int CREATE_LEVEL = 5;
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

        mentorDao.create(newMentor);
    }

    public void editMentor(){

    }

    public void displayMentors(){
        ArrayList<Mentor> mentorList = mentorDao.readAll();
        ArrayList<String> mentorStrings = new ArrayList<String>();

        for (Mentor mentor: mentorList){
            mentorStrings.add(mentor.toString());
        }

        view.outputTable(mentorStrings);
    }

    public void createGroup(){

    }

    public void createLevel(){

    }

}
