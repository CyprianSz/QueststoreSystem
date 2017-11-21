package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;
import pl.coderampart.model.Team;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteTeamController implements HttpHandler {

    private Connection connection;
    private TeamDAO teamDAO;
    private HelperController helperController;

    public DeleteTeamController(Connection connection) {
        this.connection = connection;
        this.teamDAO = new TeamDAO(this.connection);
        this.helperController = new HelperController();
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        List<Team> allTeams = readTeamsFromDB();

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        if (method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += helperController.render("mentor/mentorMenu");
            response += renderTeamsList(allTeams);
            response += helperController.render("footer");

            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = helperController.parseFormData(formData);

            if(inputs.get("confirmation").equals("yes")){
                deleteTeam(id);
            }

            httpExchange.getResponseHeaders().set("Location", "/team/delete");
            httpExchange.sendResponseHeaders(302, -1);
        }
    }

    private List<Team> readTeamsFromDB() {
        List<Team> allTeams = null;

        try {
            allTeams = teamDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allTeams;
    }

    private String renderTeamsList(List<Team> allTeams) {
        String templatePath = "templates/mentor/deleteTeam.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);

        return template.render(model);
    }

    private void deleteTeam(String id) {
        try {
            Team teamToDelete = teamDAO.getByID(id);
            teamDAO.delete( teamToDelete );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
