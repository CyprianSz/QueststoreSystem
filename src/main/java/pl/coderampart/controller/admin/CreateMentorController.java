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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class CreateMentorController implements HttpHandler{

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helperController;
    private PasswordHasher hasher;

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.helperController = new HelperController();
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
//        zadługi ten handler jest, rozwalić na metody (przykład w tym zadaniu z canvasa z loginem)
        String response = "";
        String method = httpExchange.getRequestMethod();

        response += helperController.renderHeader(httpExchange);
        response += helperController.render("admin/adminMenu");
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/createMentor.twig");
        JtwigModel model = JtwigModel.newModel();
        response += template.render(model);
        response += helperController.render("footer");

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);

            String firstName = String.valueOf(inputs.get("first-name"));
            String lastName = String.valueOf(inputs.get("last-name"));
            String dateOfBirth = String.valueOf(inputs.get("date-of-birth"));
            String password = String.valueOf(inputs.get("password"));
            String group = String.valueOf(inputs.get("group"));

            try {
                String hashedPassword = hasher.generateStrongPasswordHash( password );

                String[] data = new String[]{firstName, lastName, dateOfBirth, hashedPassword, group};

                createMentor(data);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException e) {
                e.printStackTrace();
            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void createMentor(String[] mentorData) throws SQLException {
//        to jest mentor dao i polach też jest. Sprawdzić czy tak to ma być ?
        MentorDAO mentorDAO = new MentorDAO(connection);
        GroupDAO groupDAO = new GroupDAO(connection);

        String chosenGroupName;

        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupsNames = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

//        pozmieniać te magic numbers na jakieś konkretnie nazwane zmienne !!!

        LocalDate date = LocalDate.parse(mentorData[2], formatter);

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], date,
                mentorData[3], mentorData[4]);

        for (Group group: allGroups) {
            String groupName = group.getName();
            groupsNames.add(groupName);
        }

        do {
            chosenGroupName = mentorData[5];
        } while (!groupsNames.contains(chosenGroupName));

        for (Group group : allGroups) {
            if (group.getName().equals(chosenGroupName)) {
                newMentor.setGroup( group );
            }
        }
        mentorDAO.create(newMentor);
    }
}