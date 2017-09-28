package pl.coderampart.controller;

import pl.coderampart.model.Admin;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import java.util.ArrayList;
import java.util.Arrays;
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

        String mentor
        final int EDIT_FIRSTNAME = 1;
        final int EDIT_LASTNAME = 2;
        final int EDIT_EMAIL = 3;
        final int EDIT_PASSWORD = 4;
        final int EDIT_BIRTHDATE = 5;

        ArrayList<String> mentorOptions = new ArrayList<>(Arrays.asList("Edit first name.", "Edit last name.",
                                                                        "Edit email address.", "Edit password.",
                                                                        "Edit birth date."));

        view.displayOptions(mentorOptions);

        int editChoice = view.getUserChoice();

        view.clearTerminal();

        switch(editChoice) {

            case EDIT_FIRSTNAME:
                break;
            case EDIT_LASTNAME:
                break;
            case EDIT_EMAIL:
                break;
            case EDIT_PASSWORD:
                break;
            case EDIT_BIRTHDATE:
                break;
        }
    }

    public void displayMentors(){

    }

    public void createGroup(){

    }

    public void createLevel(){

    }

}
