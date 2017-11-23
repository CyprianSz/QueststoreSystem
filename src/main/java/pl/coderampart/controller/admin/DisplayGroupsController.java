package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayGroupsController implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayGroupsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Group> allGroups = helper.readGroupsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("admin/adminMenu");
        response += renderDisplayGroups(allGroups);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayGroups(List<Group> allGroups) {
        String templatePath = "templates/admin/displayGroups.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);

        return template.render(model);
    }
}
