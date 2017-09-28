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
        String choosenOption = view.getRegExInput("^[0-3]$", "Choose option (0-3): ");
        String email = view.getInput("Email: ");
        String password = view.getInput("Password: ");

        //TODO: use enum here
        switch(choosenOption) {
            case "1":
                this.logInAsAdmin(email, password);
                break;
            case "2":
                this.logInAsMentor(email, password);
                break;
            case "3":
                this.logInAsCodecooler(email, password);
                break;
            case "0":
                break;
        }
    }

    private void logInAsAdmin(String email, String password) {
        Admin loggedAdmin = this.adminDAO.getLogged(email, password);
        this.adminController.start(loggedAdmin);
    }

    private void logInAsMentor(String email, String password) {
        Mentor loggedMentor = this.mentorDAO.getLogged(email, password);
        //this.mentorController.start(loggedMentor);
    }

    private void logInAsCodecooler(String email, String password) {
        Codecooler loggedCodecooler = this.codecoolerDAO.getLogged(email, password);
        this.codecoolerController.start(loggedCodecooler);
    }
}