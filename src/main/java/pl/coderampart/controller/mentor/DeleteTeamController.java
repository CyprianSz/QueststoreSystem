package pl.coderampart.controller.mentor;

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

public class DeleteTeamController implements HttpHandler {

    private Connection connection;
    private TeamDAO teamDAO;
    private HelperController helper;

    public DeleteTeamController(Connection connection) {
        this.connection = connection;
        this.teamDAO = new TeamDAO(this.connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Team> allTeams = helper.readTeamsFromDB();
        String teamID = helper.getIdFromURI( httpExchange );
        Team team = helper.getTeamById(teamID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(teamID, allTeams);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if(method.equals("POST")){
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")){
                deleteTeam(team);
            }
            helper.redirectTo( "/team/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String teamID, List<Team> allTeams) {
        Integer idLength = 36;
        if(teamID.length() == idLength) {
            Team teamToDelete = helper.getTeamById(teamID);
            return renderConfirmation(teamToDelete, allTeams);
        } else {
            return renderTeamsList(allTeams);
        }
    }

    private String renderConfirmation(Team team, List<Team> allTeams) {
        String templatePath = "templates/mentor/deleteChosenTeam.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);
        model.with("name", team.getName());

        return template.render( model );
    }

    private String renderTeamsList(List<Team> allTeams) {
        String templatePath = "templates/mentor/deleteTeamStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);

        return template.render(model);
    }

    private void deleteTeam(Team team) {
        try {
            teamDAO.delete( team );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
