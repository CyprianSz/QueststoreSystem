package pl.coderampart.controller;

import pl.coderampart.model.Admin;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import pl.coderampart.model.Mentor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // view.displayAdminMenu(); //TODO: ADD THIS METHOD TO VIEW
        int userChoice = view.getUserChoice();

        view.clearTerminal();

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

        String mentorFirstName = view.getInput("Enter first name of a new mentor:");
        String mentorLastName = view.getInput("Enter last name of a new mentor:");
        String birthdateData = view.getInput("Enter mentor's birth date: (dd-mm-yyyy)");
        DateTimeFormatter dupa = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        LocalDate mentorBirthDate = LocalDate.parse(birthdateData, dupa);
        String mentorPassword = view.getInput("Enter mentor's password.");
        String mentorEmail = view.getInput("Enter mentor's email address:");

        Mentor newMentor = new Mentor(mentorFirstName, mentorLastName, mentorEmail, mentorPassword, mentorBirthDate);
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
