package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.*;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Session;
import pl.coderampart.services.Loggable;

import java.io.*;
import java.net.HttpCookie;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Login implements HttpHandler{

    private Connection connection;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;
    private HelperController helper;
    private PasswordHasher hasher;

    public Login(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(this.connection);
        this.sessionDAO = new SessionDAO(this.connection);
        this.helper = new HelperController();
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if(method.equals("GET")) {
            handleGetMethod(httpExchange);
        }

        if(method.equals("POST")) {
            handlePostMethod( httpExchange );
        }
    }

    private void handleGetMethod(HttpExchange httpExchange) throws IOException {
        String response = "";
        Map<String, String> cookiesMap = helper.createCookiesMap(httpExchange);

        if (cookiesMap.containsKey( "sessionID" )) {
            String sessionID = cookiesMap.get( "sessionID" );
            response += renderProperResponse( sessionID, httpExchange );
        } else {
            response += helper.render( "login" );
        }
        helper.sendResponse(response, httpExchange);
    }

    private String renderProperResponse(String sessionID, HttpExchange httpExchange) throws IOException {
        try {
            Session currentSession = sessionDAO.getByID( sessionID );

            if (currentSession == null) {
                return helper.render( "login" );
            } else {
                String userType = currentSession.getUserType();

                switch (userType) {
                    case "Admin":
                        System.out.println("DUPSKO");
                        redirectTo( "/create-mentor", httpExchange );
                        break;
                    case "Mentor":
//                    redirectTo("/login", httpExchange);
                        break;
                    case "Codecooler":
                        redirectTo( "/display-wallet", httpExchange );
                        break;
                }
                return null;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handlePostMethod(HttpExchange httpExchange) throws IOException {
        Map<String, String> inputs = helper.getInputsMap(httpExchange);
        Loggable loggedUser = getLoggedUserFromInputs(inputs);

        if (loggedUser != null) {
            Session newSession = createNewSession( loggedUser );
            createCookieWithSessionID( newSession, httpExchange );
            addSessionToDatabase(newSession);
        }
        redirectTo("/login", httpExchange);
    }

    private Loggable getLoggedUserFromInputs(Map<String, String> inputs) {
        String userType = String.valueOf( inputs.get("user-type") );
        String email = String.valueOf( inputs.get("email") );
        String password = String.valueOf( inputs.get("password") );

        try {
            String storedPassword = userDAO.getUserHashedPassword( userType, email );
            boolean isPasswordValid = hasher.validatePassword( password, storedPassword );

            if (isPasswordValid) {
                return userDAO.getLoggedUser( userType, email );
            }
            return null;
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Session createNewSession(Loggable loggedUser) {
        String userID = loggedUser.getID();
        String userFirstName = loggedUser.getFirstName();
        String userLastName = loggedUser.getLastName();
        String userEmail = loggedUser.getEmail();
        String userType = loggedUser.getType();

        return new Session(userID, userFirstName, userLastName, userEmail, userType);
    }

    private void createCookieWithSessionID(Session session, HttpExchange httpExchange) {
        HttpCookie cookie = new HttpCookie("sessionID", session.getID());
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    private void addSessionToDatabase(Session session) {
        try {
            sessionDAO.create( session );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void redirectTo(String path, HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set( "Location", path);
        httpExchange.sendResponseHeaders( 302, -1 );
    }
}