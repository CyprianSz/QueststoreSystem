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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class CreateMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helperController;
    private PasswordHasher hasher;
    private GroupDAO groupDAO;

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.groupDAO = new GroupDAO(this.connection);
        this.helperController = new HelperController();
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        response += helperController.renderHeader(httpExchange);
        response += helperController.render("admin/adminMenu");
        response += helperController.render("admin/createMentor");
        response += helperController.render("footer");

        if(method.equals("POST")) {
            System.out.println("chuj");
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);
            formDataForMentorCreation(inputs, httpExchange);
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void formDataForMentorCreation(Map inputs, HttpExchange httpExchange) throws IOException {

        try {
            checkRegex(String.valueOf(inputs.get("date-of-birth")),String.valueOf(inputs.get("email")), httpExchange);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            String hashedPassword = hasher.generateStrongPasswordHash(String.valueOf(inputs.get("password")));

            String[] data = new String[]{String.valueOf(inputs.get("first-name")),
                                         String.valueOf(inputs.get("last-name")),
                                         String.valueOf(inputs.get("date-of-birth")),
                                         hashedPassword,
                                         String.valueOf(inputs.get("group"))};
            createMentor(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void createMentor(String[] mentorData) throws SQLException {
        ArrayList<Group> allGroups = this.groupDAO.readAll();
        ArrayList<String> groupsNames = new ArrayList<>();

        String chosenGroupName;
        String firstName = mentorData[0];
        String lastName = mentorData[1];
        String dateOfBirth = mentorData[2];
        String password = mentorData[3];
        String email = mentorData[4];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);


        Mentor newMentor = new Mentor(firstName, lastName, date, password, email);
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
        this.mentorDAO.create(newMentor);
    }

    public void checkRegex(String date, String email, HttpExchange httpExchange) throws IOException {

        String dateRegEx = "^[12][09]\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        String emailRegEx = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
                + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        if(!date.matches(dateRegEx)){
            throw new IllegalArgumentException("chujnia");
//            httpExchange.getResponseHeaders().set( "Location", "/create-mentor");
//            httpExchange.sendResponseHeaders( 302, -1 );
        }
    }
}