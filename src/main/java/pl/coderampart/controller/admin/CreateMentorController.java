package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateMentorController implements HttpHandler{

    private Connection connection;
    private MentorDAO mentorDAO;

    public CreateMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

            response += render("header");
            response += render("admin/adminMenu");
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/createMentor.twig");
            JtwigModel model = JtwigModel.newModel();
            response += template.render(model);
            response += render("footer");
//        if(method.equals("GET")) {
//        }
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = HelperController.parseFormData(formData);

            String[] data = new String[]{String.valueOf(inputs.get("first-name")),
                                        String.valueOf(inputs.get("last-name")),
                                        String.valueOf(inputs.get("date-of-birth")),
                                        String.valueOf(inputs.get("email")),
                                        String.valueOf(inputs.get("password")),
                                        String.valueOf(inputs.get("group"))};

            try{

            createMentor(data);
            }catch (SQLException se){
                se.printStackTrace();
            }

        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void createMentor(String[] mentorData) throws SQLException {

        MentorDAO mentorDAO = new MentorDAO(connection);
        GroupDAO groupDAO = new GroupDAO(connection);

        String chosenGroupName;

        ArrayList<Group> allGroups = groupDAO.readAll();
        ArrayList<String> groupsNames = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        LocalDate date = LocalDate.parse(mentorData[2], formatter);

        Mentor newMentor = new Mentor(mentorData[0], mentorData[1], date,
                mentorData[3], mentorData[4]);

        for (Group group: allGroups){
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

    private String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }
}
