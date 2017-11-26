package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


public class PasswordRecovery implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private PasswordHasher hasher;
    private FlashNoteHelper flashNoteHelper;
    private MailSender mailSender;
    private UserDAO userDAO;

    public PasswordRecovery(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController( connection );
        this.hasher = new PasswordHasher();
        this.flashNoteHelper = new FlashNoteHelper();
        this.mailSender = new MailSender();
        this.userDAO = new UserDAO( connection );
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            String response = "";
            response += helper.render("passwordRecovery");
            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);

            sendResetPasswordEmail(inputs, httpExchange);
            helper.redirectTo( "/login", httpExchange );
            helper.redirectTo( "/password-recovery", httpExchange );
        }
    }

    private void sendResetPasswordEmail(Map<String, String> inputs, HttpExchange httpExchange) {
        String userType = inputs.get( "user-type" );
        String email = inputs.get( "email" );

        try {
            boolean userExists = userDAO.checkIfUserExists(userType, email);

            if (userExists) {
                String newPassword = helper.generateRandomPassword();
                String hashedPassword = hasher.generateStrongPasswordHash( newPassword );

                userDAO.updateUserPassword( email, userType, hashedPassword );

                String resetPasswordMessage = mailSender.prepareResetPasswordMessage( newPassword );
                mailSender.send( email, resetPasswordMessage );

                String flashNote = "A NEW PASSWORD HAS BEEN SENT TO YOUR EMAIL ADDRESS";
                flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
                helper.redirectTo( "/login", httpExchange );
            } else {
                flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
                helper.redirectTo( "/password-recovery", httpExchange );
            }
        } catch (SQLException | IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
