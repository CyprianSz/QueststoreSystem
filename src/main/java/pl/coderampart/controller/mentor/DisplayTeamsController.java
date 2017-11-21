package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Team;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayTeamsController implements HttpHandler {

    private Connection connection;
    private TeamDAO teamDAO;
    private HelperController helperController;

    public DisplayTeamsController(Connection connection) {
        this.connection = connection;
        this.teamDAO = new TeamDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Team> allTeams = readTeamsFromDB();
        String response = "";
        response += helperController.renderHeader( httpExchange );
        response += helperController.render("mentor/mentorMenu");
        response += renderDisplayTeams(allTeams);
        response += helperController.render("footer");

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Team> readTeamsFromDB() {
        List<Team> allTeams = null;

        try {
            allTeams = teamDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  allTeams;
    }

    private String renderDisplayTeams(List<Team> allTeams) {
        String templatePath = "templates/mentor/displayTeams.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);

        return template.render(model);
    }
}
