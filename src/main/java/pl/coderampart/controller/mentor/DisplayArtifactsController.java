package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayArtifactsController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayArtifactsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        List<Artifact> allArtifacts = helper.readArtifactsFromDB();
        String response = "";

        response += helper.renderHeader( httpExchange, connection );
        response += helper.render("mentor/mentorMenu");
        response += renderDisplayArtifacts(allArtifacts);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayArtifacts(List<Artifact> allArtifacts) {
        String templatePath = "templates/mentor/displayArtifacts.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);

        return template.render(model);
    }
}
