package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
    private HelperController helper;

    public CreateTeamController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += helper.render("mentor/createTeam");
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if (method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);
            createTeam(inputs);
            helper.redirectTo( "/team/create", httpExchange );
        }
    }

    public void createTeam(Map<String, String> inputs) {
        String teamName = inputs.get("team-name");
        String groupName = inputs.get("group-name");

        try {
            Group choosenGroup = groupDAO.getByName( groupName );
            Team newTeam = new Team(teamName, choosenGroup);
            teamDAO.create(newTeam);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
