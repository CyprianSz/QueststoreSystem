package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.UserDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Session;
import pl.coderampart.services.Loggable;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ChangePassword implements HttpHandler {

    private Connection connection;
    private UserDAO userDAO;
    private PasswordHasher hasher;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public ChangePassword(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO( connection );
        this.hasher = new PasswordHasher();
        this.helper = new HelperController( connection );
        this.flashNoteHelper = new FlashNoteHelper();
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
            changePassword(inputs, loggedUser, httpExchange);
            helper.redirectTo( "/change-password", httpExchange );
        }
    }

    private void changePassword(Map<String, String> inputs, Loggable loggedUser, HttpExchange httpExchange) {
        String currentPassword = inputs.get( "current-password" );
        String newPassword = inputs.get( "new-password" );
        String newPasswordConfirmation = inputs.get( "new-password-confirmation" );

        try {
            boolean isCurrentPasswordValid = validateCurrentPassword( currentPassword, loggedUser );
            boolean isPasswordConfirmedProperly = newPassword.equals( newPasswordConfirmation );

            if (isCurrentPasswordValid && isPasswordConfirmedProperly) {
                String hashedPassword = hasher.generateStrongPasswordHash( newPassword );
                userDAO.updateUserPassword(loggedUser, hashedPassword);
                String flashNote = "PASSWORD CHANGED SUCCESSFULLY";
                flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
            } else {
                flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateCurrentPassword(String currentPassword, Loggable loggable)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        String storedPassword = loggable.getPassword();
        return hasher.validatePassword(currentPassword, storedPassword);
    }
}