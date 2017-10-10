package pl.coderampart;

import pl.coderampart.controller.*;
import pl.coderampart.view.*;
import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Logger {

    private LoggerView view = new LoggerView();
    private AdminDAO adminDAO;
    private MentorDAO mentorDAO;
    private CodecoolerDAO codecoolerDAO;
    private AdminController adminController;
    private MentorController mentorController;
    private CodecoolerController codecoolerController;
    private ConnectionToDB connectionToDB;
    private Connection connection;

    public Logger() {

        connectionToDB = ConnectionToDB.getInstance();

        try {
            connection = connectionToDB.connectToDataBase();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        adminDAO = new AdminDAO(connection);
        mentorDAO  = new MentorDAO(connection);
        adminController = new AdminController(connection);
        codecoolerController = new CodecoolerController(connection);
        codecoolerDAO = new CodecoolerDAO(connection);
//        mentorController  = new MentorController(connection);
        logIn();
    }


    public void logIn() {

        view.displayLoggerMenu();
        String chosenOption = view.getRegExInput("^[0-3]$", "Choose option (0-3): ");

        //TODO: use enum here
        switch (chosenOption) {
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
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                System.exit(0);
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
            loggedMentor = mentorDAO.getLogged(email, password);
            if (loggedMentor != null) {
                mentorController.start(loggedMentor);
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
            loggedCodecooler = codecoolerDAO.getLogged(email, password);

            if (loggedCodecooler != null) {
                boolean proceed = true;
                while (proceed) {
                    proceed = codecoolerController.start(loggedCodecooler);
                }
            } else {
                view.output("Wrong data");
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}