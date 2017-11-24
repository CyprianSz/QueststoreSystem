package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.PasswordHasher;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CreateMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;
    private PasswordHasher hasher;
    private GroupDAO groupDAO;

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.groupDAO = new GroupDAO(this.connection);
        this.helper = new HelperController(this.connection);
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "admin/adminMenu" );
            response += helper.renderWithDropdownGroups("admin/createMentor");
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);

            createMentor(inputs);
            helper.redirectTo( "/mentor/create", httpExchange );
        }
    }

    private void createMentor(Map<String, String> inputs) {
        String firstName = inputs.get("first-name");
        String lastName = inputs.get("last-name");
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        String password = inputs.get("password");
        String groupName = inputs.get("group");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        try {
            String hashedPassword = hasher.generateStrongPasswordHash( password );
            Group group = groupDAO.getByName( groupName );
            Mentor newMentor = new Mentor( firstName, lastName, dateOfBirthObject, email, hashedPassword );
            newMentor.setGroup( group );

            mentorDAO.create( newMentor );
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}