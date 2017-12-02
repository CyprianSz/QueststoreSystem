package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayCodecoolersController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayCodecoolersController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        List<Codecooler> allCodecoolers = helper.readCodecoolersFromDB();
        String response = "";

        response += helper.renderHeader( httpExchange, connection );
        response += helper.render("mentor/mentorMenu");
        response += renderDisplayCodecoolers(allCodecoolers);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayCodecoolers(List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/displayCodecoolers.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allCodecoolers", allCodecoolers);

        return template.render(model);
    }
}
