package pl.coderampart;

import pl.coderampart.controller.AdminController;
import pl.coderampart.demo.MentorControllerDemo;
import pl.coderampart.view.*;

public class Logger {

    private LoggerView view = new LoggerView();
    private AdminController adminController = new AdminController();
    private MentorController mentorController = new MentorController();

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
                this.logInAsMentor();
                break;
            case "3":
                this.logInAsCodecooler();
                break;
            case "0":
                break;
        }
    }

    private void logInAsAdmin(String email, String password) {

    }

    private void logInAsMentor(String email, String password) {

    }

    private void logInAsCodecooler(String email, String password) {

    }
}