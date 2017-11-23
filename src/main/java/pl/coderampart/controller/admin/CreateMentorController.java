package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;
    private PasswordHasher hasher;
    private GroupDAO groupDAO;

    private static Map<String, String> inputs = new HashMap<>();

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.groupDAO = new GroupDAO(this.connection);
        this.helper = new HelperController(connection);
        this.mentorDAO = new MentorDAO(connection);
        this.groupDAO = new GroupDAO(connection);
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(inputs);
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "admin/adminMenu" );
            response += renderCreateMentor(inputs);
            response += helper.render( "footer" );
            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            inputs = helper.getInputsMap(httpExchange);
            helper.redirectTo( "/mentor/create", httpExchange );
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private void createMentor(Map<String, String> inputs) throws IOException {
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
            Mentor newMentor = new Mentor( firstName, lastName, dateOfBirthObject, email, hashedPassword);
            newMentor.setGroup( group );

            mentorDAO.create( newMentor );
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    public String renderCreateMentor(Map<String, String> inputs) throws IOException {
        if (!inputs.isEmpty() || inputs == null) {
            return checkIfCreateMentor(inputs);
        }
        return helper.render("admin/createMentor");
    }


    public String checkIfCreateMentor(Map<String, String> inputs) throws IOException {
        if (validateData(inputs) == true) {
            createMentor(inputs);
        }
        return renderCreateWithMessages(inputs);
    }


    public boolean validateData(Map<String, String> inputs) throws IOException {
        String firstName = inputs.get("first-name");
        String lastName = inputs.get("last-name");
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        String groupName = inputs.get("group");

        return helper.checkDateRegex(dateOfBirth).equals(dateOfBirth)
                && helper.checkEmailRegex(email).equals(email)
                && checkGroup(groupName).equals(groupName)
                && helper.checkIfEmpty(inputs);
    }

    public String renderCreateWithMessages(Map<String, String> inputs) throws IOException {
        String templatePath = "templates/admin/createMentorWithExceptions.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("dateOfBirth", helper.checkDateRegex(inputs.get("date-of-birth")));
        model.with("email", helper.checkEmailRegex(inputs.get("email")));
        model.with("firstName", "firstName");
        model.with("lastName", "last name");
        model.with("password", "password");
        model.with("groupName", checkGroup(inputs.get("group")));
        return template.render(model);
    }

    public String checkGroup(String groupName) {
        if( helper.getGroupByName(groupName) == null ) {
            return "Group doesn't exist";
        }
        return helper.getGroupByName(groupName).getName();
    }
}