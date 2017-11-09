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

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Login implements HttpHandler{

    private Connection connection;
    private AdminDAO adminDAO;
    private MentorDAO mentorDAO;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helperController;

    public Login(Connection connection) {
        this.connection = connection;
        this.adminDAO = new AdminDAO(this.connection);
        this.mentorDAO = new MentorDAO(this.connection);
        this.codecoolerDAO = new CodecoolerDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        createCookieSessionId( httpExchange );

        if(method.equals("GET")) {
            response += helperController.render("login");
        }

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader( httpExchange.getRequestBody(), "utf-8" );
            BufferedReader br = new BufferedReader( isr );
            String formData = br.readLine();

            Map inputs = helperController.parseFormData( formData );

            String userType = String.valueOf( inputs.get("user-type") );
            String email = String.valueOf( inputs.get("email") );
            String password = String.valueOf( inputs.get("password") );

            boolean isLogged;

            if (userType.equals( "admin" )) {
                isLogged = loginAsAdmin(httpExchange, email, password);
                redirectIf(isLogged, "/create-mentor", "/login", httpExchange);
            } else if (userType.equals( "mentor" )) {
                isLogged = loginAsMentor(httpExchange, email, password);
            } else if (userType.equals( "codecooler" )) {
                isLogged = loginAsCodecooler(httpExchange, email, password);
            }
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void redirectIf(boolean condition,
                            String successDestination,
                            String failureDestination,
                            HttpExchange httpExchange) throws IOException {
        if (condition) {
            httpExchange.getResponseHeaders().set( "Location", successDestination);
        } else {
            httpExchange.getResponseHeaders().set( "Location", failureDestination );
        }
        httpExchange.sendResponseHeaders( 301, -1 );
    }


    private boolean loginAsAdmin(HttpExchange httpExchange, String email, String password) {
        try {
            Admin loggedUser = adminDAO.getLogged( email, password );

            if (loggedUser != null) {
                HttpCookie userId = new HttpCookie( "userId", loggedUser.getID() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", userId.toString() );
                HttpCookie typeOfUser = new HttpCookie( "typeOfUser", "Admin" );
                httpExchange.getResponseHeaders().add( "Set-Cookie", typeOfUser.toString() );
                HttpCookie firstName = new HttpCookie( "firstName", loggedUser.getFirstName() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", firstName.toString() );
                HttpCookie lastName = new HttpCookie( "lastName", loggedUser.getLastName());
                httpExchange.getResponseHeaders().add( "Set-Cookie", lastName.toString() );

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean loginAsMentor (HttpExchange httpExchange, String email, String password) {
        try {
            Mentor loggedUser = mentorDAO.getLogged( email, password );

            if (loggedUser != null) {
                HttpCookie userId = new HttpCookie( "userId", loggedUser.getID() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", userId.toString() );
                HttpCookie typeOfUser = new HttpCookie( "typeOfUser", "Mentor" );
                httpExchange.getResponseHeaders().add( "Set-Cookie", typeOfUser.toString() );
                HttpCookie firstName = new HttpCookie( "firstName", loggedUser.getFirstName() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", firstName.toString() );
                HttpCookie lastName = new HttpCookie( "lastName", loggedUser.getLastName());
                httpExchange.getResponseHeaders().add( "Set-Cookie", lastName.toString() );

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean loginAsCodecooler (HttpExchange httpExchange, String email, String password) {
        try {
            Codecooler loggedUser = codecoolerDAO.getLogged( email, password );
            if (loggedUser != null) {
                HttpCookie userId = new HttpCookie( "userId", loggedUser.getID() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", userId.toString() );
                HttpCookie typeOfUser = new HttpCookie( "typeOfUser", "Codecooler" );
                httpExchange.getResponseHeaders().add( "Set-Cookie", typeOfUser.toString() );
                HttpCookie firstName = new HttpCookie( "firstName", loggedUser.getFirstName() );
                httpExchange.getResponseHeaders().add( "Set-Cookie", firstName.toString() );
                HttpCookie lastName = new HttpCookie( "lastName", loggedUser.getLastName());
                httpExchange.getResponseHeaders().add( "Set-Cookie", lastName.toString() );

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createCookieSessionId(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;

        if (cookieStr == null) {
            cookie = new HttpCookie("sessionId", UUIDController.createUUID());
            httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        } else {
            HttpCookie.parse(cookieStr).get(0);
        }
    }
}