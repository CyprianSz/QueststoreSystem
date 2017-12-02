package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Team;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplayTeamsController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helper;

    public DisplayTeamsController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        List<Team> allTeams = helper.readTeamsFromDB();
        String response = "";

        response += helper.renderHeader( httpExchange, connection );
        response += helper.render("mentor/mentorMenu");
        response += renderDisplayTeams(allTeams);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayTeams(List<Team> allTeams) {
        String templatePath = "templates/mentor/displayTeams.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        try {
            List<List<Codecooler>> codecoolersGroupedIntoTeams = groupCodecoolersIntoTeams(allTeams);
            model.with("groupedCodecoolers", codecoolersGroupedIntoTeams);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return template.render(model);
    }

    private List<List<Codecooler>> groupCodecoolersIntoTeams (List<Team> allTeams) throws SQLException {
        List<List<Codecooler>> groupedCodecoolers = new ArrayList<>();

        for (Team team : allTeams) {
            List<Codecooler> teamMembers = codecoolerDAO.getByTeamID( team.getID() );
            groupedCodecoolers.add(teamMembers);
        }
        return groupedCodecoolers;
    }
}
