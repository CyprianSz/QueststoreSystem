package pl.coderampart.controller;

import pl.coderampart.model.Admin;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import java.time.LocalDate;
import java.util.Locale;
// import pl.coderampart.DAO.MentorDAO;
// import pl.coderampart.DAO.AdminDAO;

public class AdminController implements Bootable {

    private View view = new View();

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
    }

    public void editMentor(){

    }

    public void displayMentors(){

    }

    public void createGroup(){

    }

    public void createLevel(){

    }

}
