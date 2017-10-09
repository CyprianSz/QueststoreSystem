package pl.coderampart;

import pl.coderampart.controller.*;
import pl.coderampart.view.*;
import pl.coderampart.DAO.*;
import pl.coderampart.model.*;

public class Logger {

    private LoggerView view = new LoggerView();
    private AdminDAO adminDAO = new AdminDAO();
    private MentorDAO mentorDAO = new MentorDAO();
    private CodecoolerDAO codecoolerDAO = new CodecoolerDAO();
    private AdminController adminController = new AdminController();
    //private MentorController mentorController = new MentorController();
    private CodecoolerController codecoolerController = new CodecoolerController();

    public void logIn() {
        view.displayLoggerMenu();
        String chosenOption = view.getRegExInput("^[0-3]$", "Choose option (0-3): ");

        //TODO: use enum here
        switch(chosenOption) {
            case "1":
                this.logInAsAdmin();
                break;
            case "2":
                this.logInAsMentor();
                break;
            case "3":
                this.logInAsCodecooler();
                break;
            case "0":
                System.exit(0);
                break;
        }
    }

    private void logInAsAdmin() {
        String email = view.getInput("Email: ");
        String password = view.getInput("Password: ");
        Admin loggedAdmin = null;

        try {
            loggedAdmin = this.adminDAO.getLogged(email, password);

            if (loggedAdmin != null) {
                boolean proceed = true;
                while (proceed) {
                    proceed = this.adminController.start(loggedAdmin);
                }
            } else {
                view.output("Wrong data");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void logInAsMentor() {
        String email = view.getInput("Email: ");
        String password = view.getInput("Password: ");
        Mentor loggedMentor = null;

        try {
            loggedMentor = this.mentorDAO.getLogged(email, password);
            if (loggedMentor != null) {
                //this.mentorController.start(loggedMentor);
            } else {
                view.output("Wrong data");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void logInAsCodecooler() {
        String email = view.getInput("Email: ");
        String password = view.getInput("Password: ");
        Codecooler loggedCodecooler = null;

        try {
            loggedCodecooler = this.codecoolerDAO.getLogged(email, password);

            if (loggedCodecooler != null) {
                boolean proceed = true;
                while (proceed) {
                    proceed = this.codecoolerController.start(loggedCodecooler);
                }
            } else {
                view.output("Wrong data");
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}