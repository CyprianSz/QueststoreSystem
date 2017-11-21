package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Team;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EditTeamController implements HttpHandler {

    private Connection connection;
    private TeamDAO teamDAO;
    private HelperController helperController;

    public EditTeamController(Connection connection) {
        this.connection = connection;
        this.teamDAO = new TeamDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        List<Team> allTeams = readTeamsFromDB();

        if(method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += helperController.render("mentor/mentorMenu");
            String responseTemp = renderTeamsList(allTeams);

            if(id.length()==36){
                Team teamToChange = getTeamByID(id);
                responseTemp = renderEditTeam(teamToChange, allTeams);
            }
            response += responseTemp;
            response += helperController.render("footer");
        }

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);

            editTeam(inputs, id);

            response += helperController.render("header");
            response += helperController.render("mentor/mentorMenu");
            String responseTemp = renderTeamsList(allTeams);
            if (id.length() == 36) {

                responseTemp = renderEditTeam(getTeamByID(id), allTeams);
            }
            response += responseTemp;
            response += helperController.render("footer");
        }

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Team> readTeamsFromDB() {
        List<Team> allTeams = null;

        try {
            allTeams = teamDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return allTeams;
    }

    private Team getTeamByID(String id) {
        Team teamToChange = null;
        try {
            teamToChange = teamDAO.getByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamToChange;
    }

    private void editTeam(Map inputs, String id){
        String name = String.valueOf(inputs.get("team-name"));

        Team teamToChange = getTeamByID(id);

        if (teamToChange != null) {
            teamToChange.setName(name);
            try{
                teamDAO.update(teamToChange);
            } catch (SQLException se){
                se.printStackTrace();
            }
        }
    }
    private String renderTeamsList(List<Team> allTeams) {
        String templatePath = "templates/mentor/editTeam.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();
        model.with("allTeams", allTeams);

        return template.render(model);
    }

    private String renderEditTeam(Team team, List<Team> allTeams) {
        String templatePath = "templates/mentor/editTeam.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);
        model.with("teamName", team.getName());

        return template.render(model);
    }


}
