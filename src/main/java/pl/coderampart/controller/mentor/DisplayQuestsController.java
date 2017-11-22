package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Quest;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayQuestsController implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayQuestsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Quest> allQuests = helper.readQuestsFromDB();
        String response = "";

        response += helper.renderHeader( httpExchange, connection );
        response += helper.render("mentor/mentorMenu");
        response += renderDisplayCodecoolers(allQuests);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayCodecoolers(List<Quest> allQuests) {
        String templatePath = "templates/mentor/displayQuests.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuests);

        return template.render(model);
    }
}
