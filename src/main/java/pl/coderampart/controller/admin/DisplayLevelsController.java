package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayLevelsController extends AccessValidator implements HttpHandler{

    private Connection connection;
    private HelperController helper;

    public DisplayLevelsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        List<Level> allLevels = helper.readLevelsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("admin/adminMenu");
        response += renderDisplayLevels(allLevels);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayLevels(List<Level> allLevels) {
        String templatePath = "templates/admin/displayLevels.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);

        return template.render(model);
    }
}
