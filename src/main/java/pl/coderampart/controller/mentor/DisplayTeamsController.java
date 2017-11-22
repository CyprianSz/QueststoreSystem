package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Team;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayTeamsController implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayTeamsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Team> allTeams = helper.readTeamsFromDB();
        String response = "";

        response += helper.renderHeader( httpExchange );
        response += helper.render("mentor/mentorMenu");
        response += renderDisplayTeams(allTeams);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayTeams(List<Team> allTeams) {
        String templatePath = "templates/mentor/displayTeams.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allTeams", allTeams);

        return template.render(model);
    }
}
