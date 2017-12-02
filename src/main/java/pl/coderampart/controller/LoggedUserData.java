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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class LoggedUserData implements HttpHandler {

    private Connection connection;
    private AdminDAO adminDAO;
    private CodecoolerDAO codecoolerDAO;
    private MentorDAO mentorDAO;
    private HelperController helper;

    public LoggedUserData(Connection connection) {
        this.connection = connection;
        this.adminDAO = new AdminDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.mentorDAO = new MentorDAO( connection );
        this.helper = new HelperController( connection );
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
            response += renderLoggedUserData(loggedUser);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            editLoggedUser(inputs, loggedUser);
            helper.redirectTo( "/account", httpExchange );
        }
    }

    private String renderLoggedUserData(Loggable loggedUser) {
        String templatePath = "templates/loggedUserAccount.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("firstName", loggedUser.getFirstName());
        model.with("lastName", loggedUser.getLastName());
        model.with("email", loggedUser.getEmail());
        model.with("dateOfBirth", loggedUser.getDateOfBirth());

        return template.render(model);
    }

    private void editLoggedUser(Map<String, String> inputs, Loggable loggedUser) {
        String firstName = inputs.get("first-name");
        String lastName  = inputs.get("last-name");
        String email = inputs.get("email");
        String birthdate = inputs.get("birthdate");
        LocalDate dateOfBirthObject = LocalDate.parse( birthdate );
        String loggedUserType = loggedUser.getType();
        String loggedUserID = loggedUser.getID();

        try {
            switch (loggedUserType) {
                case "Admin":
                    Admin admin = adminDAO.getByID( loggedUserID );
                    admin.setFirstName( firstName );
                    admin.setLastName( lastName );
                    admin.setEmail( email );
                    admin.setDateOfBirth( dateOfBirthObject );
                    adminDAO.update( admin );
                    break;
                case "Mentor":
                    Mentor mentor = mentorDAO.getByID( loggedUserID );
                    mentor.setFirstName( firstName );
                    mentor.setLastName( lastName );
                    mentor.setEmail( email );
                    mentor.setDateOfBirth( dateOfBirthObject );
                    mentorDAO.update( mentor );
                    break;
                case "Codecooler":
                    Codecooler codecooler = codecoolerDAO.getByID( loggedUserID );
                    codecooler.setFirstName( firstName );
                    codecooler.setLastName( lastName );
                    codecooler.setEmail( email );
                    codecooler.setDateOfBirth( dateOfBirthObject );
                    codecoolerDAO.update( codecooler );
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
