package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.PasswordHasher;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.controller.helpers.Validator;
import pl.coderampart.model.Group;
import pl.coderampart.model.Mentor;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CreateMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;
    private PasswordHasher hasher;
    private GroupDAO groupDAO;
    private Validator validator;

    private static Map<String, String> inputs = new HashMap<>();

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.groupDAO = new GroupDAO(this.connection);
        this.helper = new HelperController(connection);
        this.mentorDAO = new MentorDAO(connection);
        this.groupDAO = new GroupDAO(connection);
        this.hasher = new PasswordHasher();
        this.validator = new Validator(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
            Mentor newMentor = new Mentor( firstName, lastName, dateOfBirthObject, email, hashedPassword );
            newMentor.setGroup( group );

            mentorDAO.create( newMentor );
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    private String renderCreateMentor(Map<String, String> inputs) throws IOException {
        if (inputs.isEmpty()) {
            return helper.render("admin/createMentor");
        }
        return checkIfCreateMentor(inputs);
    }


    private String checkIfCreateMentor(Map<String, String> inputs) throws IOException {
        if (this.validator.validateData(inputs) == true) {
            createMentor(inputs);
            return helper.render("admin/createMentor");
        }
        return renderCreateWithMessages(inputs);
    }


    private String renderCreateWithMessages(Map<String, String> inputs) throws IOException {
        String templatePath = "templates/admin/templatesWithValidations/createMentorWithValidation.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("dateOfBirth", this.validator.checkDateRegex(inputs.get("date-of-birth")));
        model.with("email", this.validator.checkEmailRegex(inputs.get("email")));
        model.with("firstName", inputs.get("first-name"));
        model.with("lastName", inputs.get("last-name"));
        model.with("password", inputs.get("password"));
        model.with("groupName", this.validator.checkGroup(inputs.get("group")));
        return template.render(model);
    }
}