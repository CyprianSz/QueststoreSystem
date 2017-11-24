package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.AdminDAO;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Admin;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Mentor;
import pl.coderampart.model.Session;
import pl.coderampart.services.Loggable;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class ChangePassword implements HttpHandler {

    private Connection connection;
    private AdminDAO adminDAO;
    private CodecoolerDAO codecoolerDAO;
    private MentorDAO mentorDAO;
    private PasswordHasher hasher;
    private HelperController helper;

    public ChangePassword(Connection connection) {
        this.connection = connection;
        this.adminDAO = new AdminDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.mentorDAO = new MentorDAO( connection );
        this.helper = new HelperController( connection );
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        Session currentSession = helper.getCurrentSession( httpExchange, connection );
        Loggable loggedUser = helper.getLoggedUser(currentSession);


        if (method.equals("GET")) {
            String response = "";
            String loggedUserType = loggedUser.getType().toLowerCase();
            String menuPath = loggedUserType + "/" + loggedUserType + "Menu";

            response += helper.renderHeader(httpExchange, connection);
            response += helper.render(menuPath);
            response += helper.render("changePassword");
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            changePassword(inputs, loggedUser);
            helper.redirectTo( "/account", httpExchange );
        }
    }

    private void changePassword(Map<String, String> inputs, Loggable loggable) {
        String currentPassword = inputs.get( "current-password" );
        String newPassword = inputs.get( "new-password" );
        String newPasswordConfirmation = inputs.get( "new-password-confirmation" );
        String storedPassword = loggable.getPassword();
        String userType = loggable.getType();

        try {
            boolean isCurrentPasswordValid = hasher.validatePassword(currentPassword, storedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }



    }

    private boolean validateCurrentPassword(String currentPassword) {
        try {
            return hasher.validatePassword(currentPassword, storedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

}
