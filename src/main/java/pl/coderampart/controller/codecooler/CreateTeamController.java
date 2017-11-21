package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;
import pl.coderampart.model.Team;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateTeamController implements HttpHandler {

    private Connection connection;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private HelperController helperController;

    public CreateTeamController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += helperController.render("mentor/mentorMenu");
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/createTeam.twig");
            JtwigModel model = JtwigModel.newModel();
            response += template.render(model);
            response += helperController.render("footer");
        }

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);
            String teamName = String.valueOf(inputs.get("team-name"));
            String groupName = String.valueOf(inputs.get("group-name"));

            try {
                String[] data = new String[]{teamName, groupName};
                createTeam(data);
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void createTeam(String[] teamData) throws SQLException {
        String teamName = teamData[0];
        String groupName = teamData[1];

        Group choosenGroup = groupDAO.getByName( groupName );
        Team newTeam = new Team(teamName, choosenGroup);

        teamDAO.create(newTeam);
    }
}
